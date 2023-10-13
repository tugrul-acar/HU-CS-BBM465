import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    private static String username = "WallE";
    private static String serial_number = "1234-5678-9035";
    private static PublicKey publicKey;

    /**
     * If licence is not exist or broken then this method creates new license.
     * @param UserTuple User information.
     * @throws Exception
     */
    private static void makeLicense(String UserTuple) throws Exception {
        System.out.println("Client -- Raw License Text:" + UserTuple);
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] secretMessageBytes = UserTuple.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        System.out.println("Client -- Encrypted License Text: "+Base64.getEncoder().encodeToString(encryptedMessageBytes));
        byte[] hashed = MD5Hash(UserTuple);
        //Convert byte array into signum representation
        BigInteger no = new BigInteger(1, hashed);
        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        System.out.println("Client -- MD5 License Text: " + hashtext);
        byte[] sign = LicenseManager.LicenseVerifier(encryptedMessageBytes);
        boolean result = verifySign(sign,hashed);
        if(result){
            FileWriter myWriter = new FileWriter("src/license.txt");
            myWriter.write(Base64.getEncoder().encodeToString(sign));
            myWriter.close();
        }
        System.out.println("Client -- License is not found.");
        System.out.println("Client -- Succeed. The license file content is secured and signed by the server.");
    }


    /**
     * Verifies the given signature.
     * @param signed signature which is signed with private key.
     * @param hashed hash value of data to be signed.
     * @return result of signature verification as boolean.
     * @throws Exception
     */
    private static boolean verifySign(byte[] signed,byte[] hashed) throws Exception {
        try {
            Signature signature1 = Signature.getInstance("SHA256withRSA");
            signature1.initVerify(publicKey);
            signature1.update(hashed);
            return signature1.verify(signed);
        }
        catch (Exception e){
            return false;
        }
    }


    /**
     * MD5 Hash Algorithm
     * @param data value which will be hashed
     * @return hashed value encoded with Base64
     */
    private static byte[] MD5Hash(String data){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data.getBytes());
            return messageDigest;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Gets drive's serial number.
     * @param drive drive name.
     * @return Drive serial number.
     */
    private static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    +"Set colDrives = objFSO.Drives\n"
                    +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    +"Wscript.Echo objDrive.SerialNumber";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
    }


    /**
     * Gets motherboard serial number. Only works on Windows OS.
     * @return motherboard serial number.
     */
    private static String getWindowsMotherboard_SerialNumber() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs =
                    "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                            + "Set colItems = objWMIService.ExecQuery _ \n"
                            + "   (\"Select * from Win32_BaseBoard\") \n"
                            + "For Each objItem in colItems \n"
                            + "    Wscript.Echo objItem.SerialNumber \n"
                            + "    exit for  ' do the first cpu only! \n"
                            + "Next \n";

            fw.write(vbs);
            fw.close();

            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(Exception E){
            System.err.println("Windows MotherBoard Exp : " + E.getMessage());
        }
        return result.trim();
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Client started");

        ///////// Collects User Information //////////
        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();
        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }

        String macAddress = String.join(":", hexadecimal);
        String driveSerialNumber = getSerialNumber("C");
        String motherboardSerialNumber = getWindowsMotherboard_SerialNumber();

        System.out.println("My MAC: "+ macAddress);
        System.out.println("My Disk ID: "+ driveSerialNumber);
        System.out.println("My Motherboard ID: "+ getWindowsMotherboard_SerialNumber());
        System.out.println("LicenseManager service started...");

        String UserTuple = username + "$" + serial_number + "$" + macAddress + "$" + driveSerialNumber + "$" + motherboardSerialNumber;

        // Set public key
        File publicKeyFile = new File("src/public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory puKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = puKeyFactory.generatePublic(publicKeySpec);

        // Sets license manager's keys.
        LicenseManager.SetKeys();
        File f = new File("src/license.txt");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("Client -- License File is already exist");
            Scanner myReader = new Scanner(f);
            byte[] sign = new byte[0];
            try{
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    sign = Base64.getDecoder().decode(data);
                    // If read text is broken it may not be decoded. In this case program ignores the decode error.
                    // Later on when verifySign function called it handles the situation.
                }
            }catch (IllegalArgumentException e){
            }

            try {
                if(verifySign(sign, MD5Hash(UserTuple))){
                    System.out.println("Client -- Succeed. The license is correct.");
                } else{
                    System.out.println("Client -- The license file has been broken!!");
                    makeLicense(UserTuple);
                }
            }
            catch (Exception e){
            }
        } else{
            System.out.println("Client -- License File is not found");
            makeLicense(UserTuple);
        }
    }
}
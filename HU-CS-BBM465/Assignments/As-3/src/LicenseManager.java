import javax.crypto.Cipher;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class LicenseManager {
private static PublicKey publicKey;
private static PrivateKey privateKey;


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
     * Reads key files and sets public and private keys.
     * @throws Exception
     */
    public static void SetKeys() throws Exception {
        File publicKeyFile = new File("src/public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory puKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = puKeyFactory.generatePublic(publicKeySpec);

        File privateKeyFile = new File("src/private.key");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        KeyFactory prKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey = prKeyFactory.generatePrivate(privateKeySpec);
    }

    /**
     * Decryption with RSA algorithm.
     * @param encryptedMessageBytes message to be decrypted.
     * @return decrypted data.
     * @throws Exception
     */
    private static byte[] decrypt(byte[] encryptedMessageBytes) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, LicenseManager.privateKey);
        return decryptCipher.doFinal(encryptedMessageBytes);
    }


    /**
     * Verifies the signature.
     * @param encryptedMessageBytes data to be signed as encrypted.
     * @return signature as byte array.
     * @throws Exception
     */
    public static byte[] LicenseVerifier(byte[] encryptedMessageBytes) throws Exception {
        System.out.println("Server -- Server is being requested...");
        System.out.println("Server -- Incoming Encrypted Text: "+ Base64.getEncoder().encodeToString(encryptedMessageBytes));
        byte[] decrypted = decrypt(encryptedMessageBytes);
        System.out.println("Server -- Decrypted Text: "+ new String(decrypted));
        byte[] hashed = MD5Hash(new String(decrypted));
        //Convert byte array into signum representation
        BigInteger no = new BigInteger(1, hashed);
        //Convert hashed into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        System.out.println("Server -- MD5 Plain License Text: " + hashtext);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hashed);
        byte[] signed = signature.sign();
        System.out.println("Server -- Digital Signature: " + Base64.getEncoder().encodeToString(signed));
        return signed;
    }
}
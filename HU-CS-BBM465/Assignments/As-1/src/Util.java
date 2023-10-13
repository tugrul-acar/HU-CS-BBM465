import java.io.*;
import java.nio.charset.StandardCharsets;

public class Util {

    /**
     * Splits given string into 8 byte blocks
     * @param plain_text String text
     * @return 2D byte array
     */
    public static byte[][] split_blocks(String plain_text){
        if(plain_text.length() %8 != 0){
            int mod = plain_text.length() %8;
            for (int i = 0; i < 8 - mod; i++) {
                plain_text += " ";
            }
        }
        int block_size= plain_text.length()/8;
        byte[][] words = new byte[block_size][8];

        int count= 0;
        for (byte[] word : words) {
            byte[] xd = plain_text.substring(8*count,8*count+8).getBytes(StandardCharsets.UTF_8);
            System.arraycopy(xd, 0, word, 0, 8);
            count++;
        }
        return words;
    }
    /**
     * Splits given byte array into 8 byte blocks
     * @param plain_text_byte byte array
     * @return 2D byte array
     */
    public static byte[][] split_blocks(byte[] plain_text_byte){
        int block_size = plain_text_byte.length/8;
        byte[][] words = new byte[block_size][8];

        int count= 0;
        for (byte[] word : words) {
            byte[] xd = new byte[8];
            System.arraycopy(plain_text_byte, 8 * count, xd, 0, 8);
            System.arraycopy(xd, 0, word, 0, 8);
            count++;
        }
        return words;
    }

    /**
     * Performs XOR operation
     * @param input1 byte array
     * @param input2 byte array
     * @return xor result
     */
    public static byte[] Xor(byte[] input1,byte[] input2){

        final byte[] output = new byte[input1.length];
        for (int i = 0; i < input1.length; i++) {
            output[i] = (byte) (input1[i] ^ input2[i]);
        }
        return output;
    }

    /**
     * Concatenates nonce value and counter and yields 8 byte.
     * @param counter int value
     * @param nonce String value
     * @return Result of concatenation as byte array.
     */
    public static byte[] NonceConcatenation(int counter, String nonce) {
        String s_counter = Integer.toString(counter);
        nonce = nonce.substring(nonce.length() - 8 + s_counter.length()) + s_counter;
        return nonce.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Reverses and returns given string. Used for second key generation for 3DES algorithm.
     * @param key String value
     * @return Reversed key string
     */
    public static String ReverseKey(String key){
        char ch;
        String new_key = "";
        for (int i=0; i<key.length(); i++) {
            ch= key.charAt(i); //extracts each character
            new_key = ch + new_key; //adds each character in front of the existing string
        }
        return new_key;
    }

    /**
     * Reads the given file line by line and returns the read text as string.
     * @param file_path path of file
     * @return read text as string
     * @throws IOException
     */
    public static String Reader(String file_path) throws IOException {
        File file = new File(file_path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String text = "";
        while ((line = br.readLine()) != null) {
            text = text + line;
        }
        return text;
    }
    /**
     * Reads the given file line by line and returns the read bytes as byte array.
     * @param file_path path of file
     * @return read bytes as byte array
     * @throws IOException
     */
    public static byte[] Reader_byte(String file_path) throws IOException {
        File file = new File(file_path);
        FileInputStream fin = null;
        fin = new FileInputStream(file);
        byte fileContent[] = new byte[(int)file.length()];
        fin.read(fileContent);
        return fileContent;
    }

    /**
     * Used to split key file by " - " .
     * @param text String will be splitted.
     * @return String array
     */
    public static String[] Splitter(String text) {
        return text.split(" - ");
    }

    /**
     * Used to adjusting length of the IV, key and nonce values.
     * @param key String value
     * @return 8 byte long String
     */
    public static String sizeAdjuster(String key){
        if(key.length() > 8){
            return key.substring(0,8);
        }else if(key.length() < 8){
            for (int i = 0; i < 9-key.length(); i++) {
                key += "*";
            }
        }
            return key;
    }

    /**
     * Writes given text to given file
     * @param file_path path of file
     * @param plaintext text to be written
     * @throws IOException
     */
    public static void Write_string_file(String file_path,String plaintext)throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file_path,false));
            writer.write(plaintext);
            writer.close();
    }

    /**
     * Writes given byte array to given file
     * @param file_path path of file
     * @param text byte array to be written
     * @throws IOException
     */
    public static void Write_byte_file(String file_path,byte[] text) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file_path,false)) {
            fos.write(text);
        }
    }
}
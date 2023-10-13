import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Util {
    //64 bit hard-coded key
    static final byte[] key = ("C8x7=0zp").getBytes(StandardCharsets.UTF_8);

    /**
     * DES algorithm encryption with ECB encryption mode.
     * @param message message which will be encoded.
     * @return encrypted data as byte array
     * @throws Exception
     */
    public static byte[][] DES_enc_ECB(String message) throws Exception {
        byte[][] blocks = split_blocks(message);
        byte[][] encrypted_blocks = new byte[blocks.length][8];
        for (int i = 0; i < blocks.length; i++) {
            SecretKeySpec keySpec = new SecretKeySpec(key,"DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            encrypted_blocks[i] = cipher.doFinal(blocks[i]);
        }
        return encrypted_blocks;
    }

    /**
     * DES algorithm decryption with CBC encryption mode.
     * @param encrypted_blocks  encrypted data as byte arrays.
     * @return decrypted data as byte array
     * @throws Exception
     */
    public static byte[][] DES_dec_ECB(byte[][] encrypted_blocks) throws Exception {
        byte[][] decrypted_blocks = new byte[encrypted_blocks.length][8];
        for (int i = 0; i < encrypted_blocks.length; i++) {
            SecretKeySpec keySpec = new SecretKeySpec(key,"DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            decrypted_blocks[i] = cipher.doFinal(encrypted_blocks[i]);
        }
        return decrypted_blocks;
    }

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
            byte[] array = plain_text.substring(8*count,8*count+8).getBytes(StandardCharsets.UTF_8);
            System.arraycopy(array, 0, word, 0, 8);
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
            byte[] array = new byte[8];
            System.arraycopy(plain_text_byte, 8 * count, array, 0, 8);
            System.arraycopy(array, 0, word, 0, 8);
            count++;
        }
        return words;
    }

    /**
     * Reads the given file line by line and returns the read text as string.
     * @param file_path path of file
     * @return read text as string
     * @throws IOException
     */
    public static ArrayList<String> Reader(String file_path) throws IOException {
        File file = new File(file_path);
        file.createNewFile();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        ArrayList<String> read = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            read.add(line);
        }
        return read;
    }

    /**
     * Used to split key file by " - " .
     * @param text String will be split.
     * @return String array
     */
    public static String[] Splitter(String text) {
        return text.split(" - ");
    }

    /**
     * Writes given byte array to given file
     * @param file_path path of file
     * @param text byte array to be written
     * @throws IOException
     */
    public static void WriteMessages(String file_path,String  text) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file_path,true));
        writer.write(text);
        writer.close();
    }
}
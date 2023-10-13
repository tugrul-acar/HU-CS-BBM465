import java.util.Arrays;

public class Decyrption {
    /**
     * Calls the related DES CBC mode decryption function for each 8 byte block in blocks.
     * @param cipher encrypted data as byte array
     * @param key 8 byte key value
     * @return decrypted value as byte array
     * @throws Exception
     */
    public static byte[] dec_CBC(byte[] cipher, byte[] key) throws Exception {
        byte[] e_text_byte = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i+=8) {
            byte[] cipher_block = Arrays.copyOfRange(cipher, i, i + 8);
            byte[] d_block = DES.DES_d_CBC(cipher_block, FileCipher.IV, key);
            System.arraycopy(d_block, 0, e_text_byte, i, 8);
        }
        return e_text_byte;
    }

    /**
     * Calls the related DES OFB mode decryption function for each 8 byte block in blocks.
     * @param cipher encrypted data as byte array
     * @param key 8 byte key value
     * @return decrypted value as byte array
     * @throws Exception
     */
    public static byte[] dec_CFB(byte[] cipher, byte[] key) throws Exception {
        byte[] e_text_byte = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i+=8) {
            byte[] cipher_block = Arrays.copyOfRange(cipher, i, i + 8);
            byte[] d_block = DES.DES_e_CFB(cipher_block,FileCipher.IV,key);
            System.arraycopy(d_block, 0, e_text_byte, i, 8);
        }
        return e_text_byte;
    }

    /**
     * Calls the related DES OFB mode decryption function for each 8 byte block in blocks.
     * @param cipher encrypted data as byte array
     * @param key 8 byte key value
     * @return decrypted value as byte array
     * @throws Exception
     */
    public static byte[] dec_OFB(byte[] cipher, byte[] key) throws Exception {
        byte[] e_text_byte = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i+=8) {
            byte[] cipher_block = Arrays.copyOfRange(cipher, i, i + 8);
            byte[] d_block = DES.DES_e_OFB(cipher_block, FileCipher.IV, key);
            System.arraycopy(d_block, 0, e_text_byte, i, 8);
        }
        return e_text_byte;
    }

    /**
     * Calls the related DES CBC mode decryption function for each 8 byte block in blocks.
     * @param cipher encrypted data as byte array
     * @param key 8 byte key value
     * @param nonce 8 byte nonce value
     * @return decrypted value as byte array
     * @throws Exception
     */
    public static byte[] dec_CTR(byte[] cipher, byte[] key, String nonce) throws Exception {
        byte[] e_text_byte = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i+=8) {
            byte[] cipher_block = Arrays.copyOfRange(cipher, i, i + 8);
            byte[] d_block = DES.DES_e_CTR(cipher_block, nonce, key);
            System.arraycopy(d_block, 0, e_text_byte, i, 8);
        }
        return e_text_byte;
    }
}

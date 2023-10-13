import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DES {
    public static int counter = 0;

    /**
     * DES algorithm encryption with CBC encryption mode.
     * @param block 8 byte data which will be encrypted
     * @param IV 8 byte Initial vector
     * @param key 8 byte key
     * @return encrypted data as byte array
     * @throws Exception
     */
    public static byte[] DES_e_CBC(byte[] block,byte[] IV, byte[] key)throws Exception{
        byte[] output = Util.Xor(block, IV);
        SecretKeySpec keySpec = new SecretKeySpec(key,"DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        output = cipher.doFinal(output);
        return output;
    }

    /**
     * DES algorithm decryption with CBC encryption mode.
     * @param cipher_block 8 byte encrypted data as byte array
     * @param IV 8 byte Initial vector
     * @param key 8 byte key
     * @return decrypted data as byte array
     * @throws Exception
     */
    public static byte[] DES_d_CBC(byte[] cipher_block, byte[] IV, byte[] key)throws Exception{
        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] output = cipher.doFinal(cipher_block);
        output = Util.Xor(output, IV);
        FileCipher.IV = cipher_block;
        return output;
    }

    /**
     * DES algorithm encryption with CFB encryption mode.
     * @param block 8 byte data which will be encrypted
     * @param IV 8 byte Initial vector
     * @param key 8 byte key
     * @return encrypted data as byte array
     * @throws Exception
     */
    public static byte[] DES_e_CFB(byte[] block,byte[] IV,byte[] key)throws Exception{
        SecretKeySpec keySpec = new SecretKeySpec(key,"DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] output = cipher.doFinal(IV);
        output = Util.Xor(block, output);
        FileCipher.IV = block; //necessary for decryption
        return output;
    }

    /**
     * DES algorithm encryption with OFB encryption mode.
     * @param block 8 byte data which will be encrypted
     * @param IV 8 byte Initial vector
     * @param key 8 byte key
     * @return encrypted data as byte array
     * @throws Exception
     */
    public static byte[] DES_e_OFB(byte[] block, byte[] IV, byte[] key)throws Exception{
        SecretKeySpec keySpec = new SecretKeySpec(key,"DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        FileCipher.IV = cipher.doFinal(IV);
        return Util.Xor(block, FileCipher.IV);
    }

    /**
     *
     * @param block 8 byte data which will be encrypted
     * @param nonce 8 byte nonce value
     * @param key 8 byte key
     * @return encrypted data as byte array
     * @throws Exception
     */
    public static byte[] DES_e_CTR(byte[] block, String nonce, byte[] key)throws Exception{
        byte[] result = Util.NonceConcatenation(counter,nonce);
        SecretKeySpec keySpec = new SecretKeySpec(key,"DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] output = cipher.doFinal(result);
        output = Util.Xor(output, block);
        counter++;
        return output;
    }
}

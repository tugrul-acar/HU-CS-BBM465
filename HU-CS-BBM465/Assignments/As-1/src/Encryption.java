public class Encryption {

    /**
     * Calls the related DES CBC mode encryption function for each 8 byte block in blocks, coppies the results into given cipher array.
     * @param blocks data which will be encrypted
     * @param cipher result of encryption will be coppied to cipher
     * @param key 8 byte key value
     * @throws Exception
     */
    public static void enc_CBC(byte[][] blocks, byte[] cipher, byte[] key) throws Exception {
        for (int i = 0; i < blocks.length; i++) {
            byte[] e_block = DES.DES_e_CBC(blocks[i], FileCipher.IV, key);
            System.arraycopy(e_block, 0, cipher, i * 8, 8);
            FileCipher.IV = e_block;
        }
    }

    /**
     * Calls the related DES CFB mode encryption function for each 8 byte block in blocks, coppies the results into given cipher array.
     * @param blocks data which will be encrypted
     * @param cipher result of encryption will be coppied to cipher
     * @param key 8 byte key value
     * @throws Exception
     */
    public static void enc_CFB(byte[][] blocks, byte[] cipher, byte[] key) throws Exception {
        for (int i = 0; i < blocks.length; i++) {
            byte[] e_block = DES.DES_e_CFB(blocks[i], FileCipher.IV, key);
            System.arraycopy(e_block, 0, cipher, i * 8, 8);
            FileCipher.IV = e_block;
        }
    }

    /**
     * Calls the related DES OFB mode encryption function for each 8 byte block in blocks, coppies the results into given cipher array.
     * @param blocks data which will be encrypted
     * @param cipher result of encryption will be coppied to cipher
     * @param key 8 byte key value
     * @throws Exception
     */
    public static void enc_OFB(byte[][] blocks, byte[] cipher, byte[] key) throws Exception{
        for (int i = 0; i < blocks.length; i++) {
            byte[] e_block = DES.DES_e_OFB(blocks[i], FileCipher.IV, key);
            System.arraycopy(e_block, 0, cipher, i * 8, 8);
        }
    }

    /**
     * Calls the related DES CTR mode encryption function for each 8 byte block in blocks, coppies the results into given cipher array.
     * @param blocks data which will be encrypted
     * @param cipher result of encryption will be coppied to cipher
     * @param key 8 byte key value
     * @param nonce 8 byte nonce value
     * @throws Exception
     */
    public static void enc_CTR(byte[][] blocks, byte[] cipher, byte[] key, String nonce) throws Exception{
        for (int i = 0; i < blocks.length; i++) {
            byte[] e_block = DES.DES_e_CTR(blocks[i], nonce,key);
            System.arraycopy(e_block, 0, cipher, i * 8, 8);
        }
    }
}

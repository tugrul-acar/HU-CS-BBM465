import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class FileCipher {

    public static byte[] IV;

    public static void main(String[] args) throws Exception {
        //Creating log file
        FileHandler handler = new FileHandler("run.log", true);
        Logger logger = Logger.getLogger("alper - tugrul");
        logger.addHandler(handler);

        String operation = args[0];
        String i_path = "";

        if(args[1].equals("-i")){
            i_path = args[2];
        }
        String o_path= "";
        if(args[3].equals("-o")){
            o_path = args[4];
        }

        String key_file =  Util.Reader(args[7]);
        String[] key_file_splitted = Util.Splitter(key_file);
        for (int i = 0; i < key_file_splitted.length; i++) {
            key_file_splitted[i] = Util.sizeAdjuster(key_file_splitted[i]);
        }
        String input_text = Util.Reader(i_path);
        String algorithm = args[5];
        String op_mode = args[6];
        String s_IV = key_file_splitted[0];
        FileCipher.IV = s_IV.getBytes(StandardCharsets.UTF_8);
        String nonce = key_file_splitted[2];
        String s_key = key_file_splitted[1];
        byte[] key = (s_key).getBytes(StandardCharsets.UTF_8);
        byte[][] blocks = Util.split_blocks(input_text);
        byte[] cipher = new byte[blocks.length*8];

        if (algorithm.equals("DES")) {
            if (operation.equals("-e")) {
                if (op_mode.equals("CBC")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_CBC(blocks, cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);

                } else if (op_mode.equals("CFB")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_CFB(blocks, cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);

                } else if (op_mode.equals("OFB")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_OFB(blocks, cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);

                } else if (op_mode.equals("CTR")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_CTR(blocks, cipher, key, nonce);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);
                }

            } else if (operation.equals("-d")) {
                byte[] plain_text_byte;
                cipher = Util.Reader_byte(i_path);
                if (op_mode.equals("CBC")) {
                    long startTime = System.currentTimeMillis();
                    plain_text_byte = Decyrption.dec_CBC(cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text_byte));

                } else if (op_mode.equals("CFB")) {
                    long startTime = System.currentTimeMillis();
                    plain_text_byte = Decyrption.dec_CFB(cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text_byte));

                } else if (op_mode.equals("OFB")) {
                    long startTime = System.currentTimeMillis();
                    plain_text_byte = Decyrption.dec_OFB(cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text_byte));

                } else if (op_mode.equals("CTR")) {
                    long startTime = System.currentTimeMillis();
                    plain_text_byte = Decyrption.dec_CTR(cipher, key, nonce);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text_byte));
                }
            }

        } else if (algorithm.equals("3DES")){
            byte[] e_plain_text;
            String s_reverse_key = Util.ReverseKey(s_key);
            byte[] reverse_key = (s_reverse_key).getBytes(StandardCharsets.UTF_8);
            if (operation.equals("-e")) {
                if (op_mode.equals("CBC")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_CBC(blocks, cipher, key);
                    IV = s_IV.getBytes("UTF-8");
                    e_plain_text = Decyrption.dec_CBC(cipher, reverse_key);
                    IV = s_IV.getBytes("UTF-8");
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_CBC(e_blocks, cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);

                 } else if (op_mode.equals("CFB")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_CFB(blocks, cipher, key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    e_plain_text = Decyrption.dec_CFB(cipher, reverse_key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_CFB(e_blocks, cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);

                } else if (op_mode.equals("OFB")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_OFB(blocks, cipher, key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    e_plain_text = Decyrption.dec_OFB(cipher, reverse_key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_OFB(e_blocks, cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);

                } else if (op_mode.equals("CTR")) {
                    long startTime = System.currentTimeMillis();
                    Encryption.enc_CTR(blocks, cipher, key, nonce);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    e_plain_text = Decyrption.dec_CTR(cipher, reverse_key, nonce);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_CTR(e_blocks, cipher, key, nonce);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" enc "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_byte_file(o_path,cipher);
                }
            }

            else if (operation.equals("-d")) {
                byte[] plain_text;
                cipher = Util.Reader_byte(i_path);
                if (op_mode.equals("CBC")) {
                    long startTime = System.currentTimeMillis();
                    e_plain_text = Decyrption.dec_CBC(cipher, key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_CBC(e_blocks, cipher, reverse_key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    plain_text = Decyrption.dec_CBC(cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text));

                } else if (op_mode.equals("CFB")) {
                    long startTime = System.currentTimeMillis();
                    e_plain_text = Decyrption.dec_CFB(cipher, key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_CFB(e_blocks, cipher, reverse_key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    plain_text = Decyrption.dec_CFB(cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text));

                } else if (op_mode.equals("OFB")) {
                    long startTime = System.currentTimeMillis();
                    e_plain_text = Decyrption.dec_OFB(cipher, key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_OFB(e_blocks, cipher, reverse_key);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    plain_text = Decyrption.dec_OFB(cipher, key);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text));

                } else if (op_mode.equals("CTR")) {
                    long startTime = System.currentTimeMillis();
                    e_plain_text = Decyrption.dec_CTR(cipher, key, nonce);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    byte[][] e_blocks = Util.split_blocks(e_plain_text);
                    Encryption.enc_CTR(e_blocks, cipher, reverse_key, nonce);
                    IV = s_IV.getBytes(StandardCharsets.UTF_8);
                    plain_text = Decyrption.dec_CTR(cipher, key, nonce);
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    logger.info(i_path+" "+ o_path+" dec "+algorithm+" "+op_mode+" "+elapsedTime);
                    Util.Write_string_file(o_path,new String(plain_text));
                }
            }
        }
    }
}
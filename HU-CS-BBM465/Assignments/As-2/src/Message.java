import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class Message {
    //Stores all messages objects.
    public static ArrayList<Message> messages = new ArrayList<>();

   public String message_id;
    public String content;
    public String password;
    public User username;

    public Message(String message_id, String content, String password, User username){
        this.message_id = message_id;
        this.content = content;
        this.password = password;
        this.username = username;
    }

    /**
     * MD5 Hash Algorithm, given argument hashed then encoded as Base64.
     * @param password value which will be hashed
     * @return hashed value encoded with Base64
     */
    public static String hashpass(String password){
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytes);
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
        return Base64.getEncoder().encodeToString(md5Digest);
    }

    /**
     * Matches the given message codename as argument and finds such message object with the same codename aand returns it's index at the messages array.
     * @param id message codename
     * @return matched messages index.
     */
    public static int code_to_message(String id){
        for (int i = 0; i < messages.size(); i++) {
            if(messages.get(i).message_id.equals(id)){
                return i;
            }
        }
        return -1;
    }
}

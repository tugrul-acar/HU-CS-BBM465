import java.util.ArrayList;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws Exception {

        ArrayList<String> user_info = Util.Reader("src/users.txt");
        for (String s : user_info) {
            String[] user_array = Util.Splitter(s);
            User.users.add(new User(user_array[0],user_array[1]));
        }
        ArrayList<String> message_info = Util.Reader("src/messages.txt");
        for (String s : message_info) {
            String[] message_array = Util.Splitter(s);
            byte[] message_byte = Base64.getDecoder().decode(message_array[3]);
            byte[][] decyrpted_mes = Util.DES_dec_ECB(Util.split_blocks(message_byte));
            String dec_mes = "";
            for (byte[] decyrpted_me : decyrpted_mes) {
                dec_mes += new String(decyrpted_me);
            }
            Message.messages.add(new Message(message_array[1],dec_mes,message_array[2], User.users.get(User.Username_to_user(message_array[0]))));
        }

        Page.HP_setter();
    }
}
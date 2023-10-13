import java.util.ArrayList;

public class User {
    public static ArrayList<User> users = new ArrayList<>();
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Matches the given user's name as argument and finds such User object with the same name and returns it's index at the User array.
     * @param name message codename
     * @return matched messages index.
     */
    public static int Username_to_user(String name){
        for (int i = 0; i < User.users.size(); i++) {
            if(User.users.get(i).username.equals(name)){
                return i;
            }
        }
        return -1;
    }
}

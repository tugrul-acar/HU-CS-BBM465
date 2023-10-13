import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

public class Page {

    static JFrame HomePage = new JFrame();
    public static JFrame MessageViewPage = new JFrame();
    public static JFrame RegisterPage = new JFrame();
    public static JFrame ViewPage = new JFrame();

    public static void HP_setter(){
        HomePage.getContentPane().setLayout(new FlowLayout());
        HomePage.setLayout(null);
        HomePage.setTitle("Main Page");
        HomePage.setSize(500,400);
        HomePage.setLocation(500,200);

        JLabel label1 = new JLabel("Welcome to MessageBox", SwingConstants.CENTER);
        label1.setBounds(50,50,400,30);

        label1.setFont(new Font("Serif", Font.PLAIN, 24));

        JButton access_button = new JButton();
        access_button.setBounds(200,150,100,30);
        access_button.setText("Access");
        access_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Page.MVP_setter();
                Page.MessageViewPage.show();
                HomePage.dispose();
            }
        });

        JButton leaveMes_button = new JButton();
        leaveMes_button.setBounds(175,190,150,50);
        leaveMes_button.setText("Leave a message");

        leaveMes_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Page.R_setter();
                Page.RegisterPage.show();
                HomePage.dispose();
            }
        });
        HomePage.getContentPane().add(label1);
        HomePage.getContentPane().add(access_button);
        HomePage.getContentPane().add(leaveMes_button);

        HomePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        HomePage.setResizable(false);
        HomePage.setVisible(true);
    }


    public static void MVP_setter(){
        MessageViewPage.setTitle("Message View");
        MessageViewPage.setSize(400,500);
        MessageViewPage.setLocation(500,200);
        MessageViewPage.getContentPane().setLayout(new FlowLayout());
        MessageViewPage.setLayout(null);

        JLabel label_mc = new JLabel("Message Codename");
        label_mc.setBounds(30,50,150,30);
        JTextField text_mc = new JTextField(10);
        text_mc.setBounds(160,50,150,30);

        JLabel label_mp = new JLabel("Message Password");
        label_mp.setBounds(30,100,150,30);
        JPasswordField pass_mp = new JPasswordField(10);
        pass_mp.setBounds(160,100,150,30);

        JLabel label_un = new JLabel("Username");
        label_un.setBounds(30,150,150,30);
        JTextField text_un = new JTextField(10);
        text_un.setBounds(160,150,150,30);

        JLabel label_up = new JLabel("User Password");
        label_up.setBounds(30,200,150,30);
        JPasswordField pass_up = new JPasswordField(10);
        pass_up.setBounds(160,200,150,30);
        JCheckBox checkBox =new JCheckBox("Show password");
        checkBox.setBounds(160,230,150,30);

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBox.isSelected()){
                    pass_up.setEchoChar((char)0);
                }else {
                    pass_up.setEchoChar('\u2022');
                }
            }
        });


        JButton button_view = new JButton("VIEW");
        button_view.setBounds(50,350,100,30);
        JButton button_reset = new JButton("RESET");
        button_reset.setBounds(225,350,100,30);
        JButton button_home = new JButton("HOME");
        button_home.setBounds(150,400,100,30);

        JLabel warning = new JLabel(" ");
        warning.setBounds(40,270,250,30);

        button_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage.show();
                text_mc.setText("");
                pass_mp.setText("");
                text_un.setText("");
                pass_up.setText("");
                MessageViewPage.dispose();
            }
        });

        button_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text_mc.setText("");
                pass_mp.setText("");
                text_un.setText("");
                pass_up.setText("");
            }
        });

        button_view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codename = text_mc.getText();
                String mes_password = String.valueOf(pass_mp.getPassword());
                String username = text_un.getText();
                String user_pass = String.valueOf(pass_up.getPassword());
                if(codename.equals("")){
                    warning.setText("Message Codename cannot be empty!");
                    warning.setForeground(Color.RED);
                }else if(mes_password.equals("")){
                    warning.setText("Message Password cannot be empty!");
                    warning.setForeground(Color.RED);
                } else if (username.equals("")) {
                    warning.setText("Username cannot be empty!");
                    warning.setForeground(Color.RED);
                }
                else if (user_pass.equals("")) {
                    warning.setText("User password cannot be empty!");
                    warning.setForeground(Color.RED);
                } else if (Message.code_to_message(codename) == -1) {
                    warning.setText("there is no message id");
                    warning.setForeground(Color.RED);
                }else if (!Message.messages.get(Message.code_to_message(codename)).password.equals(Message.hashpass(mes_password))) {
                    warning.setText("Wrong message password!!");
                    warning.setForeground(Color.RED);
                } else if (!Message.messages.get(Message.code_to_message(codename)).username.getUsername().equals(username)) {
                    warning.setText("Wrong username !!");
                    warning.setForeground(Color.RED);
                } else if (!Message.messages.get(Message.code_to_message(codename)).username.getPassword().equals(Message.hashpass(user_pass))) {
                    warning.setText("Wrong username passsword!!");
                    warning.setForeground(Color.RED);
                } else {
                    int index = Message.code_to_message(codename);
                    Page.V_setter(Message.messages.get(index).content);
                    ViewPage.show();
                    MessageViewPage.dispose();
                    warning.setText(" ");
                    warning.setForeground(Color.GREEN);
                    text_mc.setText("");
                    pass_mp.setText("");
                    text_un.setText("");
                    pass_up.setText("");
                }
            }
        });

        MessageViewPage.getContentPane().add(label_mc);
        MessageViewPage.getContentPane().add(text_mc);
        MessageViewPage.getContentPane().add(label_mp);
        MessageViewPage.getContentPane().add(pass_mp);
        MessageViewPage.getContentPane().add(label_un);
        MessageViewPage.getContentPane().add(text_un);
        MessageViewPage.getContentPane().add(label_up);
        MessageViewPage.getContentPane().add(pass_up);
        MessageViewPage.getContentPane().add(checkBox);
        MessageViewPage.getContentPane().add(button_view);
        MessageViewPage.getContentPane().add(button_reset);
        MessageViewPage.getContentPane().add(button_home);
        MessageViewPage.getContentPane().add(warning);
        MessageViewPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void R_setter(){

        RegisterPage.setVisible(true);
        RegisterPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RegisterPage.setSize(700,500);
        RegisterPage.setLocation(500,200);
        RegisterPage.setTitle("Register Form");
        RegisterPage.getContentPane().setLayout(new FlowLayout());
        RegisterPage.setLayout(null);

        JLabel lbl = new JLabel("Auth. username*");
        lbl.setVisible(true);
        lbl.setBounds(30,50,150,30);
        String[] usernames = new String[User.users.size()];
        for (int i = 0; i <User.users.size(); i++) {
            usernames[i] =  User.users.get(i).getUsername();
        }
        final JComboBox<String> cb = new JComboBox<String>(usernames);
        cb.setBounds(160,50,150,30);
        cb.setVisible(true);

        JLabel pass1= new JLabel("Password*");
        pass1.setBounds(30,100,150,30);
        JPasswordField pass1_field = new JPasswordField(10);
        pass1_field.setBounds(160,100,150,30);

        JLabel pass2 = new JLabel("Confirm Password*");
        pass2.setBounds(350,100,150,30);
        JPasswordField pass2_field = new JPasswordField(10);
        pass2_field.setBounds(480,100,150,30);

        JLabel message_code_label = new JLabel("Message codename*");
        message_code_label.setBounds(30,150,150,30);
        JTextField message_code = new JTextField(10);
        message_code.setBounds(160,150,150,30);

        JLabel message_label = new JLabel("enter message*");
        message_label.setBounds(30,200,150,30);

        JTextField message = new JTextField(10);
        message.setBounds(160,200,300,100);
        message.setFont(new Font("Consolas",Font.PLAIN,15));

        JButton create_button = new JButton();
        create_button.setBounds(125,350,150,30);
        create_button.setText("Create Message");

        JButton home_button = new JButton();
        home_button.setBounds(350,350,150,30);
        home_button.setText("HOME");

        JLabel warning = new JLabel(" ");
        warning.setBounds(30,400,350,30);

        home_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage.show();
                message.setText("");
                pass1_field.setText("");
                pass2_field.setText("");
                message_code.setText("");
                RegisterPage.dispose();
            }
        });

        create_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = cb.getSelectedItem().toString();
                String password1 = String.valueOf(pass1_field.getPassword());
                String password2 = String.valueOf(pass2_field.getPassword());
                String code = message_code.getText();
                String message_content = message.getText();

                if(!password1.equals(password2)){
                    warning.setText("Passwords are not match!!");
                    warning.setForeground(Color.RED);
                } else if (password1.equals("") || password2.equals("")) {
                    warning.setText("Passwords cannot be empty!!");
                    warning.setForeground(Color.RED);
                } else if (code.equals("")) {
                    warning.setText("Message codename cannot be empty!!");
                    warning.setForeground(Color.RED);
                } else if (message_content.equals("")) {
                    warning.setText("Message cannot be empty!!");
                    warning.setForeground(Color.RED);
                } else if (password1.length() < 8) {
                    warning.setText("Password length should be at least 8 character!!");
                    warning.setForeground(Color.RED);
                } else {
                    int index = User.Username_to_user(username);
                    String hashpass = Message.hashpass(password1);
                    Message.messages.add( new Message(code, message_content, hashpass, User.users.get(index)));
                    byte[][] encrypted_message = new byte[0][];
                    try {
                        encrypted_message = Util.DES_enc_ECB(message_content);
                        byte[] full_message = new byte[encrypted_message.length*8];

                        for (int i = 0; i < encrypted_message.length; i++) {
                            System.arraycopy(encrypted_message[i],0,full_message,i*8,8);
                        }

                        Util.WriteMessages("messages.txt",User.users.get(index).getUsername() + " - " +
                                code + " - " +
                                hashpass + " - " +
                                Base64.getEncoder().encodeToString(full_message) + "\n");

                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    warning.setText("Message created successfully");
                    warning.setForeground(Color.GREEN);
                }
            }
        });

        RegisterPage.getContentPane().add(lbl);
        RegisterPage.getContentPane().add(cb);
        RegisterPage.getContentPane().add(pass1);
        RegisterPage.getContentPane().add(pass1_field);
        RegisterPage.getContentPane().add(pass2);
        RegisterPage.getContentPane().add(pass2_field);
        RegisterPage.getContentPane().add(message_code_label);
        RegisterPage.getContentPane().add(message_code);
        RegisterPage.getContentPane().add(message_label);
        RegisterPage.getContentPane().add(message);
        RegisterPage.getContentPane().add(create_button);
        RegisterPage.getContentPane().add(home_button);
        RegisterPage.getContentPane().add(warning);
        RegisterPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void V_setter(String message){

        ViewPage.setVisible(true);
        ViewPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ViewPage.setSize(400,400);
        ViewPage.setLocation(500,200);
        ViewPage.getContentPane().setLayout(new FlowLayout());
        ViewPage.setTitle("Message");
        ViewPage.setLayout(null);

        JTextField mes_field = new JTextField();
        mes_field.setText(message);
        mes_field.setEditable(false);
        mes_field.setBounds(75,50,250,200);

        JButton return_button = new JButton();
        return_button.setBounds(125,300,150,30);
        return_button.setText("Return");
        return_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage.show();
                ViewPage.dispose();
            }
        });

        JScrollPane scroll= new JScrollPane(mes_field);
        scroll.setSize( 100, 100 );
        scroll.setBounds(75,50,250,200);

        ViewPage.getContentPane().add(scroll);
        ViewPage.getContentPane().add(return_button);
    }


}

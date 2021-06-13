/*
//import java.io.Serial;
//import java.io.Serial;
import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {

    private String username;
    private String password;
    private int TOKEN_ID;

    @Serial
    private static final long serialVersionUID = -2723363051271966964L;

    public Message(String username, String password, int TOKEN_ID) {
        this.username = username;
        this.password = password;
        this.TOKEN_ID = TOKEN_ID;
    }


    public Message(String username,String password){
        this.username = username;
        this.password = password;
    }
    public Message(String username) {
        this.username = username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTOKEN_ID(int TOKEN_ID) {
        this.TOKEN_ID = TOKEN_ID;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", TOKEN_ID=" + TOKEN_ID +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getTOKEN_ID() {
        return TOKEN_ID;
    }
}
*/

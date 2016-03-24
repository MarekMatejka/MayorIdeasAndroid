package mm.mayorideas.gson;

import mm.mayorideas.objects.User;
import mm.mayorideas.security.AESEncryptor;

public class LoginDetailsResponse {

    private final int ID;
    private String username;
    private String name;

    public LoginDetailsResponse(int ID, String username, String name) {
        this.ID = ID;
        this.username = username;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public User convertToUser() {
        return new User(ID, username, name);
    }

    public void decrypt() {
        AESEncryptor aes = new AESEncryptor();
        this.username = aes.decrypt(this.username);
        this.name = aes.decrypt(this.name);
    }

    @Override
    public String toString() {
        return "LoginDetailsResponse{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

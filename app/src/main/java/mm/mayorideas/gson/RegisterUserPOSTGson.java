package mm.mayorideas.gson;

public class RegisterUserPOSTGson {

    private final String username;
    private final String password;
    private final String name;
    private final boolean isCitizen;

    public RegisterUserPOSTGson(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.isCitizen = true;
    }
}

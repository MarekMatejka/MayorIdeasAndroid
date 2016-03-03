package mm.mayorideas.gson;

public class LoginDetailsPOSTGson {

    private final String username;
    private final String password;
    private final boolean isCitizen;

    public LoginDetailsPOSTGson(String username, String password, boolean isCitizen) {
        this.username = username;
        this.password = password;
        this.isCitizen = isCitizen;
    }
}

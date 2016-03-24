package mm.mayorideas.objects;

import mm.mayorideas.security.AESEncryptor;

public class User {

    public static final String DELIMITER = ",";
    private static User currentUser;

    private final int ID;
    private final String username;
    private final String name;

    public User(int ID, String username, String name) {
        this.ID = ID;
        this.username = username;
        this.name = name;
    }

    public static void setCurrentUser(User newCurrentUser) {
        currentUser = newCurrentUser;
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

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static User getCurrentUser() {
        return currentUser != null ? currentUser : new User(-1, "", "Guest");
    }

    public static User parse(String userRecord) {
        AESEncryptor aes = new AESEncryptor();
        String[] details = userRecord.split(DELIMITER);
        return new User(
                Integer.parseInt(details[0]),
                aes.decrypt(details[1]),
                aes.decrypt(details[2]));
    }

    public static String toRecord(User user) {
        AESEncryptor aes = new AESEncryptor();
        return  user.getID() +
                DELIMITER +
                aes.encrypt(user.getUsername()) +
                DELIMITER +
                aes.encrypt(user.getName());
    }
}

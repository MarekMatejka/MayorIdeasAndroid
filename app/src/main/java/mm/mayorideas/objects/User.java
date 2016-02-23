package mm.mayorideas.objects;

public class User {

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
        return currentUser != null ? currentUser : new User(-1, "guest", "guest");
    }
}

package mm.mayorideas.api;

public final class ServerAPIHelper {

    private static final String SERVER = "http://10.193.140.181:8015/"; //TESTING
    //private static final String SERVER = "http://192.168.1.39:8015/"; //DOMA
    //private static final String SERVER = "http://10.40.202.0:8015/"; //SKOLA
    //private static final String SERVER = "http://52.49.91.43:8015/"; //AWS

    public static String getServer() {
        return SERVER;
    }
}
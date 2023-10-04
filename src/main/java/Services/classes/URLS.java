package Services.classes;

public class URLS {
    private static String authServerUrl;
    private static String dbServerUrl;
    private static String nodeUrl;

    public static String getAuthServerUrl() {
        return authServerUrl;
    }

    public static String getDbServerUrl() {
        return dbServerUrl;
    }

    public static String getNodeUrl() {
        return nodeUrl;
    }

    public static void init(){
        authServerUrl = System.getenv().getOrDefault("AUTH_SERVER", "http://127.0.0.1");
        dbServerUrl = System.getenv().getOrDefault("BD_SERVER", "http://127.0.0.1");
        nodeUrl = System.getenv().getOrDefault("NODE_WORKER", "http://127.0.0.1");
    }
}

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        URLS.init();

        Endpoint.publish("http://0.0.0.0:8080/app", );
        System.out.println(System.getenv("TMP"));
    }
}
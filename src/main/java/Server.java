import Services.Service;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        Endpoint.publish("http://0.0.0.0:8080/app", new Service());
        System.out.println(System.getenv("TMP"));
    }
}
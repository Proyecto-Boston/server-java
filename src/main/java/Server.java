import Services.Service;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1802/app", new Service());
        System.out.println("Servicio iniciado en: http://localhost:1802/app");

        Service service = new Service();

        service.login("ejemplo1@gmail.com","123456789");
    }
}
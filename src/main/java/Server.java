import soap.Service;
import model.File;
import model.User;

import javax.xml.ws.Endpoint;


public class Server {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1802/app", new Service());
        System.out.println("Servicio iniciado en: http://localhost:1802/app");

        Service service = new Service();
        User usuario = new User();
        usuario.email = "ejempl3@gmail.com";
        usuario.password = "123456789";
        usuario.name = "Ejemplo";
        usuario.surname = "DePrueba";

        service.verifySession("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF91c2VyIjoxfQ.Q17XbTiobe4HscYgctzyM3zKCq-ZkjAgQrsr4C4uyJQ");
        File file = new File();
        service.register(usuario);
    }
}
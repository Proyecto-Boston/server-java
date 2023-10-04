import Services.Service;
import Services.classes.User;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1802/app", new Service());
        System.out.println("Servicio iniciado en: http://localhost:1802/app");

        Service service = new Service();
        User usuario = new User();
        usuario.email = "ejemplo4@gmail.com";
        usuario.password = "123456789";
        usuario.name = "Ejemplo";
        usuario.surname = "DePrueba";

        //service.registerUserDB(usuario);
    }
}
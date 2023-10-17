package SOAP;

import SOAP.controller.AuthController;
import SOAP.controller.DBController;
import SOAP.interfaz.IService;
import SOAP.model.*;
import org.json.JSONObject;

import javax.jws.WebService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebService
public class Service implements IService {

    public Service(){ URLS.init(); }

    @Override
    public Response login(User user) {
        Response response = AuthController.login(user.email, user.password);

        return response;
    }

    @Override
    public Response register(User user) {
        Response response = AuthController.register(user);

        if(response.statusCode == 202){
            int responseDB = DBController.register(user.getId());

            if(responseDB!= 202){
                response.statusCode = 503;
                response.details = "El servicio actualmente no se encuentra disponible [SCALA]";
            }
        }

        return response;
    }

    @Override
    public Response verifySession(String token){
        Response response = AuthController.verifySession(token);

        return response;
    }

    @Override
    public Response createFolder(Folder folder) {

        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]

        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error de conexion.";

        String url = URLS.getDbServerUrl() + "/directory/saveDirectories";
        System.out.println(url);
        String body = "[{" +
                "\"nombre\": \"" + folder.name + "\", " +
                "\"ruta\": \""+ folder.path  + "\"," +
                "\"usuario_id\": "+ folder.userId  + "," +
                "\"nodo_id\": "+ folder.nodeId  +  "," +
                "}]";

        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : "Bad request";

            System.out.println(response.statusCode);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response uploadFile(File file) {

        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]

        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error de conexion.";

        String url = URLS.getDbServerUrl() + "/file/saveFiles";
        System.out.println(url);
        String body = "[{" +
                "\"nombre\": \"" + file.name + "\", " +
                "\"ruta\": \""+ file.path  + "\" " +
                "\"tamano\":" + file.size + "," +
                "\"usuario_id\": "+ file.userId  + ", " +
                "\"nodo_id\": "+ file.nodeId  + "," +
                "\"directorio_id\": " + file.folderId + "," +
                "}]";

        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : "Bad request";

            System.out.println(response.statusCode);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response downloadFile(File file) {

        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]

        return null;
    }

    @Override
    public Response moveFile(String routeName, int fileId) {

        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]

        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error de conexion.";

        String url = URLS.getDbServerUrl() + "/file/move";
        System.out.println(url);
        String body = "{" +
                "\"nuevaRuta\": \""+ routeName  + "\" " +
                "\"tamano\":" + fileId + "," +
                "}";

        try{
            HttpResponse<String> res = putRequest(url, body);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : resJSON.getString("message");

            System.out.println(response.statusCode);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response deleteFolder(Folder folder) {
        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]
        // TODO: Endpoint to delete folder [Waiting]

        return null;
    }

    @Override
    public Response deleteFile(File file) {
        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]

        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error de conexion.";

        String url = URLS.getDbServerUrl() + "/file/move";
        System.out.println(url);
        String body = "{" +
                "\"id\":" + file.id + "," +
                "}";

        try{
            HttpResponse<String> res = putRequest(url, body);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : resJSON.getString("message");

            System.out.println(response.statusCode);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response shareFile(int userId, int fileId) {
        // TODO: Endpoint to shareFile [Waiting]

        return null;
    }

    @Override
    public Response stopSharingFile(int fileId) {
        // TODO: Endpoint to stopSharingFile [Waiting]

        return null;
    }

    @Override
    public Response seeStorageTree(int rootFolder) {
        // TODO: Endpoint to delete folder [Waiting]

        return null;
    }

    @Override
    public Response getUserFiles(int userId){
        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error de conexion.";

        String url = URLS.getDbServerUrl() + "/file/user/" + userId;

        try{
            HttpResponse<String> res = getRequest(url);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : resJSON.getString("message");
            response.json = (response.statusCode == 200) ?  resJSON.toString() : resJSON.getString("message");

            System.out.println(response.json);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private HttpResponse<String> getRequest(String url){
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder ()
                    .uri(URI.create(url))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private HttpResponse<String> postRequest(String url, String body){
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder ()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private HttpResponse<String> putRequest(String url, String body){
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder ()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    // TODO: Gestionar a quien se le manda el archivo y a quien se le manda la replica
}

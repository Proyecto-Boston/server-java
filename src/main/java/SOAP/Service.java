package SOAP;

import SOAP.classes.*;
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
        Response response = new Response();
        response.statusCode = 400;
        response.details = "Error al procesar la solicitud";

        String url = URLS.getAuthServerUrl() + "/login";
        String body = "{\"email\": \"" + user.email + "\", \"password\": \""+ user.password  + "\" }";

        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null) return response;
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            if(response.statusCode == 202){
                //response.json = "{\"token\": \"%s\"}".formatted(resJSON.getString("token_jwt"));
                response.details = "Ingreso exitoso";
            }else if(response.statusCode == 400){
                response.details = resJSON.getString("message");
            }

            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response register(User user) {
        Response response = new Response();
        String url = URLS.getAuthServerUrl() + "/register";
        String body = "{\"email\": \"" + user.email + "\", \"password\": \""+ user.password  + "\" }";


        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null) return response;
            JSONObject resJSON = new JSONObject(res.body());


            response.statusCode = res.statusCode();
            if(response.statusCode == 201){
                response.details = "Usuario registrado exitosamente";
            }else if(response.statusCode == 400){
                response.details = resJSON.getString("message");
            }
            System.out.println(response.details);
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }
        response.statusCode = 400;
        response.details = "Error al procesar la solicitud";
        System.out.println(response.details);
        return response;
    }

    @Override
    public Response verifySession(String token) {
        Response response = new Response();
        String url = URLS.getAuthServerUrl() + "/auth";
        String body = "{\"token_jwt\": \"" + token + "\"}";


        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null) return response;
            JSONObject resJSON = new JSONObject(res.body());


            response.statusCode = res.statusCode();
            if(response.statusCode == 202){
                response.details = "Token JWT valido";
            }else if(response.statusCode == 400){
                response.details = resJSON.getString("message");
            }
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }
        response.statusCode = 400;
        response.details = "Error al procesar la solicitud";
        System.out.println(response.details);
        return response;
    }

    @Override
    public Response createFolder(Folder folder) {

        // TODO: Use RMI method in order to create the folder in the storage node

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

        // TODO: Use RMI method in order to create the folder in the storage node

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

        // TODO: Use RMI method in order to create the folder in the storage node

        return null;
    }

    @Override
    public Response moveFile(String routeName, int fileId) {

        // TODO: Use RMI method in order to create the folder in the storage node

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

        return null;
    }

    @Override
    public Response deleteFile(File file) {

        return null;
    }

    @Override
    public Response shareFile(int userID) {

        return null;
    }

    @Override
    public Response stopSharingFile() {

        return null;
    }

    @Override
    public Response seeStorageTree() {

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

    // TODO: Update
    public int registerUserDB(User user){
        Response response = new Response();
        String url = URLS.getDbServerUrl() + "/user/register";
        System.out.println(url);
        String body = "{\"nombre\": \"" + user.name + "\", \"apellido\": \""+ user.surname  + "\" }";

        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null) return 400;
            JSONObject resJSON = new JSONObject(res.body());


            int statusCode = res.statusCode();

            System.out.println(statusCode);
            return statusCode;
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(400);
        return 400;
    }
}

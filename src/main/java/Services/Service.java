package Services;

import Services.classes.*;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@WebService
public class Service  implements IService {

    public Service(){ URLS.init(); }

    @Override
    public Response login(User user) {
        Response response = new Response();
        response.statusCode = 400;
        response.details = "Error al procesar la solicitud";

        String url = URLS.getAuthServerUrl() + "/Login";
        String body = "{\"email\": \"%s\", \"password\": \"%s\"}".formatted(user.email,user.password);

        try{
            HttpResponse<String> res = postRequest(url, body);
            if(res == null) return response;
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            if(response.statusCode == 202){
                response.json = "{\"token\": \"%s\"}".formatted(resJSON.getString("token_jwt"));
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
        String url = URLS.getAuthServerUrl() + "/Register";
        String body = "{\"email\": \"%s\", \"password\": \"%s\"}".formatted(user.email, user.password);

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
        String url = URLS.getAuthServerUrl() + "/Auth";
        String body = "{\"token_jwt\": \"%s\"}".formatted(token);

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
        return null;
    }

    @Override
    public Response uploadFile(File file) {
        return null;
    }

    @Override
    public Response downloadFile(File file) {
        return null;
    }

    @Override
    public Response moveFile(String routeName) {
        return null;
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

            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            return res;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

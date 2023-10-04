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

    public Service(){
        URLS.init();
    }

    @Override
    public Response login(String email, String password) {
        Response response = new Response();
        String url = URLS.getAuthServerUrl() + "/Login";
        String body = "{\"email\": \"%s\", \"password\": \"%s\"}".formatted(email,password);

        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder ()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
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
        response.statusCode = 400;
        response.details = "Error al procesar la solicitud";
        return response;
    }

    @Override
    public Response register(User user) {
        return null;
    }

    @Override
    public Response verifySession(String token) {
        return null;
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
}

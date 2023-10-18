package controller;

import model.*;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class DBController {
    public static int register(User user, int userId){
        int responseStatus = 503;

        String url = URLS.getDbServerUrl() + "/user/register";
        String body = "{\"auth_id\": " + userId + "," +
                "\"nombre\": \"" + user.name + "\"," +
                "\"apellido\": \"" + user.surname + "\"" +
                "}";
        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null) return responseStatus;
            responseStatus = res.statusCode();
            return responseStatus;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return responseStatus;
    }

    public static Response newFolder(Folder folder){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/directory/saveDirectories";
        String body = "[{" +
                "\"nombre\": \"" + folder.name + "\", " +
                "\"ruta\": \""+ folder.path  + "\"," +
                "\"usuario_id\": "+ folder.userId  + "," +
                "\"nodo_id\": "+ folder.nodeId  +  "," +
                "\"tamano\": "+ 0  +  "," +
                "}]";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null){ return response; }

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : "Solicitud invalida.";

            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response uploadFile(File file){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/file/saveFiles";
        String body = "[{" +
                "\"nombre\": \"" + file.name + "\", " +
                "\"ruta\": \""+ file.path  + "\" " +
                "\"tamano\":" + file.size + "," +
                "\"usuario_id\": "+ file.userId  + ", " +
                "\"nodo_id\": "+ file.nodeId  + "," +
                "\"directorio_id\": " + file.folderId + "," +
                "}]";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null){ return response; }

            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : "Solicitud invalida.";

            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response changeFilePath(String routeName, int fileId){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/file/move";
        System.out.println(url);
        String body = "{" +
                "\"nuevaRuta\": \""+ routeName  + "\" " +
                "\"tamano\":" + fileId + "," +
                "}";

        try{
            HttpResponse<String> res = Request.post(url, body);
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


    public static Response deleteFile(File file){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/file/move";
        String body = "{" +
                "\"id\":" + file.id + "," +
                "}";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : resJSON.getString("message");

            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response getUserFiles(int userId){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/file/user/" + userId;

        try{
            HttpResponse<String> res = Request.get(url);
            if(res == null) return response;

            response.statusCode = res.statusCode();
            if(response.statusCode == 200){
                String fileArray = res.body();
                if(fileArray.length() > 2){
                    response.details = "Operacion exitosa.";
                    response.json = "{ \"data\": " + res.body() + "}";
                    return response;
                }
                response.details = "El usuario no tiene archivos";
            }else{
                JSONObject resJSON = new JSONObject(res.body());
                response.details = resJSON.getString("message");
            }
            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    // TODO: ?????
    // TODO: Check method arguments in Service.java file
    public static Response deleteFolder(Folder folder){
        return null;
    }

    public static Response shareFile(int userId, int fileId){
        return null;
    }

    public static Response stopSharingFile(int fileId){
        return null;
    }

    public static Response getStorageTree(int userId){
        return null;
    }
}

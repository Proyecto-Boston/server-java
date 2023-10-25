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

    public static Response getSubFolder(int folderId){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/directory/buscarSubDirectorio";

        try{
            HttpResponse<String> res = Request.post(url, ""+folderId);
            if(res == null) return response;

            response.statusCode = res.statusCode();
            if(response.statusCode == 201){
                String files = getSubFolderFiles(folderId);

                String fileArray = res.body();
                if(fileArray.length() > 2){
                    response.details = "Operacion exitosa.";

                    response.json = "{ \"folders\": " + res.body() + ","
                            + files + "}";
                    return response;
                }
                if(files != null) response.json = "{ "+ files + "}";

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

    public static String getSubFolderFiles(int folderId){
        String url = URLS.getDbServerUrl() + "/file/fileByDirectory/" + folderId;

        try{
            HttpResponse<String> res = Request.get(url);
            if(res == null) return null;

            if(res.statusCode() == 200){
                String fileArray = res.body();
                if(fileArray.length() > 2){
                    String files = "\"files\": " + res.body();
                    return files;
                }
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Response newFolder(Folder folder){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/directory/saveSubDirectories";
        String body = "[{" +
                "\"nombre\": \"" + folder.name + "\", " +
                "\"ruta\": \""+ folder.path  + "\"," +
                "\"usuario_id\": "+ folder.userId  + "," +
                "\"nodo_id\": "+ folder.nodeId  +  "," +
                "\"respaldo_id\": "+ folder.backNodeId  +  "," +
                "\"padre_id\": "+ folder.fatherId  +
                "}]";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null){ return response; }
            System.out.println(res.body());

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 201) ? "Operacion exitosa." : "Solicitud invalida.";

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

        String url = URLS.getDbServerUrl() + "/file/save";
        String body = "[{" +
                "\"nombre\": \"" + file.name + "\", " +
                "\"ruta\": \""+ file.path  + "\", " +
                "\"tamano\":" + file.size + "," +
                "\"usuario_id\": "+ file.userId  + ", " +
                "\"nodo_id\": "+ file.nodeId  + "," +
                "\"respaldo_id\": "+ file.backNodeId  + "," +
                "\"directorio_id\": " + file.folderId +
                "}]";

        try{
            HttpResponse<String> res = Request.post(url, body);
            if(res == null){ return response; }
            System.out.println(res.body());
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
                "\"id\":" + fileId + "," +
                "\"nuevaRuta\": \""+ routeName  + "\" " +
                "}";

        try{
            HttpResponse<String> res = Request.put(url, body);
            if(res == null){ return response; }
            JSONObject resJSON = new JSONObject(res.body());

            System.out.println(res.body());
            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 201) ? "Operacion exitosa." : resJSON.getString("message");

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

        String url = URLS.getDbServerUrl() + "/file/delete";
        String body = "[" +file.id+ "]";

        try{
            HttpResponse<String> res = Request.put(url, body);
            if(res == null){ return response; }

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 200) ? "Operacion exitosa." : res.body();

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
                String folders = getRootFolders(userId);


                String fileArray = res.body();
                System.out.println(res.body());
                if(fileArray.length() > 2){
                    response.details = "Operacion exitosa.";

                    response.json = "{ \"files\": " + res.body() + ","
                    + folders + "}";
                    return response;
                }
                if(folders != null) response.json = "{ "+ folders + "}";

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

    private static String getRootFolders(int userId){
        String url = URLS.getDbServerUrl() + "/directory/buscarDirectorioRoot";
        try{
            HttpResponse<String> res = Request.post(url, userId + "");
            if(res == null) return null;

            if(res.statusCode() == 201){
                String fileArray = res.body();
                if(fileArray.length() > 2){
                    String folder = "\"folders\": " + res.body();
                    return folder;
                }
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getFileById(int fileId){
        String url = URLS.getDbServerUrl() + "/file/" + fileId;

        try{
            HttpResponse<String> res = Request.get(url);
            if(res == null) return null;

            if(res.statusCode() == 200){
                JSONObject resJSON = new JSONObject(res.body());
                File file = new File();
                file.path = resJSON.getString("ruta");
                file.id = resJSON.getInt("id");
                file.name = resJSON.getString("nombre");
                file.size = resJSON.getDouble("tamano");
                file.userId = resJSON.getInt("usuario_id");
                file.nodeId = resJSON.getInt("nodo_id");
                file.folderId = resJSON.getInt("directorio_id");
                file.backNodeId = resJSON.getInt("respaldo_id");
                return new File();
            }
            return null;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Response deleteFolder(Folder folder){
        Response response = new Response();
        response.statusCode = 503;
        response.details = "El servicio actualmente no se encuentra disponible [SCALA]";

        String url = URLS.getDbServerUrl() + "/directory/delete";
        String body = "" + folder.id;

        try{
            HttpResponse<String> res = Request.put(url, body);
            if(res == null){ return response; }

            response.statusCode = res.statusCode();
            response.details = (response.statusCode == 201) ? "Operacion exitosa." : res.body();

            return response;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
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

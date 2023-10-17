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
        Response response = DBController.newFolder(folder);

        return response;
    }

    @Override
    public Response uploadFile(File file) {
        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]
        Response response = DBController.uploadFile(file);

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
        Response response = DBController.changeFilePath(routeName, fileId);

        return response;
    }

    @Override
    public Response deleteFolder(Folder folder) {
        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]
        // TODO: Endpoint to delete folder [Waiting]
        Response response = DBController.deleteFolder(folder);

        return response;
    }

    @Override
    public Response deleteFile(File file) {
        // TODO: Use RMI method in order to create the folder in the storage node [Waiting]
        Response response = DBController.deleteFile(file);

        return response;
    }

    @Override
    public Response shareFile(int userId, int fileId) {
        // TODO: Endpoint to shareFile [Waiting]
        Response response = DBController.shareFile(userId, fileId);

        return response;
    }

    @Override
    public Response stopSharingFile(int fileId) {
        // TODO: Endpoint to stopSharingFile [Waiting]
        Response response = DBController.stopSharingFile(fileId);

        return null;
    }

    @Override
    public Response seeStorageTree(int rootFolder) {
        // TODO: Endpoint to delete folder [Waiting]
        Response response = DBController.stopSharingFile(rootFolder);

        return response;
    }

    @Override
    public Response getUserFiles(int userId){
        Response response = DBController.getUserFiles(userId);

        return response;
    }
    // TODO: Gestionar a quien se le manda el archivo y a quien se le manda la replica
}

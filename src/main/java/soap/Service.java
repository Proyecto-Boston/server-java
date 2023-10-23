package soap;

import controller.AuthController;
import controller.DBController;
import controller.NodeController;
import soap.interfaz.IService;
import model.*;

import javax.jws.WebService;

@WebService
public class Service implements IService {

    private final NodeController nodeController;
    public Service(){
        URLS.init();
        nodeController= new NodeController();
    }

    @Override
    public Response login(User user) {
        Response response = AuthController.login(user.email, user.password);

        return response;
    }

    @Override
    public Response register(User user) {
        Response response = AuthController.register(user);

        if(response.statusCode == 201){
            int id = Integer.parseInt(response.json);
            System.out.println("IdUsuario: "+id);
            int responseDB = DBController.register(user, id);
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
        Response result = nodeController.createFolder(folder.userId,folder.path);

        if(result.statusCode != 200){
            return  result;
        }
        folder.nodeId = result.mainNode;
        folder.backNodeId = result.backUpNode;

        Response response = DBController.newFolder(folder);

        return response;
    }

    @Override
    public Response uploadFile(File file) {
        Response result = nodeController.uploadFile(file.name, file.userId+"/"+file.path, file.fileData);

        if(result.statusCode != 200){
            return  result;
        }
        file.nodeId = result.mainNode;
        file.backNodeId = result.backUpNode;

        Response response = DBController.uploadFile(file);

        return response;
    }

    @Override
    public Response downloadFile(File file) {
        Response nodeResponse = nodeController.downloadFile(file.userId, file.nodeId, file.backNodeId, file.path);

        return nodeResponse;
    }


    @Override
    public Response deleteFile(File file) {
        Response response = DBController.deleteFile(file);

        if(response.statusCode == 200){
            Response result = nodeController.deleteFile(file.userId, file.nodeId, file.backNodeId, file.path);

            if(result.statusCode != 200){
                response.statusCode = 500;
                response.details = "Eliminado de BD, pero no de los nodos.";
            }
        }

        return response;
    }

    @Override
    public Response moveFile(int fileId, String oldPath, String newPath ) {
        File file = DBController.getFileById(fileId);
        Response response = new Response();
        response.statusCode = 404;
        response.details = "El archivo de id " + fileId + " no existe.";

        if(file == null) return response;

        Response nodeResponse = nodeController.updateFilePath(file.userId, file.nodeId, file.backNodeId, oldPath, newPath);
        if(nodeResponse.statusCode != 200){
            response.statusCode = 500;
            response.details = "Error [Nodos]";
            return response;
        }
        response = DBController.changeFilePath(newPath, fileId);

        return response;
    }

    @Override
    public Response getUserFiles(int userId){
        Response response = DBController.getUserFiles(userId);

        return response;
    }

    // ! DB ENDPOINTS??????????????????????????????

    // ! ----------CHECK-------------
    // ? Delete in both nodes
    @Override
    public Response deleteFolder(Folder folder) {
        // TODO: Endpoint to delete folder [Waiting]
        Response response = DBController.deleteFolder(folder);

        if(response.statusCode == 200){
            boolean result = nodeController.deleteFolder(folder.path);

            if(!result){
                response.statusCode = 500;
                response.details = "Error [Nodos]";
            }
        }

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
}

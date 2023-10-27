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
            if(responseDB!= 201){
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
        Response result;
        if(folder.fatherId == 0){
            result = nodeController.createFolder(folder.userId, folder.name);
        }else{
            String checkedPath = checkPath(folder.fatherId, folder.path);
            result = nodeController.createFolder(folder.userId,checkedPath + folder.name);
        }

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

        file.path = checkPath(file.folderId, file.path);

        Response response = DBController.uploadFile(file);

        return response;
    }

    @Override
    public Response downloadFile(int fileId) {
        File file = DBController.getFileById(fileId);

        Response response = new Response();
        response.statusCode = 404;
        response.details = "El id dado no pertenece a ningun archivo.";
        if(file == null) return response;
        String checkedPath = checkPath(file.folderId, file.path);
        System.out.println(checkedPath);
        Response nodeResponse = nodeController.downloadFile(file.userId, file.nodeId, file.backNodeId, checkedPath + file.name);

        return nodeResponse;
    }


    @Override
    public Response deleteFile(int fileId) {
        File file = DBController.getFileById(fileId);

        Response responseDB = new Response();
        responseDB.statusCode = 404;
        responseDB.details = "El id dado no pertenece a ningun archivo.";

        if(file == null) return responseDB;

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
    public Response moveFile(int fileId, int newFolderId, String newPath ) { // ? int fileId
        File file = DBController.getFileById(fileId);
        Response response = new Response();
        response.statusCode = 404;
        response.details = "El archivo de id " + fileId + " no existe.";

        if(file == null) return response;

        String checkedPath = checkPath(file.folderId, file.path);
        Response nodeResponse = nodeController.updateFilePath(file.userId, file.nodeId, file.backNodeId, checkedPath + file.name, newPath + file.name);
        if(nodeResponse.statusCode != 200){
            response.statusCode = 500;
            response.details = "Error [Nodos]";
            return response;
        }
        response = DBController.changeFilePath(newPath, fileId, newFolderId);

        return response;
    }

    @Override
    public Response getUserFiles(int userId){
        Response response = DBController.getUserFiles(userId);

        return response;
    }

    @Override
    public Response getSubFolderFiles(int folderId) {
        Response response = DBController.getSubFolder(folderId);

        return response;
    }


    @Override
    public Response deleteFolder(int folderId) { // ? int fileId
        Folder folder = DBController.getFolderById(folderId);
        Response responseDB = new Response();
        responseDB.statusCode = 404;
        responseDB.details = "El archivo de id " + folderId + " no existe.";

        if(folder == null) return responseDB;
        System.out.println(folder.nodeId);
        System.out.println(folder.backNodeId);

        Response result = nodeController.deleteFolder(folder.userId, folder.nodeId, folder.backNodeId, folder.path);

        if(result.statusCode != 200){
            return  result;
        }
        folder.nodeId = result.mainNode;
        folder.backNodeId = result.backUpNode;

        Response response = DBController.deleteFolder(folder);

        return response;
    }

    @Override
    public Response getSharedFiles(int userId){
        Response response = DBController.getSharedFiles(userId);

        return response;
    }

    @Override
    public Response shareFile(String userEmail, int fileId) {
        // TODO: Endpoint to shareFile [Waiting]
        Response responseAuth = AuthController.geIdbyEmail(userEmail);

        if(responseAuth.statusCode == 500) return responseAuth;
        int userId = Integer.parseInt(responseAuth.json);

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
        String data = DBController.getRootFolders(rootFolder);
        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error con scala";

        if (data != null){
            response.statusCode = 200;
            response.details = "Proceso exitoso.";
            response.json = "{" + data + "}";
        }

        return response;
    }

    private String checkPath(int padreId, String path){
        if(padreId != 0){
            char lastChar = path.charAt(path.length() -1 );
            char secondLastChar = path.charAt(path.length() -2 );
            if(lastChar != '/' && secondLastChar != '/'){
                return path + "/";
            }else if(lastChar == '/' && secondLastChar == '/'){
                return path.substring(0, path.length() - 1);
            }

        }
        return path;
    }
}

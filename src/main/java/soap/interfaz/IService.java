package soap.interfaz;

import model.File;
import model.Folder;
import model.Response;
import model.User;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface IService {
    @WebMethod
    Response login(User user);
    @WebMethod
    Response register(User user);
    @WebMethod
    Response verifySession(String token);
    @WebMethod
    Response createFolder(Folder folder);
    @WebMethod
    // TODO: How to upload the file (Choose a way/method)
    Response uploadFile(File file);
    @WebMethod
    // TODO: What does this method returns?
    Response downloadFile(File file);
    @WebMethod
    // * Also works as rename file
    Response moveFile(int fileId, String oldPath, String newPath);
    @WebMethod
    Response deleteFolder(Folder folder);
    @WebMethod
    Response deleteFile(File file);
    @WebMethod
    // * Share file or folder
    Response shareFile(int userId, int fileId);
    @WebMethod
    // * Stop sharing file or folder
    Response stopSharingFile(int fileId);
    @WebMethod
    Response seeStorageTree(int rootFolder);
    @WebMethod
    Response getUserFiles(int userId);
    @WebMethod
    Response getSubFolderFiles(int folderId);
}

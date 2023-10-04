package Services;

import Services.classes.*;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IService {
    @WebMethod
    Response login(String email, String password);
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
    Response moveFile(String routeName);
    @WebMethod
    Response deleteFolder(Folder folder);
    @WebMethod
    Response deleteFile(File file);
    @WebMethod
    // * Share file or folder
    Response shareFile(int userID);
    @WebMethod
    // * Stop sharing file or folder
    Response stopSharingFile();
    @WebMethod
    Response seeStorageTree();
}

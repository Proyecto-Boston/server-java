package Services;

import Services.classes.File;
import Services.classes.Folder;
import Services.classes.User;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IService {
    @WebMethod
    String login(User user);
    @WebMethod
    String register(User user);
    @WebMethod
    String verifySession(String token);

    @WebMethod
    String createFolder(Folder folder);
    @WebMethod
    // TODO: How to upload the file (Choose a way/method)
    String uploadFile(File file);
    @WebMethod
    // TODO: What does this method returns?
    String downloadFile(File file);
    @WebMethod
    // * Also works as rename file
    String moveFile(String routeName);
    @WebMethod
    String deleteFolder(Folder folder);
    @WebMethod
    String deleteFile(File file);
    @WebMethod
    // * Share file or folder
    String shareFile(int userID);
    @WebMethod
    // * Stop sharing file or folder
    String stopSharingFile();
    @WebMethod
    String seeStorageTree();
}

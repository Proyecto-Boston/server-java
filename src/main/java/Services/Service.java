package Services;

import Services.classes.*;

import javax.jws.WebService;

@WebService
public class Service  implements IService {

    public Service(){
        URLS.init();
    }

    @Override
    public Response login(User user) {
        return null;
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

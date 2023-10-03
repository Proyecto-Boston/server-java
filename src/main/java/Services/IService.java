package Services;

public interface IService {
    String login(String email, String password);
    String register(String email, String password);
    String verifySession(String token);

    String createFolder(String folderName);
    // TODO: How to uploade the file (Choose a way/method)
    String uploadFile();
    // TODO: What does this method returns?
    String downloadFile();

    // Also works as rename file
    String moveFile();
    String deleteFolder();
    String shareFile();
    String storageTree();
}

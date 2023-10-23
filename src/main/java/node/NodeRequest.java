package node;

import model.Response;
import rmi.IRMIService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public class NodeRequest implements Callable<Response> {
    private int userId;
    private String fileName;
    private byte[] fileData;
    private final int request;

    private final Node node;
    private String filePath;
    private String newFilePath;
    // A request can be:
    // 1. Upload
    // 2. Download
    // 3. Change file route
    // 4. Delete file
    // 5. Create folder
    // 6. Delete folder

    // ? Constructor for uploadFile
    public NodeRequest(int request, Node node, String fileName, String filePath, byte[] fileData ){
        this.request = request;
        this.node = node;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileData = fileData;

    }

    // ? Constructor for downloadFile || deleteFile || createFolder || deleteFolder
    public NodeRequest(int request, Node node, String filePath){
        this.request = request;
        this.node = node;
        this.filePath = filePath;
    }

    // ? Constructor for updateFilePath
    public NodeRequest(int request, Node node, String filePath, String newFilePath){
        this.request = request;
        this.node = node;
        this.filePath = filePath;
        this.newFilePath = newFilePath;
    }

    private int uploadRMI(){
        try {
            IRMIService rmiClient = connectToNode(node);
            int response = rmiClient.uploadFile(fileName, filePath, fileData);

            return response;
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] downloadRMI(){
        try {
            IRMIService rmiClient = connectToNode(node);

            byte[] response = rmiClient.downloadFile(filePath);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return null;
        }
    }

    // * Or update file name
    public boolean updateFileRMI(){
        try {
            IRMIService rmiClient = connectToNode(node);

            boolean response = rmiClient.changeFilePath(filePath, newFilePath);
            System.out.println(response);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    public boolean deleteFileRMI(){
        try {
            IRMIService rmiClient = connectToNode(node);

            boolean response = rmiClient.removeFile(filePath);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    public boolean createFolderRMI(){
        try {
            IRMIService rmiClient = connectToNode(node);
            System.out.println(rmiClient);
            boolean response = rmiClient.createDirectory(filePath); // FilePath represents the folderPath
            System.out.println(response);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    public boolean deleteFolderRMI(){
        try {
            IRMIService rmiClient = connectToNode(node);

            boolean response = rmiClient.removeFolder(filePath); // FilePath represents the folderPath
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }


    private IRMIService connectToNode(Node node) throws NotBoundException {
//        Registry registry = LocateRegistry.getRegistry(node.getIp(), node.getPort());
        IRMIService rmiClient = null;
        try {
            String name = "rmi://"+node.getIp()+":"+node.getPort()+"/node";
            rmiClient = (IRMIService) Naming.lookup(name);
        } catch (RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return rmiClient;
    }

    @Override
    public Response call() throws Exception{
        Response response = new Response();
        response.statusCode = 500;
        response.details = "Error al procesar la solicitud.";

        boolean success;

        switch (request){
            case 1: // ! 1. Upload
                int result = uploadRMI();
                if(result == 200){
                    response.statusCode = result;
                    response.details = "Archivo subido exitosamente.";
                }
                break;
            case 2: // ! 2. Download
                byte[] fileData = downloadRMI();
                if(fileData != null){
                    response.statusCode = 200;
                    response.details = "Archivo descargado exitosamente.";
                    response.fileData = fileData;
                }
                break;
            case 3: // ! 3. Change file path
                success = updateFileRMI();
                if(success){
                    response.statusCode = 200;
                    response.details = "Archivo actualizado exitosamente.";
                }
                break;
            case 4: // ! 4. Delete file
                success = deleteFileRMI();
                if(success){
                    response.statusCode = 200;
                    response.details = "Archivo eliminado exitosamente.";
                }
                break;
            case 5: // ! 5. Create folder
                success = createFolderRMI();
                if(success){
                    response.statusCode = 200;
                    response.details = "Carpeta creada exitosamente.";
                }
                break;
            case 6: // ! 6. Delete folder
                success = deleteFolderRMI();
                if(success){
                    response.statusCode = 200;
                    response.details = "Carpeta eliminada exitosamente.";
                }
                break;
        }
        return response;
    }

}

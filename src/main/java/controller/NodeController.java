package controller;

import node.Node;
import node.NodeRequest;
import rmi.IRMIService;
import rmi.RMIClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.RecursiveTask;

public class NodeController {

    private List<Node> availabeNodes = new ArrayList<Node>();
    private Node node1 = new Node(1,"127.0.0.1", 1111, 100);
    private Node node2 = new Node(1,"127.0.0.1", 1111, 100);
    private Node node3 = new Node(1,"127.0.0.1", 1111, 100);
    private Queue<NodeRequest> requests = new LinkedList<>();

    public NodeController(){
        availabeNodes.add(node1);
        availabeNodes.add(node2);
        availabeNodes.add(node3);
    }

    // TODO: When a node ends a request it must be added to the list again
    // TODO: Redefine the methods (void) and think about the parameters

    private IRMIService connectToNode(Node node) throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(node.getIp(), node.getPort());
        IRMIService rmiClient = (IRMIService) registry.lookup("node");

        return rmiClient;
    }

    public String uploadFile(String fileName, String path, byte[] fileData){
        try {
            IRMIService rmiClient = connectToNode(node1);

            int response = rmiClient.uploadFile(fileName, path, fileData);

            if(response == 200){
                return "Archivo subido exitosamente";
            }
            return "Error al subir el archivo";

        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadFile(String path){
        try {
            IRMIService rmiClient = connectToNode(node1);

            byte[] response = rmiClient.downloadFile(path);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return null;
        }
    }

    // * Or update file name
    public boolean updateFilePath(String path, String newName){
        try {
            IRMIService rmiClient = connectToNode(node1);

            boolean response = rmiClient.changeFilePath(path, newName);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    public boolean deleteFile(String path){
        try {
            IRMIService rmiClient = connectToNode(node1);

            boolean response = rmiClient.removeFile(path);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    public boolean createFolder(String path){
        try {
            IRMIService rmiClient = connectToNode(node1);

            boolean response = rmiClient.createDirectory(path);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    public boolean deleteFolder(String path){
        try {
            IRMIService rmiClient = connectToNode(node1);

            boolean response = rmiClient.removeFolder(path);
            return response;
        } catch (NotBoundException | RemoteException e) {
            return false;
        }
    }

    private void checkQueue(){

    }
}

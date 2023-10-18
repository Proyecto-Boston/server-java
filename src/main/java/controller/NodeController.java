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
    private Node node1 = new Node(1,"192.168.1.200", 1099, 100);
    private Node node2 = new Node(2,"192.168.1.201", 1099, 100);
    private Node node3 = new Node(3,"192.168.1.202", 1099, 100);
    private Queue<NodeRequest> requests = new LinkedList<>();

    public NodeController(){
        availabeNodes.add(node1);
        availabeNodes.add(node2);
        availabeNodes.add(node3);
    }

    // TODO: When a node ends a request it must be added to the list again
    // TODO: Redefine the methods (void) and think about the parameters


    public String uploadFile(String fileName, String path, byte[] fileData){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return "No hay nodos disponibles";
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        Thread saveOnMain = new Thread();

        return null;
    }











    private void checkQueue(){

    }
}

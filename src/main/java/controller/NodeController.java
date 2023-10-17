package controller;

import node.Node;
import node.NodeRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public void uploadFile(){

    }

    public void downloadFile(){

    }

    public void updateFilePath(){

    }

    public void deleteFile(){

    }

    public void createFolder(){

    }

    public void deleteFolder(){

    }
}

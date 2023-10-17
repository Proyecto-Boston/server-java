package controller;

import node.Node;
import node.NodeRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NodeController {

    private List<Node> availabeNodes = new ArrayList<Node>();
    private Queue<NodeRequest> requests = new LinkedList<>();

    public NodeController(){
        availabeNodes.add(new Node(1,"127.0.0.1", 1111, 100));
        availabeNodes.add(new Node(2,"127.0.0.2", 1111, 100));
        availabeNodes.add(new Node(3,"127.0.0.3", 1111, 100));
    }

}

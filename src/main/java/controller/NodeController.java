package controller;

import model.Response;
import node.Node;
import node.NodeRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

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

    public int uploadFile(String fileName, String path, byte[] fileData){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return 500;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(1, mainNode, fileName, path, fileData);
        Callable<Response> backUp = new NodeRequest(1, backUpNode, fileName, path, fileData);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                return mainResponse.statusCode;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return 500;
    }

    public byte[] downloadFile(String path){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return null;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(2, mainNode, path);
        Callable<Response> backUp = new NodeRequest(2, mainNode, path);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                return mainResponse.fileData;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean updateFilePath(String path, String newPath){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return false;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(3, mainNode, path, newPath);
        Callable<Response> backUp = new NodeRequest(3, mainNode, path, newPath);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                return true;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean deleteFile(String path){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return false;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(4, mainNode, path);
        Callable<Response> backUp = new NodeRequest(4, mainNode, path);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                return true;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean createFolder(String path){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return false;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(5, mainNode, path);
        Callable<Response> backUp = new NodeRequest(5, mainNode, path);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                return true;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean deleteFolder(String path){
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            return false;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(6, mainNode, path);
        Callable<Response> backUp = new NodeRequest(6, mainNode, path);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                return true;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return false;
    }













    private void checkQueue(){

    }
}

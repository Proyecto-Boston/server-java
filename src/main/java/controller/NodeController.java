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

    public Response uploadFile(String fileName, String path, byte[] fileData){
        Response response = new Response();
        response.details = "Error en el servidor";
        response.statusCode = 500;
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            response.details = "Nodos ocupados";
            return response;
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

                response.statusCode = mainResponse.statusCode;
                response.details = mainResponse.details;
                response.mainNode = mainNode.getId();
                response.backUpNode = backUpNode.getId();
                return response;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public Response downloadFile(int userId, int node, int backNode, String path){
        Response response = new Response();
        response.details = "Error en el servidor";
        response.statusCode = 500;
        if(availabeNodes.isEmpty()){
            response.details = "Nodos ocupados";
            return response;
        }
        int iMainNode = searchNode(node);
        int iBackNode = searchNode(backNode);

        Node worker;
        if(iMainNode == -1 && iBackNode == -1){
            return null;
        }else if(iBackNode == -1){
            worker = availabeNodes.remove(iMainNode);
        }else{
            worker = availabeNodes.remove(iBackNode);
        }

        ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<Response> main = new NodeRequest(2, worker, userId+"/"+path);

        Future<Response> mainRequest =  pool.submit(main);

        try {
            Response mainResponse = mainRequest.get();

            if(mainResponse.statusCode == 200 ){
                availabeNodes.add(worker);

                response.statusCode = mainResponse.statusCode;
                response.details = mainResponse.details;
                response.fileData = mainResponse.fileData;
                return response;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public Response updateFilePath(int userId, int node, int backNode, String path, String newPath){
        Response response = new Response();
        response.details = "Error en el servidor";
        response.statusCode = 500;
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            response.details = "Nodos ocupados";
            return response;
        }

        int iMainNode = searchNode(node);
        int iBackNode = searchNode(backNode);

        if(iMainNode == -1 || iBackNode == -1){
            response.details = "Nodos ocupados";
            return response;
        }

        Node mainNode = availabeNodes.remove(iMainNode);
        Node backUpNode = availabeNodes.remove(iBackNode);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(3, mainNode, userId+"/"+path, userId+"/"+newPath);
        Callable<Response> backUp = new NodeRequest(3, backUpNode, userId+"/"+path, userId+"/"+newPath);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                response.statusCode = mainResponse.statusCode;
                response.details = mainResponse.details;
                return response;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public Response deleteFile(int userId, int node, int backNode, String path){
        Response response = new Response();
        response.details = "Error en el servidor";
        response.statusCode = 500;
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            response.details = "Nodos ocupados";
            return response;
        }

        int iMainNode = searchNode(node);
        int iBackNode = searchNode(backNode);

        if(iMainNode == -1 || iBackNode == -1){
            response.details = "Nodos ocupados";
            return response;
        }

        Node mainNode = availabeNodes.remove(iMainNode);
        Node backUpNode = availabeNodes.remove(iBackNode);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(4, mainNode, userId+"/"+path );
        Callable<Response> backUp = new NodeRequest(4, backUpNode, userId+"/"+path);


        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                System.out.println(mainResponse.statusCode);
                response.statusCode = mainResponse.statusCode;
                response.details = mainResponse.details;
                return response;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public Response createFolder(int userId,String path){
        Response response = new Response();
        response.details = "Error en el servidor";
        response.statusCode = 500;
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            response.details = "Nodos ocupados";
            return response;
        }

        Node mainNode = availabeNodes.remove(availabeNodes.size() -1);
        Node backUpNode = availabeNodes.remove(availabeNodes.size() -1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(5, mainNode, userId+"/"+path);
        Callable<Response> backUp = new NodeRequest(5, backUpNode, userId+"/"+path);

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();

            if(mainResponse.statusCode == 200 && backResponse.statusCode == 200){
                availabeNodes.add(mainNode);
                availabeNodes.add(backUpNode);

                response.statusCode = mainResponse.statusCode;
                response.details = mainResponse.details;
                response.mainNode = mainNode.getId();
                response.backUpNode = backUpNode.getId();
                return response;
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    // ! Nodes can't be choosen randomly
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

    private int searchNode(int nodeId){
        for (int i = 0; i < availabeNodes.size(); i++) {
            availabeNodes.get(i);
            if(availabeNodes.get(i).getId() == nodeId){
                return i;
            }
        }
        return -1;
    }











    private void checkQueue(){

    }
}

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
    private Node node1 = new Node(1,"207.248.81.92", 1097, 100);
    private Node node2 = new Node(2,"207.248.81.92", 1098, 100);
    private Node node3 = new Node(3,"207.248.81.92", 1099, 100);
    private Queue<NodeRequest> requests = new LinkedList<>();

    public NodeController(){
        availabeNodes.add(node1);
        availabeNodes.add(node2);
        availabeNodes.add(node3);
        System.out.println(availabeNodes);
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
        boolean iMainNode = searchNode(node);
        boolean iBackNode = searchNode(backNode);

        Node worker;
        if(!iMainNode && !iBackNode ){
            return null;
        }else if(!iBackNode){
            worker = getNode(node);
        }else{
            worker = getNode(backNode);
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

        boolean iMainNode = searchNode(node);
        boolean iBackNode = searchNode(backNode);

        if(!iMainNode && !iBackNode){
            response.details = "Nodos ocupados";
            return response;
        }

        Node mainNode = getNode(node);
        Node backUpNode = getNode(backNode);

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

        boolean iMainNode = searchNode(node);
        boolean iBackNode = searchNode(backNode);

        if(!iMainNode && !iBackNode){
            response.details = "Nodos ocupados";
            return response;
        }

        Node mainNode = getNode(node);
        Node backUpNode = getNode(backNode);

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

    public Response deleteFolder(int userId, int node, int backNode, String path){
        Response response = new Response();
        response.details = "Error en el servidor";
        response.statusCode = 500;
        if(availabeNodes.isEmpty() || availabeNodes.size() < 2){
            response.details = "Nodos ocupados";
            return response;
        }

        boolean iMainNode = searchNode(node);
        boolean iBackNode = searchNode(backNode);

        if(!iMainNode && !iBackNode){
            response.details = "Nodos ocupados";
            return response;
        }

        Node mainNode = getNode(node);
        Node backUpNode = getNode(backNode);
        System.out.println(mainNode.toString() + "++++" + backUpNode.toString());


        ExecutorService pool = Executors.newFixedThreadPool(2);

        Callable<Response> main = new NodeRequest(6, mainNode, userId+"/"+path+'/');
        Callable<Response> backUp = new NodeRequest(6, backUpNode, userId+"/"+path+'/');

        Future<Response> mainRequest =  pool.submit(main);
        Future<Response> backUpRequest =  pool.submit(backUp);

        try {
            Response mainResponse = mainRequest.get();
            Response backResponse = backUpRequest.get();
            System.out.println("CODE: "+mainResponse.statusCode);


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

    private boolean searchNode(int nodeId){
        System.out.println(availabeNodes.size());
        for (int i = 0; i < availabeNodes.size(); i++) {

            if(availabeNodes.get(i).getId() == nodeId){
                System.out.println("SEARCH:  "+availabeNodes.get(i)+ "|| " + i);
                return true;
            }
        }
        return false;
    }

    private Node getNode(int nodeId){
        for (int i = 0; i < availabeNodes.size(); i++) {
            if(availabeNodes.get(i).getId() == nodeId){
                System.out.println("REMMOVE:::"+availabeNodes.get(i));
                return availabeNodes.remove(i);
            }
        }
        return null;
    }










    private void checkQueue(){

    }
}

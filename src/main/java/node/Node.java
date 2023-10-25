package node;

public class Node {
    private int id;
    private String ip;
    private int port;
    private int storageCapacity; // In gigabytes

    public Node(int id, String ip, int port, int storageCapacity){
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.storageCapacity = storageCapacity;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    @Override
    public String toString() {
        return this.id + " = " + this.ip +":"+this.port ;
    }
}

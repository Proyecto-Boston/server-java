package node;

public class NodeRequest {
    private int userId;
    private String fileName;
    private byte[] fileData;
    private int request;

    // A request can be:
    // 1. Upload
    // 2. Download
    // 3. Change file route
    // 4. Delete file
    // 5. Create folder
    // 6. Delete folder
}

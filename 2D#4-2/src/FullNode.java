// IN2011 Computer Networks
// Coursework 2023/2024
//
// Submission by
// YOUR_NAME_GOES_HERE
// YOUR_STUDENT_ID_NUMBER_GOES_HERE
// YOUR_EMAIL_GOES_HERE
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// DO NOT EDIT starts
interface FullNodeInterface {
    public boolean listen(String ipAddress, int portNumber);
    public void handleIncomingConnections(String startingNodeName, String startingNodeAddress);
}
// DO NOT EDIT ends

public class FullNode implements FullNodeInterface {

    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> networkMap = new ConcurrentHashMap<>();

    @Override
    public boolean listen(String ipAddress, int portNumber) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            serverSocket = new ServerSocket(portNumber, 50, address);
            System.out.println("Listening on " + ipAddress + ":" + portNumber);
            return true;
        } catch (IOException e) {
            System.err.println("Error creating server socket: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void handleIncomingConnections(String startingNodeName, String startingNodeAddress) {
        new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    // Handle each connection in a new thread
                    new Thread(new ClientHandler(clientSocket, startingNodeName, startingNodeAddress)).start();
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                    break;
                }
            }
        }).start();
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String startingNodeName;
        private String startingNodeAddress;

        public ClientHandler(Socket clientSocket, String startingNodeName, String startingNodeAddress) {
            this.clientSocket = clientSocket;
            this.startingNodeName = startingNodeName;
            this.startingNodeAddress = startingNodeAddress;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("START")) {
                        // Process start message
                        out.println("START ACK");
                    } else if (inputLine.startsWith("ECHO?")) {
                        out.println("OHCE");
                    } else if (inputLine.startsWith("PUT?")) {
                        boolean success = storeKeyValue(in);
                        out.println(success ? "SUCCESS" : "FAILED");
                    } else if (inputLine.startsWith("GET?")) {
                        String value = getValueForKey(in);
                        if (value != null) {
                            out.println("VALUE " + value);
                        } else {
                            out.println("NOPE");
                        }
                    } else if (inputLine.startsWith("NOTIFY?")) {
                        updateNetworkMap(in);
                        out.println("NOTIFIED");
                    } else if (inputLine.startsWith("NEAREST?")) {
                        String nodes = getNearestNodes(in);
                        out.println("NODES " + nodes);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client connection: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    
        
        private ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();
        private ConcurrentHashMap<String, String> networkMap = new ConcurrentHashMap<>();
        private ServerSocket serverSocket;
        
        private boolean storeKeyValue(BufferedReader in) throws IOException {
            // Read the line count for the key and value
            int keyLineCount = Integer.parseInt(in.readLine());
            int valueLineCount = Integer.parseInt(in.readLine());
        
            // Read the key lines
            StringBuilder keyBuilder = new StringBuilder();
            for (int i = 0; i < keyLineCount; i++) {
                keyBuilder.append(in.readLine());
                keyBuilder.append("\n"); // Append newline if needed
            }
            String key = keyBuilder.toString().trim(); // Trim any trailing newline characters
        
            // Read the value lines
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 0; i < valueLineCount; i++) {
                valueBuilder.append(in.readLine());
                valueBuilder.append("\n"); // Append newline if needed
            }
            String value = valueBuilder.toString().trim(); // Trim any trailing newline characters
        
            // Store the key-value pair
            keyValueStore.put(key, value);
        
            return true;
        }

        private String getValueForKey(BufferedReader in) throws IOException {
            try {
                // Read the line count for the key
                int keyLineCount = Integer.parseInt(in.readLine());
        
                // Read the key lines
                StringBuilder keyBuilder = new StringBuilder();
                for (int i = 0; i < keyLineCount; i++) {
                    keyBuilder.append(in.readLine());
                }
                String key = keyBuilder.toString();
        
                // Retrieve the value for the key
                String value = keyValueStore.get(key);
        
                // Return the value or an indicator that the key is not found
                return value != null ? value : "NOPE";
            } catch (NumberFormatException e) {
                System.err.println("Key line count format error: " + e.getMessage());
                return "ERROR";
            }
        }
        
        
        private void updateNetworkMap(BufferedReader in) throws IOException {
            String nodeName = in.readLine(); // Read the node name
            String nodeAddress = in.readLine(); // Read the node address
        
            networkMap.put(nodeName, nodeAddress);
        }
        
        
        private String getNearestNodes(BufferedReader in) throws IOException {
            String hashID = in.readLine(); // Read the hashID from the input
            // Placeholder for nearest nodes, should be replaced with actual logic
            Map<String, String> nearestNodes = findNearestNodes(hashID);
    
            // Build the response
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("NODES ").append(nearestNodes.size()).append("\n");
            
            for (Map.Entry<String, String> entry : nearestNodes.entrySet()) {
                String nodeName = entry.getKey();
                String nodeAddress = entry.getValue();
                responseBuilder.append(nodeName).append(" ").append(nodeAddress).append("\n");
            }
    
            return responseBuilder.toString();
        }

        private Map<String, String> findNearestNodes(String targetHashID) {
            BigInteger targetHash = new BigInteger(targetHashID, 16);
            HashMap<String, BigInteger> distanceMap = new HashMap<>();
        
            // Calculate the "distance" between the target hash ID and each node's hash ID
            for (String nodeName : networkMap.keySet()) {
                String nodeHashID = getNodeHashID(nodeName); // You need to implement getNodeHashID
                BigInteger nodeHash = new BigInteger(nodeHashID, 16);
                BigInteger distance = targetHash.subtract(nodeHash).abs(); // Simplified distance calculation
                distanceMap.put(nodeName, distance);
            }
        
            // Sort entries by distance and select the closest ones
            // This is a basic sorting operation; for large datasets, consider more efficient approaches
            List<Map.Entry<String, BigInteger>> sortedEntries = new ArrayList<>(distanceMap.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue());
        
            // Assuming we want to find the 3 nearest nodes
            Map<String, String> nearestNodes = new LinkedHashMap<>();
            int count = 0;
            for (Map.Entry<String, BigInteger> entry : sortedEntries) {
                if (count >= 3) break; // Limit to 3 nearest nodes
                nearestNodes.put(entry.getKey(), networkMap.get(entry.getKey()));
                count++;
            }
        
            return nearestNodes;
        }
        private String getNodeHashID(String nodeName) {
            return "";
        }
    }
}
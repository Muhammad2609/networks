// IN2011 Computer Networks
// Coursework 2023/2024
//
// Submission by
// YOUR_NAME_GOES_HERE
// YOUR_STUDENT_ID_NUMBER_GOES_HERE
// YOUR_EMAIL_GOES_HERE
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

// DO NOT EDIT starts
interface TemporaryNodeInterface {
    public boolean start(String startingNodeName, String startingNodeAddress);
    public boolean store(String key, String value);
    public String get(String key);
}
// DO NOT EDIT ends


public class TemporaryNode implements TemporaryNodeInterface {
	
	private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

	@Override
	public boolean start(String startingNodeName, String startingNodeAddress) {
		try {
			String[] addressParts = startingNodeAddress.split(":");
			if (addressParts.length != 2) {
				System.err.println("Invalid starting node address format.");
				return false;
			}
			String host = addressParts[0];
			int port = Integer.parseInt(addressParts[1]);
	
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
			// Sending START message
			out.println("START 1 " + startingNodeName);
			String response = in.readLine();
			return response != null && response.startsWith("START");
		} catch (IOException e) {
			System.err.println("Error starting connection: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean store(String key, String value) {
		try {
			// Correctly ensure newline terminations for protocol consistency
			if (!key.endsWith("\n")) {
				key += "\n";
			}
			if (!value.endsWith("\n")) {
				value += "\n";
			}
	
			// Calculate line counts (assuming keys and values are single-line for simplicity)
			int keyLineCount = key.split("\n").length;
			int valueLineCount = value.split("\n").length;
	
			// Send the PUT? request with expected format
			out.println("PUT?");
			out.println(keyLineCount); // Send the key line count
			out.println(valueLineCount); // Send the value line count
			out.print(key); // Use print to avoid additional newlines
			out.print(value); // Use print to maintain control over newline characters
			out.flush(); // Ensure data is sent immediately
	
			String response = in.readLine();
			return "SUCCESS".equals(response);
		} catch (IOException e) {
			System.err.println("Error storing key-value pair: " + e.getMessage());
			return false;
		}
	}
	

	@Override
	public String get(String key) {
		try {
			out.println("GET?");
			out.println(1); // Number of lines for the key
			out.println(key); // The actual key
			out.flush(); // Make sure to flush the stream
	
			String response = in.readLine();
			if (response != null && response.startsWith("VALUE")) {
				// Read the value sent by the server
				return response.substring(6); // Assuming the response is "VALUE <value>"
			} else {
				// Handle "NOPE" or any other response from the server
				return response != null ? response : "ERROR";
			}
		} catch (IOException e) {
			System.err.println("Error retrieving the value: " + e.getMessage());
			return "ERROR";
		}
	}
	
	
	
}

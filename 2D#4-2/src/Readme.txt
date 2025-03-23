How to Run the 2D#4 Network Applications
========================================

This guide explains how to compile and run the Java applications for the 2D#4 network, including FullNode, TemporaryNode, CmdLineGet, CmdLineStore, and CmdLineFullNode.

Prerequisites
-------------
- Java Development Kit (JDK) installed on your system.
- Access to a terminal or command prompt.

Compilation
-----------
1. Open your terminal or command prompt.
2. Navigate to the directory containing your Java files.
3. Compile all the Java files with the following command:
    ```
    javac HashID.java FullNode.java TemporaryNode.java CmdLineGet.java CmdLineStore.java CmdLineFullNode.java
    ```

Running the Applications
------------------------
### CmdLineGet
To retrieve a value for a given key from the network:

java CmdLineGet <StartingNodeName> <StartingNodeAddress> <Key>

- `<StartingNodeName>`: Name of the starting node.
- `<StartingNodeAddress>`: Address of the starting node (format: IP:Port).
- `<Key>`: The key for which you want to retrieve the value.

### CmdLineStore
To store a key-value pair in the network:

java CmdLineStore <StartingNodeName> <StartingNodeAddress> <Key> <Value>

- `<Value>`: The value to store in the network.

### CmdLineFullNode
To start a full node in the network:

java CmdLineFullNode <StartingNodeName> <StartingNodeAddress> <IPAddress> <PortNumber>

- `<IPAddress>`: The IP address on which the full node will listen for incoming connections.
- `<PortNumber>`: The port number on which the full node will listen for incoming connections.

Note
----
- Ensure that the starting node information (name and address) you provide corresponds to an active node in the network for testing.
- For `CmdLineGet` and `CmdLineStore`, the `<Key>` and `<Value>` arguments should end with a newline character (`\n`).

# 2D#4 Distributed Hash Table (DHT) Protocol – Java Implementation

This repository contains a Java-based implementation of the **2D#4 Distributed Hash Table (DHT)** protocol, built to comply with RFC specifications. The system is designed to be a scalable peer-to-peer (P2P) network using application-layer communication over TCP/IP.

### Key Features
- Implements the **2D#4** DHT protocol in Java.
- Supports full nodes and temporary nodes with peer discovery and communication.
- Enables key-value storage and lookup using command-line tools.
- Real-time packet inspection and protocol debugging performed using **Wireshark**.

---

## 📦 How to Run the 2D#4 Network Applications

This guide covers compiling and executing the components of the network, including:

- `FullNode`
- `TemporaryNode`
- `CmdLineGet`
- `CmdLineStore`
- `CmdLineFullNode`

---

## 🛠 Prerequisites

- **Java Development Kit (JDK)** installed (version 8+ recommended)
- A terminal or command prompt
- Basic knowledge of networking (helpful but not required)

---

## 🧱 Compilation

1. Open your terminal or command prompt.
2. Navigate to the directory containing the `.java` files.
3. Compile all source files with:

```bash
javac HashID.java FullNode.java TemporaryNode.java CmdLineGet.java CmdLineStore.java CmdLineFullNode.java
```

---

## 🚀 Running the Applications

### 🔍 CmdLineGet

Retrieve the value associated with a key from the network:

```bash
java CmdLineGet <StartingNodeName> <StartingNodeAddress> <Key>
```

- `<StartingNodeName>`: Name of the known starting node  
- `<StartingNodeAddress>`: Address in IP:Port format  
- `<Key>`: The key to look up (must end with a `\n`)

---

### 💾 CmdLineStore

Store a key-value pair in the network:

```bash
java CmdLineStore <StartingNodeName> <StartingNodeAddress> <Key> <Value>
```

- `<StartingNodeName>`: Name of the known starting node  
- `<StartingNodeAddress>`: Address in IP:Port format  
- `<Key>`: The key to store  
- `<Value>`: The value to store (must end with a `\n`)

---

### 🌐 CmdLineFullNode

Start a full node to participate in the network:

```bash
java CmdLineFullNode <StartingNodeName> <StartingNodeAddress> <IPAddress> <PortNumber>
```

- `<StartingNodeName>`: Name of the known starting node  
- `<StartingNodeAddress>`: Address in IP:Port format  
- `<IPAddress>`: IP address on which the node will listen  
- `<PortNumber>`: Port number on which the node will listen

---

### ⚠️ Notes

Ensure that `<StartingNodeName>` and `<StartingNodeAddress>` refer to a reachable and active node already part of the network.

Keys and values passed into `CmdLineGet` and `CmdLineStore` must end with a newline character (`\n`) to conform to the protocol.

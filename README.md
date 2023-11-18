# Dijkstra's Shortest Path Algorithm with Neo4j Integration

## This Java project implements Dijkstra's shortest path algorithm with Neo4j graph database integration. It consists of two main classes:
## Dijkstra Class

### Description:
The Dijkstra class provides an implementation of Dijkstra's shortest path algorithm.
It utilizes a Neo4j server connection to retrieve graph information.
### Usage:
Instantiate the class with a Neo4j server connection, start node, and end node.
Call the djikstra method to compute the shortest path.
###   Methods:
   -   Dijkstra(Nserver graphDB, String startNode, String endNode): Constructor for the Dijkstra class.
   -   HashMap<String, Double> getNeighbours(String nodeLabel): Returns a map of neighbors and their associated edges for a specified node.
   -  ArrayList<String> djikstra(): Applies Dijkstra's algorithm to find the shortest path.

## Nserver Class

### Description:
The Nserver class represents a connection to a Neo4j server and provides methods to interact with the graph database.
###   Usage:
Instantiate the class with Neo4j server connection details.
Use various methods to perform operations on the graph (create nodes, set properties, set relations, etc.).
###    Methods:
- Nserver(String uri, String username, String password): Constructor for the Nserver class.
- Nserver(String uri, String username, String password, String targetGraph): Constructor with an option to specify a target graph.
- void createNode(String nodeLabel): Creates an anonymous node with the specified label.
- void createNode(String nodeReference, String nodeLabel): Creates a node variable with a label.
- void setProperty(String nodeLabel, Nproperty property): Adds a property to a node with a label.
- void setRelation(String firstNodeName, String secondNodeName, String relation): Sets a relation between two nodes.
- void setRelation(String firstNodeName, String secondNodeName, double relation): Sets a relation with a double type.
- void removeNode(String nodeLabel): Deletes a node and its associated relations.
- List<Record> getRelations(String nodeLabel): Gets all relations outgoing from a node.
- List<Record> getNeighbours(String nodeLabel): Gets neighbors of a target node.
- ArrayList<String> getAllNodes(): Retrieves all node labels in the graph.
- String getTargetGraph(): Gets the target graph name.
- void setTargetGraph(String targetGraph): Sets the target graph name.

## Dependencies:
- Neo4j Java Driver
## License:

This project is licensed under the MIT License.

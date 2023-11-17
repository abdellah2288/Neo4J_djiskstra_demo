package N4J;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a Neo4J server connection and abstracts away some of the most important functionality
 */
public class Nserver
{
    private final Driver driver;
    /**
     * if the targetGraph variable is set, the program attemps to run all queries with the "USE testGraph" prefix to
     * to run queries in the specified graph
     */
    private String targetGraph = null;
    public Nserver(String uri, String username, String password)
    {
        driver = GraphDatabase.driver(uri,AuthTokens.basic(username,password));
    }
    public Nserver(String uri, String username, String password, String targetGraph)
    {
        driver = GraphDatabase.driver(uri,AuthTokens.basic(username,password));
        this.targetGraph = targetGraph;
    }

    /**
     * Sends Cypher query to Neo4J server
     * @param queryString the queryString is the cypher query to be ran by the Neo4J server
     * @return returns an ArrayList of records resulting from the query
     */
    private ArrayList execQuery(String queryString)
    {
        ArrayList<Record> queryResult = new ArrayList<>();

        if(this.targetGraph != null)
        {
            queryString = String.format("use %s %s", targetGraph,queryString);
        }
        try(var session = driver.session())
        {
            Result result = session.run(queryString);
            while(result.hasNext())
            {
                Record record = result.next();
                queryResult.add(record);
            }
        }
        return queryResult;
    }

    /**
     * creates anonymous node with the specified label
     * @param nodeLabel the label used to refer to the node
     */
    public void createNode(String nodeLabel)
    {
        String queryString = String.format("MERGE(: %s) ",nodeLabel);
        execQuery(queryString);
    }

    /**
     * Creates a node variable with a label
     * @param nodeReference the cypher variable containing the node
     * @param nodeLabel the node's label
     */
    public void createNode(String nodeReference, String nodeLabel)
    {
        String queryString = String.format("MERGE(%s : %s) ",nodeReference,nodeLabel);
        execQuery(queryString);
    }

    /**
     * adds property to node with label
     * @param nodeLabel The target node's label
     * @param property the Nproperty (propertyName,properyValue) pair to set
     */
    public void setProperty(String nodeLabel,Nproperty property)
    {
        String queryString = String.format("MATCH (n:%s) \n SET n.%s = \"%s\" \n RETURN n",nodeLabel,property.getAttributeName(),property.getAttributeValue());
        execQuery(queryString);
    }

    /**
     * Set relation between firstNode and secondNode in the following manner: (firstNode)-[:relation]->(secondNode)
     * @param firstNodeName
     * @param secondNodeName
     * @param relation
     */
    public void setRelation(String firstNodeName, String secondNodeName, String relation)
    {
        String queryString = String.format("MATCH (n:%s),(m:%s) \n MERGE (n)-[:%s]->(m) \n RETURN n,m",firstNodeName,secondNodeName,relation);
        execQuery(queryString);
    }
    /**
     * Set relation between firstNode and secondNode in the following manner: (firstNode)-[:relation]->(secondNode)
     * @param firstNodeName
     * @param secondNodeName
     * @param relation if relation is of type double, it requires double backticks in the query as specified by the cypher documentation on "uncommon characters"
     */
    public void setRelation(String firstNodeName, String secondNodeName, double relation)
    {
        String queryString = String.format("MATCH (n:%s),(m:%s) \n MERGE (n)-[:`%s`]->(m) \n RETURN n,m",firstNodeName,secondNodeName,relation);
        execQuery(queryString);
    }

    /**
     * Deletes node and all relations associated with it from the graph database
     * @param nodeLabel target node to be deleted
     */
    public void removeNode(String nodeLabel)
    {
        String queryString = String.format("MATCH (n:%s) \n DELETE n \n RETURN n",nodeLabel);
        execQuery(queryString);
    }

    /**
     * Get all relations outgoing from node
     * @param nodeLabel target node
     * @return a List of records containing information about relation types and related node labels
     */
    public List<Record> getRelations(String nodeLabel)
    {
        String queryString = String.format("MATCH (n:%s)-[r]->(x) RETURN type(r),labels(x)",nodeLabel);
        return execQuery(queryString);
    }

    /**
     * Get neighbours of target node, ie nodes related to target node with an outgoing relation
     * @param nodeLabel target node
     * @return a List of Records containing labels of nodes related to target node.
     */
    public List<Record> getNeighbours(String nodeLabel)
    {
        String queryString = String.format("MATCH (n:%s)-[r]->(x) RETURN labels(x)",nodeLabel);
        return execQuery(queryString);
    }

    /**
     * Dumps all node labels
     * @return an ArrayList containing all node labels in the graph
     */
    public ArrayList<String> getAllNodes()
    {
        ArrayList<String> nodeList = new ArrayList<>();
        String queryString = String.format("MATCH (n) RETURN labels(n)");
        Iterator<Record> queryIterator = execQuery(queryString).iterator();
        while(queryIterator.hasNext())
        {
            Record record = queryIterator.next();
            String nodeLabel = record.get("labels(n)").toString();
            nodeLabel = nodeLabel.substring(2,nodeLabel.length()-2);
            nodeList.add(nodeLabel);
        }
        return nodeList;
    }
    public String getTargetGraph()
    {
        return targetGraph;
    }
    public void setTargetGraph(String targetGraph)
    {
        this.targetGraph = targetGraph;
    }
}

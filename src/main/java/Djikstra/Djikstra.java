package Djikstra;
import N4J.Nserver;
import org.neo4j.driver.Record;
import java.util.*;


public class Djikstra
{
    /**
     * A map of nodes/distances for use in the djikstra algorithm
     */
    private HashMap<String,Double> distanceList = null;

    private ArrayList<String> unvisitedNodes = null;
    /**
     * An array to store the result of the djisktra algorithm
     */
    private ArrayList<String> shortestPath = null;
    private String startNode;
    private String endNode;

    private Nserver graphServer;

    /**
     * Djikstra class constructor
     * @param graphDB   the graphDB to get the nodes from
     * @param startNode the path's start node
     * @param endNode   the path's end node
     */
    public Djikstra(Nserver graphDB,String startNode,String endNode)
    {
        distanceList = new HashMap<>();
        unvisitedNodes = graphDB.getAllNodes();
        shortestPath = new ArrayList<>();
        graphServer = graphDB;
        this.startNode = startNode;
        this.endNode = endNode;

        for(String x: this.unvisitedNodes)
        {
            distanceList.put(x,Double.POSITIVE_INFINITY);
        }
        distanceList.replace(this.startNode,Double.POSITIVE_INFINITY,Double.valueOf(0.0));
    }

    /**
     * Gets all neighbours of specified node
     * @param nodeLabel target node
     * @return a map of neighbours and their associated edges
     */
    public HashMap<String,Double> getNeighbours(String nodeLabel)
    {
        HashMap<String,Double> neighbourMap = new LinkedHashMap<>();

        Iterator<Record> iterator = this.graphServer.getRelations(nodeLabel).iterator();

        while(iterator.hasNext())
        {
            Record record = iterator.next();

            String neighbourLabel = record.get("labels(x)").toString();
            String neighbourDistanceStr = record.get("type(r)").toString();

            neighbourLabel = neighbourLabel.substring(2,neighbourLabel.length()-2);

            neighbourDistanceStr = neighbourDistanceStr.substring(1,neighbourDistanceStr.length()-1);

            Double neighbourDistance = Double.valueOf(neighbourDistanceStr);

            neighbourMap.put(neighbourLabel,neighbourDistance);
        }
        return neighbourMap;
    }

    /**
     * apply's djiskstra's shortest path algorithm to get the shortest path
     * @return
     */
    public ArrayList<String> djikstra()
    {
        if(this.unvisitedNodes == null)
        {
            return this.shortestPath;
        }
        if(this.unvisitedNodes.size() == 0)
        {
            return this.shortestPath;
        }

        double minimal_distance = unvisitedNodes.stream().mapToDouble(x -> this.distanceList.get(x)).min().getAsDouble();
        String currNode = unvisitedNodes.stream().filter(x -> this.distanceList.get(x) == minimal_distance).reduce("",(c,e)->c+e);

        double currDistance = this.distanceList.get(currNode);


        this.unvisitedNodes.remove(currNode);

        this.shortestPath.add(currNode);

        if(currNode.compareToIgnoreCase(this.endNode) == 0) return this.shortestPath;

        HashMap<String,Double> neighbourMap = this.getNeighbours(currNode);
        Iterator<String> neighbourLabels = neighbourMap.keySet().iterator();

        while(neighbourLabels.hasNext())
        {
            String label = neighbourLabels.next();
            this.distanceList.replace(label,neighbourMap.get(label) + currDistance);
        }


        return djikstra();
    }
}

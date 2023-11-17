import Djikstra.Djikstra;
import N4J.*;

public class Main
{
    public static void main(String[] args)
    {
        Nserver graphdb = new Nserver("bolt://localhost:7687","neo4j","theworld","djikstra");

        graphdb.createNode("a");
        graphdb.createNode("b");
        graphdb.createNode("c");
        graphdb.createNode("d");
        graphdb.createNode("e");



        graphdb.setRelation("a","b",6);
        graphdb.setRelation("a","d", 1);
        graphdb.setRelation("a","c",2);
        graphdb.setRelation("b","e",3);
        graphdb.setRelation("c","e",1);
        graphdb.setRelation("c","b",4);
        graphdb.setRelation("d","c",3);


        Djikstra djikstra = new Djikstra(graphdb,"a","e");
        System.out.println(djikstra.djikstra());
    }
}
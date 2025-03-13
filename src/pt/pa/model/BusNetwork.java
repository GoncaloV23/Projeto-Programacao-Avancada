package pt.pa.model;

import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.Vertex;
import pt.pa.observerpattern.Subject;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusNetwork extends Subject{
    Graph<Stop, Route> netWork;

    public BusNetwork() {
        this.netWork = new GraphAdjacencyList<>();
    }

    public Graph<Stop, Route> getNetWork() {
        return netWork;
    }
    public void resetNetWork(){
            this.netWork = new GraphAdjacencyList<>();
    }

    public void setNetWork(Graph<Stop, Route> netWork) {
        this.netWork = netWork;
    }


    public Edge<Route, Stop> addRoute(Stop orig, Stop dest, int distance, int duration) throws InvalidAttributeValueException {
        if (distance <= 0) throw new InvalidAttributeValueException();
        if (duration <= 0) throw new InvalidAttributeValueException();



        Edge<Route, Stop> edge = netWork.insertEdge(orig, dest, new Route(orig, dest, distance, duration));
        notifyObservers(null);
        return edge;
    }

    public Route removeRoute(Edge<Route, Stop> edge) throws InvalidAttributeValueException {
        if (edge == null) throw new InvalidAttributeValueException();

        netWork.removeEdge(edge);
        notifyObservers(null);

        return edge.element();
    }

    public int numberOfStops(){
        return netWork.numVertices();
    }

    public int numberOfRoutes(){
        return netWork.numEdges();
    }

    public List<Map.Entry<Vertex<Stop>, Integer>> StopsCentrality(){
        Map<Vertex<Stop>, Integer> map = new HashMap<>();
        int count = 0;
        for(Vertex<Stop> vertex : netWork.vertices()) {
            count = netWork.incidentEdges(vertex).size();
            map.put(vertex, count);
        }

        List<Map.Entry<Vertex<Stop>, Integer>> list = new ArrayList<>(map.entrySet());

        list.sort((val1, val2) -> {
            return val2.getValue() - val1.getValue();
        });

        return list;
    }

    public List<Map.Entry<Vertex<Stop>, Integer>> TopStopsCentrality(){
        List<Map.Entry<Vertex<Stop>, Integer>> list = StopsCentrality();
        List<Map.Entry<Vertex<Stop>, Integer>> list2 = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            list2.add(list.get(i));
        }

        return list2;
    }

    public List<List<Vertex<Stop>>> subNetworks(){
        List<List<Vertex<Stop>>> res = new ArrayList<>();

        for (Vertex<Stop> v : netWork){
            boolean alreadyContained = false;
            for(List<Vertex<Stop>> list : res){
                if(list.contains(v)) alreadyContained = true ;
            }
            if(!alreadyContained)res.add(order(v));
        }
        return res;
    }

    public List order(Vertex<Stop> root){

        List<Vertex<Stop>> visited = new ArrayList();
        List<Vertex<Stop>> stack = new ArrayList();

        visited.add(root);
        stack.add(root);

        return checkOrder(visited, stack);
    }

    private List<Vertex<Stop>> checkOrder(List<Vertex<Stop>> visited, List<Vertex<Stop>> stack){
        List<Vertex<Stop>> res = new ArrayList();
        while(!stack.isEmpty()){
            Vertex<Stop> aux = stack.remove(stack.size()-1);

            res.add(aux);
            for(Edge e: netWork.incidentEdges(aux)){
                Vertex v = netWork.opposite(aux,e);
                if(!visited.contains(v)){
                    visited.add(v);
                    stack.add(v);
                }
            }
        }
        return res;
    }


    public DijsktraResult path(Vertex<Stop> start, Vertex<Stop> destination){
        return DijsktraResult.minimumDistancePath(start, destination, CostType.TIME, netWork);
    }
    public List<Vertex<Stop>> fastestPath(Vertex<Stop> start, Vertex<Stop> destination){
        return path(start, destination).getPath();
    }
    public List<Vertex<Stop>> shortestPath(Vertex<Stop> start, Vertex<Stop> destination){
        return DijsktraResult.minimumDistancePath(start, destination, CostType.DISTANCE, netWork).getPath();
    }

    public List<Vertex<Stop>> farthestStopPair(){
        List<Vertex<Stop>> result;

        Vertex<Stop> orig = null, dest = null;
        int cost = 0 ;
        Map<Vertex<Stop>, Vertex<Stop>> bestPredecessors = null;
        Map<Vertex<Stop>, Integer> bestCost = null;

        for (Vertex<Stop> v : netWork){
            Map<Vertex<Stop>, Integer> distances = new HashMap<>();
            Map<Vertex<Stop>, Vertex<Stop>> predecessors = new HashMap<>();
            DijsktraResult.dijkstraDistance(netWork, v, distances, predecessors, new HashMap<>());

            for(Map.Entry<Vertex<Stop>, Integer> entry : distances.entrySet()){
                if(entry.getValue()>cost && entry.getValue()!=Integer.MAX_VALUE){
                    cost = entry.getValue();
                    orig = v;
                    dest = entry.getKey();
                    bestPredecessors = predecessors;
                    bestCost = distances;
                }
            }
        }

        if(cost == 0)return null;

        return DijsktraResult.buildPath(orig, dest, bestCost, bestPredecessors, new HashMap<>()).getPath();
    }

    public List<Vertex<Stop>> getStopsAtARange(Vertex<Stop> orig, int nRoutes){
        Map<Vertex<Stop>, Vertex<Stop>> predecessors = new HashMap<>();
        Map<Vertex<Stop>, Integer> cost = new HashMap<>();
        DijsktraResult.dijkstra(netWork, orig, cost, predecessors, new HashMap<>());

        List<Vertex<Stop>> list = new ArrayList<>();

        for (Map.Entry<Vertex<Stop>, Integer> entry : cost.entrySet()){
            if(entry.getValue()<=nRoutes && entry.getValue()!=0){
                list.add(entry.getKey());
            }
        }

        return list;
    }
    enum CostType{
        DISTANCE, TIME;
    }

}

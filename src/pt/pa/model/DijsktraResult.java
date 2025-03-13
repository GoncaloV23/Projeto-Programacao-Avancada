package pt.pa.model;

import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DijsktraResult {
    private int cost = Integer.MAX_VALUE;
    private List<Vertex<Stop>> path = null;
    private List<Edge<Route, Stop>> pathEdges = null;

    public DijsktraResult(int cost, List<Vertex<Stop>> path) {
        this.cost = cost;
        this.path = path;
    }

    public DijsktraResult(int cost, List<Vertex<Stop>> path, List<Edge<Route, Stop>> pathEdges) {
        this.cost = cost;
        this.path = path;
        this.pathEdges = pathEdges;
    }

    public int getCost() {
        return cost;
    }

    public List<Vertex<Stop>> getPath() {
        return path;
    }

    public boolean hasSolution() {
        return cost != Integer.MAX_VALUE;
    }

    public List<Edge<Route, Stop>> getPathEdges() {
        return pathEdges;
    }
    public static DijsktraResult minimumDistancePath(Vertex<Stop> start, Vertex<Stop> destination, BusNetwork.CostType type, Graph<Stop, Route> netWork) {

        Map<Vertex<Stop>, Integer> costs = new HashMap<>();
        Map<Vertex<Stop>, Vertex<Stop>> predecessors = new HashMap<>();
        Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors = new HashMap<>();

        if(type == BusNetwork.CostType.DISTANCE)dijkstraDistance(netWork, start, costs, predecessors, edgePredecessors);
        else dijkstraTime(netWork, start, costs, predecessors, edgePredecessors);

        return buildPath(start, destination, costs, predecessors, edgePredecessors);
    }
    public static DijsktraResult buildPath(Vertex<Stop> start, Vertex<Stop> destination
            , Map<Vertex<Stop>, Integer> costs
            , Map<Vertex<Stop>, Vertex<Stop>> predecessors
            , Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors){

        //qual o custo entre 'start' e 'destination'?
        int cost = costs.get(destination);

        //se distancia for "infinita", é porque não existe caminho até
        //o vértice 'destination'
        if(cost == Integer.MAX_VALUE)
            return new DijsktraResult(Integer.MAX_VALUE, null);

        //qual o caminho entre 'start' e 'destination'?
        List<Vertex<Stop>> path = new ArrayList<>();
        List<Edge<Route, Stop>> edgePath = new ArrayList<>();
        Vertex<Stop> current = destination;

        while (current != start) {
            path.add(0, current);
            if(edgePredecessors!=null)edgePath.add(0, edgePredecessors.get(current));

            current = predecessors.get(current);
        }
        path.add(0, start);

        return new DijsktraResult(cost, path, edgePath);
    }
    public static void dijkstraDistance(Graph<Stop, Route> graph, Vertex<Stop> start,
                                        Map<Vertex<Stop>, Integer> distances,
                                        Map<Vertex<Stop>, Vertex<Stop>> predecessors,
                                        Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors) {

        List<Vertex<Stop>> unvisited = new ArrayList<>();

        for (Vertex<Stop> v : graph.vertices()) {
            unvisited.add(v);
            distances.put(v, Integer.MAX_VALUE);
            predecessors.put(v, null);
            edgePredecessors.put(v, null);
        }
        distances.put(start, 0);

        while(!unvisited.isEmpty()) {
            Vertex<Stop> current = findMinCostVertex(distances, unvisited);

            if(current == null || distances.get(current) == Integer.MAX_VALUE)
                break; //escusado continuar, só restam vértices não atingíveis a partir de 'start'

            for (Edge<Route, Stop> e : graph.incidentEdges(current)) {
                Vertex<Stop> neighbor = graph.opposite(current, e);

                if(!unvisited.contains(neighbor)) continue;

                int pathCost = distances.get( current ) + e.element().getDistance();

                if(pathCost < distances.get(neighbor)) {
                    distances.put(neighbor, pathCost);
                    predecessors.put(neighbor, current);
                    edgePredecessors.put(neighbor, e);
                }
            }
            unvisited.remove(current);
        }
    }
    private static void dijkstraTime(Graph<Stop, Route> graph, Vertex<Stop> start,
                              Map<Vertex<Stop>, Integer> costs,
                              Map<Vertex<Stop>, Vertex<Stop>> predecessors,
                              Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors) {

        List<Vertex<Stop>> unvisited = new ArrayList<>();

        for (Vertex<Stop> v : graph.vertices()) {
            unvisited.add(v);
            costs.put(v, Integer.MAX_VALUE);
            predecessors.put(v, null);
            edgePredecessors.put(v, null);
        }
        costs.put(start, 0);

        while(!unvisited.isEmpty()) {
            Vertex<Stop> current = findMinCostVertex(costs, unvisited);

            if(current == null || costs.get(current) == Integer.MAX_VALUE)
                break; //escusado continuar, só restam vértices não atingíveis a partir de 'start'

            for (Edge<Route, Stop> e : graph.incidentEdges(current)) {
                Vertex<Stop> neighbor = graph.opposite(current, e);

                if(!unvisited.contains(neighbor)) continue;

                int pathCost = costs.get( current ) + e.element().getDistance();

                if(pathCost < costs.get(neighbor)) {
                    costs.put(neighbor, pathCost);
                    predecessors.put(neighbor, current);
                    edgePredecessors.put(neighbor, e);
                }
            }
            unvisited.remove(current);
        }
    }
    public static void dijkstra(Graph<Stop, Route> graph, Vertex<Stop> start,
                                Map<Vertex<Stop>, Integer> costs,
                                Map<Vertex<Stop>, Vertex<Stop>> predecessors,
                                Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors) {

        List<Vertex<Stop>> unvisited = new ArrayList<>();

        for (Vertex<Stop> v : graph.vertices()) {
            unvisited.add(v);
            costs.put(v, Integer.MAX_VALUE);
            predecessors.put(v, null);
            edgePredecessors.put(v, null);
        }
        costs.put(start, 0);

        while(!unvisited.isEmpty()) {
            Vertex<Stop> current = findMinCostVertex(costs, unvisited);

            if(current == null || costs.get(current) == Integer.MAX_VALUE)
                break; //escusado continuar, só restam vértices não atingíveis a partir de 'start'

            for (Edge<Route, Stop> e : graph.incidentEdges(current)) {
                Vertex<Stop> neighbor = graph.opposite(current, e);

                if(!unvisited.contains(neighbor)) continue;

                int pathCost = costs.get( current ) + 1;

                if(pathCost < costs.get(neighbor)) {
                    costs.put(neighbor, pathCost);
                    predecessors.put(neighbor, current);
                    edgePredecessors.put(neighbor, e);
                }
            }
            unvisited.remove(current);
        }
    }

    private static Vertex<Stop> findMinCostVertex(Map<Vertex<Stop>, Integer> distances,
                                                  List<Vertex<Stop>> unvisited) {

        if(unvisited.isEmpty())
            return null; //embora nao esperado -> prog. defensiva.

        Vertex<Stop> minVertex = unvisited.get(0);
        int minCost = distances.get(minVertex);

        for(int i=1; i < unvisited.size(); i++) {
            Vertex<Stop> current = unvisited.get(i);
            int currentCost = distances.get(current);

            if(currentCost < minCost) {
                minVertex = current;
                minCost = currentCost;
            }
        }
        return minVertex;
    }



}



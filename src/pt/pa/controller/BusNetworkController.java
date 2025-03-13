package pt.pa.controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import pt.pa.ticketStrategy.SimpleTicket;
import pt.pa.ticketStrategy.TicketStrategy;
import pt.pa.command.Command;
import pt.pa.command.CommandAddRoute;
import pt.pa.command.CommandManager;
import pt.pa.command.CommandRemoveRoute;
import pt.pa.graph.Vertex;
import pt.pa.model.BusNetwork;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.view.BusNetworkView;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusNetworkController {
    private BusNetworkView view;
    private BusNetwork model;
    private TicketStrategy ticketPrinter;
    private CommandManager manager;

    public BusNetworkController(BusNetworkView view, BusNetwork model) {
        this.view = view;
        this.model = model;
        this.view.setTriggers(this);
        this.model.addObserver(view);
        ticketPrinter = new SimpleTicket();
        manager = new CommandManager();
    }

    public void setTicketStrategy(TicketStrategy strat){
        ticketPrinter = strat;
    }
    public void printTicket(Vertex<Stop> orig, Vertex<Stop> dest) throws IOException {
        ticketPrinter.printTicket(model.path(orig, dest));
    }

    public void addRoute(List<SmartGraphVertex<Stop>> selectedVertices, int distance, int duration) throws InvalidAttributeValueException {
        Command c = new CommandAddRoute(model, selectedVertices, distance, duration);
        manager.executeCommand(c);

    }

    public int numberOfStops(){
        return model.numberOfStops();
    }

    public int numberOfRoutes(){
        return model.numberOfRoutes();
    }

    public List<Map.Entry<Vertex<Stop>, Integer>> StopsCentrality(){
        return model.StopsCentrality();
    }

    public List<Map.Entry<Vertex<Stop>, Integer>> TopStopsCentrality(){
        return model.TopStopsCentrality();
    }

    public int getSubNetWorkOut(){
        return model.subNetworks().size();
    }

    public void removeRoute(SmartGraphEdge<Route, Stop> selectedEdge) throws InvalidAttributeValueException {
        Command c = new CommandRemoveRoute(model, selectedEdge);
        manager.executeCommand(c);
    }

    public void undo() {
        manager.undo();
    }

    public void redo() {
        manager.redo();
    }

    public List<Stop> getBestPath(Vertex<Stop> orig, Vertex<Stop> dest, String pathType) {
        List<Stop> path = new ArrayList<>();
        switch (pathType.toLowerCase()){
            case "distancia":
                for (Vertex<Stop> v : model.shortestPath(orig, dest))path.add(v.element());
            break;
            case "tempo":
                for (Vertex<Stop> v : model.fastestPath(orig, dest))path.add(v.element());
            break;
            default:return null;
        }


        return path;
    }
    public List<Stop> getWorstPair(){
        List<Stop> path = new ArrayList<>();
        for (Vertex<Stop> v : model.farthestStopPair())path.add(v.element());
        return path;
    }

    public List<Stop> getNRouthStopsOfDistance(Vertex<Stop> orig, int nRoute){
        List<Stop> path = new ArrayList<>();
        for (Vertex<Stop> v : model.getStopsAtARange(orig, nRoute))path.add(v.element());
        return path;
    }
}

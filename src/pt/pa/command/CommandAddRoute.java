package pt.pa.command;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import pt.pa.graph.Edge;
import pt.pa.model.BusNetwork;
import pt.pa.model.Route;
import pt.pa.model.Stop;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.List;

public class CommandAddRoute implements Command {
    private BusNetwork model;
    private List<SmartGraphVertex<Stop>> vertices;
    private Edge<Route, Stop> selectedEdge;
    private int distance, duration;

    public CommandAddRoute(BusNetwork model, List<SmartGraphVertex<Stop>> vertices, int duration, int distance) {
        this.model = model;
        this.vertices = new ArrayList<>(vertices);
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public void execute() throws InvalidAttributeValueException {
        System.out.println("add route");
        selectedEdge = model.addRoute(vertices.get(0).getUnderlyingVertex().element(), vertices.get(1).getUnderlyingVertex().element(), distance, duration);
    }

    @Override
    public void unExecute() throws InvalidAttributeValueException {
        System.out.println("undo add route");
        model.removeRoute(selectedEdge);
    }
}

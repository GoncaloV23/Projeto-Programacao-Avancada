package pt.pa.command;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import pt.pa.graph.Edge;
import pt.pa.model.BusNetwork;
import pt.pa.model.Route;
import pt.pa.model.Stop;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public class CommandRemoveRoute implements Command {

    private BusNetwork model;
    private List<SmartGraphVertex<Stop>> vertices;
    private Edge<Route, Stop> selectedEdge;
    private Route route;

    public CommandRemoveRoute(BusNetwork model, SmartGraphEdge<Route, Stop> selectedEdge) {
        this.model = model;
        this.selectedEdge = selectedEdge.getUnderlyingEdge();
    }

    @Override
    public void execute() throws InvalidAttributeValueException {
        System.out.println("remove route");
        route = model.removeRoute(selectedEdge);
    }

    @Override
    public void unExecute() throws InvalidAttributeValueException {
        System.out.println("undo remove route");
        selectedEdge = model.addRoute(route.getOrig(), route.getDest(), route.getDistance(), route.getDuration());
    }
}

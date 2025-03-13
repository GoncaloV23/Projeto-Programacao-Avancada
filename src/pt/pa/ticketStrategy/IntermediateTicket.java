package pt.pa.ticketStrategy;

import pt.pa.graph.Vertex;
import pt.pa.model.DijsktraResult;
import pt.pa.model.Stop;

import java.time.LocalDate;
import java.util.List;

public class IntermediateTicket  extends TicketStrategy{

    @Override
    protected String[] generateTicket(DijsktraResult result) {
        List<Vertex<Stop>> path = result.getPath();
        int distance = result.getCost();
        String time = getTime(result.getPathEdges());
        String[] ticket = new String[8];
        ticket[0] = "+--------------------------------------------------------------------------------------------+";
        ticket[1] =  "+                                                                                            +";
        ticket[2] = "+ Data: "+ LocalDate.now() +" "+ path(path)+"+";
        ticket[3] = "+                                                                                            +";
        ticket[4] = "+ Distância: "+ distance +" km +";
        ticket[5] = "+ Duração: "+ time +" +";
        ticket[6] = "+                                                                                            +";
        ticket[7] = "+--------------------------------------------------------------------------------------------+";
        return ticket;
    }
}

package pt.pa.ticketStrategy;

import pt.pa.graph.Vertex;
import pt.pa.model.DijsktraResult;
import pt.pa.model.Stop;

import java.time.LocalDate;
import java.util.List;

public class CompleteTicket extends TicketStrategy{

    @Override
    protected String[] generateTicket(DijsktraResult result) {
        List<Vertex<Stop>> path = result.getPath();
        int size = path.size();
        Vertex<Stop> orig = path.get(0);
        Vertex<Stop> dest = path.get(path.size()-1);
        int distance = result.getCost();
        String time = getTime(result.getPathEdges());
        String[] ticket = new String[9];
        ticket[0] = "+--------------------------------------------------------------------------------------------+";
        ticket[1] =  "+                                                                                            +";
        ticket[2] = "+ Data: "+ LocalDate.now() +" Origem: "+ orig.element().getStop() +" Destino: "+ dest.element().getStop() + " +";
        ticket[3] = "+                                                                                            +";
        ticket[4] = "+ Distância: "+ distance +" km Duração: "+ time +" Nº paragens: "+ size +" +";
        ticket[5] =  "+                                                                                            +";
        ticket[6] = "+ Percurso: "+path(path)+" +";
        ticket[7] = "+                                                                                            +";
        ticket[8] = "+--------------------------------------------------------------------------------------------+";
        return ticket;
    }

}

package pt.pa.ticketStrategy;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import pt.pa.graph.Edge;
import pt.pa.graph.Vertex;
import pt.pa.model.DijsktraResult;
import pt.pa.model.Route;
import pt.pa.model.Stop;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class TicketStrategy {
    static final String DEFAULT_FOLDER = "tickets";
    public void printTicket(DijsktraResult result) throws IOException {
        File folder = new File(DEFAULT_FOLDER);
        folder.mkdir();
        String aux = folder.getAbsolutePath() +"\\ticket"+ ((folder.list()!=null)?folder.list().length:0) + ".pdf";

        pdf(aux, generateTicket(result));
    }
    protected abstract String[] generateTicket(DijsktraResult result);
    protected String getTime(List<Edge<Route, Stop>> edgePath){
        double time = 0;
        for (Edge<Route, Stop> e : edgePath) {
            time *= e.element().getDuration();
        }
        String aux = "" + time/60;
        aux += ":" + time%60/100;
        return aux;
    }
    protected String path(List<Vertex<Stop>> path){
        String result = "<<"+path.get(0).element().getStop()+">> ->";
        for(int i = 1;i<path.size()-1;i++){
            result += path.get(i).element().getStop()+ " -> ";
        }
        result += "<<<" + path.get(0).element().getStop()+">>>";

        return result;
    }

    public void pdf(String path, String[] ticket){

        try {
            PDDocument document = new PDDocument();

            PDPage page = new PDPage();

            document.addPage( page );

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            //Begin the Content stream
            contentStream.beginText();

            //Setting the font to the Content stream
            contentStream.setFont(PDType1Font.COURIER, 9);

            //Setting leading
            contentStream.setLeading(15.5f);

            //Setting the position for the line
            contentStream.newLineAtOffset(25, 700);

            //Adding text in the form of string
            contentStream.showText(ticket[0]);
            for(int i=1;i<ticket.length;i++){
                contentStream.newLine();
                contentStream.showText(ticket[i]);}

            //Ending the content stream
            contentStream.endText();

            //Closing the content stream
            contentStream.close();

            document.save(path);
            System.out.println("PDF Criado");
            document.close();

        }catch (Exception err){
            System.out.println(err.getMessage());
        }

    }
}

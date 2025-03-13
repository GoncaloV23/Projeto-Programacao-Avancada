package pt.pa.utils;

import javafx.scene.image.Image;
import pt.pa.utils.Reader;
import pt.pa.graph.*;
import pt.pa.model.Route;
import pt.pa.model.Stop;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class DatasetImporter {
    private String folderPathName;
    public static final String DEFAULT_FOLDER_TO_IMPORT =".\\datasets\\demo";
    public static final String DEFAULT_IMAGE_TYPE = "dark";

    public DatasetImporter(String folderPathName) {
        this.folderPathName = folderPathName;
    }

    public DatasetImporter() {
        this.folderPathName = DEFAULT_FOLDER_TO_IMPORT;
    }

    public  void importDataset(Graph<Stop, Route> graph){

        importStops(graph);
        importRoutes(graph);

    }

    public Image importImage(String imgType)throws IllegalArgumentException{
        String imageFilepath = folderPathName + "\\img\\" + imgType + ".png";
        FileInputStream input = null;

        try {
            input = new FileInputStream(imageFilepath);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Image type not recognized");
        }

        return new Image(input);
    }
    public Image importImage(){
        return importImage(DEFAULT_IMAGE_TYPE);
    }
    public Map<String, Double[]> importCoordenates(){
        String stopsFile = folderPathName + "\\xy.txt";
        Map<String, Double[]> coordenates = new HashMap<>();

        for(String[] line :getInputLines(stopsFile)){
            try{
                int i = 0;
                String code = line[i++];
                Double[] xyArr = new Double[2];
                xyArr[0] = Double.parseDouble(line[i++]);
                xyArr[1] = Double.parseDouble(line[i]);
                coordenates.put(code, xyArr);
            }catch(ArrayIndexOutOfBoundsException|NumberFormatException e){}
        }

        return coordenates;
    }

    private void importStops(Graph<Stop, Route> graph) {
        String stopsFile = folderPathName + "\\stops.txt";

        for (String[] line : getInputLines(stopsFile)) {
            try{
                String code = line[0];

                String stopName = line[1];

                Double lat = Double.parseDouble(line[2]);
                Double lon = Double.parseDouble(line[3]);

                Stop stop = new Stop(code, stopName, lat, lon);


                graph.insertVertex(stop);
            }catch(ArrayIndexOutOfBoundsException | InvalidVertexException | NumberFormatException e){}
        }

    }
    private void importRoutes(Graph<Stop, Route> graph){
        routeImport(graph);
        routeDuration(graph);
    }

    private void routeImport(Graph<Stop, Route> graph){
        String routeFile = folderPathName + "\\routes-distance.txt";

        for (String[] line : getInputLines(routeFile)){
            try{
                Stop orig = getStop(graph.vertices(), line[0]).element();
                Stop dest = getStop(graph.vertices(), line[1]).element();
                int distance = Integer.parseInt(line[2]);
                int duration = -1;

                Route route = new Route(orig, dest, distance, duration);


                graph.insertEdge(orig, dest, route);
            }catch(ArrayIndexOutOfBoundsException|InvalidVertexException|InvalidEdgeException|NumberFormatException e){}
        }
    }

    private void routeDuration(Graph<Stop, Route> graph){
        String routeFile = folderPathName + "\\routes-duration.txt";

        for (String[] line : getInputLines(routeFile)){
            try {
                Stop a = getStop(graph.vertices(), line[0]).element();
                Stop b = getStop(graph.vertices(), line[1]).element();
                Edge<Route, Stop> edge = getEdge(graph.edges(), a, b);
                if (edge == null) edge = getEdge(graph.edges(), b, a);

                int duration = Integer.parseInt(line[2]);

                if (edge != null) edge.element().setDuration(duration);
            }catch (ArrayIndexOutOfBoundsException|InvalidVertexException|NumberFormatException|InvalidEdgeException e){}
        }
    }
    private Vertex<Stop> getStop(Collection<Vertex<Stop>> vertices, String code){
        for(Vertex<Stop> v : vertices)
            if(v.element().checkStop(code))
                return v;
        throw new InvalidVertexException();
    }

    private Edge<Route, Stop> getEdge(Collection<Edge<Route, Stop>> edges, Stop orig, Stop dest){
        for(Edge<Route, Stop> e: edges)
            if(e.element().checkRoute(orig, dest))
                return e;

        throw new InvalidEdgeException();
    }

    private List<String[]> getInputLines(String filePathName){
        List<String[]> listToReturn = new ArrayList<>();
        int i = 0;
        Reader reader = new Reader(filePathName);
        try{
            reader.open();
            List<String> list = reader.read();
            reader.close();

            for(String line : list) {
                if (line.isEmpty() || line.charAt(0) == '#') continue;
                listToReturn.add(line.split("\t"));//Separar str por espaços
            }
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        return listToReturn;
    }
}

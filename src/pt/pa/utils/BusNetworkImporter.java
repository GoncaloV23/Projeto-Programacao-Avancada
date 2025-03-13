package pt.pa.utils;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;
import pt.pa.model.Route;
import pt.pa.model.Stop;

import java.util.HashMap;
import java.util.Map;

public class BusNetworkImporter {
    private DatasetImporter importer;

    public BusNetworkImporter(String folderPath){
        this.importer = new DatasetImporter(folderPath);
    }

    public void importGraph(Graph<Stop, Route> graph){
        importer.importDataset(graph);
    }

    public Map<Vertex<Stop>, Double[]> getPositions(Graph<Stop, Route> graph){
        Map<Vertex<Stop>, Double[]> positions = new HashMap<>();
        Map<String, Double[]> m = importer.importCoordenates();
        for (Vertex<Stop> v : graph.vertices()) {
            Double[] aux = m.get(v.element().getCode());
            positions.put(v, aux);
        }

        return positions;
    }
    public Background getBackgroundImage(String imageType) throws IllegalArgumentException{

        Image img = importer.importImage(imageType);

        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        return bGround;
    }
}

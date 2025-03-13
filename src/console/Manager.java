package console;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.view.DatasetImporter;
import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;

import java.util.Map;

public class Manager {
    private Graph<Stop, Route> graph;
    private DatasetImporter importer;
    public Manager(Graph<Stop, Route> graph) {
        this.graph = graph;
        this.importer = new DatasetImporter();
    }

    public void setDatasetFolder(String folderPath){
        this.importer = new DatasetImporter(folderPath);
    }
    public void setDefaultDatasetFolder(){
        this.importer = new DatasetImporter();
    }

    public void importGraph(){
        importer.importDataset(this.graph);
    }

    public void setPositions(SmartGraphPanel graphView){
        Map<String, Double[]> m = importer.importCoordenates();
        for (Vertex<Stop> v : graph.vertices()) {
            Double[] aux = m.get(v.element().getCode());
            double x = aux[0];
            double y = aux[1];
            graphView.setVertexPosition(v, x, y);
        }

    }
    public void setImage(SmartGraphPanel graphView, String imageType){
        switch (imageType.toLowerCase()){
            case "dark":
            case "map":
            case "satellite":
            case "terrain":break;
            default:throw new IllegalArgumentException("Image Type not recognized!");
        }
        Image img = importer.importImage(imageType);

        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        graphView.setBackground(bGround);
    }
    public void setImage(SmartGraphPanel graphView){
        setImage(graphView, DatasetImporter.DEFAULT_IMAGE_TYPE);
    }
    public void drawGraph(SmartGraphPanel graphView, String imageType){
        setImage(graphView, imageType);
        setPositions(graphView);

        graphView.update();
    }
    public void drawGraph(SmartGraphPanel graphView){
        drawGraph(graphView, DatasetImporter.DEFAULT_IMAGE_TYPE);
    }
}

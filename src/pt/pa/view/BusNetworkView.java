package pt.pa.view;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.pa.controller.BusNetworkController;
import pt.pa.graph.InvalidEdgeException;
import pt.pa.graph.Vertex;
import pt.pa.model.BusNetwork;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.observerpattern.Observable;
import pt.pa.ticketStrategy.CompleteTicket;
import pt.pa.ticketStrategy.IntermediateTicket;
import pt.pa.ticketStrategy.SimpleTicket;
import pt.pa.utils.BusNetworkImporter;
import pt.pa.utils.DatasetExporter;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusNetworkView extends BorderPane implements BusNetworkUI {
    private BusNetwork model;
    private List<SmartGraphVertex<Stop>> selectedVertices;
    private SmartGraphEdge<Route, Stop> selectedEdge;
    private BusNetworkImporter importer;

    /*CENTER PANEL COMPONENTS*/
    private SmartGraphPanel<Stop, Route> graphView;
    private Pane graphContainer;
    private ScrollPane centerPane;
    private String imageType;

    /*model auxiliars*/
    private String dataSetFolderPathImported = "None";

    /*Right PANEL COMPONENTS*/
    private Label lbHeading;
    private Button btImportGraph;
    private Button btViewGraph;
    private Button btExportGraph;
    private Label lbDataSetFolderPathImported;



    /*BOTTOM PANEL COMPONENTS*/
    private Button btUndo;
    private Button btRedo;
    private Button btAddRoute;
    private Button btRemoveRoute;

    private Button btCalcSmallestTripp;
    private Button btFarthestStops;
    private Button btNRouthStopsOfDistance;
    private ToggleGroup tripTypeGroup;

    private Button btStopsCount;
    private Button btRoutsCount;
    private Button btStopsCentrality;
    private Button btTopStopsCentrality;
    private Button btNetsCount;

    private Button btPrintTicket;
    private ToggleGroup ticketTypeGroup;

    private BorderPane resultsPane;

    public BusNetworkView(BusNetwork model) {
        this.model = model;
        createLayout();
        selectedVertices = new ArrayList<>();
        imageType = "";
    }

    public List<SmartGraphVertex<Stop>> getSelectedVertices() {
        return selectedVertices;
    }
    public SmartGraphEdge<Route, Stop> getSelectedEdges(){
        return selectedEdge;
    }

    @Override
    public void update(Observable subject, Object arg) {
        setUpGraphPanel();
        graphContainer.setBackground(importer.getBackgroundImage(imageType));
        drawGraph();
    }

    public String getInput(String question){
        Label title = new Label(question);
        TextField input = new TextField();
        input.setPrefWidth(300);
        Button btConfirm = new Button("Ok");
        VBox vbox = new VBox(title, input, btConfirm);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Scene root = new Scene(vbox, 400, 150);


        Stage stage = new Stage();
        stage.setTitle("Tipo de imagem");
        stage.setResizable(false);
        stage.setScene(root);

        btConfirm.setOnAction(e ->{
            stage.close();
        });

        stage.showAndWait();

        return input.getText();
    }
    @Override
    public void displayError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("***ERRO***");
        alert.setContentText(msg);
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }
    @Override
    public void setTriggers(BusNetworkController controller) {
        setTriggerBtImportGraph();
        setTriggerbtViewGraph();
        setTriggerBtExportGraph();
        setTriggerBtAddRoute(controller);
        setTriggerBtRemoveRoute(controller);
        setTriggerBtUndo(controller);
        setTriggerBtRedo(controller);
        setTriggerBtStopsCount(controller);
        setTriggerBbtRoutsCount(controller);
        setTriggerBtStopsCentrality(controller);
        setTriggerBtTopStopsCentrality(controller);
        setTriggerBtNetsCount(controller);
        setTriggerBtCalcSmallestTripp(controller);
        setTriggerBtFarthestStops(controller);
        setTriggerBtNRouthStopsOfDistance(controller);
        setTriggerBtPrintTicket(controller);

    }
    private void setTriggerBtImportGraph() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(".\\"));

        btImportGraph.setOnAction(e -> {
            model.resetNetWork();
            File selectedDirectory = directoryChooser.showDialog(new Stage());
            importer = new BusNetworkImporter(selectedDirectory.getAbsolutePath());
            importer.importGraph(model.getNetWork());
            dataSetFolderPathImported = selectedDirectory.getAbsolutePath();
            String[] name = dataSetFolderPathImported.split("\\\\");
            lbDataSetFolderPathImported.setText(name[name.length - 1]);

            centerPane = new ScrollPane();
            setCenter(centerPane);
        });
    }
    private void setTriggerbtViewGraph() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\"));

        btViewGraph.setOnAction(e -> {
            try {
                imageType = getInput("Qual a imagem");
                update(model, null);
                graphContainer.setBackground(importer.getBackgroundImage(imageType));

            } catch (IllegalArgumentException err) {
                displayError("Imagem não existente!");
            }

        });
    }
    private void setTriggerBtExportGraph(){
        btExportGraph.setOnAction(e -> {
            DatasetExporter exporter = new DatasetExporter();
            try {
                exporter.exportDataset(getInput("Nome da Pasta"), model.getNetWork(), graphView, dataSetFolderPathImported);
            }catch (IOException err){
                displayError("Alguma coisa correu mál na exportação da Rede de Autocarros!");
            }
        });
    }
    private void setTriggerBtAddRoute(BusNetworkController controller){
        btAddRoute.setOnAction(e -> {
            int distance = -1;
            int duration = -1;
            try {
                if(getSelectedVertices().size() != 2) throw new InvalidParameterException();

                distance = Integer.parseInt(getInput("What's the distance?"));
                duration = Integer.parseInt(getInput("What's the duration?"));

                controller.addRoute(selectedVertices, distance, duration);

                selectedVertices.clear();

            } catch (InvalidAttributeValueException err) {
                if (distance <= 0) {
                    displayError("Invalid distance!");
                } else if(duration <= 0){
                    displayError("Invalid duration!");
                }
            } catch (InvalidParameterException err){
                update(null, null);
                displayError("You must select 2 stops!");
            }catch (NumberFormatException err){
                if (distance ==-1) {
                    displayError("Invalid distance!");
                } else{
                    displayError("Invalid duration!");
                }
            }catch (InvalidEdgeException err){displayError("You can't have 2 routes between the 2 stops!");}
        });
    }
    private void setTriggerBtRemoveRoute(BusNetworkController controller){
        btRemoveRoute.setOnAction(e -> {
            try {
                if(getSelectedEdges() == null) throw new InvalidParameterException();
                controller.removeRoute(getSelectedEdges());
                selectedEdge = null;
            } catch (InvalidParameterException err){
                displayError("You must select 1 route!");
            } catch (InvalidAttributeValueException err){
                displayError("The route can't be deleted because doesn't exist!");
            }
        });
    }
    private void setTriggerBtUndo(BusNetworkController controller){
        btUndo.setOnAction(e -> {
            controller.undo();
        });
    }
    private void setTriggerBtRedo(BusNetworkController controller){
        btRedo.setOnAction(e -> {
            controller.redo();
        });
    }
    private void setTriggerBtStopsCount(BusNetworkController controller){
        btStopsCount.setOnAction(e -> {
            TextArea results = new TextArea("Resultados: nº de Paragens " + controller.numberOfStops());
            results.setEditable(false);
            results.setPrefSize(400, 100);

            resultsPane.setCenter(results);
        });
    }
    private void setTriggerBbtRoutsCount(BusNetworkController controller){
        btRoutsCount.setOnAction(e -> {
            TextArea results = new TextArea("Resultados: nº de Rotas " + controller.numberOfRoutes());
            results.setEditable(false);
            results.setPrefSize(400, 100);

            resultsPane.setCenter(results);
        });
    }
    private void setTriggerBtStopsCentrality(BusNetworkController controller){
        btStopsCentrality.setOnAction(e -> {
            TextArea results = new TextArea("Resultados: \n" + outputEntrys(controller.StopsCentrality()));
            results.setEditable(false);
            results.setPrefSize(400, 100);

            resultsPane.setCenter(results);
        });
    }
    private void setTriggerBtTopStopsCentrality(BusNetworkController controller){
        btTopStopsCentrality.setOnAction(e -> {
            CategoryAxis xAxis    = new CategoryAxis();
            xAxis.setLabel("Estações");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Adjacentes");

            BarChart  barChart = new BarChart(xAxis, yAxis);
            barChart.setPrefSize(400, 250);

            XYChart.Series dataSeries1 = new XYChart.Series();

            for(Map.Entry<Vertex<Stop>, Integer> map : controller.TopStopsCentrality()){
                dataSeries1.getData().add(new XYChart.Data(map.getKey().element().getCode(), map.getValue()));
            }

            barChart.getData().add(dataSeries1);

            resultsPane.setCenter(barChart);
        });
    }
    private void setTriggerBtNetsCount(BusNetworkController controller){
        btNetsCount.setOnAction(e -> {
            TextArea results = new TextArea("Resultados: nº de redes " + controller.getSubNetWorkOut());
            results.setEditable(false);
            results.setPrefSize(400, 100);

            resultsPane.setCenter(results);
        });
    }
    private void setTriggerBtCalcSmallestTripp(BusNetworkController controller){
        btCalcSmallestTripp.setOnAction(e->{
            TextArea results = new TextArea("Resultados: Nenhum");
            try {
                if(getSelectedVertices().size() != 2) throw new InvalidParameterException();
                RadioButton toggled = (RadioButton)tripTypeGroup.getSelectedToggle();

                String type = toggled.getText();

                List<Stop> path = controller.getBestPath(selectedVertices.get(0).getUnderlyingVertex(),
                        selectedVertices.get(1).getUnderlyingVertex(), type);

                String aux = (type.equalsIgnoreCase("tempo"))? "Caminho mais rápido \n":"Caminho mais curto\n";
                results = new TextArea("Resultados: " + aux + outputPath(path));

            }catch (InvalidParameterException err){
                update(null, null);
                displayError("You must select 2 stops!");
            }catch(NullPointerException err){
                results = new TextArea("Resultados: Não tem;");
            }

            resultsPane.setCenter(results);
            results.setEditable(false);
            results.setPrefSize(400, 100);
        });
    }
    private void setTriggerBtFarthestStops(BusNetworkController controller){
        btFarthestStops.setOnAction(e->{
            String aux = "Caminho entre o par de estações mais distantes\n";
            TextArea results = new TextArea("Resultados: " + aux + outputPath(controller.getWorstPair()));

            resultsPane.setCenter(results);
            results.setEditable(false);
            results.setPrefSize(400, 100);
        });
    }
    private void setTriggerBtNRouthStopsOfDistance(BusNetworkController controller){
        btNRouthStopsOfDistance.setOnAction(e->{
            try {
                if(selectedVertices.size() == 1){
                    int nRoutes = Integer.parseInt(getInput("Nº maximo de estações de distancia"));

                    String aux = "Estações a igual ou menos de " + nRoutes + " estações de distancia\n";

                    Vertex<Stop> selectedVertex = selectedVertices.get(0).getUnderlyingVertex();

                    TextArea results = new TextArea("Resultados: " + aux
                            + outputStops(controller.getNRouthStopsOfDistance(selectedVertex, nRoutes)));

                    resultsPane.setCenter(results);
                    results.setEditable(false);
                    results.setPrefSize(400, 100);
                }else displayError("Tem de selecionar 1 estação!");

            }catch (NumberFormatException err){displayError("Número máximo invalido de estações!");}
            catch (InvalidParameterException err){displayError("Número máximo tem de ser positivo!");}
            catch (IndexOutOfBoundsException err){
                ;
                TextArea results = new TextArea("Não tem!");
                resultsPane.setCenter(results);
                results.setEditable(false);
                results.setPrefSize(400, 100);}
        });
    }
    private void setTriggerBtPrintTicket(BusNetworkController controller){
        btPrintTicket.setOnAction(e->{
            if(ticketTypeGroup.getToggles().get(0).isSelected()) controller.setTicketStrategy(new SimpleTicket());
            if(ticketTypeGroup.getToggles().get(1).isSelected()) controller.setTicketStrategy(new IntermediateTicket());
            if(ticketTypeGroup.getToggles().get(2).isSelected()) controller.setTicketStrategy(new CompleteTicket());

            if(selectedVertices.size()!=2){update(null, null);displayError("You must select 2 stops!");}
            else {
                try {
                    controller.printTicket(selectedVertices.get(0).getUnderlyingVertex(), selectedVertices.get(1).getUnderlyingVertex());
                } catch (IOException ex) {
                    displayError("Someting has gone wrong try again!");
                }
            }
        });
    }

    private String outputEntrys(List<Map.Entry<Vertex<Stop>, Integer>> list){
        String result = "";

        for(Map.Entry<Vertex<Stop>, Integer> map : list){
            result += map.getKey().element().getCode() + " " + map.getValue() + " Paragens incidentes \n";
        }

        return result;
    }
    private String outputPath(List<Stop> stops){
        String result = stops.get(0).getCode();
        for(int i = 1;i<stops.size();i++){
            result += " -> " + stops.get(i).getCode();
        }

        return result;
    }
    private String outputStops(List<Stop> stops){
        String result = "";
        for(int i = 0;i<stops.size();i++){
            result += " - " + stops.get(i).getCode() + "\n";
        }

        return result;
    }
    private void drawGraph(){
        Map<Vertex<Stop>, Double[]> positions = importer.getPositions(model.getNetWork());
        for (Vertex<Stop> vertex: positions.keySet()) {
            Double[] aux = positions.get(vertex);
            if(aux != null) {
                double x = aux[0];
                double y = aux[1];
                graphView.setVertexPosition(vertex, x, y);
            }
        }

        try {
            graphView.update();
        }catch (IllegalStateException e){
            System.err.println(e.getMessage());
        }
    }
    private void createLayout() {

        /* CENTER PANEL*/
        centerPane = new ScrollPane();
        setCenter(centerPane);

        /* RIGHT PANEL */
        setRight(createSidePanel());

        /* BOTTOM PANEL*/
        setBottom(createBottomPanel());


    }
    private void setUpGraphPanel() {
        graphView = new SmartGraphPanel(model.getNetWork());
        graphView.setPrefSize(1000,800);
        graphView.setMinSize(100,80);
        graphView.setMaxSize(1200,960);

        bindDoubleClickOnVertex();

        bindDoubleClickOnEdge();

        graphContainer = new Pane(graphView);
        graphContainer.setPrefSize(1000, 1000);

        centerPane.setContent(graphContainer);

        try {
            graphView.init();
        }catch (IllegalStateException e){
            System.err.println(e.getMessage());
        }
    }
    private void bindDoubleClickOnVertex() {
        graphView.setVertexDoubleClickAction((SmartGraphVertex<Stop> graphVertex) -> {
            if (!selectedVertices.contains(graphVertex)) {
                if (selectedVertices.size() >= 2) {
                    selectedVertices.get(0).setStyle("-fx-stroke: #61B5F1;");;
                    selectedVertices.get(1).setStyle("-fx-stroke: #61B5F1;");;
                    selectedVertices.clear();
                    displayError("Só pode haver no máximo 2 vertices selecionados!");
                } else {
                    selectedVertices.add(graphVertex);
                    graphVertex.setStyle("-fx-stroke: red;");
                }
            } else {
                selectedVertices.remove(graphVertex);
                graphVertex.setStyle("-fx-stroke: #61B5F1;");
            }
        });
    }
    private void bindDoubleClickOnEdge() {
        graphView.setEdgeDoubleClickAction((SmartGraphEdge<Route, Stop> graphEdge) -> {

            if (graphEdge.equals(selectedEdge)) {
                selectedEdge = null;
                graphEdge.setStyle("-fx-stroke: yellow;");
            } else {
                if (selectedEdge != null) selectedEdge.setStyle("-fx-stroke: yellow;");
                selectedEdge = graphEdge;
                graphEdge.setStyle("-fx-stroke: red;");
            }
        });
    }
    private VBox createSidePanel() {

        lbHeading = new Label("    Bus\nNetwork");
        lbHeading.setStyle("-fx-font-weight: bold; -fx-font-size: 40px;");
        lbHeading.setPadding(new Insets(30, 10, 50, 10));

        btImportGraph = new Button("Import");
        btImportGraph.setMinSize(120, 40);

        btViewGraph = new Button("Visualize");
        btViewGraph.setMinSize(120, 40);

        btExportGraph = new Button("Export");
        btExportGraph.setMinSize(120, 40);

        lbDataSetFolderPathImported = new Label("Imported: "+dataSetFolderPathImported);

        VBox panel = new VBox(lbHeading, btImportGraph, btViewGraph, btExportGraph, lbDataSetFolderPathImported);
        panel.setPadding(new Insets(50, 10, 10, 10));
        panel.setSpacing(20);
        panel.setAlignment(Pos.TOP_CENTER);

        return panel;
    }
    private HBox createBottomPanel(){
        VBox vBox0 = createAddRemoveVBox();

        VBox vBox1 = createCalcSmallestTrippVBox();

        VBox vBox2 = createCalcRoutesButtons(vBox1);

        VBox vBox3 = createCalcStatsVBox();

        VBox vBox4 = createTicketVBox();

        TextArea results = createTextArea();

        resultsPane = new BorderPane();
        resultsPane.setCenter(results);

        HBox bottomPane = new HBox(vBox0, vBox2, vBox3, vBox4, resultsPane);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setSpacing(30);

        return bottomPane;
    }
    private VBox createAddRemoveVBox() {
        btUndo = new Button("<--");
        btUndo.setPrefSize(60, 20);

        btRedo = new Button("-->");
        btRedo.setPrefSize(60, 20);

        btAddRoute = new Button("Add");
        btAddRoute.setPrefSize(60, 20);

        btRemoveRoute = new Button("Remove");
        btRemoveRoute.setPrefSize(60, 20);

        VBox vBox0 = new VBox(new HBox(btUndo, btRedo), new HBox(btAddRoute, btRemoveRoute));
        vBox0.setSpacing(30);

        return vBox0;
    }
    private VBox createCalcSmallestTrippVBox() {
        btCalcSmallestTripp = new Button("Caminho mais curto");
        setNode(btCalcSmallestTripp, 140, 40);

        tripTypeGroup = new ToggleGroup();

        RadioButton radTrip1 = new RadioButton("Distancia");
        radTrip1.setToggleGroup(tripTypeGroup);


        RadioButton radTrip2 = new RadioButton("Tempo");
        radTrip2.setToggleGroup(tripTypeGroup);

        VBox vBox1 = new VBox(radTrip1, radTrip2);
        vBox1.setSpacing(10);


        tripTypeGroup.selectToggle(radTrip1);

        return vBox1;

    }
    private VBox createCalcRoutesButtons(VBox vBox1) {
        btFarthestStops = new Button("Paragens mais\ndistantes");
        setNode(btFarthestStops, 140, 40);

        btNRouthStopsOfDistance = new Button("Lista de paragens\n N rotas de distancia");
        setNode(btNRouthStopsOfDistance, 140, 40);


        HBox hbox = new HBox(btCalcSmallestTripp, vBox1);
        hbox.setSpacing(10);

        VBox vBox2 = new VBox(hbox, btFarthestStops, btNRouthStopsOfDistance);
        vBox2.setSpacing(10);

        return vBox2;
    }
    private VBox createCalcStatsVBox() {
        btStopsCount = new Button("Nº Paragens");
        setNode(btStopsCount, 140, 20);

        btRoutsCount = new Button("Nº Rotas");
        setNode(btRoutsCount, 140, 20);

        btStopsCentrality = new Button("Centralidade");
        setNode(btStopsCentrality, 140, 20);

        btTopStopsCentrality = new Button("Top Centr.");
        setNode(btTopStopsCentrality, 140, 20);

        btNetsCount = new Button("Nº Redes");
        setNode(btNetsCount, 140, 20);

        VBox vBox3 = new VBox(btStopsCount, btRoutsCount, btStopsCentrality, btTopStopsCentrality, btNetsCount);
        vBox3.setSpacing(0);

        return vBox3;
    }
    private void setNode(Labeled node, int prefWidth, int prefHeight){
        node.setPrefSize(prefWidth, prefHeight);
        node.setStyle("-fx-font-size: 10px;");
    }
    private VBox createTicketVBox() {
        btPrintTicket = new Button("Bilhete");
        btPrintTicket.setPrefSize(100, 40);

        ticketTypeGroup = new ToggleGroup();

        RadioButton rad1 = new RadioButton("Simples");
        rad1.setToggleGroup(ticketTypeGroup);

        RadioButton rad2 = new RadioButton("Intermédio");
        rad2.setToggleGroup(ticketTypeGroup);

        RadioButton rad3 = new RadioButton("Completo");
        rad3.setToggleGroup(ticketTypeGroup);

        VBox vBox4 = new VBox(btPrintTicket, rad1, rad2, rad3);
        vBox4.setSpacing(10);


        ticketTypeGroup.selectToggle(rad1);
        return vBox4;
    }
    private TextArea createTextArea() {
        TextArea results = new TextArea("Resultados: Nenhum");
        results.setEditable(false);
        results.setPrefSize(400, 100);

        return results;
    }
}

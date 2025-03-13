package pt.pa.command.console;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.model.Route;
import pt.pa.model.Stop;

import java.util.Scanner;

public class ConsoleMenu {
    private Manager manager;
    private Scanner inputReader;
    private Graph<Stop, Route> graph;
    public ConsoleMenu(Stage primaryStage) {
        this.graph = new GraphAdjacencyList<>();
        this.manager = new Manager(this.graph);

        inputReader = new Scanner(System.in);
        clearConsole();
        mainMenu(primaryStage);
    }
    public void mainMenu(Stage primaryStage){
        String option = "";
        while(!option.equals("S")){

            System.out.print("************Menu**************\n" +
                    "1 - Carregar Grafo Default\n" +
                    "2 - Carregar Grafo especifico\n" +
                    "S - Sair\n");
            option = getOption().toUpperCase();
            switch(option){
                case "1": clearConsole();
                    manager.setDefaultDatasetFolder();
                    manager.importGraph();
                    showGraph(primaryStage, getInput("Qual tipo de imagem pretendido"));
                    option = "S";
                    break;
                case "2": clearConsole();
                    String folderPath = getInput("Qual o Caminho para a pasta?");
                    manager.setDatasetFolder(folderPath);
                    manager.importGraph();
                    showGraph(primaryStage, getInput("Qual tipo de imagem pretendido"));
                    option = "S";
                    break;
                case "S": break;
                default: clearConsole();System.out.println("\nOpção não reconhecida.");
            }
        }
    }
    /**
     * Método mostra o grafo importado numa janela..
     */
    private void showGraph(Stage primaryStage, String imageType) {
        SmartGraphPanel graphView = new SmartGraphPanel(graph);
        graphView.setMinSize(1200,900);

        SmartGraphDemoContainer graphContainer = new SmartGraphDemoContainer(graphView);



        BorderPane root = new BorderPane();
        root.setCenter(graphContainer);
        Scene scene = new Scene(root, 1200, 768);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Projeto PA 2223 - Bus Network");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

        graphView.init();

       manager.drawGraph(graphView, imageType);
    }

    /**
     * Método lê o input do utilizador.
     * @return String lida.
     */
    private String getOption(){
        System.out.print("Opção>");
        return inputReader.nextLine();
    }
    /**
     * Dá 30 quebras de linha simulando assim a limpesa da consola.
     */
    private void clearConsole(){
        for(int i=0;i<10;i++){System.out.println("\n\n\n");}
    }
    /**
     * Confirma uma escolha pelo utilizador.
     * @return boolean
     */
    private boolean getConfirmation(){
        System.out.println("Tesn a certesa desta opção? (Y se sim)");
        return getOption().equals("Y");
    }
    /**
     * Faz uma pergunta ao utilizador e lê a sua resposta.
     * @param question
     * @return
     */
    private String getInput(String question){
        while(true){
            System.out.println("\n"+question);
            String response = getOption();
            if(response!=null && !response.isEmpty())return response;
        }
    }
}

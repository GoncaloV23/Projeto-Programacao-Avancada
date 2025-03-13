package pt.pa.utils;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.Vertex;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.utils.Writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DatasetExporter {
    private File curExportingDirectory;
    public static final String DEFAULT_FOLDER_TO_EXPORT ="datasets";
    private Writer writer;

    public DatasetExporter() {
        this.curExportingDirectory = null;
        writer = new Writer(".\\DEFAULT_FOLDER_TO_EXPORT");
    }
    public File createDirectory(String folderName){
        String dirPath = DEFAULT_FOLDER_TO_EXPORT + "\\" + folderName;
        //System.out.println(dirPath);
        this.curExportingDirectory = new File(dirPath);
        this.curExportingDirectory.mkdir();

        return this.curExportingDirectory;
    }
    public String getCurExportingDirectoryName(){
        return getFileName(curExportingDirectory);
    }
    public String getFileName(File file){
        String[] aux = file.getAbsolutePath().split("\\\\");
        return aux[aux.length - 1];
    }
    public void exportDataset(String folderName, Graph<Stop, Route> graph, SmartGraphPanel<Stop, Route> graphView, String folderPath)throws IOException{
        try {
            createDirectory(folderName);

            exportImagesFolder(new File(folderPath));
            exportStopsFileContent(graph);
            exportXYFileContent(graphView, graph);
            exportRoutesFilesContent(graph);
        }catch (IOException e){throw e;}
    }

    public void exportStopsFileContent(Graph<Stop, Route> graph) throws IOException {

        String result = "#\n# data file: " + getCurExportingDirectoryName() + "-stops\n#\nstop_code\tstop_name\tlat\tlon\n";
        for(Vertex<Stop> vertex : graph.vertices()){
            Stop stop = vertex.element();
            result += stop.getCode() + "\t" + stop.getStop() + "\t" + stop.getLatitude() + "\t" + stop.getLongitude() + "\n";
        }
        writer.write(curExportingDirectory.getAbsolutePath() + "\\stops.txt", result);
    }

    public void exportXYFileContent(SmartGraphPanel<Stop, Route> graphView, Graph<Stop, Route> graph) throws IOException {
        String result = "#\n# data file: " + getCurExportingDirectoryName() + "-positions XY\n#\nstop_code\tx\ty\n";
        for(Vertex<Stop> vertex : graph.vertices()){
            result += vertex.element().getCode() + "\t"
                    + graphView.getVertexPositionX(vertex) + "\t"
                    + graphView.getVertexPositionY(vertex) + "\n";
        }
        writer.write(curExportingDirectory.getAbsolutePath() + "\\xy.txt", result);
    }
    public void exportRoutesFilesContent(Graph<Stop, Route> graph) throws IOException {
        String durations = "#\n# data file: " + getCurExportingDirectoryName() + "-positions XY\n#\nstop_code\tx\ty\n";
        String distances = "#\n# data file: " + getCurExportingDirectoryName() + "-positions XY\n#\nstop_code\tx\ty\n";
        for(Edge<Route, Stop> edge : graph.edges()){
            durations += edge.element().getOrig() + "\t"
                    + edge.element().getDest() + "\t"
                    + edge.element().getDuration() + "\n";

            distances += edge.element().getOrig() + "\t"
                    + edge.element().getDest() + "\t"
                    + edge.element().getDistance() + "\n";
        }
        writer.write(curExportingDirectory.getAbsolutePath() + "\\routes-duration.txt", durations);
        writer.write(curExportingDirectory.getAbsolutePath() + "\\routes-distance.txt", distances);
    }

    public void exportImagesFolder(File folder) throws IOException {
        File sourceImgFolder = new File(folder.getAbsolutePath()+"\\img");
        File destImgFolder = new File(curExportingDirectory.getAbsolutePath()+"\\img");

        Path source = Paths.get(sourceImgFolder.getAbsolutePath());
        Path dest = Paths.get(destImgFolder.getAbsolutePath());

        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        File[] imgs = sourceImgFolder.listFiles();
        for(int i=0; i<imgs.length; i++){
            source = Paths.get(imgs[i].getAbsolutePath());
            dest = Paths.get(destImgFolder.getAbsolutePath() + "\\"+getFileName(imgs[i]));
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        }

    }
}

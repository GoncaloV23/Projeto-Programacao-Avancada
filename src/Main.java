
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.controller.BusNetworkController;
import pt.pa.model.BusNetwork;
import pt.pa.view.BusNetworkView;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ignored) throws Exception {
        BusNetwork model = new BusNetwork();
        BusNetworkView view = new BusNetworkView(model);
        BusNetworkController controller = new BusNetworkController(view, model);

        Scene scene = new Scene(view, 1200, 800);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Bus Network");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();

    }

}

import com.drink.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        // 关键：给控制器传入主窗口，模态要用
        MainController controller = loader.getController();
        controller.setMainStage(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("饮料管理系统");
        primaryStage.centerOnScreen();;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
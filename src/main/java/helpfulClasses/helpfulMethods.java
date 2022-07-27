package helpfulClasses;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicReference;

public class helpfulMethods {
    public static void setStageUndecorated(Stage stage){
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public static void addDragging(Stage stage, Scene scene){
        scene.setCursor(Cursor.DEFAULT);
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        scene.setOnMousePressed(mouseEvent -> {
            xOffset.set(stage.getX() - mouseEvent.getScreenX());
            yOffset.set(stage.getY() - mouseEvent.getScreenY());
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + xOffset.get());
            stage.setY(mouseEvent.getScreenY() + yOffset.get());
            stage.setOpacity(.8);

            scene.setCursor(Cursor.CLOSED_HAND);
        });
        scene.setOnMouseMoved(mouseMovedEvent -> {
            stage.setOpacity(1);
            scene.setCursor(Cursor.DEFAULT);
        });
    }
}

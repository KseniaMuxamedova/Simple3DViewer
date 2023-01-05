package com.cgvsu;

import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.RenderEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.*;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {
    private final static boolean willItWriteInformationToConsole = true;

    final private float TRANSLATION = 0.5F;
    private JLabel labelImg;

    private JSpinner spinnerSize;

    private JSpinner spinnerLevelCount;

    private JFileChooser fileChooserSave;

    @FXML
    private ComboBox listOfModels;


    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML

    private ArrayList<Model> models = new ArrayList<>();
    private ArrayList<String> modelsNames = new ArrayList<>();




    private Camera camera = new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        listOfModels.setLayoutX(250);
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));
            if (models != null) {
                listOfModels.setItems(FXCollections
                        .observableArrayList(listToArr(modelsNames)));
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, models, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    FileChooser fileChooser = new FileChooser();


    @FXML
    private void onOpenModelMenuItemClick() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");


        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());

        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            models.add(ObjReader.read(fileContent, willItWriteInformationToConsole));
            modelsNames.add(file.getName());
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    private Object[] listToArr(ArrayList<String> arrayList){
        String[] arr = new String[arrayList.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = arrayList.get(i);
        }
        return arr;
    }


    @FXML
    private void onWriteModelMenuItemClick() throws IOException {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");
        fileChooser.setInitialFileName("Saved Model");
        try {
            File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
            ObjWriter.write(file.getAbsolutePath(), models.get(models.size() - 1));
            // todo: обработка ошибок
        } catch (Exception exception) {

        }
    }

    public void onOpenModelFillingPolygons(){

    }





    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}
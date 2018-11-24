package com;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Main extends Application {
    private PerspectiveCamera camera;
    private final double cameraModifier = 50.0;
    private final double cameraQuantity = 10.0;
    private final double sceneWidth = 600.0;
    private final double sceneHeight = 600.0;
    private double mouseXold = 0.0;
    private double mouseYold = 0.0;
    private final double cameraYlimit = 15.0;
    private final double rotateModifier = 25.0;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(50000.0);
        camera.setTranslateZ(-1000);

        Cylinder cylinder = new Cylinder(50, 100);
        PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKGREEN);
        blueMaterial.setSpecularColor(Color.BLUE);
        cylinder.setMaterial(blueMaterial);
        cylinder.setRotationAxis(Rotate.X_AXIS);
        cylinder.setRotate(45);


        Group root = new Group();
        root.getChildren().add(cylinder);
        primaryStage.setTitle("3D");
        Scene scene = new Scene(root, 500, 400);
        scene.setCamera(camera);
        scene.setFill(Color.YELLOW);
        scene.setOnMouseClicked(event -> {
            Node picked = event.getPickResult().getIntersectedNode();
            if (null != picked) {
                double scalar = 2;
                if (picked.getScaleX() > 1)
                    scalar = 1;
                picked.setScaleX(scalar);
                picked.setScaleY(scalar);
                picked.setScaleZ(scalar);
            }
        });

        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            if (event.isShiftDown()) {
                change = cameraModifier;
            }
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.W) {
                camera.setTranslateZ(camera.getTranslateZ() + change);
            }
            if (keyCode == KeyCode.S) {
                camera.setTranslateZ(camera.getTranslateZ() - change);
            }
            if (keyCode == KeyCode.A) {
                camera.setTranslateX(camera.getTranslateX() + change);
            }
            if (keyCode == KeyCode.D) {
                camera.setTranslateX(camera.getTranslateX() - change);
            }
        });

        Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        camera.getTransforms().addAll(xRotate, yRotate);
        scene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED ||
                    event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                double mouseXnew = event.getSceneX();
                double mouseYnew = event.getSceneY();
                if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    double pitchRotate = xRotate.getAngle() + (mouseYnew - mouseYold) / rotateModifier;
                    pitchRotate = pitchRotate > cameraYlimit ? cameraYlimit : pitchRotate;
                    pitchRotate = pitchRotate < -cameraYlimit ? -cameraYlimit : pitchRotate;
                    xRotate.setAngle(pitchRotate);
                    double yawRotate = yRotate.getAngle() - (mouseXnew - mouseXold) / rotateModifier;
                    yRotate.setAngle(yawRotate);
                }
                mouseXold = mouseXnew;
                mouseYold = mouseYnew;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}













































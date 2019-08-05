package de.joshuaschulz.space;

import de.joshuaschulz.space.objects.Xform;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PlanetsController implements Initializable {
    public BorderPane anchor;
    public SubScene subScene;
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Group animationGroup = new Group();
    final Group axisGroup = new Group();
    final Xform world = new Xform();
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    final double cameraDistance = 4000;
    final Xform moleculeGroup = new Xform();
    private Timeline timeline;
    boolean timelinePlaying = false;
    double CONTROL_MULTIPLIER = 0.1;
    double SHIFT_MULTIPLIER = 0.1;
    double ALT_MULTIPLIER = 0.5;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildScene();
        buildCamera();
        buildAxes();
        buildMolecule();
        subScene = new SubScene(animationGroup,1464*2,1000*2,true,SceneAntialiasing.DISABLED);
        handleKeyboard(subScene, world);
        handleMouse(subScene, world);
        subScene.setCamera(camera);
        anchor.setCenter(subScene);
    }


    private void buildScene() {
        System.out.println("buildScene");
        animationGroup.getChildren().add(world);
    }

    private void buildCamera() {
        animationGroup.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-cameraDistance);
        cameraXform.ry.setAngle(0.0);
        cameraXform.rx.setAngle(0);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
    }

    private void buildMolecule() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.YELLOW);
        redMaterial.setSpecularColor(Color.LIGHTYELLOW);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        // Molecule Hierarchy
        // [*] systemXform
        //     [*] sunXform
        //         [*] sunSphere
        //     [*] mercurySideXform
        //         [*] mercuryXform
        //             [*] mercurySphere
        //     [*] venusSideXform
        //         [*] venusXform
        //             [*] venusSphere

        Xform systemXform = new Xform();
        Xform sunXform = new Xform();
        Xform mercurySideXform = new Xform();
        Xform mercuryXform = new Xform();
        Xform venusSideXform = new Xform();
        Xform venusXform = new Xform();
        Xform earthSideXform = new Xform();
        Xform earthXform = new Xform();
        Xform marsSideXform = new Xform();
        Xform marsXform = new Xform();
        Xform jupiterSideXform = new Xform();
        Xform jupiterXform = new Xform();
        Xform saturnSideXform = new Xform();
        Xform saturnXform = new Xform();
        Xform uranusSideXform = new Xform();
        Xform uranusXform = new Xform();
        Xform neptuneSideXform = new Xform();
        Xform neptuneXform = new Xform();

        Sphere sunSphere = new Sphere(40.0);
        sunSphere.setMaterial(redMaterial);

        Circle c = new Circle(50.0);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.AQUA);

        Sphere mercurySphere = new Sphere(5.0);
        mercurySphere.setMaterial(whiteMaterial);
        mercurySphere.setTranslateZ(0.0);

        Sphere venusSphere = new Sphere(12.0);
        venusSphere.setMaterial(whiteMaterial);
        venusSphere.setTranslateZ(0.0);

        Sphere earthSphere = new Sphere(13.0);
        earthSphere.setMaterial(whiteMaterial);
        earthSphere.setTranslateZ(0.0);

        Sphere marsSphere = new Sphere(7.0);
        marsSphere.setMaterial(whiteMaterial);
        marsSphere.setTranslateZ(0.0);

        Sphere jupiterSphere = new Sphere(143.0);
        jupiterSphere.setMaterial(whiteMaterial);
        jupiterSphere.setTranslateZ(0.0);

        Sphere saturnSphere = new Sphere(125.0);
        saturnSphere.setMaterial(whiteMaterial);
        saturnSphere.setTranslateZ(0.0);

        Sphere uranusSphere = new Sphere(51.0);
        uranusSphere.setMaterial(whiteMaterial);
        uranusSphere.setTranslateZ(0.0);

        Sphere neptuneSphere = new Sphere(50.0);
        neptuneSphere.setMaterial(whiteMaterial);
        neptuneSphere.setTranslateZ(0.0);

        systemXform.getChildren().add(sunXform);
        systemXform.getChildren().add(mercurySideXform);
        systemXform.getChildren().add(venusSideXform);
        systemXform.getChildren().add(earthSideXform);
        systemXform.getChildren().add(marsSideXform);
        systemXform.getChildren().add(jupiterSideXform);
        systemXform.getChildren().add(saturnSideXform);
        systemXform.getChildren().add(uranusSideXform);
        systemXform.getChildren().add(neptuneSideXform);
        sunXform.getChildren().add(sunSphere);
        sunXform.getChildren().add(c);
        mercurySideXform.getChildren().add(mercuryXform);
        venusSideXform.getChildren().add(venusXform);
        earthSideXform.getChildren().add(earthXform);
        marsSideXform.getChildren().add(marsXform);
        jupiterSideXform.getChildren().add(jupiterXform);
        saturnSideXform.getChildren().add(saturnXform);
        uranusSideXform.getChildren().add(uranusXform);
        neptuneSideXform.getChildren().add(neptuneXform);

        mercuryXform.getChildren().add(mercurySphere);
        venusXform.getChildren().add(venusSphere);
        earthXform.getChildren().add(earthSphere);
        marsXform.getChildren().add(marsSphere);
        jupiterXform.getChildren().add(jupiterSphere);
        saturnXform.getChildren().add(saturnSphere);
        uranusXform.getChildren().add(uranusSphere);
        neptuneXform.getChildren().add(neptuneSphere);


        mercuryXform.setTx(100.0);
        venusXform.setTx(181.25);
        earthXform.setTx(300.0);
        marsXform.setTx(381.25);
        jupiterXform.setTx(1300.0);
        saturnXform.setTx(2400.0);
        uranusXform.setTx(4800.0);
        neptuneXform.setTx(7525.0);
        //venusSideXform.setRotateY(104.5);

        moleculeGroup.getChildren().add(systemXform);

        world.getChildren().addAll(moleculeGroup);
    }

    private void handleMouse(SubScene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;
                double modifierFactor = 0.1;

                if (me.isControlDown()) {
                    modifier = 0.1;
                }
                if (me.isShiftDown()) {
                    modifier = 10.0;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*modifierFactor*modifier*2.0);  // +
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*modifierFactor*modifier*2.0);  // -
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*modifierFactor*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*modifierFactor*modifier*0.3);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*modifierFactor*modifier*0.3);  // -
                }
            }
        });
    }

    private void handleKeyboard(SubScene scene, final Node root) {
        final boolean moveCamera = true;
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Duration currentTime;
                switch (event.getCode()) {
                    case Z:
                        if (event.isShiftDown()) {
                            cameraXform.ry.setAngle(0.0);
                            cameraXform.rx.setAngle(0.0);
                            camera.setTranslateZ(-300.0);
                        }
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        break;
                    case X:
                        if (event.isControlDown()) {
                            if (axisGroup.isVisible()) {
                                System.out.println("setVisible(false)");
                                axisGroup.setVisible(false);
                            }
                            else {
                                System.out.println("setVisible(true)");
                                axisGroup.setVisible(true);
                            }
                        }
                        break;
                    case S:
                        if (event.isControlDown()) {
                            if (moleculeGroup.isVisible()) {
                                moleculeGroup.setVisible(false);
                            }
                            else {
                                moleculeGroup.setVisible(true);
                            }
                        }
                        break;
                    case SPACE:
                        if (timelinePlaying) {
                            timeline.pause();
                            timelinePlaying = false;
                        }
                        else {
                            timeline.play();
                            timelinePlaying = true;
                        }
                        break;
                    case UP:
                        if (event.isControlDown() && event.isShiftDown()) {
                            cameraXform2.t.setY(cameraXform2.t.getY() - 10.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown() && event.isShiftDown()) {
                            cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 10.0*ALT_MULTIPLIER);
                        }
                        else if (event.isControlDown()) {
                            cameraXform2.t.setY(cameraXform2.t.getY() - 1.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown()) {
                            cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 2.0*ALT_MULTIPLIER);
                        }
                        else if (event.isShiftDown()) {
                            double z = camera.getTranslateZ();
                            double newZ = z + 5.0*SHIFT_MULTIPLIER;
                            camera.setTranslateZ(newZ);
                        }
                        break;
                    case DOWN:
                        if (event.isControlDown() && event.isShiftDown()) {
                            cameraXform2.t.setY(cameraXform2.t.getY() + 10.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown() && event.isShiftDown()) {
                            cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 10.0*ALT_MULTIPLIER);
                        }
                        else if (event.isControlDown()) {
                            cameraXform2.t.setY(cameraXform2.t.getY() + 1.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown()) {
                            cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0*ALT_MULTIPLIER);
                        }
                        else if (event.isShiftDown()) {
                            double z = camera.getTranslateZ();
                            double newZ = z - 5.0*SHIFT_MULTIPLIER;
                            camera.setTranslateZ(newZ);
                        }
                        break;
                    case RIGHT:
                        if (event.isControlDown() && event.isShiftDown()) {
                            cameraXform2.t.setX(cameraXform2.t.getX() + 10.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown() && event.isShiftDown()) {
                            cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 10.0*ALT_MULTIPLIER);
                        }
                        else if (event.isControlDown()) {
                            cameraXform2.t.setX(cameraXform2.t.getX() + 1.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown()) {
                            cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 2.0*ALT_MULTIPLIER);
                        }
                        break;
                    case LEFT:
                        if (event.isControlDown() && event.isShiftDown()) {
                            cameraXform2.t.setX(cameraXform2.t.getX() - 10.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown() && event.isShiftDown()) {
                            cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 10.0*ALT_MULTIPLIER);  // -
                        }
                        else if (event.isControlDown()) {
                            cameraXform2.t.setX(cameraXform2.t.getX() - 1.0*CONTROL_MULTIPLIER);
                        }
                        else if (event.isAltDown()) {
                            cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0*ALT_MULTIPLIER);  // -
                        }
                        break;
                }
            }
        });
    }
}

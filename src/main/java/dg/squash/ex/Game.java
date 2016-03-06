package dg.squash.ex;

import dg.squash.ecs.Entity;
import dg.squash.ecs.EntityEngine;
import dg.squash.ecs.components.InputComponent;
import dg.squash.ecs.components.NodeComponent;
import dg.squash.ecs.components.ShapeComponent;
import dg.squash.ecs.components.VelocityComponent;
import dg.squash.events.InputListener;
import dg.squash.main.AnimationCreator;
import dg.squash.main.Initiator;
import dg.squash.math.Vector2D;
import dg.squash.shape.CircleShape;
import dg.squash.utils.TwoTuple;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Scene scene;
    private final Group visualDimension;
    private final EntityEngine logicDimension;
    private List<Entity> entityBin = new ArrayList<>();
    private AnimationTimer worldTime;
    private boolean isRunning = false;
    private final static int TOTAL_ENEMIES = 50;
    private int destroyedEnemies;
    private final double width;
    private final double height;
    private SimpleIntegerProperty totalScoreProperty;
    private SimpleDoubleProperty progress;


    public Game(double width, double height) {
        this.visualDimension = new Group();
        this.scene = new Scene(visualDimension, width, height);
        this.width = width;
        this.height = height;
        this.logicDimension = new EntityEngine();
        init();
    }

    public List<Entity> getEntityBin() {
        return entityBin;
    }

    private void initVisualDimension() {
        for (Entity e : logicDimension.getEntities()) {
            if (e.hasComponent(NodeComponent.class))
                visualDimension.getChildren().add(e.getComponent(NodeComponent.class).show());
        }
    }

    public void increaseScore(int increaseBy) {
        int totalScore = totalScoreProperty.get();
        totalScoreProperty.set(totalScore + increaseBy);
    }



    public void init() {
        destroyedEnemies = 0;
        totalScoreProperty = new SimpleIntegerProperty(this, "totalScore", 0);
        progress = new SimpleDoubleProperty(this, "progress", 0);
        createEntities();
        initVisualDimension();
    }

    private void cleanBin() {
        for (Entity entity : entityBin) {
            logicDimension.removeEntity(entity);
            visualDimension.getChildren().remove(entity.getComponent(NodeComponent.class).show());
        }
    }

    public Group getVisualDimension() {
        return visualDimension;
    }

    public void bigBang() {
        this.worldTime = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                gameWorldLaws.applyRules(logicDimension);
                cleanBin();
            }
        };
        worldTime.start();
        isRunning = true;
    }

    public void stopTime() {
        worldTime.stop();
        isRunning = false;
    }

    public void reset() {
        logicDimension.getEntities().clear();
        visualDimension.getChildren().clear();
        getEntityBin().clear();
        init();
        AnimationCreator.timerAnimation(this).play();
    }

    public void resumeTime() {
        worldTime.start();
        isRunning = true;
    }

    public Scene getScene() {
        return scene;
    }

    public boolean isRunning() {
        return isRunning;
    }







}

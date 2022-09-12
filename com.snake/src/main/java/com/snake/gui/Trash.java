package com.snake.gui;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class Trash extends Application {
	
	int sceneX = 600;
	int sceneY = 400;
	int size = 10;
	int currentDirection;
	int score = 0;
	double animationRate = 1.00;
	boolean gameOver = false;
	ArrayList <Rectangle> snake = new ArrayList<>();
	Rectangle food = new Rectangle(size-1, size-1, Color.BLUE);
	Rectangle dummy;
	Pane pane;
	Timeline timeline;
	Text scoreBoard;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;    
	
	public Node sceneGraph() {
        pane = new Pane();
        pane.setStyle("-fx-background-color:gray;");
        pane.setPrefSize(sceneX, sceneY);
        createSnake();
        createFood();
        createDummy();
		pane.getChildren().add(food);
        return pane;		
	}
	
	private void run() {	
		if (gameOver) {
			Text gameOver = new Text(sceneX/5.5, sceneY/2, "GAME OVER");
			gameOver.setFill(Color.RED);
			gameOver.setFont(Font.font(STYLESHEET_CASPIAN, 70));
			pane.getChildren().add(gameOver);
			System.out.println(snake.get(0).getLayoutX()+", "+snake.get(0).getLayoutY());
			pane.getChildren().removeAll(snake.get(0), snake.get(1));
			timeline.stop();
//			return;
		}
		for (int i = snake.size()-1; i>=1; i--) {
			snake.get(i).setLayoutX(snake.get(i-1).getLayoutX());
			snake.get(i).setLayoutY(snake.get(i-1).getLayoutY());
		}	
				
		eatFood();

        switch (currentDirection) {
        case RIGHT:
        	snake.get(0).setLayoutX(snake.get(0).getLayoutX()+size);
        	break;
        case LEFT:
        	snake.get(0).setLayoutX(snake.get(0).getLayoutX()-size);
            break;
        case UP:
        	snake.get(0).setLayoutY(snake.get(0).getLayoutY()-size);
            break;
        case DOWN:
        	snake.get(0).setLayoutY(snake.get(0).getLayoutY()+size);
            break;
    }
		gameOver();


	}
	
	private void createSnake() {
        for (int i = 0; i < 3; i++) {
            dummy = new Rectangle(size-1, size-1, Color.BLACK);
            // Position the shapes in the Pane using the layoutX/Y properties.
            dummy.setLayoutX(100);
            dummy.setLayoutY(300);
            dummy.setArcHeight(size-5);
            dummy.setArcWidth(size-5);
            pane.getChildren().add(dummy);
            snake.add(dummy);
        }
	}
	
	private void createFood() {
		food.setLayoutX(Math.round(Math.random()*(sceneX-size)/size)*size);
		food.setLayoutY(Math.round(Math.random()*(sceneY-size)/size)*size);
		food.setArcHeight(5);
		food.setArcWidth(5);
	}
	
	private void createDummy() {
		dummy = new Rectangle(size-1, size-1, Color.BLACK);
		dummy.setLayoutX(-100);
		dummy.setLayoutY(-100);
		dummy.setArcHeight(size-5);
		dummy.setArcWidth(size-5);
		pane.getChildren().add(dummy);
	}		
	
	private void eatFood() {
		if (snake.get(0).getLayoutX() == food.getLayoutX() && snake.get(0).getLayoutY() == food.getLayoutY()) {
			createFood();
			snake.add(dummy);
			createDummy();
			score +=5;
			scoreBoard.setText("Score: "+score);			
			animationRate += 0.05;
			timeline.setRate(animationRate);
		}	
	}	
	
	private void gameOver() {
		if (snake.get(0).getLayoutX() < 0 || snake.get(0).getLayoutY() < 0 
				|| snake.get(0).getLayoutX() >= (sceneX) || snake.get(0).getLayoutY() >= (sceneY)) {
			gameOver = true;
		}		
		
	}
	
	private void newGame() {
    	pane.getChildren().removeAll(snake);
    	pane.getChildren().remove(dummy);
    	createSnake();
    	createFood();
    	createDummy();
    	pane.getChildren().add(food);
    	score = 0;
		scoreBoard.setText("Score: "+score);			
    	animationRate = 1;
    	timeline.setRate(animationRate);
    	timeline.play();
	}
    
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("SNAKE");
    	GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);
        root.addRow(0, scoreBoard = new Text("Score: "+score));
        root.addRow(1, sceneGraph());
    	Scene scene = new Scene (root);
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    if (currentDirection != LEFT) {
                        currentDirection = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        currentDirection = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (currentDirection != DOWN) {
                        currentDirection = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (currentDirection != UP) {
                        currentDirection = DOWN;
                    }
                }
                if (code == KeyCode.N) {
                	newGame();
                }
            }
        });
    	    	
    	timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run()));
    	timeline.setCycleCount(Animation.INDEFINITE);
    	timeline.play();   

    }
 public static void main(String[] args) {
        launch(args);
    }
	
}
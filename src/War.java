import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Jiadong Yin
 * This is the driver class
 */
public class War extends Application {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private Pane splashLayout;
    private Timeline animation;
    private WarPane warPane;
    private Button startBtn, exitBtn, resumeBtn, pauseBtn;

    public void start (Stage primaryStage) {	
        warPane = new WarPane();
        primaryStage.setTitle(warPane.getTitle());     
        showSplashScreen(primaryStage);
    }

	private void showMainScreen(Stage primaryStage) {
		Scene scene = warPane.init(WIDTH, HEIGHT);
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> warPane.step(SECOND_DELAY, WIDTH, HEIGHT));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        warPane.setStageandAnimation(primaryStage, animation);
        
    	addGameCtrlButtons();
    	
        primaryStage.setScene(scene);
        primaryStage.show();
        
        animation.play();
	}

	private void showSplashScreen(Stage primaryStage) {
		splashLayout = new Pane();
		splashLayout.setPrefSize(WIDTH, HEIGHT);
		
		Image splashimg = new Image(getClass().getClassLoader().getResourceAsStream("splash.jpg"));
		splashLayout.getChildren().add(new ImageView(splashimg));
		
		Scene splashScene = new Scene(splashLayout);
		
        primaryStage.setScene(splashScene);
        primaryStage.show();
        
        addSplashLogo();
        addSplashCtrlButtons(primaryStage);
	}

	private void addSplashLogo() {
		Text gameLogo = new Text("Thunder Strike");
        gameLogo.setFont(Font.font("Tahoma",30));
        gameLogo.setFill(Color.RED);
        gameLogo.setLayoutX(WIDTH / 2 - 100);
        gameLogo.setLayoutY(HEIGHT / 2 - 50);
        splashLayout.getChildren().add(gameLogo);
	}

	private void addSplashCtrlButtons(Stage primaryStage) {
		VBox vbox = new VBox();
    	vbox.setLayoutX(WIDTH / 2 - 40);
    	vbox.setLayoutY(HEIGHT / 2 - 30);
    	vbox.setSpacing(10);
    	
    	startBtn = new Button("Start Game");
    	startBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent me) {
    			showMainScreen(primaryStage);
    		}
		});
    	
    	exitBtn = new Button("Exit Game");
    	exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent me) {
    			System.exit(0);
    		}
		});
    	
    	vbox.getChildren().addAll(startBtn, exitBtn);
        splashLayout.getChildren().add(vbox);
	}

	private void addGameCtrlButtons() {
		HBox hbox = new HBox();
    	hbox.setLayoutX(10);
    	hbox.setLayoutY(10);
    	hbox.setSpacing(10);
    	
    	resumeBtn = new Button("Resume");
    	resumeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent me) {
    			animation.play();
    		}
		});
    	
    	pauseBtn = new Button("Pause");
    	pauseBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent me) {
    			animation.pause();
    		}
		});
    	
    	hbox.getChildren().addAll(resumeBtn, pauseBtn);
        warPane.getRoot().getChildren().add(hbox);
	}
 
    public static void main (String[] args) {
        launch(args);
    }
}
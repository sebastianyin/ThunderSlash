import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Jiadong Yin
 * This is the main game class
 */

//This entire file is part of my masterpiece.
//Jiadong Yin
 
public class WarPane {
    private static final String TITLE = "Thunder Strike";
    private static final int KEY_INPUT_SPEED = 5;
    private static final int ENEMY_SPEED = 50;
    private static final int MAX_LIFE = 3;
    private static final int MAX_BOSS_HP = 100;
    private static final int LAG_TIME_EXPLOSION = 60;
    private static final int LAG_TIME_PLAYER_RESET = 600;
    private static final int MOB_SPAWN_INTEVAL = 60;
    private static final int MOB_STOP_SPAWN_TIME = 1200;
    private static final int BOSS_SPAWN_TIME = 1400;
    private static final int BOSS_BULLET_SPAWN_INTEVAL = 60;
    private static final int PLAYER_BULLET_DAMAGE = 5;
    
    private Stage primaryStage;
    private Timeline animation;
    private Scene scene;
    private Pane root;
    private Player p1;
    private ArrayList<Enemy> mobs;
    private Boss boss;
    private Random RdnGenerator = new Random();
    private Boolean isDeadEventTrigger = false;
    private Boolean isBossDead = false;
    private ImageView explosionToAdd;
    private int lagTimeCounter;
    private int mainCounter;
    private int enemySpawnCounter;
    private int bossBulletSpawnCounter;
    private int totalScore = 0;
    private int lifeLeft = MAX_LIFE;
     
    public String getTitle () {
        return TITLE;
    }
    
    public Pane getRoot() {
    	return root;
    }
    
    public void setStageandAnimation(Stage primaryStage, Timeline animation) {
    	this.primaryStage = primaryStage;
    	this.animation = animation;
    }

    public Scene init (int width, int height) {
    	setBackground(width, height);
    	
        addPlayer(width, height);
        mobs = new ArrayList<Enemy>();
        
        initCounter();
        
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

	private void initCounter() {
		lagTimeCounter = 0;
		mainCounter = 0;
        enemySpawnCounter = 0;
        bossBulletSpawnCounter = 0;
	}

	private void setBackground(int width, int height) {
		root = new Pane();
    	root.setPrefSize(width, height);
    	Image bg = new Image(getClass().getClassLoader().getResourceAsStream("galaxy.jpg"));
    	ImageView img_bg = new ImageView(bg);
    	img_bg.setFitWidth(width);
    	img_bg.setFitHeight(height);
    	
    	root.getChildren().addAll(img_bg);
    	scene = new Scene(root);
	}

	private void addPlayer(int width, int height) {
		Image image_player = new Image(getClass().getClassLoader().getResourceAsStream("plane.jpg"));
        p1 = new Player(new ImageView(image_player));
        p1.getImage().setX(width / 2 - p1.getImage().getBoundsInLocal().getWidth() / 2);
        p1.getImage().setY(height - p1.getImage().getBoundsInLocal().getHeight());
        
        root.getChildren().add(p1.getImage());   
	}

    public void step (double elapsedTime, int width, int height) {	
    	if (enemySpawnCounter == MOB_SPAWN_INTEVAL && mainCounter < MOB_STOP_SPAWN_TIME) {
    		addMob(width, height);
    		enemySpawnCounter = 0;
    	}
    	
		if (mainCounter == BOSS_SPAWN_TIME) {
			addBoss(width, height);
		}
    	
    	if (mainCounter > BOSS_SPAWN_TIME) {
    		checkBossPlayerInteraction(elapsedTime, width, height);
    	}
    	
    	checkMobPlayerInteraction(elapsedTime);
    	
    	removeOutOfBoundPlayerBullet(height);
    	removeOutOfBoundMob(height);
    	
    	if (lifeLeft == 0 ) {
    		animation.stop();
    		showEndingScreen(primaryStage, "Game Over", width, height);
    	}
    	
    	if (isBossDead) {
    		animation.stop();
    		showEndingScreen(primaryStage, "  You Win", width, height);
    	}
    	
    	mainCounter++;
        enemySpawnCounter++;
    }

	private void showEndingScreen(Stage primaryStage, String endingText, int width, int height) {
		Pane endingLayout = new Pane();
		endingLayout.setPrefSize(width, height);
		
		Image endingImage = new Image(getClass().getClassLoader().getResourceAsStream("ending.jpg"));
		endingLayout.getChildren().add(new ImageView(endingImage));
		
		Scene endingScene = new Scene(endingLayout);
		
        primaryStage.setScene(endingScene);
        primaryStage.show();
        
        addEndingText(endingLayout, endingText, width, height);   
        addGameOverCtrlButtons(primaryStage, endingLayout, width, height);
	}

	private void addEndingText(Pane endingLayout, String endingText, int width, int height) {
		Text endingLogo = new Text(endingText);
        endingLogo.setFont(Font.font("Tahoma",30));
        endingLogo.setFill(Color.LIME);
        endingLogo.setLayoutX(width / 2 - 80);
        endingLogo.setLayoutY(height / 2 - 50);
        endingLayout.getChildren().add(endingLogo);
	}
	
	private void addGameOverCtrlButtons(Stage primaryStage, Pane gameOverLayout, int width, int height) {
		VBox vbox = new VBox();
    	vbox.setLayoutX(width / 2 - 40);
    	vbox.setLayoutY(height / 2 - 30);
    	vbox.setSpacing(10);
    	
    	Button exitBtn = new Button("Exit Game");
    	exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent me) {
    			System.exit(0);
    		}
		});
    	
    	vbox.getChildren().add(exitBtn);
        gameOverLayout.getChildren().add(vbox);
	}
    
	private void checkMobPlayerInteraction(double elapsedTime) {
		checkMobDamage();
    	checkMobPlayerCollision(elapsedTime);
	}

	private void checkBossPlayerInteraction(double elapsedTime, int width,
			int height) {
		if(bossBulletSpawnCounter == BOSS_BULLET_SPAWN_INTEVAL) {
			boss_shoot();
			bossBulletSpawnCounter = 0;
		}
		updateBossPosition(elapsedTime, width, height);
		checkBossDamage();
		checkPlayerDamage();
		removeOutOfBoundBossBullet(height);
		
		bossBulletSpawnCounter++;
	}

	private void checkPlayerDamage() {
		for(int i = 0; i < boss.getBullets().size(); i++) {
			boss.getBullets().get(i).getImage().setY(boss.getBullets().get(i).getImage().getY() + boss.getBullets().get(i).getBulletProperty().getBulletSpeed());
				
    		if (!isDeadEventTrigger) {
    			if (boss.getBullets().get(i).getImage().getBoundsInParent().intersects(p1.getImage().getBoundsInParent())) {
    		    	root.getChildren().remove(boss.getBullets().get(i).getImage());
    		    	boss.getBullets().remove(i);
    				processPlayerDeath();
    			}
    		}
    		
    		else {
    			processPlayerRevive();
    		}
		}
	}

	private void checkBossDamage() {
		for(int i = 0; i < p1.getBullets().size(); i++) {
			if (p1.getBullets().get(i).getImage().intersects(boss.getImage().getBoundsInParent())) {
				boss.setHP(boss.getHP() - PLAYER_BULLET_DAMAGE);
				root.getChildren().remove(p1.getBullets().get(i).getImage());
				p1.getBullets().remove(i);
				
				if(boss.getHP() == 0) {
					isBossDead = true;
					processBossDeath();
					break;
				}
			}
		}
	}

	private void processBossDeath() {
		root.getChildren().remove(boss.getImage());
		
    	Image image_bossDeath = new Image(getClass().getClassLoader().getResourceAsStream("explosion_boss.jpg"));
    	explosionToAdd = new ImageView(image_bossDeath);
    	explosionToAdd.setX(boss.getImage().getX());
    	explosionToAdd.setY(boss.getImage().getY());
    	root.getChildren().add(explosionToAdd); 
	}

	private void boss_shoot() {
		root.getChildren().add(boss.shoot().getImage());
	}

	private void removeOutOfBoundBossBullet(int height) {
		for (int i = 0; i < boss.getBullets().size(); i++) {
			if (boss.getBullets().get(i).getImage().getY() > height) {
				root.getChildren().remove(boss.getBullets().get(i).getImage());
				boss.getBullets().remove(i);
			}
		}	
	}

	private void updateBossPosition(double elapsedTime, int width, int height) {
		boss.getImage().setX(boss.getImage().getX() + boss.getVelocity().getX() * elapsedTime);
		boss.getImage().setY(boss.getImage().getY() + boss.getVelocity().getY() * elapsedTime);
		
		if (boss.getImage().getX() <= 0 || boss.getImage().getX() >= width - boss.getImage().getBoundsInLocal().getWidth()) {
			boss.setVelocity(new Point2D(-1 * boss.getVelocity().getX(), boss.getVelocity().getY()));
		}
	}

	private void addBoss(int width, int height) {
		Image image_boss = new Image(getClass().getClassLoader().getResourceAsStream("boss.jpg"));
    	Point2D veloCurrent = new Point2D (RdnGenerator.nextInt(ENEMY_SPEED) + ENEMY_SPEED, 0);
    	boss = new Boss(new ImageView(image_boss), veloCurrent, MAX_BOSS_HP);
    	boss.getImage().setX(RdnGenerator.nextInt(width - (int)boss.getImage().getBoundsInLocal().getWidth()));
    	boss.getImage().setY(0);
    	root.getChildren().add(boss.getImage());
	}

	private void checkMobDamage() {
		for (int i = 0; i < p1.getBullets().size(); i++) {
			p1.getBullets().get(i).getImage().setY(p1.getBullets().get(i).getImage().getY() - p1.getBullets().get(i).getBulletProperty().getBulletSpeed());

    		for (int j = 0; j < mobs.size(); j ++) {
    			if (p1.getBullets().get(i).getImage().intersects(mobs.get(j).getImage().getBoundsInParent())) {
    				totalScore++;
    				root.getChildren().remove(p1.getBullets().get(i).getImage());
    				p1.getBullets().remove(i);
    				root.getChildren().remove(mobs.get(j).getImage());
    				mobs.remove(j);
    				System.out.println("Score: " + totalScore);
    				break;
    			}
    		}
    	}
	}

	private void checkMobPlayerCollision(double elapsedTime) {
		for (int i = 0; i < mobs.size(); i++) {	
    		mobs.get(i).getImage().setX(mobs.get(i).getImage().getX() + mobs.get(i).getVelocity().getX() * elapsedTime);
    		mobs.get(i).getImage().setY(mobs.get(i).getImage().getY() + mobs.get(i).getVelocity().getY() * elapsedTime);
    		
    		if (!isDeadEventTrigger) {
    			if (mobs.get(i).getImage().getBoundsInParent().intersects(p1.getImage().getBoundsInParent())) {
    		    	root.getChildren().remove(mobs.get(i).getImage());
    				mobs.remove(i);
    				processPlayerDeath();
    			}
    		}
    		
    		else {
    			processPlayerRevive();
    		}
    	}
	}

	private void removeOutOfBoundPlayerBullet(int height) {
		for (int i = 0; i < p1.getBullets().size(); i++) {
			if (p1.getBullets().get(i).getImage().getY() < - p1.getBullets().get(i).getImage().getBoundsInLocal().getHeight()) {
				root.getChildren().remove(p1.getBullets().get(i).getImage());
				p1.getBullets().remove(i);
			}
		}
	}
	
	private void removeOutOfBoundMob(int height) {
		for (int i = 0; i < mobs.size(); i++) {
			if (mobs.get(i).getImage().getY() > height) {
				root.getChildren().remove(mobs.get(i).getImage());
				mobs.remove(i);
			}
		}
	}

	private void addMob(int width, int height) {
		Image image_enemy = new Image(getClass().getClassLoader().getResourceAsStream("mob.jpg"));
    	Point2D veloCurrent = new Point2D (0, RdnGenerator.nextInt(ENEMY_SPEED) + ENEMY_SPEED);
    	mobs.add(new Enemy(new ImageView(image_enemy), veloCurrent));
    	mobs.get(mobs.size() - 1).getImage().setX(RdnGenerator.nextInt(width - (int)mobs.get(mobs.size() - 1).getImage().getBoundsInLocal().getWidth()));
    	mobs.get(mobs.size() - 1).getImage().setY(RdnGenerator.nextInt(height) - height);
    	root.getChildren().add(mobs.get(mobs.size() - 1).getImage());
	}

	private void processPlayerDeath() {
	    	lifeLeft--;
	    	isDeadEventTrigger = true;
	    	
	    	Image image_ghost = new Image(getClass().getClassLoader().getResourceAsStream("plane_ghost.jpg"));
	    	p1.getImage().setImage(image_ghost);
	    	
	    	Image image_explosion = new Image(getClass().getClassLoader().getResourceAsStream("explosion.jpg"));
	    	explosionToAdd = new ImageView(image_explosion);
	    	explosionToAdd.setX(p1.getImage().getX());
	    	explosionToAdd.setY(p1.getImage().getY());
	    	root.getChildren().add(explosionToAdd);      
	    	
	    	System.out.println("Life left: " + lifeLeft);
	}
		
	private void processPlayerRevive() {
			if (lagTimeCounter == LAG_TIME_EXPLOSION) {
				root.getChildren().remove(explosionToAdd);
			}
			
			if (lagTimeCounter == LAG_TIME_PLAYER_RESET) {
				Image image_revive = new Image(getClass().getClassLoader().getResourceAsStream("plane.jpg"));
		    	p1.getImage().setImage(image_revive);
				isDeadEventTrigger = false;
				lagTimeCounter = 0;
			}
			lagTimeCounter++;
	}

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case RIGHT:
            	if (p1.getImage().getX() < scene.getWidth() - p1.getImage().getBoundsInLocal().getWidth()) {
            		p1.getImage().setX(p1.getImage().getX() + KEY_INPUT_SPEED);
            	}
                break;
            case LEFT:
            	if (p1.getImage().getX() > 0) {
            		p1.getImage().setX(p1.getImage().getX() - KEY_INPUT_SPEED);
            	}
                break;
            case UP:
            	if (p1.getImage().getY() > 0) {
            		p1.getImage().setY(p1.getImage().getY() - KEY_INPUT_SPEED);
            	}
                break;
            case DOWN:
            	if (p1.getImage().getY() < scene.getHeight() - p1.getImage().getBoundsInLocal().getHeight()) {
            		p1.getImage().setY(p1.getImage().getY() + KEY_INPUT_SPEED);
            	}
                break;
            case D:
            	player_shoot();
            	break;
            default:
        }
    }

	private void player_shoot() {
		root.getChildren().add(p1.shoot().getImage());
	}
	
	public int getTotalScore() {
		return totalScore;
	}
	
	public int getLifeLeft() {
		return lifeLeft;
	}
}
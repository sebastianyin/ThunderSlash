import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Player extends Battleship {
	private ArrayList<Bullet> bullets;
	
	public Player(ImageView shipImage) {
		super(shipImage);
		bullets = new ArrayList<Bullet>();
	}
	
	public Bullet shoot() {
		Bullet bullet = createBullet();
		bullets.add(bullet);
		return bullet;
	}

	private Bullet createBullet() {
		BulletProperties bulletProperty = new BulletProperties(5, 10, 10);
		double bullet_init_x = shipImage.getX() + (shipImage.getBoundsInLocal().getWidth() - bulletProperty.getBulletWidth()) / 2;
		double bullet_init_y = shipImage.getY() - bulletProperty.getBulletHeight();
		Bullet bullet = new Bullet(new Rectangle(bullet_init_x, bullet_init_y, bulletProperty.getBulletWidth(), bulletProperty.getBulletHeight()), bulletProperty);
		bullet.getImage().setFill(Color.RED);
		return bullet;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
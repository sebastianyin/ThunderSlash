import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Boss extends Enemy {
	private int hp;
	private ArrayList<Bullet> bullets;
	
	public Boss(ImageView shipImage, Point2D velocity, int hp) {
		super(shipImage, velocity);
		this.hp = hp;
		bullets = new ArrayList<Bullet>();
	}
	
	public Bullet shoot() {
		Bullet bullet = createBullet();
		bullets.add(bullet);
		return bullet;
	}

	private Bullet createBullet() {
		BulletProperties bulletProperty = new BulletProperties(5, 10, 5);
		double bullet_init_x = shipImage.getX() + (shipImage.getBoundsInLocal().getWidth() - bulletProperty.getBulletWidth()) / 2;
		double bullet_init_y = shipImage.getY() + shipImage.getBoundsInLocal().getHeight();
		Bullet bullet = new Bullet(new Rectangle(bullet_init_x, bullet_init_y, bulletProperty.getBulletWidth(), bulletProperty.getBulletHeight()), bulletProperty);
		bullet.getImage().setFill(Color.LIME);
		return bullet;
	}
	
	public int getHP() {
		return hp;
	}
	
	public void setHP(int hp) {
		this.hp = hp;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
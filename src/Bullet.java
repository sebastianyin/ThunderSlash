import javafx.scene.shape.Rectangle;


public class Bullet {
	
	private Rectangle bulletImage;
	private BulletProperties bulletProperty;
	
	public Bullet(Rectangle bulletImage, BulletProperties bulletProperty) {
		this.bulletImage = bulletImage;
		this.bulletProperty = bulletProperty;
	}
	
	public Rectangle getImage() {
		return bulletImage;
	}
	
	public BulletProperties getBulletProperty() {
		return bulletProperty;
	}
	
	public void setBulletImage(Rectangle bulletImage) {
		this.bulletImage = bulletImage;
	}
	
}

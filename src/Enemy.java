import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;


public class Enemy extends Battleship {
	private Point2D velocity;
	
	public Enemy(ImageView shipImage, Point2D velocity) {
		super(shipImage);
		this.velocity = velocity;
	}
	
	public Point2D getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}
}
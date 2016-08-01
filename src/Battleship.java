import javafx.scene.image.ImageView;


public class Battleship {
	protected ImageView shipImage;
	
	public Battleship(ImageView shipImage) {
		this.shipImage = shipImage;
	}
	
	public ImageView getImage() {
		return shipImage;
	}
	
	public void setImage(ImageView shipImage) {
		this.shipImage = shipImage;
	}
}
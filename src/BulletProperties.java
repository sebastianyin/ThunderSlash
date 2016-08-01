
public class BulletProperties {
	
	private int bulletWidth;
	private int bulletHeight;
	private int bulletSpeed;
	
	public BulletProperties(int bulletWidth, int bulletHeight, int bulletSpeed) {
		this.bulletWidth = bulletWidth;
		this.bulletHeight = bulletHeight;
		this.bulletSpeed = bulletSpeed;
	}
	
	public int getBulletWidth() {
		return bulletWidth;
	}
	
	public int getBulletHeight() {
		return bulletHeight;
	}
	
	public int getBulletSpeed() {
		return bulletSpeed;
	}
}

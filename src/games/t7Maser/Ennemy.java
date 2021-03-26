package games.t7Maser;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Ennemy {
	private int x = 0;
	private int y = 0;
	private World w;
	private Image image;

	int moveTimer = 100;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Ennemy(World world, int x, int y){
		image = AppLoader.loadPicture(World.DIRECTORY_IMAGES+"nyan.png");
		this.x = x;
		this.y = y;
		w=world;
		w.getGrid().getCell(x, y).setDeadly(true);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		//Affichage
		image.draw(280+(x*100*w.getRenderScale())+360-w.getGrid().getColumns()*100*w.getRenderScale()/2,y*100*w.getRenderScale()+360-w.getGrid().getColumns()*100*w.getRenderScale()/2,100*w.getRenderScale(),100*w.getRenderScale());

	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) {
		moveTimer--;
		Player nearestPlayer = w.getPlayers().get(0);
		double minDistance=Math.sqrt((double) Math.pow((nearestPlayer.getX()-x),2)+Math.pow((nearestPlayer.getY()-y),2));
		for (Player p : w.getPlayers()) {
			double tmp = Math.sqrt((double) Math.pow((p.getX()-x),2)+Math.pow((p.getY()-y),2));
			if ((minDistance < tmp) || (minDistance == tmp && Math.random()<0.5)) {
				nearestPlayer = p;
				minDistance = tmp;
			}
		}
		int newX = x;
		int newY = y;
		if(moveTimer <= 0){
			if(x > nearestPlayer.getX())
				newX = x-1;
			if(x < nearestPlayer.getX())
				newX = x+1;
			if(y > nearestPlayer.getY())
				newY = y-1;
			if(y < nearestPlayer.getY())
				newY = y+1;

			if(w.getGrid().MoveEnnemy(newX, newY, this)){
				//move
				x = newX;
				y = newY;
			}

			moveTimer=50;
		}
	}
}

package games.t7Maser;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Cell {

	public static final int NORMAL_TYPE = 0 ;
	public static final int MINE_TYPE = 1 ;
	public static final int BONUS_TYPE = 2 ;

	protected Image NORMAL;
	protected Image MINE;
	protected Image BONUS;

	//Variables
	private int x;
	private int y;
	private int contains;
	private boolean deadly;
	private boolean hasBonus;
	private boolean hasEnnemy;
	private World w;
	private int imageType;

	//Constructeur
	public Cell(World world, int x, int y, int c, Boolean d){
		this.x=x;
		this.y=y;
		this.contains=c;
		this.deadly=d;
		this.hasBonus=false;
		this.hasEnnemy = false;
		this.w=world;

		if(NORMAL==null){
			NORMAL= AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Cell.png");
			MINE=AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Mine.png");
			BONUS=AppLoader.loadPicture(World.DIRECTORY_IMAGES+"Bonus.png");
		}
	}

	//Getters et Setters
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
	public int getContains() {
		return contains;
	}
	public void setContains(int contains) {
		this.contains = contains;
	}
	public Boolean getDeadly() {
		return deadly;
	}
	public void setDeadly(Boolean deadly) {
		this.deadly = deadly;
		if(this.imageType == MINE_TYPE)
			this.deadly = true;
	}
	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

    public Image getImage() {
        if(imageType==MINE_TYPE)return MINE;
        else if(imageType==NORMAL_TYPE)return NORMAL;
        else if(imageType==BONUS_TYPE)return BONUS;
        return NORMAL;
    }
	public boolean isHasEnnemy() {
		return hasEnnemy;
	}
	public void setHasEnnemy(boolean hasEnnemy) {
		this.hasEnnemy = hasEnnemy;
	}
	public Boolean getHasBonus() {
		return hasBonus;
	}
	public void setHasBonus(Boolean hasBonus) {
		this.hasBonus = hasBonus;
	}

	//render et update
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) {
		//Affichage
		arg2.drawImage(getImage(),0,0);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) {
		if(deadly && contains!=-1)
			w.getPlayers().get(contains).setLives(0);

		if(hasBonus && contains!=-1){
			w.getPlayers().get(contains).addScore(77);
			w.getGrid().getCell(x, y).setHasBonus(false);
			w.getGrid().getCell(x,y).setImageType(NORMAL_TYPE);
			World.cat.playAsSoundEffect(1f, .3f, false);
		}

	}

}

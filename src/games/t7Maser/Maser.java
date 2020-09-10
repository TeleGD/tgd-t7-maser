package games.t7Maser;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Maser {

	private int axe; //0 pour horizontal et 1 pour vertical
	private int pos; //position associée à la case correspondante
	private Boolean shooting = false;
	private World w;
	private int setupTime;
	private int shootingTime;

	public Maser(World w,int axe, int pos){
		this.setAxe(axe);
		this.setPos(pos);
		this.w=w;
		this.setupTime = 50;
		this.shootingTime = 100;

	}

	public int getAxe() {
		return axe;
	}

	public void setAxe(int axe) {
		this.axe = axe;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Boolean getShooting() {
		return shooting;
	}

	public void setShooting(Boolean shooting) {
		this.shooting = shooting;
	}

	public void setDeadlyCells(boolean deadly){
		Grid g = w.getGrid();
		if(axe == 0) {
			//horizontal
			for(int i = 0;i<g.getColumns();i++){
				g.getCell(i, pos).setDeadly(deadly);
			}
		}
		else {
			//vertical
			for(int i = 0;i<g.getRows();i++){
				g.getCell(pos, i).setDeadly(deadly);
			}
		}
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) {
		if(!shooting)
			g.setColor(new Color(255,128,0,204));
		else
			g.setColor(new Color(255,0,0,204));
		//Affichage
		if(axe == 0) {
			//horizontal
			g.fillRect((float)280+360-w.getGrid().getColumns()*100*w.getRenderScale()/2, (pos*100+25)*w.getRenderScale()+360-w.getGrid().getColumns()*100*w.getRenderScale()/2, (float)100*w.getGrid().getRows()*w.getRenderScale(), 50*w.getRenderScale());
		}
		else {
			//vertical
			g.fillRect((pos*100+25)*w.getRenderScale()+280+360-w.getGrid().getColumns()*100*w.getRenderScale()/2, +360-w.getGrid().getColumns()*100*w.getRenderScale()/2, 50*w.getRenderScale(), (float)100*w.getGrid().getColumns()*w.getRenderScale());
		}

	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) {
		if(!shooting){ //temps de prévention
			setupTime--;
			if(setupTime == 0){
				shooting = true;
				this.setDeadlyCells(true);
			}
		}
		if(shooting){ //temps de tir
			shootingTime--;
			if(shootingTime == 0){
				this.setDeadlyCells(false);
			}
		}
		//une fois que tout ça c'est fini
		if(setupTime == 0 && shootingTime == 0)
			w.getGrid().removeMaser(this);
	}

}

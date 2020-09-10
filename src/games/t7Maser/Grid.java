package games.t7Maser;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Grid {
	private int rows;
	private int columns;
	public Cell grid[][];

	private int maxRows; //limite horizontale
	private int maxCols;

	public List<Maser> maserList;
	public List<Ennemy> ennemyList;
	private int maserTimer;

	private int waveTimer;
	private int waveNumber;
	private World w;

	public Grid(World world, int r, int c){
		maxRows = 20;
		maxCols = 20;
		rows = r;
		columns = c;
		w=world;
		grid = new Cell[maxRows][maxRows];
		for(int i = 0; i<maxRows; i++)//init row
			for(int j=0;j<maxCols;j++) //init cologne
				grid[i][j] = new Cell(world, i,j,-1,false);

		maserList = new LinkedList<Maser>();
		maserTimer = 100;
		waveTimer = 200;
		waveNumber = 0;

		ennemyList = new LinkedList<Ennemy>();

	}

	public Cell[][] getGrid() {
		return grid;
	}

	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	public Cell getCell(int x, int y){
		return grid[x][y];
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) {
		for(int i = 0; i<this.rows; i++)//init row
			for(int j=0;j<this.columns;j++) //init cologne
				grid[i][j].getImage().draw(280+360-this.getColumns()*100*w.getRenderScale()/2+i*100*w.getRenderScale(),0+j*100*w.getRenderScale()+360-this.getColumns()*100*w.getRenderScale()/2,100*w.getRenderScale(),100*w.getRenderScale());

		for(Maser l : maserList)
			l.render(arg0, arg1, arg2);

		for(Ennemy e : ennemyList)
			e.render(arg0, arg1, arg2);

	}

	public int getWaveNumber() {
		return waveNumber;
	}

	public void setWaveNumber(int waveNumber) {
		this.waveNumber = waveNumber;
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) {
		if(waveNumber % 5 == 2 && waveTimer == 1)
			addEnnemy();

		maserTimer--;
		if(maserTimer <= 0){
			addMaser();
			maserTimer = Math.max(50-waveNumber*5, 0)+20;
		}
		try{
			for(Maser l : maserList)
				l.update(arg0, arg1, arg2);

			for(Ennemy e : ennemyList)
				e.update(arg0, arg1, arg2);
		}
		catch(Exception e){
			//System.out.println(e.getMessage());
		}

		for(int i = 0; i<this.rows; i++)//init row
			for(int j=0;j<this.columns;j++) //init column
				grid[i][j].update(arg0, arg1, arg2);

		waveTimer--;
		if(waveTimer == 0){
			if(rows+1 < maxRows)
				rows++;
			if(columns+1 < maxCols)
				columns++;
			waveNumber++;
			if(rows==maxRows){
				for(int i=0; i<5; i++){
					addMine();
				}
			}
			else{
				addMine();
			}
			waveTimer = 100;
			Random b1 = new Random();
			Random b2 = new Random();
			int rowB = b1.nextInt(this.rows);
			int columnB = b2.nextInt(this.columns);
			if(grid[rowB][columnB].getImageType()!=Cell.MINE_TYPE)
					addBonus(rowB,columnB);

			if(rows > 7)
				w.setRenderScale((float)w.height/(100*rows));
		}
	}

	public int getRows(){
		return rows;
	}

	public int getColumns(){
		return columns;
	}

	public int getMaxRows(){
		return maxRows;
	}

	public int getMaxColumns(){
		return maxCols;
	}

	//x y new position
	public boolean MovePlayer(int x,int y, Player p){
		if(x < rows && y < columns && x >= 0 && y >= 0 && grid[x][y].getContains()==-1){
			//set new cell true
			grid[x][y].setContains(w.getPlayers().indexOf(p));
			//set old cell false
			grid[p.getX()][p.getY()].setContains(-1);

			return true;
		}
		return  false;
	}

	public boolean MoveEnnemy(int x,int y, Ennemy p){
		if(x < rows && y < columns && x >= 0 && y >= 0 && !getCell(x, y).isHasEnnemy()){
			//set new cell true
			grid[x][y].setDeadly(true);
			grid[x][y].setHasEnnemy(true);
			//set old cell false
			grid[p.getX()][p.getY()].setDeadly(false);
			grid[p.getX()][p.getY()].setHasEnnemy(false);

			return true;
		}
		return  false;
	}

	public void addEnnemy(){
		Random r = new Random();
		int x = 0;
		int y = 0;
		do{
			x = r.nextInt(rows);
			y = r.nextInt(columns);
		}
		while(getCell(x, y).getDeadly() || getCell(x, y).getContains()!=-1);

		ennemyList.add(new Ennemy(w,x,y));

	}

	public void addMaser(){
		Random r = new Random();
		boolean axe = r.nextBoolean();
		if(axe) //horizontal
			maserList.add(new Maser(w,0,r.nextInt(columns)));
		else //vertical

			maserList.add(new Maser(w,1,r.nextInt(rows)));
	}

	public void removeMaser(Maser l){
		maserList.remove(l);
	}

	public void addMine(){
		Random r1 = new Random();
		Random r2 = new Random();
		int row = r1.nextInt(this.rows);
		int column = r2.nextInt(this.columns);
		if(getCell(row, column).getContains()==-1){
			this.grid[row][column].setDeadly(true);
			this.grid[row][column].setImageType(Cell.MINE_TYPE);
		}

	}

	public void addBonus(int row, int column){
		this.grid[row][column].setDeadly(false);
		this.grid[row][column].setHasBonus(true);
		this.grid[row][column].setImageType(Cell.BONUS_TYPE);
	}

}

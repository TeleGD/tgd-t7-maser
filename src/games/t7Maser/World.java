package games.t7Maser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppFont;
import app.AppGame;
import app.AppLoader;
import app.AppWorld;

public class World extends AppWorld {

	public final static String DIRECTORY_SOUNDS="/sounds/t7Maser/";
	public final static String DIRECTORY_MUSICS="/musics/t7Maser/";
	public final static String DIRECTORY_IMAGES="/images/t7Maser/";
	public static final Font Font = AppLoader.loadFont("/fonts/vt323.ttf", AppFont.BOLD, 18);

	private List<Player> players;
	private List<Player> morts;
	private static Grid grid;
	private static Audio music;
	private static Audio end;
	static Audio cat;

	static {
		music = AppLoader.loadAudio(DIRECTORY_MUSICS+"EpicSaxGuy.ogg");
		end = AppLoader.loadAudio(DIRECTORY_SOUNDS+"EndSong.ogg");
		cat= AppLoader.loadAudio(DIRECTORY_SOUNDS+"Cat.ogg");
	}

	private float musicPos;
	private float renderScale = 1;
	public int height;
	public int width;
	private int nbJoueursInit;
	private boolean fin;

	public World (int ID) {
		super (ID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		width=container.getWidth();
		height=container.getHeight();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		//Affichage
		Color c = fin ? Color.black : Color.white;
		g.setColor(c);
		g.fillRect(0,0,width,height);
		if (!fin) {
			grid.render(container,game,g);
			for (Player p : players) {
				p.render(container,game,g);
			}
		} else {
			g.setColor(Color.black);
			for (int i=morts.size()-1; i>=0;i--) {
				g.setColor(morts.get(i).getCouleur());
				String s = morts.get(i).getName()+" : "+morts.get(i).getScore();
				g.drawString(s,width/2-Font.getWidth(s)/2,height/2-(Font.getHeight(s)+5)*(nbJoueursInit/2-i));
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		super.update (container, game, delta);
		if (!fin) {
			for (Player p : players) {
				p.update(container,game,delta);
			}
			grid.update(container,game,delta);

			for (int i=0 ; i<players.size() ; i++) {
				if (players.get(i).getLives()==0) {
					players.get(i).addScore(200*morts.size());
					morts.add(players.get(i));
					grid.getCell(players.get(i).getX(), players.get(i).getY()).setContains(-1);
					players.remove(i);
				}
			}

			if (morts.size()>=nbJoueursInit) {
				music.stop();
				end.playAsSoundEffect(1f, .3f, false);
				Player tri[] = new Player[morts.size()];
				for (int i=0; i<morts.size(); i++) {
					tri[i] = morts.get(i);
				}
				for (int i=tri.length-1 ; i>0 ; i--) {
					for (int j=0; j<i ; j++) {
						if (tri[j+1].getScore()<tri[j].getScore()) {
							Player tmp = tri[j+1];
							tri[j+1] = tri[j];
							tri[j]=tmp;
						}
					}
				}
				morts = new ArrayList<>(Arrays.asList(tri));
				fin = true;
			}
		}
	}

	public Grid getGrid(){
		return grid;
	}

	public List<Player> getPlayers(){
		return players;
	}

	public float  getRenderScale(){
		return renderScale;
	}

	public void setRenderScale(float d){
		 renderScale = d;
	}

	@Override
	public void play(GameContainer container, StateBasedGame game) {
		AppGame appGame = (AppGame) game;
		fin = false;
		grid = new Grid(this,4,4);
		renderScale = 1;
		nbJoueursInit = appGame.appPlayers.size ();
		this.players = new ArrayList<Player>();
		this.morts = new ArrayList<Player>();
		int w = grid.getColumns ();
		int h = grid.getRows ();
		for (int i = 0; i < nbJoueursInit; i++) {
			this.players.add (new Player (this, (-i >> 1 & 1) * (w-1), (i & 1) * (h-1), i, appGame.appPlayers.get(i)));
		}
		music.playAsMusic(1f, .3f, true);
	}

	@Override
	public void pause(GameContainer container, StateBasedGame game) {
		this.musicPos = music.getPosition();
		music.stop();
	}

	@Override
	public void resume(GameContainer container, StateBasedGame game) {
		music.playAsMusic(1, .3f, true);
		music.setPosition(this.musicPos);
	}

	@Override
	public void stop(GameContainer container, StateBasedGame game) {
		music.stop();
	}

}

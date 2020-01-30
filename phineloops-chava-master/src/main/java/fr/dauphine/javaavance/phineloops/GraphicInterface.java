package fr.dauphine.javaavance.phineloops;
import javax.swing.JPanel;

import fr.dauphine.javaavance.phineloops.pieces.Piece;

import java.awt.*;

import javax.swing.JFrame;

/**
 * class in charge of update teh GI every  0.04 seconds (24 FPS)
 *
 */
class Updater extends Thread{
	private boolean killed;
	private final  GraphicInterface gi;
	public Updater(GraphicInterface gi) {
		super();
		killed=false;
		this.gi = gi;
	}
	public void kill() {
		killed=true;
	}
	public void run() {
		while( ! killed) {
			try {
				Thread.sleep(1000/24);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gi.repaint();
		}
	}
}

/**
 * Class in charge of display a Game
 *
 */
public class GraphicInterface extends JPanel{

	private static final long serialVersionUID = -3119582745716440771L;
	
	//VARIABLES
	/**
	 * Game to display
	 */
	private Game game;
	private JFrame frame;
	private boolean linkvisible;
	private boolean boardvisible;
	private Updater demon;
	
	//CONSTRUCTOR
	public GraphicInterface(Game g) {
		super();
		game=g;
		linkvisible=false;
		boardvisible=false;
		
		frame = new JFrame("Loop");
		frame.setSize(new Dimension(500,500));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		demon=new Updater(this);
	}
	
	//METHODS
	public void paintComponent(Graphics graphic) {
			
		super.paintComponent(graphic);
		Graphics2D g = (Graphics2D)graphic;
	
		Color colorTrue = new Color( 19, 213, 86 );
		Color colorFalse = new Color( 206, 45, 5 );  
		Color colorPiece = new Color( 87, 45, 141 );
		Color colorBoard = new Color( 127, 122, 121 );
		Color colorFixed = new Color(143, 148, 255  );
		Color colorError = Color.red;
		
		int taille = Math.max(5, Math.min(super.getSize().width/game.getWidth(),super.getSize().height/game.getLength()));
		int stroke = taille/10;
		int margin;
		if(linkvisible) {
			margin=(int)(taille*0.1);
		}else {
			margin=0;
		}
		
		g.setStroke(new BasicStroke(stroke));
		//Draw lines and column
		g.setColor(colorBoard);
		if(boardvisible) {
			for(int i=0; i<game.getWidth()+1; i++) {
				g.drawLine(taille*i, 0,taille*i, taille*game.getLength());
			}
			for(int j=0; j<game.getLength()+1;j++) {
				g.drawLine(0,taille*j, taille*game.getWidth(),taille*j);
			}
		}
		
		/*
		g.drawLine(0, 0,0, taille*game.getLength());
		g.drawLine(taille*(game.getWidth()), 0,taille*(game.getLength()), taille*game.getWidth());
		g.drawLine(0,0, taille*game.getWidth(),0);
		g.drawLine(0,taille*(game.getLength()), taille*game.getWidth(),taille*(game.getLength()));
		*/
		
		for(Piece piece : this.game){
			
			int j = piece.getLine();
			int i = piece.getColumn();
			int x0 = taille*i+margin,
				y0 = taille*j+margin,
				xM = taille*i+taille/2,
				yM = taille*j+taille/2,
				x1 = taille*(i+1)-margin,
				y1 = taille*(j+1)-margin;
			
			if( piece.getPossibilities()!=null) {
				if(piece.getPossibilities().size()==1) {
					g.setColor(colorFixed);
				}else if(piece.getPossibilities().size()==0) {
					g.setColor(colorError);
				}else {
					g.setColor(colorPiece);
				}
			} else {
				g.setColor(colorPiece);
			}
			
			switch( piece.getId() ) {
			case(0)://Empty
				break;
			case(1)://End
				switch(piece.getOrientation()) {
					case(0):
						g.drawLine(xM, y0,xM, yM);
						break;
					case(1):
						g.drawLine(xM, yM,x1, yM);
						break;
					case(2):
						g.drawLine(xM, yM,xM, y1);
						break;
					case(3):
						g.drawLine(x0, yM,xM, yM);
						break;
				}
				break;
				
			case(2)://Line
				switch(piece.getOrientation()) {
				case(0):
					g.drawLine(xM,y0,xM,y1);
					break;
				case(1):
					g.drawLine(x0,yM,x1,yM);
					break;
				}
				break;
				
			case(3)://T
				switch(piece.getOrientation()) {
				case(0):
					g.drawLine(x0,yM,x1,yM);
					g.drawLine(xM, yM,xM, y0);
					break;
				case(1):
					g.drawLine(xM,y0,xM,y1);
					g.drawLine(xM, yM,x1, yM);
					break;
				case(2):
					g.drawLine(x0,yM,x1,yM);
					g.drawLine(xM, yM,xM, y1);
					break;
				case(3):
					g.drawLine(xM,y0,xM,y1);
					g.drawLine(xM, yM,x0, yM);
					break;	
				}
				break;
				
			case(4)://Plus
				g.drawLine(x0,yM,x1,yM);
				g.drawLine(xM,y0,xM,y1);
				break;
					
			case(5)://L
				switch(piece.getOrientation()) {
				case(0):
					g.drawLine(xM, yM,xM, y0);
					g.drawLine(xM, yM,x1, yM);
					break;
				case(1):
					g.drawLine(xM, yM,xM, y1);
					g.drawLine(xM, yM,x1, yM);
					break;
				case(2):
					g.drawLine(xM, yM,xM, y1);
					g.drawLine(xM, yM,x0, yM);
					break;
				case(3):
					g.drawLine(xM, yM,xM, y0);
					g.drawLine(xM, yM,x0, yM);
					break;	
				}
				break;
			
			}
			if(linkvisible) {
				
				if(piece.getUp().valid()) {
					g.setColor(colorTrue);
					g.fillRect(xM-margin, y0-margin, 2*margin, margin);
				}else {
					g.setColor(colorFalse);
					g.fillRect(xM-margin, y0-margin, 2*margin, margin);
				}
				
				if(piece.getLeft().valid()) {
					g.setColor(colorTrue);
					g.fillRect(x0-margin, yM-margin, margin, 2*margin);
				}else {
					g.setColor(colorFalse);
					g.fillRect(x0-margin, yM-margin, margin, 2*margin);
				}
				
				if(piece.getDown().valid()) {
					g.setColor(colorTrue);
					g.fillRect(xM-margin, y1, 2*margin, margin);
				}else {
					g.setColor(colorFalse);
					g.fillRect(xM-margin, y1, 2*margin, margin);
				}
				
				if(piece.getRight().valid()) {
					g.setColor(colorTrue);
					g.fillRect(x1, yM-margin, margin, 2*margin);
				}else {
					g.setColor(colorFalse);
					g.fillRect(x1, yM-margin, margin, 2*margin);
				}
			}
		}
	}
	/**
	 * Display the GI
	 */
	public void show(){
		frame.setVisible(true);
		frame.add(this);
		demon.start();
	}
	/**
	 * Allowed to wait the GI to be close
	 */
	public void keepWait() {
		try {
			this.demon.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		demon.kill();
	}
	public void setLinksVisibleON(){
		linkvisible=true;
	}
	public void setBoardVisibleON(){
		boardvisible=true;
	}

}


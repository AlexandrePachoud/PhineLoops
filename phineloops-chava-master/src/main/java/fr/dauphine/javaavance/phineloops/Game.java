package fr.dauphine.javaavance.phineloops;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import fr.dauphine.javaavance.phineloops.pieces.Empty;
import fr.dauphine.javaavance.phineloops.pieces.End;
import fr.dauphine.javaavance.phineloops.pieces.L;
import fr.dauphine.javaavance.phineloops.pieces.Line;
import fr.dauphine.javaavance.phineloops.pieces.Piece;
import fr.dauphine.javaavance.phineloops.pieces.Plus;
import fr.dauphine.javaavance.phineloops.pieces.T;


/**
 * Object that represent the Game
 * Pieces, links, etc...
 * 
 */
public class Game implements Iterable<Piece> {
	/**
	 * Number of Piece in width (West-East axe)
	 */
	private final int width;
	/**
	 * Number of Piece in length (North-South axe)
	 */
	private final int length;
	/**
	 * Matrix of all the Piece
	 */
	private final ArrayList<ArrayList<Piece>> GameBoard; // <=> Piece [][] 
	// Line then column ( .get(line).get(column) )

	/**
	 * Set of all the Links used
	 */
	private final Set<Link> allLinks;
	
	/**
	 * Read a File to convert it into Piece, Link, etc.
	 * @param F File without errors
	 */
	private GraphicInterface gi;
	/**
	 * Build a Game from a File :
	 * The file must be like that :
	 * 
	 * Number_of_Lines
	 * Number_of_Columns
	 * Piece(0,0).id Piece(0,0).orientation
	 * PieceId(0,1).id Piece(0,1).orientation
	 * ...
	 * Piece(Number_of_Lines,Number_of_Columns).id Piece(Number_of_Lines,Number_of_Columns).orientation
	 * @param File to read
	 */
	public Game (File F) {
		int tmpWidth=0, tmpLength=0;
		Scanner scan;
		try {
			scan = new Scanner(F);

		} catch (FileNotFoundException e) {
			e.printStackTrace();

			width=tmpWidth;
			length=tmpLength;
			GameBoard=new ArrayList<>();
			allLinks=new HashSet<>();
			return;
		}

		int pieceId, pieceOrientation;
		Piece piece;
		ArrayList<Piece> array;
		
		GameBoard = new ArrayList<>();
		allLinks = new HashSet<>();
		
		if(scan.hasNext())tmpLength=scan.nextInt();
		else tmpLength=0;
		if(scan.hasNext())tmpWidth=scan.nextInt();
		else tmpWidth=0;
		width=tmpWidth;
		length=tmpLength;
		
		//Step 1
		//Creating the pieces
		for(int i=0; i<tmpLength ;i++) {
			array=new ArrayList<>();
			for(int j=0; j<tmpWidth ; j++) {
				pieceId=0;pieceOrientation=0;
				if(!scan.hasNext())System.out.println("error file");
				else {
					pieceId = scan.nextInt();	
				}
				if(!scan.hasNext())System.out.println("error file");
				else {
					pieceOrientation = scan.nextInt();
				}
				switch(pieceId) {
				case(0):
					piece = new Empty();
					break;
				case(1):
					piece = new End();
					break;
				case(2):
					piece = new Line();
					break;
				case(3):
					piece = new T();
					break;
				case(4):
					piece = new Plus();
					break;
				case(5):
					piece = new L();
					break;
				default:
					piece=new Empty();
				}
				piece.setLine(i);
				piece.setColumn(j);
				piece.setOrientation(pieceOrientation);
				array.add(piece);
			}
			GameBoard.add(array);
		}

		//Step 2
		//Creating the links
		
		for(Piece p : this) {
			Link newLink;
			
			if(p.getLine()==0) {
				newLink=new Link();
				allLinks.add(newLink);
			} else newLink = this.get(p.getLine()-1,p.getColumn()).getDown(); 
			p.setUp(newLink);
			newLink.setP2(p);
			
			newLink = new Link();
			allLinks.add(newLink);
			p.setDown(newLink);
			newLink.setP1(p);
			
			if(p.getColumn()==0) {newLink=new Link();allLinks.add(newLink);}
			else newLink=this.get(p.getLine(), p.getColumn()-1).getRight();
			p.setLeft(newLink);
			newLink.setP2(p);
			
			newLink=new Link();
			allLinks.add(newLink);
			p.setRight(newLink);
			newLink.setP1(p);
		}
		
		//Step 3
		//Update all the links
		for(int i=0; i<tmpLength ;i++) {
			for(int j=0; j<tmpWidth ; j++) {
				piece=GameBoard.get(i).get(j);
				piece.rotate();
				piece.rotate();
				piece.rotate();
				piece.rotate();
			}
		}
		scan.close();		
	}
	
	//GETTERS
	public int getWidth() {
		return width;
	}
	
	public int getLength() {
		return length;
	}
	
	//toString
	/**
	 * Return a string on the format of a file
	 */
	public String toString() {
		String s = new String("");
		s+=(this.length+"\n");
		s+=(this.width+"\n");
		
		for(ArrayList<Piece> array : this.GameBoard) {
			for(Piece p : array) {
				s+= p.toString()+"\n";
			}
		}
		System.out.println(s);
		return s;
	}
	
	public void write(PrintWriter w) {
		w.println(this.length);
		w.println(this.width);
		
		for(ArrayList<Piece> array : this.GameBoard) {
			for(Piece p : array) {
				w.println(p.write());
			}
		}
	}
	
	//USEFUL TOOLS
	
	/**
	 * Make easier to browse all the pieces of the game
	 * Up to bottom, left to right, as the way of reading
	 * 
	 * Example : for(Piece p : (Game) game){...}
	 */
	@Override
	public java.util.Iterator<Piece> iterator() {
		return new GameIterator(this);
	}
	
	/**
	 * Return a Piece a coordinate
	 * @param i line
	 * @param j column
	 * @return Piece at the coordinate (i,j)
	 */
	public Piece get(int i, int j) {
		return this.GameBoard.get(i).get(j);
	}

	/**
	 * Method who creates a Window with a simplify sheme off the Game
	 */
	public void displaySimple() {
		gi = new GraphicInterface(this);
		gi.show();
	}
	/**
	 * Method who creates a Window with a complex sheme off the Game
	 * Useful for debuging
	 */
	public void displayAdvanced() {
		gi = new GraphicInterface(this);
		gi.setLinksVisibleON();
		gi.setBoardVisibleON();
		gi.show();
	}

	public void keepGiWait() {
		gi.keepWait();
	}

	public void stopDisplay() {
		if(gi!=null)gi.stop();
	}
	
	public boolean isValid() {
		boolean ok = true;
		for(Link l : this.allLinks) {
			ok &= l.valid();
		}
		return ok;
	}
	
	/**
	 * 
	 * @return a copy of the Game
	 */
	public Game copy() {
		File temporaryFile;
		try {
			temporaryFile = File.createTempFile("copy", ".dat");
			PrintWriter wr;
			try {
				wr = new   PrintWriter(temporaryFile);
				this.write(wr);
		    	wr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Game returnGame = new Game(temporaryFile);
		
		for(Piece p : this) {
			if(p.getPossibilities() != null) {
				returnGame.get(p.getLine(), p.getColumn())
				.setPossibilities(new ArrayList<Integer>(p.getPossibilities()));
			}
		}
		temporaryFile.delete();
		return returnGame;
	}
	
	public byte[] getOrientations() {
		byte [] l = new byte[width*length];
		int i=0;
		for(Piece p : this) {
			l[i++]=(byte)p.getOrientation();
		}return l;
	}
	
	public void setOrientations(byte[] l) {
		int i=0;
		for(Piece p : this) {
			p.setOrientationAndUpdateLinks((int)l[i++]);
		}
		return;	
	}
	
	/**
	 * Put a new Piece in the good place
	 * @param i
	 * @param j
	 * @param newPiece
	 * @param orientation
	 */
	public void put(int i, int j, Piece newPiece) {
		Piece previous = this.get(i, j);
		this.GameBoard.get(i).remove(j);
		this.GameBoard.get(i).add(j, newPiece);
		newPiece.setUp(previous.getUp());
		newPiece.getUp().setP2(newPiece);
		newPiece.setDown(previous.getDown());
		newPiece.getDown().setP1(newPiece);
		newPiece.setLeft(previous.getLeft());
		newPiece.getLeft().setP2(newPiece);
		newPiece.setRight(previous.getRight());
		newPiece.getRight().setP1(newPiece);
		newPiece.setColumn(j);
		newPiece.setLine(i);
	}
	
	public Set<Link> getAllLinks() {
		return allLinks;
	}
	
}

/**
 * Iterator of a Game
 *
 */
class GameIterator implements java.util.Iterator<Piece>{
	private Game g;
	private int i=0,j=0;
	private boolean stop;
	
	public GameIterator(Game g) {
		super();
		this.g=g;
		stop=false;
	}
	
	@Override
	public boolean hasNext() {
		return (!stop);
	}

	@Override
	public Piece next() {
		Piece r = g.get(i, j);
		if(j==(g.getWidth()-1)) {
			j=0;
			if(i==(g.getLength()-1)) {
				stop=true;
			} else {
				i++;
			}	
		} else {
			j++;
		}
		return r;
	}
	
}
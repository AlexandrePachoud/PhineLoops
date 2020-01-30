package fr.dauphine.javaavance.phineloops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import fr.dauphine.javaavance.phineloops.pieces.Empty;
import fr.dauphine.javaavance.phineloops.pieces.End;
import fr.dauphine.javaavance.phineloops.pieces.L;
import fr.dauphine.javaavance.phineloops.pieces.Line;
import fr.dauphine.javaavance.phineloops.pieces.Piece;
import fr.dauphine.javaavance.phineloops.pieces.Plus;
import fr.dauphine.javaavance.phineloops.pieces.T;


public class Generator {
	private final int width;
	private final int length;
	private Game game;
	
	
	public Generator(int l, int w){
		this.width = w;
		this.length = l;
	}
	
	//Final method
	/**
	 * Generate a random game
	 * with a given probability p
	 * 0: lots of Links
	 * 1: less Links
	 * @param p is a probability between 1 and 0
	 * @return game unsolved
	 */
	public Game generate(double p) {
		emptyContructor();
		linkRandomizer(p);
		pieceRecognizer();
		shuffle();
		return game;
	}
	
	/**
	 * Generate a random game
	 * with a probability 0.5 by default
	 * @return a random Game
	 */
	public Game generate() {
		return generate(0.5);
	}
	
	/**
	 * creates a game with empty pieces
	 */
	private void emptyContructor() {
		File temporaryFile;
		try {
			temporaryFile = File.createTempFile("generate", ".dat");
			PrintWriter wr;
			try {
				wr = new   PrintWriter(temporaryFile);
				wr.println(length);
				wr.println(width);
				for(int i=0; i<width*length; i++) {
					wr.println("0 0"); //On fait que des Empty
				}
		    	wr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		game = new Game(temporaryFile);
		temporaryFile.delete();
	}
	
	
	/**
	 * Uses the empty game and generates all links (false for the frontiers of the game
	 * and randomly for others links). Necessary for creating a new game
	 * @param proba is a probabilty of generation
	 */
	private void linkRandomizer(double proba) {
		boolean b;
		
		for(Piece p : game) {
			if(p.getLine() == 0) {
				p.getUp().setLinkB(false, false);
			} else {
				b = randp(proba); 
				p.getUp().setLinkB(b, b);
			}
			
			if(p.getLine() == length - 1) {
				p.getDown().setLinkB(false, false);
			} else {
				b = randp(proba); 
				p.getDown().setLinkB(b, b);
			}
			
			if(p.getColumn() == 0) {
				p.getLeft().setLinkB(false, false);
			} else {
				b = randp(proba);
				p.getLeft().setLinkB(b, b);
			}
			
			if(p.getColumn() == width - 1) {
				p.getRight().setLinkB(false, false);
			} else {
				b = randp(proba);
				p.getRight().setLinkB(b, b);
			}
		}
	}
	
	/**
	 * recognizes the pieces regarding its four links in order to create a valid game
	 * 
	 */
	private void pieceRecognizer() {
		
		for(Piece p : game) {
			int k = 0;
			if(p.getUp().isLinkTrue()) k++;
			if(p.getDown().isLinkTrue())	k++;
			if(p.getLeft().isLinkTrue())	k++;
			if(p.getRight().isLinkTrue()) k++;
			switch(k) {
				case 0:
					game.put(p.getLine(), p.getColumn(), new Empty());
					break;
				case 1:
					game.put(p.getLine(), p.getColumn(), new End());
					break;
				case 2:
					if((p.getUp().getB1() && p.getDown().getB2()) || (p.getLeft().getB2() && p.getRight().getB1())) {
						game.put(p.getLine(), p.getColumn(),new Line());
					}
					else {
						game.put(p.getLine(), p.getColumn(),new L());
					}
					break;
				case 3:
					game.put(p.getLine(), p.getColumn(),new T());
					break;
				case 4:
					game.put(p.getLine(), p.getColumn(),new Plus());
					break;
			}
		}
		
	}
	
	
	/**
	 * rotates randomly all pieces of the valid game
	 * 
	 */
	private void shuffle() {
	 	for(Piece p : game) {
	 		Random r = new Random();
	 		p.rotate();p.rotate();p.rotate();p.rotate();
	 		p.rotate(r.nextInt(4));
		}
	 	
	}
	
	/**
	 * generates a boolean with a given probability p
	 * @param p probability
	 * @return boolean 
	 */	
	private boolean randp(double p) {
		Random r = new Random();
		if(r.nextDouble() < p) {
			return false;
		} else return true;
	}
	
}


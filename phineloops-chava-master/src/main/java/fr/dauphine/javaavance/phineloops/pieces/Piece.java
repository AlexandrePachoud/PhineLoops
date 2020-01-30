package fr.dauphine.javaavance.phineloops.pieces;

import java.util.*;

import fr.dauphine.javaavance.phineloops.Link;

public abstract class Piece {
	// VARIABLES
	protected int column, line;
	protected int orientation;
	protected Link up;
	protected Link right;
	protected Link down;
	protected Link left;
	private ArrayList<Integer> possibilities; // contains all the possible positions for the piece
	
	// SETTERS
	public void setColumn(int column) {
		this.column = column;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void setOrientation(int o) {
		orientation = o;
	}

	public void setUp(Link up) {
		this.up = up;
	}

	public void setDown(Link down) {
		this.down = down;
	}

	public void setLeft(Link left) {
		this.left = left;
	}

	public void setRight(Link right) {
		this.right = right;
	}

	// GET METHODS
	public int getOrientation() {
		return orientation;
	}

	public abstract int getId();

	public Link getDown() {
		return down;
	}

	public Link getLeft() {
		return left;
	}

	public Link getRight() {
		return right;
	}

	public Link getUp() {
		return up;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}

	// METHODS
	public abstract void rotate();
	
	/**
	 * This method rotates x times a piece 90° 
	 * @param
	 */
	public void rotate(int x) {
		int k = x%4;
		if(x>=0) {
			for (int i = 1; i<=k; i++) {
				this.rotate();
			}
		} else {
			k = 4 - k;
			for (int i = 1; i<=k; i++) {
				this.rotate();
			} 
		}
	}
	
	public abstract ArrayList<Integer> getAllowedOrientation();
	
	public String toString() {
		return "(" + this.getLine() + "," + this.getColumn()+")";
	}
	
	public String write() {
		return "" + this.getId() + " " + orientation;
	}

	public ArrayList<Integer> getPossibilities() {
		return possibilities;
	}

	public void setPossibilities(ArrayList<Integer> possibilities) {
		this.possibilities = possibilities;
	}
	
	public abstract void downIsFixed();
	
	public abstract void upIsFixed();
	
	public abstract void leftIsFixed();
	
	public abstract void rightIsFixed();
	
	public void eliminateNeighborPossibilities() {
		Link up 	= 	this.up;
		Link down 	=  	this.down;
		Link left 	=  	this.left;
		Link right =  	this.right;
		Piece p; 
		
		p=up.getP1();
		if(p!=null) {
			p.downIsFixed();
		}
		
		p=down.getP2();
		if(p!=null) {
			p.upIsFixed();
		}
		
		p=left.getP1();
		if(p!=null) {
			p.rightIsFixed();
		}
		
		p=right.getP2();
		if(p!=null) {
			p.leftIsFixed();
		}
	}
	
	public void setOrientationAndUpdateLinks2(Integer integer){
		for(int i=0; i<4; i++)this.rotate();
		while(this.orientation!=integer)this.rotate();
	}
	
	public abstract void setOrientationAndUpdateLinks(Integer integer);
	
}

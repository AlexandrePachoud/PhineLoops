package fr.dauphine.javaavance.phineloops;

import fr.dauphine.javaavance.phineloops.pieces.Piece;

/**
 * Represent the connection between two Pieces
 * if the two Piece are facing each other or both not facing each other then the Link is True
 * if the two Pieces aren't "agreed" then the link is false;
 */
public class Link {
	
	//VARIABLES
	/**
	 * p1 is the upper piece or the left  piece
	 */
	private Piece p1; 	
	/**
	 * p2 is the down  piece or the right piece
	 */
	private	Piece p2;	
	/**
	 * b1 is true if one of the edge of p1 is oriented towards the link
	 */
	private boolean b1;
	/**
	 * b2 is true if one of the edge of p2 is oriented towards the link
	 */
	private boolean b2;
	
	//SETTERS
	public void setB1(boolean b1) {
		this.b1 = b1;
	}
	
	public void setB2(boolean b2) {
		this.b2 = b2;
	}
	
	public void setP1(Piece p1) {
		this.p1 = p1;
	}
	
	public void setP2(Piece p2) {
		this.p2 = p2;
	}
	
	public void setLinkB(boolean b1, boolean b2) {
		this.b1 = b1;
		this.b2 = b2;
	}
	
	//GETTERS
	public Piece getP1() {
		return p1;
	}
	
	public Piece getP2() {
		return p2;
	}
	
	public boolean getB1() {
		return b1;
	}
	
	public boolean getB2() {
		return b2;
	}
	
	//CONSTRUCTORS
	public Link() {
		b1=false;
		b2=false;
		p1=null;
		p2=null;
	}
	
	//METHODS
	/**
	 * A connection is valid if the two Pieces face each other , or not facing each other
	 * They have to be "agree"
	 * @return true if the connection is valid 
	 */
	public boolean valid() {
		return((b1&&b2) || (!b1&&!b2) );
	}
	
	/**
	 * checks if a link is true or not
	 * @return boolean
	 */
	public boolean isLinkTrue() {
		return (b1&&b2);
	}
	
	@Override
	public String toString() {
		String s="";
		if(p1!=null)s+="("+p1.getLine()+","+p1.getColumn()+") ";
		s+=b1+" <--> "+b2;
		if(p2!=null)s+=" ("+p2.getLine()+","+p2.getColumn()+")";
		return(s);
	}
}

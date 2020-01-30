package fr.dauphine.javaavance.phineloops.pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class Empty extends Piece {
	static final int id=0;
	
	@Override
	public void rotate() {
		up.setB2(false);
		down.setB1(false);
		right.setB1(false);
		left.setB2(false);
		return;
	}
	
	public ArrayList<Integer> getAllowedOrientation() {
		return new ArrayList<>(Arrays.asList(0)) ;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public void downIsFixed(){}
	
	@Override
	public void upIsFixed() {}
	
	@Override
	public void leftIsFixed() {}
	
	@Override
	public void rightIsFixed() {}
	
	@Override
	public void setOrientationAndUpdateLinks(Integer integer) {
		this.setOrientation(integer);
		up.setB2(false);
		down.setB1(false);
		right.setB1(false);
		left.setB2(false);}
}

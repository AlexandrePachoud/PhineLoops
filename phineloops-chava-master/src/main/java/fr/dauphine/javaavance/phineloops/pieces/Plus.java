package fr.dauphine.javaavance.phineloops.pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class Plus extends Piece {
	static final int id=4;
	
	@Override
	public void rotate() {
		up.setB2(true);
		down.setB1(true);
		right.setB1(true);
		left.setB2(true);
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
		up.setB2(true);
		down.setB1(true);
		right.setB1(true);
		left.setB2(true);
	}
}

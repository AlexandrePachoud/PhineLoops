package fr.dauphine.javaavance.phineloops.pieces;

import java.util.ArrayList;
import java.util.Arrays;

import fr.dauphine.javaavance.phineloops.solvers.MasterSlaveSolver;

public class L extends Piece {
	static final int id=5;
	
	@Override
	public void rotate() {
		switch(orientation) {
		case(0):
			orientation=1;
			up.setB2(false);
			down.setB1(true);
			left.setB2(false);
			right.setB1(true);
			break; 
		case(1):
			orientation=2;
			left.setB2(true);
			right.setB1(false);
			up.setB2(false);
			down.setB1(true);
			break;
		case(2):
			orientation=3;
			left.setB2(true);
			right.setB1(false);
			down.setB1(false);
			up.setB2(true);
			break;
		case(3):
			orientation=0;
			right.setB1(true);
			left.setB2(false);
			break;
		}
	}
	
	public ArrayList<Integer> getAllowedOrientation() {
		return new ArrayList<>(Arrays.asList(0,1,2,3)) ;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public void downIsFixed(){
		if(down.getB2()) {
			MasterSlaveSolver.removePossibility(this,0);
			MasterSlaveSolver.removePossibility(this,3);
		}else {
			MasterSlaveSolver.removePossibility(this,1);
			MasterSlaveSolver.removePossibility(this,2);
		}
	}
	
	@Override
	public void upIsFixed() {
		if(up.getB1()) {
			MasterSlaveSolver.removePossibility(this,2);
			MasterSlaveSolver.removePossibility(this,1);	
		}else {
			MasterSlaveSolver.removePossibility(this,0);
			MasterSlaveSolver.removePossibility(this,3);	
		}
	}
	
	@Override
	public void leftIsFixed() {
		if(left.getB1()) {
			MasterSlaveSolver.removePossibility(this,0);
			MasterSlaveSolver.removePossibility(this,1);
		}else {
			MasterSlaveSolver.removePossibility(this,2);
			MasterSlaveSolver.removePossibility(this,3);
		}
	}
	
	@Override
	public void rightIsFixed() {
		if(right.getB2()) {
			MasterSlaveSolver.removePossibility(this,3);
			MasterSlaveSolver.removePossibility(this,2);
		}else {
			MasterSlaveSolver.removePossibility(this,1);
			MasterSlaveSolver.removePossibility(this,0);
		}
	}
	
	@Override
	public void setOrientationAndUpdateLinks(Integer integer) {
		switch(integer) {
		case(0):
			orientation=0;
			down.setB1(false);
			right.setB1(true);
			left.setB2(false);
			up.setB2(true);
			break;
		case(1):
			orientation=1;
			left.setB2(false);
			down.setB1(true);
			up.setB2(false);
			right.setB1(true);
			break; 
		case(2):
			orientation=2;
			up.setB2(false);
			left.setB2(true);
			down.setB1(true);
			right.setB1(false);
			break;
		case(3):
			orientation=3;
			right.setB1(false);
			up.setB2(true);
			left.setB2(true);
			down.setB1(false);
			break;
		}
	}	
}

package fr.dauphine.javaavance.phineloops.pieces;

import java.util.ArrayList;
import java.util.Arrays;

import fr.dauphine.javaavance.phineloops.solvers.MasterSlaveSolver;

public class End extends Piece  {
	static final int id=1;
	
	@Override
	public void rotate() {
		switch(orientation) {
		case(0):
			orientation=1;
			up.setB2(false);
			right.setB1(true);
			down.setB1(false);
			left.setB2(false);
			break; 
		case(1):
			orientation=2;
			right.setB1(false);
			down.setB1(true);
			up.setB2(false);
			left.setB2(false);
			break;
		case(2):
			orientation=3;
			down.setB1(false);
			left.setB2(true);
			right.setB1(false);
			up.setB2(false);
			break;
		case(3):
			orientation=0;
			left.setB2(false);
			up.setB2(true);
			right.setB1(false);
			down.setB1(false);
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
			MasterSlaveSolver.removePossibility(this,1);
			MasterSlaveSolver.removePossibility(this,3);
		}else {
			MasterSlaveSolver.removePossibility(this,2);
		}
	}

	@Override
	public void upIsFixed() {
		if(up.getB1()) {
			MasterSlaveSolver.removePossibility(this,2);
			MasterSlaveSolver.removePossibility(this,1);
			MasterSlaveSolver.removePossibility(this,3);
		}else {
			MasterSlaveSolver.removePossibility(this,0);
		}
	}

	@Override
	public void leftIsFixed() {
		if(left.getB1()) {
			MasterSlaveSolver.removePossibility(this,2);
			MasterSlaveSolver.removePossibility(this,1);
			MasterSlaveSolver.removePossibility(this,0);
		}else {
			MasterSlaveSolver.removePossibility(this,3);
		}
	}

	@Override
	public void rightIsFixed() {
		if(right.getB2()) {
			MasterSlaveSolver.removePossibility(this,2);
			MasterSlaveSolver.removePossibility(this,3);
			MasterSlaveSolver.removePossibility(this,0);
		}else {
			MasterSlaveSolver.removePossibility(this,1);
		}
	}
	
	@Override
	public void setOrientationAndUpdateLinks(Integer integer) {
		switch(integer) {
		case(0):
			orientation=0;
			left.setB2(false);
			up.setB2(true);
			right.setB1(false);
			down.setB1(false);
			break;
		case(1):
			orientation=1;
			up.setB2(false);
			right.setB1(true);
			down.setB1(false);
			left.setB2(false);
			break;
		case(2):
			orientation=2;
			right.setB1(false);
			down.setB1(true);
			up.setB2(false);
			left.setB2(false);
			break;
		case(3):
			orientation=3;
			down.setB1(false);
			left.setB2(true);
			right.setB1(false);
			up.setB2(false);
			break;
		}
	}
}

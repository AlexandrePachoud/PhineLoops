package fr.dauphine.javaavance.phineloops.solvers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import fr.dauphine.javaavance.phineloops.Game;
import fr.dauphine.javaavance.phineloops.pieces.Piece;

class SlaveThread extends Thread{
	Game slaveGame;
	LinkedList<Piece> fixed = new LinkedList<>();
	LinkedList<Piece> newfixed = new LinkedList<>();
	LinkedList<Piece> unfixed = new LinkedList<>();
	int returnValue;
	private final Object monitor;
	
	public SlaveThread(
			Game g,
			LinkedList<Piece> fixed,
			LinkedList<Piece> newfixed,
			LinkedList<Piece> unfixed,
			Object monitor
	) {
		slaveGame=g;
		this.fixed=fixed;
		this.newfixed=newfixed;
		this.unfixed=unfixed;
		this.monitor = monitor;
	}
	
	protected static void removePossibility(Piece p, int i) {
		MasterSlaveSolver.removePossibility(p, i);
	}
	
	@Override
	public void run() {
		returnValue = EliminatePossibilities();
		synchronized (monitor) {
			monitor.notify();
		}
		return;	
	}

	int EliminatePossibilities(){
		while( ! slaveGame.isValid()) {
			LinkedList<Piece> unfixedcopy = new LinkedList<>(unfixed);
			for(Piece p : unfixedcopy) {
				if(p.getPossibilities().size()==0){
					return -1;
				}
				if(p.getPossibilities().size()==1) {
					Integer i=0;
					for(Integer j : p.getPossibilities()) {
						i=j;
						break;
					}
					while(p.getOrientation() != i) {
						p.rotate();
					}
					newfixed.add(p);
					unfixed.remove(p);
				}
			}
			if(newfixed.isEmpty()) {
				return 0;
			}
			for(Piece p2 : newfixed) {
				p2.eliminateNeighborPossibilities();
			}
			fixed.addAll(newfixed);
			newfixed.clear();
		}
		return 1;
	}	
}

//--------------------------------------------------------------------------------

public class MasterSlaveSolver implements Solver {
	private Game game;
	private int maxThread;
	private static int nbThreadRunning;
	private LinkedList<Piece> fixed = new LinkedList<>();
	private LinkedList<Piece> newfixed = new LinkedList<>();
	private LinkedList<Piece> unfixed = new LinkedList<>();
	
	private LinkedList<SlaveThread> activSlavesList = new LinkedList<>();
	private int returnValue;
	private LinkedList<SlaveThread> waintingSlavesList = new LinkedList<>();
	
	private final Object monitor = new Object();
	
	public MasterSlaveSolver(Game g, int nbThread) {
		game=g;
		maxThread=Math.min(1, nbThread);
		nbThreadRunning=0;
	}
	
	public boolean solve() {
		initialization();
		
		SlaveThread initialThread=  new SlaveThread(game, fixed, newfixed, unfixed,monitor);
		initialThread.start();
		synchronized (monitor) {
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		activSlavesList.add(initialThread);
		returnValue=0;
		
		if(game.isValid()) {
			returnValue = 1;	
			return true;
		}
		
		while(returnValue==0) {
			synchronized (monitor) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			for(SlaveThread sTh : new LinkedList<>(activSlavesList) ) {
				if(! sTh.isAlive()) {
					game.setOrientations(sTh.slaveGame.getOrientations());
					activSlavesList.remove(sTh);
					switch (sTh.returnValue) {
					case 1:
						returnValue = 1;	
						game.setOrientations(sTh.slaveGame.getOrientations());
						return true;
					case 0:
						if(sTh.unfixed.isEmpty()) {
							if(sTh.slaveGame.isValid()) {
								returnValue = 1;	
								game.setOrientations(sTh.slaveGame.getOrientations());
							}
							return true;
						}
						Random r = new Random();
						Piece chossenP = sTh.unfixed.get(r.nextInt(sTh.unfixed.size()));
						for(Integer o : chossenP.getPossibilities()) {
							
							Game newGame = sTh.slaveGame.copy();
							
							ArrayList<Integer> newpossibilite = new ArrayList<>();
							newpossibilite.add(o);
							newGame.get(chossenP.getLine(), chossenP.getColumn()).setPossibilities(newpossibilite);

							LinkedList<Piece> newFixed = new LinkedList<>();
							for(Piece p : sTh.fixed) {
								newFixed.add(newGame.get(p.getLine(), p.getColumn()));
							}
							LinkedList<Piece> newNewFixed = new LinkedList<>();
							for(Piece p : sTh.newfixed) {
								newNewFixed.add(newGame.get(p.getLine(), p.getColumn()));
							}
							LinkedList<Piece> newUnfixed = new LinkedList<>();
							for(Piece p : sTh.unfixed) {
								newUnfixed.add(newGame.get(p.getLine(), p.getColumn()));
							}
							SlaveThread newSTh = new SlaveThread(
									newGame,
									newFixed,
									newNewFixed,
									newUnfixed,
									monitor
							);
							
							if(nbThreadRunning <=  maxThread) {
								activSlavesList.add(newSTh);
								newSTh.start();
								nbThreadRunning++;
							}
							else {
								waintingSlavesList.add(newSTh);
							}
						}
					}
				}
				if(waintingSlavesList.isEmpty()) {
					nbThreadRunning--;
					if(sTh.returnValue == -1 && activSlavesList.isEmpty()) {
						returnValue = 1;	
						return true ;
					}
				}else {
					waintingSlavesList.getFirst().start();
					activSlavesList.addLast(waintingSlavesList.getFirst());
					waintingSlavesList.remove();
				}
			}
		}
		return false;
	}
	
	public static void removePossibility(Piece p, int i) {
		int indexPossibility = p.getPossibilities().indexOf(i);
		if (indexPossibility!=-1) {
			p.getPossibilities().remove(indexPossibility);
		}
	}
	
	public void initialization() {
		for(Piece p : game) {
			p.setPossibilities(p.getAllowedOrientation()) ;
			unfixed.add(p);
		}
		for(Piece p : unfixed) {
			if(p.getLine()==0) {
				p.upIsFixed();
			}
			if(p.getLine()==game.getLength()-1) {
				p.downIsFixed();
			}
			if(p.getColumn()==0) {
				p.leftIsFixed();
			}
			if(p.getColumn()==game.getWidth()-1) {
				p.rightIsFixed();
			}
		}
	}
}

package fr.dauphine.javaavance.phineloops.solvers;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.dauphine.javaavance.phineloops.Game;
import fr.dauphine.javaavance.phineloops.pieces.Piece;

public class TreeSolver extends Thread implements Solver{

	private final Game game;
	private LinkedList<Piece> fixed , justfixed, unfixed ;
	private boolean solved = false;
	private final Object monitor;
	private final TreeSolver father;
	private boolean running=true;
	private ArrayList<TreeSolver>  branchs = new ArrayList<>(); 
	
	public TreeSolver(Game g){
		super();
		this.game=g;
		this.fixed =new LinkedList<>() ;
		this.justfixed =new LinkedList<>();
		this.unfixed =new LinkedList<>();
		this.monitor=new Object();
		this.father=null;
	}
	
	private TreeSolver(
			Game g,
			LinkedList<Piece> fixed,LinkedList<Piece> justfixed,LinkedList<Piece> unfixed,
			TreeSolver father
	){
		super();
		this.game=g;
		this.fixed =fixed;
		this.justfixed =justfixed;
		this.unfixed = unfixed;
		this.monitor=new Object();
		this.father=father;
	}

	private void kill() {
		running=false;
		synchronized (branchs) {
			for(TreeSolver th : branchs) {
				if(th.running) {
					th.kill();
				}
			}
		}
		synchronized (this.monitor) {
			this.monitor.notify();
		}
	}
	
	private void initialization() {
		for(Piece p: game) {
			p.setPossibilities(p.getAllowedOrientation());
			unfixed.add(p);
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
	
	private Piece selectGoodPiece(LinkedList<Piece> unfixed) {
		Piece pmax = unfixed.getFirst();
		return pmax;
	}
	
	public boolean solve() {
		initialization();
		this.start();
		try {this.join();}
		catch (InterruptedException e){System.out.println("cant join");}
		return solved;
	}

	public void run() {
		//System.out.println(this.getName()+" start running");
		while( ! game.isValid() && running) {
			//System.out.println(this.getName()+" unfixed :"+unfixed);
			for(Piece p : new LinkedList<>(unfixed) ) {
				//System.out.println(super.getName()+" "+p+" possibilities "+p.getPossibilities());
				if(p.getPossibilities().size() == 1) {
					justfixed.add(p);
					p.setOrientationAndUpdateLinks2(p.getPossibilities().get(0));
					unfixed.remove(p);
				}
				else if(p.getPossibilities().size() == 0) {
					return;
				}
			}
			//System.out.println(super.getName()+" justfixed "+justfixed);
			
			//If nothing has been just fixed then we have to set orientation of a random Piece
			if(justfixed.isEmpty()) {
				Piece chosenPiece = selectGoodPiece(unfixed);
				//unfixed.remove(chosenPiece);
				//justfixed.add(chosenPiece);
				synchronized (branchs) {
					for(Integer chosenOrientation : chosenPiece.getPossibilities()) {
						ArrayList<Integer> newPossibilities = new ArrayList<>();
						newPossibilities.add(chosenOrientation);
						
						chosenPiece.setPossibilities(newPossibilities);
						Game modifiedCopy = game.copy();
						Piece copyChosenPiece = modifiedCopy.get(chosenPiece.getLine(), chosenPiece.getColumn());
						copyChosenPiece.setOrientationAndUpdateLinks(chosenOrientation);
						copyChosenPiece.eliminateNeighborPossibilities();
						//System.out.println(copyChosenPiece+" "+copyChosenPiece.write());
						
						LinkedList<Piece> unfixedCopy = new LinkedList<>();
						for(Piece p: unfixed) {
							unfixedCopy.add(modifiedCopy.get(p.getLine(), p.getColumn()));
						}
						LinkedList<Piece> fixedCopy = new LinkedList<>();
						for(Piece p: fixed) {
							fixedCopy.add(modifiedCopy.get(p.getLine(), p.getColumn()));
						}
						LinkedList<Piece> justfixedCopy = new LinkedList<>();
						for(Piece p: justfixed) {
							justfixedCopy.add(modifiedCopy.get(p.getLine(), p.getColumn()));
						}
						
						TreeSolver th = new TreeSolver(modifiedCopy, fixedCopy, justfixedCopy, unfixedCopy,this);
						branchs.add(th);
						//System.out.println(this.getName()+" lance "+th.getName()+" avec "+chosenPiece +"="+chosenOrientation+" reelement"+copyChosenPiece.getOrientation());

						th.start();
					}
				}
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(TreeSolver th: branchs) {
							try {
								th.join();
								if(th.solved) {
									return;
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if(!running || solved) {
							return;
						}else {
							running=false;
							synchronized (monitor) {
								monitor.notify();
							}
						}
					}
				}).start();

				synchronized (monitor) {
					while(running && !solved) {
						try {
							monitor.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				//System.out.println(this.getName()+"-- out wait");
				
				if(solved) {
					if(father!=null) {
						synchronized (game) {
							synchronized (father.game) {
								father.game.setOrientations(this.game.getOrientations());
							}
						}
						father.solved=true;
						father.kill();
						synchronized (father.monitor){
							father.monitor.notify();
						}
						return;
					}else { //si je suis la racine
						solved = true;
					}
				}
				this.kill();
				return;
			}
			
			//For all just fixed piece we elimminate the possibilities of neighbor possibilities
			else for(Piece p : justfixed) p.eliminateNeighborPossibilities();
			fixed.addAll(justfixed);
			justfixed.clear();
		}
		
		if(running) {
			solved = true;
			if(father!=null) {
				father.solved = true;
				synchronized (game) {
					synchronized (father.game) {
						father.game.setOrientations(this.game.getOrientations());
					}
				}
				father.kill();
				synchronized (father.monitor) {
					father.monitor.notify();
				}
			}else {
				this.kill();
			}
		}
	}
}
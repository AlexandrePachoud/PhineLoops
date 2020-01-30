package fr.dauphine.javaavance.phineloops.solvers;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.dauphine.javaavance.phineloops.Game;
import fr.dauphine.javaavance.phineloops.Link;
import fr.dauphine.javaavance.phineloops.pieces.Piece;

public class ZoneSolver implements Solver {
	private final Game game;
	private LinkedList<Piece> unfixed ;
	
	public ZoneSolver(Game g){
		super();
		this.game=g;
		this.unfixed =new LinkedList<>();
	}
	/**
	 * Initiate all the possibilities
	 */
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
	/**
	 * Return a List of List of Piece equivalent to zones of interest
	 * @param List of Piece to cut in zones
	 * @return
	 */
	private static LinkedList<LinkedList<Piece>> getZones(LinkedList<Piece> unfixed){
		//System.out.println(unfixed);
		LinkedList<LinkedList<Piece>> list = new LinkedList<>();
		LinkedList<LinkedList<Piece>> retenue;
		for(Piece p : unfixed) {
			//System.out.println("-"+p);
			retenue=new LinkedList<>();
			for(LinkedList<Piece> zone : list) {
				for(Piece voisin : zone) {
					if(p.getUp().getP1() == voisin || 
					   p.getLeft().getP1() == voisin ||
					   p.getDown().getP2() == voisin||
					   p.getRight().getP2() == voisin) {
						retenue.add(zone);	
						break;
					}
				}
			}
			if(retenue.isEmpty()) {
				LinkedList<Piece> newZone = new LinkedList<Piece>();
				newZone.add(p);
				list.add(newZone );
			}
			if(retenue.size()==1) {
				retenue.get(0).add(p);
			}
			if(retenue.size()>1) {
				LinkedList<Piece> zonegoMergeIn = retenue.get(0);
				zonegoMergeIn.add(p);
				retenue.remove(zonegoMergeIn);
				for(LinkedList<Piece> zonetoMerge : retenue) {
					zonegoMergeIn.addAll(zonetoMerge);
					list.remove(zonetoMerge);
				}
			}
		}
		return list;
	}
	
	public boolean solve() {
		initialization();
		return zoneSolving(unfixed,false);
	}
	
	public boolean zoneSolving(LinkedList<Piece> list, boolean needinitialize) {
		LinkedList<Piece> recentlyfixed;
		LinkedList<Link> interstingLinks = new LinkedList<>();
		if(needinitialize) {
			for(Piece p : list) {
				interstingLinks.add(p.getUp());
				interstingLinks.add(p.getDown());
				interstingLinks.add(p.getLeft());
				interstingLinks.add(p.getRight());
			}
		}else {
			interstingLinks=new LinkedList<>(game.getAllLinks());
		}
		
		boolean valid = true;
		for(Link l : interstingLinks) {
			valid &= l.valid();
		}
		while( ! valid && !list.isEmpty() ) {
			recentlyfixed=new LinkedList<>();
			for(Piece p : list ) {
				if(p.getPossibilities().size() == 1) {
					recentlyfixed.add(p);
					p.setOrientationAndUpdateLinks(p.getPossibilities().get(0));
				}
				else if(p.getPossibilities().size() == 0) {
					return false;
				}
			}
			list.removeAll(recentlyfixed);
			if(recentlyfixed.isEmpty()) {
				break;
			}
			else for(Piece p : recentlyfixed) p.eliminateNeighborPossibilities();
			
			valid = true;
			for(Link l : interstingLinks) {
				valid &= l.valid();
			}
			if(valid) {
				recentlyfixed=new LinkedList<>();
				for(Piece p : list ) {
					if(p.getPossibilities().size() == 1) {
						recentlyfixed.add(p);
						p.setOrientationAndUpdateLinks(p.getPossibilities().get(0));
					}
					else if(p.getPossibilities().size() == 0) {
						return false;
					}
				}
				list.removeAll(recentlyfixed);
				for(Piece p : recentlyfixed) p.eliminateNeighborPossibilities();
			}
		}
		if(valid) {
			return true;
		}
		//String s = ""; for(int i=0; i<recur; i++) s+="|";System.out.println(s+"les zones sont :"+getZones(list));
		for(LinkedList<Piece> zone : getZones(list)) {
			//s = "";for(int i=0; i<recur; i++) s+="|";System.out.println(s + zone);
			boolean zonevalid = false;
			Piece chossenPiece = zone.getFirst();
			
			ArrayList<ArrayList<Integer>> memPossibilities = new ArrayList<>();
			for(Piece p : zone) {
				memPossibilities.add(p.getPossibilities());
			}
			
			for(Integer chossenOrientation : new LinkedList<>(chossenPiece.getPossibilities()) ) {
				int i=0;
				for(Piece p : zone) {
					p.setPossibilities(new ArrayList<>(memPossibilities.get(i++)));
				}
				chossenPiece.setOrientationAndUpdateLinks(chossenOrientation);
				ArrayList<Integer> newPoss = new ArrayList<>();
				newPoss.add(chossenOrientation);
				chossenPiece.setPossibilities(newPoss);
				if(zoneSolving(new LinkedList<>(zone),true)) {
					zonevalid = true;
					break;
				}
			}
			if(!zonevalid){
				return false;
			}
		}
		return true;
	}
}
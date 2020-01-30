package fr.dauphine.javaavance.phineloops.solvers;

import java.util.LinkedList;

import fr.dauphine.javaavance.phineloops.Game;
import fr.dauphine.javaavance.phineloops.Link;
import fr.dauphine.javaavance.phineloops.pieces.Piece;
/**
 * A solver who use the AStar algorithm to solve the Game
 * 
 * The heuristic is the number of Links who are true;
 */
public class AStarSolver implements Solver{

	private final Game game;
	
	public AStarSolver(Game g) {
		game=g;
	}
	
	private Integer h(Game g) {
		int v=0;
		for(Link l : g.getAllLinks()) {
			if(! l.valid()) {
				v++;
			}
		}
		return v;
	}
	
	private void sortedAdd(
			byte[] o,
			Integer v,
			int c,
			LinkedList<byte[]> listO,
			LinkedList<Integer> listV, 
			LinkedList<Integer> listC
	) {
		
		if(listV.isEmpty()) {
			listO.add(o);
			listV.add(v);
			listC.add(c);
		}
		
		int i=0;
		for(Integer ele : listV) {
			if(ele>v) {
				listO.add(i, o);
				listV.add(i, v);
				listC.add(c);
				break;
			}
			i++;
		}
	}
	
	public boolean solve(){
		LinkedList<byte[]> listOTotal = new LinkedList<>();
		LinkedList<byte[]> listO = new LinkedList<>();
		LinkedList<Integer> listV = new LinkedList<>();
		LinkedList<Integer> listC = new LinkedList<>();
		byte[] o;
		Integer c;
		
		sortedAdd(game.getOrientations(),h(game),0,listO,listV,listC);
		listOTotal.add(game.getOrientations());
		
		int t;
		while(! game.isValid()) {
			o=listO.pop();
			listV.pop();	
			c=listC.pop();
			game.setOrientations(o);
			for(Piece p : game) {
				if(p.getId()==0 || p.getId()==4) {
					continue;
				}
				if(! (p.getDown().valid() && p.getUp().valid() && p.getRight().valid() && p.getLeft().valid()) ) {
					if(p.getId()==2)t=2;
					else t=4;
					for(int r=1; r<t ; r++) {
						p.rotate(r);
						o=game.getOrientations();
						if(! listOTotal.contains(o)) {
							sortedAdd(o,h(game)+c+1,c+1,listO,listV,listC);
							listOTotal.add(o);
						}
						p.rotate(4-r);
					}
				}
			}
		}
		return true;		
	}
}

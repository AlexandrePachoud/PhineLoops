package fr.dauphine.javaavance.phineloops.solvers;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.Game;
import fr.dauphine.javaavance.phineloops.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public class ChocoSolver implements Solver{
	
	private final Game game;
	private Map<Piece,IntVar> var;
	
	public ChocoSolver(Game game){
		this.game=game;
		var = new HashMap<>();
	}
	
	private boolean unsolvable() {
		return false;
	}
	
	public boolean solve() {

        // 1. Create a Model
        Model model = new Model("phineloop problem");

        // 2. Create variables	
        for (Piece p: game) {
        	String name = "("+p.getLine()+","+p.getColumn()+")";
        	IntVar intvar;
        	if(p.getId()==0 || p.getId()==4) {
        		intvar = model.intVar(name,0);
        		var.put(p, intvar);
        	}
        	else if(p.getId()==2){
        		intvar = model.intVar(name,0,1);
        		var.put(p, intvar);
        	}
        	else {
        		intvar = model.intVar(name,0,3);
        		var.put(p, intvar);
        	}
        }
        
        // 3. Create constraints
        for(Piece p : game) {
        	Piece up = p.getUp().getP1();
        	Piece left = p.getLeft().getP1();
        	Piece down = p.getDown().getP2();
        	Piece right = p.getRight().getP2();
        	
        	try {	
	        	switch(p.getId()) {
	        	case 0:
	        		if(up==null) {}
	        		else switch(up.getId()) {
	        		case 0 :
	        			break;
	        		case 1 :
	        			model.arithm(var.get(up),"!=",2).post();
	        			break;
	        		case 2 :
	        			model.arithm(var.get(up),"=",1).post();
	        			break;
	        		case 3 :
	        			model.arithm(var.get(up),"=",0).post();
	        			break;
	        		case 4 :
	        			unsolvable();
	        			break;
	        		case 5 :
	        			model.or(
	        				model.arithm(var.get(up),"=",0),
	        				model.arithm(var.get(up),"=",3)
	            		).post();
	        			break;
	        		}
	        		
	        		if(left==null) {}
	        		else switch (left.getId()) {
					case 0:
						break;
					case 1 :
	        			model.arithm(var.get(left),"!=",1).post();
	        			break;
	        		case 2 :
	        			model.arithm(var.get(left),"=",0).post();
	        			break;
	        		case 3 :
	        			model.arithm(var.get(left),"=",3).post();
	        			break;
	        		case 4 :
	        			unsolvable();
	        			break;
	        		case 5 :
	        			model.or(
	        				model.arithm(var.get(left),"=",2),
	        				model.arithm(var.get(left),"=",3)
	            		).post();
	        			break;
	        		}
	        		break;
	        	
	        	case 1 :
	        		if(down==null) {
	        			model.arithm(var.get(p),"!=",2).post();
	        		}
	        		if(right==null) {
	        			model.arithm(var.get(p),"!=",1).post();
	        		}
	        		if(up==null) {
	        			model.arithm(var.get(p),"!=",0).post();
	        		}
	        		else switch(up.getId()) {
	        		case 0 :
	        			model.arithm(var.get(p),"!=",0).post();
	        			break;
	        		case 1 :
	        			model.or(
	        				model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(up),"=",2)),
	        				model.and(model.arithm(var.get(p),"!=",0),model.arithm(var.get(up),"!=",2))
	        			).post();
	        			break;
	        		case 2 :
	        			model.or(
	        				model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(up),"=",0)),
	        				model.and(model.arithm(var.get(p),"!=",0),model.arithm(var.get(up),"=",1))
	        			).post();
	        			break;
	        		case 3 :
	        			model.or(
	        				model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(up),"!=",0)),
	        				model.and(model.arithm(var.get(p),"!=",0),model.arithm(var.get(up),"=",0))
	        			).post();
	        			break;	
	        		case 4 :
	        			model.arithm(var.get(p),"=",0).post();
	        			break;	
	        		case 5 :
	        			model.or(
	            			model.and(model.arithm(var.get(p),"=",0),model.or(model.arithm(var.get(up),"=",1),model.arithm(var.get(up),"=",2))),
	            			model.and(model.arithm(var.get(p),"!=",0),model.or(model.arithm(var.get(up),"=",0),model.arithm(var.get(up),"=",3)))
	            		).post();
	            		break;		
	        		}

	        		if(left==null) {
	        			model.arithm(var.get(p),"!=",3).post();
	        		}
	        		else switch(left.getId()) {
	        		case 0 :
	        			model.arithm(var.get(p),"!=",3).post();
	        			break;
	        		case 1 :
	        			model.or(
	        				model.and(model.arithm(var.get(p),"=",3),model.arithm(var.get(left),"=",1)),
	        				model.and(model.arithm(var.get(p),"!=",3),model.arithm(var.get(left),"!=",1))
	        			).post();
	        			break;
	        		case 2 :
	        			model.or(
	        				model.and(model.arithm(var.get(p),"=",3),model.arithm(var.get(left),"=",1)),
	        				model.and(model.arithm(var.get(p),"!=",3),model.arithm(var.get(left),"=",0))
	        			).post();
	        			break;
	        		case 3 :
	        			model.or(
	        				model.and(model.arithm(var.get(p),"=",3),model.arithm(var.get(left),"!=",3)),
	        				model.and(model.arithm(var.get(p),"!=",3),model.arithm(var.get(left),"=",3))
	        			).post();
	        			break;
	        		case 4 :
	        			model.arithm(var.get(p),"=",3).post();
	        			break;
	        		case 5 :
	        			model.or(
	            			model.and(model.arithm(var.get(p),"=",3),model.or(model.arithm(var.get(left),"=",0),model.arithm(var.get(left),"=",1))),
	            			model.and(model.arithm(var.get(p),"!=",3),model.or(model.arithm(var.get(left),"=",2),model.arithm(var.get(left),"=",3)))
	            		).post();
	            		break;
	        		}
	        		break;
	        	
	        	case 2:
	        		if(down==null) {
	        			model.arithm(var.get(p),"=",1).post();
	        		}
	        		if(right==null) {
	        			model.arithm(var.get(p),"=",0).post();
	        		}
	        		if(up==null) {
	        			model.arithm(var.get(p),"=",1).post();
	        		}
	        		else switch(up.getId()) {
	        		case 0 :
	        			model.arithm(var.get(p),"=",1).post();
	        			break;
	        		case 1 :
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(up),"=",2)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(up),"!=",2))
	        			).post();
	        			break;
	        		case 2:
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(up),"=",0)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(up),"=",1))
	        			).post();
	        			break;
	        		case 3:
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(up),"!=",0)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(up),"=",0))
	        			).post();
	        			break;
	        		case 4:
	        			model.arithm(var.get(p),"=",0).post();
	        			break;
	        		case 5:
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.or(model.arithm(var.get(up),"=",1), model.arithm(var.get(up),"=",2))),
	        					model.and(model.arithm(var.get(p),"=",1),model.or(model.arithm(var.get(up),"=",0), model.arithm(var.get(up),"=",3)))
	        			).post();
	        			break;
	        		}
	        		
	        		if(left==null) {
	        			model.arithm(var.get(p),"=",0).post();
	        		}
	        		else switch(left.getId()) {
	        		case 0 :
	        			model.arithm(var.get(p),"=",0).post();
	        			break;
	        		case 1 :
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(left),"!=",1)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(left),"=",1))
	        			).post();
	        			break;
	        		case 2:
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(left),"=",0)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(left),"=",1))
	        			).post();
	        			break;
	        		case 3:
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.arithm(var.get(left),"=",3)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(left),"!=",3))
	        			).post();
	        			break;
	        		case 4:
	        			model.arithm(var.get(p),"=",1).post();
	        			break;
	        		case 5:
	        			model.or(
	        					model.and(model.arithm(var.get(p),"=",0),model.or(model.arithm(var.get(left),"=",3), model.arithm(var.get(left),"=",2))),
	        					model.and(model.arithm(var.get(p),"=",1),model.or(model.arithm(var.get(left),"=",1), model.arithm(var.get(left),"=",0)))
	        			).post();
	        			break;
	        		}
	        		break;
	        	
	        	case 3 : 
	        		if(down==null) {
	        			model.arithm(var.get(p),"=",0).post();
	        		}
	        		if(right==null) {
	        			model.arithm(var.get(p),"=",3).post();
	        		}
	        		if(up==null) {
	        			model.arithm(var.get(p),"=",2).post();
	        		}
	        		else switch(up.getId()) {
	        		case 0 : 
	        			model.arithm(var.get(p),"=",2).post();
	        			break;
	        		case 1 :
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",2),model.arithm(var.get(up),"=",2)),
	        					model.and(model.arithm(var.get(p),"=",2),model.arithm(var.get(up),"!=",2))
	        			).post();
	        			break;
	        		case 2 : 
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",2),model.arithm(var.get(up),"=",0)),
	        					model.and(model.arithm(var.get(p),"=",2),model.arithm(var.get(up),"=",1))
	        			).post();
	        			break;
	        		case 3 : 
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",2),model.arithm(var.get(up),"!=",0)),
	        					model.and(model.arithm(var.get(p),"=",2),model.arithm(var.get(up),"=",0))
	        			).post();
	        			break;
	        		case 4 : 
	        			model.arithm(var.get(p),"!=",2).post();
	        			break;
	        		case 5 :
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",2),model.or(model.arithm(var.get(up),"=",1),model.arithm(var.get(up),"=",2))),
	        					model.and(model.arithm(var.get(p),"=",2),model.or(model.arithm(var.get(up),"=",0),model.arithm(var.get(up),"=",3)))
	        			).post();
	        			break;
	        		}
	        		
	        		if(left==null) {
	        			model.arithm(var.get(p),"=",1).post();
	        		}
	        		else switch(left.getId()) {
	        		case 0 : 
	        			model.arithm(var.get(p),"=",1).post();
	        			break;
	        		case 1 :
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",1),model.arithm(var.get(left),"=",1)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(left),"!=",1))
	        			).post();
	        			break;
	        		case 2 : 
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",1),model.arithm(var.get(left),"=",1)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(left),"=",0))
	        			).post();
	        			break;
	        		case 3 : 
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",1),model.arithm(var.get(left),"!=",3)),
	        					model.and(model.arithm(var.get(p),"=",1),model.arithm(var.get(left),"=",3))
	        			).post();
	        			break;
	        		case 4 : 
	        			model.arithm(var.get(p),"!=",1).post();
	        			break;
	        		case 5 :
	        			model.or(
	        					model.and(model.arithm(var.get(p),"!=",1),model.or(model.arithm(var.get(left),"=",1),model.arithm(var.get(left),"=",0))),
	        					model.and(model.arithm(var.get(p),"=",1),model.or(model.arithm(var.get(left),"=",2),model.arithm(var.get(left),"=",3)))
	        			).post();
	        			break;
	        		}
	        		break;
	        	
	        	case 4 : 
	        		if(down==null) {
	        			unsolvable();
	        		}
	        		if(right==null) {
	        			unsolvable();
	        		}
	        		if(up==null) {
	        			unsolvable();
	        		}
	        		else switch(up.getId()) {
	        		case 0 :
	        			unsolvable();
	        			break;
	        		case 1 : 
	        			model.arithm(var.get(up),"=",2).post();
	        			break;
	        		case 2 : 
	        			model.arithm(var.get(up),"=",0).post();
	        			break;
	        		case 3 : 
	        			model.arithm(var.get(up),"!=",0).post();
	        			break;
	        		case 4: break;
	        		case 5 :
	        			model.or(model.arithm(var.get(up),"=",1),model.arithm(var.get(up),"=",2)).post();
	        			break;
	        		}
	        		
	        		if(left==null) {
	        			unsolvable();
	        		}
	        		else switch(left.getId()) {
	        		case 0 :
	        			unsolvable();
	        			break;
	        		case 1 : 
	        			model.arithm(var.get(left),"=",1).post();
	        			break;
	        		case 2 : 
	        			model.arithm(var.get(left),"=",1).post();
	        			break;
	        		case 3 : 
	        			model.arithm(var.get(left),"!=",3).post();
	        			break;
	        		case 4: break;
	        		case 5 :
	        			model.or(model.arithm(var.get(left),"=",1),model.arithm(var.get(left),"=",0)).post();
	        			break;
	        		}
	        		break;
	        		
	        	case 5:
	        		if(down==null) {
	        			model.or(
	        					model.arithm(var.get(p),"=",3),
	        					model.arithm(var.get(p),"=",0)
	        			).post();
	        		}
	        		if(right==null) {
	        			model.or(
	        					model.arithm(var.get(p),"=",2),
	        					model.arithm(var.get(p),"=",3)
	        			).post();
	        		}
	        		if(up==null) {
	        			model.or(
	        					model.arithm(var.get(p),"=",1),
	        					model.arithm(var.get(p),"=",2)
	        			).post();
	        		}
	        		else switch(up.getId()) {
	        		case 0 : 
	        			model.or(
	        					model.arithm(var.get(p),"=",1),
	        					model.arithm(var.get(p),"=",2)
	        			).post();
	        			break;
	        		case 1 :
	        			model.or(
	        				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",2)), model.arithm(var.get(up),"!=",2)),	
	        				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",0)), model.arithm(var.get(up),"=",2))
	        			).post();
	        			break;
	        		case 2:
	        			model.or(
	            				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",2)), model.arithm(var.get(up),"=",1)),	
	            				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",0)), model.arithm(var.get(up),"=",0))
	        			).post();
	            		break;
	        		case 3 :
	        			model.or(
	            				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",2)), model.arithm(var.get(up),"=",0)),	
	            				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",0)), model.arithm(var.get(up),"!=",0))
	        			).post();
	            		break;
	        		case 4 :
	        			model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",0)).post();
	        			break;
	        		case 5 :
	        			model.or(
	            				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",2)), model.or(model.arithm(var.get(up),"=",0),model.arithm(var.get(up),"=",3))),	
	            				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",0)), model.or(model.arithm(var.get(up),"=",1),model.arithm(var.get(up),"=",2)))
	        			).post();
	            		break;
	        		}
	        		
	        		if(left==null) {
	        			model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",0)).post();
	        		}
	        		else switch(left.getId()) {
	        		case 0 :
	        			model.or(
	        					model.arithm(var.get(p),"=",1),
	        					model.arithm(var.get(p),"=",0)
	        			).post();
	        			break;
	        		case 1 :
	        			model.or(
	        				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",0)), model.arithm(var.get(left),"!=",1)),	
	        				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",2)), model.arithm(var.get(left),"=",1))
	        			).post();
	        			break;
	        		case 2:
	        			model.or(
	            				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",0)), model.arithm(var.get(left),"=",0)),	
	            				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",2)), model.arithm(var.get(left),"=",1))
	        			).post();
	            		break;
	        		case 3 :
	        			model.or(
	            				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",0)), model.arithm(var.get(left),"=",3)),	
	            				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",2)), model.arithm(var.get(left),"!=",3))
	        			).post();
	            		break;
	        		case 4 :
	        			model.or(model.arithm(var.get(p),"=",2),model.arithm(var.get(p),"=",3)           			
	        			).post();
	        			break;
	        		case 5 :
	        			model.or(
	            				model.and(model.or(model.arithm(var.get(p),"=",1),model.arithm(var.get(p),"=",0)), model.or(model.arithm(var.get(left),"=",2),model.arithm(var.get(left),"=",3))),	
	            				model.and(model.or(model.arithm(var.get(p),"=",3),model.arithm(var.get(p),"=",2)), model.or(model.arithm(var.get(left),"=",1),model.arithm(var.get(left),"=",0)))
	        			).post();
	            		break;
	        		}
	        		break;
        	}
	        }catch (NullPointerException e) {
				return false;
			}
        }

        // 4. Solve the problem
        model.getSolver().solve();
        
        for(Piece p_final : game) {
        	Integer i = var.get(p_final).getValue();
        	while(p_final.getOrientation() != i) {
        		p_final.rotate();
        	}
        }
        return true;
    }
}

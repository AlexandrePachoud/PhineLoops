package fr.dauphine.javaavance.phineloops; 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import fr.dauphine.javaavance.phineloops.pieces.Piece;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class MainTest extends TestCase
{
    /**
     * Rigourous Test :-)
     */
    
    public void testGameConstructor() {
    	
    	Random r= new Random();
    	File temporaryFile;
    	int width = r.nextInt(900)+100;
    	int length = r.nextInt(900)+100;
		try {
			temporaryFile = File.createTempFile("generate", ".dat");
			PrintWriter wr;
			try {
				wr = new   PrintWriter(temporaryFile);
				wr.println(length);
				wr.println(width);
				
				for(int i=0; i<width*length; i++) {
					
					wr.println(r.nextInt(6)+" 0"); //On fait que des Empty
				}
		    	wr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Game testgame = new Game(temporaryFile);
		
		//Check if everthing good
		
		Scanner sc;
		try {
			sc = new Scanner(temporaryFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assertFalse( true );
			return;
		}
		
		if(sc.hasNextInt()) {
			if(sc.nextInt() == (testgame.getLength())) assertTrue( true );
			else assertFalse( true );
		}
		else assertFalse( true );
		
		if(sc.hasNextInt()) {
			if(sc.nextInt()== testgame.getWidth()) assertTrue( true );
			else assertFalse( true );
		}
		else assertFalse( true );
		
		for(Piece p : testgame) {
			if(sc.hasNextInt()) {
				if(sc.nextInt()==(p.getId())) assertTrue( true );
				else assertFalse( true );
			}else assertFalse( true );
			
			if(sc.hasNextInt()) {
				if(sc.nextInt()==(p.getOrientation())) assertTrue( true );
				else assertFalse( true );
			}else assertFalse( true );
		}
		sc.close();
		temporaryFile.delete();
    	
    }
   
    public void testGenerator() {
    	int width = 5;
    	int length = 5;
    	Generator gen = new Generator(length, width);
    	Game g = gen.generate();
    	assertFalse(g.isValid());
    	
    	Main.testIt();
    }
    
    
}

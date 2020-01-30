package fr.dauphine.javaavance.phineloops;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.dauphine.javaavance.phineloops.solvers.*;

public class Main {
    private static String inputFile = null;  
    private static String outputFile = null;
    private static Integer width = -1;
    private static Integer height = -1;
    private static Integer maxcc = -1;
	private static boolean withGI=false; 
	
	@SuppressWarnings("unused")
	private static Integer threads=Integer.MAX_VALUE; 
    

    public static void generate(int width, int height, String outputFile){
    	Generator generator = new Generator(width, height);
    	Game game = generator.generate();
    	if(withGI)game.displayAdvanced();
    	File fout = new File(outputFile);
    	if(! fout.exists()) {
    		try {
				fout.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	PrintWriter wr;
		try {
			wr = new   PrintWriter(fout);
			game.write(wr);
	    	wr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(withGI)game.keepGiWait();
    	
    }

    public static boolean solve(String inputFile, String outputFile){
    	boolean solveBool;
    	
    	File f = new File(inputFile);
    	Game g = new Game(f);
    	if(withGI)g.displayAdvanced();
    	//MultiThreadSolver solMT = new MultiThreadSolver(g, threads);
    	//solMT.solve();
    	//solveBool = solMT.returnValue==1;
    	
    	//CPModel cp = new CPModel(g);
    	//solveBool = cp.solve();
    	
    	//TreeSolver sol = new TreeSolver(g);
    	
    	ZoneSolver sol = new ZoneSolver(g);
    	solveBool=sol.solve();
    	
    	
    	
    	File fout = new File(outputFile);
    	if(! fout.exists()) {
    		try {
				fout.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	PrintWriter wr;
		try {
			wr = new   PrintWriter(fout);
			g.write(wr);
	    	wr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(withGI)g.keepGiWait();
		return solveBool;  
    }

    private static boolean check(String inputFile){
    	File f = new File(inputFile);
    	Game g = new Game(f);
    	if(withGI)g.displayAdvanced();
    	boolean solved = g.isValid();
    	if(withGI)g.keepGiWait();
    	return solved;
    }
    
    public static void main(String[] args) {
    	/*
    	int[] tailles = new int[]{3,5,7,10,20,30,50,80,150,300,500};
    	for(int i=0; i<tailles.length;i++) {
    		File file = new File("./instances/private/"+tailles[i]+"x"+tailles[i]+".dat");
        	if(! file.exists()) {
        		try {
        			file.createNewFile();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        	PrintWriter wr;
    		try {
    			wr = new   PrintWriter(file);
    			Game g = new Generator(tailles[i],tailles[i]).generate();
    			g.displayAdvanced();
    			g.write(wr);
    			System.out.println(tailles[i]+" fait");
    			wr.close();
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
      		
    	}
    	
    	
    	if(2!=1)return;
    	*/
    	
    	
    	
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        
        options.addOption("g", "generate ", true, "Generate a grid of size height x width.");
        options.addOption("c", "check", true, "Check whether the grid in <arg> is solved.");
        options.addOption("s", "solve", true, "Solve the grid stored in <arg>.");   
        
        options.addOption("o", "output", true, "Store the generated or solved grid in <arg>. (Use only with --generate and --solve.)");
        options.addOption("t", "threads", true, "Maximum number of solver threads. (Use only with --solve.)");
        options.addOption("x", "nbcc", true, "Maximum number of connected components. (Use only with --generate.)");
        options.addOption("G", "gui", false, "Run with the graphic user interface.");
        options.addOption("h", "help", false, "Display this help");
        try {
            cmd = parser.parse( options, args);         
        } catch (ParseException e) {
            System.err.println("Error: invalid command line format.");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "phineloops", options );
            System.exit(1);
        }       
                
        try{    
    	if( cmd.hasOption( "G" ) ) {
    		withGI=true;
    	}
    	if( cmd.hasOption( "t" ) ) {
    		threads = Integer.valueOf(cmd.getOptionValue("t"));
    	}
    	if( cmd.hasOption( "x" ) ) {
    		maxcc = Integer.valueOf(cmd.getOptionValue("x"));
    		maxcc=maxcc+1;
    		maxcc--;
    	}
    	
    	
	    if( cmd.hasOption( "g" ) ) {
			System.out.println("Running phineloops generator.");
			String[] gridformat = cmd.getOptionValue( "g" ).split("x");
			width = Integer.parseInt(gridformat[0]);
			height = Integer.parseInt(gridformat[1]); 
			if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");
			outputFile = cmd.getOptionValue( "o" );
	
			generate(width, height, outputFile); 
	    }
	    else if( cmd.hasOption( "s" ) ) {
		System.out.println("Running phineloops solver.");
		inputFile = cmd.getOptionValue( "s" );
		if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");      
		outputFile = cmd.getOptionValue( "o" );

		boolean solved = solve(inputFile, outputFile); 

		System.out.println("SOLVED: " + solved);            
	    }
        
	    else if( cmd.hasOption( "c" )) {
		System.out.println("Running phineloops checker.");
		inputFile = cmd.getOptionValue( "c" );
            		
		boolean solved = check(inputFile); 

		System.out.println("SOLVED: " + solved);           
	    }
	    else {
		throw new ParseException("You must specify at least one of the following options: -generate -check -solve ");           
	    }
		} 
        catch (ParseException e) {
            System.err.println("Error parsing commandline : " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "phineloops", options );         
            System.exit(1); // exit with error      
		}

        System.exit(0); // exit with success                            
    }

	public static void testIt() {
		final long timeOutLimit = 120000;
  		List<File> fileList = new ArrayList<>();
  		List<Boolean> AnswersList = new ArrayList<>();
  		fileList.add(new File("./instances/public/grid_8x8_dist.0_vflip.false_hflip.false_messedup.false_id.3.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/public/grid_256x256_dist.6_vflip.true_hflip.false_messedup.true_id.0.dat"));
  		AnswersList.add(false);
  		fileList.add(new File("./instances/private/3x3solved.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/4x8_1.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/public/grid_16x16_dist.0_vflip.false_hflip.true_messedup.false_id.1.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/public/grid_16x16_dist.6_vflip.false_hflip.true_messedup.true_id.0.dat"));
  		AnswersList.add(false);
  		fileList.add(new File("./instances/public/grid_32x32_dist.0_vflip.false_hflip.false_messedup.false_id.0.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/public/grid_256x256_dist.0_vflip.false_hflip.false_messedup.false_id.0.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/2x2multiplesolution.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/3x4T"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/4x4L"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/4x4-impossible"));
  		AnswersList.add(false);
  		fileList.add(new File("./instances/public/grid_256x256_dist.6_vflip.false_hflip.false_messedup.false_id.1.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/2x5-2zones.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/4x3-test"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/Test/Test-10x15-0.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/Test/Test-10x15-1.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/Test/Test-10x15-2.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/Test/Test-10x15-3.dat"));
  		AnswersList.add(true);
  		fileList.add(new File("./instances/private/7x6.dat"));
  		AnswersList.add(true);
  		int[] tailles = new int[]{3,5,7,10,20,30,50,80,150,300,500};
    	for(int i=0; i<tailles.length;i++) {
    		fileList.add(new File("./instances/private/"+tailles[i]+"x"+tailles[i]+".dat"));
    		AnswersList.add(true);
    	}
  		Game g;
  		File CSVFile = new File("./TestResults.csv");
    	if(! CSVFile.exists()) {
    		try {
    			CSVFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}else {
    		CSVFile.delete();
    	}
    	
    	PrintWriter wr;
		try {
			wr = new   PrintWriter(CSVFile);
			wr.write("File_name;Length;Width;Pieces;AStar;MasterSlave;ChocoSolver;TreeSolver;ZoneSolver\n");
			fileList.sort(new Comparator<File>() {
				@Override
				public int compare(File f1, File f2){
					Game g1 = new Game(f1);
					Game g2 = new Game(f2);
					Integer n1 = g1.getLength()*g2.getWidth();
					Integer n2 = g2.getLength()*g2.getWidth();
					return n1.compareTo(n2);
				}
			});
			final ArrayList<Boolean> stopper = new ArrayList<>();
			stopper.add(false);stopper.add(false);stopper.add(false);stopper.add(false);stopper.add(false);
			for(int nbFile=0 ; nbFile<fileList.size();nbFile++) {
				File f = fileList.get(nbFile);
				g = new Game(f);
				wr.write(f.getName()+";");
				wr.write(g.getLength()+";");
				wr.write(g.getWidth()+";");
				wr.write((int)(g.getWidth()*g.getLength())+";");
				
				final Solver[] solvers = new Solver[] {/*new AStarSolver(g),new MasterSlaveSolver(g,10),new ChocoSolver(g),new TreeSolver(g),*/new ZoneSolver(g)/**/};
				for(int i=0;i<solvers.length;i++) {
					if(stopper.get(i)) {
						wr.write("Time Out;");
						continue;
					}
					g = new Game(f);
					final int i2=i;
					try {
						final Thread solveThread = new Thread(new Runnable() {
							@Override
							public void run() {
								solvers[i2].solve();
							}
						});
						final Thread TimmerThread = new Thread(new Runnable() {
							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								try {
									Thread.sleep(timeOutLimit, 0);
								} catch (InterruptedException e) {
								}
								if(solveThread.isAlive()) {
									solveThread.stop();
									stopper.remove(i2);
									stopper.add(i2,new Boolean(true));
								}
								
							}
						});
						
						long timein = System.nanoTime();
						solveThread.start();
						TimmerThread.start();
						try {
							solveThread.join();
						} catch (InterruptedException e) {
						}
						long timeout = System.nanoTime();
						wr.write((long)(timeout-timein)+";");
						
					}catch(Exception e) {
						
					}
					
					
				}
				wr.write("\n");
			}
			wr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}

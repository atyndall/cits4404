package robowiki.runner;

import ga.Config;
import ga.GATree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

public class BattleRunner {
  private static final Joiner COMMA_JOINER = Joiner.on(",");

  private Queue<Process> _processQueue;
  private ConcurrentHashMap<Process, String> _commandMapping;
  private ExecutorService _threadPool;
  private ExecutorService _callbackPool;
  private int _threadPoolSize;
  private int _numRounds;
  private int _battleFieldWidth;
  private int _battleFieldHeight;
  private String _jvmArgs;
  private List<Process> _allProcs;
  
  public BattleRunner(Set<String> robocodeEnginePaths, String jvmArgs,
      int numRounds, int battleFieldWidth, int battleFieldHeight) {
	_commandMapping = new ConcurrentHashMap<Process, String>();
	_allProcs = new LinkedList<Process>();
    _numRounds = numRounds;
    _battleFieldWidth = battleFieldWidth;
    _battleFieldHeight = battleFieldHeight;
    _jvmArgs = jvmArgs;

    _threadPoolSize = robocodeEnginePaths.size();
    _threadPool = Executors.newFixedThreadPool(_threadPoolSize);
    _callbackPool = Executors.newFixedThreadPool(1);
    _processQueue = Queues.newConcurrentLinkedQueue();
    for (String enginePath : robocodeEnginePaths) {
      initEngine(enginePath, jvmArgs);
    }
  }

  private void initEngine(String enginePath, String jvmArgs) {
	Process p = initEngine2(enginePath, jvmArgs);
	_processQueue.add(p);
	_commandMapping.put(p, enginePath);
  }
  
  private Process initEngine2(String enginePath, String jvmArgs) {
	    try {
	      List<String> command = Lists.newArrayList();
	      command.add("java");
	      command.addAll(Lists.newArrayList(jvmArgs.trim().split(" +")));
	      command.addAll(Lists.newArrayList("-cp",
	          System.getProperty("java.class.path"),
	          "robowiki.runner.BattleProcess", "-rounds", "" + _numRounds,
	          "-width", "" + _battleFieldWidth, "-height", "" + _battleFieldHeight,
	          "-path", enginePath));
	      
	      //System.out.println(command);

	      //System.out.print("Initializing engine: " + enginePath + "... ");
	      ProcessBuilder builder = new ProcessBuilder(command);
	      builder.redirectErrorStream(true);
	      Process battleProcess = builder.start();
	      BufferedReader reader = new BufferedReader(
	          new InputStreamReader(battleProcess.getInputStream()));
	      String processOutput;
	      do {
	        processOutput = reader.readLine();
	      } while (!processOutput.equals(BattleProcess.READY_SIGNAL));
	      //System.out.println("done!");
	      System.out.print("*");
	      _allProcs.add(battleProcess);
		  return battleProcess;
	    } catch (IOException e) {
	      e.printStackTrace();
	      System.exit(1);
	      return null;
	    }
	  }

  public void runBattles(List<GATree> trees, BotList opponents, BattleResultHandler handler) {
	  Queue<GATree> treesToTest = new LinkedList<GATree>(trees);
	  int numPer = treesToTest.size() / _threadPoolSize;
	  
	  List<GATree[]> queuedTrees = new LinkedList<GATree[]>();
	  for (int j = 1; j <= _threadPoolSize; j++) {
		  List<GATree> nl = new LinkedList<GATree>();
		  for (int i = 0; i < numPer; i++) {
			  if (treesToTest.size() == 0) break;
			  nl.add(treesToTest.remove());
		  }

		  if (j == _threadPoolSize && treesToTest.size() != 0) {
			  for (GATree t : treesToTest) nl.add(t);
			  treesToTest.clear();
		  }

		  if (nl.size() > 0) {
			  queuedTrees.add(nl.toArray(new GATree[nl.size()]));
		  }
	  } 
	  
	  
    List<Future<List<String>>> futures = Lists.newArrayList();
    int num = 1;
    for (final GATree[] t : queuedTrees) {
      futures.add(_threadPool.submit(newBattleCallable(num, new TreeList(Lists.newArrayList(t)), opponents, handler)));
      num++;
    }
    
    getAllFutures(futures);
    System.out.println();
  }


  private void getAllFutures(List<Future<List<String>>> futures) {
    for (Future<List<String>> future : futures) {
      try {
        future.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  private Callable<List<String>> newBattleCallable(
      int num, TreeList trees, BotList opponentList, BattleResultHandler handler) {
    return new BattleCallable(num, trees, opponentList, handler);
  }

  private List<RobotScore> getRobotScoreList(String battleResults) {
    List<RobotScore> robotScores = Lists.newArrayList();
    String[] botScores =
        battleResults.replaceFirst(BattleProcess.RESULT_SIGNAL, "")
            .replaceAll("\n", "").split(BattleProcess.BOT_DELIMITER);
    for (String scoreString : botScores) {
      try {
      String[] scoreFields = scoreString.split(BattleProcess.SCORE_DELIMITER);
      String botName = scoreFields[0];
      int score = Integer.parseInt(scoreFields[1]);
      int firsts = Integer.parseInt(scoreFields[2]);
      int survivalScore = Integer.parseInt(scoreFields[3]);
      double bulletDamage = Double.parseDouble(scoreFields[4]);
      RobotScore robotScore =
          new RobotScore(botName, score, firsts, survivalScore, bulletDamage);
      robotScores.add(robotScore);
      } catch (NumberFormatException e) {
    	  e.printStackTrace();
    	  return null;
      }
    }
    return ImmutableList.copyOf(robotScores);
  }

  private boolean isBattleResult(String line) {
    return line != null && line.startsWith(BattleProcess.RESULT_SIGNAL);
  }

  public void shutdown() {
    _threadPool.shutdown();
    _callbackPool.shutdown();
    for (Process p : _allProcs) {
    	p.destroy();
    }
  }

  public interface BattleResultHandler {
    /**
     * Processes the scores from a battle.
     *
     * @param robotScores scores for each robot in the battle
     * @param elapsedTime elapsed time of the battle, in nanoseconds
     */
    void processResults(GATree t, List<RobotScore> robotScores, long elapsedTime);
  }

  public interface BattleSelector {
    BotList nextBotList();
  }

  private class BattleCallable implements Callable<List<String>> {
    private BotList _opponentList;
    private BattleResultHandler _listener;
    private TreeList _treeList;
    private int _threadNum;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Process battleProcess;
    
    class InputReader implements Callable<String> {
    	BufferedReader s;
    	
  	  	public InputReader(BufferedReader s) {
  	  		this.s = s;
  	  	}

        public String call() throws IOException {
        	return s.readLine();
        }
    }
    
    public BattleCallable(int threadNum, TreeList treeList, BotList opponentList, BattleResultHandler listener) {
      _opponentList = opponentList;
      _treeList = treeList;
      _threadNum = threadNum;
      _listener = listener;
    }
    
    private void toFile(String path, Object obj) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

    private void reinitialize() {
    	//System.out.print(_commandMapping);
    	String path = _commandMapping.remove(battleProcess);
		if (path == null) throw new RuntimeException("Can't get path");
		
//		try {
//			writer.close();
//			reader.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		battleProcess.destroy();
		battleProcess = initEngine2(path, _jvmArgs);
		//System.out.println("New process " + battleProcess + " created");
		System.out.print("!");
		_commandMapping.put(battleProcess, path);
		//System.out.print(_commandMapping);

		writer = new BufferedWriter(
				new OutputStreamWriter(battleProcess.getOutputStream()));
		reader = new BufferedReader(
				new InputStreamReader(battleProcess.getInputStream()));
		
    }

    @Override
    public List<String> call() throws Exception {
      final long startTime = System.nanoTime();
      battleProcess = _processQueue.poll();
      writer = new BufferedWriter(
          new OutputStreamWriter(battleProcess.getOutputStream()));
      reader = new BufferedReader(
          new InputStreamReader(battleProcess.getInputStream()));
      BotList botList;
      
      botList = _opponentList;
      List<GATree> trees = _treeList.getTrees();
      List<String> results = new ArrayList(trees.size());
      
      ExecutorService executor = Executors.newFixedThreadPool(1);
      
      int originalLength = trees.size();
      for (int i = 0; i < originalLength; i++) {
      //for (final GATree t : trees) {
    	  
    	  final GATree t = trees.get(i);
    	  toFile(Config.serializedLoc + _threadNum + ".ser", t);
    	  
    	  List<String> bots = new ArrayList<String>(botList.getBotNames());
    	  bots.add("ga.botthreads.ThreadedGABot" + _threadNum + "*");
    	  
    	  boolean cont = false;
    	  
    	  try {
		      writer.append(COMMA_JOINER.join(new BotList(bots).getBotNames()) + "\n");
		      writer.flush();
    	  } catch (IOException e) {
    		  //System.out.println("Output stream problem detected, restarting");
    		  reinitialize();
    		  i--;
    		  break;
    	  }
    	  
    	  if (cont) continue;
	      
	      String input = "";
	      
	      do {
	        // TODO: How to handle other output, errors etc?
	    	try {
		    	Future<String> f = executor.submit(new InputReader(reader));
		    	
		        input = f.get(2000 + (t.size() * 500), TimeUnit.MILLISECONDS);
		        
		        if (isBattleResult(input)) {
		        	//System.out.println(input);
		        	break;
		        } else {
		        	System.out.println(input);
		        }
	    	} catch (TimeoutException e) {
	    		//System.out.println();
	    		//System.out.println("We haven't heard from a process in a while, terminating " +battleProcess+ "and restarting (will skip this tree).");
	    		//System.out.println(t);
	    		
	    		reinitialize();
	    		
	    		cont = true;
	    		break;
	    	} catch (ExecutionException e) {
	    		//System.out.println();
	    		//System.out.println("Execution exception");
	    		//e.printStackTrace();
	    		reinitialize();
	    		cont = true;
	    		break;
	    	}
	      } while (true);
	      
	      if (cont) continue;
	      
	      final String result = input;
	      _processQueue.add(battleProcess);
	      

	      System.out.print(".");
	      //System.out.println("going to callback pool");
	      _callbackPool.submit(new Runnable() {
	        @Override
	        public void run() {
	          List<RobotScore> scores = getRobotScoreList(result);
	          if (scores != null) {
	        	  _listener.processResults(
	        			  t, scores , System.nanoTime() - startTime);
	          }
	          
	        }
	      }).get();
	      //System.out.println("outputting result");
	      results.add(result);
      }
      System.out.print("$");
      return results;
    }
  }
}

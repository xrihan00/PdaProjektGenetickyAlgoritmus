package tanks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;



import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
//import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class RobocodeRunner {

	double[] fitnesses;
	int popSize;
	DNA[] populace;
	public RobocodeRunner() {
		popSize=10;
		fitnesses=new double[popSize];
		populace=new DNA[popSize];
		for (int i = 0; i < popSize; i++) {
			populace[i]=new DNA("GATANK_"+i);
		}
	}

	public static void main(String[] args) throws IOException {
		// test master pull request 2
		
		String seznamProtivniku = "SuperBoxBot, SuperCorners, SuperCrazy, SuperMercutio, SuperRamFire, SuperSittingDuck, SuperSpinbot, SuperTrackFire, SuperWalls";

		RobocodeRunner runner = new RobocodeRunner();
		for (int i = 0; i < runner.popSize; i++) {
			//runner.populace[i].printGenes();
			runner.runRobocode(runner.populace[i].name, seznamProtivniku, i);
		}
		for (double x : runner.fitnesses) {
			System.out.println(x);
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void runRobocode(String mujRobot, String seznamProtivniku, int index) throws IOException {

		// create src and dest path for compiling
		String src = "src/sample/" + mujRobot + ".java";
		String dst = "robots/sample/" + mujRobot + ".java";
		// compile our created robot and store it to robots/samples
		File source = new File(src);
		File dest = new File(dst);
		Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		compiler.run(null, System.out, System.out, dst);
		// remove all whitespaces
		seznamProtivniku = seznamProtivniku.replaceAll("\\s", "");
		// create list of tanks to fight

		String tanks[] = seznamProtivniku.split(",");
		String finalListOfTanks = "";
		for (String string : tanks) {
			string = "sample." + string + ",";
			finalListOfTanks += string;
		}

		finalListOfTanks += "sample." + mujRobot;

		// ("sample.Corners, sample.MujRobot"

		// Battle listener used for receiving battle events
		BattleObserver battleListener = new BattleObserver();

		// Create the RobocodeEngine
		RobocodeEngine engine = new RobocodeEngine(); // Run from current
														// working directory

		// Add battle listener to our RobocodeEngine
		engine.addBattleListener(battleListener);

		// Show the battles
		engine.setVisible(false);

		// Setup the battle specification

		int numberOfRounds = 500;
		BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
		// RobotSpecification[] selectedRobots =
		// engine.getLocalRepository("sample.Corners, sample.MujRobot");
		RobotSpecification[] selectedRobots = engine.getLocalRepository(finalListOfTanks);

		BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
		// Run our specified battle and let it run till it's over
		engine.setLogMessagesEnabled(false);
		engine.setLogErrorsEnabled(false);
		engine.runBattle(battleSpec, true/* wait till the battle is over */);
		

		for (BattleResults result : battleListener.getResults()) {
			//System.out.println(result.getTeamLeaderName() + " - " + result.getScore());
			if (result.getTeamLeaderName().contains(mujRobot)) {
				this.fitnesses[index] = result.getScore();
				//System.out.println("Huraa");
			}
		}

		// Cleanup our RobocodeEngine
		engine.close();

		// Make sure that the Java VM is shut down properly --- to asi nechceme kdyz je to jen jeden z generace
		// System.exit(0);
	}
}
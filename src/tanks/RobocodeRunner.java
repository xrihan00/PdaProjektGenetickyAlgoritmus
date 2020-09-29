package tanks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.Scanner;
import genetics.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import genetics.Population;
import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
//import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class RobocodeRunner {

	public static void main(String[] args) throws IOException {
		// test master pull request 2

		String nazevTridyMehoRobota = "GeneticTank";
		//String seznamProtivniku = "Crazy, Corners, Fire";
		String lepsiProtivnici="SuperBoxBot, SuperCorners, SuperCrazy, SuperMercutio, SuperRamFire, SuperSittingDuck, SuperSpinbot, SuperTrackFire, SuperWalls";
		runRobocode(nazevTridyMehoRobota, lepsiProtivnici);

		/*
		 * Population pop=new Population(); Random rand=new
		 * Random(System.currentTimeMillis()); float[]arr=new float[10]; for (int i = 0;
		 * i < arr.length; i++) {
		 * 
		 * arr[i]=rand.nextFloat();
		 * 
		 * 
		 * } float[] genes=new
		 * float[]{(float)1.0,(float)2.0,(float)3.0,(float)4.0,(float)5.0,(float)6.5,(
		 * float)-9.0,(float)4.5,(float)5465456.0}; pop.setGenome("GeneticTank",
		 * Population.genomeFloatArrToString(arr)); pop.getGenome("GeneticTank");
		 * 
		 * for (float f : genes) { System.out.println(f); }
		 */ }

	public static void runRobocode(String mujRobot, String seznamProtivniku) throws IOException {

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

		int numberOfRounds = 100;
		BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
		// RobotSpecification[] selectedRobots =
		// engine.getLocalRepository("sample.Corners, sample.MujRobot");
		RobotSpecification[] selectedRobots = engine.getLocalRepository(finalListOfTanks);

		BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
		// Run our specified battle and let it run till it's over
		engine.runBattle(battleSpec, true/* wait till the battle is over */);

		for (BattleResults result : battleListener.getResults()) {
			System.out.println(result.getTeamLeaderName() + " - " + result.getScore());
		}

		// Cleanup our RobocodeEngine
		engine.close();

		// Make sure that the Java VM is shut down properly
		System.exit(0);
	}
}
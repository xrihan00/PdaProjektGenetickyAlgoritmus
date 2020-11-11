package tanks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;

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
	String seznamProtivniku;
	int numRounds;

	public RobocodeRunner() {
		popSize = 10;
		numRounds=20;
		fitnesses = new double[popSize];
		populace = new DNA[popSize];
		seznamProtivniku = "SuperBoxBot, SuperCorners, SuperCrazy, SuperMercutio, SuperRamFire, SuperSittingDuck, SuperSpinbot, SuperTrackFire, SuperWalls";
		for (int i = 0; i < popSize; i++) {
			populace[i] = new DNA("GATANK_" + i, new float[] { (float) 12, (float) 150, (float) 140, (float) 140,
					(float) 140, (float) 0.9, (float) 3, (float) 3, (float) 15, (float) 12 });
		}
	}

	public static void main(String[] args) throws IOException {
		// test master pull request 2

		RobocodeRunner runner = new RobocodeRunner();
		int t = 0;// prednaska druhy tyden slide 13
		// JEDNA GENERACE
		runner.mutate();
		runner.evaluate();

		// KONEC GENERACE
		boolean done = false;
		while (!done) {
			t += 1;
			runner.selectParents();

			runner.mutate();
			runner.evaluate();
			for(DNA x:runner.populace) {
				x.printGenes();
			}
			DNA.mutationRate*=0.95;
			DNA.mutationSeverity*=0.95;
			if(t==10)done=true;

		}

	}

	public void evaluate() {
		for (int i = 0; i < this.popSize; i++) {
			// runner.populace[i].printGenes();
			try {
				this.runRobocode(this.populace[i].name, this.seznamProtivniku, i,this.numRounds);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		for (int i = 0; i < this.popSize; i++) {
			this.populace[i].fitness = Double.valueOf(this.fitnesses[i]);// prirazeni fitness funkce do objektu DNA

		}
	}

	public void selectParents() {
		Arrays.sort(populace);
		int celkem = 0;
		for (DNA x : populace) {
			celkem += x.fitness;
		}
		double[] pocet = new double[this.popSize];
		double mezisoucet = 0;
		for (int i = 0; i < this.popSize; i++) {

			double prirustek = populace[i].fitness / celkem;
			mezisoucet += prirustek;
			pocet[i] = mezisoucet;

		}
		DNA[] novaPopulace = new DNA[this.popSize];
		Random rand = new Random();
		for (int i = 0; i < this.popSize; i++) {
			double nahoda = rand.nextDouble();
			DNA rodic1 = null;
			DNA rodic2 = null;
			// NAHODNA SELEKCE RODICU
			for (int j = 0; j < this.popSize; j++) {
				if (nahoda < pocet[j]) {
					rodic1 = populace[j];
					break;
				}
			}
			nahoda = rand.nextDouble();
			for (int j = 0; j < this.popSize; j++) {
				if (nahoda < pocet[j]) {
					rodic2 = populace[j];
					break;
				}
			}
			// RODICE VYBRANI
			populace[i] = DNA.crossover(rodic1, rodic2, "GATANK_" + i);
		}

	}

	public void mutate() {
		for (int i = 0; i < this.popSize; i++) {
			populace[i] = DNA.mutate(populace[i], "GATANK_" + i);
		}
	}

	public void runRobocode(String mujRobot, String seznamProtivniku, int index,int numberOfRounds) throws IOException {

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
			// System.out.println(result.getTeamLeaderName() + " - " + result.getScore());
			if (result.getTeamLeaderName().contains(mujRobot)) {
				this.fitnesses[index] = result.getScore();
				// System.out.println("Huraa");
			}
		}

		// Cleanup our RobocodeEngine
		engine.close();

		// Make sure that the Java VM is shut down properly --- to asi nechceme kdyz je
		// to jen jeden z generace
		// System.exit(0);
	}
}
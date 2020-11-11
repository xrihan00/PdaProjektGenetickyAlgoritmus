package tanks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.Scanner;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import robocode.AdvancedRobot;
import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import tanks.BattleObserver;

public class DNA implements Comparable<DNA> {

	String name;
	static float mutationRate = (float) 0.4;
	Random rand;
	float[] genes;
	Double fitness;
	static float mutationSeverity=(float) 0.5;
	

	public DNA(String name) {
		rand = new Random(System.currentTimeMillis());
		this.name = name;
		
		genes=new float[10];
		for (int i = 0; i < genes.length; i++) {
			genes[i]=rand.nextFloat();
			
		}
		this.makeTank();
	}

	public DNA(String name, float[] genes) {
		
		this.genes = genes;
		this.name = name;
		this.makeTank();

	}

	public static DNA crossover(DNA first, DNA second, String name) {
		float[] newgenes = new float[10];
		int midpoint = 5;
		for (int i = 0; i < newgenes.length; i++) {
			if (i < midpoint)
				newgenes[i] = first.genes[i];
			else
				newgenes[i] = second.genes[i];

		}
		return new DNA(name, newgenes);
	}

	public static DNA mutate(DNA first,String name) {
		Random rand = new Random(System.currentTimeMillis());
		float[] newgenes = new float[10];
		for (int i = 0; i < 10; i++) {
			float f = first.genes[i];
			if (rand.nextFloat() < mutationRate)
				newgenes[i] = ((rand.nextFloat()*2)-1)*mutationSeverity+first.genes[i];
			else
				newgenes[i] = first.genes[i];
		}
		return new DNA(name, newgenes);
	}

	public void printGenes() {
		for (float f : genes) {
			System.out.println(f);
		}
	}

	public void makeTank() {
		try {
			File myObj = new File("src/sample/GeneticTankBlueprint.java");
			
			StringBuffer inputBuffer = new StringBuffer();
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.contains("GeneticTankBlueprint"))
					inputBuffer.append("public class " + this.name + " extends AdvancedRobot {");
				else if (data.contains("float[] genes = new float[]")) {
					System.out.println("NASEL JSEM TEN RADEK");
					inputBuffer.append("	float[] genes=new float[]{" + this.genomeFloatArrToString(genes) + "};");

				} else
					inputBuffer.append(data);
				inputBuffer.append('\n');
			}
			myReader.close();
			FileOutputStream fileOut = new FileOutputStream("src/sample/" + this.name + ".java");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String genomeFloatArrToString(float[] arr) {
		StringBuilder res = new StringBuilder();
		for (float f : arr) {
			res.append("(float)" + f + ",");
		}
		res.deleteCharAt(res.lastIndexOf(","));
		return res.toString();
	}

	@Override
	public int compareTo(DNA o) {
		return Double.compare(o.fitness,this.fitness);
	}

}
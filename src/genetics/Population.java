package genetics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Population {

	public String getGenome(String tankName) {
		try {
			File myObj = new File("geneticTanks/" + tankName + ".java");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				System.out.println(data);
				if (data.contains("genes")) {
					return data;

				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "neni";
	}

	public void setGenome(String tankName, String genome) {
		try {
			File myObj = new File("geneticTanks/" + tankName + ".java");
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
			StringBuffer inputBuffer = new StringBuffer();
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				if (data.contains("genes")) {
					inputBuffer.append("	float[] genes=new float[]{" + genome + "};");

				} else
					inputBuffer.append(data);
				inputBuffer.append('\n');
			}
			myReader.close();
			FileOutputStream fileOut = new FileOutputStream("geneticTanks/" + tankName + ".java");
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

	public static String genomeFloatArrToString(float[] arr) {
		StringBuilder res = new StringBuilder();
		for (float f : arr) {
			res.append("(float)" + f + ",");
		}
		res.deleteCharAt(res.lastIndexOf(","));
		return res.toString();
	}

	public static float[] genomeStringtoFloatArr(String str) {
		//TODO
		return new float[1];
	}
}

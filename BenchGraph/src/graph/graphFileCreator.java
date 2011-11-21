package graph;

import generators.ScrambledZipfianGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class graphFileCreator {

	private static BufferedWriter out;

	public static void main(String[] args) throws IOException {

		int ub = Integer.parseInt(args[0]);
		int rel = Integer.parseInt(args[1]);

		FileWriter fstream = new FileWriter("followers.txt");
		out = new BufferedWriter(fstream);
		
		//int lb = 1;
		// UniformIntegerGenerator g = new UniformIntegerGenerator(lb,ub);

		// double newzetan =
		// ZipfianGenerator.zetastatic(ITEM_COUNT,ZipfianGenerator.ZIPFIAN_CONSTANT);
		ScrambledZipfianGenerator gen = new ScrambledZipfianGenerator(ub);

		Hashtable<String, Integer> currentRelations = new Hashtable<String, Integer>();

		int a, b;
		for (int i = 0; i < rel; ++i) { // how many rels
			// a = g.nextInt();
			// b = g.nextInt();

			a = gen.nextInt();
			b = gen.nextInt();

			while (b == a) {
				// b = g.nextInt();
				b = gen.nextInt();

			}
			if (currentRelations.get(a + "" + b) == null) {
				writeToFile(a, b);
				currentRelations.put(a + "" + b, 0);
			} else
				i = i - 1;

		}
		out.close();

	}

	static void writeToFile(int a, int b) {
		try {

			out.write(a + "\t" + b + "\n");

		} catch (Exception e) {

			System.err.println("Error while creating graph file: "
					+ e.getMessage());
		}
	}

}

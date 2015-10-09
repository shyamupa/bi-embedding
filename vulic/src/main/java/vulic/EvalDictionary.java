package vulic;

import java.io.FileNotFoundException;
import java.util.*;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.HashedMap;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.experiments.ClassificationTester;
import edu.illinois.cs.cogcomp.core.io.LineIO;

public class EvalDictionary {

	static String dictFile = "/shared/bronte/upadhya3/comparable_data/ground_truth/ESEN_Eval.dic";

	public static void evaluate(String goldFile, MultiMap mydict)
			throws FileNotFoundException {
		List<String> lines = LineIO.read(goldFile);
		Map<String, String> dict = new HashMap<>();
		for (String line : lines) {
			String[] parts = line.split("\\s+");
			dict.put(parts[0], parts[1]);
		}
		// mydict
		ClassificationTester tester = new ClassificationTester();
		int coverage = 0;
		for (String k : dict.keySet()) {
			if (mydict.containsKey(k)) {
				coverage++;
			}

		}
		// tester.record(goldLabel, predictedLabel);
	}

}

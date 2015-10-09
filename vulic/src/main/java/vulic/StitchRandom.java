package vulic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.illinois.cs.cogcomp.core.io.LineIO;

public class StitchRandom {
	List<String> eng_vocab;
	List<String> fr_vocab;

	public StitchRandom(String engVocabFile,String frVocabFile) throws Exception{
		eng_vocab=LineIO.read(engVocabFile);
		fr_vocab=LineIO.read(frVocabFile);
	}

	public StitchRandom(List<String> eng_vocab2, List<String> fr_vocab2) {
		eng_vocab=eng_vocab2;
		fr_vocab=fr_vocab2;
	}

	List<String> randomize(String eng, String fr) {
		String[] engTokens = eng.split("\\s+");
		String[] frTokens = fr.split("\\s+");
		List<String> engToks = FilterTokens(eng_vocab, engTokens);
		List<String> frToks = FilterTokens(fr_vocab, frTokens);
		List<String> allTokens = new ArrayList<>();
		allTokens.addAll(engToks);
		allTokens.addAll(frToks);
		Collections.shuffle(allTokens, new Random());
		return allTokens;
	}


	private List<String> FilterTokens(List<String> vocab, String[] tokens) {
		List<String> ans = new ArrayList<String>();
		for (String w : tokens) {
			if (vocab.contains(w)) {
				ans.add(w);
			}
		}
		return ans;
	}

	public void stitch(String path, int num, String outputFile)
			throws FileNotFoundException {
		File folder = new File(path);
		PrintWriter w = new PrintWriter(new File(outputFile));

		FilenameFilter engFiles = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.split("\\.")[1].equals("eng"))
					return true;
				return false;
			}

			// @Override
			// public boolean accept(File pathname) {
			// if (pathname.getName().split("\\.")[1].equals("eng"))
			// return true;
			// return false;
			// }
		};
		String[] listOfFiles = folder.list(engFiles);

		int c = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			String id = listOfFiles[i].split("\\.")[0];
			System.out.println(id + ".eng");
			System.out.println(id + ".fr");
			String eng = LineIO.slurp(path + "/" + id + ".eng");
			String fr = LineIO.slurp(path + "/" + id + ".fr");
			List<String> toWrite = randomize(eng, fr);
			for (String word : toWrite) {
				w.print(word + " ");
			}
			w.println();

			if (i == num) {
				break;
			}
		}

		w.close();
	}
	
}

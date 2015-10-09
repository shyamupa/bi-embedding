package vulic;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import vulic.StitchLengthRatio;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.io.IOUtils;
import edu.illinois.cs.cogcomp.core.io.LineIO;
import edu.illinois.cs.cogcomp.core.utilities.commands.CommandDescription;
import edu.illinois.cs.cogcomp.core.utilities.commands.InteractiveShell;
import edu.illinois.cs.cogcomp.wikifier.utils.datastructure.StringCounter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainClass {

	static BufferedReader getBufferedReaderForCompressedFile(String fileIn)
			throws FileNotFoundException, CompressorException {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory()
				.createCompressorInputStream(bis);
		return new BufferedReader(new InputStreamReader(input));
	}

	static int count = 0;

	static void processBuf(int id, String str) throws FileNotFoundException {

		// System.out.println("ID " + id);
		str = str.replaceAll("</article>", "\n======================\n");
		str = StringEscapeUtils.unescapeXml(str);
		str = str.replaceAll("(?s)<table>.*?</table>", " ");
		str = str.replaceAll("(?s)<h>.*?</h>", " ");
		str = str.replaceAll("<p>", "\n");
		str = str.replaceAll("</p>", "\n");
		str = str.replaceAll("<[^>]+>", " ");
		String[] parts = str.split("======================");
		String eng = parts[0];
		String fr = parts[1];
		if (IOUtils.exists(Params.writedir + "/" + id + ".eng")) {
			System.out.println("already there " + id);
			return;
		}
		PrintWriter w;
		if (count % 1000 == 0)
			System.out.println("Writing to " + Params.writedir + "/" + id
					+ ".eng");
		w = new PrintWriter(Params.writedir + "/" + id + ".eng");
		// System.out.println("-------ENG------");
		w.println(eng);
		w.close();

		w = new PrintWriter(Params.writedir + "/" + id + ".fr");
		// System.out.println("-------FR------");
		w.println(fr);
		w.close();
		//
		// System.out.println(str);
		count++;
	}

	@CommandDescription(description = "wikifilepath,configFile")
	public static void ParseComparableWiki(String wikifilepath,
			String configFile) throws CompressorException,
			NumberFormatException, IOException, ConfigurationException {
		// Params.initialize();
		Params.loadConfig(configFile);
		System.out.println(Params.writedir);
		// System.exit(-1);
		BufferedReader br = getBufferedReaderForCompressedFile(wikifilepath);
		// BufferedReader br =
		// getBufferedReaderForCompressedFile("wikicomp-2014_entr.xml.bz2");
		String line;
		String buf = "";
		// Pattern pattern = Pattern.compile("\"(.*?)\"");
		Boolean add = false;
		int cnt = 0;
		int id = -1;
		while ((line = br.readLine()) != null) {
			if (line.contains("<articlePair")) {
				buf = line + "\n";
				// System.out.println(line);
				add = true;
				id = Integer.parseInt(StringUtils.substringBetween(line, "\"",
						"\""));
				// Matcher m = pattern.matcher(line);
				// System.out.println(line+" "+m.group(1));
			}
			if (line.contains("</articlePair>")) {
				buf += line + "\n";
				add = false;
				cnt++;
				if (cnt % 1000 == 0) {
					System.out.println("HI " + cnt);
				}
				if (cnt == 1000000) {
					System.exit(-1);
				}
				// process
				processBuf(id, buf);
				// System.out.println(buf);
				buf = "";

			}
			if (add) {
				buf += line;
			}
		}
		System.out.println(cnt);
		System.exit(-1);
	}

	@CommandDescription(description = "String binfile, String engvoc, String frvoc, String goldFile")
	public static void getNoisyDict(String binfile, String engvoc,
			String frvoc, String goldFile) throws FileNotFoundException,
			IOException {
		CreateBLDict dd = new CreateBLDict(engvoc, frvoc, binfile);
		float[][] dist_matrix = dd.computeDistMatrix();
		MultiMap mydict = dd.getBestPairs(dist_matrix);
		EvalDictionary.evaluate(goldFile, mydict);
	}

	public static void main(String args[]) throws Exception {

		InteractiveShell<MainClass> tester = new InteractiveShell<MainClass>(
				MainClass.class);
		if (args.length == 0)
			tester.showDocumentation();
		else {
			tester.runCommand(args);
		}
	}

	@CommandDescription(description = "String dirpath, String numFiles, String outFile,String engdict,String frdict,String enStopWords, String frStopWords")
	public static void createTrainingData(String dirpath, String numFiles,
			String outFile, String engdict, String frdict,
			String enStopWordsFile, String frStopWordsFile) throws Exception {
		List<String> eng_vocab = ComputeVocab(dirpath, 10000, "eng",
				enStopWordsFile);
		List<String> fr_vocab = ComputeVocab(dirpath, 10000, "fr",
				frStopWordsFile);
		LineIO.write(engdict, eng_vocab);
		LineIO.write(frdict, fr_vocab);

		// StitchLengthRatio strategy = new StitchLengthRatio(eng_vocab,
		// fr_vocab);
		// strategy.stitch(dirpath, Integer.parseInt(numFiles), outFile);

	}

	private static List<String> ComputeVocab(String dir, int k,
			final String mode, String stopWordsFile) throws Exception {
		System.out.println("Computing vocab in mode: " + mode);
		StringCounter sc = new StringCounter();
		List<String> stopWords = LineIO.read(stopWordsFile);
		File folder = new File(dir);
		FilenameFilter engFiles = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.split("\\.")[1].equals(mode))
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
		for (String f : listOfFiles) {
			String doctext = LineIO.slurp(dir + "/" + f);
			// this cannot be a negative filter
			// doctext = doctext.replaceAll("[^a-zA-Z ]", "").toLowerCase();
			doctext = doctext.replaceAll("\\p{P}", "");
			for (String token : doctext.split("\\s+")) {
				if (!stopWords.contains(token.toLowerCase()))
					sc.count(token);
			}
			c++;
			if (c % 10000 == 0) {
				System.out.println("read files:" + c);
				// break;
			}
		}

		List<Pair<String, Double>> top = sc.getTopK(k);
		List<String> ans = new ArrayList<String>();
		for (Pair<String, Double> tt : top) {
			ans.add(tt.getFirst());
		}
		return ans;
	}
}

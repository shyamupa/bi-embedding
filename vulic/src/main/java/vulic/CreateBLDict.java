package vulic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.io.LineIO;

public class CreateBLDict {

	// why do you get OOV??
	private static final float DELTA = 0.6f;
	private List<String> fr_vocab;
	private List<String> eng_vocab;
	private Vectors vectors;

	public CreateBLDict(String eng_vocab, String fr_vocab, String binfile)
			throws FileNotFoundException, IOException {
		this.eng_vocab = LineIO.read(eng_vocab);
		this.fr_vocab = LineIO.read(fr_vocab);
		this.vectors = new Vectors(new FileInputStream(new File(binfile)));
		System.out.println("done!");
	}

	public CreateBLDict(List<String> eng_vocab, List<String> fr_vocab,
			String binfile) throws FileNotFoundException, IOException {
		this.eng_vocab = eng_vocab;
		this.fr_vocab = fr_vocab;
		this.vectors = new Vectors(new FileInputStream(new File(binfile)));
		System.out.println("done!");
	}

	public float[][] computeDistMatrix() {
		float[][] dist_matrix = new float[eng_vocab.size()][fr_vocab.size()];
		
		System.out.println("dist matrix start!");
		for (int i = 0; i < eng_vocab.size(); i++) {
			for (int j = 0; j < fr_vocab.size(); j++) {
				dist_matrix[i][j] = ComputeDistance(eng_vocab.get(i),
						fr_vocab.get(j));
			}
		}
		System.out.println("dist matrix done!");
		return dist_matrix;
	}

	public MultiMap getBestPairs(float[][] dist_matrix) {
//		List<Pair<String, String>> ans = new ArrayList<>();
		MultiMap ans = new MultiHashMap();
		for (int i = 0; i < dist_matrix.length; i++) {
			for (int j = 0; j < dist_matrix[0].length; j++) {
				//
				if (dist_matrix[i][j] > DELTA) {
					if (!fr_vocab.contains(eng_vocab.get(i))
							&& !eng_vocab.contains(fr_vocab.get(j)))
						System.out.println(eng_vocab.get(i) + " "
								+ fr_vocab.get(j) + " " + dist_matrix[i][j]);
//					ans.add(new Pair<String,String>(fr_vocab.get(j), eng_vocab.get(i)));
					ans.put(fr_vocab.get(j), eng_vocab.get(i));
				}
			}
		}
		return ans;
	}

	private float ComputeDistance(String word1, String word2) {
		try {
			float[] w1 = vectors.getVector(word1);
			float[] w2 = vectors.getVector(word2);
			//
			float len = 0.0f;
			float dist = 0.0f;
			for (int i = 0; i < w1.length; i++) {
				len += w1[i] * w1[i];
			}
			len = (float) Math.sqrt(len);
			for (int i = 0; i < w1.length; i++) {
				w1[i] /= len;
			}
			//
			len = 0;
			for (int i = 0; i < w2.length; i++) {
				len += w2[i] * w2[i];
			}
			len = (float) Math.sqrt(len);
			for (int i = 0; i < w2.length; i++) {
				w2[i] /= len;
			}
			for (int i = 0; i < w2.length; i++) {
				dist += w1[i] * w2[i];
			}
			return dist;
		} catch (OutOfVocabularyException e) {
			// e.printStackTrace();
		}

		return 0;
	}
}
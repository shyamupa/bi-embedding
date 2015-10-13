package rcv2;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.ca.CatalanAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.ckb.SoraniAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.da.DanishAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.eu.BasqueAnalyzer;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.ga.IrishAnalyzer;
import org.apache.lucene.analysis.gl.GalicianAnalyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.analysis.hy.ArmenianAnalyzer;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.lt.LithuanianAnalyzer;
import org.apache.lucene.analysis.lv.LatvianAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.sv.SwedishAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.analysis.tr.TurkishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

public class AnalyzerFactory {

	public static HashMap<String, Integer> analyzerTypes = new HashMap<String, Integer>();

	public static String defaultAnalyzerName = "standard";

	public static void initializeTypes () {
		analyzerTypes.put("standard",0);
		analyzerTypes.put("ar",1);
		analyzerTypes.put("bg",2);
		analyzerTypes.put("br",3);
		analyzerTypes.put("ca",4);
		analyzerTypes.put("zh",5); //cjk
		analyzerTypes.put("ja",6); //cjk
		analyzerTypes.put("ko",7); //cjk
		analyzerTypes.put("ckb",8); //ckb Sorani Kurdish 
		analyzerTypes.put("ku",9); //ckb Sorani Kurdish
		analyzerTypes.put("cz",10);

		analyzerTypes.put("da",11);
		analyzerTypes.put("de",12);
		analyzerTypes.put("el",13);
		analyzerTypes.put("en",14);
		analyzerTypes.put("es",15);
		analyzerTypes.put("eu",16);
		analyzerTypes.put("fa",17);
		analyzerTypes.put("fi",18);
		analyzerTypes.put("fr",19);
		analyzerTypes.put("ga",20);

		analyzerTypes.put("gl",21);
		analyzerTypes.put("hi",22);
		analyzerTypes.put("hu",23);
		analyzerTypes.put("hy",24);
		analyzerTypes.put("id",25);
		analyzerTypes.put("it",26);
		analyzerTypes.put("lt",27);
		analyzerTypes.put("lv",28);
		analyzerTypes.put("nl",29);
		analyzerTypes.put("no",30);

		analyzerTypes.put("pt",31);
		analyzerTypes.put("ro",32);
		analyzerTypes.put("ru",33);
		analyzerTypes.put("sv",34);
		analyzerTypes.put("th",35);
		analyzerTypes.put("tr",36);

		analyzerTypes.put("pl",37);

	}

	private static BufferedReader getBufferedReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader
				: new BufferedReader(reader);
	}

	public static Analyzer initialize(String analyzerType) {
		String stopWordsFileName = "data/stopwords/stopwords_" + analyzerType + ".txt";
		File stopWordsFile = new File (stopWordsFileName);
		if (stopWordsFile.exists() == false) {
			stopWordsFileName = "data/stopwords/stopwords_null.txt";
			stopWordsFile = new File (stopWordsFileName);
		}

		Path path = stopWordsFile.toPath();
		Reader reader = null;
		CharArraySet charStopSet = new CharArraySet(100, false);
		try {
			reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
			BufferedReader br = getBufferedReader(reader);
			String word = null;
			while ((word = br.readLine()) != null) {
				charStopSet.add(word.trim());
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initializeTypes();

		Analyzer analyzer = null;

		int type = -1;
		if (analyzerTypes.containsKey(analyzerType)) {
			type = analyzerTypes.get(analyzerType);
		}

		switch (type) {
			case 0:
				analyzer = new StandardAnalyzer();
				break;
			case 1:
				analyzer = new ArabicAnalyzer();
				break;
			case 2:
				analyzer = new BulgarianAnalyzer();
				break;
			case 3:
				analyzer = new BrazilianAnalyzer();
				break;
			case 4:
				analyzer = new CatalanAnalyzer();
				break;
			case 5:
				analyzer = new SmartChineseAnalyzer();
				break;
			case 6:
				analyzer = new JapaneseAnalyzer();
				break;
			case 7:
				analyzer = new CJKAnalyzer();
				break;
			case 8:
				analyzer = new SoraniAnalyzer();
				break;
			case 9:
				analyzer = new SoraniAnalyzer();
				break;
			case 10:
				analyzer = new CzechAnalyzer();
				break;

			case 11:
				analyzer = new DanishAnalyzer();
				break;
			case 12:
				analyzer = new GermanAnalyzer();
				break;
			case 13:
				analyzer = new GreekAnalyzer();
				break;
			case 14:
				analyzer = new EnglishAnalyzer();
				break;
			case 15:
				analyzer = new SpanishAnalyzer();
				break;
			case 16:
				analyzer = new BasqueAnalyzer();
				break;
			case 17:
				analyzer = new PersianAnalyzer();
				break;
			case 18:
				analyzer = new FinnishAnalyzer();
				break;
			case 19:
				analyzer = new FrenchAnalyzer();
				break;
			case 20:
				analyzer = new IrishAnalyzer();
				break;

			case 21:
				analyzer = new GalicianAnalyzer();
				break;
			case 22:
				analyzer = new HindiAnalyzer();
				break;
			case 23:
				analyzer = new HungarianAnalyzer();
				break;
			case 24:
				analyzer = new ArmenianAnalyzer();
				break;
			case 25:
				analyzer = new IndonesianAnalyzer();
				break;
			case 26:
				analyzer = new ItalianAnalyzer();
				break;
			case 27:
				analyzer = new LithuanianAnalyzer();
				break;
			case 28:
				analyzer = new LatvianAnalyzer();
				break;
			case 29:
				analyzer = new DutchAnalyzer();
				break;
			case 30:
				analyzer = new NorwegianAnalyzer();
				break;

			case 31:
				analyzer = new PortugueseAnalyzer();
				break;
			case 32:
				analyzer = new RomanianAnalyzer();
				break;
			case 33:
				analyzer = new RussianAnalyzer();
				break;
			case 34:
				analyzer = new SwedishAnalyzer();
				break;
			case 35:
				analyzer = new ThaiAnalyzer();
				break;
			case 36:
				analyzer = new TurkishAnalyzer();
				break;

			case 37:
				analyzer = new PolishAnalyzer(); // MorfologikAnalyzer
				break;

			default:
				analyzer = new WhitespaceAnalyzer();
		}

		return analyzer;
	}

}

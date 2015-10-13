package rcv2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RCV2DataReader {
	
	public static void main (String[] args) throws FileNotFoundException {
//		String path = "/shared/shelley/yqsong/data/rcv2/RCV2_Multilingual_Corpus/chinese/FDCH1/19281.xml";
//		Analyzer analyzer = AnalyzerFactory.initialize("zh");
//		readDocument(path, analyzer);
		
//		String path = "/shared/shelley/yqsong/data/rcv2/RCV2_Multilingual_Corpus/japanese/FDJA1/421896.xml";
//		Analyzer analyzer = AnalyzerFactory.initialize("ja");
//		readDocument(path, analyzer);
		
		String path = "/shared/shelley/yqsong/data/rcv2/RCV2_Multilingual_Corpus/danish/REUTDA6/86619.xml";
		Analyzer analyzer = AnalyzerFactory.initialize("da");
		RCV2Data data	 = readDocument(path, analyzer);
		System.out.println(data.categories);
		System.out.println(data.content);
		
	}
	
	public static RCV2Data readDocument (String path, Analyzer analyzer) throws FileNotFoundException {
		String document = IOManager.readContent(path);
		Pattern pattern = null;
		Matcher matcher = null;
		
		String headLine = "";
		String content = "";
		List<String> categories = new ArrayList<String>();
		
		pattern = Pattern.compile("<headline>(.*?)</headline>");
		matcher = pattern.matcher(document);
		while (matcher.find()) {
			headLine = (matcher.group(1)).trim();
	    }
		
		pattern = Pattern.compile("<text>(.*?)</text>");
		matcher = pattern.matcher(document);
		while (matcher.find()) {
			String text = (matcher.group(1)).trim();
			
			Pattern subPattern = null;
			Matcher subMatcher = null;
			subPattern = Pattern.compile("<p>(.*?)</p>");
			subMatcher = subPattern.matcher(text);
			while (subMatcher.find()) {
				content += (subMatcher.group(1)).trim() + " ";
		    }
	    }
		
		pattern = Pattern.compile("<codes class=\"bip:topics:1.0\">(.*?)</codes>");
		matcher = pattern.matcher(document);
		while (matcher.find()) {
			String text = (matcher.group(1)).trim();
			int start = 0;
			while (start != -1) {
				int index = text.indexOf("<code code=\"", start);
				if (index == -1)
					break;
				int cateStart = index + "<code code=\"".length();
				int cateEnd =  text.indexOf("\">", cateStart);
				start = cateEnd + 1;
				if (cateStart != -1) {
					String cate = text.substring(cateStart, cateEnd);
					categories.add(cate);
				}
			}
	    }
		
//		System.out.println(headLine);
//		System.out.println(content);
//		for (int i = 0; i < categories.size(); ++i) {
//			System.out.println(categories.get(i));
//		}
		
		RCV2Data data = new RCV2Data();
		data.headLine = headLine;
		data.content = content;
		data.categories = categories;
		data.contentAll = headLine + " " + content;
		
		try {

			String contentNew = "";
			TokenStream stream = analyzer.tokenStream(null, data.contentAll);
			CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
			  contentNew += cattr.toString() + " ";
			}
			stream.end();
			stream.close();
			
			data.tokenizedContent = contentNew;
			
//			System.out.println(data.contentAll);
//			System.out.println(contentNew);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
		
	}

}

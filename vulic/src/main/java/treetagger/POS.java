package treetagger;

//import org.annolab.tt4j.TokenHandler;
//import org.annolab.tt4j.TreeTaggerException;
//import org.annolab.tt4j.TreeTaggerWrapper;
import vulic.MainClass;

import java.io.IOException;

/**
 * Created by upadhya3 on 9/21/15.
 */
public class POS {

    private static String TREETAGGER = "/shared/experiments/upadhya3/tree-tagger";
    public static void main(String[] args) {

//        System.setProperty("treetagger.home",TREETAGGER);
//        TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
//
//        try {
//            tt.setModel("english.par:utf8");
//            tt.setHandler(new TokenHandler<String>()
//            {
//                public void token(String token, String pos, String lemma)
//                {
//                    System.out.println(token + "\t" + pos + "\t" + lemma);
//                }
//            });
//
//            tt.process(new String[] { "This", "is", "a", "test", "." });
//        } catch (TreeTaggerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            tt.destroy();
//        }
    }
}

package vulic;

import edu.illinois.cs.cogcomp.bigdata.lucene.Lucene;
import org.apache.lucene.index.IndexReader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by upadhya3 on 9/25/15.
 */
public class DocVector {

    static Map<String, Float> idfs;

    public DocVector()
    {
        // load idfs into mapdb
        // load word vecs into mapdb
    }
    public static void main(String[] args) throws IOException {
        IndexReader reader = null;
        idfs = Lucene.getIdfs(reader, "text");
    }

    public float[] getDocVector(String[] tokens)
    {
        float[] doc_vec= new float[Params.VECSIZE];
        for(String tok:tokens)
        {
            float[] vec = fetchVector(tok);
            Float scale = idfs.get(tok);
            for(int i=0;i<doc_vec.length;i++)
            {
                doc_vec[i]+=scale*vec[i];
            }
        }
        return doc_vec;
    }

    private float[] fetchVector(String tok) {
        return null;
    }
}

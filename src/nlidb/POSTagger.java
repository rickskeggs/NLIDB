/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlidb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 *
 * @author rskeggs
 */
public class POSTagger 
{
    public String strTokenModel;
    
    public void setTokenModel(String strTokenModel)
    {
        this.strTokenModel = strTokenModel;
    }
    
    public String getTokenModel()
    {
        return this.strTokenModel;
    }

    public String [] tagger(String [] inputTokens)
    {
        InputStream modelIn = null;
        String tags[] = null;
        
        try
        {
            //modelIn = new FileInputStream("classifiers/en-pos-maxent.bin");
            System.out.println("Model " + getTokenModel());
            modelIn = new FileInputStream(getTokenModel());
            System.out.println( "Available " + modelIn.available());
            System.out.println("Model " + modelIn.toString() );
            POSModel model = new POSModel(modelIn);
            
            POSTaggerME tagger = new POSTaggerME(model);
            
            		  
            tags = tagger.tag(inputTokens);
        }
        catch (IOException e){ e.printStackTrace(); }
        finally
        {
            if (modelIn != null)
            {
                try { modelIn.close(); }
                catch (IOException e) {}
            }
        }
        return tags;
    }
    
    public static void main(String[] args)
    {
        NLTokenizer nlt = new NLTokenizer();
        
        //nlt.setTokenModel("en-pos-model.bin");
        nlt.strTokenModel = "classifiers/en-token.bin";
        //Which customer has the 
        String tokens[] = nlt.tokenize("Which customer has the postcode CM13#2PE");
        //String tokens[] = nlt.tokenize("postcode CM13#2LR");
        
        POSTagger pos = new POSTagger(); 
        pos.setTokenModel("en-pos-model.bin");
        String[] tags = pos.tagger(tokens);
        
        for (int i=0; i<tokens.length; i++)
        {
            System.out.println(tags[i] + " "+ tokens[i]);
        }
            
                
    }
}

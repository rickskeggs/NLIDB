/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlidb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 *
 * @author rskeggs
 */
public class NLTokenizer 
{
    protected String strTokenModel;
    
    public void setTokenModel(String strTokenModel)
    {
        this.strTokenModel = strTokenModel;
    }
    
    public String getTokenModel()
    {
        return strTokenModel;
    }
    
    public String [] tokenize(String inputStr)
    {
        InputStream modelIn=null;
        String tokens[]=null;
        
        try
        {
            //modelIn = new FileInputStream("classifiers/en-token.bin");
            modelIn = new FileInputStream(strTokenModel);
            TokenizerModel model = new TokenizerModel(modelIn);
            
            Tokenizer tokenizer = new TokenizerME(model);
            tokens = tokenizer.tokenize(inputStr);
        }
        catch (IOException e) { e.printStackTrace(); }
        finally
        {
            if (modelIn != null)
            {
                try { modelIn.close(); }
                catch (IOException e) { }
            }       
            
        }
        return tokens;
    }
    
    public static void main(String[] args)
    {
        NLTokenizer nlt = new NLTokenizer();
        nlt.strTokenModel = "classifiers/en-token.bin";
        String tokens[] = nlt.tokenize("Which customer has the postcode CM13#2LR");
        
        for (int i=0; i<tokens.length; i++)
        {
            System.out.println(tokens[i]);
        }
            
                
    }
            
}

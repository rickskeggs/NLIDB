/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlidb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


/**
 *
 * @author rskeggs
 */
public class POSNLIDB 
{
    static String WHITESPACE = null;
    static String TAGGER = null;
    static String TOKENIZER = null;
    
    public POSNLIDB(String properties)
   {
       try
       {   
           //Reading properties file in Java example
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(properties);
     
            //loading properites from properties file
            props.load(fis);

            //reading property
            WHITESPACE = props.getProperty("grammar.whitespace");
            TAGGER = props.getProperty("models.taggermodel");
            TOKENIZER = props.getProperty("models.tokenizermodel");
            
       }
       catch(FileNotFoundException fnfe){ fnfe.printStackTrace(); }
       catch(IOException ioe){ ioe.printStackTrace(); }        
    }
    
    public ArrayList query(String tags[], String tokens[])
    {
        ArrayList query = new ArrayList();
        ArrayList sqlQuery = new ArrayList();
        String newToken = null;
        //System.out.println("TAGS " + tags.length + " TOKENS " + tokens.length);
        String sql = " Select * from ";
        String [] parts;
        String [] table = new String [2];
        
        for (int i=0; i<tokens.length; i++)
        {
            if (!tags[i].matches("irr"))
            {
                //System.out.println("TAGS " + tags[i] + " TOKENS " + tokens[i]);

                //System.out.print(tags[i] + " "+ tokens[i]);
                if (tokens[i].contains(WHITESPACE))
                {
                    newToken = tokens[i].replaceAll(WHITESPACE, " ");
                }
                else
                {
                    newToken = tokens[i];
                }
                query.add(tags[i] + " = " + newToken);
            }
        }
        
        for (int i=0; i < query.size(); i++)
        {
            
            parts = query.get(i).toString().split("=");
            if (parts[0].contains("."))
            {
                //System.out.println(sql);
                table = parts[0].split("\\.");
                sql = sql + " " + table[0];
            }
            else
            {
                sql = sql + " where " + query.get(i).toString();
                //System.out.println(sql);
                sqlQuery.add(sql);
            }    
            
            
        }
        return sqlQuery;
    }   
    
    
    public static void main(String[] args)
    {
        NLTokenizer nlt = new NLTokenizer();
        POSNLIDB nlidb = new POSNLIDB("/Users/rskeggs/NetBeansProjects/NLPTraining/src/nlptraining/trainingdata.properties");
        
        
        nlt.strTokenModel = TOKENIZER;
         
        String tokens[] = nlt.tokenize("Which customer has the postcode KT6#6FF");
        //String tokens[] = nlt.tokenize("postcode CM13#2LR");
        
        POSTagger pos = new POSTagger(); 
        pos.setTokenModel(nlidb.TAGGER);
        String[] tags = pos.tagger(tokens);
        
        ArrayList list = nlidb.query(tags, tokens);
        
        for (int i=0; i < list.size(); i++)
        {
            System.out.println(list.get(i));
        }
            
            
    }    
}

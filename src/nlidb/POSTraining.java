/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlidb;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 *
 * @author rskeggs
 */
public class POSTraining
{
    protected String strModelFile;
    
    public POSModel trainModel(String modelDataFile)
    {
        POSModel model = null;
        InputStream dataIn = null;
        try 
        {
            //dataIn = new FileInputStream("training/en-pos-model.train");
            dataIn = new FileInputStream(modelDataFile);
            
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
            ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

            model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), null, null);
            
            //model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), null);
        }
        catch (IOException e)
        {
            // Failed to read or parse training data, training failed
            e.printStackTrace();
        }
        finally
        {
            if (dataIn != null) 
            {
                try 
                {
                    dataIn.close();
                }
                catch (IOException e)
                {
                    // Not an issue, training already finished.
                    // The exception should be logged and investigated
                    // if part of a production system.
                    e.printStackTrace();
                }
            }
        }
        return model;
    }        
            
    public void saveModel(POSModel model)
    {
        OutputStream modelOut = null;
        try
        {
            modelOut = new BufferedOutputStream(new FileOutputStream(strModelFile));
            model.serialize(modelOut);
        }
        catch (IOException e)
        {
            // Failed to save model
            e.printStackTrace();
        }
        finally
        {
            if (modelOut != null)
            {
                try { modelOut.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
    
    public String getModelFile()
    {
        return strModelFile;
    }
    
    public void setModelFile(String strModelFile)
    {
        this.strModelFile = strModelFile;
    }
    
    public static void main(String[] args)
    {
        POSTraining post = new POSTraining();
        post.setModelFile("en-pos-model.bin");
        POSModel model = post.trainModel("en-pos-model.train");
        post.saveModel(model);
        System.out.println(post.getModelFile());
    }
}

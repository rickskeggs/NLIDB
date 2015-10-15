/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nlptraining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



/**
 *
 * @author rskeggs
 */
public class NLPTraining 
{
   
   static String DB_URL = "";
   static String JDBC_DRIVER = "";
   static String USER = "";
   static String PASS = "";
   static String LINE_ENTRY="";
   static String SQL_QUERY = "";
   static String LINE_NAME="";
   static String TRAINING_DATA = "";
   static String HEADER_FILE = "";
   static String WHITESPACE = "";
   
   public NLPTraining(String properties)
   {
       try
       {   
           //Reading properties file in Java example
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(properties);
     
            //loading properites from properties file
            props.load(fis);

            //reading property
            USER = props.getProperty("jdbc.username");
            PASS = props.getProperty("jdbc.password");
            JDBC_DRIVER = props.getProperty("jdbc.driver");
            DB_URL = props.getProperty("jdbc.url");
            SQL_QUERY = props.getProperty("jdbc.query");
            TRAINING_DATA = props.getProperty("jdbc.trainingdata");
            LINE_ENTRY = props.getProperty("jdbc.lineentry");
            LINE_NAME = props.getProperty("jdbc.linename");
            HEADER_FILE = props.getProperty("grammar.headerfile");
            WHITESPACE = props.getProperty("grammar.whitespace");
            
            System.out.println("jdbc.username: " + USER);
            System.out.println("jdbc.driver: " + JDBC_DRIVER);
            System.out.println("jdbc.lineentry" + LINE_ENTRY);
        }
       catch(FileNotFoundException fnfe){ fnfe.printStackTrace(); }
       catch(IOException ioe){ ioe.printStackTrace(); }        
    }

   public List JDBCExecute()
   {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rsTableList = null;
        List resultList = null;
//        String strDataLine = LINE_ENTRY;
//        String strWriteData;
        
        try
        {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = conn.createStatement();
      
      
            rsTableList= stmt.executeQuery(SQL_QUERY);
            resultList = resultSetToArrayList(rsTableList);
            
            System.out.println("Database searched successfully...");
        }
        catch(SQLException se){ se.printStackTrace(); }
        catch(Exception e){ e.printStackTrace(); }
        finally
        {
            //finally block used to close resources
            try
            {
                if(stmt!=null)
                    stmt.close();
            }
            catch(SQLException se2){ }// nothing we can do
            try
            {
                if(conn!=null) conn.close(); 
            }
            catch(SQLException se){ se.printStackTrace(); }//end finally try
        }//end try
        System.out.println("Goodbye!");
        return resultList;
    }
   
    public void createData(List rs) 
    {
        String strDataLine = LINE_ENTRY;
        String strWriteData;

        
        
        try
        {    
            File file = new File(TRAINING_DATA);
 
            // if file doesnt exists, then create it
            if (!file.exists())
            {
		file.createNewFile();
            }
            
            IOCopier.joinFiles(file, new File(HEADER_FILE));
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            
            Iterator itr = rs.iterator();
            while(itr.hasNext())
            {
                Object element = itr.next().toString();
                String strElement [] = element.toString().split("=");
            
                strWriteData = strDataLine.format(strDataLine, LINE_NAME, strElement[1].replace("}", "") );
                bw.write(strWriteData);
                bw.newLine();

            }            
            
            bw.flush();
            bw.close();
            

        }
        catch (IOException ioe) { ioe.printStackTrace(); }

    }
    
    public List<HashMap<String,Object>> resultSetToArrayList(ResultSet rs) throws SQLException 
    {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        while (rs.next())
        {
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i)
            {    
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
            
        }

    return list;
    }
    
    public static void main(String[] args)
    {
        //if (args.length > 0)
        //{    
            //NLPTraining nlpt = new NLPTraining(args[0]);
            NLPTraining nlpt = new NLPTraining("/Users/rskeggs/NetBeansProjects/NLPTraining/src/nlptraining/trainingdata.properties");
            List rs = nlpt.JDBCExecute();
            nlpt.createData(rs);
        //}        
    }
}

//}//end JDBCExample
    


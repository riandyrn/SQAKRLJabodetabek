package com.sqakrljabodetabek.sql_things;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQL {

	private Connection con = null;
    private PreparedStatement pst = null;
    
    private String db_url = "";
    private String db_user = "";
    private String db_password = "";
    
    private final int NUMBER_OF_PARAMETERS = 5;
    
    public SQL()
    {
    	initConfig();
    }
    
    private void initConfig()
    {
    	ArrayList<String> configs = loadTextFile("db_config");
    	
        db_url = "jdbc:mysql://" + configs.get(0) + ":" + configs.get(1) + "/" + configs.get(2);
        db_user = configs.get(3);
        
        if(configs.size() == NUMBER_OF_PARAMETERS)
        {
        	db_password = configs.get(4); //for allowing empty password
        }
        
    }
    
    private ArrayList<String> loadTextFile(String filename)
	{
		ArrayList<String> ret = new ArrayList<>();
		
		URL file = getClass().getResource(filename);
        BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(file.openStream()));
			
			String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        {
	        	if(inputLine.charAt(0) != '#')
	        	{
	        		ret.add(inputLine);
	        	}
	        }
	            
	        in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}
    
	public SQLRows executeSelect(String query)
	{
		ResultSet rs = null;
		SQLRows rows = new SQLRows();
		
        try {
        	con = DriverManager.getConnection(db_url, db_user, db_password);
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            
            ResultSetMetaData metadata = rs.getMetaData();
            
            while(rs.next())
            {
            	ArrayList<String> columns = new ArrayList<>();
            	ArrayList<String> values = new ArrayList<>();
            	
            	for(int i = 1; i <= metadata.getColumnCount(); i++)
                {
            		columns.add(metadata.getColumnName(i));
            		values.add(rs.getString(i));
                }
            	
            	SQLRow row = new SQLRow(values, columns);
            	rows.add(row);
            }
            
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(getClass().getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(getClass().getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        
        return rows;
        
	}
	
	public static void printRows(SQLRows rows)
	{
		
		for(SQLRow row: rows.getContent())
		{
			row.printRow();
			System.out.println();
		}
	}
	
	public void driver(String query)
	{
		SQLRows rows = executeSelect(query);
		printRows(rows);
	}
	
    public static void main(String args[])
    {
    	SQL sql = new SQL();
    	sql.driver("SELECT id, name FROM patient");
    }
	
}

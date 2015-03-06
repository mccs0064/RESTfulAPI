package com.rest.steven.status;


import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import com.rest.dao.*;


@Path("/v1/status/")
public class V1_status {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(){
		
		return "<p>Java Rest Service</p>";
	}
	
	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion(){
		
		return "<p><b>Version</b>: 1.111</p>";
	}
	
	
	@Path("/database")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnDatabaseStatus() throws Exception{
		
		PreparedStatement query = null;
		String myString = null;
		String returnString = null;
		Connection conn = null;
		
		try{
			conn = Sql.SqlConn().getConnection();
			query = conn.prepareStatement("SELECT * FROM  posts");
			
			ResultSet rs = query.executeQuery();
			
			while (rs.next()){
				myString += rs.getString("user_name") + "<br/>";
				myString += rs.getString("TEXT") + "<br/> ";
				myString += rs.getString("time_posted");
				
			}
			query.close();//close connection
			
			returnString = "<p>DB TIME: "+ myString + "</p>";
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		finally{
			if (conn != null) conn.close();
		}
		
		
		return returnString;
	}


}

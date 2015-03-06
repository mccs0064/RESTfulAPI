package com.rest.dao;
import java.sql.*;
import org.codehaus.jettison.json.JSONArray;
import com.steven.util.*;

public class SchemaRest extends Sql {
	
	//query the database all posts for the specified user. 
	//Return result as JSON.
public JSONArray queryReturnUserPosts(String user) throws Exception {
		
	//set statement and query to null
		PreparedStatement query = null;
		Connection conn = null;
	//create new instance of JSON converter  
		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();
		
		//try to make connection to database
		//query for all posts that have the specified user name
		//convert the result to JSON
		//close our connection
		try {
				conn = sqlConnection();
				query = conn.prepareStatement("SELECT  * FROM  posts WHERE  user_name =  ?");
				
				query.setString(1, user); //protect against SQL injection
				ResultSet rs = query.executeQuery();
				
				json = converter.toJSONArray(rs);
				query.close(); //close connection
		}
		
		catch(SQLException sqlError) {
			sqlError.printStackTrace();
			return json;
		}
		catch(Exception e) {
			e.printStackTrace();
			return json;
		}
		finally {
			if (conn != null) conn.close();//make sure connection close
		}
		
		return json;
	}

	//query the database all posts. 
	//Return result as JSON.
public JSONArray allPostsFeed() throws Exception {
		
	//set statement and query to null
		PreparedStatement query = null;
		Connection conn = null;
	//create new instance of JSON converter  
		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();
		
		//try to make connection to database
		//query for all posts 
		//convert the result to JSON
		//close our connection
		try {
				conn = sqlConnection();
				query = conn.prepareStatement("SELECT  * FROM  posts ");
				
				ResultSet rs = query.executeQuery();
				
				json = converter.toJSONArray(rs);
				query.close(); //close connection
}
		
		catch(SQLException sqlError) {
			sqlError.printStackTrace();
		
		}
		catch(Exception e) {
			e.printStackTrace();
	
		}
		finally {
			if (conn != null) conn.close();//make sure connection close
		}
		
		return json;
	}
	
	//create a new post
	//inserts the user name and post into the database
	//if there is a problem we return a 500 response
	//if the record has been inserted successfully, a 200 is returned
public JSONArray insertIntoPost(String userName, String text, String lat, String lon, String city, String temprature)

	throws Exception {
	//create new instance of JSON converter  
			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();
	
		PreparedStatement query = null;
		Connection conn = null;
		
		
		try {

			conn = sqlConnection();
			query = conn.prepareStatement("INSERT INTO rest.posts (user_name, text, lat, lon, city, temprature) VALUES (?, ?, ?, ?, ?, ?) ");
			query.setString(1, userName);//protect against SQL injection
			query.setString(2, text);//protect against SQL injection
			query.setString(3, lat);//protect against SQL injection
			query.setString(4, lon);//protect against SQL injection
			query.setString(5, city);//protect against SQL injection
			query.setString(6, temprature);//protect against SQL injection
			query.executeUpdate();
			
			
			//grab the record we just inserted to return to the view
			query = conn.prepareStatement("SELECT * FROM posts ORDER BY post_id DESC LIMIT 1");
			ResultSet rs = query.executeQuery();
			json = converter.toJSONArray(rs);
			
			
		
			// if a error occurs, return a 500
		} catch (Exception e) {
			e.printStackTrace();
		
		
			//always close our connection 
		} finally {
			if (conn != null)
				conn.close();
		}
		// if all goes as planned return a 200 response with the JSON that was inserted 
		query.close(); //close connection
		return json;
	}

	//create a new comment
	//inserts the comment and its details into the database
	//if there is a problem we return a 500 response
	//if the record has been inserted successfully, a 200 is returned
public JSONArray insertIntoComment(String author, String comment_text, String comment_post_id)
		
		throws Exception {
			//create new instance of JSON converter  
			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();
		
			PreparedStatement query = null;
			Connection conn = null;

			try {

				conn = sqlConnection();
				query = conn.prepareStatement("INSERT INTO rest.comments (author, comment_text, comment_post_id) VALUES (?, ?, ?) ");
				query.setString(1, author);//protect against SQL injection
				query.setString(2, comment_text);//protect against SQL injection
				query.setString(3, comment_post_id);//protect against SQL injection
			
				
				//execute our query
				query.executeUpdate();
			
				//grab the record we just inserted to return to the view
				query = conn.prepareStatement("SELECT * FROM comments ORDER BY comment_id DESC LIMIT 1");
				ResultSet rs = query.executeQuery();
				json = converter.toJSONArray(rs);
				
				
			
				// if a error occurs, controller will return a 500
			} catch (Exception e) {
				e.printStackTrace();
			
			
				//always close our connection 
			} finally {
				if (conn != null)
					conn.close();
			}
			// if all goes as planned return controller returns 200 response with the JSON that was inserted 
			query.close(); //close connection
			return json;
		}


	//queries the database for all comments
	//returns the comments in JSON
public JSONArray allCommentsFeed() throws Exception {
	
	//set statement and query to null
		PreparedStatement query = null;
		Connection conn = null;
	//create new instance of JSON converter  
		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();
		
		//try to make connection to database
		//query for all comments
		//convert the result to JSON
		//close our connection
		try {
				conn = sqlConnection();
				query = conn.prepareStatement("SELECT  * FROM  comments ");
				
				ResultSet rs = query.executeQuery();
				
				json = converter.toJSONArray(rs);
				query.close(); //close connection
}
		
		catch(SQLException sqlError) {
			sqlError.printStackTrace();
		
		}
		catch(Exception e) {
			e.printStackTrace();
	
		}
		finally {
			if (conn != null) conn.close();//make sure connection close
		}
		
		return json;
	}
	


}



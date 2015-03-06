package com.rest.steven.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.*;

import com.rest.dao.*;
import com.rest.steven.model.CommentModel;

@Path("/comments")
public class V1_CommentController {
	
	// Make a GET request for all of the Comments 
	// This is used for our Feed/Home page
	// The request will return JSON to our front end
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllComments() throws Exception {
		String returnString = null;
		JSONArray json = new JSONArray();

		try {
			
			SchemaRest dao = new SchemaRest();
			json = dao.allCommentsFeed();
			returnString = json.toString();

		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok(returnString).build();

	}

	
	//post comment
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComment(String incomingData) throws Exception {
		
		SchemaRest dao = new SchemaRest();
		String returnString = null;
		JSONArray json = new JSONArray();
	try {

		//create a new object mapper
		ObjectMapper mapper = new ObjectMapper();
		//map the incoming data to our CommentModel Class
		CommentModel CommentModel = mapper.readValue(incomingData,
				CommentModel.class);
		
		//send data to insertIntoComment Method
		json = dao.insertIntoComment( CommentModel.author,
									  CommentModel.comment_text,
									  CommentModel.comment_post_id);

		returnString = json.toString();
		
	} catch (Exception e) {
		e.printStackTrace();
		return Response.status(500)
				.entity("Server was not able to process your request")
				.build();
	}
	//return 200 with the inserted record
	return Response.ok(returnString).build();
	
}

	
}



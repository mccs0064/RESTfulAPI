package com.rest.steven.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.*;

import com.rest.dao.*;
import com.rest.steven.model.PostModel;

@Path("/posts")
public class V1_PostController {
	// Make a GET request for all of the posts for all of the users
	// This is used for our Feed/Home page
	// The request will return JSON to our front end
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllUsers() throws Exception {
		String returnString = null;
		JSONArray json = new JSONArray();

		try {
			SchemaRest dao = new SchemaRest();
			json = dao.allPostsFeed();
			returnString = json.toString();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok(returnString).build();

	}

	// get individual user posts
	@Path("/{user}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnUserPosts(@PathParam("user") String user)
			throws Exception {

		String returnString = null;
		JSONArray json = new JSONArray();

		try {
			if (user == null) {
				return Response.status(400).entity("Error: No User Name!!!!")
						.build();
			}

			SchemaRest dao = new SchemaRest();
			json = dao.queryReturnUserPosts(user);
			returnString = json.toString();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok(returnString).build();

	}

	//add a post 
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPost(String incomingData) throws Exception {
		SchemaRest dao = new SchemaRest();
		String returnString = null;
		JSONArray json = new JSONArray();
		try {			
			
			/*
			 * ObjectMapper is from Jackson Processor framework
			 * http://jackson.codehaus.org/
			 * 
			 * Using the readValue method, you can parse the json from the http
			 * request and data bind it to a Java Class.
			 */
			
			// create a new object mapper
			ObjectMapper mapper = new ObjectMapper();
			//map incoming data to PostModel class
			PostModel PostModel = mapper.readValue(incomingData,
					PostModel.class);
			//send data to insertIntoPost Method
			json = dao.insertIntoPost(PostModel.userName,
									  PostModel.text,
									  PostModel.lat,
									  PostModel.lon,
									  PostModel.city,
									  PostModel.temprature);
			
			returnString = json.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		}

		return Response.ok(returnString).build();
	}

}


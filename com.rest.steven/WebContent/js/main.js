$(document).ready(function (){
	var $posts = $('#posts');
	var $name = $('#name');
	var $message = $('#message');
	var $city = $('#city');

//	add post function / template
	function addPost(message){
		$posts.append('<li>'+
				'<div class="panel panel-primary">'+
				'<div class="panel-heading">'+
				'<h3 class="panel-title">'+
				'<a href="/com.rest.steven/api/posts/'+message.user_name+
				'">'+message.user_name+'</a></h3>'+
				'</div>'+
				'<div class="panel-body">'+
				' <p><strong>Posted: </strong>'+message.time_posted+'</p>'+
				' <p><strong>City:</strong> '+message.city+'</p>'+
				' <p><strong>Temprature:</strong> ' +message.temprature+'&deg;</p>'+
				'<h3>Message: </h3> <p>'+message.text+ '</p>'+
				'</div>'+
				'<div class="panel-footer">'+
				'<h4>Comments</h4>'+
				'<ul id="'+message.post_id+'"></ul>'+
				'<ul>'+
				'	 <p>name: <input type = "text" class="CommentName"></p>'+
				'	 <p>message: <input type = "text" class="CommentText"></p>'+
				'	 <button data-id="'+message.post_id+'" class="add-comment btn btn-default">Comment!</button></ul>'+'</div></div></li>');
	}
	
//add comment function
	function addComment(msg){
		$id = "#"+msg.comment_post_id;
		$comm = $($id);
		$comm.append('<li>'+msg.author+' says:<br>'+msg.comment_text+'</li><br>');	
	}

//get all posts an add them to the page
	$.ajax({
		type: 'GET',
		url:'http://localhost:7001/com.rest.steven/api/posts',
		success: function(post)
		{
			$.each(post, function(i,message){
				addPost(message);		
			});//end for each post
		},
		error: function(){
			alert("Something went terribly wrong call the cops !");
		}

	});


	// add post to the database then deal with the response
	$('#add-message').on('click',function() {
		var geocoder =  new google.maps.Geocoder();
		var $lat;
		var $lon;
		var $current_temp;
		//get lat n long from google api
		geocoder.geocode( { 'address': $('#city').val()}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				$lat= results[0].geometry.location.lat();
				$lon= results[0].geometry.location.lng();
				console.log("location : " + results[0].geometry.location.lat().toFixed(4) + 
						" " +results[0].geometry.location.lng().toFixed(4));
				
				//get weather request from wunderground api
				$.ajax({
					type : 'GET',
					url : 'http://api.wunderground.com/api/8915131b1786d3a5/conditions/q/'+$lat+','+$lon+'.json',
					success : function(weather) {
						$current_temp = weather.current_observation.temp_c;
						
						//create the post object with correct data
						var post = {
								userName : $name.val(),
								text : $message.val(),
								lat: $lat,
								lon: $lon,
								city: $('#city').val(),
								temprature: $current_temp
						};
						
						console.log(post);//log out post to debug info 
						
						//send our object to the back end and deal with a response
						$.ajax({
							type : 'POST',
							url : "http://localhost:7001/com.rest.steven/api/posts",
							data : JSON.stringify(post),
							contentType : "application/json",
							success : function(newPost) {
								$.each(newPost, function(i,message){
									addPost(message);
								});
								//clear input fields
								$('#name').val('');
								$('#message').val('');
								$('#city').val('');
							}
						});
						//end post 
					},

					error : function() {
						alert("Something went terribly wrong call the cops !");
					}

				});

			} else{alert("SOMETHING WENT WRONG WITH GEOLOCATION");}

		});

	});


	//add comment
	$posts.delegate('.add-comment','click',function() {
		
		//grab the info in the comment box
		var $ul = $(this).closest('ul');
		var $comment_author = $ul.find('input.CommentName').val();
		var $comment_text = $ul.find('input.CommentText').val();
		var $btnID = $(this).attr('data-id');

		//add info into comment object 
		var comment = {
				author : $comment_author,
				comment_text : $comment_text,
				comment_post_id: $btnID
		};

		$.ajax({
			type : 'POST',
			url : "http://localhost:7001/com.rest.steven/api/comments",
			data : JSON.stringify(comment),
			contentType : "application/json",
			success : function(newComment) {
				var $comm;
				var $id;
				console.log(comment);
				$.each(newComment, function(x, msg) {
					addComment(msg);
				});
				//clear input fields
				$ul.find('input.CommentText').val('');
				$ul.find('input.CommentName').val('');
			}

		});

	});

//	get all comments and add them as a child element to the appropriate post item.
	$.ajax({
		type : 'GET',
		url : 'http://localhost:7001/com.rest.steven/api/comments',
		success : function(comment) {
			var $comm; //=$("#11");
			var $id;
			$.each(comment, function(x, msg) {
				addComment(msg);
			});

		},

		error : function() {
			alert("Something went terribly wrong call the cops !");
		}

	});

});


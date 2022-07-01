#  Critics System

#  Critics Service

**Developer: Sogolon Vieira Jauará**  
[Github](https://github.com/Sogolonmvj)
[Email](mailto:sogolonmvj@yahoo.com)

<https://github.com/Sogolonmvj/ItauChallenge.git>

This project was created as a step of the Itaú Dev Experts selection process. The objective of this project was to show my coding abilities.

This application is a service that belongs to the Critics System. It was developed in order to permit users to search for movies watched by them and rate, comment, like or dislike. To use this system users need to register themselves and after this they can log in the application to  interact. 

All users start as READER which is the lowest role level and can only see informations about the movie, comments and rate a movie. When the "critic" rate a movie the user get a point and when the person reaches 20 points the role is increased to BASIC which has all the capacities of the latest and can post a comment and reply to other comments getting 1 point for each interaction. As soon as the "critic" reaches 100 points, an ADVANCED role is awarded and the user have all the capacities of the lowest ones and can tag other comments and like or dislike them. The highest role is MODERATOR which comes after the ADVANCED and is awarded by the "critic" when 1000 points are acumulated or when endorsed with this role by another MODERATOR. This role have all the capacities of the others and more to delete a comment and mark as repeated.

Security techniques were used in this application and for this reason the authentication process is made by another service. This other service can be found in the following repository:

###  Service (Authenticator) <https://github.com/Sogolonmvj/ItauChallengeAuthorizationMicroservice.git>

## Architecture
The following architecture was followed in this project:

![structure](https://raw.githubusercontent.com/Sogolonmvj/ItauChallenge/main/img.png?token=GHSAT0AAAAAABWGHDID4L44IDKZNSYXPMVKYV7C4FA)

## How to execute

If you want to execute this apllication you can clone this github repository and execute in your machine or you can copy only the `docker-compose.yml` file that is present in this repository and when in same directory as the file downloaded do

> docker-compose up -d

in the terminal and wait for the download of the images and building of the containers.

##  Endpoints documentation

To start using this system users must register themselves sending a request using the method POST to the following endpoint:

>POST <http://localhost:8080/registration>

In the body of the request the user must send the FIRST and LAST NAMES, the EMAIL and the PASSWORD as following:

```sh
{

	"firstName":  "Jim",

	"lastName":  "Carey",

	"email":  "carey@mask.com",

	"password":  "H4lL0"

}
```
After doing these steps an email will be sent to the mail server that the user can access using the browser in:

>GET (Browser) <http://localhost:1080>

It is important to know that an user will not be able to log in the system if the email was not confirmed before. 

Then, the user will be ready to login sending a POST request with the credentials as X-WWW-FORM-URLENCODED content type to:

>POST <http://localhost:8081/api/login>

In the example registered above it should be:

| key | value |
|--|--|
| username: | carey@mask.com |
| password: | H4lL0 |

Therefore, the user will receive as response the token to access the critics system as following:

```sh
	{
		"access_token":  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJleUB1c2EuY29tIiwicm9sZXMiOlsiUkVBREVSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MS9hcGkvbG9naW4iLCJleHAiOjE2NTY2NTQ3MjF9.FQKOUc6oYWZnaCC5D8w8mQ6E-OIzlQ_gjhCXGbYycUk"
	}
```
Again, it is important to know that if an user try to log in the system with a wrong password for 3 times, in the 4th time a "Login attempt limit exceeded" exception will be thrown by the system.

The user can get the refresh token sending a request to:

>GET <http://localhost:8080/api/token/refresh>

Now, the user can utilize the system sending the access token in the header as following:

| Key | Value |
|--|--|
| Authorization: | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJleUB1c2EuY29tIiwicm9sZXMiOlsiUkVBREVSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MS9hcGkvbG9naW4iLCJleHAiOjE2NTY2NTQ3MjF9.FQKOUc6oYWZnaCC5D8w8mQ6E-OIzlQ_gjhCXGbYycUk |
 
Now, the user is a critic and can search for all movies in the database in:

>GET <http://localhost:8080/movies>

Or search for a specific movie in:

>GET <http://localhost:8080/movies/titanic>

Example of the data retrieved:

```sh
{
	"id":  1,

	"imdbID":  "tt0120338",

	"imdbRating":  "7.9",

	"imdbVotes":  "1,141,554",

	"rating":  0.0,

	"votes":  0,

	"comments":  [],

	"Title":  "Titanic",

	"Year":  "1997",

	"Runtime":  "194 min",

	"Genre":  "Drama, Romance"
}
```

In the example above the critic is searching for the movie Titanic.
Also, the critic can rate a movie sending the movie id and the rating as params as following:

>POST <http://localhost:8080/api/critic/rate?id=1&rating=10.0>

In the example above the critic gave 10.0 for Titanic which has id = 1;

Now,

```sh
{
	"id":  1,

	"imdbID":  "tt0120338",

	"imdbRating":  "7.9",

	"imdbVotes":  "1,141,554",

	"rating":  10.0,

	"votes":  1,

	"comments":  [],

	"Title":  "Titanic",

	"Year":  "1997",

	"Runtime":  "194 min",

	"Genre":  "Drama, Romance"
}
```
The rating has changed to 10.0 and the vote counter has increased by 1. If the second critic rated this movie with 5.0 the movie rating would be 7.5 as it is 15.0 divided by 2.

If the critic have BASIC role the endpoints to post comment and reply to comments are allowed to be accessed by them and it can be done as following:

>POST <http://localhost:8080/api/critic/comment?id=1>

This endpoint requires the comment in the body:

```sh
{
	"text":  "Very good movie!"
}
```
In the example above the critic carey@mask.com is commenting "Very good movie!" to Titanic and the result is the following:

```sh
{

	"id":  1,

	"imdbID":  "tt0120338",

	"imdbRating":  "7.9",

	"imdbVotes":  "1,136,141",

	"rating":  10.0,

	"votes":  1,

	"comments":  [

					{

						"id":  2,

						"username":  "carey@mask.com",

						"text":  "Very good movie!",

						"likes":  0,

						"dislikes":  0,

						"repeated":  false,

						"responses":  []

					}
				],

	"Title":  "Titanic",

	"Year":  "1997",

	"Runtime":  "194 min",

	"Genre":  "Drama, Romance"

},
```
To answer a comment the following endpoint must be used:

>POST <http://localhost:8080/api/critic/comment/response?id=2>

Note that id that is sent as param now is the comment id, not the movie id.

Example,

```sh
{

	"id":  2,

	"username":  "kasama@japan.com",

	"text":  "Nice!",

	"likes":  0,

	"dislikes":  0,

	"repeated":  false,

	"responses":  [

					{

						"id":  5,

						"username":  "carey@mask.com",

						"text":  "Agree bro!",

						"repeated":  false,

						"likes":  0,

						"dislikes":  0

					}

				]

}
```


To post a comment tagging another one the endpoint must be the following:

>POST <http://localhost:8080/api/critic/comment/tag?id=2>

Again, the id sent as param is the id of the comment being tagged, not the movie id.

Example,

```sh
{

	"id":  3,

	"username":  "kasama@japan.com",

	"text":  "That's it!",

	"taggedComment":  {

						"id":  2,

						"username":  "carey@mask.com",

						"text":  "Nice!",

						"likes":  0,

						"dislikes":  0,

						"repeated":  false,

						"responses":  []

					}

}
```

To like a comment:

>POST <http://localhost:8080/api/critic/comment/like?id=2>

Example,

```sh
{

	"id":  5,

	"username":  "carey@mask.com",

	"text":  "Agree bro!",

	"repeated":  false,

	"likes":  1,

	"dislikes":  0

}
```

Or dislike:

>POST <http://localhost:8080/api/critic/comment/dislike?id=2>

Example,

```sh
{

	"id":  5,

	"username":  "carey@mask.com",

	"text":  "Agree bro!",

	"repeated":  false,

	"likes":  1,

	"dislikes":  1

}
```


Also, a comment that is answering another comment can be liked doing:

>POST <http://localhost:8080/api/critic/comment/response/like?id=3>

Or disliked doing:

>POST <http://localhost:8080/api/critic/comment/response/dislike?id=3>

A moderator can mark a comment as repeated doing:

>POST <http://localhost:8080/api/moderator/comment/repeated?repeated_comment_id=2>

Example,
```sh
{

	"id":  5,

	"username":  "carey@mask.com",

	"text":  "Agree bro!",

	"repeated":  true,

	"likes":  1,

	"dislikes":  1

},
{

	"id":  8,

	"username":  "carey@mask.com",

	"text":  "Agree bro!",

	"repeated":  true,

	"likes":  0,

	"dislikes":  0

},

```

A comment response can be marked as repeated too:

>POST <http://localhost:8080/api/moderator/comment/response/repeated?repeated_comment_response_id=3>

To delete a comment:

>DELETE <http://localhost:8080/api/moderator/comment?id=5&comment_id=1>

The first id sent as param is the movie id and the second the id of the comment to be deleted.

>DELETE <http://localhost:8080/api/moderator/comment?id=5&comment_id=1>

The same can be done for a comment answering to another comment:

>DELETE <http://localhost:8080/api/moderator/comment/response?id=5&comment_response_id=7>

Being the first id sent as param the comment id and the second the comment response id.

The MODERATOR can endorse a critic as MODERATOR too doing:

>PATCH <http://localhost:8080/api/moderator/user/carey@mask.com>

Being carey@mask.com in the example above the user who will be endorsed.
This funcionality requires a body in the request that must have the following format:

    {
	    "userRole":  "Moderator"
	}

As of the moment when this documentation was written, the MODERATOR could only endorse a critic with the MODERATOR role. Further implementations might permit endorsement with other roles.

This is all! 

Thank you and enjoy the application!

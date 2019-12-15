
let url = "http://ec2-52-56-129-37.eu-west-2.compute.amazonaws.com:8888/";
// let url = "http://localhost:8888/";

// Movie
function addMovie(data) {
	return post("movies", data);
}

function deleteMovie(id) {
	return del("movies/" + id);
}

function getMovie(id) {
	return get("movies/" + id);
}

function getAllMovies() {
	return get("movies");
}

function updateMovie(id, data) {
	return put("movies/" + id, data);
}

// Director
function addDirector(data) {
	return post("directors", data);
}

function deleteDirector(id) {
	return del("directors/" + id);
}

function getDirector(id) {
	return get("directors/" + id);
}

function getAllDirectors() {
	return get("directors");
}

function updateDirector(id, data) {
	return put("directors/" + id, data);
}

// User
function addUser(data) {
	return post("users", data);
}

function addMovieToUserList(userID, listIndex, movieID) {
	return get("users/" + userID + "/" + listIndex + "/" + movieID);
}

function deleteUser(id) {
	return del("users/" + id);
}

function getUser(id) {
	return get("users/" + id);
}

function getAllUsers() {
	return get("users");
}

function hasLoggedIn(id, token) {
	return post("users/hasLoggedIn/" + id, token);
}

function loginUser(userDTO) {
	return post("users/login", userDTO);
}

function updateUser(id, data) {
	return put("users/" + id, data);
}

// Generic request method functions.
function get(query) {
	return request("GET", query);
}

function post(query, data) {
	return requestSend("POST", query, data);
}

function put(query, data) {
	return requestSend("PUT", query, data);
}

function del(query) {
	return request("DELETE", query);
}

// Generic request functions.
function request(type, query) {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + query;
  console.log("2 "+ requestURL);
	xmlHttp.open(type, requestURL, false); // False for synchronous request.
	xmlHttp.send();
	return xmlHttp.responseText;
}

function requestSend(type, query, data) {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + query;
  console.log("3 "+ requestURL);
	xmlHttp.open(type, requestURL, false);
	xmlHttp.send(data);
	return xmlHttp.responseText;
}


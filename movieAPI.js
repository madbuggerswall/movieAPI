
let url = "http://ec2-52-56-129-37.eu-west-2.compute.amazonaws.com:8888/";
// let url = "http://localhost:8888/";

// Movie
function addMovie(data) {
	return post("/movies", data);
}

function deleteMovie(id) {
	deleteDoc("/movies/" + id);
}

function getMovie(id) {
	return get("/movies/" + id);
}

function getAllMovies() {
	return get("/movies");
}

function updateMovie(id, data) {
	return put("/movies/" + id, data);
}

// Director
function addDirector(data) {
	return post("/directors", data)
}

function deleteMovie(id) {
	deleteDoc("/directors/" + id);
}

function getDirector(id) {
	return get("/directors/" + id);
}

function getAllDirectors() {
	return get("/directors");
}

function updateDirector(id, data) {
	return put("/directors/" + id, data);
}

// User
function addUser(data) {
	return post("/users", data);
}

function deleteMovie(id) {
	deleteDoc("/users/" + id);
}

function getUser(id) {
	return get("/users/" + id);
}

function getAllUsers() {
	return get("/users");
}

function updateUser(id, data) {
	return put("/users/" + id, data);
}

function get(query) {
	return request("GET", query);
}

function post(query, data) {
	return request("POST", query, data);
}

function put(query, data) {
	return request("PUT", query, data);
}

function deleteDoc(query) {
	return request("DELETE", query)
}

function request(type, query) {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + query;
	xmlHttp.open(type, requestURL, false); // False for synchronous request.
	xmlHttp.send();
	return xmlHttp.responseText;
}
function request(type, query, data) {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + query;
	xmlHttp.open(type, requestURL, false); 
	xmlHttp.send(data);
	return xmlHttp.responseText;
}

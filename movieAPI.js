
let url = "http://ec2-52-56-129-37.eu-west-2.compute.amazonaws.com:8888/";
// let url = "http://localhost:8888/";

function getMovie(id) {
	return get("/movies/"+id);
}

function getAllMovies() {
	return get("/movies");
}

function getDirector(id){
	return get("/directors/"+id);
}

function getAllDirectors() {
	return get("/directors");
}

function get(request){
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + request;
	xmlHttp.open("GET", requestURL, false); // false for synchronous request
	xmlHttp.send();
	return xmlHttp.responseText;
}
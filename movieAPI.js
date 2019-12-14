
let url = "https://8888-dot-6899741-dot-devshell.appspot.com/";
// let url = "http://localhost:8888/";

function getMovie(id) {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + "api/movie/get/id/" + id;
	xmlHttp.open("GET", requestURL, false); // false for synchronous request
	xmlHttp.send();
	return xmlHttp.responseText;
}

function getAllMovies() {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + "api/movie/get/all";
	xmlHttp.open("GET", requestURL, false); // false for synchronous request
	xmlHttp.send();
	return xmlHttp.responseText;
}

function getDirector(id){
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + "api/director/id/" + id;
	xmlHttp.open("GET", requestURL, false); // false for synchronous request
	xmlHttp.send();
	return xmlHttp.responseText;
}

function getAllDirectors() {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + "api/director/all";
	xmlHttp.open("GET", requestURL, false); // false for synchronous request
	xmlHttp.send();
	return xmlHttp.responseText;
}

let url = "https://8888-dot-6899741-dot-devshell.appspot.com/";

function getMovie(id) {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + "api/movie/id/" + id;
	xmlHttp.open("GET", requestURL, false); // false for synchronous request
	xmlHttp.send();
	return xmlHttp.responseText;
}

function getAllMovies() {
	let xmlHttp = new XMLHttpRequest();
	let requestURL = url + "api/movie/all";
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

console.log(getAllDirectors());

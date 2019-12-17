public class LoginResponse {
	int status;
	User user;
	LoginResponse(int status, User user) {
		this.status = status;
		this.user = user;
	}
}
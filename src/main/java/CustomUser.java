public class CustomUser {
    int status;
    String username;
	String password;
	String role;
	String accessToken;
	String name;
    String surname;
    
    CustomUser(int status, User user){
        this.status = status;
        username=user.getUsername();
        password=user.getPassword();
        role=user.getRole();
        accessToken=user.getAccessToken();
        name=user.getName();
        surname=user.getSurname();
    }
}
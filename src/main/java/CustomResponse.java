import org.eclipse.jetty.http.MetaData.Response;

class CustomResponse extends Response {
    private int status;
    private String body;

    CustomResponse(int status, String body){
        super.setStatus(status);
        this.status = status;
        this.body = body;
        System.out.println(body);
    }
    

}
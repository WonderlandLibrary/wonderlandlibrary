package me.yarukon.oauth.web;

import java.io.OutputStream;
import me.yarukon.oauth.web.Request;

public class Response {
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() {
        try {
            String succMessage = "HTTP/1.1 200 OK\r\nContent-Type:text/html\r\n\r\n<h1>Successfully obtain your token!</h1>";
            this.output.write(succMessage.getBytes());
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

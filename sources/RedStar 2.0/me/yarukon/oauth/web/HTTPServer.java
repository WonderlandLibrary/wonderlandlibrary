package me.yarukon.oauth.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import me.yarukon.oauth.web.Request;
import me.yarukon.oauth.web.Response;

public class HTTPServer {
    private boolean shutdown = false;
    private boolean created = false;
    public String token = "";
    public int port;

    public HTTPServer(int port) {
        this.port = port;
    }

    public void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port, 1, InetAddress.getByName("127.0.0.1"));
            this.created = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        while (!this.shutdown && this.created) {
            try {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                if (request.getUri() == null || !request.getUri().contains("auth-response")) continue;
                this.token = request.getUri().replace("/auth-response?code=", "");
                System.out.println("Token obtained! Shutting down server...");
                this.shutdown = true;
            }
            catch (Exception exception) {}
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

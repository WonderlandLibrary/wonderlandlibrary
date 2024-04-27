package me.yarukon.oauth.web;

import java.io.InputStream;

public class Request {
    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public void parse() {
        int i;
        StringBuilder request = new StringBuilder(2048);
        byte[] buffer = new byte[2048];
        try {
            i = this.input.read(buffer);
        }
        catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; ++j) {
            request.append((char)buffer[j]);
        }
        this.uri = this.parseUri(request.toString());
    }

    public String parseUri(String requestString) {
        int index2;
        int index1 = requestString.indexOf(" ");
        if (index1 != -1 && (index2 = requestString.indexOf(" ", index1 + 1)) > index1) {
            return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return this.uri;
    }
}

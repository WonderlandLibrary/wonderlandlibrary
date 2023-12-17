// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.http;

import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class HttpPipelineSender extends Thread
{
    private HttpPipelineConnection httpPipelineConnection;
    private static final String CRLF = "\r\n";
    private static Charset ASCII;
    
    public HttpPipelineSender(final HttpPipelineConnection httpPipelineConnection) {
        super("HttpPipelineSender");
        this.httpPipelineConnection = null;
        this.httpPipelineConnection = httpPipelineConnection;
    }
    
    @Override
    public void run() {
        HttpPipelineRequest httppipelinerequest = null;
        try {
            this.connect();
            while (!Thread.interrupted()) {
                httppipelinerequest = this.httpPipelineConnection.getNextRequestSend();
                final HttpRequest httprequest = httppipelinerequest.getHttpRequest();
                final OutputStream outputstream = this.httpPipelineConnection.getOutputStream();
                this.writeRequest(httprequest, outputstream);
                this.httpPipelineConnection.onRequestSent(httppipelinerequest);
            }
        }
        catch (InterruptedException var4) {}
        catch (Exception exception) {
            this.httpPipelineConnection.onExceptionSend(httppipelinerequest, exception);
        }
    }
    
    private void connect() throws IOException {
        final String s = this.httpPipelineConnection.getHost();
        final int i = this.httpPipelineConnection.getPort();
        final Proxy proxy = this.httpPipelineConnection.getProxy();
        final Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress(s, i), 5000);
        this.httpPipelineConnection.setSocket(socket);
    }
    
    private void writeRequest(final HttpRequest req, final OutputStream out) throws IOException {
        this.write(out, req.getMethod() + " " + req.getFile() + " " + req.getHttp() + "\r\n");
        final Map<String, String> map = req.getHeaders();
        for (final String s : map.keySet()) {
            final String s2 = req.getHeaders().get(s);
            this.write(out, s + ": " + s2 + "\r\n");
        }
        this.write(out, "\r\n");
    }
    
    private void write(final OutputStream out, final String str) throws IOException {
        final byte[] abyte = str.getBytes(HttpPipelineSender.ASCII);
        out.write(abyte);
    }
    
    static {
        HttpPipelineSender.ASCII = Charset.forName("ASCII");
    }
}

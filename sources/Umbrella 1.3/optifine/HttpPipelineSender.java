/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import optifine.HttpPipelineConnection;
import optifine.HttpPipelineRequest;
import optifine.HttpRequest;

public class HttpPipelineSender
extends Thread {
    private HttpPipelineConnection httpPipelineConnection = null;
    private static final String CRLF = "\r\n";
    private static Charset ASCII = Charset.forName("ASCII");

    public HttpPipelineSender(HttpPipelineConnection httpPipelineConnection) {
        super("HttpPipelineSender");
        this.httpPipelineConnection = httpPipelineConnection;
    }

    @Override
    public void run() {
        HttpPipelineRequest hpr = null;
        try {
            this.connect();
            while (!Thread.interrupted()) {
                hpr = this.httpPipelineConnection.getNextRequestSend();
                HttpRequest e = hpr.getHttpRequest();
                OutputStream out = this.httpPipelineConnection.getOutputStream();
                this.writeRequest(e, out);
                this.httpPipelineConnection.onRequestSent(hpr);
            }
        }
        catch (InterruptedException var4) {
            return;
        }
        catch (Exception var5) {
            this.httpPipelineConnection.onExceptionSend(hpr, var5);
        }
    }

    private void connect() throws IOException {
        String host = this.httpPipelineConnection.getHost();
        int port = this.httpPipelineConnection.getPort();
        Proxy proxy = this.httpPipelineConnection.getProxy();
        Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress(host, port), 5000);
        this.httpPipelineConnection.setSocket(socket);
    }

    private void writeRequest(HttpRequest req, OutputStream out) throws IOException {
        this.write(out, String.valueOf(req.getMethod()) + " " + req.getFile() + " " + req.getHttp() + CRLF);
        Map<String, String> headers = req.getHeaders();
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            String val = req.getHeaders().get(key);
            this.write(out, String.valueOf(key) + ": " + val + CRLF);
        }
        this.write(out, CRLF);
    }

    private void write(OutputStream out, String str) throws IOException {
        byte[] bytes = str.getBytes(ASCII);
        out.write(bytes);
    }
}


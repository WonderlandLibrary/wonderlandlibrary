// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.io.EOFException;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class HttpPipelineReceiver extends Thread
{
    private HttpPipelineConnection httpPipelineConnection;
    private static final Charset ASCII;
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final char CR = '\r';
    private static final char LF = '\n';
    
    static {
        ASCII = Charset.forName("ASCII");
    }
    
    public HttpPipelineReceiver(final HttpPipelineConnection httpPipelineConnection) {
        super("HttpPipelineReceiver");
        this.httpPipelineConnection = null;
        this.httpPipelineConnection = httpPipelineConnection;
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            HttpPipelineRequest currentRequest = null;
            try {
                currentRequest = this.httpPipelineConnection.getNextRequestReceive();
                final InputStream e = this.httpPipelineConnection.getInputStream();
                final HttpResponse resp = this.readResponse(e);
                this.httpPipelineConnection.onResponseReceived(currentRequest, resp);
            }
            catch (InterruptedException var6) {}
            catch (Exception var5) {
                this.httpPipelineConnection.onExceptionReceive(currentRequest, var5);
            }
        }
    }
    
    private HttpResponse readResponse(final InputStream in) throws IOException {
        final String statusLine = this.readLine(in);
        final String[] parts = Config.tokenize(statusLine, " ");
        if (parts.length < 3) {
            throw new IOException("Invalid status line: " + statusLine);
        }
        final String http = parts[0];
        final int status = Config.parseInt(parts[1], 0);
        final String message = parts[2];
        final LinkedHashMap headers = new LinkedHashMap();
        while (true) {
            final String body = this.readLine(in);
            if (body.length() <= 0) {
                break;
            }
            final int lenStr = body.indexOf(":");
            if (lenStr <= 0) {
                continue;
            }
            final String enc = body.substring(0, lenStr).trim();
            final String val = body.substring(lenStr + 1).trim();
            headers.put(enc, val);
        }
        byte[] body2 = null;
        final String lenStr2 = headers.get("Content-Length");
        if (lenStr2 != null) {
            final int enc2 = Config.parseInt(lenStr2, -1);
            if (enc2 > 0) {
                body2 = new byte[enc2];
                this.readFull(body2, in);
            }
        }
        else {
            final String enc = headers.get("Transfer-Encoding");
            if (Config.equals(enc, "chunked")) {
                body2 = this.readContentChunked(in);
            }
        }
        return new HttpResponse(status, statusLine, headers, body2);
    }
    
    private byte[] readContentChunked(final InputStream in) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        do {
            final String line = this.readLine(in);
            final String[] parts = Config.tokenize(line, "; ");
            len = Integer.parseInt(parts[0], 16);
            final byte[] buf = new byte[len];
            this.readFull(buf, in);
            baos.write(buf);
            this.readLine(in);
        } while (len != 0);
        return baos.toByteArray();
    }
    
    private void readFull(final byte[] buf, final InputStream in) throws IOException {
        int len;
        for (int pos = 0; pos < buf.length; pos += len) {
            len = in.read(buf, pos, buf.length - pos);
            if (len < 0) {
                throw new EOFException();
            }
        }
    }
    
    private String readLine(final InputStream in) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int prev = -1;
        boolean hasCRLF = false;
        while (true) {
            final int bytes = in.read();
            if (bytes < 0) {
                break;
            }
            baos.write(bytes);
            if (prev == 13 && bytes == 10) {
                hasCRLF = true;
                break;
            }
            prev = bytes;
        }
        final byte[] bytes2 = baos.toByteArray();
        String str = new String(bytes2, HttpPipelineReceiver.ASCII);
        if (hasCRLF) {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
}

package optifine;

import java.util.Map;

public class HttpResponse
{
    private final int status;
    private final String statusLine;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse(int status, String statusLine, Map<String, String> headers, byte[] body)
    {
        this.status = status;
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public int getStatus()
    {
        return this.status;
    }

    public String getStatusLine()
    {
        return this.statusLine;
    }

    public Map<String, String> getHeaders()
    {
        return this.headers;
    }

    public String getHeader(String p_getHeader_1_)
    {
        return this.headers.get(p_getHeader_1_);
    }

    public byte[] getBody()
    {
        return this.body;
    }
}

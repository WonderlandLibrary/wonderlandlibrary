package net.optifine.http;

import java.util.Map;
import net.optifine.http.HttpUtils;
import net.optifine.http.IFileUploadListener;

public class FileUploadThread
extends Thread {
    private String urlString;
    private Map headers;
    private byte[] content;
    private IFileUploadListener listener;

    public FileUploadThread(String urlString, Map headers, byte[] content, IFileUploadListener listener) {
        this.urlString = urlString;
        this.headers = headers;
        this.content = content;
        this.listener = listener;
    }

    public void run() {
        try {
            HttpUtils.post((String)this.urlString, (Map)this.headers, (byte[])this.content);
            this.listener.fileUploadFinished(this.urlString, this.content, (Throwable)null);
        }
        catch (Exception exception) {
            this.listener.fileUploadFinished(this.urlString, this.content, (Throwable)exception);
        }
    }

    public String getUrlString() {
        return this.urlString;
    }

    public byte[] getContent() {
        return this.content;
    }

    public IFileUploadListener getListener() {
        return this.listener;
    }
}

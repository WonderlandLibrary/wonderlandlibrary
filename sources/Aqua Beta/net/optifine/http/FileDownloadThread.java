package net.optifine.http;

import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.optifine.http.HttpPipeline;
import net.optifine.http.IFileDownloadListener;

public class FileDownloadThread
extends Thread {
    private String urlString = null;
    private IFileDownloadListener listener = null;

    public FileDownloadThread(String urlString, IFileDownloadListener listener) {
        this.urlString = urlString;
        this.listener = listener;
    }

    public void run() {
        try {
            byte[] abyte = HttpPipeline.get((String)this.urlString, (Proxy)Minecraft.getMinecraft().getProxy());
            this.listener.fileDownloadFinished(this.urlString, abyte, (Throwable)null);
        }
        catch (Exception exception) {
            this.listener.fileDownloadFinished(this.urlString, (byte[])null, (Throwable)exception);
        }
    }

    public String getUrlString() {
        return this.urlString;
    }

    public IFileDownloadListener getListener() {
        return this.listener;
    }
}

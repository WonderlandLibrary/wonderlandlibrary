// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.http;

import net.minecraft.client.Minecraft;

public class FileDownloadThread extends Thread
{
    private String urlString;
    private IFileDownloadListener listener;
    
    public FileDownloadThread(final String urlString, final IFileDownloadListener listener) {
        this.urlString = null;
        this.listener = null;
        this.urlString = urlString;
        this.listener = listener;
    }
    
    @Override
    public void run() {
        try {
            final byte[] abyte = HttpPipeline.get(this.urlString, Minecraft.getMinecraft().getProxy());
            this.listener.fileDownloadFinished(this.urlString, abyte, null);
        }
        catch (Exception exception) {
            this.listener.fileDownloadFinished(this.urlString, null, exception);
        }
    }
    
    public String getUrlString() {
        return this.urlString;
    }
    
    public IFileDownloadListener getListener() {
        return this.listener;
    }
}

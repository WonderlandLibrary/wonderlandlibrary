// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

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
            final byte[] e = HttpUtils.get(this.urlString);
            this.listener.fileDownloadFinished(this.urlString, e, null);
        }
        catch (Exception var2) {
            this.listener.fileDownloadFinished(this.urlString, null, var2);
        }
    }
    
    public String getUrlString() {
        return this.urlString;
    }
    
    public IFileDownloadListener getListener() {
        return this.listener;
    }
}

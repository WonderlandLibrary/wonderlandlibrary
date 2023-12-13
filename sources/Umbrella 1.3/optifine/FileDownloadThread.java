/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import net.minecraft.client.Minecraft;
import optifine.HttpPipeline;
import optifine.IFileDownloadListener;

public class FileDownloadThread
extends Thread {
    private String urlString = null;
    private IFileDownloadListener listener = null;

    public FileDownloadThread(String urlString, IFileDownloadListener listener) {
        this.urlString = urlString;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            byte[] e = HttpPipeline.get(this.urlString, Minecraft.getMinecraft().getProxy());
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


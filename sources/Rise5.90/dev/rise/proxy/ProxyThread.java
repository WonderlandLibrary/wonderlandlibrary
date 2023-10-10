package dev.rise.proxy;

import net.minecraft.client.Minecraft;

/**
 * @author Tecnio (my ass)
 */
public final class ProxyThread extends Thread {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final String ip;
    private final String port;
    private final String type;
    private String status;

    public ProxyThread(final String ip, final String port, final String type) {
        super("Proxy Thread");
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.status = "Thread Running...";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public void run() {
        status = "Setting Proxy...";

        if (this.ip != null && this.port != null) {
            this.status = "A " + type + " proxy was set! (" + ip + ":" + port + ")";
        }
    }
}

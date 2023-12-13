/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.web;

import java.io.IOException;
import java.net.Socket;
import javassist.tools.web.Webserver;

class ServiceThread
extends Thread {
    Webserver web;
    Socket sock;

    public ServiceThread(Webserver w2, Socket s2) {
        this.web = w2;
        this.sock = s2;
    }

    @Override
    public void run() {
        try {
            this.web.process(this.sock);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}


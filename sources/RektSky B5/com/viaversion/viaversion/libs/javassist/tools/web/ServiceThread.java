/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.web;

import com.viaversion.viaversion.libs.javassist.tools.web.Webserver;
import java.io.IOException;
import java.net.Socket;

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


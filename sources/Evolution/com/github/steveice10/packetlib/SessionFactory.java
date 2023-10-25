/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.ConnectionListener;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;

public interface SessionFactory {
    public Session createClientSession(Client var1);

    public ConnectionListener createServerListener(Server var1);
}


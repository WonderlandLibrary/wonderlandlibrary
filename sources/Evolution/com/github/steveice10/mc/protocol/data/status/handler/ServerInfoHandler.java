/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.status.handler;

import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.packetlib.Session;

public interface ServerInfoHandler {
    public void handle(Session var1, ServerStatusInfo var2);
}


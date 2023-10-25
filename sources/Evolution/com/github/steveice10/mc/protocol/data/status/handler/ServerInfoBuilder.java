/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.status.handler;

import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.packetlib.Session;

public interface ServerInfoBuilder {
    public ServerStatusInfo buildInfo(Session var1);
}


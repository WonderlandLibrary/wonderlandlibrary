/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;

public interface INetHandlerStatusServer
extends INetHandler {
    public void processPing(CPacketPing var1);

    public void processServerQuery(CPacketServerQuery var1);
}


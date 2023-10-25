/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.login;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;

public interface INetHandlerLoginServer
extends INetHandler {
    public void processLoginStart(CPacketLoginStart var1);

    public void processEncryptionResponse(CPacketEncryptionResponse var1);
}


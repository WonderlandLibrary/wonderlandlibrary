/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.login;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;

public interface INetHandlerLoginClient
extends INetHandler {
    public void handleEncryptionRequest(SPacketEncryptionRequest var1);

    public void handleLoginSuccess(SPacketLoginSuccess var1);

    public void handleDisconnect(SPacketDisconnect var1);

    public void handleEnableCompression(SPacketEnableCompression var1);
}


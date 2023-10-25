/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class S01PacketEncryptionRequest
implements Packet<INetHandlerLoginClient> {
    private String hashedServerId;
    private PublicKey publicKey;
    private byte[] verifyToken;

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.hashedServerId);
        packetBuffer.writeByteArray(this.publicKey.getEncoded());
        packetBuffer.writeByteArray(this.verifyToken);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public S01PacketEncryptionRequest() {
    }

    public String getServerId() {
        return this.hashedServerId;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.hashedServerId = packetBuffer.readStringFromBuffer(20);
        this.publicKey = CryptManager.decodePublicKey(packetBuffer.readByteArray());
        this.verifyToken = packetBuffer.readByteArray();
    }

    public S01PacketEncryptionRequest(String string, PublicKey publicKey, byte[] byArray) {
        this.hashedServerId = string;
        this.publicKey = publicKey;
        this.verifyToken = byArray;
    }

    @Override
    public void processPacket(INetHandlerLoginClient iNetHandlerLoginClient) {
        iNetHandlerLoginClient.handleEncryptionRequest(this);
    }
}


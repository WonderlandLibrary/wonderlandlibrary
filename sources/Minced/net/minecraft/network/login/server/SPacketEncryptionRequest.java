// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.util.CryptManager;
import net.minecraft.network.PacketBuffer;
import java.security.PublicKey;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.Packet;

public class SPacketEncryptionRequest implements Packet<INetHandlerLoginClient>
{
    private String hashedServerId;
    private PublicKey publicKey;
    private byte[] verifyToken;
    
    public SPacketEncryptionRequest() {
    }
    
    public SPacketEncryptionRequest(final String serverIdIn, final PublicKey publicKeyIn, final byte[] verifyTokenIn) {
        this.hashedServerId = serverIdIn;
        this.publicKey = publicKeyIn;
        this.verifyToken = verifyTokenIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.hashedServerId = buf.readString(20);
        this.publicKey = CryptManager.decodePublicKey(buf.readByteArray());
        this.verifyToken = buf.readByteArray();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.hashedServerId);
        buf.writeByteArray(this.publicKey.getEncoded());
        buf.writeByteArray(this.verifyToken);
    }
    
    @Override
    public void processPacket(final INetHandlerLoginClient handler) {
        handler.handleEncryptionRequest(this);
    }
    
    public String getServerId() {
        return this.hashedServerId;
    }
    
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    
    public byte[] getVerifyToken() {
        return this.verifyToken;
    }
}

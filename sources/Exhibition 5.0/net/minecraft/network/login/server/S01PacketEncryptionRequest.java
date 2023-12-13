// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.INetHandlerLoginClient;
import java.io.IOException;
import net.minecraft.util.CryptManager;
import net.minecraft.network.PacketBuffer;
import java.security.PublicKey;
import net.minecraft.network.Packet;

public class S01PacketEncryptionRequest implements Packet
{
    private String hashedServerId;
    private PublicKey publicKey;
    private byte[] field_149611_c;
    private static final String __OBFID = "CL_00001376";
    
    public S01PacketEncryptionRequest() {
    }
    
    public S01PacketEncryptionRequest(final String serverId, final PublicKey key, final byte[] p_i45268_3_) {
        this.hashedServerId = serverId;
        this.publicKey = key;
        this.field_149611_c = p_i45268_3_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.hashedServerId = data.readStringFromBuffer(20);
        this.publicKey = CryptManager.decodePublicKey(data.readByteArray());
        this.field_149611_c = data.readByteArray();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.hashedServerId);
        data.writeByteArray(this.publicKey.getEncoded());
        data.writeByteArray(this.field_149611_c);
    }
    
    public void processPacket(final INetHandlerLoginClient handler) {
        handler.handleEncryptionRequest(this);
    }
    
    public String func_149609_c() {
        return this.hashedServerId;
    }
    
    public PublicKey func_149608_d() {
        return this.publicKey;
    }
    
    public byte[] func_149607_e() {
        return this.field_149611_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerLoginClient)handler);
    }
}

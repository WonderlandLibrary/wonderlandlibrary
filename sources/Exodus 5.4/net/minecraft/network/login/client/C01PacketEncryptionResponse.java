/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.login.client;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

public class C01PacketEncryptionResponse
implements Packet<INetHandlerLoginServer> {
    private byte[] secretKeyEncrypted = new byte[0];
    private byte[] verifyTokenEncrypted = new byte[0];

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return CryptManager.decryptSharedKey(privateKey, this.secretKeyEncrypted);
    }

    @Override
    public void processPacket(INetHandlerLoginServer iNetHandlerLoginServer) {
        iNetHandlerLoginServer.processEncryptionResponse(this);
    }

    public byte[] getVerifyToken(PrivateKey privateKey) {
        return privateKey == null ? this.verifyTokenEncrypted : CryptManager.decryptData(privateKey, this.verifyTokenEncrypted);
    }

    public C01PacketEncryptionResponse(SecretKey secretKey, PublicKey publicKey, byte[] byArray) {
        this.secretKeyEncrypted = CryptManager.encryptData(publicKey, secretKey.getEncoded());
        this.verifyTokenEncrypted = CryptManager.encryptData(publicKey, byArray);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.secretKeyEncrypted = packetBuffer.readByteArray();
        this.verifyTokenEncrypted = packetBuffer.readByteArray();
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByteArray(this.secretKeyEncrypted);
        packetBuffer.writeByteArray(this.verifyTokenEncrypted);
    }

    public C01PacketEncryptionResponse() {
    }
}


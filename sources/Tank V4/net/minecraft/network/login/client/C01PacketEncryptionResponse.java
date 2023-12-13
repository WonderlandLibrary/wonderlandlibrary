package net.minecraft.network.login.client;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.util.CryptManager;

public class C01PacketEncryptionResponse implements Packet {
   private byte[] verifyTokenEncrypted = new byte[0];
   private byte[] secretKeyEncrypted = new byte[0];

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByteArray(this.secretKeyEncrypted);
      var1.writeByteArray(this.verifyTokenEncrypted);
   }

   public C01PacketEncryptionResponse(SecretKey var1, PublicKey var2, byte[] var3) {
      this.secretKeyEncrypted = CryptManager.encryptData(var2, var1.getEncoded());
      this.verifyTokenEncrypted = CryptManager.encryptData(var2, var3);
   }

   public void processPacket(INetHandlerLoginServer var1) {
      var1.processEncryptionResponse(this);
   }

   public C01PacketEncryptionResponse() {
   }

   public byte[] getVerifyToken(PrivateKey var1) {
      return var1 == null ? this.verifyTokenEncrypted : CryptManager.decryptData(var1, this.verifyTokenEncrypted);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.secretKeyEncrypted = var1.readByteArray();
      this.verifyTokenEncrypted = var1.readByteArray();
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerLoginServer)var1);
   }

   public SecretKey getSecretKey(PrivateKey var1) {
      return CryptManager.decryptSharedKey(var1, this.secretKeyEncrypted);
   }
}

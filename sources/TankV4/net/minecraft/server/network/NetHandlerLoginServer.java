package net.minecraft.server.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable {
   private static final Random RANDOM = new Random();
   private static final Logger logger = LogManager.getLogger();
   private final MinecraftServer server;
   private EntityPlayerMP field_181025_l;
   private GameProfile loginGameProfile;
   private NetHandlerLoginServer.LoginState currentLoginState;
   private final byte[] verifyToken = new byte[4];
   private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
   private int connectionTimer;
   public final NetworkManager networkManager;
   private SecretKey secretKey;
   private String serverId;

   static String access$2(NetHandlerLoginServer var0) {
      return var0.serverId;
   }

   public void processEncryptionResponse(C01PacketEncryptionResponse var1) {
      Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet");
      PrivateKey var2 = this.server.getKeyPair().getPrivate();
      if (!Arrays.equals(this.verifyToken, var1.getVerifyToken(var2))) {
         throw new IllegalStateException("Invalid nonce!");
      } else {
         this.secretKey = var1.getSecretKey(var2);
         this.currentLoginState = NetHandlerLoginServer.LoginState.AUTHENTICATING;
         this.networkManager.enableEncryption(this.secretKey);
         (new Thread(this, "User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
            final NetHandlerLoginServer this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               GameProfile var1 = NetHandlerLoginServer.access$1(this.this$0);

               try {
                  String var2 = (new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.access$2(this.this$0), NetHandlerLoginServer.access$0(this.this$0).getKeyPair().getPublic(), NetHandlerLoginServer.access$3(this.this$0)))).toString(16);
                  NetHandlerLoginServer.access$4(this.this$0, NetHandlerLoginServer.access$0(this.this$0).getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID)null, var1.getName()), var2));
                  if (NetHandlerLoginServer.access$1(this.this$0) != null) {
                     NetHandlerLoginServer.access$5().info("UUID of player " + NetHandlerLoginServer.access$1(this.this$0).getName() + " is " + NetHandlerLoginServer.access$1(this.this$0).getId());
                     NetHandlerLoginServer.access$6(this.this$0, NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
                  } else if (NetHandlerLoginServer.access$0(this.this$0).isSinglePlayer()) {
                     NetHandlerLoginServer.access$5().warn("Failed to verify username but will let them in anyway!");
                     NetHandlerLoginServer.access$4(this.this$0, this.this$0.getOfflineProfile(var1));
                     NetHandlerLoginServer.access$6(this.this$0, NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
                  } else {
                     this.this$0.closeConnection("Failed to verify username!");
                     NetHandlerLoginServer.access$5().error("Username '" + NetHandlerLoginServer.access$1(this.this$0).getName() + "' tried to join with an invalid session");
                  }
               } catch (AuthenticationUnavailableException var4) {
                  if (NetHandlerLoginServer.access$0(this.this$0).isSinglePlayer()) {
                     NetHandlerLoginServer.access$5().warn("Authentication servers are down but will let them in anyway!");
                     NetHandlerLoginServer.access$4(this.this$0, this.this$0.getOfflineProfile(var1));
                     NetHandlerLoginServer.access$6(this.this$0, NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
                  } else {
                     this.this$0.closeConnection("Authentication servers are down. Please try again later, sorry!");
                     NetHandlerLoginServer.access$5().error("Couldn't verify username because servers are unavailable");
                  }
               }

            }
         }).start();
      }
   }

   public void onDisconnect(IChatComponent var1) {
      logger.info(this.getConnectionInfo() + " lost connection: " + var1.getUnformattedText());
   }

   public void closeConnection(String var1) {
      try {
         logger.info("Disconnecting " + this.getConnectionInfo() + ": " + var1);
         ChatComponentText var2 = new ChatComponentText(var1);
         this.networkManager.sendPacket(new S00PacketDisconnect(var2));
         this.networkManager.closeChannel(var2);
      } catch (Exception var3) {
         logger.error((String)"Error whilst disconnecting player", (Throwable)var3);
      }

   }

   static Logger access$5() {
      return logger;
   }

   public void update() {
      if (this.currentLoginState == NetHandlerLoginServer.LoginState.READY_TO_ACCEPT) {
         this.tryAcceptPlayer();
      } else if (this.currentLoginState == NetHandlerLoginServer.LoginState.DELAY_ACCEPT) {
         EntityPlayerMP var1 = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
         if (var1 == null) {
            this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
            this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.field_181025_l);
            this.field_181025_l = null;
         }
      }

      if (this.connectionTimer++ == 600) {
         this.closeConnection("Took too long to log in");
      }

   }

   protected GameProfile getOfflineProfile(GameProfile var1) {
      UUID var2 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + var1.getName()).getBytes(Charsets.UTF_8));
      return new GameProfile(var2, var1.getName());
   }

   static void access$6(NetHandlerLoginServer var0, NetHandlerLoginServer.LoginState var1) {
      var0.currentLoginState = var1;
   }

   public String getConnectionInfo() {
      return this.loginGameProfile != null ? this.loginGameProfile.toString() + " (" + this.networkManager.getRemoteAddress().toString() + ")" : String.valueOf(this.networkManager.getRemoteAddress());
   }

   static void access$4(NetHandlerLoginServer var0, GameProfile var1) {
      var0.loginGameProfile = var1;
   }

   static SecretKey access$3(NetHandlerLoginServer var0) {
      return var0.secretKey;
   }

   public void processLoginStart(C00PacketLoginStart var1) {
      Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet");
      this.loginGameProfile = var1.getProfile();
      if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
         this.currentLoginState = NetHandlerLoginServer.LoginState.KEY;
         this.networkManager.sendPacket(new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.verifyToken));
      } else {
         this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
      }

   }

   static GameProfile access$1(NetHandlerLoginServer var0) {
      return var0.loginGameProfile;
   }

   static MinecraftServer access$0(NetHandlerLoginServer var0) {
      return var0.server;
   }

   public void tryAcceptPlayer() {
      if (!this.loginGameProfile.isComplete()) {
         this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
      }

      String var1 = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
      if (var1 != null) {
         this.closeConnection(var1);
      } else {
         this.currentLoginState = NetHandlerLoginServer.LoginState.ACCEPTED;
         if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel()) {
            this.networkManager.sendPacket(new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), new ChannelFutureListener(this) {
               final NetHandlerLoginServer this$0;

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  this.this$0.networkManager.setCompressionTreshold(NetHandlerLoginServer.access$0(this.this$0).getNetworkCompressionTreshold());
               }

               {
                  this.this$0 = var1;
               }
            });
         }

         this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile));
         EntityPlayerMP var2 = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
         if (var2 != null) {
            this.currentLoginState = NetHandlerLoginServer.LoginState.DELAY_ACCEPT;
            this.field_181025_l = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
         } else {
            this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
         }
      }

   }

   public NetHandlerLoginServer(MinecraftServer var1, NetworkManager var2) {
      this.currentLoginState = NetHandlerLoginServer.LoginState.HELLO;
      this.serverId = "";
      this.server = var1;
      this.networkManager = var2;
      RANDOM.nextBytes(this.verifyToken);
   }

   static enum LoginState {
      HELLO;

      private static final NetHandlerLoginServer.LoginState[] ENUM$VALUES = new NetHandlerLoginServer.LoginState[]{HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, DELAY_ACCEPT, ACCEPTED};
      DELAY_ACCEPT,
      AUTHENTICATING,
      READY_TO_ACCEPT,
      ACCEPTED,
      KEY;
   }
}

package net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.UUID;
import javax.crypto.SecretKey;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.CryptManager;

/*
 * Exception performing whole class analysis ignored.
 */
class NetHandlerLoginServer.2
extends Thread {
    NetHandlerLoginServer.2(String arg0) {
        super(arg0);
    }

    public void run() {
        GameProfile gameprofile = NetHandlerLoginServer.access$100((NetHandlerLoginServer)NetHandlerLoginServer.this);
        try {
            String s = new BigInteger(CryptManager.getServerIdHash((String)NetHandlerLoginServer.access$200((NetHandlerLoginServer)NetHandlerLoginServer.this), (PublicKey)NetHandlerLoginServer.access$000((NetHandlerLoginServer)NetHandlerLoginServer.this).getKeyPair().getPublic(), (SecretKey)NetHandlerLoginServer.access$300((NetHandlerLoginServer)NetHandlerLoginServer.this))).toString(16);
            NetHandlerLoginServer.access$102((NetHandlerLoginServer)NetHandlerLoginServer.this, (GameProfile)NetHandlerLoginServer.access$000((NetHandlerLoginServer)NetHandlerLoginServer.this).getMinecraftSessionService().hasJoinedServer(new GameProfile((UUID)null, gameprofile.getName()), s));
            if (NetHandlerLoginServer.access$100((NetHandlerLoginServer)NetHandlerLoginServer.this) != null) {
                NetHandlerLoginServer.access$400().info("UUID of player " + NetHandlerLoginServer.access$100((NetHandlerLoginServer)NetHandlerLoginServer.this).getName() + " is " + NetHandlerLoginServer.access$100((NetHandlerLoginServer)NetHandlerLoginServer.this).getId());
                NetHandlerLoginServer.access$502((NetHandlerLoginServer)NetHandlerLoginServer.this, (NetHandlerLoginServer.LoginState)NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
            } else if (NetHandlerLoginServer.access$000((NetHandlerLoginServer)NetHandlerLoginServer.this).isSinglePlayer()) {
                NetHandlerLoginServer.access$400().warn("Failed to verify username but will let them in anyway!");
                NetHandlerLoginServer.access$102((NetHandlerLoginServer)NetHandlerLoginServer.this, (GameProfile)NetHandlerLoginServer.this.getOfflineProfile(gameprofile));
                NetHandlerLoginServer.access$502((NetHandlerLoginServer)NetHandlerLoginServer.this, (NetHandlerLoginServer.LoginState)NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
            } else {
                NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
                NetHandlerLoginServer.access$400().error("Username '" + NetHandlerLoginServer.access$100((NetHandlerLoginServer)NetHandlerLoginServer.this).getName() + "' tried to join with an invalid session");
            }
        }
        catch (AuthenticationUnavailableException var3) {
            if (NetHandlerLoginServer.access$000((NetHandlerLoginServer)NetHandlerLoginServer.this).isSinglePlayer()) {
                NetHandlerLoginServer.access$400().warn("Authentication servers are down but will let them in anyway!");
                NetHandlerLoginServer.access$102((NetHandlerLoginServer)NetHandlerLoginServer.this, (GameProfile)NetHandlerLoginServer.this.getOfflineProfile(gameprofile));
                NetHandlerLoginServer.access$502((NetHandlerLoginServer)NetHandlerLoginServer.this, (NetHandlerLoginServer.LoginState)NetHandlerLoginServer.LoginState.READY_TO_ACCEPT);
            }
            NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
            NetHandlerLoginServer.access$400().error("Couldn't verify username because servers are unavailable");
        }
    }
}

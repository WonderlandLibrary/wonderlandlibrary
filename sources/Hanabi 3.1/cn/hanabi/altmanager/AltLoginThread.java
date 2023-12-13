package cn.hanabi.altmanager;

import cn.hanabi.Hanabi;
import cn.hanabi.injection.interfaces.IMinecraft;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import me.ratsiel.auth.model.mojang.MinecraftAuthenticator;
import me.ratsiel.auth.model.mojang.MinecraftToken;
import me.ratsiel.auth.model.mojang.profile.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThread extends Thread {
   private final Alt alt;
   private String status;

   public AltLoginThread(Alt alt) {
      super("Alt Login Thread");
      Minecraft mc = Minecraft.getMinecraft();
      this.alt = alt;
      this.status = EnumChatFormatting.GRAY + "Waiting...";
   }

   private Session createSession(String username, String password, boolean mslogin) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      MinecraftAuthenticator minecraftAuthenticator = new MinecraftAuthenticator();
      auth.setUsername(username);
      auth.setPassword(password);
      if (mslogin) {
         try {
            MinecraftToken minecraftToken = minecraftAuthenticator.loginWithXbox(username, password);
            MinecraftProfile minecraftProfile = minecraftAuthenticator.checkOwnership(minecraftToken);
            return new Session(minecraftProfile.getUsername(), minecraftProfile.getUuid().toString(), minecraftToken.getAccessToken(), "mojang");
         } catch (Exception var9) {
            var9.printStackTrace();
            return null;
         }
      } else {
         try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
         } catch (AuthenticationException var10) {
            var10.printStackTrace();
            return null;
         }
      }
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void run() {
      Session auth;
      if (this.alt.getPassword().equals("")) {
         auth = new Session(this.alt.getUsername(), "", "", "mojang");
         ((IMinecraft)Minecraft.getMinecraft()).setSession(auth);
         this.status = EnumChatFormatting.GREEN + "Logged in. (" + this.alt.getUsername() + " - offline name)";
      } else {
         this.status = EnumChatFormatting.AQUA + "Logging in...";
         auth = this.createSession(this.alt.getUsername(), this.alt.getPassword(), Hanabi.INSTANCE.mslogin);
         if (auth == null) {
            this.status = EnumChatFormatting.RED + "Login failed!";
         } else {
            AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
            this.alt.setMask(auth.getUsername());
            ((IMinecraft)Minecraft.getMinecraft()).setSession(auth);

            try {
               Hanabi.INSTANCE.altFileMgr.getFile(Alts.class).saveFile();
            } catch (Exception var3) {
            }
         }

      }
   }
}

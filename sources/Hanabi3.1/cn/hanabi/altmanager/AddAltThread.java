package cn.hanabi.altmanager;

import cn.hanabi.Hanabi;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import me.ratsiel.auth.model.mojang.MinecraftAuthenticator;
import me.ratsiel.auth.model.mojang.MinecraftToken;
import me.ratsiel.auth.model.mojang.profile.MinecraftProfile;
import net.minecraft.util.EnumChatFormatting;

public class AddAltThread extends Thread {
   private final String password;
   private final String username;

   public AddAltThread(String username, String password) {
      this.username = username;
      this.password = password;
      GuiAltManager.setStatus(EnumChatFormatting.GRAY + "Idle...");
   }

   private void checkAndAddAlt(String username, String password, boolean mslogin) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      MinecraftAuthenticator minecraftAuthenticator = new MinecraftAuthenticator();
      auth.setUsername(username);
      auth.setPassword(password);

      try {
         String name;
         if (mslogin) {
            MinecraftToken minecraftToken = minecraftAuthenticator.loginWithXbox(username, password);
            MinecraftProfile minecraftProfile = minecraftAuthenticator.checkOwnership(minecraftToken);
            name = minecraftProfile.getUsername();
         } else {
            auth.logIn();
            name = auth.getSelectedProfile().getName();
         }

         AltManager.registry.add(new Alt(username, password, name));

         try {
            Hanabi.INSTANCE.altFileMgr.getFile(Alts.class).saveFile();
         } catch (Exception var10) {
         }

         GuiAltManager.setStatus("Alt added. (" + username + ")");
      } catch (AuthenticationException var11) {
         GuiAltManager.setStatus(EnumChatFormatting.RED + "Alt failed!");
         var11.printStackTrace();
      }

   }

   public void run() {
      if (this.password.equals("")) {
         AltManager.registry.add(new Alt(this.username, ""));
         GuiAltManager.setStatus(EnumChatFormatting.GREEN + "Alt added. (" + this.username + " - offline name)");
      } else {
         GuiAltManager.setStatus(EnumChatFormatting.AQUA + "Trying alt...");
         this.checkAndAddAlt(this.username, this.password, Hanabi.INSTANCE.mslogin);
      }
   }
}

package team.massacre.api.ui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.SSLController;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public class AltLoginThread extends Thread {
   public String password;
   private String status;
   public String username;
   private Minecraft mc = Minecraft.getMinecraft();
   private SSLController ssl = new SSLController();
   private TheAlteningAuthentication mojang = TheAlteningAuthentication.mojang();

   public AltLoginThread(String username, String password) {
      super("Alt Login Thread");
      this.username = username;
      this.password = password;
      this.status = EnumChatFormatting.GRAY + "";
   }

   private Session createSession(String username, String password) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);

      try {
         if (username.endsWith("@alt.com")) {
            this.ssl.disableCertificateValidation();
            this.mojang.updateService(AlteningServiceType.THEALTENING);
         } else if (this.mojang.getService() == AlteningServiceType.THEALTENING) {
            this.ssl.enableCertificateValidation();
            this.mojang.updateService(AlteningServiceType.MOJANG);
         }

         auth.logIn();
         return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
      } catch (AuthenticationException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public String getStatus() {
      return this.status;
   }

   public void run() {
      if (this.password.equals("")) {
         this.mc.session = new Session(this.username, "", "", "mojang");
         this.status = EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline name)";
      } else {
         Session auth = this.createSession(this.username, this.password);
         if (auth == null) {
            this.status = EnumChatFormatting.RED + "Login failed!";
         } else {
            new AltManager();
            AltManager.lastAlt = new Alt(this.username, this.password);
            this.status = EnumChatFormatting.GREEN + "Login success!";
            this.mc.session = auth;
         }

      }
   }

   public void setStatus(String status) {
      this.status = status;
   }
}

package com.mojang.authlib.legacy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.net.Proxy;
import org.apache.commons.lang3.Validate;

public class LegacyAuthenticationService extends HttpAuthenticationService {
   protected LegacyAuthenticationService(Proxy var1) {
      super(var1);
   }

   public LegacyUserAuthentication createUserAuthentication(Agent var1) {
      Validate.notNull(var1);
      if (var1 != Agent.MINECRAFT) {
         throw new IllegalArgumentException("Legacy authentication cannot handle anything but Minecraft");
      } else {
         return new LegacyUserAuthentication(this);
      }
   }

   public LegacyMinecraftSessionService createMinecraftSessionService() {
      return new LegacyMinecraftSessionService(this);
   }

   public GameProfileRepository createProfileRepository() {
      throw new UnsupportedOperationException("Legacy authentication service has no profile repository");
   }

   public MinecraftSessionService createMinecraftSessionService() {
      return this.createMinecraftSessionService();
   }

   public UserAuthentication createUserAuthentication(Agent var1) {
      return this.createUserAuthentication(var1);
   }
}

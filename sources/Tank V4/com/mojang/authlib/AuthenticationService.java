package com.mojang.authlib;

import com.mojang.authlib.minecraft.MinecraftSessionService;

public interface AuthenticationService {
   UserAuthentication createUserAuthentication(Agent var1);

   MinecraftSessionService createMinecraftSessionService();

   GameProfileRepository createProfileRepository();
}

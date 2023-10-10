package com.mojang.authlib;

public interface ProfileLookupCallback {
   void onProfileLookupSucceeded(GameProfile var1);

   void onProfileLookupFailed(GameProfile var1, Exception var2);
}

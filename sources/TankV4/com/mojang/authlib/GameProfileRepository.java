package com.mojang.authlib;

public interface GameProfileRepository {
   void findProfilesByNames(String[] var1, Agent var2, ProfileLookupCallback var3);
}

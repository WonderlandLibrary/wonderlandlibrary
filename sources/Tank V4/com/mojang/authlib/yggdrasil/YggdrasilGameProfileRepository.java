package com.mojang.authlib.yggdrasil;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilGameProfileRepository implements GameProfileRepository {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String BASE_URL = "https://api.mojang.com/";
   private static final String SEARCH_PAGE_URL = "https://api.mojang.com/profiles/";
   private static final int ENTRIES_PER_PAGE = 2;
   private static final int MAX_FAIL_COUNT = 3;
   private static final int DELAY_BETWEEN_PAGES = 100;
   private static final int DELAY_BETWEEN_FAILURES = 750;
   private final YggdrasilAuthenticationService authenticationService;

   public YggdrasilGameProfileRepository(YggdrasilAuthenticationService var1) {
      this.authenticationService = var1;
   }

   public void findProfilesByNames(String[] var1, Agent var2, ProfileLookupCallback var3) {
      HashSet var4 = Sets.newHashSet();
      String[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         if (!Strings.isNullOrEmpty(var8)) {
            var4.add(var8.toLowerCase());
         }
      }

      byte var19 = 0;
      Iterator var20 = Iterables.partition(var4, 2).iterator();

      while(var20.hasNext()) {
         List var21 = (List)var20.next();
         int var22 = 0;

         while(true) {
            boolean var9 = false;

            try {
               ProfileSearchResultsResponse var23 = (ProfileSearchResultsResponse)this.authenticationService.makeRequest(HttpAuthenticationService.constantURL("https://api.mojang.com/profiles/" + var2.getName().toLowerCase()), var21, ProfileSearchResultsResponse.class);
               var22 = 0;
               LOGGER.debug("Page {} returned {} results, parsing", Integer.valueOf(var19), var23.getProfiles().length);
               HashSet var24 = Sets.newHashSet((Iterable)var21);
               GameProfile[] var25 = var23.getProfiles();
               int var13 = var25.length;

               for(int var14 = 0; var14 < var13; ++var14) {
                  GameProfile var15 = var25[var14];
                  LOGGER.debug("Successfully looked up profile {}", var15);
                  var24.remove(var15.getName().toLowerCase());
                  var3.onProfileLookupSucceeded(var15);
               }

               Iterator var26 = var24.iterator();

               while(var26.hasNext()) {
                  String var27 = (String)var26.next();
                  LOGGER.debug("Couldn't find profile {}", var27);
                  var3.onProfileLookupFailed(new GameProfile((UUID)null, var27), new ProfileNotFoundException("Server did not find the requested profile"));
               }

               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var17) {
               }
            } catch (AuthenticationException var18) {
               AuthenticationException var10 = var18;
               ++var22;
               if (var22 == 3) {
                  Iterator var11 = var21.iterator();

                  while(var11.hasNext()) {
                     String var12 = (String)var11.next();
                     LOGGER.debug("Couldn't find profile {} because of a server error", var12);
                     var3.onProfileLookupFailed(new GameProfile((UUID)null, var12), var10);
                  }
               } else {
                  try {
                     Thread.sleep(750L);
                  } catch (InterruptedException var16) {
                  }

                  var9 = true;
               }
            }

            if (!var9) {
               break;
            }
         }
      }

   }
}

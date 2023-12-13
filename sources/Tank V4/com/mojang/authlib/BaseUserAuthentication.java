package com.mojang.authlib;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseUserAuthentication implements UserAuthentication {
   private static final Logger LOGGER = LogManager.getLogger();
   protected static final String STORAGE_KEY_PROFILE_NAME = "displayName";
   protected static final String STORAGE_KEY_PROFILE_ID = "uuid";
   protected static final String STORAGE_KEY_PROFILE_PROPERTIES = "profileProperties";
   protected static final String STORAGE_KEY_USER_NAME = "username";
   protected static final String STORAGE_KEY_USER_ID = "userid";
   protected static final String STORAGE_KEY_USER_PROPERTIES = "userProperties";
   private final AuthenticationService authenticationService;
   private final PropertyMap userProperties = new PropertyMap();
   private String userid;
   private String username;
   private String password;
   private GameProfile selectedProfile;
   private UserType userType;

   protected BaseUserAuthentication(AuthenticationService var1) {
      Validate.notNull(var1);
      this.authenticationService = var1;
   }

   public boolean canLogIn() {
      return !this.canPlayOnline() && StringUtils.isNotBlank(this.getUsername()) && StringUtils.isNotBlank(this.getPassword());
   }

   public void logOut() {
      this.password = null;
      this.userid = null;
      this.setSelectedProfile((GameProfile)null);
      this.getModifiableUserProperties().clear();
      this.setUserType((UserType)null);
   }

   public void setUsername(String var1) {
      if (this != null && this.canPlayOnline()) {
         throw new IllegalStateException("Cannot change username whilst logged in & online");
      } else {
         this.username = var1;
      }
   }

   public void setPassword(String var1) {
      if (this != null && this.canPlayOnline() && StringUtils.isNotBlank(var1)) {
         throw new IllegalStateException("Cannot set password whilst logged in & online");
      } else {
         this.password = var1;
      }
   }

   protected String getUsername() {
      return this.username;
   }

   protected String getPassword() {
      return this.password;
   }

   public void loadFromStorage(Map var1) {
      this.logOut();
      this.setUsername(String.valueOf(var1.get("username")));
      if (var1.containsKey("userid")) {
         this.userid = String.valueOf(var1.get("userid"));
      } else {
         this.userid = this.username;
      }

      String var6;
      String var7;
      if (var1.containsKey("userProperties")) {
         try {
            List var2 = (List)var1.get("userProperties");
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Map var4 = (Map)var3.next();
               String var5 = (String)var4.get("name");
               var6 = (String)var4.get("value");
               var7 = (String)var4.get("signature");
               if (var7 == null) {
                  this.getModifiableUserProperties().put(var5, new Property(var5, var6));
               } else {
                  this.getModifiableUserProperties().put(var5, new Property(var5, var6, var7));
               }
            }
         } catch (Throwable var10) {
            LOGGER.warn("Couldn't deserialize user properties", var10);
         }
      }

      if (var1.containsKey("displayName") && var1.containsKey("uuid")) {
         GameProfile var11 = new GameProfile(UUIDTypeAdapter.fromString(String.valueOf(var1.get("uuid"))), String.valueOf(var1.get("displayName")));
         if (var1.containsKey("profileProperties")) {
            try {
               List var12 = (List)var1.get("profileProperties");
               Iterator var13 = var12.iterator();

               while(var13.hasNext()) {
                  Map var14 = (Map)var13.next();
                  var6 = (String)var14.get("name");
                  var7 = (String)var14.get("value");
                  String var8 = (String)var14.get("signature");
                  if (var8 == null) {
                     var11.getProperties().put(var6, new Property(var6, var7));
                  } else {
                     var11.getProperties().put(var6, new Property(var6, var7, var8));
                  }
               }
            } catch (Throwable var9) {
               LOGGER.warn("Couldn't deserialize profile properties", var9);
            }
         }

         this.setSelectedProfile(var11);
      }

   }

   public Map saveForStorage() {
      HashMap var1 = new HashMap();
      if (this.getUsername() != null) {
         var1.put("username", this.getUsername());
      }

      if (this.getUserID() != null) {
         var1.put("userid", this.getUserID());
      } else if (this.getUsername() != null) {
         var1.put("username", this.getUsername());
      }

      if (!this.getUserProperties().isEmpty()) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.getUserProperties().values().iterator();

         while(var3.hasNext()) {
            Property var4 = (Property)var3.next();
            HashMap var5 = new HashMap();
            var5.put("name", var4.getName());
            var5.put("value", var4.getValue());
            var5.put("signature", var4.getSignature());
            var2.add(var5);
         }

         var1.put("userProperties", var2);
      }

      GameProfile var7 = this.getSelectedProfile();
      if (var7 != null) {
         var1.put("displayName", var7.getName());
         var1.put("uuid", var7.getId());
         ArrayList var8 = new ArrayList();
         Iterator var9 = var7.getProperties().values().iterator();

         while(var9.hasNext()) {
            Property var10 = (Property)var9.next();
            HashMap var6 = new HashMap();
            var6.put("name", var10.getName());
            var6.put("value", var10.getValue());
            var6.put("signature", var10.getSignature());
            var8.add(var6);
         }

         if (!var8.isEmpty()) {
            var1.put("profileProperties", var8);
         }
      }

      return var1;
   }

   protected void setSelectedProfile(GameProfile var1) {
      this.selectedProfile = var1;
   }

   public GameProfile getSelectedProfile() {
      return this.selectedProfile;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append("{");
      if (this != null) {
         var1.append("Logged in as ");
         var1.append(this.getUsername());
         if (this.getSelectedProfile() != null) {
            var1.append(" / ");
            var1.append(this.getSelectedProfile());
            var1.append(" - ");
            if (this.canPlayOnline()) {
               var1.append("Online");
            } else {
               var1.append("Offline");
            }
         }
      } else {
         var1.append("Not logged in");
      }

      var1.append("}");
      return var1.toString();
   }

   public AuthenticationService getAuthenticationService() {
      return this.authenticationService;
   }

   public String getUserID() {
      return this.userid;
   }

   public PropertyMap getUserProperties() {
      if (this != null) {
         PropertyMap var1 = new PropertyMap();
         var1.putAll(this.getModifiableUserProperties());
         return var1;
      } else {
         return new PropertyMap();
      }
   }

   protected PropertyMap getModifiableUserProperties() {
      return this.userProperties;
   }

   public UserType getUserType() {
      if (this != null) {
         return this.userType == null ? UserType.LEGACY : this.userType;
      } else {
         return null;
      }
   }

   protected void setUserType(UserType var1) {
      this.userType = var1;
   }

   protected void setUserid(String var1) {
      this.userid = var1;
   }
}

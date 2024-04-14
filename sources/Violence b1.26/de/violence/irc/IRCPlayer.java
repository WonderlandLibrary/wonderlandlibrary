package de.violence.irc;

import java.util.HashMap;

public class IRCPlayer {
   public static HashMap ignToPlayer = new HashMap();
   private String name;
   private String ign;
   private boolean admin;
   private boolean mod;
   private boolean vip;
   private int clientID;

   public IRCPlayer(String name, String ign, boolean admin, boolean mod, boolean vip, int clientID) {
      this.name = name;
      this.ign = ign;
      this.admin = admin;
      this.mod = mod;
      this.vip = vip;
      this.clientID = clientID;
      ignToPlayer.put(ign, this);
   }

   public boolean isAdmin() {
      return this.admin;
   }

   public boolean isMod() {
      return this.mod;
   }

   public boolean isVip() {
      return this.vip;
   }

   public String getIGN() {
      return this.ign;
   }

   public String getName() {
      return this.name;
   }

   public int getClientID() {
      return this.clientID;
   }

   public boolean isViolenceUser() {
      return this.clientID == 1;
   }

   public String getClient() {
      return !this.isViolenceUser()?"Skid":"Violence";
   }
}

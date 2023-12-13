package com.mojang.realmsclient.dto;

public class PlayerInfo {
   private String name;
   private String uuid;
   private boolean operator = false;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public boolean isOperator() {
      return this.operator;
   }

   public void setOperator(boolean var1) {
      this.operator = var1;
   }
}

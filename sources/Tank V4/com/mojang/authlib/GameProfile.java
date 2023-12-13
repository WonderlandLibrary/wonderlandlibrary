package com.mojang.authlib;

import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameProfile {
   private final UUID id;
   private final String name;
   private final PropertyMap properties = new PropertyMap();
   private boolean legacy;

   public GameProfile(UUID var1, String var2) {
      if (var1 == null && StringUtils.isBlank(var2)) {
         throw new IllegalArgumentException("Name and ID cannot both be blank");
      } else {
         this.id = var1;
         this.name = var2;
      }
   }

   public UUID getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public PropertyMap getProperties() {
      return this.properties;
   }

   public boolean isComplete() {
      return this.id != null && StringUtils.isNotBlank(this.getName());
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         GameProfile var2 = (GameProfile)var1;
         if (this.id != null) {
            if (!this.id.equals(var2.id)) {
               return false;
            }
         } else if (var2.id != null) {
            return false;
         }

         if (this.name != null) {
            if (this.name.equals(var2.name)) {
               return true;
            }
         } else if (var2.name == null) {
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.id != null ? this.id.hashCode() : 0;
      var1 = 31 * var1 + (this.name != null ? this.name.hashCode() : 0);
      return var1;
   }

   public String toString() {
      return (new ToStringBuilder(this)).append("id", (Object)this.id).append("name", (Object)this.name).append("properties", (Object)this.properties).append("legacy", this.legacy).toString();
   }

   public boolean isLegacy() {
      return this.legacy;
   }
}

package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class SimpleResource implements IResource {
   private final InputStream mcmetaInputStream;
   private final ResourceLocation srResourceLocation;
   private final String resourcePackName;
   private final IMetadataSerializer srMetadataSerializer;
   private final InputStream resourceInputStream;
   private boolean mcmetaJsonChecked;
   private final Map mapMetadataSections = Maps.newHashMap();
   private JsonObject mcmetaJson;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof SimpleResource)) {
         return false;
      } else {
         SimpleResource var2 = (SimpleResource)var1;
         if (this.srResourceLocation != null) {
            if (!this.srResourceLocation.equals(var2.srResourceLocation)) {
               return false;
            }
         } else if (var2.srResourceLocation != null) {
            return false;
         }

         if (this.resourcePackName != null) {
            if (!this.resourcePackName.equals(var2.resourcePackName)) {
               return false;
            }
         } else if (var2.resourcePackName != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
      var1 = 31 * var1 + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
      return var1;
   }

   public ResourceLocation getResourceLocation() {
      return this.srResourceLocation;
   }

   public SimpleResource(String var1, ResourceLocation var2, InputStream var3, InputStream var4, IMetadataSerializer var5) {
      this.resourcePackName = var1;
      this.srResourceLocation = var2;
      this.resourceInputStream = var3;
      this.mcmetaInputStream = var4;
      this.srMetadataSerializer = var5;
   }

   public String getResourcePackName() {
      return this.resourcePackName;
   }

   public InputStream getInputStream() {
      return this.resourceInputStream;
   }

   public IMetadataSection getMetadata(String var1) {
      if (this != null) {
         return null;
      } else {
         if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
            this.mcmetaJsonChecked = true;
            BufferedReader var2 = null;
            var2 = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
            this.mcmetaJson = (new JsonParser()).parse((Reader)var2).getAsJsonObject();
            IOUtils.closeQuietly((Reader)var2);
         }

         IMetadataSection var4 = (IMetadataSection)this.mapMetadataSections.get(var1);
         if (var4 == null) {
            var4 = this.srMetadataSerializer.parseMetadataSection(var1, this.mcmetaJson);
         }

         return var4;
      }
   }
}

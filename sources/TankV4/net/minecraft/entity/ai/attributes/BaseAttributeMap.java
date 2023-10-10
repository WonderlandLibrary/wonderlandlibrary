package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.management.LowerStringMap;

public abstract class BaseAttributeMap {
   protected final Multimap field_180377_c = HashMultimap.create();
   protected final Map attributes = Maps.newHashMap();
   protected final Map attributesByName = new LowerStringMap();

   public Collection getAllAttributes() {
      return this.attributesByName.values();
   }

   public void applyAttributeModifiers(Multimap var1) {
      Iterator var3 = var1.entries().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         IAttributeInstance var4 = this.getAttributeInstanceByName((String)var2.getKey());
         if (var4 != null) {
            var4.removeModifier((AttributeModifier)var2.getValue());
            var4.applyModifier((AttributeModifier)var2.getValue());
         }
      }

   }

   protected abstract IAttributeInstance func_180376_c(IAttribute var1);

   public IAttributeInstance getAttributeInstanceByName(String var1) {
      return (IAttributeInstance)this.attributesByName.get(var1);
   }

   public void func_180794_a(IAttributeInstance var1) {
   }

   public void removeAttributeModifiers(Multimap var1) {
      Iterator var3 = var1.entries().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         IAttributeInstance var4 = this.getAttributeInstanceByName((String)var2.getKey());
         if (var4 != null) {
            var4.removeModifier((AttributeModifier)var2.getValue());
         }
      }

   }

   public IAttributeInstance registerAttribute(IAttribute var1) {
      if (this.attributesByName.containsKey(var1.getAttributeUnlocalizedName())) {
         throw new IllegalArgumentException("Attribute is already registered!");
      } else {
         IAttributeInstance var2 = this.func_180376_c(var1);
         this.attributesByName.put(var1.getAttributeUnlocalizedName(), var2);
         this.attributes.put(var1, var2);

         for(IAttribute var3 = var1.func_180372_d(); var3 != null; var3 = var3.func_180372_d()) {
            this.field_180377_c.put(var3, var1);
         }

         return var2;
      }
   }

   public IAttributeInstance getAttributeInstance(IAttribute var1) {
      return (IAttributeInstance)this.attributes.get(var1);
   }
}

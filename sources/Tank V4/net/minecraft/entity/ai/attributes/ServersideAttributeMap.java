package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.management.LowerStringMap;

public class ServersideAttributeMap extends BaseAttributeMap {
   private final Set attributeInstanceSet = Sets.newHashSet();
   protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();

   public ModifiableAttributeInstance getAttributeInstance(IAttribute var1) {
      return (ModifiableAttributeInstance)super.getAttributeInstance(var1);
   }

   public IAttributeInstance getAttributeInstanceByName(String var1) {
      return this.getAttributeInstanceByName(var1);
   }

   protected IAttributeInstance func_180376_c(IAttribute var1) {
      return new ModifiableAttributeInstance(this, var1);
   }

   public IAttributeInstance getAttributeInstance(IAttribute var1) {
      return this.getAttributeInstance(var1);
   }

   public ModifiableAttributeInstance getAttributeInstanceByName(String var1) {
      IAttributeInstance var2 = super.getAttributeInstanceByName(var1);
      if (var2 == null) {
         var2 = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get(var1);
      }

      return (ModifiableAttributeInstance)var2;
   }

   public Set getAttributeInstanceSet() {
      return this.attributeInstanceSet;
   }

   public Collection getWatchedAttributes() {
      HashSet var1 = Sets.newHashSet();
      Iterator var3 = this.getAllAttributes().iterator();

      while(var3.hasNext()) {
         IAttributeInstance var2 = (IAttributeInstance)var3.next();
         if (var2.getAttribute().getShouldWatch()) {
            var1.add(var2);
         }
      }

      return var1;
   }

   public void func_180794_a(IAttributeInstance var1) {
      if (var1.getAttribute().getShouldWatch()) {
         this.attributeInstanceSet.add(var1);
      }

      Iterator var3 = this.field_180377_c.get(var1.getAttribute()).iterator();

      while(var3.hasNext()) {
         IAttribute var2 = (IAttribute)var3.next();
         ModifiableAttributeInstance var4 = this.getAttributeInstance(var2);
         if (var4 != null) {
            var4.flagForUpdate();
         }
      }

   }

   public IAttributeInstance registerAttribute(IAttribute var1) {
      IAttributeInstance var2 = super.registerAttribute(var1);
      if (var1 instanceof RangedAttribute && ((RangedAttribute)var1).getDescription() != null) {
         this.descriptionToAttributeInstanceMap.put(((RangedAttribute)var1).getDescription(), var2);
      }

      return var2;
   }
}

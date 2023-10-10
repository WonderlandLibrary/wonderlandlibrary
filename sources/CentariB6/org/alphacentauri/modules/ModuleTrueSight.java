package org.alphacentauri.modules;

import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleTrueSight extends Module {
   public Property SemiInvisible = new Property(this, "SemiInvisible", Boolean.valueOf(true));
   public Property Barriers = new Property(this, "Barriers", Boolean.valueOf(true));

   public ModuleTrueSight() {
      super("TrueSight", "Let\'s you see the truth (inv. entities)", new String[]{"truesight"}, Module.Category.Render, 12880871);
   }

   public boolean shouldShowBarriers() {
      return this.isEnabled() && ((Boolean)this.Barriers.value).booleanValue();
   }
}

package net.minecraft.entity.player;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum EnumPlayerModelParts {
   RIGHT_SLEEVE(3, "right_sleeve"),
   LEFT_SLEEVE(2, "left_sleeve");

   private static final EnumPlayerModelParts[] ENUM$VALUES = new EnumPlayerModelParts[]{CAPE, JACKET, LEFT_SLEEVE, RIGHT_SLEEVE, LEFT_PANTS_LEG, RIGHT_PANTS_LEG, HAT};
   CAPE(0, "cape");

   private final int partId;
   private final String partName;
   private final int partMask;
   LEFT_PANTS_LEG(4, "left_pants_leg");

   private final IChatComponent field_179339_k;
   JACKET(1, "jacket"),
   RIGHT_PANTS_LEG(5, "right_pants_leg"),
   HAT(6, "hat");

   public int getPartMask() {
      return this.partMask;
   }

   public int getPartId() {
      return this.partId;
   }

   public String getPartName() {
      return this.partName;
   }

   private EnumPlayerModelParts(int var3, String var4) {
      this.partId = var3;
      this.partMask = 1 << var3;
      this.partName = var4;
      this.field_179339_k = new ChatComponentTranslation("options.modelPart." + var4, new Object[0]);
   }

   public IChatComponent func_179326_d() {
      return this.field_179339_k;
   }
}

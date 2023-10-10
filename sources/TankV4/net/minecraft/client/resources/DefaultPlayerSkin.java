package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin {
   private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
   private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

   public static ResourceLocation getDefaultSkin(UUID var0) {
      return var0 != false ? TEXTURE_ALEX : TEXTURE_STEVE;
   }

   public static String getSkinType(UUID param0) {
      // $FF: Couldn't be decompiled
   }

   public static ResourceLocation getDefaultSkinLegacy() {
      return TEXTURE_STEVE;
   }
}

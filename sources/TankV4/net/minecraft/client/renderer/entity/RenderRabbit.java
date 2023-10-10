package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class RenderRabbit extends RenderLiving {
   private static final ResourceLocation WHITE_SPLOTCHED = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
   private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
   private static final ResourceLocation TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
   private static final ResourceLocation CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
   private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
   private static final ResourceLocation BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
   private static final ResourceLocation GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
   private static final ResourceLocation SALT = new ResourceLocation("textures/entity/rabbit/salt.png");

   protected ResourceLocation getEntityTexture(EntityRabbit var1) {
      String var2 = EnumChatFormatting.getTextWithoutFormattingCodes(var1.getName());
      if (var2 != null && var2.equals("Toast")) {
         return TOAST;
      } else {
         switch(var1.getRabbitType()) {
         case 0:
         default:
            return BROWN;
         case 1:
            return WHITE;
         case 2:
            return BLACK;
         case 3:
            return WHITE_SPLOTCHED;
         case 4:
            return GOLD;
         case 5:
            return SALT;
         case 99:
            return CAERBANNOG;
         }
      }
   }

   public RenderRabbit(RenderManager var1, ModelBase var2, float var3) {
      super(var1, var2, var3);
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityRabbit)var1);
   }
}

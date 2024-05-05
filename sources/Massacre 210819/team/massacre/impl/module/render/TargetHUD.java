package team.massacre.impl.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventRender2D;
import team.massacre.impl.module.combat.KillAura2;
import team.massacre.utils.GuiUtils;
import team.massacre.utils.MathUtil;
import team.massacre.utils.PlayerUtils;
import team.massacre.utils.TimerUtil;

public final class TargetHUD extends Module {
   private final TimerUtil animationStopwatch = new TimerUtil();
   private double healthBarWidth;
   private float lastHealth = 0.0F;
   public final EnumProperty<TargetHUD.Rape> mode;
   private final Property<Boolean> showPlayers;
   private final Property<Boolean> showMonsters;
   private final Property<Boolean> showInvisibles;
   private final Property<Boolean> showAnimals;
   private final Property<Boolean> showPassives;
   private final EnumProperty<TargetHUD.ColorMode> colorModeProperty;
   private final Property<Integer> colorProperty;
   private final Property<Integer> secondColorProperty;
   private final Map<EntityLivingBase, Double> entityDamageMap;
   private final Map<EntityLivingBase, Integer> entityArmorCache;
   private double x;
   private double y;

   public TargetHUD() {
      super("TargetHUD", 0, Category.RENDER);
      this.mode = new EnumProperty("TargetHud Mode", TargetHUD.Rape.Normal);
      this.showPlayers = new Property("Show Players", true);
      this.showMonsters = new Property("Show Monsters", true);
      this.showInvisibles = new Property("Show Invisibles", true);
      this.showAnimals = new Property("Show Animals", true);
      this.showPassives = new Property("Show Passives", true);
      this.colorModeProperty = new EnumProperty("Color Mode", TargetHUD.ColorMode.COLOR);
      this.colorProperty = new Property("Color", -1);
      this.secondColorProperty = new Property("Second Color", -1);
      this.entityDamageMap = new HashMap();
      this.entityArmorCache = new HashMap();
      this.addValues(new Property[]{this.mode, this.showPlayers, this.showMonsters, this.showAnimals, this.showInvisibles, this.showPassives});
   }

   @Handler
   public void onRender2D(EventRender2D event) {
      this.setSuffix(((TargetHUD.Rape)this.mode.getValue()).name());
      FontRenderer fr = this.mc.fontRendererObj;
      Hud hud = (Hud)Massacre.INSTANCE.getModuleManager().getModule(Hud.class);
      KillAura2 killAura = (KillAura2)Massacre.INSTANCE.getModuleManager().getModule(KillAura2.class);
      boolean guichat = this.mc.currentScreen instanceof GuiChat;
      ScaledResolution scaledResolution = event.getScaledResolution();
      int var141 = scaledResolution.getScaledWidth();
      int var151 = scaledResolution.getScaledHeight();
      float n = 2.0F;
      if (this.mode.getValue() != TargetHUD.Rape.Radium) {
         scaledResolution.scaledWidth *= 0;
         scaledResolution.scaledHeight *= 0;
      }

      int mouseX = Mouse.getX() * var141 / this.mc.displayWidth;
      int mouseY = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
      if (Mouse.isButtonDown(0) && guichat) {
         this.setX((double)(mouseX - 300));
         this.setY((double)(mouseY - 200));
      }

      EntityLivingBase target = guichat ? this.mc.thePlayer : killAura.target;
      if (target != null) {
         int n2;
         int n3;
         float health;
         float healthPercentage;
         float scaledWidth;
         float scaledHeight;
         int x1;
         int i;
         int x;
         int yAdd;
         double damageAsHealthBarWidth;
         Color healthcolor;
         float armorValue;
         String name;
         float healthPercentage;
         float diff;
         switch((TargetHUD.Rape)this.mode.getValue()) {
         case Radium:
            boolean isPlayer = target instanceof EntityOtherPlayerMP;
            ScaledResolution lr = event.getScaledResolution();
            FontRenderer fontRenderer = this.mc.fontRendererObj;
            int sWidth = lr.getScaledWidth();
            int sHeight = lr.getScaledHeight();
            int middleX = sWidth / 2;
            int middleY = sHeight / 2;
            String name1;
            if (isPlayer) {
               name1 = ((EntityPlayer)target).getGameProfile().getName();
            } else {
               name1 = ((EntityLivingBase)target).getDisplayName().getUnformattedText();
            }

            int yOffset = true;
            int headSize = true;
            int margin = true;
            int width = Math.max(100, 32 + (int)Math.ceil((double)(fontRenderer.getStringWidth(name1) / 2)) + 4);
            int half = width / 2;
            int left = middleX - half;
            int right = middleX + half;
            int top = middleY + 20;
            int bottom = top + 32;
            float alpha = 0.6F;
            float maxHealth1;
            if (isPlayer) {
               GuiUtils.enableBlending();
               AbstractClientPlayer clientPlayer = (AbstractClientPlayer)target;
               GL11.glEnable(3553);
               Minecraft.getMinecraft().getTextureManager().bindTexture(clientPlayer.getLocationSkin());
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
               maxHealth1 = 0.125F;
               GL11.glBegin(7);
               GL11.glTexCoord2f(0.125F, 0.125F);
               GL11.glVertex2i(left, top);
               GL11.glTexCoord2f(0.125F, 0.25F);
               GL11.glVertex2i(left, bottom);
               GL11.glTexCoord2f(0.25F, 0.25F);
               GL11.glVertex2i(left + 32, bottom);
               GL11.glTexCoord2f(0.25F, 0.125F);
               GL11.glVertex2i(left + 32, top);
               GL11.glEnd();
               GL11.glDisable(3042);
            }

            float health1 = ((EntityLivingBase)target).getHealth();
            maxHealth1 = ((EntityLivingBase)target).getMaxHealth();
            float healthPercentage1 = health1 / maxHealth1;
            int fadeColor;
            switch((TargetHUD.ColorMode)this.colorModeProperty.getValue()) {
            case COLOR:
               fadeColor = (Integer)this.colorProperty.getValue();
               break;
            case BLEND:
               fadeColor = GuiUtils.fadeBetween((Integer)this.colorProperty.getValue(), (Integer)this.secondColorProperty.getValue());
               break;
            default:
               fadeColor = GuiUtils.blendColors(healthPercentage1);
            }

            fadeColor = GuiUtils.fadeBetween(fadeColor, GuiUtils.darker(fadeColor));
            int alphaInt = alphaToInt(0.6F, 0);
            int textAlpha = alphaToInt(0.6F, 70);
            fadeColor += textAlpha << 24;
            int backgroundColor = alphaInt << 24;
            Gui.drawRect(left + 32, top, right, bottom, backgroundColor);
            float infoLeft = (float)(left + 32 + 2);
            float infoTop = (float)(top + 2);
            float scale = 0.5F;
            GL11.glScalef(0.5F, 0.5F, 1.0F);
            float infoypos = infoTop / 0.5F;
            fontRenderer.drawStringWithShadow(name1, infoLeft / 0.5F, infoypos, 16777215 + (textAlpha << 24));
            infoypos += (float)fontRenderer.FONT_HEIGHT;
            String healthText = String.format("§FHP: §R%.1f", (double)health1 / 2.0D);
            fontRenderer.drawStringWithShadow(healthText, infoLeft / 0.5F, infoypos, fadeColor);
            infoypos += (float)fontRenderer.FONT_HEIGHT;
            if (isPlayer) {
               EntityPlayer player = (EntityPlayer)target;
               int targetArmor = this.getOrCacheArmor(player);
               int localArmor = this.getOrCacheArmor(this.mc.thePlayer);
               char prefix;
               if (targetArmor > localArmor) {
                  prefix = '4';
               } else if (targetArmor < localArmor) {
                  prefix = 'A';
               } else {
                  prefix = 'F';
               }

               String armorText = String.format("§FArmor: §R%s%% §F/ §%s%s%%", targetArmor, prefix, Math.abs(targetArmor - localArmor));
               fontRenderer.drawStringWithShadow(armorText, infoLeft / 0.5F, infoypos, 5308415 + (textAlpha << 24));
            }

            float scaleUp = 2.0F;
            GL11.glScalef(2.0F, 2.0F, 1.0F);
            ((EntityLivingBase)target).healthProgressX = (float)GuiUtils.linearAnimation((double)((EntityLivingBase)target).healthProgressX, (double)healthPercentage1, 0.02D);
            float healthBarRight = (float)(right - 2);
            float xDif = healthBarRight - infoLeft;
            float healthBarThickness = 4.0F;
            float healthBarEnd = infoLeft + xDif * ((EntityLivingBase)target).healthProgressX;
            float healthBarBottom = (float)(bottom - 2);
            float healthBarTop = healthBarBottom - 4.0F;
            Gui.drawRect((double)infoLeft, (double)healthBarTop, (double)healthBarRight, (double)healthBarBottom, backgroundColor);
            if (this.entityDamageMap.containsKey(target)) {
               double lastDamage = (Double)this.entityDamageMap.get(target);
               if (lastDamage > 0.0D) {
                  damageAsHealthBarWidth = (double)xDif * (lastDamage / (double)maxHealth1);
                  Gui.drawRect((double)healthBarEnd, (double)healthBarTop, Math.min((double)healthBarEnd + damageAsHealthBarWidth, (double)healthBarRight), (double)healthBarBottom, GuiUtils.darker(fadeColor));
               }
            }

            Gui.drawRect((double)infoLeft, (double)healthBarTop, (double)healthBarEnd, (double)healthBarBottom, fadeColor);
            break;
         case Old:
            Float object2 = (float)MathUtil.round((double)this.mc.thePlayer.getDistanceToEntity((Entity)target), 1);
            String object = "";
            if (((EntityLivingBase)target).getHealth() > this.mc.thePlayer.getHealth()) {
               object = "You are losing";
            }

            if (((EntityLivingBase)target).getHealth() < this.mc.thePlayer.getHealth()) {
               object = "You are winning";
            }

            if (((EntityLivingBase)target).getHealth() == this.mc.thePlayer.getHealth()) {
               object = "You are equal";
            }

            drawEntityOnScreen(this.mc.displayWidth / 2 - 440, this.mc.displayHeight / 2 - 143, 25, 20.0F, 20.0F, (EntityLivingBase)target);
            GuiUtils.drawRoundedRect2(500.0D, 310.0D, 650.0D, 370.0D, 6.0D, 1342177280);
            fr.drawStringWithShadow(String.valueOf((new StringBuilder("Name: ")).append(((EntityPlayer)target).getGameProfile().getName())), 540.0F, 320.0F, -1);
            fr.drawStringWithShadow(String.valueOf((new StringBuilder("Health: ")).append((float)MathUtil.round((double)((EntityLivingBase)target).getHealth(), 1))), 540.0F, 337.0F, -1);
            fr.drawStringWithShadow(String.valueOf((new StringBuilder("Distance: ")).append(object2)), 540.0F, 355.0F, -1);
            GuiUtils.drawRoundedRect2(500.0D, 370.0D, 650.0D, 373.0D, 6.0D, GuiUtils.getGradientOffset(new Color(64, 85, 150), new Color(130, 191, 226), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D).getRGB());
            break;
         case Normal:
            if (this.mc.thePlayer != null && killAura.target != null || guichat) {
               if (this.isValidEntity((EntityLivingBase)target) || target == this.mc.thePlayer) {
                  GlStateManager.pushMatrix();
                  GlStateManager.translate(this.x, this.y, 0.0D);
                  GlStateManager.enableBlend();
                  n2 = scaledResolution.getScaledWidth() / 2 + 300;
                  n3 = scaledResolution.getScaledHeight() / 2 + 200;
                  GuiUtils.drawRect((double)((float)n2 + 1.0F), (double)(n3 + 1), 140.0D, 37.6D, (new Color(25, 25, 25, 210)).getRGB());
                  String string1 = String.format("%.1f", ((EntityLivingBase)target).getHealth() / 2.0F);
                  GlStateManager.pushMatrix();
                  GlStateManager.scale(2.0F, 2.0F, 2.0F);
                  this.mc.fontRendererObj.drawStringWithShadow(string1.replace(".0", ""), (float)(n2 - 121), (float)(n3 - 93), GuiUtils.drawHealth((EntityLivingBase)target));
                  this.mc.fontRendererObj.drawStringWithShadow("❤", (float)(n2 - 130), (float)(n3 - 94), GuiUtils.drawHealth((EntityLivingBase)target));
                  GlStateManager.popMatrix();
                  double n4 = (double)(137.0F / ((EntityLivingBase)target).getMaxHealth()) * (double)Math.min(((EntityLivingBase)target).getHealth(), ((EntityLivingBase)target).getMaxHealth());
                  if (this.animationStopwatch.hasHit(15.0F)) {
                     this.healthBarWidth = GuiUtils.linearAnimation(this.healthBarWidth, n4, 0.05D);
                     this.animationStopwatch.reset();
                  }

                  GuiUtils.drawRect((double)((float)n2 + 2.0F), (double)((float)n3 + 34.0F), 138.0D, 3.5D, GuiUtils.darker(new Color(GuiUtils.drawHealth((EntityLivingBase)target)), 0.35F).getRGB());
                  float var93;
                  if (((EntityLivingBase)target).getHealth() != 0.0F && ((EntityLivingBase)target).getHealth() != ((EntityLivingBase)target).getMaxHealth()) {
                     var93 = 4.0F;
                  } else {
                     var93 = 0.0F;
                  }

                  GuiUtils.drawRect((double)((float)n2 + 2.0F), (double)((float)n3 + 34.0F), (double)((float)this.healthBarWidth + 0.9F), 3.5D, GuiUtils.drawHealth(((EntityLivingBase)target).getHealth(), ((EntityLivingBase)target).getMaxHealth()).getRGB());
                  GuiUtils.drawRect((double)((float)n2 + 2.0F), (double)((float)n3 + 34.0F), n4 + 0.8999999761581421D, 3.5D, GuiUtils.drawHealth((EntityLivingBase)target));
                  name = target instanceof EntityPlayer ? ((EntityPlayer)target).getGameProfile().getName() : killAura.target.getDisplayName().getFormattedText();
                  GlStateManager.enableBlend();
                  this.mc.fontRendererObj.drawStringWithShadow(name, (float)(n2 + 35), (float)(n3 + 3), -855638017);
                  if (target instanceof EntityPlayer) {
                     this.mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
                     GlStateManager.enableBlend();
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
                     Gui.drawScaledCustomSizeModalRect(n2 + 2, n3 + 2, 8.0F, 8.0F, 5, 5, 31, 31, 64.0F, 64.0F);
                  }

                  GlStateManager.disableBlend();
                  GlStateManager.popMatrix();
               }

               if (!this.isValidEntity((EntityLivingBase)target)) {
                  target = null;
               }
            }
            break;
         case Informatic:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               GlStateManager.popMatrix();
               GlStateManager.pushMatrix();
               n2 = scaledResolution.getScaledHeight() / 2 + 200;
               n3 = scaledResolution.getScaledWidth() / 2 + 300;
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 30.0F, (float)n2 + 80.0F, (float)n3 + 105.0F, 5.0F, (new Color(0, 0, 0, 180)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 104.0F, (float)n2 - 70.0F + 149.0F * scaledWidth, (float)n3 + 106.0F, 6.0F, healthcolor.getRGB());
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 104.0F, (float)n2 - 70.0F + 149.0F * healthPercentage, (float)n3 + 106.0F, 6.0F, Color.GREEN.getRGB());
               fr.drawString(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 30), (float)(n3 + 40), -1);
               name = "";
               if (killAura.target.onGround) {
                  name = "§2onGround";
               } else {
                  name = "§4offGround";
               }

               fr.drawString("Distance: " + Math.round(((EntityLivingBase)target).getDistanceToEntity(this.mc.thePlayer)), (float)(n2 + 19), (float)(n3 + 62), -1);
               fr.drawString("Health: " + Math.round(((EntityLivingBase)target).getHealth()), (float)(n2 - 30), (float)(n3 + 62), -1);
               if (!(this.mc.thePlayer.getHealth() >= ((EntityLivingBase)target).getHealth()) && this.mc.thePlayer.getHealth() < ((EntityLivingBase)target).getHealth()) {
               }

               for(i = 3; i > -1; --i) {
                  ItemStack itemstack = ((EntityPlayer)target).inventory.armorInventory[i];
                  yAdd = i * 15;
                  GL11.glPushMatrix();
                  RenderHelper.enableGUIStandardItemLighting();
                  this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, n2 + 15 - yAdd, n3 + 80);
                  GL11.glPopMatrix();
               }

               GL11.glPushMatrix();
               RenderHelper.enableGUIStandardItemLighting();
               this.mc.getRenderItem().renderItemAndEffectIntoGUI(((EntityPlayer)target).getCurrentEquippedItem(), n2 + 40, n3 + 80);
               GL11.glPopMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               drawEntityOnScreen(n2 - 50, n3 + 95, 28, 25.0F, 25.0F, (EntityLivingBase)target);
               GlStateManager.popMatrix();
            }
            break;
         case NovolineOld:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 50.0F, (float)n2 + 60.0F, (float)n3 + 105.0F, 6.0F, (new Color(0, 0, 0, 180)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 104.0F, (float)n2 - 70.0F + 130.0F * scaledWidth, (float)n3 + 106.0F, 5.0F, healthcolor.getRGB());
               fr.drawString(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 30), (float)(n3 + 60), -1);

               for(x1 = 3; x1 > -1; --x1) {
                  ItemStack itemstack = ((EntityPlayer)target).inventory.armorInventory[x1];
                  x = x1 * 15;
                  GL11.glPushMatrix();
                  RenderHelper.enableGUIStandardItemLighting();
                  this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, n2 + 10 - x, n3 + 80);
                  GL11.glPopMatrix();
               }

               GL11.glPushMatrix();
               RenderHelper.enableGUIStandardItemLighting();
               this.mc.getRenderItem().renderItemAndEffectIntoGUI(((EntityPlayer)target).getCurrentEquippedItem(), n2 + 34, n3 + 80);
               GL11.glPopMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               drawEntityOnScreen(n2 - 50, n3 + 95, 20, 25.0F, 25.0F, (EntityLivingBase)target);
               GlStateManager.popMatrix();
            }
            break;
         case Novoline:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 71.0F, (float)n3 + 51.0F, (float)n2 + 95.0F, (float)n3 + 93.0F, 6.0F, (new Color(44, 38, 40)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawRoundedRect2((float)n2 - 28.0F, (float)n3 + 68.0F, (float)n2 - 28.0F + 114.0F, (float)n3 + 77.5F, 6.0F, GuiUtils.getGradientOffset(new Color(64, 85, 150), new Color(130, 191, 226), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D).darker().getRGB());
               GuiUtils.drawRoundedRect2((float)n2 - 28.0F, (float)n3 + 68.0F, (float)n2 - 28.0F + 114.0F * scaledWidth, (float)n3 + 77.5F, 6.0F, GuiUtils.getGradientOffset(new Color(64, 85, 150), new Color(130, 191, 226), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.0D).getRGB());
               GuiUtils.drawRoundedRect2((float)n2 - 28.0F, (float)n3 + 68.0F, (float)n2 - 28.0F + 114.0F * healthPercentage, (float)n3 + 77.5F, 6.0F, -1879048192);
               fr.drawString(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 28), (float)(n3 + 55), -1);
               fr.drawString(ChatFormatting.WHITE + String.format("%.1f", ((EntityLivingBase)target).getHealth() / 2.0F) + ChatFormatting.RESET + " ❤", (float)(n2 - 28), (float)(n3 + 82), -1);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawFace((double)(n2 - 70), (double)(n3 + 52), 5.0F, 5.0F, 5, 5, 40, 40, 64.0F, 64.0F, (AbstractClientPlayer)target);
               GlStateManager.popMatrix();
            }
            break;
         case MichaelXF:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 55.0F, (float)n2 + 94.0F, (float)n3 + 106.0F, 6.0F, (new Color(22, 22, 22, 255)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawGradientSideways((double)((float)n2 - 70.0F), (double)((float)n3 + 103.0F), (double)((float)n2 - 70.0F + 164.0F * scaledWidth), (double)((float)n3 + 106.0F), Color.RED.getRGB(), Color.GREEN.getRGB());
               fr.drawString(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 60), (float)(n3 + 63), -1);
               name = ((EntityPlayer)target).getCurrentEquippedItem() == null ? "Nothing" : ((EntityPlayer)target).getCurrentEquippedItem().getDisplayName().toString();
               fr.drawString(ChatFormatting.GRAY + name, (float)(n2 + 15), (float)(n3 + 63), Color.GRAY.getRGB());
               i = n2 - 90;
               x = n3 + 60;
               GL11.glPushMatrix();
               GlStateManager.translate((float)i, (float)x, 1.0F);
               GL11.glScalef(1.8F, 1.8F, 1.8F);
               GlStateManager.translate((float)(-i), (float)(-x), 1.0F);
               this.mc.fontRendererObj.drawStringWithShadow(Math.round(((EntityLivingBase)target).getHealth() / 2.0F) + " HP", (float)(i + 16), (float)(x + 10), GuiUtils.getRainbow(300, 0, 1.0F));
               GL11.glPopMatrix();
               GlStateManager.popMatrix();
            }
            break;
         case Simple:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 30.0F, (float)n2 + 80.0F, (float)n3 + 105.0F, 6.0F, (new Color(0, 0, 0, 180)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 104.0F, (float)n2 - 70.0F + 149.0F * scaledWidth, (float)n3 + 106.0F, 6.0F, healthcolor.getRGB());
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 104.0F, (float)n2 - 70.0F + 149.0F * healthPercentage, (float)n3 + 106.0F, 6.0F, Color.GREEN.getRGB());
               fr.drawString(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 30), (float)(n3 + 40), -1);
               fr.drawString("Health: " + Math.round(((EntityLivingBase)target).getHealth()), (float)(n2 - 30), (float)(n3 + 62), -1);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               drawEntityOnScreen(n2 - 50, n3 + 95, 28, 25.0F, 25.0F, (EntityLivingBase)target);
               GlStateManager.popMatrix();
            }
            break;
         case Blocky:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 62.0F, (float)n3 + 20.0F, (float)n2 + 72.0F, (float)n3 + 105.0F, 6.0F, (new Color(0, 0, 0, 110)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth() + 0.29F;
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 95.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 80.0F && healthPercentage * 100.0F < 95.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 80.0F && healthPercentage * 100.0F > 55.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 55.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawRoundedRect2((float)n2 - 42.0F, (float)n3 + 82.0F, (float)n2 - 70.0F + 129.0F, (float)n3 + 98.0F, 6.0F, Color.DARK_GRAY.darker().getRGB());
               GuiUtils.drawRoundedRect2((float)n2 - 40.0F, (float)n3 + 84.0F, (float)n2 - 70.0F + 99.0F * scaledWidth, (float)n3 + 96.0F, 6.0F, healthcolor.getRGB());
               name = this.mc.getNetHandler().getPlayerInfo(((EntityLivingBase)target).getUniqueID()) == null ? "0ms" : this.mc.getNetHandler().getPlayerInfo(((EntityLivingBase)target).getUniqueID()).getResponseTime() + "ms";
               fr.drawString("Health: " + Math.round(((EntityLivingBase)target).getHealth()), (float)((int)((float)n2 - 25.0F)), (float)(n3 + 28), -1);
               fr.drawString("Distance: " + Math.round(this.mc.thePlayer.getDistanceToEntity((Entity)target)), (float)((int)((float)n2 - 25.0F)), (float)(n3 + 48), -1);
               fr.drawString("Ping: " + name, (float)((int)((float)n2 - 25.0F)), (float)(n3 + 68), -1);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               drawEntityOnScreen((int)((float)n2 - 42.0F), n3 + 78, 25, 25.0F, 30.0F, (EntityLivingBase)target);
               GlStateManager.popMatrix();
            }
            break;
         case OldAstolfo:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               String winStatus = String.format("%s", ((EntityLivingBase)target).getHealth() > this.mc.thePlayer.getHealth() ? "Losing" : "Winning");
               String ping = this.mc.getNetHandler().getPlayerInfo(((EntityLivingBase)target).getUniqueID()) == null ? "0ms" : this.mc.getNetHandler().getPlayerInfo(((EntityLivingBase)target).getUniqueID()).getResponseTime() + "ms";
               double hpPercentage = (double)(((EntityLivingBase)target).getHealth() / ((EntityLivingBase)target).getMaxHealth());
               scaledWidth = 775.0F;
               scaledHeight = 732.0F;
               armorValue = ((EntityLivingBase)target).getHealth();
               healthPercentage = armorValue / ((EntityLivingBase)target).getMaxHealth() + 0.29F;
               diff = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  float diff = healthPercentage - this.lastHealth;
                  diff = this.lastHealth;
                  this.lastHealth += diff / 8.0F;
               }

               Color healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 95.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 80.0F && healthPercentage * 100.0F < 95.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 80.0F && healthPercentage * 100.0F > 55.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 55.0F) {
                  healthcolor = Color.RED;
               }

               if (hpPercentage > 1.0D) {
                  hpPercentage = 1.0D;
               } else if (hpPercentage < 0.0D) {
                  hpPercentage = 0.0D;
               }

               float n2 = 200.0F;
               GuiUtils.drawRoundedRect2(scaledWidth / 2.0F, scaledHeight / 3.0F - 10.0F, scaledWidth / 2.0F + 40.0F + (float)(this.mc.fontRendererObj.getStringWidth(((EntityPlayer)target).getGameProfile().getName()) > 105 ? this.mc.fontRendererObj.getStringWidth(((EntityPlayer)target).getGameProfile().getName()) - 10 : 105), scaledHeight / 3.0F + 32.0F, 6.0F, -1728053248);
               drawEntityOnScreen((int)(scaledWidth / 2.0F + 17.0F), (int)scaledHeight / 3 + 29, 17, 17.0F, 15.0F, (EntityLivingBase)target);
               fr.drawStringWithShadow(((EntityPlayer)target).getGameProfile().getName(), scaledWidth / 2.0F + 40.0F, scaledHeight / 3.0F - 5.0F, Color.WHITE.getRGB());
               fr.drawStringWithShadow(ping + "", scaledWidth / 2.0F + 40.0F, scaledHeight / 3.0F + 20.0F, Color.WHITE.getRGB());
               GuiUtils.drawRoundedRect2((double)(scaledWidth / 2.0F + 40.0F), (double)(scaledHeight / 3.0F + 4.0F), (double)(scaledWidth / 2.0F + 40.0F) + 87.5D, (double)(scaledHeight / 3.0F + 16.0F), 6.0D, (new Color(0, 0, 0)).getRGB());
               GuiUtils.drawRoundedRect2((double)(scaledWidth / 2.0F + 40.0F), (double)(scaledHeight / 3.0F + 4.0F), (double)(scaledWidth / 2.0F + 40.0F) + hpPercentage * 1.25D * 70.0D, (double)(scaledHeight / 3.0F + 16.0F), 6.0D, healthcolor.getRGB());
               this.mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", ((EntityLivingBase)target).getHealth()), scaledWidth / 2.0F + 40.0F + 36.0F, scaledHeight / 3.0F + 5.0F, healthcolor.getRGB());
               GlStateManager.popMatrix();
            }
            break;
         case Milo:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               damageAsHealthBarWidth = (double)(((EntityLivingBase)target).getHealth() / ((EntityLivingBase)target).getMaxHealth());
               int n2 = scaledResolution.getScaledWidth() / 2 + 300;
               int n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 69.0F, (float)(n2 + 80), (float)n3 + 106.0F, 6.0F, (new Color(0, 0, 0, 90)).getRGB());
               scaledWidth = ((EntityLivingBase)target).getHealth();
               double healthPercentage = (double)(scaledWidth / ((EntityLivingBase)target).getMaxHealth());
               healthPercentage = 0.0F;
               if (damageAsHealthBarWidth != (double)this.lastHealth) {
                  diff = (float)(damageAsHealthBarWidth - (double)this.lastHealth);
                  healthPercentage = this.lastHealth;
                  this.lastHealth += diff / 8.0F;
               }

               Color healthcolor = Color.WHITE;
               if (damageAsHealthBarWidth * 100.0D > 75.0D) {
                  healthcolor = Color.GREEN;
               } else if (damageAsHealthBarWidth * 100.0D > 50.0D && damageAsHealthBarWidth * 100.0D < 75.0D) {
                  healthcolor = Color.YELLOW;
               } else if (damageAsHealthBarWidth * 100.0D < 50.0D && damageAsHealthBarWidth * 100.0D > 25.0D) {
                  healthcolor = Color.ORANGE;
               } else if (damageAsHealthBarWidth * 100.0D < 25.0D) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawGradientSideways((double)((float)n2 - 70.0F), (double)((float)n3 + 101.0F), (double)((float)n2 + 80.0F * healthPercentage), (double)((float)n3 + 106.0F), Color.PINK.getRGB(), Color.BLUE.getRGB());
               fr.drawString(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 37), (float)(n3 + 70), Color.GRAY.getRGB());
               if (((EntityPlayer)target).getCurrentEquippedItem() == null) {
                  String var10000 = "Nothing";
               } else {
                  ((EntityPlayer)target).getCurrentEquippedItem().getDisplayName().toString();
               }

               int x = n2 - 90;
               int y = n3 + 60;
               GL11.glPushMatrix();
               GlStateManager.translate((float)x, (float)y, 1.0F);
               GL11.glScalef(1.8F, 1.8F, 1.8F);
               GlStateManager.translate((float)(-x), (float)(-y), 1.0F);
               this.mc.fontRendererObj.drawStringWithShadow(Math.round(((EntityLivingBase)target).getHealth() / 2.0F) + " HP", (float)(x + 29), (float)(y + 12), Color.GRAY.getRGB());
               GL11.glPopMatrix();
               this.drawFace((double)(n2 - 70), (double)((int)((double)n3 + 71.8D)), 5.0F, 5.0F, 5, 5, 30, 30, 64.0F, 64.0F, (AbstractClientPlayer)target);
               GlStateManager.popMatrix();
            }
            break;
         case Moon:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 72.0F, (float)n3 + 35.0F, (float)n2 - 30.0F + 120.0F, (float)n3 + 78.0F, 6.0F, (new Color(0, 0, 0, 95)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               Gui.drawRect((double)((float)n2 - 71.0F), (double)((float)n3 + 68.0F), (double)((float)n2 - 30.0F + 119.0F), (double)((float)n3 + 71.0F), Color.BLACK.getRGB());
               Gui.drawRect((double)((float)n2 - 70.0F), (double)((float)n3 + 69.0F), (double)((float)n2 - 70.0F + 158.0F * scaledWidth), (double)((float)n3 + 70.0F), healthcolor.getRGB());
               armorValue = (float)((EntityLivingBase)target).getTotalArmorValue();
               double armorWidth = (double)armorValue / 20.0D;
               Gui.drawRect((double)((float)n2 - 71.0F), (double)((float)n3 + 73.0F), (double)((float)(n2 - 30) + 119.0F), (double)((float)n3 + 76.0F), Color.BLACK.getRGB());
               Gui.drawRect((double)((float)n2 - 70.0F), (double)((float)n3 + 74.0F), (double)((float)n2 - 70.0F) + 158.0D * armorWidth, (double)((float)n3 + 75.0F), Color.BLUE.getRGB());
               fr.drawStringWithShadow(((EntityPlayer)target).getGameProfile().getName(), (float)(n2 - 38), (float)(n3 + 35), -1);
               fr.drawStringWithShadow("Health: " + String.format("%.1f", ((EntityLivingBase)target).getHealth()), (float)(n2 - 38), (float)(n3 + 55), -1);

               try {
                  this.drawFace((double)(n2 - 70), (double)(n3 + 37), 5.0F, 5.0F, 5, 5, 30, 30, 64.0F, 64.0F, (AbstractClientPlayer)target);
               } catch (Exception var64) {
                  var64.printStackTrace();
               }

               GlStateManager.popMatrix();
            }
            break;
         case Astolfo:
            if (target instanceof EntityPlayer) {
               GlStateManager.pushMatrix();
               GlStateManager.translate(this.x, this.y, 0.0D);
               n2 = scaledResolution.getScaledWidth() / 2 + 300;
               n3 = scaledResolution.getScaledHeight() / 2 + 200;
               GuiUtils.drawRoundedRect2((float)n2 - 70.0F, (float)n3 + 35.0F, (float)n2 + 88.0F, (float)n3 + 89.0F, 6.0F, (new Color(0, 0, 0, 190)).getRGB());
               health = ((EntityLivingBase)target).getHealth();
               healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
               scaledWidth = 0.0F;
               if (healthPercentage != this.lastHealth) {
                  scaledHeight = healthPercentage - this.lastHealth;
                  scaledWidth = this.lastHealth;
                  this.lastHealth += scaledHeight / 8.0F;
               }

               healthcolor = Color.WHITE;
               if (healthPercentage * 100.0F > 75.0F) {
                  healthcolor = Color.GREEN;
               } else if (healthPercentage * 100.0F > 50.0F && healthPercentage * 100.0F < 75.0F) {
                  healthcolor = Color.YELLOW;
               } else if (healthPercentage * 100.0F < 50.0F && healthPercentage * 100.0F > 25.0F) {
                  healthcolor = Color.ORANGE;
               } else if (healthPercentage * 100.0F < 25.0F) {
                  healthcolor = Color.RED;
               }

               GuiUtils.drawRoundedRect2((double)((float)n2 - 36.0F), (double)((float)n3 + 78.0F), (double)((float)n2 - 36.0F) + 120.0D, (double)((float)n3 + 85.0F), 6.0D, GuiUtils.pulseBrightness(new Color(14, 60, 190), 2, 2).getRGB());
               if (!(healthPercentage * 100.0F > 75.0F)) {
                  GuiUtils.drawRoundedRect2((float)n2 - 36.0F, (float)n3 + 78.0F, (float)n2 - 36.0F + 126.0F * scaledWidth, (float)n3 + 85.0F, 6.0F, GuiUtils.pulseBrightness(new Color(13, 108, 244), 2, 2).getRGB());
                  GuiUtils.drawRoundedRect2((float)n2 - 36.0F, (float)n3 + 78.0F, (float)n2 - 36.0F + 120.0F * scaledWidth, (float)n3 + 85.0F, 6.0F, GuiUtils.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
               } else {
                  GuiUtils.drawRoundedRect2((float)n2 - 36.0F, (float)n3 + 78.0F, (float)n2 - 36.0F + 120.0F * scaledWidth, (float)n3 + 85.0F, 6.0F, GuiUtils.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
               }

               x1 = n2 - 50;
               i = n3 + 32;
               GL11.glPushMatrix();
               GlStateManager.translate((float)x1, (float)i, 1.0F);
               GL11.glScaled(1.1D, 1.1D, 1.1D);
               GlStateManager.translate((float)(-x1), (float)(-i), 1.0F);
               fr.drawStringWithShadow(((EntityPlayer)target).getGameProfile().getName(), (float)x1 + 13.5F, (float)i + 7.5F, -1);
               GL11.glPopMatrix();
               x = n2 - 64;
               yAdd = n3 + 40;
               GL11.glPushMatrix();
               GlStateManager.translate((float)x, (float)yAdd, 1.0F);
               GL11.glScalef(2.0F, 2.0F, 2.0F);
               GlStateManager.translate((float)(-x), (float)(-yAdd), 1.0F);
               this.mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", ((EntityLivingBase)target).getHealth() / 2.0F) + " ❤", (float)x + 13.5F, (float)yAdd + 7.5F, GuiUtils.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
               GL11.glPopMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               drawEntityOnScreen(n2 - 53, n3 + 85, 24, 25.0F, 25.0F, (EntityLivingBase)target);
               GlStateManager.popMatrix();
            }
         }
      }

   }

   public void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
      try {
         ResourceLocation skin = target.getLocationSkin();
         Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
         GL11.glEnable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Gui.drawScaledCustomSizeModalRect((int)x, (int)y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
         GL11.glDisable(3042);
      } catch (Exception var15) {
         var15.printStackTrace();
      }

   }

   public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
      GlStateManager.enableColorMaterial();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)posX, (float)posY, 50.0F);
      GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      float f = ent.renderYawOffset;
      float f1 = ent.rotationYaw;
      float f2 = ent.rotationPitch;
      float f3 = ent.prevRotationYawHead;
      float f4 = ent.rotationYawHead;
      GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
      ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
      ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
      ent.rotationYawHead = ent.rotationYaw;
      ent.prevRotationYawHead = ent.rotationYaw;
      GlStateManager.translate(0.0F, 0.0F, 0.0F);
      RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
      rendermanager.setPlayerViewY(180.0F);
      rendermanager.setRenderShadow(false);
      rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
      rendermanager.setRenderShadow(true);
      ent.renderYawOffset = f;
      ent.rotationYaw = f1;
      ent.rotationPitch = f2;
      ent.prevRotationYawHead = f3;
      ent.rotationYawHead = f4;
      GlStateManager.popMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   private boolean isValidEntity(EntityLivingBase target) {
      if (target instanceof EntityMob && !(Boolean)this.showMonsters.getValue()) {
         return false;
      } else if (target.isInvisible() && target instanceof EntityPlayer && !(Boolean)this.showInvisibles.getValue()) {
         return false;
      } else if (target instanceof EntityPlayer && !(Boolean)this.showPlayers.getValue()) {
         return false;
      } else if (target instanceof EntityGolem || target instanceof EntityVillager && !(Boolean)this.showPassives.getValue()) {
         return false;
      } else {
         return !(target instanceof EntityAnimal) || (Boolean)this.showAnimals.getValue();
      }
   }

   private int getOrCacheArmor(EntityPlayer player) {
      Integer cachedTargetArmor = (Integer)this.entityArmorCache.get(player);
      if (cachedTargetArmor == null) {
         int targetArmor = (int)Math.ceil(PlayerUtils.getTotalArmorProtection(player) / 20.0D * 100.0D);
         this.entityArmorCache.put(player, targetArmor);
         return targetArmor;
      } else {
         return cachedTargetArmor;
      }
   }

   private static int alphaToInt(float alpha, int offset) {
      return Math.min(255, (int)Math.ceil((double)(alpha * 255.0F)) + offset);
   }

   public static enum Rape {
      Normal,
      Radium,
      Informatic,
      Simple,
      Moon,
      Astolfo,
      OldAstolfo,
      MichaelXF,
      NovolineOld,
      Novoline,
      Blocky,
      Milo,
      Old;
   }

   private static enum ColorMode {
      HEALTH,
      COLOR,
      BLEND;
   }
}

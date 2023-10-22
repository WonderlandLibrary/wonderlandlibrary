package cn.hanabi.modules.modules.render;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.events.EventText;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.gui.cloudmusic.ui.MusicOverlayRenderer;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.tabgui.SubTab;
import cn.hanabi.gui.tabgui.Tab;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.world.Scaffold;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.CompassUtil;
import cn.hanabi.utils.MathUtils;
import cn.hanabi.utils.PacketHelper;
import cn.hanabi.utils.PaletteUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.animation.SmoothAnimation;
import cn.hanabi.utils.fontmanager.HanabiFonts;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.yarukon.YRenderUtil;
import me.yarukon.palette.ColorValue;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

public class HUD extends Mod {
   private static final ArrayList times = new ArrayList();
   public static Value musicPosX = new Value("HUD", "MusicPlayerX", 70.0, 0.0, 400.0, 1.0);
   public static Value musicPosY = new Value("HUD", "MusicPlayerY", 5.0, 0.0, 200.0, 1.0);
   public static Value musicPosYlyr = new Value("HUD", "MusicPlayerLyricY", 120.0, 0.0, 200.0, 1.0);
   public static Value hudMode = new Value("HUD", "HudMode", 0);
   private final HashMap potionMaxDurations = new HashMap();
   private final WaitTimer tpsTimer = new WaitTimer();
   public Value arraylist = new Value("HUD", "ArrayList", true);
   public Value arraylistfade = new Value("HUD", "Fade", false);
   public Value logo = new Value("HUD", "Logo", true);
   public Value hotbar = new Value("HUD", "Hotbar", true);
   public Value music = new Value("HUD", "MusicPlayer", true);
   public Value potion = new Value("HUD", "Potion", true);
   public Value armor = new Value("HUD", "Armor", true);
   public Value compass = new Value("HUD", "Compass", true);
   public Value noti = new Value("HUD", "Notification", true);
   public Value posDisplay = new Value("HUD", "Postion", true);
   public Value fixname = new Value("HUD", "Obfuscation Name Fix", true);
   public Value sound = new Value("HUD", "Sound", 0);
   public Value hitsound = new Value("HUD", "Hit Sound", 0);
   public static Value array = (new Value("HUD", "Array List Color", 0)).LoadValue(new String[]{"Random", "Theme", "Rainbow"});
   public static ColorValue design = new ColorValue("Design Color", 0.5F, 1.0F, 1.0F, 1.0F, false, false, 10.0F);
   public static Value rainbowspeed = new Value("HUD", "ArrayList Speed", 3.0, 1.0, 6.0, 1.0);
   public static Value offset = new Value("HUD", "RainBow Offset", 2.0, 1.0, 6.0, 1.0);
   public static Value fade = new Value("HUD", "Fade Offset", 14.0, 1.0, 20.0, 1.0);
   public float lastTPS = 20.0F;
   public float alphaAnimation = 0.0F;
   public float yAxisAnimation = 0.0F;
   public int blockCount = 0;
   SimpleDateFormat f = new SimpleDateFormat("HH:mm");
   SimpleDateFormat f2 = new SimpleDateFormat("yyyy/MM/dd");
   public CompassUtil compasslol;
   Map timerMap = new HashMap();
   boolean isLag;
   private SmoothAnimation hotbarAnimation = new SmoothAnimation(0.0, 200);

   public HUD() {
      super("HUD", Category.RENDER, true, false, 0);
      this.sound.LoadValue(new String[]{"Custom2", "Minecraft", "Custom1"});
      this.hitsound.LoadValue(new String[]{"Minecraft", "Ding", "Crack"});
      hudMode.LoadValue(new String[]{"Classic", "Simple"});
      this.compasslol = new CompassUtil(325.0F, 325.0F, 1.0F, 2, true);
      this.setState(true);
      HashMap moduleCategoryMap = new HashMap();

      Mod module;
      for(Iterator var2 = ModManager.getModules().iterator(); var2.hasNext(); ((List)moduleCategoryMap.get(module.getCategory())).add(module)) {
         module = (Mod)var2.next();
         if (!moduleCategoryMap.containsKey(module.getCategory())) {
            moduleCategoryMap.put(module.getCategory(), new ArrayList());
         }
      }

      moduleCategoryMap.entrySet().stream().sorted(Comparator.comparingInt((cat) -> {
         return ((Category)cat.getKey()).toString().hashCode();
      })).forEach((cat) -> {
         Tab tab = new Tab(((Category)cat.getKey()).toString());
         Iterator var2 = ((List)cat.getValue()).iterator();

         while(var2.hasNext()) {
            Mod module = (Mod)var2.next();
            tab.addSubTab(new SubTab(module.getName(), (subTab) -> {
               ((Mod)subTab.getObject()).setState(!((Mod)subTab.getObject()).getState());
            }, module));
         }

      });
   }

   public static int rainbow(int delay) {
      double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
      rainbowState %= 360.0;
      return Color.getHSBColor((float)(rainbowState / 360.0), 0.8F, 0.7F).getRGB();
   }

   @EventTarget
   public void onText(EventText event) {
      if (mc.thePlayer != null) {
         if ((Boolean)this.fixname.getValue()) {
            event.setText(StringUtils.replace(event.getText(), "§k", ""));
         }

      }
   }

   @EventTarget
   public void onReload(EventWorldChange e) {
   }

   public void color(int color) {
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
   }

   @EventTarget(4)
   private void render2D(EventRender2D event) {
      ScaledResolution sr = new ScaledResolution(mc);
      float width = (float)sr.getScaledWidth();
      float height = (float)sr.getScaledHeight();
      if ((Boolean)this.potion.getValueState()) {
         this.renderPotionStatus((int)width, (int)height);
      }

      if ((Boolean)this.compass.getValueState()) {
         this.renderCompass(sr);
      }

      if ((Boolean)this.armor.getValueState()) {
         int slot = 3;

         for(int xOffset = 0; slot >= 0; --slot) {
            ItemStack stack = mc.thePlayer.inventory.armorItemInSlot(slot);
            if (stack != null) {
               mc.getRenderItem().renderItemIntoGUI(stack, RenderUtil.width() / 2 + 15 - xOffset, RenderUtil.height() - 55 - ((!mc.thePlayer.isInsideOfMaterial(Material.water) || mc.thePlayer.getAir() <= 0) && !mc.thePlayer.isRidingHorse() ? 0 : 10));
               GL11.glDisable(2929);
               GL11.glScalef(0.5F, 0.5F, 0.5F);
               GL11.glScalef(2.0F, 2.0F, 2.0F);
               GL11.glEnable(2929);
               xOffset -= 18;
            }
         }
      }

      if ((Boolean)this.arraylist.getValueState()) {
         this.renderArray(sr);
      }

      if ((Boolean)this.noti.getValueState()) {
         if (mc.thePlayer.ticksExisted <= 10) {
            ClientUtil.clear();
         }

         ClientUtil.INSTANCE.drawNotifications();
      }

      if ((Boolean)this.music.getValueState()) {
         MusicOverlayRenderer.INSTANCE.renderOverlay();
      }

      if ((Boolean)this.logo.getValueState()) {
         if (hudMode.isCurrentMode("Classic")) {
            Hanabi.INSTANCE.fontManager.icon130.drawStringWithShadow(HanabiFonts.ICON_HANABI_LOGO, 15.0F, 60.0F, array.isCurrentMode("Rainbow") ? (new Color(47, 100, 253)).getRGB() : design.getColor(), 120);
         } else {
            Hanabi.INSTANCE.fontManager.usans50.drawStringWithShadow("Hanabi", 10.0F, 20.0F, (new Color(255, 255, 255, PaletteUtil.fade(new Color(Colors.WHITE.c), 1, 5).getRed())).getRGB(), 100);
            Hanabi.INSTANCE.fontManager.usans20.drawStringWithShadow("Build 3.1", 12.0F, 35.0F, (new Color(255, 255, 255, PaletteUtil.fade(new Color(Colors.WHITE.c), 1, 5).getRed())).getRGB(), 100);
         }
      }

      if ((Boolean)this.posDisplay.getValueState()) {
         String pos = "";
         if (mc.thePlayer != null) {
            BlockPos blockPos = mc.thePlayer.getPosition();
            pos = "X:" + blockPos.getX() + "   Y:" + blockPos.getY() + "   Z:" + blockPos.getZ();
            Hanabi.INSTANCE.fontManager.wqy18.drawCenteredString(pos, (float)sr.getScaledWidth() / 2.0F, 50.0F, (new Color(255, 255, 255, 180)).getRGB());
         }
      }

      if ((Boolean)this.hotbar.getValueState() && mc.getRenderViewEntity() instanceof EntityPlayer && !mc.gameSettings.hideGUI) {
         HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
         RenderUtil.drawRect(width / 2.0F - 91.0F, height - 22.0F, width / 2.0F + 90.0F, height - 2.0F, (new Color(17, 17, 17, 200)).getRGB());
         RenderUtil.drawRect((float)(sr.getScaledWidth() / 2 - 100), (float)(sr.getScaledHeight() + 230), (float)(sr.getScaledWidth() / 2 + 100), (float)(sr.getScaledHeight() + 250), (new Color(0, 0, 0)).getRGB());
         long ping = mc.getCurrentServerData() != null ? mc.getCurrentServerData().pingToServer : -1L;
         font.drawString("PING:" + (ping <= 0L ? "N/A" : ping) + "ms     FPS:" + Minecraft.getDebugFPS(), 16.0F, height - 16.0F, -1);
         String ez = "Hanabi Build 3.1 - " + Client.username;
         Hanabi.INSTANCE.fontManager.wqy18.drawString(ez, (float)(sr.getScaledWidth() - font.getStringWidth(ez) - 5), (float)(sr.getScaledHeight() - 16), -1);
         this.hotbarAnimation.set((double)(mc.thePlayer.inventory.currentItem * 20));
         float hbx = width / 2.0F - 91.0F + (float)this.hotbarAnimation.get();
         RenderUtil.drawRect(hbx, height - 2.0F, hbx + 21.0F, height, design.getColor());
         RenderHelper.enableGUIStandardItemLighting();

         for(int j = 0; j < 9; ++j) {
            int k = (int)(width / 2.0F - 90.0F + (float)(j * 20) + 2.0F);
            int l = (int)(height - 16.0F - 4.0F);
            this.customRenderHotbarItem(j, k, l, event.partialTicks, mc.thePlayer);
            ItemStack itemstack = mc.thePlayer.inventory.mainInventory[j];
            if (itemstack == null) {
               Hanabi.INSTANCE.fontManager.tahomabd_15.drawString(String.valueOf(j + 1), (float)(k + 4), (float)l, -1);
            }
         }

         GlStateManager.disableBlend();
         GlStateManager.color(1.0F, 1.0F, 1.0F);
         RenderHelper.disableStandardItemLighting();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.renderBlockCount((float)sr.getScaledWidth(), (float)sr.getScaledHeight() / 2.0F);
      }

      this.isLag = PacketHelper.getServerLagTime() != 0L;
      if (this.isLag) {
         mc.fontRendererObj.drawString(Minecraft.getMinecraft().isSingleplayer() ? "§4§lX" : "§4§" + PacketHelper.getServerLagTime(), 16, 240, -1);
      }

   }

   public void renderCompass(ScaledResolution sr) {
      CompassUtil.draw(sr);
   }

   public void customRenderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {
      GlStateManager.disableBlend();
      ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];
      if (itemstack != null) {
         float f = (float)itemstack.animationsToGo - partialTicks;
         if (f > 0.0F) {
            GlStateManager.pushMatrix();
            float f1 = 1.0F + f / 5.0F;
            GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), 0.0F);
            GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
            GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0F);
         }

         mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
         if (f > 0.0F) {
            GlStateManager.popMatrix();
         }

         mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemstack, xPos - 1, yPos);
      }

   }

   private void renderArray(ScaledResolution sr) {
      ArrayList mods = new ArrayList(ModManager.getEnabledModListHUD());
      float nextY = 3.0F + (hudMode.isCurrentMode("Simple") ? 6.0F : 0.0F);
      int lastX = false;
      HFontRenderer font = Hanabi.INSTANCE.fontManager.raleway17;
      HFontRenderer newfont = Hanabi.INSTANCE.fontManager.usans18;
      boolean simple = !hudMode.isCurrentMode("Classic");
      boolean first = true;
      Iterator var11 = mods.iterator();

      while(true) {
         int color;
         Mod module;
         do {
            do {
               do {
                  if (!var11.hasNext()) {
                     return;
                  }

                  module = (Mod)var11.next();
                  module.lastY = module.posY;
                  module.posY = nextY;
                  int base;
                  if (array.isCurrentMode("Random")) {
                     base = module.getColor();
                  } else if (array.isCurrentMode("Theme")) {
                     base = design.getColor();
                  } else {
                     base = RenderUtil.getRainbow(6000, (int)(-15.0F * nextY), (Double)rainbowspeed.getValue(), (Double)offset.getValue(), design.saturation, design.brightness);
                  }

                  if ((Boolean)this.arraylistfade.getValueState()) {
                     if (simple) {
                        color = PaletteUtil.fade(new Color(RenderUtil.reAlpha(Colors.WHITE.c, 0.75F)), (int)((nextY + 11.0F) / 11.0F), ((Double)fade.getValue()).intValue()).getRGB();
                     } else {
                        color = PaletteUtil.fade(new Color(base), (int)((nextY + 11.0F) / 11.0F), ((Double)fade.getValue()).intValue()).getRGB();
                     }
                  } else if (simple) {
                     color = (new Color(RenderUtil.reAlpha(Colors.WHITE.c, 0.75F))).getRGB();
                  } else {
                     color = base;
                  }

                  module.onRenderArray();
               } while(module.getName().equals("TargetStrafe"));
            } while(module.getCategory() == Category.RENDER);
         } while(!module.isEnabled() && module.posX <= 0.0F);

         String modName = module.getName();
         String displayName = module.getDisplayName();
         float modwidth = module.posX;
         if (!simple) {
            int var16 = (int)((float)sr.getScaledWidth() - modwidth - 3.0F);
         }

         if (simple) {
            newfont.drawStringWithShadow(modName, (float)sr.getScaledWidth() - modwidth - 11.0F, nextY + module.posYRend - 1.5F, color, 80);
         } else {
            font.drawStringWithShadow(modName, (float)sr.getScaledWidth() - modwidth - 11.0F, nextY + module.posYRend + 1.0F, color, 100);
         }

         first = false;
         int lastX = (int)((float)sr.getScaledWidth() - modwidth - 3.0F);
         if (displayName != null) {
            if (simple) {
               newfont.drawStringWithShadow(displayName, (float)sr.getScaledWidth() - modwidth + (float)newfont.getStringWidth(modName) - 8.0F, nextY + module.posYRend - 1.5F, (new Color(159, 159, 159)).getRGB(), 80);
            } else {
               font.drawStringWithShadow(displayName, (float)(sr.getScaledWidth() - 8) - modwidth + (float)font.getStringWidth(modName), nextY + module.posYRend + 1.0F, (new Color(159, 159, 159)).getRGB(), 200);
            }
         }

         if (simple) {
            nextY += 13.0F;
         } else {
            nextY += (float)font.FONT_HEIGHT;
         }
      }
   }

   public void renderBlockCount(float width, float height) {
      boolean state = ModManager.getModule("Scaffold").isEnabled();
      this.alphaAnimation = RenderUtil.getAnimationState(this.alphaAnimation, state ? 0.7F : 0.0F, 10.0F);
      this.yAxisAnimation = RenderUtil.getAnimationState(this.yAxisAnimation, state ? 0.0F : 10.0F, (float)Math.max(10.0, (double)(Math.abs(this.yAxisAnimation - (float)(state ? 0 : 10)) * 50.0F) * 0.5));
      float trueHeight = 18.0F;
      if (this.alphaAnimation > 0.2F) {
         try {
            this.blockCount = Scaffold.items.stackSize;
         } catch (Exception var9) {
            this.blockCount = 0;
         }

         String cunt = "block" + (this.blockCount > 1 ? "s" : "");
         HFontRenderer font = Hanabi.INSTANCE.fontManager.usans20;
         HFontRenderer font2 = Hanabi.INSTANCE.fontManager.usans18;
         float length = (float)(font.getStringWidth(this.blockCount + "  ") + font2.getStringWidth(cunt)) + 1.0F;
         YRenderUtil.drawRoundedRect(width / 2.0F - length / 2.0F, height + trueHeight - this.yAxisAnimation, length, 15.0F, 2.0F, RenderUtil.reAlpha(Colors.BLACK.c, this.alphaAnimation), 0.5F, RenderUtil.reAlpha(Colors.BLACK.c, this.alphaAnimation));
         this.drawArrowRect(width / 2.0F - 5.0F, height + trueHeight - 5.0F - this.yAxisAnimation, width / 2.0F + 5.0F, height + trueHeight - this.yAxisAnimation, RenderUtil.reAlpha(-16777216, this.alphaAnimation));
         font.drawString(this.blockCount + "", width / 2.0F - (length / 2.0F - 2.0F), height + trueHeight + 3.0F - this.yAxisAnimation, RenderUtil.reAlpha(Colors.WHITE.c, MathUtils.clampValue(this.alphaAnimation + 0.25F, 0.0F, 1.0F)));
         font2.drawString(cunt, width / 2.0F - (length / 2.0F - 1.0F) + (float)font.getStringWidth(this.blockCount + " "), height + trueHeight + 4.0F - this.yAxisAnimation, RenderUtil.reAlpha(Colors.WHITE.c, MathUtils.clampValue(this.alphaAnimation - 0.1F, 0.0F, 1.0F)));
      }

   }

   public void drawArrowRect(float left, float top, float right, float bottom, int color) {
      float e;
      if (left < right) {
         e = left;
         left = right;
         right = e;
      }

      if (top < bottom) {
         e = top;
         top = bottom;
         bottom = e;
      }

      float a = (float)(color >> 24 & 255) / 255.0F;
      float b = (float)(color >> 16 & 255) / 255.0F;
      float c = (float)(color >> 8 & 255) / 255.0F;
      float d = (float)(color & 255) / 255.0F;
      Tessellator tes = Tessellator.getInstance();
      WorldRenderer bufferBuilder = Tessellator.getInstance().getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(b, c, d, a);
      bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferBuilder.pos((double)(left - 5.0F), (double)bottom, 0.0).endVertex();
      bufferBuilder.pos((double)(right + 5.0F), (double)bottom, 0.0).endVertex();
      bufferBuilder.pos((double)right, (double)top, 0.0).endVertex();
      bufferBuilder.pos((double)left, (double)top, 0.0).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void renderPotionStatus(int width, int height) {
      int x = 0;
      ArrayList needRemove = new ArrayList();
      Iterator var5 = this.potionMaxDurations.entrySet().iterator();

      while(var5.hasNext()) {
         Map.Entry entry = (Map.Entry)var5.next();
         if (mc.thePlayer.getActivePotionEffect(Potion.potionTypes[(Integer)entry.getKey()]) == null) {
            needRemove.add(entry.getKey());
         }
      }

      var5 = needRemove.iterator();

      while(var5.hasNext()) {
         int id = (Integer)var5.next();
         this.potionMaxDurations.remove(id);
      }

      for(var5 = mc.thePlayer.getActivePotionEffects().iterator(); var5.hasNext(); x -= 35) {
         PotionEffect effect = (PotionEffect)var5.next();
         if (!this.potionMaxDurations.containsKey(effect.getPotionID()) || (Integer)this.potionMaxDurations.get(effect.getPotionID()) < effect.getDuration()) {
            this.potionMaxDurations.put(effect.getPotionID(), effect.getDuration());
         }

         Potion potion = Potion.potionTypes[effect.getPotionID()];
         String PType = I18n.format(potion.getName(), new Object[0]);

         int minutes;
         int seconds;
         try {
            minutes = Integer.parseInt(Potion.getDurationString(effect).split(":")[0]);
            seconds = Integer.parseInt(Potion.getDurationString(effect).split(":")[1]);
         } catch (Exception var18) {
            minutes = 0;
            seconds = 0;
         }

         double total = (double)(minutes * 60 + seconds);
         if (!this.timerMap.containsKey(potion)) {
            this.timerMap.put(potion, total);
         }

         if ((Double)this.timerMap.get(potion) == 0.0 || total > (Double)this.timerMap.get(potion)) {
            this.timerMap.replace(potion, total);
         }

         switch (effect.getAmplifier()) {
            case 0:
               PType = PType + " I";
               break;
            case 1:
               PType = PType + " II";
               break;
            case 2:
               PType = PType + " III";
               break;
            case 3:
               PType = PType + " IV";
               break;
            default:
               PType = PType + " " + (effect.getAmplifier() + 1);
         }

         int x1 = (int)((float)(width - 6) * 1.33F);
         int y1 = (int)((float)(height - 52 - mc.fontRendererObj.FONT_HEIGHT + x + 5) * 1.33F);
         float rectX = (float)(width - 120) + 110.0F * ((float)effect.getDuration() / (1.0F * (float)(Integer)this.potionMaxDurations.get(effect.getPotionID())));
         RenderUtil.drawRect((float)(width - 120), (float)(height - 60 + x), rectX, (float)(height - 30 + x), (new Color(0, 0, 0, 100)).getRGB());
         RenderUtil.drawRect(rectX, (float)(height - 60 + x), (float)(width - 10), (float)(height - 30 + x), (new Color(50, 50, 50, 100)).getRGB());
         int y;
         if (potion.hasStatusIcon()) {
            GlStateManager.pushMatrix();
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            y = potion.getStatusIconIndex();
            ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
            mc.getTextureManager().bindTexture(location);
            GlStateManager.scale(0.75, 0.75, 0.75);
            mc.ingameGUI.drawTexturedModalRect(x1 - 138, y1 + 8, y % 8 * 18, 198 + y / 8 * 18, 18, 18);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GlStateManager.popMatrix();
         }

         y = height - mc.fontRendererObj.FONT_HEIGHT + x - 38;
         HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
         font.drawString(PType.replaceAll("§.", ""), (float)width - 91.0F, (float)(y - mc.fontRendererObj.FONT_HEIGHT + 1), design.getColor());
         Hanabi.INSTANCE.fontManager.comfortaa16.drawString(Potion.getDurationString(effect).replaceAll("§.", ""), (float)width - 91.0F, (float)(y + 4), ClientUtil.reAlpha(-1, 0.8F));
      }

   }

   @EventTarget
   public void onRPacket(EventPacket event) {
      if (event.getPacket() instanceof S03PacketTimeUpdate) {
         times.add(Math.max(1000L, this.tpsTimer.getTime()));
         long timesAdded = 0L;
         if (times.size() > 5) {
            times.remove(0);
         }

         long l;
         for(Iterator var4 = times.iterator(); var4.hasNext(); timesAdded += l) {
            l = (Long)var4.next();
         }

         long roundedTps = timesAdded / (long)times.size();
         this.lastTPS = (float)(20.0 / (double)roundedTps * 1000.0);
         this.tpsTimer.reset();
      }

   }

   public static class WaitTimer {
      public long time = System.nanoTime() / 1000000L;

      public boolean hasTimeElapsed(long time, boolean reset) {
         if (this.getTime() >= time) {
            if (reset) {
               this.reset();
            }

            return true;
         } else {
            return false;
         }
      }

      public long getTime() {
         return System.nanoTime() / 1000000L - this.time;
      }

      public void reset() {
         this.time = System.nanoTime() / 1000000L;
      }
   }
}

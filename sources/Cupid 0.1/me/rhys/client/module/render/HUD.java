package me.rhys.client.module.render;

import java.awt.Color;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.event.impl.render.RenderGameOverlayEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.base.util.vec.Vec4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HUD extends Module {
  @Name("Color Type")
  public ColorMode colorMode = ColorMode.STATIC;
  
  @Name("Rainbow type")
  public RainbowType rainbowType = RainbowType.NORMAL;
  
  @Name("Background")
  public boolean background = true;
  
  @Name("Watermark")
  public boolean waterMark = true;
  
  @Name("Icon")
  public THINGGGIEMODE mode = THINGGGIEMODE.TEXT;
  
  @Name("ArrayList")
  public boolean arrayList = true;
  
  @Name("Scale")
  @Clamp(min = 0.10000000149011612D, max = 2.0D)
  public double scale = 1.0D;
  
  @Name("R")
  @Clamp(min = 0.0D, max = 255.0D)
  public int r = 52;
  
  @Name("G")
  @Clamp(min = 0.0D, max = 255.0D)
  public int g = 152;
  
  @Name("B")
  @Clamp(min = 0.0D, max = 255.0D)
  public int b = 219;
  
  @Name("Scoreboard")
  public boolean scoreboard = false;
  
  @Name("Scoreboard Y")
  @Clamp(min = 0.0D, max = 300.0D)
  public int scoreboardY = 75;
  
  @Name("Fade To R")
  @Clamp(min = 0.0D, max = 255.0D)
  public int fadeToR = 52;
  
  @Name("Fade To G")
  @Clamp(min = 0.0D, max = 255.0D)
  public int fadeToG = 152;
  
  @Name("Fade To B")
  @Clamp(min = 0.0D, max = 255.0D)
  public int fadeToB = 219;
  
  @Name("Fade Speed")
  @Clamp(min = 0.0D, max = 90.0D)
  public int fadeSpeed = 3;
  
  @Name("Font")
  public Fonts font = Fonts.RISE;
  
  public int rCopy = this.r;
  
  public int gCopy = this.g;
  
  public int bCopy = this.b;
  
  private boolean rBack;
  
  private boolean gBack;
  
  private boolean bBack;
  
  private long lastR;
  
  private long lastG;
  
  private long lastB;
  
  private long nextRandomR;
  
  private long nextRandomG;
  
  private long nextRandomB;
  
  public HUD(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    setHidden(true);
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    if (this.colorMode == ColorMode.RAINBOW && this.rainbowType == RainbowType.INCREASE) {
      if (System.currentTimeMillis() - this.lastR > this.nextRandomR) {
        this.lastR = System.currentTimeMillis();
        this.nextRandomR = (long)MathUtil.randFloat(1.0F, 100.0F);
        if (!this.rBack)
          if (this.rCopy < 255) {
            this.rCopy++;
          } else {
            this.rBack = true;
          }  
        if (this.rBack)
          if (this.rCopy > 0) {
            this.rCopy--;
          } else {
            this.rCopy = 1;
            this.rBack = false;
          }  
      } 
      if (System.currentTimeMillis() - this.lastG > this.nextRandomG) {
        this.lastG = System.currentTimeMillis();
        this.nextRandomG = (long)MathUtil.randFloat(1.0F, 100.0F);
        if (!this.gBack)
          if (this.gCopy < 255) {
            this.gCopy++;
          } else {
            this.gBack = true;
          }  
        if (this.gBack)
          if (this.gCopy > 0) {
            this.gCopy--;
          } else {
            this.gCopy = 1;
            this.gBack = false;
          }  
      } 
      if (System.currentTimeMillis() - this.lastB > this.nextRandomB) {
        this.lastB = System.currentTimeMillis();
        this.nextRandomB = (long)MathUtil.randFloat(1.0F, 100.0F);
        if (!this.bBack)
          if (this.bCopy < 255) {
            this.bCopy++;
          } else {
            this.bBack = true;
          }  
        if (this.bBack)
          if (this.bCopy > 0) {
            this.bCopy--;
          } else {
            this.bCopy = 1;
            this.bBack = false;
          }  
      } 
      return;
    } 
    this.rCopy = this.r;
    this.gCopy = this.g;
    this.bCopy = this.b;
  }
  
  @EventTarget
  void renderGameOverlay(RenderGameOverlayEvent event) {
    if (this.mc.gameSettings.showDebugInfo)
      return; 
    if (this.waterMark)
      switch (this.mode) {
        case TEXT:
          me.rhys.base.font.Fonts.INSTANCE.getApple().drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), new Vec2f(10.0F, 5.0F), 
              getColor(0, 1));
          me.rhys.base.font.Fonts.INSTANCE.getApple().drawStringWithShadow("Name: " + this.mc.thePlayer.getName(), new Vec2f(52.0F, 5.0F), 
              getColor(0, 1));
          me.rhys.base.font.Fonts.INSTANCE.getBigRise().drawStringWithShadow(Lite.MANIFEST
              .getName().toUpperCase(Locale.ROOT).charAt(0) + EnumChatFormatting.WHITE
              .toString() + Lite.MANIFEST
              .getName().substring(1), new Vec2f(10.0F, 15.0F), 
              getColor(0, 1));
          break;
        case NIGGER:
          me.rhys.base.font.Fonts.INSTANCE.getRise().drawString("Nigger" + EnumChatFormatting.BLACK
              .toString(), new Vec2f(10.0F, 15.0F), 
              getColor(0, 0));
          break;
        case LOGO:
          RenderUtil.drawImage(new ResourceLocation("Lite/hud/b1.png"), new Vec2f(5.0F, 5.0F), 125, 50);
          break;
      }  
    if (this.arrayList)
      drawArrayList(event); 
  }
  
  public static void drawImage(ResourceLocation location, Vec2f pos, int width, int height, float alpha) {
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
    Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    Gui.drawModalRectWithCustomSizedTexture((int)pos.getVecX(), (int)pos.getVecY(), 0.0F, 0.0F, width, height, width, height);
    GlStateManager.popMatrix();
  }
  
  void drawArrayList(RenderGameOverlayEvent event) {
    int margin = 5;
    Vec2f pos = new Vec2f((event.getWidth() - margin), margin / 2.0F - FontUtil.getFontHeight() + margin + 1.0F);
    AtomicReference<Vec2f> lastPosition = new AtomicReference<>(null);
    AtomicInteger index = new AtomicInteger(0);
    Lite.MODULE_FACTORY
      .getActiveModules().filter(module -> !module.isHidden())
      .sorted((o1, o2) -> (int)(FontUtil.getStringWidth(o2.getDisplayName()) - FontUtil.getStringWidth(o1.getDisplayName())))
      .forEachOrdered(module -> {
          float width = FontUtil.getStringWidth(module.getDisplayName());
          float height = FontUtil.getFontHeight();
          int color = getColor(index.get(), 25);
          Vec2f currentPos = pos.add(0.0F, height + margin).clone().add(-width, 0.0F);
          FontUtil.drawStringWithShadow(module.getDisplayName(), currentPos, color);
          lastPosition.set(currentPos);
          index.addAndGet(1);
        });
  }
  
  public int getColor(int offset, int rainbowOffset) {
    if (this.colorMode == ColorMode.RAINBOW && this.rainbowType == RainbowType.NORMAL)
      return Color.HSBtoRGB((float)(Minecraft.getSystemTime() + (10 * ((Minecraft.getMinecraft()).thePlayer.ticksExisted + offset * rainbowOffset))) % 5000.0F / 5000.0F, 1.0F, 1.0F); 
    if (this.colorMode == ColorMode.FADE) {
      int speed = 1000 * ((this.fadeSpeed > 0) ? this.fadeSpeed : 1);
      return fade((float)((System.currentTimeMillis() + (offset * 147)) % speed) / speed / 2.0F);
    } 
    return (new Color(this.rCopy, this.gCopy, this.bCopy)).getRGB();
  }
  
  public int fade(float time) {
    if (time > 1.0F)
      time = 1.0F - time % 1.0F; 
    Vec4f from = ColorUtil.getColor((new Color(this.r, this.g, this.b)).getRGB());
    Vec4f to = ColorUtil.getColor((new Color(this.fadeToR, this.fadeToG, this.fadeToB)).getRGB());
    Vec4f diff = to.clone().sub(from);
    Vec4f newColor = from.clone().add(diff.clone().mul(time));
    return ColorUtil.getColor(newColor);
  }
  
  public enum RainbowType {
    NORMAL, INCREASE;
  }
  
  public enum ColorMode {
    STATIC, RAINBOW, FADE;
  }
  
  public enum Fonts {
    MINECRAFT, APPLE, RISE;
  }
  
  public enum THINGGGIEMODE {
    TEXT, NIGGER, LOGO;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\HUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
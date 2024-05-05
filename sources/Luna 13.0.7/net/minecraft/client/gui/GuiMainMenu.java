package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.impl.gui.LunaFontRenderer;
import space.lunaclient.luna.impl.gui.alt.GuiAltManager;
import space.lunaclient.luna.impl.managers.FontManager;
import space.lunaclient.luna.util.particle.Particle;
import space.lunaclient.luna.util.particle.ParticleGenerator;

public class GuiMainMenu
  extends GuiScreen
  implements GuiYesNoCallback
{
  private static final AtomicInteger field_175373_f = new AtomicInteger(0);
  private static final Logger logger = LogManager.getLogger();
  private static final Random field_175374_h = new Random();
  private ParticleGenerator particles;
  private String splashText;
  private GuiButton buttonResetDemo;
  private int panoramaTimer;
  private DynamicTexture viewportTexture;
  private final Object field_104025_t = new Object();
  private String field_92025_p;
  private String field_146972_A;
  private String field_104024_v;
  private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
  private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
  private static ResourceLocation[] titlePanoramaPaths = { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
  private static String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
  private int field_92022_t;
  private int field_92021_u;
  private int field_92020_v;
  private int field_92019_w;
  private ResourceLocation field_110351_G;
  private GuiButton field_175372_K;
  
  public GuiMainMenu()
  {
    this.field_146972_A = field_96138_a;
    this.splashText = "missingno";
    
    ArrayList var2 = Lists.newArrayList();
    try
    {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));Throwable localThrowable3 = null;
      try
      {
        String var3;
        while ((var3 = var1.readLine()) != null)
        {
          var3 = var3.trim();
          if (!var3.isEmpty()) {
            var2.add(var3);
          }
        }
        if (!var2.isEmpty()) {
          do
          {
            this.splashText = ((String)var2.get(field_175374_h.nextInt(var2.size())));
          } while (this.splashText.hashCode() == 125780783);
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (var1 != null) {
          if (localThrowable3 != null) {
            try
            {
              var1.close();
            }
            catch (Throwable localThrowable2)
            {
              localThrowable3.addSuppressed(localThrowable2);
            }
          } else {
            var1.close();
          }
        }
      }
    }
    catch (IOException localIOException) {}
    float updateCounter = field_175374_h.nextFloat();
    this.field_92025_p = "";
    if ((!GLContext.getCapabilities().OpenGL20) && (!OpenGlHelper.areShadersSupported()))
    {
      this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
      this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
      this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
    }
  }
  
  public void updateScreen()
  {
    Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
    this.panoramaTimer += 1;
  }
  
  public boolean doesGuiPauseGame()
  {
    return false;
  }
  
  protected void keyTyped(char typedChar, int keyCode)
    throws IOException
  {}
  
  public void initGui()
  {
    Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
    this.viewportTexture = new DynamicTexture(256, 256);
    this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
    Calendar var1 = Calendar.getInstance();
    var1.setTime(new Date());
    if ((var1.get(2) + 1 == 11) && (var1.get(5) == 9)) {
      this.splashText = "Happy birthday, ez!";
    } else if ((var1.get(2) + 1 == 6) && (var1.get(5) == 1)) {
      this.splashText = "Happy birthday, Notch!";
    } else if ((var1.get(2) + 1 == 12) && (var1.get(5) == 24)) {
      this.splashText = "Merry X-mas!";
    } else if ((var1.get(2) + 1 == 1) && (var1.get(5) == 1)) {
      this.splashText = "Happy new year!";
    } else if ((var1.get(2) + 1 == 10) && (var1.get(5) == 31)) {
      this.splashText = "OOoooOOOoooo! Spooky!";
    }
    boolean var2 = true;
    int var3 = height / 4 + 48;
    if (this.mc.isDemo()) {
      addDemoButtons(var3, 24);
    } else {
      addSingleplayerMultiplayerButtons(var3, 24);
    }
    this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
    this.buttonList.add(new GuiButton(4, width / 2 + 2, var3 + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
    this.buttonList.add(new GuiButtonLanguage(5, width / 2 - 124, var3 + 72 + 12));
    synchronized (this.field_104025_t)
    {
      int field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
      int field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
      int var5 = Math.max(field_92023_s, field_92024_r);
      this.field_92022_t = ((width - var5) / 2);
      this.field_92021_u = (((GuiButton)this.buttonList.get(0)).yPosition - 24);
      this.field_92020_v = (this.field_92022_t + var5);
      this.field_92019_w = (this.field_92021_u + 24);
    }
    this.particles = new ParticleGenerator(100, width, height);
  }
  
  private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
  {
    this.buttonList.add(new GuiButton(1, width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
    this.buttonList.add(new GuiButton(2, width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
    this.buttonList.add(this.field_175372_K = new GuiButton(14, width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("Alt Manager", new Object[0])));
  }
  
  private void addDemoButtons(int p_73972_1_, int p_73972_2_)
  {
    this.buttonList.add(new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
    this.buttonList.add(this.buttonResetDemo = new GuiButton(12, width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
    ISaveFormat var3 = this.mc.getSaveLoader();
    WorldInfo var4 = var3.getWorldInfo("Demo_World");
    if (var4 == null) {
      this.buttonResetDemo.enabled = false;
    }
  }
  
  protected void actionPerformed(GuiButton button)
    throws IOException
  {
    if (button.id == 0) {
      this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
    }
    if (button.id == 5) {
      this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
    }
    if (button.id == 1) {
      this.mc.displayGuiScreen(new GuiSelectWorld(this));
    }
    if (button.id == 2) {
      this.mc.displayGuiScreen(new GuiMultiplayer(this));
    }
    if ((button.id == 14) && (this.field_175372_K.visible)) {
      this.mc.displayGuiScreen(new GuiAltManager());
    }
    if (button.id == 4) {
      this.mc.shutdown();
    }
    if (button.id == 11) {
      this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
    }
    if (button.id == 12)
    {
      ISaveFormat var2 = this.mc.getSaveLoader();
      WorldInfo var3 = var2.getWorldInfo("Demo_World");
      if (var3 != null)
      {
        GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
        this.mc.displayGuiScreen(var4);
      }
    }
  }
  
  private void switchToRealms()
  {
    RealmsBridge var1 = new RealmsBridge();
    var1.switchToRealms(this);
  }
  
  public void confirmClicked(boolean result, int id)
  {
    if ((result) && (id == 12))
    {
      ISaveFormat var6 = this.mc.getSaveLoader();
      var6.flushCache();
      var6.deleteWorldDirectory("Demo_World");
      this.mc.displayGuiScreen(this);
    }
    else if (id == 13)
    {
      if (result) {
        try
        {
          Class var3 = Class.forName("java.awt.Desktop");
          Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
          var3.getMethod("browse", new Class[] { URI.class }).invoke(var4, new Object[] { new URI(this.field_104024_v) });
        }
        catch (Throwable var5)
        {
          logger.error("Couldn't open link", var5);
        }
      }
      this.mc.displayGuiScreen(this);
    }
  }
  
  private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
  {
    Tessellator var4 = Tessellator.getInstance();
    WorldRenderer var5 = var4.getWorldRenderer();
    GlStateManager.matrixMode(5889);
    GlStateManager.pushMatrix();
    GlStateManager.loadIdentity();
    Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
    GlStateManager.matrixMode(5888);
    GlStateManager.pushMatrix();
    GlStateManager.loadIdentity();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.disableCull();
    GlStateManager.depthMask(false);
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    byte var6 = 8;
    for (int var7 = 0; var7 < var6 * var6; var7++)
    {
      GlStateManager.pushMatrix();
      float var8 = (var7 % var6 / var6 - 0.5F) / 64.0F;
      float var9 = (var7 / var6 / var6 - 0.5F) / 64.0F;
      float var10 = 0.0F;
      GlStateManager.translate(var8, var9, var10);
      GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);
      for (int var11 = 0; var11 < 6; var11++)
      {
        GlStateManager.pushMatrix();
        if (var11 == 1) {
          GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        }
        if (var11 == 2) {
          GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        }
        if (var11 == 3) {
          GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        }
        if (var11 == 4) {
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        }
        if (var11 == 5) {
          GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
        }
        this.mc.getTextureManager().bindTexture(titlePanoramaPaths[var11]);
        var5.startDrawingQuads();
        var5.func_178974_a(16777215, 255 / (var7 + 1));
        float var12 = 0.0F;
        var5.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + var12, 0.0F + var12);
        var5.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - var12, 0.0F + var12);
        var5.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - var12, 1.0F - var12);
        var5.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + var12, 1.0F - var12);
        var4.draw();
        GlStateManager.popMatrix();
      }
      GlStateManager.popMatrix();
      GlStateManager.colorMask(true, true, true, false);
    }
    var5.setTranslation(0.0D, 0.0D, 0.0D);
    GlStateManager.colorMask(true, true, true, true);
    GlStateManager.matrixMode(5889);
    GlStateManager.popMatrix();
    GlStateManager.matrixMode(5888);
    GlStateManager.popMatrix();
    GlStateManager.depthMask(true);
    GlStateManager.enableCull();
    GlStateManager.enableDepth();
  }
  
  private void rotateAndBlurSkybox(float p_73968_1_)
  {
    this.mc.getTextureManager().bindTexture(this.field_110351_G);
    GL11.glTexParameteri(3553, 10241, 9729);
    GL11.glTexParameteri(3553, 10240, 9729);
    GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.colorMask(true, true, true, false);
    Tessellator var2 = Tessellator.getInstance();
    WorldRenderer var3 = var2.getWorldRenderer();
    var3.startDrawingQuads();
    GlStateManager.disableAlpha();
    byte var4 = 3;
    for (int var5 = 0; var5 < var4; var5++)
    {
      var3.func_178960_a(1.0F, 1.0F, 1.0F, 1.0F / (var5 + 1));
      int var6 = width;
      int var7 = height;
      float var8 = (var5 - var4 / 2) / 256.0F;
      var3.addVertexWithUV(var6, var7, this.zLevel, 0.0F + var8, 1.0D);
      var3.addVertexWithUV(var6, 0.0D, this.zLevel, 1.0F + var8, 1.0D);
      var3.addVertexWithUV(0.0D, 0.0D, this.zLevel, 1.0F + var8, 0.0D);
      var3.addVertexWithUV(0.0D, var7, this.zLevel, 0.0F + var8, 0.0D);
    }
    var2.draw();
    GlStateManager.enableAlpha();
    GlStateManager.colorMask(true, true, true, true);
  }
  
  private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
  {
    this.mc.getFramebuffer().unbindFramebuffer();
    GlStateManager.viewport(0, 0, 256, 256);
    drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    rotateAndBlurSkybox(p_73971_3_);
    this.mc.getFramebuffer().bindFramebuffer(true);
    GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
    Tessellator var4 = Tessellator.getInstance();
    WorldRenderer var5 = var4.getWorldRenderer();
    var5.startDrawingQuads();
    float var6 = width > height ? 120.0F / width : 120.0F / height;
    float var7 = height * var6 / 256.0F;
    float var8 = width * var6 / 256.0F;
    var5.func_178960_a(1.0F, 1.0F, 1.0F, 1.0F);
    int var9 = width;
    int var10 = height;
    var5.addVertexWithUV(0.0D, var10, this.zLevel, 0.5F - var7, 0.5F + var8);
    var5.addVertexWithUV(var9, var10, this.zLevel, 0.5F - var7, 0.5F - var8);
    var5.addVertexWithUV(var9, 0.0D, this.zLevel, 0.5F + var7, 0.5F - var8);
    var5.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.5F + var7, 0.5F + var8);
    var4.draw();
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    GlStateManager.disableAlpha();
    renderSkybox(mouseX, mouseY, partialTicks);
    GlStateManager.enableAlpha();
    Tessellator var4 = Tessellator.getInstance();
    WorldRenderer var5 = var4.getWorldRenderer();
    drawGradientRect(0, 0, width, height, -2130706433, 16777215);
    drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
    this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
    renderBackground(width, height);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    
    String var11 = "Copyright Mojang AB. Do not distribute!";
    
    var5.func_178991_c(-1);
    GlStateManager.pushMatrix();
    GlStateManager.translate(width / 2 + 90, 70.0F, 0.0F);
    GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
    float var9 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
    var9 = var9 * 100.0F / (this.fontRendererObj.getStringWidth(this.splashText) + 32);
    GlStateManager.scale(var9, var9, var9);
    GlStateManager.popMatrix();
    
    String var10 = "Luna Hacked Client";
    FontManager.fontRendererMAIN.drawCenteredString("", GuiScreen.width / 2, GuiScreen.height / 4, -2348819);
    for (Iterator localIterator1 = this.particles.particles.iterator(); localIterator1.hasNext();)
    {
      p = (Particle)localIterator1.next();
      for (Particle p2 : this.particles.particles)
      {
        int xx = (int)(MathHelper.cos(0.1F * (p.x + p.k)) * 10.0F);
        int xx2 = (int)(MathHelper.cos(0.1F * (p2.x + p2.k)) * 10.0F);
        
        boolean mouseOver = (mouseX >= p.x + xx - 95) && (mouseY >= p.y - 90) && (mouseX <= p.x) && (mouseY <= p.y);
        if ((mouseOver) && 
          (mouseY >= p.y - 80) && (mouseX >= p2.x - 100) && (mouseY >= p2.y) && (mouseY <= p2.y + 70) && (mouseX <= p2.x))
        {
          int maxDistance = 100;
          
          int xDif = p.x - mouseX;
          int yDif = p.y - mouseY;
          int distance = (int)Math.sqrt(xDif * xDif + yDif + yDif);
          
          int xDif1 = p2.x - mouseX;
          int yDif1 = p2.y - mouseY;
          int distance2 = (int)Math.sqrt(xDif1 * xDif1 + yDif1 + yDif1);
          if ((distance < maxDistance) && (distance2 < maxDistance))
          {
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(1.5F);
            GL11.glBegin(1);
            
            GL11.glVertex2d(p.x + xx, p.y);
            GL11.glVertex2d(p2.x + xx2, p2.y);
            GL11.glEnd();
            GL11.glPopMatrix();
          }
        }
      }
    }
    Particle p;
    this.particles.drawParticles();
    
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  private static final ResourceLocation background = new ResourceLocation(Luna.BACKGROUND_MAIN);
  
  public void renderBackground(int par1, int par2)
  {
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(3008);
    this.mc.getTextureManager().bindTexture(background);
    Tessellator var3 = Tessellator.getInstance();
    var3.getWorldRenderer().startDrawingQuads();
    var3.getWorldRenderer().addVertexWithUV(0.0D, par2, -90.0D, 0.0D, 1.0D);
    var3.getWorldRenderer().addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
    var3.getWorldRenderer().addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
    var3.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
    var3.draw();
    GL11.glDepthMask(true);
    GL11.glEnable(2929);
    GL11.glEnable(3008);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    throws IOException
  {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    synchronized (this.field_104025_t)
    {
      if ((this.field_92025_p.length() > 0) && (mouseX >= this.field_92022_t) && (mouseX <= this.field_92020_v) && (mouseY >= this.field_92021_u) && (mouseY <= this.field_92019_w))
      {
        GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
        var5.disableSecurityWarning();
        this.mc.displayGuiScreen(var5);
      }
    }
  }
}

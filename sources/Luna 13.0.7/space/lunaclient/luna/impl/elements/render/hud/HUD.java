package space.lunaclient.luna.impl.elements.render.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.BooleanSetting;
import space.lunaclient.luna.api.setting.DoubleSetting;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.render.hud.structure.Armor;
import space.lunaclient.luna.impl.elements.render.hud.structure.TabGui;
import space.lunaclient.luna.impl.elements.render.hud.structure.ToggledMods;
import space.lunaclient.luna.impl.events.EventRender2D;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.gui.LunaFontRenderer;
import space.lunaclient.luna.impl.gui.notifications.ClientNotification.Type;
import space.lunaclient.luna.impl.gui.notifications.NotificationUtil;
import space.lunaclient.luna.impl.managers.EventManager;
import space.lunaclient.luna.impl.managers.FontManager;
import space.lunaclient.luna.util.ColorUtils;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.RenderUtils.R2DUtils;

@ElementInfo(name="HUD", category=Category.RENDER)
public class HUD
  extends Element
{
  public static boolean isModifiedTime;
  public static int worldTime;
  @DoubleSetting(name="Red", currentValue=255.0D, minValue=0.0D, maxValue=255.0D, onlyInt=true, locked=false)
  public static Setting red;
  @DoubleSetting(name="Green", currentValue=0.0D, minValue=0.0D, maxValue=255.0D, onlyInt=true, locked=false)
  public static Setting green;
  @DoubleSetting(name="Blue", currentValue=93.0D, minValue=0.0D, maxValue=255.0D, onlyInt=true, locked=false)
  public static Setting blue;
  @BooleanSetting(name="Logo", booleanValue=true)
  private static Setting logo;
  @DoubleSetting(name="Logo X", currentValue=-15.0D, minValue=-20.0D, maxValue=100.0D, onlyInt=true, locked=false)
  public static Setting x;
  @DoubleSetting(name="Logo X-Width", currentValue=135.0D, minValue=10.0D, maxValue=300.0D, onlyInt=true, locked=false)
  private static Setting xw;
  @DoubleSetting(name="Logo Y", currentValue=-40.0D, minValue=-55.0D, maxValue=250.0D, onlyInt=true, locked=false)
  public static Setting y;
  @DoubleSetting(name="Logo Y-Width", currentValue=200.0D, minValue=10.0D, maxValue=400.0D, onlyInt=true, locked=false)
  private static Setting yw;
  @BooleanSetting(name="ArrayList", booleanValue=true)
  public static Setting arrayList;
  private ToggledMods toggledMods = new ToggledMods();
  @BooleanSetting(name="TabGui", booleanValue=false)
  public static Setting tabGui;
  private TabGui tabGui1 = new TabGui();
  @BooleanSetting(name="Armor", booleanValue=true)
  public static Setting armor;
  private Armor armor1 = new Armor();
  @BooleanSetting(name="Rainbow", booleanValue=true)
  public static Setting rainbowArrayList;
  @BooleanSetting(name="Notifications", booleanValue=true)
  public static Setting noti;
  @BooleanSetting(name="FPS-Helper", booleanValue=false)
  public static Setting fps;
  @ModeSetting(name="Rectangle", currentOption="Slim", options={"Slim", "None"}, locked=false)
  public static Setting rectangle;
  @BooleanSetting(name="Hotbar", booleanValue=true)
  public static Setting hotBar;
  
  public HUD() {}
  
  private static void drawCustomImage(double x, double y, double xWidth, double yWidth, ResourceLocation image)
  {
    double par1 = x + xWidth;
    double par2 = y + yWidth;
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    Minecraft.getMinecraft().getTextureManager().bindTexture(image);
    Tessellator var3 = Tessellator.getInstance();
    WorldRenderer var4 = var3.getWorldRenderer();
    var4.startDrawingQuads();
    
    var4.addVertexWithUV(x, par2, 0.0D, 0.0D, 1.0D);
    var4.addVertexWithUV(par1, par2, 0.0D, 1.0D, 1.0D);
    var4.addVertexWithUV(par1, y, 0.0D, 1.0D, 0.0D);
    var4.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
    var3.draw();
    GL11.glDepthMask(true);
    GL11.glEnable(2929);
    GL11.glEnable(3008);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
  
  @EventRegister
  public void onRender(EventRender2D e)
  {
    if (((!tabGui.getValBoolean() ? 1 : 0) & (!logo.getValBoolean() ? 1 : 0)) != 0) {
      FontManager.fontRendererComfortaaReg.drawStringWithShadow(ChatFormatting.WHITE + Luna.INSTANCE.NAME + ChatFormatting.RESET + " " + Luna.INSTANCE.CURRENT_FORMAT + Luna.INSTANCE.BUILD, 2.0F, 1.0F, ColorUtils.getMainColor());
    }
    if (!(mc.currentScreen instanceof GuiChat)) {
      if (hotBar.getValBoolean())
      {
        RenderUtils.R2DUtils.drawBorderedRect(GuiIngame.animatedWidth - 11, 
          PlayerUtils.getScaledRes().getScaledHeight() - 22, GuiIngame.animatedWidth + 11, 
          PlayerUtils.getScaledRes().getScaledHeight(), 1.0F, 620756992, ColorUtils.getMainColor());
        RenderUtils.R2DUtils.drawBorderedRect(GuiIngame.animatedWidth - 11, 
          PlayerUtils.getScaledRes().getScaledHeight() - 22, GuiIngame.animatedWidth + 11, 
          PlayerUtils.getScaledRes().getScaledHeight(), 0.0F, 620756992, ColorUtils.getMainColor());
        
        FontManager.fontRendererLuna.drawStringWithShadow(Luna.INSTANCE.NAME, 5.0F, GuiMainMenu.height - 23.3F, 
          ColorUtils.getMainColor());
        
        FontManager.fontRendererBebasRegMed.drawStringWithShadow("§7Name: §8" + Minecraft.thePlayer.getName(), 55.0F, GuiMainMenu.height - 22.0F, ColorUtils.getMainColor());
        
        FontManager.fontRendererBebasRegMed.drawStringWithShadow("§7FPS: §8" + Minecraft.debugFPS, 55.0F, GuiMainMenu.height - 12.0F, 
          ColorUtils.getMainColor());
        GL11.glDisable(2848);
      }
    }
    if ((!tabGui.getValBoolean() & logo.getValBoolean())) {
      drawCustomImage(x.getValDouble(), y.getValDouble(), xw.getValDouble(), yw.getValDouble(), new ResourceLocation(Luna.LOGO_INGAME));
    }
    if (hotBar.getValBoolean())
    {
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date today = Calendar.getInstance().getTime();
      String renderDate = dateFormat.format(today);
      DateFormat dff = new SimpleDateFormat("HH:mm:ss");
      Date today2 = Calendar.getInstance().getTime();
      String renderTime = dff.format(today2);
      
      String timeString = renderTime;
      
      FontManager.fontRendererBebasRegMed.drawStringWithShadow("§7" + renderDate, (float)(PlayerUtils.getScaledRes().getScaledWidth() - FontManager.fontRendererBebasRegMed.getStringWidth("§7" + renderDate) - 4.5D), PlayerUtils.getScaledRes().getScaledHeight() - 22, ColorUtils.getMainColor());
      FontManager.fontRendererBebasRegMed.drawStringWithShadow("§7" + renderTime, (float)(PlayerUtils.getScaledRes().getScaledWidth() - FontManager.fontRendererBebasRegMed.getStringWidth("§7" + renderTime) - 4.5D), PlayerUtils.getScaledRes().getScaledHeight() - 12, ColorUtils.getMainColor());
    }
  }
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (isToggled())
    {
      Luna.loadPresence();
      if (fps.getValBoolean())
      {
        mc.gameSettings.renderDistanceChunks = 7;
        mc.gameSettings.enableVsync = true;
        mc.gameSettings.fancyGraphics = false;
        mc.gameSettings.ofFogStart = 0.8F;
        mc.gameSettings.ofAnimatedLava = 1;
        mc.gameSettings.ofSmoothFps = false;
        mc.gameSettings.particleSetting = 2;
        mc.gameSettings.ofFastMath = false;
        mc.gameSettings.touchscreen = false;
        mc.gameSettings.ofRain = 1;
        mc.gameSettings.ofClouds = 1;
        mc.gameSettings.ofCloudsHeight = 256.0F;
        mc.gameSettings.clouds = false;
        isModifiedTime = true;
        worldTime = 16022;
        if (!mc.isSingleplayer()) {
          mc.loadingScreen = null;
        }
      }
      if ((tabGui.getValBoolean() & logo.getValBoolean() | logo.getValBoolean() & tabGui.getValBoolean()))
      {
        NotificationUtil.sendClientMessage("§cLogo can't be display with TabGUI!", 3600, ClientNotification.Type.ERROR);
        logo.setValBoolean(false);
      }
    }
  }
  
  public void onEnable()
  {
    super.onEnable();
    INSTANCE.EVENT_MANAGER.register(this.toggledMods);
    INSTANCE.EVENT_MANAGER.register(this.armor1);
    INSTANCE.EVENT_MANAGER.register(this.tabGui1);
  }
  
  public void onDisable()
  {
    super.onDisable();
    INSTANCE.EVENT_MANAGER.unregister(this.toggledMods);
    INSTANCE.EVENT_MANAGER.unregister(this.armor1);
    INSTANCE.EVENT_MANAGER.unregister(this.tabGui1);
  }
}

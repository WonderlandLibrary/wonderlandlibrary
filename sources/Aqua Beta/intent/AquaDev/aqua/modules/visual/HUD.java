package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.fr.lavache.anime.Easing;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import net.aql.Lib;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class HUD
extends Module {
    Animate anim = new Animate();
    Animate anim2 = new Animate();
    Animate anim3 = new Animate();

    public HUD() {
        super("HUD", "HUD", 0, Category.Visual);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void setup() {
        Aqua.setmgr.register(new Setting("Red", (Module)this, 255.0, 0.0, 255.0, true));
        Aqua.setmgr.register(new Setting("Green", (Module)this, 26.0, 0.0, 255.0, true));
        Aqua.setmgr.register(new Setting("Blue", (Module)this, 42.0, 0.0, 255.0, true));
        Aqua.setmgr.register(new Setting("Color", (Module)this));
        Aqua.setmgr.register(new Setting("Watermarks", (Module)this, "Modern", new String[]{"Normal", "Japan", "Modern"}));
    }

    public void onEvent(Event event) {
        if (event instanceof EventRender2D && Aqua.setmgr.getSetting("HUDWatermarks").getCurrentMode().equalsIgnoreCase("Modern")) {
            int posX = 0;
            int posY = 4;
            int width = 55;
            int height = 15;
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                Blur.drawBlurred(() -> RenderUtil.drawRoundedRect2Alpha((double)(posX - 5), (double)posY, (double)width, (double)height, (double)3.0, (Color)new Color(0, 0, 0, 255)), (boolean)false);
            }
        }
        if (event instanceof EventPostRender2D) {
            this.anim2.setEase(Easing.LINEAR).setMin(0.0f).setMax(27.0f).setSpeed(45.0f).setReversed(false).update();
            this.anim.setEase(Easing.LINEAR).setMin(0.0f).setMax(13.0f).setSpeed(45.0f).setReversed(false).update();
            this.anim3.setEase(Easing.LINEAR).setMin(0.0f).setMax(27.0f).setSpeed(45.0f).setReversed(!GuiNewChat.animatedChatOpen).update();
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int posX = 4;
            int posY = (int)((float)scaledResolution.getScaledHeight() - this.anim2.getValue());
            int posY2 = (int)((float)scaledResolution.getScaledHeight() + this.anim.getValue());
            Gui.drawRect2((double)2.0, (double)((float)GuiScreen.height - this.anim3.getValue() + 12.0f), (double)(GuiScreen.width - 2), (double)GuiScreen.height, (int)new Color(0, 0, 0, 70).getRGB());
            if (GuiNewChat.getChatOpen()) {
                GuiNewChat.animatedChatOpen = true;
                Arraylist.drawGlowArray(() -> Aqua.INSTANCE.comfortaa4.drawString("UID : " + Lib.getUID() + " | Nickname : " + HUD.mc.session.getUsername() + " | BPS : " + HUD.calculateBPS(), (float)posX, (float)posY, this.getColor2()), (boolean)false);
                Aqua.INSTANCE.comfortaa4.drawString("UID : " + Lib.getUID() + " | Nickname : " + HUD.mc.session.getUsername() + " | BPS : " + HUD.calculateBPS(), (float)posX, (float)posY, this.getColor2());
                this.anim.reset();
            } else {
                GuiNewChat.animatedChatOpen = false;
                this.anim2.reset();
                Arraylist.drawGlowArray(() -> Aqua.INSTANCE.comfortaa4.drawString("UID : " + Lib.getUID() + " | Nickname : " + HUD.mc.session.getUsername() + " | BPS : " + HUD.calculateBPS(), (float)posX, (float)(posY2 - 25), this.getColor2()), (boolean)false);
                Aqua.INSTANCE.comfortaa4.drawString("UID : " + Lib.getUID() + " | Nickname : " + HUD.mc.session.getUsername() + " | BPS : " + HUD.calculateBPS(), (float)posX, (float)(posY2 - 25), this.getColor2());
            }
            this.drawLogo();
        }
    }

    public void drawLogo() {
        boolean i = false;
        int color = Aqua.setmgr.getSetting("HUDColor").getColor();
        if (Aqua.setmgr.getSetting("HUDWatermarks").getCurrentMode().equalsIgnoreCase("Modern")) {
            int posX = 0;
            int posY = 4;
            int width = 55;
            int height = 15;
            if (Aqua.moduleManager.getModuleByName("Shadow").isToggled()) {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect2Alpha((double)(posX - 5), (double)posY, (double)width, (double)height, (double)1.0, (Color)new Color(0, 0, 0, 255)), (boolean)false);
            }
            RenderUtil.drawRoundedRect2Alpha((double)(posX - 5), (double)posY, (double)width, (double)height, (double)1.0, (Color)new Color(0, 0, 0, 90));
            Aqua.INSTANCE.comfortaa4.drawString("Aqua ", 6.0f, 7.0f, -1);
            Aqua.INSTANCE.comfortaa4.drawString("B1.6", 31.0f, 7.0f, Arraylist.getGradientOffset((Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (Color)new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor()), (double)15.0).getRGB());
            Gui.drawRect2((double)posX, (double)posY, (double)2.0, (double)(height + 4), (int)Arraylist.getGradientOffset((Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (Color)new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor()), (double)15.0).getRGB());
        }
        if (Aqua.setmgr.getSetting("HUDWatermarks").getCurrentMode().equalsIgnoreCase("Normal")) {
            RenderUtil.drawRoundedRect2Alpha((double)5.0, (double)4.0, (double)Aqua.INSTANCE.comfortaa3.getWidth("Aqua | " + mc.getDebugFPS() + " | " + mc.getSession().getUsername() + 5), (double)13.0, (double)2.0, (Color)new Color(20, 20, 20, 70));
            if (Aqua.moduleManager.getModuleByName("Shadow").isToggled()) {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)5.0, (double)4.0, (double)Aqua.INSTANCE.comfortaa3.getWidth("Aqua | " + mc.getDebugFPS() + " | " + mc.getSession().getUsername() + 5), (double)13.0, (double)2.0, (int)new Color(0, 0, 0, 255).getRGB()), (boolean)false);
            }
            if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)5.0, (double)4.0, (double)Aqua.INSTANCE.comfortaa3.getWidth("Aqua | " + mc.getDebugFPS() + " | " + mc.getSession().getUsername() + 5), (double)16.0, (double)2.0, (int)new Color(0, 0, 0, 255).getRGB()), (boolean)false);
            }
            if (Aqua.moduleManager.getModuleByName("Arraylist").isToggled()) {
                Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect2Alpha((double)5.0, (double)17.0, (double)Aqua.INSTANCE.comfortaa3.getWidth("Aqua | " + mc.getDebugFPS() + " | " + mc.getSession().getUsername() + 5), (double)2.0, (double)1.0, (Color)Arraylist.getGradientOffset((Color)new Color(this.getColor2()), (Color)new Color(this.getColor2()), (double)0.0)), (boolean)false);
            }
            RenderUtil.drawRoundedRect2Alpha((double)5.0, (double)17.0, (double)Aqua.INSTANCE.comfortaa3.getWidth("Aqua | " + mc.getDebugFPS() + " | " + mc.getSession().getUsername() + 5), (double)2.0, (double)1.0, (Color)Arraylist.getGradientOffset((Color)new Color(this.getColor2()), (Color)new Color(this.getColor2()), (double)0.0));
            Aqua.INSTANCE.comfortaa3.drawString("Aqua | " + mc.getDebugFPS() + " | " + mc.getSession().getUsername(), 7.0f, 5.0f, -1);
        }
        if (Aqua.setmgr.getSetting("HUDWatermarks").getCurrentMode().equalsIgnoreCase("Japan")) {
            Arraylist.drawGlowArray(() -> Gui.drawRect((int)4, (int)2, (int)(Aqua.INSTANCE.japan.getStringWidth("Aqua") + 8), (int)21, (int)color), (boolean)false);
            Gui.drawRect((int)4, (int)2, (int)(Aqua.INSTANCE.japan.getStringWidth("Aqua") + 8), (int)21, (int)new Color(0, 0, 0, 60).getRGB());
            Blur.drawBlurred(() -> Gui.drawRect((int)4, (int)2, (int)(Aqua.INSTANCE.japan.getStringWidth("Aqua") + 8), (int)21, (int)new Color(0, 0, 0, 60).getRGB()), (boolean)false);
            Aqua.INSTANCE.japan.drawString("A", 5.0f, 1.0f, color);
            Aqua.INSTANCE.japan.drawString("qua", 23.0f, 1.0f, -1);
        }
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((double)((System.currentTimeMillis() + (long)delay) / 7L));
        return Color.getHSBColor((float)((float)((rainbowState %= 360.0) / 360.0)), (float)0.9f, (float)1.0f).getRGB();
    }

    public int getColor2() {
        try {
            return Aqua.setmgr.getSetting("HUDColor").getColor();
        }
        catch (Exception e) {
            return Color.white.getRGB();
        }
    }

    public static Color setAlpha(int color, int alpha) {
        return new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = MathHelper.clamp_int((int)alpha, (int)0, (int)255) << 24;
        color |= MathHelper.clamp_int((int)red, (int)0, (int)255) << 16;
        color |= MathHelper.clamp_int((int)green, (int)0, (int)255) << 8;
        return color |= MathHelper.clamp_int((int)blue, (int)0, (int)255);
    }

    public static double calculateBPS() {
        double calculateTicks = HUD.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null && Aqua.moduleManager.getModuleByName("Speed").isToggled() ? 30.0 : 20.0;
        double bps = Math.hypot((double)(HUD.mc.thePlayer.posX - HUD.mc.thePlayer.prevPosX), (double)(HUD.mc.thePlayer.posZ - HUD.mc.thePlayer.prevPosZ)) * (double)HUD.mc.timer.timerSpeed * calculateTicks;
        return (double)Math.round((double)(bps * 100.0)) / 100.0;
    }
}

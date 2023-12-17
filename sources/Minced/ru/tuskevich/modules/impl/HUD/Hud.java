// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.HUD;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.ITextureObject;
import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import ru.tuskevich.util.font.FontRenderer;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemAir;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.util.animations.AnimationMath;
import net.minecraft.client.gui.GuiChat;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.render.GlowUtility;
import ru.tuskevich.util.color.ColorUtility;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.font.Fonts;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.Minced;
import ru.tuskevich.util.drag.Dragging;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Hud", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.HUD)
public class Hud extends Module
{
    float upanimation;
    double anim;
    double anim2;
    public static MultiBoxSetting hudElements;
    public static ModeSetting colorMode;
    public static MultiBoxSetting arrayListElements;
    public static MultiBoxSetting arrayListLimitations;
    public static ColorSetting onecolor;
    public static ColorSetting twocolor;
    public static SliderSetting speed;
    Dragging bindsDrag;
    float yBinds;
    Dragging arrayDrag;
    public final Dragging timerDrag;
    
    public Hud() {
        this.upanimation = 0.0f;
        this.anim = 0.0;
        this.anim2 = 0.0;
        this.bindsDrag = Minced.getInstance().createDrag(this, "bindsDrag", 150.0f, 150.0f);
        this.arrayDrag = Minced.getInstance().createDrag(this, "arrayList", 5.0f, 10.0f);
        this.timerDrag = Minced.getInstance().createDrag(this, "timerDrag", 100.0f, 75.0f);
        this.add(Hud.hudElements, Hud.arrayListElements, Hud.arrayListLimitations, Hud.onecolor, Hud.twocolor);
    }
    
    @EventTarget
    public void render(final EventDisplay e) {
        if (Hud.hudElements.get(0) && !Hud.mc.gameSettings.showDebugInfo) {
            GL11.glEnable(3008);
            GL11.glEnable(3042);
            final float width = (float)Fonts.Nunito16.getStringWidth("Minced Beta | Build - 0.4");
            final int count = 0;
            final int offset = count * (Fonts.Nunito16.getFontHeight() + 2);
            final int color = getColor(offset).getRGB();
            final FontRenderer nunito16 = Fonts.Nunito16;
            final StringBuilder append = new StringBuilder().append("");
            final Minecraft mc = Hud.mc;
            final float fps = (float)nunito16.getStringWidth(append.append(Minecraft.getDebugFPS()).toString());
            final FontRenderer nunito17 = Fonts.Nunito16;
            final String format = "%.1f";
            final Object[] args = { null };
            final int n = 0;
            final Minecraft mc2 = Hud.mc;
            final double posX2 = Minecraft.player.posX;
            final Minecraft mc3 = Hud.mc;
            final double x3 = posX2 - Minecraft.player.prevPosX;
            final Minecraft mc4 = Hud.mc;
            final double posZ = Minecraft.player.posZ;
            final Minecraft mc5 = Hud.mc;
            args[n] = Math.abs(Math.hypot(x3, posZ - Minecraft.player.prevPosZ)) * 15.0;
            final float width2 = (float)nunito17.getStringWidth(String.format(format, args));
            if (Hud.arrayListElements.get(0)) {
                GlowUtility.drawGlow(6.0f, 8.0f, width + 7.0f, 13.0f, 15, ColorUtility.applyOpacity(new Color(color), 255));
            }
            RenderUtility.drawGradientRound(5.0f, 7.0f, width + 9.0f, 15.0f, 3.0f, ColorUtility.applyOpacity(getColor(270), 0.85f), getColor(0), getColor(180), getColor(90));
            RenderUtility.drawRound(6.0f, 8.0f, width + 7.0f, 13.0f, 2.0f, new Color(25, 25, 25, 255));
            Fonts.Nunito16.drawString(ChatFormatting.WHITE + "Minced Beta" + ChatFormatting.GRAY + " | " + ChatFormatting.WHITE + "Build - 0.4", 9.0f, 12.0f, ColorUtility.applyOpacity(new Color(color), 255).getRGB());
        }
        final Minecraft mc6 = Hud.mc;
        final double prevPosX = Minecraft.player.prevPosX;
        final Minecraft mc7 = Hud.mc;
        final double x4 = prevPosX - Minecraft.player.posX;
        final Minecraft mc8 = Hud.mc;
        final double prevPosZ = Minecraft.player.prevPosZ;
        final Minecraft mc9 = Hud.mc;
        final double blockpersecord = Math.hypot(x4, prevPosZ - Minecraft.player.posZ) * 20.0;
        final int height = Minced.getInstance().scaleMath.calc(e.sr.getScaledHeight());
        this.upanimation = AnimationMath.animation(this.upanimation, (float)((Hud.mc.currentScreen instanceof GuiChat) ? (height - 22) : (height - 9)), (float)(6.0 * AnimationMath.deltaTime()));
        if (Hud.hudElements.get(1) && !Hud.mc.gameSettings.showDebugInfo) {
            final ScaledResolution sr = new ScaledResolution(Hud.mc);
            final String speed = String.format("%.2f ", MoveUtility.getSpeed() * 16.0f * Hud.mc.timer.timerSpeed);
            final String fps2 = "" + Minecraft.getDebugFPS();
            final StringBuilder append2 = new StringBuilder().append("");
            final Minecraft mc10 = Hud.mc;
            final String x = append2.append(Math.round(Minecraft.player.posX)).toString();
            final StringBuilder append3 = new StringBuilder().append("");
            final Minecraft mc11 = Hud.mc;
            final String y = append3.append(Math.round(Minecraft.player.posY)).toString();
            final StringBuilder append4 = new StringBuilder().append("");
            final Minecraft mc12 = Hud.mc;
            final String z = append4.append(Math.round(Minecraft.player.posZ)).toString();
            final int color2 = getColor(280).getRGB();
            final FontRenderer nunito18 = Fonts.Nunito19;
            final StringBuilder append5 = new StringBuilder().append("          XYZ: ");
            final Minecraft mc13 = Hud.mc;
            final StringBuilder append6 = append5.append(Minecraft.player.getPosition().getX()).append(" ");
            final Minecraft mc14 = Hud.mc;
            final StringBuilder append7 = append6.append(Minecraft.player.getPosition().getY()).append(" ");
            final Minecraft mc15 = Hud.mc;
            final int coords = nunito18.getStringWidth(append7.append(Minecraft.player.getPosition().getZ()).append(" / UID: Test").toString());
            final String format2 = "%.1f";
            final Object[] args2 = { null };
            final int n2 = 0;
            final Minecraft mc16 = Hud.mc;
            final double posX3 = Minecraft.player.posX;
            final Minecraft mc17 = Hud.mc;
            final double x5 = posX3 - Minecraft.player.prevPosX;
            final Minecraft mc18 = Hud.mc;
            final double posZ2 = Minecraft.player.posZ;
            final Minecraft mc19 = Hud.mc;
            args2[n2] = Math.abs(Math.hypot(x5, posZ2 - Minecraft.player.prevPosZ)) * 15.0;
            final String bps = String.format(format2, args2);
            this.anim = AnimationMath.animation((float)this.anim, (Hud.mc.currentScreen instanceof GuiChat) ? ((float)(sr.getScaledHeight() - 7 - 13)) : ((float)(sr.getScaledHeight() - 7)), (float)AnimationMath.deltaTime());
            final FontRenderer nunito19 = Fonts.Nunito19;
            final StringBuilder append8 = new StringBuilder().append("          XYZ: ").append(ChatFormatting.WHITE);
            final Minecraft mc20 = Hud.mc;
            final StringBuilder append9 = append8.append(Minecraft.player.getPosition().getX()).append(" ");
            final Minecraft mc21 = Hud.mc;
            final StringBuilder append10 = append9.append(Minecraft.player.getPosition().getY()).append(" ");
            final Minecraft mc22 = Hud.mc;
            nunito19.drawStringWithShadow(append10.append(Minecraft.player.getPosition().getZ()).append(ChatFormatting.GRAY).append(" / ").append(ChatFormatting.RESET).append("UID: ").append(ChatFormatting.WHITE).append("Test").toString(), -27.0f, (float)this.anim - 3.0f, color2);
        }
        if (Hud.hudElements.get(2) && !Hud.mc.gameSettings.showDebugInfo) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            ScaledResolution res = e.sr;
            final List<ItemStack> armor = new ArrayList<ItemStack>();
            Hud.mc.entityRenderer.setupOverlayRendering();
            res = new ScaledResolution(Hud.mc);
            final Minecraft mc23 = Hud.mc;
            Minecraft.player.getArmorInventoryList().forEach(armor::add);
            GL11.glPushMatrix();
            final Minecraft mc24 = Hud.mc;
            if (Minecraft.player.getAir() < 300) {
                GL11.glTranslatef(0.0f, -8.0f, 0.0f);
            }
            if (!armor.isEmpty()) {
                for (int i = 0; i < 4; ++i) {
                    final ItemStack stack = armor.get(i);
                    if (!(stack.getItem() instanceof ItemAir)) {
                        final String str = String.valueOf(stack.getMaxDamage() - stack.getItemDamage());
                        RenderHelper.enableGUIStandardItemLighting();
                        this.drawItemStack(stack, res.getScaledWidth() / 2 + 30 + i * 15, res.getScaledHeight() - 55);
                        RenderHelper.disableStandardItemLighting();
                    }
                }
            }
            GL11.glPopMatrix();
            Hud.mc.entityRenderer.setupOverlayRendering();
        }
        if (Hud.hudElements.get(3) && !Hud.mc.gameSettings.showDebugInfo) {
            final List<Module> isActiveModule = Minced.getInstance().manager.getModules();
            isActiveModule.sort((f1, f2) -> Hud.arrayListElements.get(2) ? ((Fonts.Nunito15.getStringWidth(f1.name.toLowerCase()) > Fonts.Nunito15.getStringWidth(f2.name.toLowerCase())) ? -1 : 1) : ((Fonts.Nunito15.getStringWidth(f1.name) > Fonts.Nunito15.getStringWidth(f2.name)) ? -1 : 1));
            final float x2 = (float)(e.sr.getScaledWidth() - 3);
            final float y2 = 5.0f;
            int count2 = 0;
            final int width3 = Minced.getInstance().scaleMath.calc(e.sr.getScaledWidth());
            final boolean reverse = x2 > width3 / 2;
            for (final Module m : isActiveModule) {
                if (m.state && (!Hud.arrayListLimitations.get(0) || m.category != Type.RENDER)) {
                    if (Hud.arrayListLimitations.get(1) && m.bind == 0) {
                        continue;
                    }
                    final int offset2 = count2 * (Fonts.Nunito15.getFontHeight() + 2);
                    final int color3 = getColor(offset2).getRGB();
                    if (Hud.arrayListElements.get(0)) {
                        GlowUtility.drawGlow(5.0f, y2 + offset2 + 32.0f, (float)(Fonts.Nunito15.getStringWidth(Hud.arrayListElements.get(2) ? m.name.toLowerCase() : m.name) + 7), 9.0f, 10, ColorUtility.applyOpacity(new Color(color3), 100));
                        RenderUtility.drawRect(5.0, y2 + offset2 + 29.0f, Fonts.Nunito15.getStringWidth(Hud.arrayListElements.get(2) ? m.name.toLowerCase() : m.name) + 7, 9.0, new Color(20, 20, 20, 255).getRGB());
                    }
                    RenderUtility.drawRect(5.0, y2 + offset2 + 29.0f, Fonts.Nunito15.getStringWidth(Hud.arrayListElements.get(2) ? m.name.toLowerCase() : m.name) + 7, 9.0, new Color(20, 20, 20, 200).getRGB());
                    RenderUtility.drawRect(5.0, offset2 + 34, 2.0, 9.0, color3);
                    Fonts.Nunito15.drawString(Hud.arrayListElements.get(2) ? m.name.toLowerCase() : m.name, 9.5f, y2 + offset2 + 31.0f, color3);
                    if (Hud.arrayListElements.get(1)) {
                        GlowUtility.drawGlow(7.0f, y2 + offset2 + 31.0f, 0.75f, 6.0f, 4, ColorUtility.applyOpacity(new Color(color3), 255));
                    }
                    ++count2;
                }
            }
            this.arrayDrag.setHeight((float)(count2 * (Fonts.Nunito15.getFontHeight() + 4)));
            this.arrayDrag.setWidth((float)Fonts.Nunito15.getStringWidth(Minced.getInstance().manager.list.get(0).name));
        }
        if (Hud.hudElements.get(5) && !Hud.mc.gameSettings.showDebugInfo) {
            this.yBinds = Hud.sr.getScaledHeight() / 2.0f + 2.0f;
            this.bindsDrag.setWidth(84.0f);
            this.bindsDrag.setHeight(this.yBinds);
            final float posX = (float)(int)this.bindsDrag.getX();
            final float posY = (float)(int)this.bindsDrag.getY();
            for (final Module module : Minced.instance.manager.getModules()) {
                if (module.state && module.bind != 0 && !module.name.equals("ClickGUI")) {
                    RenderUtility.drawRound(posX + 6.5f, posY + this.yBinds, 83.0f, 10.7f, 0.0f, new Color(21, 21, 21, 200));
                    final String state = Keyboard.getKeyName(module.bind);
                    Fonts.Nunito12.drawStringWithShadow(module.name.toLowerCase(), posX + 8.0f, posY + this.yBinds + 5.5f, -1);
                    Fonts.Nunito12.drawStringWithShadow("[" + state + "]", posX + 75.0f - Fonts.Nunito12.getStringWidth(state) + 8.0f, posY + this.yBinds + 5.5f, -1);
                    this.yBinds += 12.0f;
                }
            }
            if (Hud.arrayListElements.get(0)) {
                GlowUtility.drawGlow(posX + 6.0f, posY + Hud.sr.getScaledHeight() / 2.0f - 8.0f, 84.0f, 10.0f, 10, ColorUtility.applyOpacity(getColor(270), 0.85f));
            }
            RenderUtility.drawGradientRound(posX + 5.5f, posY + Hud.sr.getScaledHeight() / 2.0f - 8.5f, 85.0f, 11.0f, 3.0f, ColorUtility.applyOpacity(getColor(270), 0.85f), getColor(0), getColor(180), getColor(90));
            RenderUtility.drawRound(posX + 6.0f, posY + Hud.sr.getScaledHeight() / 2.0f - 8.0f, 84.0f, 10.0f, 3.0f, new Color(25, 25, 25, 255));
            Fonts.icons20.drawString("n", posX + Fonts.icons20.getStringWidth("n") - 2.0f, posY + Hud.sr.getScaledHeight() / 2.0f - 5.25f, Hud.onecolor.getColorValue());
            Fonts.Nunito13.drawString("KeyBinds", posX + 6.0f + Fonts.Nunito12.getStringWidth("keybinds"), posY + Hud.sr.getScaledHeight() / 2.0f - 4.3f, new Color(255, 255, 255).getRGB());
        }
    }
    
    public static Color getColor(final int index) {
        final String mode = Hud.colorMode.getOptions();
        if (mode.equalsIgnoreCase("Fade")) {
            return new Color(ColorUtility.fade(4, index * 7, Hud.onecolor.getColorValueColor(), 1.0f).getRGB());
        }
        if (mode.equalsIgnoreCase("Astolfo")) {
            return ColorUtility.rainbow(10, index, 0.5f, 1.0f, 1.0f);
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            return ColorUtility.TwoColorEffect(Hud.onecolor.getColorValueColor(), Hud.twocolor.getColorValueColor(), Math.abs(System.currentTimeMillis() / 15.0) / 100.0 + 3.0 * index / 10.0 - 15.0);
        }
        if (mode.equalsIgnoreCase("Rainbow-Fade")) {
            return ColorUtility.rainbow(6, index * 2, 0.4f, 1.0f, 1.0f);
        }
        return Color.WHITE;
    }
    
    private void drawItemStack(final ItemStack stack, final double x, final double y) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0.0);
        Hud.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        Hud.mc.getRenderItem().renderItemOverlayIntoGUI(Hud.mc.standardGalacticFontRenderer, stack, 0.0, 0.0, null);
        GL11.glPopMatrix();
    }
    
    public static BufferedImage parseBufferedImage(final ITextureObject ito) throws Exception {
        if (ito instanceof ThreadDownloadImageData) {
            final BufferedImage bi = new BufferedImage(64, 64, 2);
            final ThreadDownloadImageData t = (ThreadDownloadImageData)ito;
            return t.imageBuffer.cache();
        }
        if (ito instanceof SimpleTexture) {
            final SimpleTexture st = (SimpleTexture)ito;
            return TextureUtil.readBufferedImage(Hud.mc.renderEngine.resourceManager.getResource(st.textureLocation).getInputStream());
        }
        return null;
    }
    
    public static Color astolfo(final float yDist, final float yTotal, final float saturation, final float speedt) {
        float speed;
        float hue;
        for (speed = 1800.0f, hue = System.currentTimeMillis() % (int)speed + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.getHSBColor(hue, saturation, 1.0f);
    }
    
    static {
        Hud.hudElements = new MultiBoxSetting("Elements ", new String[] { "Watermark", "Client info", "Armor info", "Arraylist", "Timer Indicator", "KeyBinds" });
        Hud.colorMode = new ModeSetting("Client Color", "Rainbow", () -> Hud.hudElements.get(3), new String[] { "Astolfo", "Rainbow", "Rainbow-Fade", "Fade" });
        Hud.arrayListElements = new MultiBoxSetting("List Elements", new String[] { "Shadow", "Glow", "Lower Case" }, () -> Hud.hudElements.get(3));
        Hud.arrayListLimitations = new MultiBoxSetting("List Limitations", new String[] { "Hide Render", "Only Bounds" }, () -> Hud.hudElements.get(3));
        Hud.onecolor = new ColorSetting("Color Fade/Rainbow", new Color(255, 0, 0).getRGB());
        Hud.twocolor = new ColorSetting("Color Fade/Rainbow(2)", new Color(255, 255, 255).getRGB());
        Hud.speed = new SliderSetting("Color Speed", 5.0f, 1.0f, 9.9f, 1.0f, () -> Hud.hudElements.get(3));
    }
}

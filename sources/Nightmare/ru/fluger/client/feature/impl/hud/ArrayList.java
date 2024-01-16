// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.render.AnimationHelper;
import java.util.Comparator;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class ArrayList extends Feature
{
    public float scale;
    public static ListSetting colorList;
    public static BooleanSetting background;
    public static ListSetting backGroundMode;
    public static ListSetting backGroundColorMode;
    public NumberSetting offsetY;
    public static NumberSetting fontSize;
    public NumberSetting fontY;
    public NumberSetting fontX;
    public ColorSetting backGroundColor;
    public BooleanSetting rightBorder;
    public BooleanSetting onlyBinds;
    public BooleanSetting glow;
    public ColorSetting glowColor;
    public NumberSetting glowRadius;
    public NumberSetting glowAlpha;
    public static ColorSetting oneColor;
    public static ColorSetting twoColor;
    public BooleanSetting noVisualModules;
    
    public ArrayList() {
        super("ArrayList", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0441\u043f\u0438\u0441\u043e\u043a \u0432\u043a\u043b\u044e\u0447\u0430\u043d\u043d\u044b\u0445 \u043c\u043e\u0434\u0443\u043b\u0435\u0439 \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435", Type.Hud);
        this.scale = 2.0f;
        this.offsetY = new NumberSetting("Offset Y", 10.0f, 3.0f, 10.0f, 0.01f, () -> true);
        this.fontY = new NumberSetting("Font Y", -2.0f, -2.0f, 10.0f, 0.01f, () -> true);
        this.fontX = new NumberSetting("Font X", 1.38f, -2.0f, 10.0f, 0.01f, () -> true);
        this.backGroundColor = new ColorSetting("Color", new Color(17, 17, 17, 180).getRGB(), () -> ArrayList.background.getCurrentValue() && ArrayList.backGroundColorMode.currentMode.equals("Custom"));
        this.rightBorder = new BooleanSetting("Right Border", true, () -> true);
        this.onlyBinds = new BooleanSetting("Only Binds", false, () -> true);
        this.glow = new BooleanSetting("Glow", false, () -> true);
        this.glowColor = new ColorSetting("Glow Color", new Color(16777215).getRGB(), () -> this.glow.getCurrentValue());
        this.glowRadius = new NumberSetting("Glow Radius", 10.0f, 0.0f, 50.0f, 1.0f, () -> this.glow.getCurrentValue());
        this.glowAlpha = new NumberSetting("Glow Alpha", 80.0f, 30.0f, 255.0f, 1.0f, () -> this.glow.getCurrentValue());
        this.noVisualModules = new BooleanSetting("No Visual Modules", false, () -> true);
        this.addSettings(ArrayList.colorList, ArrayList.oneColor, ArrayList.twoColor, this.onlyBinds, this.noVisualModules, ArrayList.background, ArrayList.backGroundMode, ArrayList.backGroundColorMode, this.backGroundColor, this.rightBorder, this.glow, this.glowRadius, this.glowColor, this.glowAlpha, this.offsetY, ArrayList.fontSize, this.fontY, this.fontX);
    }
    
    @EventTarget
    public void Event2D(final EventRender2D event) {
        if (!this.getState()) {
            return;
        }
        final List<Feature> activeModules = Fluger.instance.featureManager.getFeatureList();
        activeModules.sort(Comparator.comparingDouble(s -> -ClientHelper.getFontRender().getStringWidth(s.getSuffix())));
        final float displayWidth = event.getResolution().a() * (event.getResolution().e() / 2.0f);
        float yPotionOffset = 2.0f;
        for (final va potionEffect : ArrayList.mc.h.ca()) {
            if (potionEffect.a().i()) {
                yPotionOffset = 30.0f;
            }
            if (potionEffect.a().e()) {
                yPotionOffset = 60.0f;
            }
        }
        int y = (int)(5.0f + yPotionOffset);
        int yTotal = 0;
        for (int i = 0; i < Fluger.instance.featureManager.getFeatureList().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }
        for (final Feature module : activeModules) {
            module.animYto = AnimationHelper.Move(module.animYto, (float)(module.getState() ? 1 : 0), (float)(6.5 * Fluger.deltaTime()), (float)(6.5 * Fluger.deltaTime()), (float)Fluger.deltaTime());
            if (module.animYto > 0.01f) {
                if (module.getSuffix().equals("ClickGui") || (this.noVisualModules.getCurrentValue() && module.getType() == Type.Visuals)) {
                    continue;
                }
                if (this.onlyBinds.getCurrentValue() && module.getBind() == 0) {
                    continue;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(1.0, (double)y, 1.0);
                GL11.glScaled(1.0, (double)module.animYto, 1.0);
                GL11.glTranslated(-1.0, (double)(-y), 1.0);
                if (ArrayList.background.getCurrentValue() && ArrayList.backGroundMode.currentMode.equals("Shadow")) {
                    RenderHelper.drawBlurredShadow(displayWidth - ClientHelper.getFontRender().getStringWidth(module.getSuffix()) - 3.0f, y + this.offsetY.getCurrentValue(), displayWidth, 9.0f, 8, ArrayList.backGroundColorMode.currentMode.equals("Client") ? RenderHelper.injectAlpha(ClientHelper.getClientColor((float)y, (float)yTotal, 5), 90) : new Color(this.backGroundColor.getColor()));
                }
                if (ArrayList.background.getCurrentValue() && ArrayList.backGroundMode.currentMode.equals("Default")) {
                    RectHelper.drawRect(displayWidth - ClientHelper.getFontRender().getStringWidth(module.getSuffix()) - 4.5f, y, displayWidth, y + this.offsetY.getCurrentValue(), ArrayList.backGroundColorMode.currentMode.equals("Client") ? RenderHelper.injectAlpha(ClientHelper.getClientColor((float)y, (float)yTotal, 5), 90).getRGB() : this.backGroundColor.getColor());
                }
                if (this.rightBorder.getCurrentValue()) {
                    RectHelper.drawRect(displayWidth - 1.0f, y, displayWidth, y + this.offsetY.getCurrentValue(), ClientHelper.getClientColor((float)y, (float)yTotal, 5).getRGB());
                }
                final String modeArrayFont = ClientFont.font.getOptions();
                final float f = modeArrayFont.equalsIgnoreCase("Verdana") ? 0.5f : (modeArrayFont.equalsIgnoreCase("Bebas Book") ? 2.5f : (modeArrayFont.equalsIgnoreCase("Kollektif") ? 0.9f : (modeArrayFont.equalsIgnoreCase("Product Sans") ? 0.5f : (modeArrayFont.equalsIgnoreCase("RaleWay") ? 0.3f : (modeArrayFont.equalsIgnoreCase("LucidaConsole") ? 3.0f : (modeArrayFont.equalsIgnoreCase("Lato") ? 1.2f : (modeArrayFont.equalsIgnoreCase("Open Sans") ? 0.5f : (modeArrayFont.equalsIgnoreCase("SF UI") ? 1.3f : 2.0f))))))));
                if (!this.glow.getCurrentValue()) {
                    ClientHelper.getFontRender().drawString(module.getSuffix(), displayWidth - ClientHelper.getFontRender().getStringWidth(module.getSuffix()) - this.fontX.getCurrentValue() - (this.rightBorder.getCurrentValue() ? 2.0f : this.fontX.getCurrentValue()), y + ClientHelper.getFontRender().getFontHeight() - this.fontY.getCurrentValue() - f, ClientHelper.getClientColor((float)y, (float)yTotal, 5).getRGB());
                }
                else {
                    ClientHelper.getFontRender().drawBlurredString(module.getSuffix(), displayWidth - ClientHelper.getFontRender().getStringWidth(module.getSuffix()) - this.fontX.getCurrentValue() - (this.rightBorder.getCurrentValue() ? 2.0f : this.fontX.getCurrentValue()), y + ClientHelper.getFontRender().getFontHeight() - this.fontY.getCurrentValue() - f, (int)this.glowRadius.getCurrentValue(), RenderHelper.injectAlpha(new Color(this.glowColor.getColor()), (int)this.glowAlpha.getCurrentValue()), ClientHelper.getClientColor((float)y, (float)yTotal, 9).getRGB());
                }
                y += (int)(this.offsetY.getCurrentValue() * module.animYto);
                GL11.glPopMatrix();
            }
        }
    }
    
    static {
        ArrayList.colorList = new ListSetting("ArrayList Color", "Fade", () -> true, new String[] { "Astolfo", "Rainbow", "Fade", "Custom" });
        ArrayList.background = new BooleanSetting("Background", true, () -> true);
        ArrayList.backGroundMode = new ListSetting("Background Mode", "Default", () -> ArrayList.background.getCurrentValue(), new String[] { "Default", "Shadow" });
        ArrayList.backGroundColorMode = new ListSetting("Background Color", "Custom", () -> ArrayList.background.getCurrentValue(), new String[] { "Custom", "Client" });
        ArrayList.fontSize = new NumberSetting("Font Size", 14.0f, 14.0f, 19.0f, 1.0f, () -> ClientFont.font.currentMode.equals("Ubuntu"));
        ArrayList.oneColor = new ColorSetting("One Color", new Color(42080).getRGB(), () -> ArrayList.colorList.currentMode.equals("Custom") || ArrayList.colorList.currentMode.equals("Fade"));
        ArrayList.twoColor = new ColorSetting("Two Color", new Color(91178).getRGB(), () -> ArrayList.colorList.currentMode.equals("Custom"));
    }
}

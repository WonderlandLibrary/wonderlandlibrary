package me.aquavit.liquidsense.ui.client.clickgui.neverlose.module;

import me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud.HUD;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.implvalue.*;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.ui.font.GameFontRenderer;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class CustomModule extends MinecraftInstance {
    public float positionX;
    public float positionY;
    public int mouseX;
    public int mouseY;

    public Module module;
    public Main main;

    public int list = 0;
    private List<? extends Value<?>> values = null;
    public int valuePosY = 0;

    public boolean MiidelKey = false;

    public CustomModule(float x, float y, int mX, int mY, Module moduleclass, Main mainclass) {
        this.positionX = x;
        this.positionY = y;
        this.mouseX = mX;
        this.mouseY = mY;
        this.module = moduleclass;
        this.main = mainclass;
    }

    public void drawModule() {
        boolean isClick = main.hoverConfig(Impl.coordinateX + 103f, Impl.coordinateY + 60f, Impl.coordinateX + 455f, Impl.coordinateY + 335f, mouseX, mouseY, false);

        if (main.hoverConfig(positionX, positionY, positionX + 173f, positionY + 20f + (20 * (module.getValues().size() + module.getOutvalue())), mouseX, mouseY, false) && isClick) {
            if (!HUD.isdrag) Fonts.font18.drawString(module.getDescription(), Impl.coordinateX + 105, Impl.coordinateY + 50, -1);
            if (Mouse.isButtonDown(2) && !main.midclick) {
                Impl.openmidmanger = !Impl.openmidmanger;
                Impl.midmangermodule = module;
                if (Impl.openmidmanger) {
                    Impl.midmangerPositionX = (int) (mouseX - Impl.coordinateX);
                    Impl.midmangerPositionY = (int) (mouseY - Impl.coordinateY);
                    Impl.midmangerSetnameString = module.getArrayListName();
                }
            }
        }

        GlStateManager.pushMatrix();
        RenderUtils.makeScissorBox((int)Impl.coordinateX + 103.0f, (int)Impl.coordinateY + 60.0f,
                (int)Impl.coordinateX + 455.0f, (int)Impl.coordinateY + 335.0f);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        module.getClickAnimation().translate(module.getState() ? 10.0f : 0.0f, 0.0f);
        if (main.hovertoFloatL(positionX, positionY + 3,
                positionX + 173f, positionY + 15f, mouseX, mouseY, true) && isClick) module.setState(!module.getState());

        Color backgroundColor;
        Color barColor;
        Color textColor;
        Color buttonColor;
        Color circleColor;

        switch (Impl.hue) {
            case "white":
                backgroundColor = new Color(245, 245, 245);
                barColor = new Color(213, 213, 213);
                textColor = module.getState() ? new Color(1, 1, 1) : new Color(90, 90, 90);
                buttonColor = module.getState() ? new Color(0,120,194) : new Color(230,230,230);
                circleColor = Color.WHITE;
                break;
            case "black":
                backgroundColor = new Color(11, 11, 14);
                barColor = new Color(29, 29, 29);
                textColor = module.getState() ? new Color(-1) : new Color(175, 175 ,175);
                buttonColor = module.getState() ? new Color(3, 23, 46) : new Color(7, 19, 31);
                circleColor = module.getState() ? new Color(3, 168, 245) : new Color(74, 87, 97);
                break;
            default:
                backgroundColor = new Color(10, 22, 34);
                barColor = new Color(14, 26, 38);
                textColor = module.getState() ? new Color(-1) : new Color(175, 175 ,175);
                buttonColor = module.getState() ? new Color(3, 23, 46) : new Color(7, 19, 31);
                circleColor = module.getState() ? new Color(3, 168, 245) : new Color(74, 87, 97);
                break;
        }

        RenderUtils.drawRect(positionX, positionY, positionX + 173f,
                positionY + 20f + (20 * module.getValues().size() + 12 * module.getOutvalue()),
                backgroundColor);
        RenderUtils.drawRect(positionX + 3f, positionY + 17f, positionX + 170f, positionY + 18f, barColor);

        Fonts.font17.drawString(module.getName(), positionX + 6, positionY + 7, textColor.getRGB());
        RenderUtils.drawNLRect(positionX + 153, positionY + 6, positionX + 167f, positionY + 12f, 2.1f, buttonColor.getRGB());
        RenderUtils.drawFullCircle(positionX + 155 + module.getClickAnimation().getX(), positionY + 9, 3.5f, 0f, circleColor);

        List<Value<?>> moduleValues = module.getValues();
        module.setOutvalue(0);

        if (!moduleValues.isEmpty()) {
            valuePosY = 0;

            module.getOpenValue().translate(0.0f, 20.0f);
            for (Value<?> value : module.getValues()) {
                if (value instanceof BoolValue) {
                    BooleanValueDraw booleanValuedraw = new BooleanValueDraw(this, value);
                    booleanValuedraw.setClick(isClick);
                    booleanValuedraw.draw();
                } else if (value instanceof ListValue) {
                    ListValueDraw listValuedraw = new ListValueDraw(this, value);
                    listValuedraw.setClick(isClick);
                    listValuedraw.draw();
                } else if (value instanceof MultiBoolValue) {
                    MultiBoolValueDraw multiBoolValuedraw = new MultiBoolValueDraw(this, value);
                    multiBoolValuedraw.setClick(isClick);
                    multiBoolValuedraw.draw();
                } else if (value instanceof IntegerValue) {
                    IntegerValueDraw integerValuedraw = new IntegerValueDraw(this, value);
                    integerValuedraw.setClick(isClick);
                    integerValuedraw.draw();
                } else if (value instanceof FloatValue) {
                    FloatValueDraw floatValuedraw = new FloatValueDraw(this, value);
                    floatValuedraw.setClick(isClick);
                    floatValuedraw.draw();
                } else if (value instanceof FontValue){
                    List<FontRenderer> fonts = Fonts.getFonts();
                    String displayString = "Font: Unknown";

                    if (((FontValue)value).get() instanceof GameFontRenderer) {
                        GameFontRenderer liquidFontRenderer = (GameFontRenderer)((FontValue)value).get();
                        displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " +
                                liquidFontRenderer.getDefaultFont().getFont().getSize();
                    } else if (((FontValue)value).get() == Fonts.minecraftFont) {
                        displayString = "Font: Minecraft";
                    } else {
                        Object[] objects = Fonts.getFontDetails(((FontValue)value).get());
                        if (objects != null) {
                            displayString = objects[0].toString() + (objects[1] instanceof Integer && (int) objects[1] != -1 ? " - " + objects[1] : "");
                        }
                    }

                    if (main.hovertoFloatL(positionX + 8f, positionY + 22f + valuePosY,
                            positionX + 120f, positionY + 34f + valuePosY, mouseX, mouseY, true) && isClick) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 5f));

                        for (int i = 0; i < fonts.size(); ++i) {
                            FontRenderer font = fonts.get(i);
                            if (font != ((FontValue)value).get()) continue;
                            if (++i >= fonts.size()) {
                                i = 0;
                            }
                            ((FontValue)value).set(fonts.get(i));
                            break;
                        }

                    }

                    if (main.hovertoFloatR(positionX + 8f, positionY + 22f + valuePosY, positionX + 120f, positionY + 34f + valuePosY,
                            mouseX, mouseY, true) && isClick && module.getShowSettings()) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 5f));

                        for (int i = fonts.size() - 1; i >= 0; --i) {
                            FontRenderer font = fonts.get(i);
                            if (font != ((FontValue)value).get()) continue;
                            if (--i >= fonts.size()) i = 0;
                            if (i < 0) i = fonts.size() - 1;
                            ((FontValue)value).set(fonts.get(i));
                            break;
                        }
                    }

                    main.drawText(displayString, 100, Fonts.font15,positionX + 8, positionY + 25 + valuePosY, new Color(175, 175, 175).getRGB());
                    valuePosY += (int)module.getOpenValue().getY();
                } else {
                    main.drawText(ColorUtils.translateAlternateColorCodes(value.name + " : " + value.get()), 45, Fonts.font17, positionX + 6, positionY + 27 + valuePosY, new Color(175, 175, 175).getRGB());
                    valuePosY += (int)module.getOpenValue().getY();
                }
            }
        }


        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
    }
}

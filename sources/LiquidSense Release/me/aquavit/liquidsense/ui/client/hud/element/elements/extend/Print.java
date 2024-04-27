package me.aquavit.liquidsense.ui.client.hud.element.elements.extend;

import me.aquavit.liquidsense.utils.render.Translate;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class Print {

    public int size = 0;
    public int index = 0;
    private final float timer;
    public float x;
    public float y;
    public int textLength;
    public FadeState fadeState;
    public Type type;
    public String message;
    public boolean removeing;
    public Translate translate;
    public Translate removeingtranslate;
    private float stay;
    public String typemsg;

    public Print(String Message, float Timer, Type T) {
        this.message = Message;
        this.timer = Timer;
        removeing = false;
        fadeState = FadeState.IN;
        type = T;
        translate = new Translate(0f, 0f);
        removeingtranslate = new Translate(0f, 0f);
    }

    public void drawPrint() {

        switch (type) {
            case state: {
                typemsg = "V";
                break;
            }
            case error: {
                typemsg = "U";
                break;
            }
            case info: {
                typemsg = "M";
                break;
            }
            case success: {
                typemsg = "T";
                break;
            }
        }

        textLength = 60;
        float width = textLength + 8F;

        if (150 - removeingtranslate.getX() > 30) {
            GlStateManager.pushMatrix();
            GlStateManager.resetColor();
            if (!message.isEmpty() && type != Type.none) {
                RenderUtils.drawGradientSideway(-width + (Fonts.csgo35.getStringWidth(typemsg) + Fonts.font15.getStringWidth(message) + 10f), y - 5, -width - 36, y - 16f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, (150 - (int) removeingtranslate.getX())).getRGB());
                switch (type){
                    case info:
                        Fonts.csgo35.drawString(typemsg, -width - 29, y - 12f, new Color(0, 131, 193, (150 - (int) removeingtranslate.getX())).getRGB());
                        Fonts.font15.drawString(message, -width - 29 + (Fonts.csgo35.getStringWidth(typemsg) * 2), y - 12f, new Color(255, 255, 255, (150 - (int) removeingtranslate.getX())).getRGB());
                        break;
                    case error:
                    case success:
                        Fonts.csgo35.drawString(typemsg, -width - 32, y - 13f, new Color(0, 131, 193, (150 - (int) removeingtranslate.getX())).getRGB());
                        Fonts.font15.drawString(message, -width - 32 + Fonts.csgo35.getStringWidth(typemsg), y - 12f, new Color(255, 255, 255, (150 - (int) removeingtranslate.getX())).getRGB());
                        break;
                    case state:
                        Fonts.csgo35.drawString(typemsg, -width - 32, y - 12f, new Color(0, 131, 193, (150 - (int) removeingtranslate.getX())).getRGB());
                        Fonts.font15.drawString(message, -width - 32 + Fonts.csgo35.getStringWidth(typemsg), y - 12f, new Color(255, 255, 255, (150 - (int) removeingtranslate.getX())).getRGB());
                        break;
                }
            }
            GlStateManager.popMatrix();
        }

        switch (fadeState) {
            case IN: {
                size = size + 1;
                stay = timer;
                fadeState = FadeState.STAY;
                break;
            }
            case STAY: {
                if (stay > 0) {
                    stay -= RenderUtils.deltaTime;
                } else {
                    fadeState = FadeState.OUT;
                }
                break;
            }
            case OUT: {
                removeing = index == 0 || removeingtranslate.getX() > 0;
                if (removeing) {
                    removeingtranslate.translate(150f, 0f, 1.0);
                }
                if (150 - removeingtranslate.getX() <= 1) {
                    fadeState = FadeState.END;
                }
                break;
            }
            case END: {
                break;
            }
        }
    }
}


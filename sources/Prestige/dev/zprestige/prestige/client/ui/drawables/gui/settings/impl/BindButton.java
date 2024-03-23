package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.BindSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Locale;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.util.math.MatrixStack;

public class BindButton extends SettingsDrawable {
    public BindSetting key;
    public float anim;
    public boolean listening;
    public long currentTime;

    public BindButton(BindSetting bindSetting, float f, float f2, float f3, float f4) {
        super(bindSetting, f, f2, f3, f4);
        key = bindSetting;
        anim = 0.3f;
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        anim = MathUtil.interpolate(anim, isInsideCross(n, n2) ? 0.7f : 0.3f, Interface.getDeltaTime() * 0.005f);
        float f7 = 0.95f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f7, f7, f7);
        font.drawString(key.getName() + ":", getX() / f7, (getY() + getHeight() / 2 - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(anim, f2));
        String string;
        if (listening) {
            string = "Listening";
        } else if (key.getObject() == -1) {
            string = "Unbound";
        } else {
            string = KeyEvent.getKeyText(key.getObject()).contains("Unknown") ? getKey(key.getObject()) : KeyEvent.getKeyText(key.getObject());
        }
        font.drawString(string + (listening ? getDots() : ""), (getX() + getWidth() - font.getStringWidth(string + (listening ? "..." : "")) * f7) / f7, (getY() + getHeight() / 2 - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(anim, f2));
        matrixStack.pop();
        if (key.getObject() > 9996) {
            key.setListening(true);
        }
        if (key.isListening()) {
            f7 = 0.7f;
            matrixStack.push();
            matrixStack.scale(f7, f7, f7);
            font.drawString("(Hold)", (getX() + font.getStringWidth(key.getName() + ":") * 0.95f) / f7, (getY() + 0.1f + getHeight() / 1.1f - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(anim, f2));
            matrixStack.pop();
        }
        this.setHeight(15);
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0) {
            if (listening) {
                key.invokeValue(9999);
                listening = false;
            }
            if (isInsideCross((int)d, (int)d2)) {
                listening = !listening;
            }
        }
        if (n == 1) {
            if (isInsideCross((int)d, (int)d2)) {
                key.setListening(!key.isListening());
            }
            if (listening) {
                key.invokeValue(9998);
                listening = false;
            }
        }
        if (n == 2 && listening) {
            key.invokeValue(9997);
            listening = false;
        }
    }

    @Override
    public void keyPressed(int n, int n2, int n3) {
        if (listening) {
            switch (n) {
                case 256, 259, 261 -> {
                    key.invokeValue(-1);
                    listening = false;
                }
            }
            if (getKey(n) != null) {
                key.invokeValue(n);
                listening = false;
            }
        }
    }

    @Override
    public void charTyped(char c, int n) {
        if (listening && c != '§' && c >= ' ' && c != '') {
            char[] cArray = String.valueOf(c).toUpperCase(Locale.ROOT).toCharArray();
            key.invokeValue((int) cArray[0]);
            listening = false;
        }
    }

    String getDots() {
        if (System.currentTimeMillis() - currentTime > 1333L) {
            currentTime = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - currentTime > 999L ? "..." : (System.currentTimeMillis() - currentTime > 666L ? ".." : (System.currentTimeMillis() - currentTime > 333L ? "." : ""));
    }

    public static String getKey(int i) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(9999, "LMB");
        hashMap.put(9998, "RMB");
        hashMap.put(9997, "MMB");
        hashMap.put(345, "R-Ctrl");
        hashMap.put(344, "Right Shift");
        hashMap.put(342, "L-Alt");
        hashMap.put(341, "L-Ctrl");
        hashMap.put(340, "Shift");
        hashMap.put(258, "Tab");
        return hashMap.get(i);
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + getHeight();
    }
}
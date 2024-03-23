/*
add jenny mod - cloovey
*/
package dev.zprestige.prestige.client.ui.drawables.gui.screens.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.HWIDGrabber;
import dev.zprestige.prestige.client.protection.Session;
import dev.zprestige.prestige.client.protection.auth.ClientLogin;
import dev.zprestige.prestige.client.ui.drawables.gui.field.CustomTextField;
import dev.zprestige.prestige.client.ui.drawables.gui.field.PasswordField;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.DrawableScreen;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class LoginScreen extends DrawableScreen {

    public CustomTextField username = new CustomTextField("Username/Email");
    public PasswordField password = new PasswordField("Password");
    public long time;
    public boolean active;
    public String value = "";

    public void render(DrawContext drawContext, int n, int n2, float f) {
        RenderHelper.setMatrixStack(drawContext.getMatrices());
        Prestige.Companion.getFontManager().setMatrixStack(drawContext.getMatrices());
        MatrixStack matrixStack = Prestige.Companion.getFontManager().getMatrixStack();
        RenderUtil.renderColoredQuad(0, 0, width, height, new Color(14, 14, 14));
        float f2 = 200;
        float f3 = 120;
        float f4 = (float)width / 2 - 100;
        float f5 = (float)height / 2 - 50;
        RenderUtil.renderRoundedRect(f4, f5, f4 + f2, f5 + f3, new Color(13, 13, 13), 10);
        RenderUtil.renderRoundedRectOutline(f4, f5, f4 + f2, f5 + f3, new Color(10, 10, 10), 10);
        if (value.equals("Default GUI key is RSHIFT")) {
            value = "";
        }
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        Color color = Color.WHITE;
        font.drawString( "Welcome", f4 + f2 / 2 - font.getStringWidth("Welcome") / 2, f5, color);
        font.drawString(value, f4 + f2 / 2 - font.getStringWidth(value) / 2, f5 + 15, new Color(100, 100, 100));
        username.setPosition(f4 + f2 / 2 - 80, f5 + 40);
        username.setDimension(160, 20.1f);
        username.render(n, n2, 1, 1);
        password.setPosition(f4 + f2 / 2 - 80, f5 + 65);
        password.setDimension(140, 20.1f);
        password.render(n, n2, 1, 1, active);
        RenderUtil.renderColoredQuad(f4 + f2 / 2 + 60, f5 + 65, f4 + f2 / 2 + 80, f5 + 85, new Color(12, 12, 12));
        RenderUtil.renderColoredRectangleOutline(f4 + f2 / 2 + 60, f5 + 65, f4 + f2 / 2 + 80, f5 + 85, new Color(9, 9, 9));
        color = active ? Color.WHITE : new Color(50, 50, 50);
        RenderUtil.renderTexturedRect(f4 + f2 / 2 + 62.5f, f5 + 67.5f, 15, 15, new Identifier("prestige", "icons/categories/visual.png"), color);
        if (isInsideReveal(n, n2)) {
            RenderUtil.renderColoredQuad(f4 + f2 / 2 + 60, f5 + 65, f4 + f2 / 2 + 80, f5 + 85, new Color(0, 0, 0, 50));
        }
        RenderUtil.renderColoredQuad(f4 + f2 / 2 - 80, f5 + 90, f4 + f2 / 2 + 80, f5 + 110, new Color(12, 12, 12));
        RenderUtil.renderColoredRectangleOutline(f4 + f2 / 2 - 80, f5 + 90, f4 + f2 / 2 + 80, f5 + 110, new Color(9, 9, 9));
        if (isInside(n, n2) && username.isValueNotEmpty()) {
            RenderUtil.renderColoredQuad(f4 + f2 / 2 - 80, f5 + 90, f4 + f2 / 2 + 80, f5 + 110, new Color(0, 0, 0, 50));
        }
        float f8 = 1;
        matrixStack.push();
        matrixStack.scale(f8, f8, f8);
        int n3 = isInside(n, n2) ? 50 : 0;
        font.drawString("Login", (f4 + f2 / 2 - font.getStringWidth("Login") * f8 / 2) / f8, (f5 + 90.1f) / f8, username.isValueNotEmpty() ? new Color(200 + n3, 200 + n3, 200 + n3) : new Color(100 + n3, 100 + n3, 100 + n3));
        font.drawString("Login", (f4 + f2 / 2 - font.getStringWidth("Login") * f8 / 2f) / f8, (f5 + 90.1f) / f8, username.isValueNotEmpty() ? new Color(200 + n3, 200 + n3, 200 + n3) : new Color(0.30980393f, 0.27450982f, 0.8980392f, 0.1f + (float)n3 / 400));
        matrixStack.pop();
        if (Prestige.Companion.getSession() != null && System.currentTimeMillis() - time > 1000L) {
            close();
        }
    }

    public boolean mouseClicked(double d, double d2, int n) {
        username.mouseClicked(n, (int)d, (int)d2);
        password.mouseClicked(n, (int)d, (int)d2);
        if (n == 0) {
            if (isInside((int)d, (int)d2)) {
                login();
            }
            if (isInsideReveal((int)d, (int)d2)) {
                active = !active;
            }
        }
        return super.mouseClicked(d, d2, n);
    }

    public void close() {
        if (Prestige.Companion.getSession() == null) {
            ProtectionManager.exit("M");
        }
        super.close();
    }

    public boolean keyPressed(int n, int n2, int n3) {
        username.keyPressed(n);
        password.keyPressed(n);
        if (n == 258 && username.getActive()) {
            password.setActive(true);
            username.setActive(false);
        }
        if (n == 257) {
            login();
        }
        return super.keyPressed(n, n2, n3);
    }

    public boolean charTyped(char c, int n) {
        username.charTyped(c);
        password.charTyped(c);
        return super.charTyped(c, n);
    }

    void login() {
        if (username.getValue().contains(":") || username.getValue().contains("=")) {
            value = "Username/Email contains a disallowed character (\":\" or \"=\")!";
            return;
        }
        if (password.getValue().contains(":") || password.getValue().contains("=")) {
            value = "Password contains a disallowed character (\":\" or \"=\")!";
            return;
        }
        value = ClientLogin.run(username.getValue(), HWIDGrabber.encrypt(password.getValue()));;
        if (value.contains("Successfully processed login!")) {
            String[] string = value.split("\\(")[1].split(":");
            Prestige.Companion.setSession(new Session(Integer.parseInt(string[0]), string[1], string[2], string[3], HWIDGrabber.getHWID()));
            time = System.currentTimeMillis();
            value = "Successfully processed login!";
        } else {
            password.setValue("");
        }
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    boolean isInsideReveal(int n, int n2) {
        return n >= width / 2 - 100 + 200 / 2 + 60 && n <= width / 2 - 100 + 200 / 2 + 80 && n2 >= height / 2 - 50 + 65 && n2 <= height / 2 - 50 + 85;
    }

    boolean isInside(int n, int n2) {
        return n >= width / 2 - 100 + 200 / 2 - 80 && n <= width / 2 - 100 + 200 / 2 + 80 && n2 >= height / 2 - 50 + 90 && n2 <= height / 2 - 50 + 110;
    }
}
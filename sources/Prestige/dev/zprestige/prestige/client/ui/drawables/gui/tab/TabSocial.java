package dev.zprestige.prestige.client.ui.drawables.gui.tab;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.ui.drawables.Drawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.MC;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import java.awt.Color;

import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class TabSocial extends Drawable implements MC {
    public Identifier social = new Identifier("prestige", "icons/other/social.png");
    public String search = "";
    public int status = -1;
    public boolean isTyping;
    public int fuckoff;
    public long time;

    public TabSocial(float f, float f2, float f3, float f4) {
        super(f, f2, f3, f4, null);
    }

    @Override
    public void render(int n, int n2, float f, float f2) {
        RenderUtil.renderRoundedRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(0, f2), 7);
        RenderUtil.renderRoundedRectOutline(getX(), getY(), getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(-3, f2), 7);
        RenderUtil.renderRoundedRect(getX() + 5, getY() + 5, getX() + getWidth() - 65, getY() + 20, RenderUtil.getColor(-2, f2), 5);
        RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 5, getX() + getWidth() - 65, getY() + 20, RenderUtil.getColor(-5, f2), 5);
        RenderUtil.renderRoundedRect(getX() + getWidth() - 60, getY() + 5, getX() + getWidth() - 45, getY() + 20, RenderUtil.getColor(fuckoff == 0 ? Prestige.Companion.getModuleManager().getMenu().getColor().getObject() : RenderUtil.getColor(-2, 1), f2), 5);
        RenderUtil.renderRoundedRectOutline(getX() + getWidth() - 60, getY() + 5, getX() + getWidth() - 45, getY() + 20, RenderUtil.getColor(-5, f2), 5);
        RenderUtil.renderTexturedRect(getX() + getWidth() - 57.5f, getY() + 7.5f, 10, 10, social, RenderUtil.getColor(Color.WHITE, f2));
        RenderUtil.renderRoundedRect(getX() + getWidth() - 40, getY() + 5, getX() + getWidth() - 25, getY() + 20, RenderUtil.getColor(fuckoff == 1 ? Prestige.Companion.getModuleManager().getMenu().getColor().getObject() : RenderUtil.getColor(-2, 1), f2), 5);
        RenderUtil.renderRoundedRectOutline(getX() + getWidth() - 40, getY() + 5, getX() + getWidth() - 25, getY() + 20, RenderUtil.getColor(-5, f2), 5);
        RenderUtil.renderTexturedRect(getX() + getWidth() - 37.5f, getY() + 7.5f, 10, 10, social, RenderUtil.getColor(Color.WHITE, f2));
        RenderUtil.renderTexturedRect(getX() + getWidth() - 37.5f, getY() + 7.5f, 10, 10, social, RenderUtil.getColor(Color.CYAN, f2 * 0.5f));
        RenderUtil.renderRoundedRect(getX() + getWidth() - 20, getY() + 5, getX() + getWidth() - 5, getY() + 20, RenderUtil.getColor(fuckoff == -1 ? Prestige.Companion.getModuleManager().getMenu().getColor().getObject() : RenderUtil.getColor(-2, 1), f2), 5);
        RenderUtil.renderRoundedRectOutline(getX() + getWidth() - 20, getY() + 5, getX() + getWidth() - 5, getY() + 20, RenderUtil.getColor(-5, f2), 5);
        RenderUtil.renderTexturedRect(getX() + getWidth() - 17.5f, getY() + 7.5f, 10, 10, social, RenderUtil.getColor(Color.WHITE, f2));
        RenderUtil.renderTexturedRect(getX() + getWidth() - 17.5f, getY() + 7.5f, 10, 10, social, RenderUtil.getColor(Color.RED, f2 * 0.5f));
        if (status == 0) {
            if (n > getX() + getWidth() - 60 && n < getX() + getWidth() - 45 && n2 > getY() + 5) {
                if (n2 < getY() + 20) {
                    fuckoff = 0;
                }
            }
            if (n > getX() + getWidth() - 40 && n < getX() + getWidth() - 25 && n2 > getY() + 5) {
                if (n2 < getY() + 20) {
                    fuckoff = 1;
                }
            }
            if (n > getX() + getWidth() - 20 && n < getX() + getWidth() - 5 && n2 > getY() + 5) {
                if (n2 < getY() + 20) {
                    fuckoff = -1;
                }
            }
        }
        RenderUtil.renderRoundedRect(getX() + 5, getY() + 25, getX() + getWidth() - 5, getY() + getHeight() - 5, RenderUtil.getColor(-2, f2), 5);
        RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 25, getX() + getWidth() - 5, getY() + getHeight() - 5, RenderUtil.getColor(-5, f2), 5);
        RenderUtil.setScissorRegion(getX(), getY(), getX() + getWidth() - 47.5f, getY() + getHeight());
        String s;
        if (search.isEmpty()) {
            s = "Search";
        } else {
            s = search;
        }
        float f13 = 0.9f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f13, f13, f13);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        font.drawString(s + (isTyping ? getTyping() : ""), (getX() + 7.5f) / f13, (getY() + 10.01f - font.getStringHeight() * f13 / 2) / f13, RenderUtil.getColor(search.isEmpty() ? 0.6f : 1, f2));
        matrixStack.pop();
        GL11.glDisable(3089);
        float f14 = getY() + 30;
        int n3 = 0;
        for (PlayerListEntry playerListEntry : getMc().getNetworkHandler().getPlayerList()) {
            String string = playerListEntry.getProfile().getName();
            if (!search.isEmpty() && !string.contains(search)) {
                continue;
            }
            switch (fuckoff) {
                case -1 -> {
                    if (!Prestige.Companion.getSocialsManager().isEnemy(string)) continue;
                }
                case 0 -> {
                    if (Prestige.Companion.getSocialsManager().isFriend(string) || Prestige.Companion.getSocialsManager().isEnemy(string)) continue;
                }
                case 1 -> {
                    if (!Prestige.Companion.getSocialsManager().isFriend(string)) continue;
                }
            }
            if (n3 < 8) {
                drawPlayer(playerListEntry, getX() + 10, f14, getWidth() - 20, n, n2, status, f2);
                f14 += 20;
                n3 += 1;
            }
        }
        status = -1;
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0 && isInsideCross(d, d2)) {
            isTyping = !isTyping;
        }
        status = n;
    }

    @Override
    public void charTyped(char c, int n) {
        if (isTyping && c != '§' && c >= ' ' && c != '') {
            search = search + c;
        }
    }

    @Override
    public void keyPressed(int n, int n2, int n3) {
        if (isTyping) {
            switch (n) {
                case 256, 257 -> isTyping = false;
            }
            if (n == 259) {
                if (search.length() > 0) {
                    search = search.substring(0, search.length() - 1);
                }
            }
        }
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    void drawPlayer(PlayerListEntry playerListEntry, float f, float f2, float f3, int n, int n2, int n3, float f4) {
        float f5 = 15;
        RenderUtil.renderRoundedRect(f, f2, f + f3, f2 + f5, RenderUtil.getColor(1, f4), 5);
        RenderUtil.renderRoundedRectOutline(f, f2, f + f3, f2 + f5, RenderUtil.getColor(-3, f4), 5);
        RenderUtil.renderTexturedQuad(playerListEntry.getSkinTexture(), f + 2.5f, f2 + 2.5f, 0, 10, 10, 10, 10, 80, 80);
        RenderUtil.renderRoundedRectOutline(f + 2.5f, f2 + 2.5f, f + 12.5f, f2 + 12.5f, RenderUtil.getColor(-5, f4), 0);
        RenderUtil.renderRoundedRect(f + f3 - 12.5f, f2 + 2.5f, f + f3 - 2.5f, f2 + f5 - 2.5f, RenderUtil.getColor(0, f4), 2);
        RenderUtil.renderRoundedRectOutline(f + f3 - 12.5f, f2 + 2.5f, f + f3 - 2.5f, f2 + f5 - 2.5f, RenderUtil.getColor(-3, f4), 2);
        RenderUtil.renderRoundedRect(f + f3 - 25, f2 + 2.5f, f + f3 - 15, f2 + f5 - 2.5f, RenderUtil.getColor(0, f4), 2);
        RenderUtil.renderRoundedRectOutline(f + f3 - 25, f2 + 2.5f, f + f3 - 15, f2 + f5 - 2.5f, RenderUtil.getColor(-3, f4), 2);
        RenderUtil.renderRoundedRect(f + f3 - 37.5f, f2 + 2.5f, f + f3 - 27.5f, f2 + f5 - 2.5f, RenderUtil.getColor(0, f4), 2);
        RenderUtil.renderRoundedRectOutline(f + f3 - 37.5f, f2 + 2.5f, f + f3 - 27.5f, f2 + f5 - 2.5f, RenderUtil.getColor(-3, f4), 2);
        String name = playerListEntry.getProfile().getName();
        float f6 = 0.8f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f6, f6, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        font.drawString(name, (f + 15) / f6, (f2 + 1.5f) / f6, RenderUtil.getColor(Prestige.Companion.getSocialsManager().getColor(name), f4));
        font.drawString(String.valueOf(Prestige.Companion.getSocialsManager().getType(name)), (f + f3 - 19 - font.getStringWidth(String.valueOf(Prestige.Companion.getSocialsManager().getType(name))) / 2) / f6, f2 / f6, RenderUtil.getColor(Prestige.Companion.getSocialsManager().getColor(name), f4));
        matrixStack.pop();
        RenderUtil.renderColoredQuad(f + f3 - 35, f2 + 7, f + f3 - 30, f2 + 8, RenderUtil.getColor(1, f4));
        RenderUtil.renderColoredQuad(f + f3 - 10, f2 + 7, f + f3 - 5, f2 + 8, RenderUtil.getColor(1, f4));
        RenderUtil.renderColoredQuad(f + f3 - 8, f2 + 5, f + f3 - 7, f2 + 10, RenderUtil.getColor(1, f4));
        if (n3 == 0) {
            if (n > f + f3 - 37.5 && n < f + f3 - 27.5 && n2 > f2 + 2.5 && n2 < f2 + f5 - 2.5) {
                if (Prestige.Companion.getSocialsManager().isFriend(name)) {
                    Prestige.Companion.getSocialsManager().removeFriend(name);
                } else if (!Prestige.Companion.getSocialsManager().isEnemy(name)) {
                    Prestige.Companion.getSocialsManager().addEnemy(name);
                }
            }
            if (n > f + f3 - 12.5 && n < f + f3 - 2.5 && n2 > f2 + 2.5 && n2 < f2 + f5 - 2.5) {
                if (Prestige.Companion.getSocialsManager().isEnemy(name)) {
                    Prestige.Companion.getSocialsManager().removeEnemy(name);
                } else if (!Prestige.Companion.getSocialsManager().isFriend(name)) {
                    Prestige.Companion.getSocialsManager().addFriend(name);
                }
            }
        }
    }

    String getTyping() {
        if (System.currentTimeMillis() - time > 1000L) {
            time = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - time > 500L ? "|" : "";
    }

    boolean isInsideCross(double d, double d2) {
        return d > getX() && d < (getX() + getWidth()) && d2 > getY() && d2 < (getY() + 20);
    }

}
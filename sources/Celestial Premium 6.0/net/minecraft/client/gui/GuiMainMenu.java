/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.helpers.input.MouseHelper;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.button.GuiMainMenuButton;
import org.celestial.client.ui.button.ImageButton;
import org.celestial.client.ui.components.altmanager.GuiAltManager;
import org.celestial.client.ui.components.changelog.ChangeLog;
import org.celestial.client.ui.particle.ParticleUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiMainMenu
extends GuiScreen {
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private double scrollOffset;

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        this.buttonList.add(new GuiMainMenuButton(0, width / 2 - 90, height / 2 + 4, 180, 15, "Singleplayer"));
        this.buttonList.add(new GuiMainMenuButton(1, width / 2 - 90, height / 2 + 32, 180, 15, "Multiplayer"));
        this.buttonList.add(new GuiMainMenuButton(2, width / 2 - 90, height / 2 + 60, 180, 15, "Alt Manager"));
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/world.png"), width / 2 + 100, height / 2 + 4, 24, 24, " Language", 15));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/misc.png"), width / 2 + 100, height / 2 + 34, 24, 24, "  Options", 16));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/quit.png"), width / 2 + 105, height / 2 + 68, 15, 15, "Quit Game", 14));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/logo.png"), width / 2 - 60, height / 2 - 160, 120, 120, "", 13));
        this.imageButtons.add(new ImageButton(new ResourceLocation("celestial/shop.png"), 3, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT - 19, 24, 24, "\u041d\u0430\u0448 \u043c\u0430\u0433\u0430\u0437\u0438\u043d \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u043e\u0432", 228));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RectHelper.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(17, 17, 17, 255).getRGB());
        ParticleUtils.drawParticles(Mouse.getX() * this.width / this.mc.displayWidth, this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1);
        RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), 0.0f, 0.0f, sr.getScaledWidth(), 2.0f, ClientHelper.getClientColor());
        double y = 23.0;
        double x = 8.0;
        double right = x + (double)((int)((float)sr.getScaledWidth() / 4.0f));
        double top = y + (double)(sr.getScaledHeight() - 50);
        String welcome = "Welcome, " + (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : this.mc.getSession().getUsername());
        this.mc.robotoRegularFontRender.drawStringWithOutline(welcome, sr.getScaledWidth() - 2 - this.mc.robotoRegularFontRender.getStringWidth(welcome), sr.getScaledHeight() - this.mc.robotoRegularFontRender.getFontHeight() - 4, -1);
        RectHelper.drawSkeetButton(37.0f, 49.0f, 40.0f, 50.0f);
        this.mc.clickguismall.drawStringWithOutline("ChangeLog", 15.0, 9.5, new Color(185, 185, 185, 255).getRGB());
        GlStateManager.pushMatrix();
        GL11.glEnable(3089);
        RenderHelper.scissorRect((int)x, (int)y - 3, (int)right + sr.getScaledWidth(), top);
        if (MouseHelper.isHovered(x, y, right, top, mouseX, mouseY) && Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.scrollOffset += 28.0;
                if (this.scrollOffset < 0.0) {
                    this.scrollOffset = 0.0;
                }
            } else if (wheel > 0) {
                this.scrollOffset -= 28.0;
                if (this.scrollOffset < 0.0) {
                    this.scrollOffset = 0.0;
                }
            }
        }
        for (ChangeLog log : Celestial.instance.changeLogManager.getChangeLogs()) {
            if (log == null) continue;
            this.mc.smallfontRenderer.drawStringWithShadow(log.getLogName(), x, y - this.scrollOffset, -1);
            y += 8.0;
        }
        GL11.glDisable(3089);
        GlStateManager.popMatrix();
        if (!ImageButton.hoverShop) {
            this.mc.robotoRegularFontRender.drawStringWithOutline("Celestial Client (#" + Celestial.version + ")", 30.0, sr.getScaledHeight() - this.mc.robotoRegularFontRender.getFontHeight() - 4, -1);
        }
        for (ImageButton imageButton : this.imageButtons) {
            if (imageButton == null) continue;
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected boolean isHovered(int x, int y, int mouseX, int mouseY) {
        return MouseHelper.isHovered(x, y, x + this.width, y + this.height, mouseX, mouseY);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 4: {
                System.exit(0);
            }
        }
        super.actionPerformed(button);
    }
}


package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.components.DiscordRPC;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.ui.button.GuiAltButton;
import org.dreamcore.client.ui.button.ImageButton;
import org.dreamcore.client.ui.components.altmanager.GuiAltManager;
import org.dreamcore.client.ui.components.changelog.ChangeLog;
import org.dreamcore.client.ui.particle.ParticleSystem;
import org.dreamcore.security.utils.LicenseUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiMainMenu extends GuiScreen {

    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    protected ParticleSystem engine = new ParticleSystem();

    @Override
    public void initGui() {

        new DiscordRPC().update("Screen", "In MainMenu");

        this.buttonList.clear();
        this.buttonList.add(new GuiAltButton(0, (width / 2) - 55, height / 2 - 10, 110, 15, "Singleplayer"));
        this.buttonList.add(new GuiAltButton(1, width / 2 - 55, height / 2 + 10, 110, 15, "Multiplayer"));
        this.buttonList.add(new GuiAltButton(2, width / 2 - 55, height / 2 + 30, 110, 15, "Options"));
        this.buttonList.add(new GuiAltButton(3, width / 2 - 55, height / 2 + 50, 110, 15, "Alt Manager"));

        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("dreamcore/logo.png"), width / 2 - 30, height / 2 - 140, 60, 70, "89", 13));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        GlStateManager.pushMatrix();

        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (new Color(22, 22, 22, 255)).getRGB());

        for (int i = 0; i < width; i++) {
            Gui.drawRect(i, 0, width, 1, PaletteHelper.rainbow(i * 7, 0.5F, 1).getRGB());
        }

        mc.robotoRegularFontRender.drawStringWithShadow("dreamcore Software (v" + dreamcore.instance.version + ")", 2, sr.getScaledHeight() - 10, -1);
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }

        mc.robotoRegularFontRender.drawStringWithShadow("Hello, " + TextFormatting.GRAY + LicenseUtil.userName, sr.getScaledWidth() - mc.robotoRegularFontRender.getStringWidth("Hello, " + LicenseUtil.userName) - 2, sr.getScaledHeight() - 10, -1);

        int y = 10;
        int x = 4;

        for (ChangeLog log : dreamcore.instance.changeManager.getChangeLogs()) {
            GlStateManager.pushMatrix();
            if (log != null) {
                GlStateManager.scale(0.5, 0.5, 0.5);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderHelper.scissorRect(0, 2, width / 2 - 20, height - 10);
                mc.fontRendererObj.drawStringWithShadow(log.getLogName(), x, y - 0.1f, -1);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GlStateManager.scale(2, 2, 2);
            }
            y += 11;
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
        }
        super.actionPerformed(button);
    }
}

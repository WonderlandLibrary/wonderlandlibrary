package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.dreamcore.client.helpers.misc.ClientHelper;

public class GuiIngameMenu extends GuiScreen
{
    private int saveStep;
    private int visibleTime;

    public static float animTicks;
    public static float addition;
    public static float lastTime;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        animTicks = 0;
        addition = 0;
        lastTime = 0;
        this.saveStep = 0;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + -16, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
            this.buttonList.add(new GuiButton(90, width / 2 - 100, height / 4 + 72 + -16, "Reconnect"));

        }

        this.buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + -16, 98, 20, I18n.format("menu.options")));
        GuiButton guibutton = this.addButton(new GuiButton(7, width / 2 + 2, height / 4 + 96 + -16, 98, 20, I18n.format("menu.shareToLan")));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
        this.buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
        this.buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                boolean flag = this.mc.isIntegratedServerRunning();
                button.enabled = false;
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.func_191982_f()));
                break;
            case 6:
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 90:
                mc.world.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, ClientHelper.serverData));
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.visibleTime;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        addition = lastTime < this.mc.timer.renderPartialTicks ? mc.timer.renderPartialTicks - lastTime : (lastTime != mc.timer.renderPartialTicks ? 1 - lastTime + mc.timer.renderPartialTicks : 0);
        if (animTicks < 20) {
            animTicks = animTicks + Math.min(addition / 1.5F * (20.5F - animTicks), 20.0F - animTicks);
        }

        this.drawDefaultBackground();

        GlStateManager.translate(width / 2F, height / 2F, 1F);
        GlStateManager.scale(animTicks * 0.05F, animTicks * 0.05F, animTicks * 0.05F);
        GlStateManager.translate(-width / 2F, -height / 2F, 1);
        lastTime = this.mc.timer.renderPartialTicks;

        mc.circleregular.drawCenteredString("Game Menu", this.width / 2, 45, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

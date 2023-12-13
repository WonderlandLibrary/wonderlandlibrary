package net.minecraft.client.gui;

import vip.radium.RadiumClient;
import vip.radium.gui.alt.impl.GuiAltManager;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.optifine.reflect.Reflector;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    private GuiButton buttonResetDemo;
    private DynamicTexture viewportTexture;

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);

        int i = 24;
        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j, 24);

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));

        this.mc.func_181537_a(false);
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Alt Manager"));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        } else if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (button.id == 14) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (button.id == 100) {
            this.mc.displayGuiScreen(new GuiAltManager(this));
        } else if (button.id == 4) {
            this.mc.shutdown();
        } else if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen) Reflector.newInstance(Reflector.GuiModList_Constructor, new Object[]{this}));
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderingUtils.drawGuiBackground(width, height);

        String firstChar = String.valueOf(RadiumClient.NAME.charAt(0));
        String restOfName = RadiumClient.NAME.substring(1);

        float scale = 4;

        float firstCharWidth = Wrapper.getMinecraftFontRenderer().getWidth(firstChar);
        float restOfNameWidth = Wrapper.getMinecraftFontRenderer().getWidth(restOfName);

        float textX = width / 2.0F - (((firstCharWidth + restOfNameWidth) * scale) / 2);
        int textHeight = this.height / 4 - 24;

        textX /= scale;
        textHeight /= scale;
        GL11.glScaled(scale, scale, 1);

        Wrapper.getMinecraftFontRenderer().drawStringWithShadow(RadiumClient.NAME,
                textX, textHeight, Colors.DEEP_PURPLE);

        GL11.glScaled(1 / scale, 1 / scale, 1);

//        ChangeLogUtils.drawChangeLog();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

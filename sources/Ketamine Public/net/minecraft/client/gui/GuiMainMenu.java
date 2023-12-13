package net.minecraft.client.gui;

import java.io.IOException;

import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.ui.account.GuiAccountManager;
import io.github.nevalackin.client.util.render.GLShader;
import io.github.nevalackin.client.util.render.Shaders;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private long initTime = System.currentTimeMillis();

    private final GLShader czBackgroundShader = new GLShader(Shaders.VERTEX_SHADER, Shaders.BACKGROUND_CZ_FRAG_SHADER) {
        @Override
        public void setupUniforms() {
            setupUniform("time");
            setupUniform("resolution");
        }

        @Override
        public void updateUniforms() {
            glUniform1f(this.getUniformLocation("time"), (System.currentTimeMillis() - initTime) / 1000.0F);
            glUniform2f(this.getUniformLocation("resolution"), mc.displayWidth, mc.displayHeight);
        }
    };

    private final GLShader backgroundShader = new GLShader(Shaders.VERTEX_SHADER, Shaders.BACKGROUND_FRAG_SHADER) {
        @Override
        public void setupUniforms() {
            setupUniform("time");
            setupUniform("resolution");
            setupUniform("mouse");
        }

        @Override
        public void updateUniforms() {
            glUniform1f(this.getUniformLocation("time"), (System.currentTimeMillis() - initTime) / 1000.0F);
            glUniform2f(this.getUniformLocation("resolution"), mc.displayWidth, mc.displayHeight);
            glUniform2f(this.getUniformLocation("mouse"), Mouse.getX() / 1000.0F, Mouse.getY() / 1000.0F);
        }
    };

    public GuiMainMenu() {
        // FIND ME :: DiscordRPC in main menu
        KetamineClient.getInstance().updateDiscordRPC("In Main Menu");
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public void initGui()
    {
        final int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j, 24);

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));

        this.initTime = System.currentTimeMillis();
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
    {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Alt Manager"));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiAccountManager(this));
                break;
            case 4:
                this.mc.shutdown();
                break;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        glDisable(GL_CULL_FACE);

        final boolean czechMode = this.mc.gameSettings.language.equals("cs_CZ");

        final GLShader bgShader = czechMode ? this.czBackgroundShader : this.backgroundShader;

        bgShader.use();

        glBegin(GL_QUADS);
        {
            glVertex2i(-1, -1);
            glVertex2i(-1, 1);
            glVertex2i(1, 1);
            glVertex2i(1, -1);
        }
        glEnd();

        glUseProgram(0);

        if (czechMode) {
            glScaled(2, 2, 1);
            this.fontRendererObj.drawString("\247LAhoj ty hloupa kurvo!", 2, height / 2.0 - 9 - 2, 0xFF000000);
            glScaled(0.5, 0.5, 1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

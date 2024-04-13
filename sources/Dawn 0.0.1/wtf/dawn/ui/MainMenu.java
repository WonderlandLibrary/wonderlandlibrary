package wtf.dawn.ui;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import wtf.dawn.utils.animations.simple.SimpleAnimation;
import wtf.dawn.Dawn;
import wtf.dawn.ui.impl.AltManager;
import wtf.dawn.utils.font.FontUtil;
import wtf.dawn.utils.font.MinecraftFontRenderer;
import wtf.dawn.utils.shaders.GLSLShaders;


import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends GuiScreen {



    SimpleAnimation introAnimation = new SimpleAnimation(-50F);
    Map<String, Font> locationMap = new HashMap<>();

    MinecraftFontRenderer titleRenderer = new MinecraftFontRenderer(FontUtil.getFont(locationMap, "Product Sans Regular.ttf", 35), true, true);

    private GLSLShaders shaders;
    private long initTime = System.currentTimeMillis();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.shaders.useShader(width * 2, height * 2, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);
        GlStateManager.disableCull();
        GL11.glColor4f(1, 1, 1, 1);

        introAnimation.setAnimation(height / 2 - 30, 5);

        titleRenderer.drawCenteredString(Dawn.getInstance().getName(), (float) (width / 2), (float) introAnimation.getValue() - 50, -1);


        super.drawScreen(mouseX, mouseY, partialTicks);

    }


    @Override
    public void initGui() {

        try {
            this.shaders = new GLSLShaders("/assets/minecraft/dawn/shader/main.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2        - 50, "Singleplayer"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 30   - 50, "Multiplayer"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 60   - 50, 95, 20,"Settings"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 5, this.height / 2 + 60     - 50, 95, 20,"Quit"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + 90   - 50,  "Alts"));

        super.initGui();
    }



    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if(button.id == 1) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if(button.id == 2) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        if(button.id == 3) {
            mc.shutdown();
        }
        if(button.id == 4) {
            mc.displayGuiScreen(new AltManager());
        }

        super.actionPerformed(button);
    }
}

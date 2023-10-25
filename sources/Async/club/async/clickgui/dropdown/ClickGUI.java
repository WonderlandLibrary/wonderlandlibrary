package club.async.clickgui.dropdown;

import club.async.Async;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.impl.hud.ClickGui;
import club.async.util.ColorUtil;
import club.async.util.ModuleSaver;
import club.async.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

    public boolean renderingSettings;
    private ScaledResolution scaledResolution;
    private final ArrayList<Panel> panels = new ArrayList<>();
    public ArrayList<Component> components = new ArrayList<>();
    public Module selectedModule;

    public ClickGUI() {
        int offset = 10;
        for (Category category : Category.values())
        {
            panels.add(new Panel(category, offset, this));
            offset+= 140;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        /*
        Apply blur shader
        */
        if(Async.INSTANCE.getModuleManager().moduleBy(ClickGui.class).blur.get()) {
            if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (mc.entityRenderer.theShaderGroup != null) {
                    mc.entityRenderer.theShaderGroup.deleteShaderGroup();
                }
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }

        /*
        Correcting the res and initialize panels
        */
        scaledResolution = new ScaledResolution(mc);
        for (Panel panel : panels)
        {
            panel.init();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        /*
        Sexy gradientRect
        */
        if(Async.INSTANCE.getModuleManager().moduleBy(ClickGui.class).gradient.get()){
            drawGradientRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(0, 0, 0, 0), ColorUtil.getMainColor());
        }

        /*
        Rendering the panels
        */
        for (Panel panel : panels)
        {
            panel.drawScreen(mouseX, mouseY);
        }

        /*
        Rendering settings, if a module is expanded
         */
        if (renderingSettings) {
            Gui.drawRect(0,0,scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(100,100,100,180));
            Async.INSTANCE.getFontManager().getFont("Arial 50").drawCenteredString(selectedModule.getName() + "[" + Keyboard.getKeyName(selectedModule.getKey()) + "]",scaledResolution.getScaledWidth() / 2,60,new Color(255,255,255,180));
            GL11.glPushMatrix();
            RenderUtil.prepareScissorBox(scaledResolution.getScaledWidth() / 2 - 150,105,scaledResolution.getScaledWidth() / 2 + 150, scaledResolution.getScaledHeight() - 50);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Gui.drawRect(scaledResolution.getScaledWidth() / 2 - 150, 105,scaledResolution.getScaledWidth() / 2 + 150, scaledResolution.getScaledHeight() - 50, new Color(35,35,35));
            for (Component component : components) {
                component.renderComponent(mouseX, mouseY);
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        /*
        Handling exiting the ClickGui when not in setting screen
        */
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (!renderingSettings) {
                if (!MButton.binding) {
                    this.mc.displayGuiScreen(null);

                    if (this.mc.currentScreen == null) {
                        this.mc.setIngameFocus();
                    }
                }
            } else {
                renderingSettings = false;
            }
        }
        for (Panel panel : panels)
        {
            panel.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Panel panel : panels)
        {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (renderingSettings)
        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (Panel panel : panels)
        {
            panel.mouseReleased(mouseX, mouseY  );
        }
        if (renderingSettings)
            for (Component component : components) {
                component.mouseReleased(mouseX, mouseY);
            }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        ModuleSaver.save();
//        if(Async.INSTANCE.getModuleManager().moduleBy(ClickGui.class).blur.get()) {
            if (mc.entityRenderer.theShaderGroup != null) {
                mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
//        }
        Async.INSTANCE.getModuleManager().getModule("Clickgui").setToggled(false);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

package me.nyan.flush.clickgui.flush;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.render.ModuleClickGui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class FlushClickGui extends ClickGui {
    private final ArrayList<FlushPanel> panels = new ArrayList<>();
    private boolean closing;
    private float animation;

    public FlushClickGui() {
        int x = 30;
        for (Module.Category category : Module.Category.values()) {
            panels.add(new FlushPanel(category, x, 30));
            x += 120 + 10;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        animation = 0;
        closing = false;

        for (FlushPanel panel : panels) {
            panel.init();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animation += (closing ? -1 : 1) * 0.005F * Flush.getFrameTime();
        animation = Math.max(Math.min(animation, 1), 0);

        if (animation == 0) {
            closing = false;

            mc.displayGuiScreen(null);
        }

        GlStateManager.pushMatrix();
        float translateX = width / 2F * (1 - animation);
        float translateY = height / 2F * (1 - animation);
        float scale = animation;
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(translateX, translateY, 0);
        int wheel = Mouse.hasWheel() ? Mouse.getDWheel() : 0;
        for (FlushPanel panel : panels) {
            panel.draw(translateX, translateY, mouseX, mouseY, partialTicks, scale, wheel);
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (animation != 1) {
            return;
        }

        for (FlushPanel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (animation != 1) {
            return;
        }

        for (FlushPanel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (animation != 1) {
            return;
        }

        boolean cancel = false;
        for (FlushPanel panel : panels) {
            if (panel.keyTyped(typedChar, keyCode)) {
                cancel = true;
            }
        }

        if (cancel) {
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            closing = true;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        flush.getModuleManager().getModule(ModuleClickGui.class).disable();
    }
}

package graphite.impl.gui.click;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import graphite.api.event.Listen;
import graphite.impl.Graphite;
import graphite.impl.event.game.KeyPressEvent;
import graphite.impl.gui.click.component.RootComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGUI extends GuiScreen {
	private final RootComponent rootComponent;

    public ClickGUI() {
        this.mc = Minecraft.getMinecraft();
        this.rootComponent = new RootComponent(new ScaledResolution(this.mc));
    }
    
    public void init() {
    	Graphite.INSTANCE.getEventBus().subscribe(this);
    }
    
    @Listen
    public void onKeyPress(KeyPressEvent event) {
    	if (event.getKeyPressed() == Keyboard.KEY_RSHIFT) {
    		mc.displayGuiScreen(this);
    	}
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.rootComponent.onDraw(new ScaledResolution(mc), mouseX, mouseY);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_ESCAPE:
            case Keyboard.KEY_RSHIFT:
                this.mc.displayGuiScreen(null);
                return;
        }

        this.rootComponent.onKeyPress(keyCode);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        this.rootComponent.onMouseRelease(state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

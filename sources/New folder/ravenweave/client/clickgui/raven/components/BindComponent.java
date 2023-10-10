package ravenweave.client.clickgui.raven.components;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ravenweave.client.clickgui.raven.Component;
import ravenweave.client.module.Module;
import ravenweave.client.module.modules.client.ClickGuiModule;

public class BindComponent extends Component {

    private ModuleComponent module;
    private Module mod;
    private boolean isBinding;

    public BindComponent(ModuleComponent moduleComponent) {
        this.module = moduleComponent;
        this.mod = moduleComponent.mod;
    }


    @Override
    public void draw(int mouseX, int mouseY) {

        setDimensions(module.getWidth() - 10, 14);
        int x = this.x + 5;

        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        Minecraft.getMinecraft().fontRendererObj.drawString(isBinding ? "Press a key" : "Bind: " + mod.getBindAsString(),
                        (float) ((x) * 2),
                        (float) ((y + (height/2)) * 2),
                        0xFFFFFFFE,
                        false);
        GL11.glPopMatrix();
    }

    @Override
    public void clicked(int x, int y, int button) {
        if(button == 0)
            isBinding = true;
    }

    @Override
    public void keyTyped(char t, int k) {
        if (isBinding) {
            if ((k == Keyboard.KEY_0) || (k == Keyboard.KEY_ESCAPE)) {
                if (mod instanceof ClickGuiModule)
                    mod.setBind(54);
                else
                    mod.setBind(0);
            } else
                mod.setBind(k);

            this.isBinding = false;
        }
    }
}

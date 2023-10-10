package ravenweave.client.clickgui.raven.components;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.module.setting.impl.DescriptionSetting;

public class DescriptionComponent extends SettingComponent {

    private DescriptionSetting setting;

    public DescriptionComponent(Setting setting, ModuleComponent category) {
        super(setting, category);
        this.setting = (DescriptionSetting) setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        int x = this.x + 5;
        setDimensions(moduleComponent.getWidth(), 7);

        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        Minecraft.getMinecraft().fontRendererObj.drawString(
                setting.getDesc(),
                (float) (x * 2),
                (float) ((y + (height/2)) * 2),
                0xFFA020F0,
                false);
        GL11.glPopMatrix();
    }

}
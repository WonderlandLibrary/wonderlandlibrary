package ravenweave.client.clickgui.kv.components;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import ravenweave.client.clickgui.kv.KvComponent;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.module.setting.impl.DescriptionSetting;

public class KvDescriptionComponent extends KvComponent {

	private DescriptionSetting setting;

	public KvDescriptionComponent(Setting setting) {
		this.setting = (DescriptionSetting) setting;
	}

    @Override
	public void draw(int mouseX, int mouseY) {
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

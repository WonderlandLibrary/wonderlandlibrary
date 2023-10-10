package ravenweave.client.clickgui.kv.components;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import ravenweave.client.clickgui.kv.KvComponent;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.module.setting.impl.RGBSetting;
import ravenweave.client.utils.RenderUtils;

public class KvRgbComponent extends KvComponent {

	private RGBSetting setting;
    private boolean mouseDown;
    private Helping helping;

	public KvRgbComponent(Setting setting) {
		this.setting = (RGBSetting) setting;
	}

	@Override
    public void draw(int mouseX, int mouseY) {
        //moving the bars
        if(mouseDown) {
            float percentageAcross = (mouseX - x) / (float) width;
            setting.setColor(helping.id, (int) (percentageAcross*255f));
        }
        if (setting.getBlue() < 0){
            setting.setBlue(0);
        }
        if (setting.getRed() < 0){
            setting.setRed(0);
        }
        if (setting.getGreen() < 0){
            setting.setGreen(0);
        }

        if (setting.getBlue() > 255){
            setting.setBlue(255);
        }
        if (setting.getRed() > 255){
            setting.setRed(255);
        }
        if (setting.getGreen() > 255){
            setting.setGreen(255);
        }
        //name + input
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        Minecraft.getMinecraft().fontRendererObj.drawString(
                setting.getName() + ": "
        + EnumChatFormatting.RED + setting.getRed()
        + ", " + EnumChatFormatting.GREEN + setting.getGreen()
        + ", " + EnumChatFormatting.BLUE + setting.getBlue(),
                (float) (x * 2),
                (float) (y * 2),
                0xFEFFFFFF,
                false);
        GL11.glPopMatrix();

        //bars
        int boxY = y + (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT/2) + 1;
        int boxHeight = height - (boxY - y);
        RenderUtils.drawBorderedRoundedRect(x, boxY, x + width, (boxY + boxHeight) , 4, 2, 0x20FFFFFF, 0x20FFFFFF);
        int[] drawColor = { 0xffff0000, 0xff00ff00, 0xff0000ff };
        for (int i = 0; i < 3; i++) {
        	int colorX = (int) (((width * this.setting.getColor(i))/ 255f) + x);
        	RenderUtils.drawBorderedRoundedRect(
        			colorX - 2,
                    boxY,
                    colorX + 2,
                    (boxY + boxHeight),
                    4,
                    2,
                    drawColor[i],
                    0x90FFFFFF);
        }
    }

    @Override
    public void clicked(int button, int mouseX, int mouseY) {
        mouseDown = true;
        float percentageAcross = (mouseX - x) / (float) width;
        int r = 0;
        float c = 1;
        for (int i = 0; i < 3; i++)
			if (Math.abs((this.setting.getColor(i) / 255f) - percentageAcross) < c) {
                r = i;
                c = Math.abs((this.setting.getColor(i) / 255f) - percentageAcross);
            }
        switch (r) {
        case 0:
        	helping = Helping.RED;
            break;
        case 1:
        	helping = Helping.GREEN;
            break;
        case 2:
        	helping = Helping.BLUE;
            break;
        }
    }

    @Override
    public void mouseReleased(int x, int y, int button) {
        mouseDown = false;
    }

    public enum Helping {
        RED(0), GREEN(1), BLUE(2), NONE(-1);

        private final int id;

        Helping(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}

package ravenweave.client.clickgui.raven.components;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import ravenweave.client.main.Raven;
import ravenweave.client.module.modules.client.ClickGuiModule;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.CoolDown;
import ravenweave.client.utils.RenderUtils;
import ravenweave.client.utils.Utils;

import java.awt.*;

public class TickComponent extends SettingComponent {

    private TickSetting setting;
    private CoolDown timer = new CoolDown(1);
    private final int buttonWidth = 13;

    public TickComponent(Setting setting, ModuleComponent category) {
        super(setting, category);
        this.setting = (TickSetting) setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        setDimensions(moduleComponent.getWidth() - 10, 11);
        int x = this.x + 5;

        float percent = Utils.Client.smoothPercent((setting.isToggled() ?  timer.getElapsedTime() : timer.getTimeLeft())/(float) timer.getCooldownTime());
        int green = (int) (percent * 255);
        int red = 255 - green;
        final int colour = new Color(red, green, 0).getRGB();
        float offSet = (percent * buttonWidth)/3;
        int fh = (Raven.mc.fontRendererObj.FONT_HEIGHT/2) + 1;

        RenderUtils.drawBorderedRoundedRect(x, y + fh, x + buttonWidth, y + height, height/2, 2, ClickGuiModule.getBoarderColour(), 0xFF000000);
        RenderUtils.drawBorderedRoundedRect(x + offSet, y + fh, x + ((buttonWidth/3)*2) + offSet, y + height, height/2, 2, ClickGuiModule.getBoarderColour(), colour);
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        Minecraft.getMinecraft().fontRendererObj.drawString(
                setting.getName(),
                (float) ((x + buttonWidth + 2) * 2),
                (float) ((y + (height/2)) * 2),
                0xFEFFFFFF,
                false);
        GL11.glPopMatrix();
    }


    @Override
    public void clicked(int x, int y, int button) {
        timer.setCooldown(500);
        timer.start();
        setting.toggle();
        moduleComponent.mod.guiButtonToggled(setting);
    }

}
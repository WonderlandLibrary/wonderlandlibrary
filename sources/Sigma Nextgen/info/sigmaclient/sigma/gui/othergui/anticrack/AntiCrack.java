package info.sigmaclient.sigma.gui.othergui.anticrack;

import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class AntiCrack extends Screen {
    public AntiCrack() {
        super(new StringTextComponent(""));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Color r = new Color(255, 0, 0);
        if(System.currentTimeMillis() % 100 < 50){
            r = new Color(-1);
        }
        RenderUtils.drawTextureLocation(0,0,width,height,"yuankong", r);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

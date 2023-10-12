package wtf.diablo.gui.clickGuiAlternate.impl.userinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import wtf.diablo.utils.font.Fonts;

public class UserInfo {
    private Minecraft mc = Minecraft.getMinecraft();
    private ScaledResolution sr;

    public double x, y, width, height, x2, y2, barOffset;

    public UserInfo() {

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.sr = new ScaledResolution(mc);
        this.width = 150;
        this.height = 70;
        this.barOffset = 13;
        this.x = 5;
        this.y = sr.getScaledHeight() - height - 5;
        this.x2 = x + width;
        this.y2 = y + height;

        Gui.drawRect(x, y, x2, y + barOffset, 0xFF131313);

        Fonts.IconFont.drawStringWithShadow("p", x + 4.5, y + 1 + (Fonts.IconFont.getHeight() / 2), -1);
        Fonts.SFReg18.drawStringWithShadow("User Info", x + 19, y + 0.5 + (Fonts.SFReg18.getHeight() / 2), -1);

        Fonts.SFReg18.drawStringWithShadow("User Info", x + 19, y + 0.5 + (Fonts.SFReg18.getHeight() / 2), -1);
        Gui.drawRect(x, y + barOffset, x2, y2, 0x80000000);
        
    }
}

package wtf.diablo.gui.mainMenu;

import net.minecraft.client.gui.Gui;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

public class MainMenuButton {

    public double x,y,width,height;
    public String title;
    public int id;

    public MainMenuButton(int id,String title, double x, double y, double width, double height) {
        this.x = x;
        this.id = id;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void drawScreen(int mouseX, int mouseY, boolean biggerText) {
        int color = 0xFF9e47e6;
        if(RenderUtil.isHovered(mouseX,mouseY, x,y,x + width,y + height))
            color = 0xFF6f33a1;
        Gui.drawRect(x,y, x + width, y + height, color);

        Gui.drawRect(x + 1,y + 1, x + width - 1, y + height - 1, 0xff212121);

        if(biggerText) {
            Fonts.axi24.drawString(title, x + (width / 2) - (Fonts.axi24.getStringWidth(title) / 2f), y + (height / 2) - (Fonts.axi24.getHeight() / 2f)+1, -1);

        } else {
            Fonts.axi18.drawString(title, x + (width / 2) - (Fonts.axi18.getStringWidth(title) / 2f), y + (height / 2) - (Fonts.axi18.getHeight() / 2f)+1, -1);

        }
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        return RenderUtil.isHovered(mouseX,mouseY,x,y,x + width,y + height);
    }
}

package wtf.diablo.gui.config;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

public class ConfigInputBox {
    public String data = "";
    public ConfigInputBox() {

    }
    public boolean focused = false;
    public double x,y;
    public void drawScreen(int mouseX, int mouseY, double x, double y){
        this.x = x;
        this.y = y;
        Gui.drawRect(x,y, x + 100, y + 10, 0xFF1F1F1F);
        Fonts.SFReg18.drawStringWithShadow(data, x + 3, y + 2, -1);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if(focused && keyCode != Keyboard.KEY_LSHIFT && keyCode != Keyboard.KEY_RSHIFT && keyCode != Keyboard.KEY_CAPITAL) {
            String c = Character.toString(typedChar);
            if (keyCode == Keyboard.KEY_BACK) {
                if(data.length() > 0)
                    data = data.substring(0, data.length() - 1);
            } else {
                data += c;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX,mouseY,x,y,x + 100, y + 10)){
            focused = true;
        }else{
            focused = false;
        }
    }
}

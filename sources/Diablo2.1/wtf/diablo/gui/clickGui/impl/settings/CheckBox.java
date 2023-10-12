package wtf.diablo.gui.clickGui.impl.settings;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wtf.diablo.gui.clickGui.impl.Button;
import wtf.diablo.settings.Setting;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

public class CheckBox extends SetBase {

    public Button parent;
    public double y;

    public CheckBox(Setting set, Button parent) {
        this.setting = set;
        this.parent = parent;
    }
    double x;
    double width;
    BooleanSetting mode;
    boolean sliding;
    int height;

    public double drawScreen(int mouseX, int mouseY, float partialTicks, double settingHeight) {
        height = 15;
        mode = (BooleanSetting) setting;
        x = parent.parent.x;
        width = parent.parent.width;
        y = settingHeight + parent.y + parent.height;
        Gui.drawRect(x, y, x + width, y + height, 0xFF252525);
        Fonts.SFReg18.drawStringWithShadow(mode.getName(), x + 3, y + ((height - Fonts.SFReg18.getHeight()) / 2) + 0.5, -1);
        RenderUtil.drawRect(x + width - 3 - 11, y + 0 + height /2 - 5, x + width - 3, y  + 11 + height /2 - 5, 0xFF434343);
        RenderUtil.drawRect(x + width - 3 - 10, y + 1 + height/2 - 5, x + width - 4, y +10+ height /2 - 5, 0xFF232323);

        if(mode.getValue()){
            GL11.glColor4d(1,1,1,1);
            Fonts.Checkmark.drawStringWithShadow("a",x + width - 4 - 10.5, (y + height /2 - 2),-1);
        }

        return height;
    }

    public double getHeight(){
        return height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, x + width - 3 - 11, y, x + width - 3, y + height)){
            mode.setValue(!mode.getValue());
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliding = false;
    }

    public boolean isHidden() {
        return setting.isHidden();
    }
}

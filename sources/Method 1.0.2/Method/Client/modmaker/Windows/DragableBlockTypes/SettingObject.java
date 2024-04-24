package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.managers.Setting;
import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.io.Serializable;

public class SettingObject extends DragableBlock implements Serializable {

    public Setting setting;
    public boolean visible = true;

    public SettingObject(Block block, MainMaker maker) {
        super(block, maker.module);
        this.x = 300;
        this.y = 300;
        this.isDragging = false;
        this.offsetx = 0;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, MainMaker maker, boolean cancelOverride) {
        if (visible)
            super.drawScreen(mouseX, mouseY, maker, false);
    }

    @Override
    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
        if (visible)
            this.dragableinput(thisblock.getBlockItems(), maker);
    }

}

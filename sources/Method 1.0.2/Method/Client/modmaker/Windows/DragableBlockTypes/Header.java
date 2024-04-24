package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;
import net.minecraft.client.gui.Gui;

import java.io.Serializable;

public class Header extends DragableBlock implements Serializable {


    public Header(Block block, int mouseX, int mouseY, MainMaker maker) {
        super(block, maker.module);
        this.x = mouseX + 50;
        this.y = mouseY;
        this.isDragging = true;
    }

    public Header(Module module) {
        super(module);
    }

    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
        if (forwardBlock() != this) {
            int bottomY = forwardBlock().getBottom() - this.y;

            Gui.drawRect(x - 10, y + 15, x, y + 20 + bottomY, niceColor.getRGB());
            Gui.drawRect(x, y + 20 + bottomY, x + 40, y + 30 + bottomY, niceColor.getRGB());
        }

        this.dragableinput(thisblock.getBlockItems(), maker);
    }

}

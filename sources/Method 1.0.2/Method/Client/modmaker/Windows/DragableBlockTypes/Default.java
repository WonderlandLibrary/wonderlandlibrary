package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;

import java.io.Serializable;

public class Default extends DragableBlock implements Serializable {

    public Default(Block block, int mouseX, int mouseY,MainMaker maker) {
        super(block, maker.module);
        this.x = mouseX + 50;
        this.y = mouseY;
        this.isDragging = true;
    }

    public void Blockdraw(int mouseX, int mouseY,MainMaker maker) {
        this.dragableinput(thisblock.getBlockItems(),maker);
    }

    public Default( Module module) {
        super(module);
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state,MainMaker maker) {

        if (this.isDragging) {
            for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
                if (!activeBlock.isDragging)
                    if (!(activeBlock instanceof Insertable)) {
                        if (activeBlock instanceof Contained) {
                            Contained block = (Contained) activeBlock;
                            if (block.withinloop(mouseX, mouseY,maker)) {
                                this.isplaced = true;
                                ((Contained) activeBlock).blockcontained = this;
                                break;
                            }
                        }
                        if (activeBlock.nextBlock == null) {
                            if (activeBlock.belowSlot(mouseX, mouseY,maker)) {
                                this.isplaced = true;
                                activeBlock.nextBlock = this;
                                break;
                            }
                        }
                    }
            }
        }
        super.mouseReleased(mouseX, mouseY, state,maker);
    }
}

package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.InsertableBubble;
import Method.Client.module.Module;

import java.io.Serializable;

public class Insertable extends DragableBlock implements Serializable {


    public Insertable(Block block, int mouseX, int mouseY, MainMaker maker) {
        super(block, maker.module);
        this.x = mouseX + 50;
        this.y = mouseY;
        this.isDragging = true;
        this.offsetx = 0;
    }

    public Insertable(Module module) {
        super(module);
    }

    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
        this.dragableinput(thisblock.getBlockItems(), maker);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state, MainMaker maker) {
        if (this.isDragging) {
            loop1:
            for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
                for (InsertableBubble box : this.inputBoxes) {
                    if (box.containedBlock == activeBlock)
                        continue loop1;
                    if (box.containedBlock != null)
                        if (box.containedBlock.inputBoxes != null)
                            for (InsertableBubble inputBox : box.containedBlock.inputBoxes)
                                if (inputBox.containedBlock == activeBlock)
                                    continue loop1;
                }
                if (!activeBlock.isDragging)
                    for (InsertableBubble inputBox : activeBlock.inputBoxes) {
                        if (inputBox.isGlowing()) {
                            if (((thisblock.MainBlockTypeGiven == MainBlockType.Boolean && inputBox.onlyBoolean))
                                    || (!inputBox.onlyBoolean && inputBox.acceptsType(thisblock.MainBlockTypeGiven))
                                    || thisblock.MainBlockTypeGiven == MainBlockType.Wild) {
                                this.isplaced = true;
                                inputBox.dropAndExpand(this);
                            }
                            inputBox.setGlowing(false);
                            if (this.isplaced)
                                break loop1;
                        }
                    }
            }
        }
        super.mouseReleased(mouseX, mouseY, state, maker);
    }

}

package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.io.Serializable;

public class Contained extends DragableBlock implements Serializable {

    public DragableBlock blockcontained;

    public int saverblockcontained = -1;

    public boolean loopGlow;

    public Contained(Block block, int mouseX, int mouseY, MainMaker maker) {
        super(block, maker.module);
        this.x = mouseX + 50;
        this.y = mouseY;
        this.isDragging = true;
    }

    public Contained(Module module) {
        super(module);
    }


    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
        this.moveInnerBlocks();
        this.dragableinput(thisblock.getBlockItems(), maker);
        this.drawNubs(maker);
        this.expandableBox(maker);
    }

    public void moveInnerBlocks() {
        if (blockcontained != null) {
            if (blockcontained.isDragging) {
                blockcontained = null;
            } else {
                blockcontained.x = this.x + 10;
                blockcontained.y = this.y + 21;
            }
        }
    }

    public void drawNubs(MainMaker maker) {
        if (isGlowingBelow)
            Gui.drawRect(x, y + 30 + yCalculator(), x + 5, y + 35 + yCalculator(), new Color(231, 16, 16, 190).getRGB());
        else
            Gui.drawRect(x, y + 30 + yCalculator(), x + 5, y + 35 + yCalculator(), new Color(16, 231, 141, 190).getRGB());

        if (this.loopGlow)
            Gui.drawRect(x, y + 15, x + 5, y + 20, new Color(231, 16, 16, 190).getRGB());
        else
            Gui.drawRect(x, y + 15, x + 5, y + 20, new Color(16, 231, 141, 190).getRGB());
    }

    public void expandableBox(MainMaker maker) {
        Gui.drawRect(x - 10, y + 15, x, y + 20 + yCalculator(), this.niceColor.getRGB());
        Gui.drawRect(x, y + 20 + yCalculator(), x + 40, y + 30 + yCalculator(), this.niceColor.getRGB());
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state, MainMaker maker) {
        if (this.isDragging) {
            for (DragableBlock activeBlock : maker.module.ActiveBlocks) {
                if (!activeBlock.isDragging)
                    if (!(activeBlock instanceof Insertable)) {
                        if (activeBlock instanceof Contained) {
                            Contained block = (Contained) activeBlock;
                            if (block.withinloop(mouseX, mouseY, maker)) {
                                this.isplaced = true;
                                ((Contained) activeBlock).blockcontained = this;
                                break;
                            }
                        }
                        if (activeBlock.nextBlock == null) {
                            if (activeBlock.belowSlot(mouseX, mouseY, maker)) {
                                this.isplaced = true;
                                activeBlock.nextBlock = this;
                                break;
                            }
                        }
                    }
            }
        }
        super.mouseReleased(mouseX, mouseY, state, maker);
    }

    public boolean withinloop(int mouseX, int mouseY, MainMaker maker) {
        if (this.blockcontained == null) {
            if (mouseX > this.x - 10 && mouseX < this.x + 10 + (this.offsetx / 4)) {
                if (mouseY > this.y && mouseY < this.y + 35) {
                    return true;
                }
            }
        }
        return false;
    }

    public void runContainedCode(DragableBlock dragableBlock, Object event) {
        if (blockcontained != null)
            blockcontained.runCode(event);
    }

    public int yCalculator() {

        if (blockcontained != null) {
            if (blockcontained.forwardBlock() instanceof Contained && blockcontained.forwardBlock() != this) {
                int ycal = ((Contained) blockcontained.forwardBlock()).yCalculator();
                if (ycal > 9999)
                    ycal = 100;
                return (blockcontained.forwardBlock().y - this.y) + 15 + ycal;
            }
            return (blockcontained.forwardBlock().y - this.y) + 15;
        }
        return 10;
    }

    @Override
    public boolean belowSlot(int mouseX, int mouseY, MainMaker maker) {
        if (mouseX > this.x - 10 && mouseX < this.x + (this.offsetx / 4)) {
            if (mouseY > this.y + 30 + yCalculator() && mouseY < this.y + 45 + yCalculator()) {
                return true;
            }
        }
        return false;
    }

}

package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.io.Serializable;
import java.util.ArrayList;

public class heldArray extends DragableBlock implements Serializable {

    public ArrayList<Object> array;


    public heldArray(Block block, MainMaker maker) {
        super(block, maker.module);
        this.array = new ArrayList<>();
        this.x = 9000;
        this.y = 9000;
        this.isDragging = false;
        this.offsetx = 0;
    }

    // Dont draw anything
    @Override
    public void drawScreen(int mouseX, int mouseY, MainMaker maker, boolean cancelOverride) {
    }

    @Override
    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
        //this.dragableinput(thisblock.getBlockItems());
    }


}

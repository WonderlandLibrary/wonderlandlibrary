package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;

public class SignReplace extends Block {

    public SignReplace() {
        super("SignReplace", MainBlockType.Default, Tabs.Utils, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.DropDown, BlockObjects.Words,
                BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.TileEntity));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns the text of the sign at the given position";
        this.words[0] = "Sign";
        this.words[1] = "Replace";
        ddOptions.add("0");
        ddOptions.add("1");
        ddOptions.add("2");
        ddOptions.add("3");
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((TileEntitySign) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).signText[Integer.parseInt(dragableBlock.dropDowns.getSelected())]
                = new TextComponentString((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));


        super.runCode(dragableBlock, event);
    }

}

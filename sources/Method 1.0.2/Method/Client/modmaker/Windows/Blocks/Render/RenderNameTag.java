package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class RenderNameTag extends Block {

    public RenderNameTag() {
        super("RenderNameTag", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        ddOptions.add("Yes");
        ddOptions.add("No");
        this.description = "Renders the name tag of the entity.";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Yes":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).setAlwaysRenderNameTag(true);
                break;
            case "No":
                ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).setAlwaysRenderNameTag(false);
                break;
        }

        super.runCode(dragableBlock, event);
    }
}

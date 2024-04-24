package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class UpdateEntityForce extends Block {

    public UpdateEntityForce() {
        super("UpdateEntityForce", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.description = "Synchronizes the current entity with the server.";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Force";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().world.updateEntityWithOptionalForce(((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)), (boolean) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
        super.runCode(dragableBlock, event);
    }

}

package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.tileentity.TileEntity;

public class EntityBlockPos extends Block {

    public EntityBlockPos() {
        super("EntityBlockPos", MainBlockType.BlockPos, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.TileEntity, MainBlockType.Entity));
        this.description = "Gets position of Entity  " + "\n" + "Entity.getPos()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof TileEntity)
            return ((TileEntity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getPos();
        else if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof net.minecraft.entity.Entity)
            return ((net.minecraft.entity.Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getPosition();
        return null;
    }

}

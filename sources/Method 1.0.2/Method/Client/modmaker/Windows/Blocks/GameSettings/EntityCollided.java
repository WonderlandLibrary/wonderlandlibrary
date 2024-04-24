package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class EntityCollided extends Block {

    public EntityCollided() {
        super("EntityCollided", MainBlockType.Boolean, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.ddOptions.add("Collided");
        this.ddOptions.add("Horizontally");
        this.ddOptions.add("Vertically");
        this.description = "Returns true if Entity Living is collided" + "\n" + "EntityLiving.collided" + "\n" + "EntityLiving.collidedHorizontally" + "\n" + "EntityLiving.collidedVertically";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Horizontally":
                return ((EntityLivingBase)dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).collidedHorizontally;
            case "Vertically":
                return ((EntityLivingBase)dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).collidedVertically;
            case "collided":
                return ((EntityLivingBase)dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).collided;
        }
        return false;
    }

}

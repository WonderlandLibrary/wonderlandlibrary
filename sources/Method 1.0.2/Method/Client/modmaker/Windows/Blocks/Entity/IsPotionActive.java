package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class IsPotionActive extends Block {

    public IsPotionActive() {
        super("IsPotionActive", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name,  BlockObjects.NumericalTextEnter,BlockObjects.Words,
                BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Potion));
        this.words[0] = "potion";
        this.description = "Returns true if entity has given potion is active   " + "\n" + "EntityLiving.isPotionActive()";


    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isPotionActive((Potion) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }

}

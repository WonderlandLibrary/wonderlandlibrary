package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.ValidUtils;
import net.minecraft.entity.EntityLivingBase;

public class isBot extends Block {

    public isBot() {
        super("isBot", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "If Antibot is enabled, returns if the entity is in the bot list.";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return  ValidUtils.isBot((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}

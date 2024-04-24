package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class CanEntityBeSeen extends Block {

    public CanEntityBeSeen() {
        super("CanEntityBeSeen", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns true if the entity can be seen by the player" + "\n" + "mc.player.canEntityBeSeen(entity)";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().player.canEntityBeSeen((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
    }

}

package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class WorldRemoveEntity extends Block {

    public WorldRemoveEntity() {
        super("WorldRemoveEntity", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Remove Entity from world, Client Side" + "\n" + "mc.world.removeEntity(Entity)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().world.removeEntity((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}

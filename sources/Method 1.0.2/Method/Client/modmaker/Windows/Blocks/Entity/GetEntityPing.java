package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class GetEntityPing extends Block {

    public GetEntityPing() {
        super("GetEntityPing", MainBlockType.Numbers, Tabs.Entity,BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Ping of entity" + "\n" + "mc.getConnection()).getPlayerInfo(e.getUniqueID()).getResponseTime()";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        try {
            return (float) Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getUniqueID()).getResponseTime();
        } catch (NullPointerException ignored) {
        }
        return 0;
    }
}

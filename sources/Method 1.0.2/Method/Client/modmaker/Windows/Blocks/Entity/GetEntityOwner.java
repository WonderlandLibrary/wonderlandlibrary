package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;

import java.util.Objects;

import static Method.Client.utils.PlayerIdentity.getPlayerIdentity;

public class GetEntityOwner extends Block {

    public GetEntityOwner() {
        super("GetEntityOwner", MainBlockType.String, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Name of EntityTameable" + "\n" + "(EntityTameable).getOwnerId().getDisplayName()"
                + "\n" + "Also works for FireworkRocketEntity boostedEntity";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof EntityTameable)
            return getPlayerIdentity(Objects.requireNonNull(((EntityTameable) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getOwnerId()).toString()).getDisplayName();
        else if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof AbstractHorse)
            return getPlayerIdentity(Objects.requireNonNull(((AbstractHorse) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getOwnerUniqueId()).toString()).getDisplayName();
        else if (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event) instanceof net.minecraft.entity.item.EntityFireworkRocket) {
            if (((net.minecraft.entity.item.EntityFireworkRocket) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).boostedEntity != null)
                return ((net.minecraft.entity.item.EntityFireworkRocket) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).boostedEntity.getName();
        }
        return "No Owner";
    }

}

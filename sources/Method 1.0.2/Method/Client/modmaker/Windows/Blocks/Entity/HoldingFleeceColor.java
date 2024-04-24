package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;

public class HoldingFleeceColor extends Block {

    public HoldingFleeceColor() {
        super("HoldingFleeceColor", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Sheep Only!";
        this.description = "Given a Sheep returns if not holding same color dye  " + "\n" + "(Entitysheep).getFleeceColor";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((EntitySheep) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getFleeceColor() != EnumDyeColor.byDyeDamage(Minecraft.getMinecraft().player.inventory.getCurrentItem().getMetadata());
    }

}

package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class EnchantCrit extends Block {

    public EnchantCrit() {
        super("EnchantCrit", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Enchantment Critical" + "\n" + "mc.player.onEnchantmentCritical(Entity)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.onEnchantmentCritical((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}

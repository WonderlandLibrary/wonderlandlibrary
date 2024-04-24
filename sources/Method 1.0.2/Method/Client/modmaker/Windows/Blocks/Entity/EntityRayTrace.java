package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class EntityRayTrace extends Block {

    public EntityRayTrace() {
        super("EntityRayTrace", MainBlockType.RayTraceResult, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "Distance";

        this.description = "Returns distance RayTraced" + "\n" + "Entity.rayTrace(blockReachDistance)" + "\n" + " Returns RayTraceResult";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).rayTrace(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), Minecraft.getMinecraft().getRenderPartialTicks());
    }

}

package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class InterpolateEntity extends Block {

    public InterpolateEntity() {
        super("InterpolateEntity", MainBlockType.Vec3d, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter );
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Return vec3d of entity interpolated";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Utils.interpolateEntity((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), Minecraft.getMinecraft().getRenderPartialTicks());
    }
}

package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.EnumParticleTypes;

public class ParticleTypes extends Block{
        public ParticleTypes() {
            super( "ParticleTypes", MainBlockType.ParticleTypes, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
            for (EnumParticleTypes value : EnumParticleTypes.values()) {
                this.ddOptions.add(value.toString());
            }


        }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return EnumParticleTypes.valueOf(dragableBlock.dropDowns.getSelected());
    }
}

package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.ResourceLocation;

public class SoundEvent extends Block {

    public SoundEvent() {
        super("SoundEvent", MainBlockType.SoundEvent, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (net.minecraft.util.SoundEvent soundEvent : net.minecraft.util.SoundEvent.REGISTRY) {
            ddOptions.add(soundEvent.getRegistryName().toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(dragableBlock.dropDowns.getSelected()));
    }


}

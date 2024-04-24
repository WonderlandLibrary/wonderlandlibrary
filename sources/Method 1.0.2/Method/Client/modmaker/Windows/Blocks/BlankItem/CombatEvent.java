package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketCombatEvent;

public class CombatEvent extends Block {

    public CombatEvent() {
        super("CombatEvent", MainBlockType.CombatEvent, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (SPacketCombatEvent.Event value : SPacketCombatEvent.Event.values()) {
            this.ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return SPacketCombatEvent.Event.valueOf(dragableBlock.dropDowns.getSelected());
    }


}

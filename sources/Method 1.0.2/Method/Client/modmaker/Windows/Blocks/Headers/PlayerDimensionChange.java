package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerDimensionChange extends Block {
    public PlayerDimensionChange() {
        super("PlayerDimensionChange", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerDimensionChangeEvent);
        this.description = "Called when a player changes dimension.";
    }

    public static class Playerdimchange extends Block {
        public Playerdimchange() {
            super("Playerdimchange", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.PlayerDimensionChangeEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerEvent.PlayerChangedDimensionEvent) event).player;
        }
    }

    public static class DimchangeNumber extends Block {
        public DimchangeNumber() {
            super("DimchangeNumber", MainBlockType.Numbers, Tabs.Sub, Headers.PlayerDimensionChangeEvent, BlockObjects.Name, BlockObjects.DropDown);
            ddOptions.add("From");
            ddOptions.add("To");

        }
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "From":
                return ((PlayerEvent.PlayerChangedDimensionEvent) event).fromDim;
            case "To":
                return ((PlayerEvent.PlayerChangedDimensionEvent) event).toDim;
        }
        return ((PlayerEvent.PlayerChangedDimensionEvent) event).fromDim;

    }

}




package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class PlaySound extends Block {

    public PlaySound() {
        super("PlaySound", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);

        ddOptions.add("ExpOrb");
        ddOptions.add("thunder");
        ddOptions.add("lightningImpact");
        this.description = "Plays a sound";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "ExpOrb":
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
                break;
            case "thunder":
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_LIGHTNING_THUNDER, 1.0f, 1.0f));
                break;
            case "lightningImpact":
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_LIGHTNING_IMPACT, 1.0f, 1.0f));
                break;
        }
        super.runCode(dragableBlock, event);
    }


}

package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class InteractWithEntity extends Block {

    public InteractWithEntity() {
        super("InteractWithEntity", MainBlockType.Default, Tabs.Game, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        this.words[0] = "Player";
        this.words[1] = "Entity";
        this.words[2] = "Hand";
        this.description = "Given a player interact with Entity from Hand" + "\n" + "mc.playerController.interactWithEntity(player, entity, hand)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.interactWithEntity((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), (Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event),
                (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock,2, event));
        super.runCode(dragableBlock, event);
    }

}

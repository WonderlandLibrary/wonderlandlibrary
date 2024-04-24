package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class CenterPlayer extends Block {

    public CenterPlayer() {
        super("CenterPlayer", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.words[0] = "Packet";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Creates a (Client-Side) player";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        boolean flag = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
        if (flag) {
            double lMotionX = (Math.floor(Minecraft.getMinecraft().player.posX) + .5) - Minecraft.getMinecraft().player.posX;
            double lMotionZ = (Math.floor(Minecraft.getMinecraft().player.posZ) + .5) - Minecraft.getMinecraft().player.posZ;
            Minecraft.getMinecraft().player.motionX = lMotionX / 2;
            Minecraft.getMinecraft().player.motionZ = lMotionZ / 2;
        } else {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(Minecraft.getMinecraft().player.posX) + .5,
                    Minecraft.getMinecraft().player.posY, Math.floor(Minecraft.getMinecraft().player.posZ) + .5, true));
            Minecraft.getMinecraft().player.setPosition(Math.floor(Minecraft.getMinecraft().player.posX) + .5, Minecraft.getMinecraft().player.posY, Math.floor(Minecraft.getMinecraft().player.posZ) + .5);
        }
        super.runCode(dragableBlock, event);

    }


}

package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class GetNetPlayerInfo extends Block {

    public GetNetPlayerInfo() {
        super("GetNetPlayerInfo", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));

        this.ddOptions.add("gameType");
        this.ddOptions.add("lastHealth");
        this.ddOptions.add("displayHealth");
        this.ddOptions.add("lastHealthTime");
        this.ddOptions.add("healthBlinkTime");
        this.ddOptions.add("renderVisibilityId");

        this.description = "Returns Network Player Info of Entity"

                + "\n" + "gameType: -1 notset , 0 survival , 1 creative , 2 adventure , 3 spectator"
                + "\n" + "lastHealth: 0-20"
                + "\n" + "displayHealth: 0-20"
                + "\n" + "lastHealthTime: 0-1000"
                + "\n" + "healthBlinkTime: 0-1000"
                + "\n" + "renderVisibilityId: 0-1000";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "lastHealth":
                return Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getUniqueID()).getLastHealth();
            case "displayHealth":
                return Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getUniqueID()).getDisplayHealth();
            case "lastHealthTime":
                return Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getUniqueID()).getLastHealthTime();
            case "healthBlinkTime":
                return Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getUniqueID()).getHealthBlinkTime();
            case "renderVisibilityId":
                return Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getUniqueID()).getRenderVisibilityId();
            case "gameType":
                return Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getUniqueID()).getGameType().getID();
        }
        return 0;
    }
}

package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class McKeybinds extends Block {

    public McKeybinds() {
        super("McKeybinds", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("keyBindForward");
        ddOptions.add("keyBindLeft");
        ddOptions.add("keyBindBack");
        ddOptions.add("keyBindRight");
        ddOptions.add("keyBindJump");
        ddOptions.add("keyBindSneak");
        ddOptions.add("keyBindSprint");
        ddOptions.add("keyBindInventory");
        ddOptions.add("keyBindSwapHands");
        ddOptions.add("keyBindDrop");
        ddOptions.add("keyBindUseItem");
        ddOptions.add("keyBindAttack");
        ddOptions.add("keyBindPickBlock");
        ddOptions.add("keyBindChat");
        ddOptions.add("keyBindPlayerList");
        ddOptions.add("keyBindCommand");
        ddOptions.add("keyBindScreenshot");
        ddOptions.add("keyBindTogglePerspective");
        ddOptions.add("keyBindSmoothCamera");
        ddOptions.add("keyBindFullscreen");
        ddOptions.add("keyBindSpectatorOutlines");
        ddOptions.add("keyBindAdvancements");
        ddOptions.add("keyBindSaveToolbar");
        ddOptions.add("keyBindLoadToolbar");
        this.description = "Returns true if the keybind is pressed";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "keyBindForward":
                return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
            case "keyBindLeft":
                return Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
            case "keyBindBack":
                return Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
            case "keyBindRight":
                return Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
            case "keyBindJump":
                return Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
            case "keyBindSneak":
                return Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
            case "keyBindSprint":
                return Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown();
            case "keyBindInventory":
                return Minecraft.getMinecraft().gameSettings.keyBindInventory.isKeyDown();
            case "keyBindSwapHands":
                return Minecraft.getMinecraft().gameSettings.keyBindSwapHands.isKeyDown();
            case "keyBindDrop":
                return Minecraft.getMinecraft().gameSettings.keyBindDrop.isKeyDown();
            case "keyBindUseItem":
                return Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();
            case "keyBindAttack":
                return Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();
            case "keyBindPickBlock":
                return Minecraft.getMinecraft().gameSettings.keyBindPickBlock.isKeyDown();
            case "keyBindChat":
                return Minecraft.getMinecraft().gameSettings.keyBindChat.isKeyDown();
            case "keyBindPlayerList":
                return Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isKeyDown();
            case "keyBindCommand":
                return Minecraft.getMinecraft().gameSettings.keyBindCommand.isKeyDown();
            case "keyBindScreenshot":
                return Minecraft.getMinecraft().gameSettings.keyBindScreenshot.isKeyDown();
            case "keyBindTogglePerspective":
                return Minecraft.getMinecraft().gameSettings.keyBindTogglePerspective.isKeyDown();
            case "keyBindSmoothCamera":
                return Minecraft.getMinecraft().gameSettings.keyBindSmoothCamera.isKeyDown();
            case "keyBindFullscreen":
                return Minecraft.getMinecraft().gameSettings.keyBindFullscreen.isKeyDown();
            case "keyBindSpectatorOutlines":
                return Minecraft.getMinecraft().gameSettings.keyBindSpectatorOutlines.isKeyDown();
            case "keyBindAdvancements":
                return Minecraft.getMinecraft().gameSettings.keyBindAdvancements.isKeyDown();
            case "keyBindSaveToolbar":
                return Minecraft.getMinecraft().gameSettings.keyBindSaveToolbar.isKeyDown();
            case "keyBindLoadToolbar":
                return Minecraft.getMinecraft().gameSettings.keyBindLoadToolbar.isKeyDown();
        }
        return false;
    }

}

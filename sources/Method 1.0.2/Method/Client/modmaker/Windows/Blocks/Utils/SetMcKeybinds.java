package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class SetMcKeybinds extends Block {

    public SetMcKeybinds() {
        super("SetMcKeybinds", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
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
        this.description = "Sets the keybinds of the game";
    }

    @Override

    public void runCode(DragableBlock dragableBlock, Object event) {

        switch (dragableBlock.dropDowns.getSelected()) {
            case "keyBindForward":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindLeft":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindBack":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindRight":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindJump":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindSneak":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindSprint":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindInventory":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindSwapHands":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSwapHands.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindDrop":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindUseItem":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindAttack":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindPickBlock":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindPickBlock.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindChat":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindChat.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindTogglePerspective":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindTogglePerspective.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindSmoothCamera":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSmoothCamera.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindFullscreen":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindFullscreen.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;
            case "keyBindSpectatorOutlines":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSpectatorOutlines.getKeyCode(), Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode()));
                break;


        }
        super.runCode(dragableBlock, event);
    }

}

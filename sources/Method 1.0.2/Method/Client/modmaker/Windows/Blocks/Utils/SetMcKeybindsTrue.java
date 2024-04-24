package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class SetMcKeybindsTrue extends Block {

    public SetMcKeybindsTrue() {
        super("SetMcKeybindsTrue", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.Words, BlockObjects.BooleanTextEnter);
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
        this.words[0] = "Pressed: ";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "keyBindForward":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindLeft":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindBack":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindRight":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindJump":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindSneak":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindSprint":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindInventory":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindSwapHands":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSwapHands.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindDrop":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindUseItem":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindAttack":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindPickBlock":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindPickBlock.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindChat":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindChat.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindTogglePerspective":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindTogglePerspective.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindSmoothCamera":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSmoothCamera.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindFullscreen":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindFullscreen.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "keyBindSpectatorOutlines":
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSpectatorOutlines.getKeyCode(), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
        }
        super.runCode(dragableBlock, event);
    }

}

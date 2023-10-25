package fun.expensive.client.feature.impl.player;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class NoInteract extends Feature {
    public static BooleanSetting armorStands;
    public static BooleanSetting chest;
    public static BooleanSetting doors;
    public static BooleanSetting hopper;
    public static BooleanSetting buttons;
    public static BooleanSetting dispenser;
    public static BooleanSetting noteBlock;
    public static BooleanSetting craftingTable;
    public static BooleanSetting trapDoor;
    public static BooleanSetting furnace;
    public static BooleanSetting gate;
    public static BooleanSetting anvil;
    public static BooleanSetting lever;

    public NoInteract() {
        super("NoInteract", "Позволяет не нажимать ПКМ по верстакам, печкам и т.д", FeatureCategory.Player);
        armorStands = new BooleanSetting("Armor Stand", true, () -> true);
        chest = new BooleanSetting("Chest", false, () -> true);
        doors = new BooleanSetting("Doors", true, () -> true);
        hopper = new BooleanSetting("Hopper", true, () -> true);
        buttons = new BooleanSetting("Buttons", true, () -> true);
        dispenser = new BooleanSetting("Dispenser", true, () -> true);
        noteBlock = new BooleanSetting("Note Block", true, () -> true);
        craftingTable = new BooleanSetting("Crafting Table", true, () -> true);
        trapDoor = new BooleanSetting("TrapDoor", true, () -> true);
        furnace = new BooleanSetting("Furnace", true, () -> true);
        gate = new BooleanSetting("Gate", true, () -> true);
        anvil = new BooleanSetting("Anvil", true, () -> true);
        lever = new BooleanSetting("Lever", true, () -> true);
        addSettings(armorStands, chest, doors, hopper, buttons, dispenser, noteBlock, craftingTable, trapDoor, furnace, gate, anvil, lever);
    }

    public static List<Block> getRightClickableBlocks() {
        ArrayList<Block> rightClickableBlocks = new ArrayList<Block>();
        if (doors.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(64));
            rightClickableBlocks.add(Block.getBlockById(71));
            rightClickableBlocks.add(Block.getBlockById(193));
            rightClickableBlocks.add(Block.getBlockById(194));
            rightClickableBlocks.add(Block.getBlockById(195));
            rightClickableBlocks.add(Block.getBlockById(196));
            rightClickableBlocks.add(Block.getBlockById(197));
        }
        if (hopper.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(154));
        }
        if (buttons.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(77));
            rightClickableBlocks.add(Block.getBlockById(143));
        }
        if (noteBlock.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(84));
            rightClickableBlocks.add(Block.getBlockById(25));
        }
        if (trapDoor.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(96));
            rightClickableBlocks.add(Block.getBlockById(167));
        }
        if (furnace.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(61));
            rightClickableBlocks.add(Block.getBlockById(62));
        }
        if (chest.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(130));
            rightClickableBlocks.add(Block.getBlockById(146));
            rightClickableBlocks.add(Block.getBlockById(54));
        }
        if (craftingTable.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(58));
        }
        if (gate.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(107));
            rightClickableBlocks.add(Block.getBlockById(183));
            rightClickableBlocks.add(Block.getBlockById(184));
            rightClickableBlocks.add(Block.getBlockById(185));
            rightClickableBlocks.add(Block.getBlockById(186));
            rightClickableBlocks.add(Block.getBlockById(187));
        }
        if (anvil.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(145));
        }
        if (dispenser.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(23));
        }
        if (lever.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(69));
        }
        return rightClickableBlocks;
    }
}
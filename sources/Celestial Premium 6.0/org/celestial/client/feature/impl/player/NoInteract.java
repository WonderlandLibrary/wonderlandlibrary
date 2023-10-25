/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;

public class NoInteract
extends Feature {
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
        super("NoInteract", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043d\u0435 \u043d\u0430\u0436\u0438\u043c\u0430\u0442\u044c \u041f\u041a\u041c \u043f\u043e \u0432\u0435\u0440\u0441\u0442\u0430\u043a\u0430\u043c, \u043f\u0435\u0447\u043a\u0430\u043c \u0438 \u0442.\u0434", Type.Player);
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
        this.addSettings(armorStands, chest, doors, hopper, buttons, dispenser, noteBlock, craftingTable, trapDoor, furnace, gate, anvil, lever);
    }

    public static List<Block> getRightClickableBlocks() {
        ArrayList<Block> rightClickableBlocks = new ArrayList<Block>();
        if (doors.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(64));
            rightClickableBlocks.add(Block.getBlockById(71));
            rightClickableBlocks.add(Block.getBlockById(193));
            rightClickableBlocks.add(Block.getBlockById(194));
            rightClickableBlocks.add(Block.getBlockById(195));
            rightClickableBlocks.add(Block.getBlockById(196));
            rightClickableBlocks.add(Block.getBlockById(197));
        }
        if (hopper.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(154));
        }
        if (buttons.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(77));
            rightClickableBlocks.add(Block.getBlockById(143));
        }
        if (noteBlock.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(84));
            rightClickableBlocks.add(Block.getBlockById(25));
        }
        if (trapDoor.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(96));
            rightClickableBlocks.add(Block.getBlockById(167));
        }
        if (furnace.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(61));
            rightClickableBlocks.add(Block.getBlockById(62));
        }
        if (chest.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(130));
            rightClickableBlocks.add(Block.getBlockById(146));
            rightClickableBlocks.add(Block.getBlockById(54));
        }
        if (craftingTable.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(58));
        }
        if (gate.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(107));
            rightClickableBlocks.add(Block.getBlockById(183));
            rightClickableBlocks.add(Block.getBlockById(184));
            rightClickableBlocks.add(Block.getBlockById(185));
            rightClickableBlocks.add(Block.getBlockById(186));
            rightClickableBlocks.add(Block.getBlockById(187));
        }
        if (anvil.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(145));
        }
        if (dispenser.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(23));
        }
        if (lever.getCurrentValue()) {
            rightClickableBlocks.add(Block.getBlockById(69));
        }
        return rightClickableBlocks;
    }
}


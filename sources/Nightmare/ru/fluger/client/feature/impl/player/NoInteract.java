// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import java.util.ArrayList;
import java.util.List;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class NoInteract extends Feature
{
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
        NoInteract.armorStands = new BooleanSetting("Armor Stand", true, () -> true);
        NoInteract.chest = new BooleanSetting("Chest", false, () -> true);
        NoInteract.doors = new BooleanSetting("Doors", true, () -> true);
        NoInteract.hopper = new BooleanSetting("Hopper", true, () -> true);
        NoInteract.buttons = new BooleanSetting("Buttons", true, () -> true);
        NoInteract.dispenser = new BooleanSetting("Dispenser", true, () -> true);
        NoInteract.noteBlock = new BooleanSetting("Note Block", true, () -> true);
        NoInteract.craftingTable = new BooleanSetting("Crafting Table", true, () -> true);
        NoInteract.trapDoor = new BooleanSetting("TrapDoor", true, () -> true);
        NoInteract.furnace = new BooleanSetting("Furnace", true, () -> true);
        NoInteract.gate = new BooleanSetting("Gate", true, () -> true);
        NoInteract.anvil = new BooleanSetting("Anvil", true, () -> true);
        NoInteract.lever = new BooleanSetting("Lever", true, () -> true);
        this.addSettings(NoInteract.armorStands, NoInteract.chest, NoInteract.doors, NoInteract.hopper, NoInteract.buttons, NoInteract.dispenser, NoInteract.noteBlock, NoInteract.craftingTable, NoInteract.trapDoor, NoInteract.furnace, NoInteract.gate, NoInteract.anvil, NoInteract.lever);
    }
    
    public static List<aow> getRightClickableBlocks() {
        final ArrayList<aow> rightClickableBlocks = new ArrayList<aow>();
        if (NoInteract.doors.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(64));
            rightClickableBlocks.add(aow.c(71));
            rightClickableBlocks.add(aow.c(193));
            rightClickableBlocks.add(aow.c(194));
            rightClickableBlocks.add(aow.c(195));
            rightClickableBlocks.add(aow.c(196));
            rightClickableBlocks.add(aow.c(197));
        }
        if (NoInteract.hopper.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(154));
        }
        if (NoInteract.buttons.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(77));
            rightClickableBlocks.add(aow.c(143));
        }
        if (NoInteract.noteBlock.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(84));
            rightClickableBlocks.add(aow.c(25));
        }
        if (NoInteract.trapDoor.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(96));
            rightClickableBlocks.add(aow.c(167));
        }
        if (NoInteract.furnace.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(61));
            rightClickableBlocks.add(aow.c(62));
        }
        if (NoInteract.chest.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(130));
            rightClickableBlocks.add(aow.c(146));
            rightClickableBlocks.add(aow.c(54));
        }
        if (NoInteract.craftingTable.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(58));
        }
        if (NoInteract.gate.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(107));
            rightClickableBlocks.add(aow.c(183));
            rightClickableBlocks.add(aow.c(184));
            rightClickableBlocks.add(aow.c(185));
            rightClickableBlocks.add(aow.c(186));
            rightClickableBlocks.add(aow.c(187));
        }
        if (NoInteract.anvil.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(145));
        }
        if (NoInteract.dispenser.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(23));
        }
        if (NoInteract.lever.getCurrentValue()) {
            rightClickableBlocks.add(aow.c(69));
        }
        return rightClickableBlocks;
    }
}

package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.block.Block;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;

import java.util.ArrayList;
import java.util.List;

public class NoInteract extends Feature {
	  public static MultipleBoolSetting sel = new MultipleBoolSetting("Object selection",
	    		new BooleanSetting("Players", true),
	    		new BooleanSetting("Chest",true),
	    		new BooleanSetting("Doors",true),
	    		new BooleanSetting("Hopper",true),
	    		new BooleanSetting("Buttons",true),
	    		new BooleanSetting("Dispenser",true),
	    		new BooleanSetting("Note Block",true),
	    		new BooleanSetting("Crafting Table",true),
	    		new BooleanSetting("Trap Door",true),
	    		new BooleanSetting("Furnace",true),
	    		new BooleanSetting("Gate",true),
	    		new BooleanSetting("Anvil",true),
	    		new BooleanSetting("Lever",true),
	    		new BooleanSetting("Other",true));

    public NoInteract() {
        super("No Interact", "Убирает все не нужные блоки на которые вы можете нажать", FeatureCategory.Player);
        addSettings(sel);
    }

    public static List<Block> getRightClickableBlocks() {
        ArrayList<Block> rightClickableBlocks = new ArrayList<Block>();
        if (sel.getSetting("Doors").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(64));
            rightClickableBlocks.add(Block.getBlockById(71));
            rightClickableBlocks.add(Block.getBlockById(193));
            rightClickableBlocks.add(Block.getBlockById(194));
            rightClickableBlocks.add(Block.getBlockById(195));
            rightClickableBlocks.add(Block.getBlockById(196));
            rightClickableBlocks.add(Block.getBlockById(197));
        }
        if (sel.getSetting("Hopper").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(154));
        }
        if (sel.getSetting("Buttons").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(77));
            rightClickableBlocks.add(Block.getBlockById(143));
        }
        if (sel.getSetting("Note Block").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(84));
            rightClickableBlocks.add(Block.getBlockById(25));
        }
        if (sel.getSetting("Trap Door").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(96));
            rightClickableBlocks.add(Block.getBlockById(167));
        }
        if (sel.getSetting("Furnace").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(61));
            rightClickableBlocks.add(Block.getBlockById(62));
        }
        if (sel.getSetting("Chest").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(130));
            rightClickableBlocks.add(Block.getBlockById(146));
            rightClickableBlocks.add(Block.getBlockById(54));
        }
        if (sel.getSetting("Crafting Table").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(58));
        }
        if (sel.getSetting("Gate").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(107));
            rightClickableBlocks.add(Block.getBlockById(183));
            rightClickableBlocks.add(Block.getBlockById(184));
            rightClickableBlocks.add(Block.getBlockById(185));
            rightClickableBlocks.add(Block.getBlockById(186));
            rightClickableBlocks.add(Block.getBlockById(187));
        }
        if (sel.getSetting("Anvil").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(145));
        }
        if (sel.getSetting("Dispenser").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(23));
        }
        if (sel.getSetting("Lever").getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(69));
        }
        if (sel.getSetting("Other").getBoolValue()) {
        	 for (int a = 0; a < 256; a ++) {
            	 rightClickableBlocks.add(Block.getBlockById(255));
            }
        }
        return rightClickableBlocks;
    }
}
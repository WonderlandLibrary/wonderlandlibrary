// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import java.util.ArrayList;
import net.minecraft.block.Block;
import java.util.List;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoInteract", desc = "\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class NoInteract extends Module
{
    public static MultiBoxSetting ignoreInteract;
    
    public NoInteract() {
        this.add(NoInteract.ignoreInteract);
    }
    
    public static List<Block> getBlocks() {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (NoInteract.ignoreInteract.get(2)) {
            blocks.add(Block.getBlockById(64));
            blocks.add(Block.getBlockById(71));
            blocks.add(Block.getBlockById(193));
            blocks.add(Block.getBlockById(194));
            blocks.add(Block.getBlockById(195));
            blocks.add(Block.getBlockById(196));
            blocks.add(Block.getBlockById(197));
        }
        if (NoInteract.ignoreInteract.get(4)) {
            blocks.add(Block.getBlockById(154));
        }
        if (NoInteract.ignoreInteract.get(3)) {
            blocks.add(Block.getBlockById(77));
            blocks.add(Block.getBlockById(143));
        }
        if (NoInteract.ignoreInteract.get(6)) {
            blocks.add(Block.getBlockById(84));
            blocks.add(Block.getBlockById(25));
        }
        if (NoInteract.ignoreInteract.get(8)) {
            blocks.add(Block.getBlockById(96));
            blocks.add(Block.getBlockById(167));
        }
        if (NoInteract.ignoreInteract.get(9)) {
            blocks.add(Block.getBlockById(61));
            blocks.add(Block.getBlockById(62));
        }
        if (NoInteract.ignoreInteract.get(1)) {
            blocks.add(Block.getBlockById(130));
            blocks.add(Block.getBlockById(146));
            blocks.add(Block.getBlockById(54));
        }
        if (NoInteract.ignoreInteract.get(7)) {
            blocks.add(Block.getBlockById(58));
        }
        if (NoInteract.ignoreInteract.get(10)) {
            blocks.add(Block.getBlockById(107));
            blocks.add(Block.getBlockById(183));
            blocks.add(Block.getBlockById(184));
            blocks.add(Block.getBlockById(185));
            blocks.add(Block.getBlockById(186));
            blocks.add(Block.getBlockById(187));
        }
        if (NoInteract.ignoreInteract.get(11)) {
            blocks.add(Block.getBlockById(145));
        }
        if (NoInteract.ignoreInteract.get(5)) {
            blocks.add(Block.getBlockById(23));
        }
        if (NoInteract.ignoreInteract.get(12)) {
            blocks.add(Block.getBlockById(69));
        }
        return blocks;
    }
    
    static {
        NoInteract.ignoreInteract = new MultiBoxSetting("Object", new String[] { "Armor Stand", "Chest", "Door", "Button", "Hopper", "Dispenser", "Note Blocks", "Crafting Table", "Trap Door", "Furnace", "Gate", "Anvil", "Lever" });
    }
}

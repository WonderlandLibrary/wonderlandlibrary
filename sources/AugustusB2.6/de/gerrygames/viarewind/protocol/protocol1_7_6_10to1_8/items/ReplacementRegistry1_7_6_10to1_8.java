// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items;

import de.gerrygames.viarewind.replacement.Replacement;
import de.gerrygames.viarewind.storage.BlockState;
import com.viaversion.viaversion.api.minecraft.item.Item;
import de.gerrygames.viarewind.replacement.ReplacementRegistry;

public class ReplacementRegistry1_7_6_10to1_8
{
    private static final ReplacementRegistry registry;
    
    public static Item replace(final Item item) {
        return ReplacementRegistry1_7_6_10to1_8.registry.replace(item);
    }
    
    public static int replace(final int raw) {
        final int data = BlockState.extractData(raw);
        final Replacement replace = ReplacementRegistry1_7_6_10to1_8.registry.replace(BlockState.extractId(raw), data);
        return (replace != null) ? BlockState.stateToRaw(replace.getId(), replace.replaceData(data)) : raw;
    }
    
    public static Replacement getReplacement(final int id, final int data) {
        return ReplacementRegistry1_7_6_10to1_8.registry.replace(id, data);
    }
    
    static {
        (registry = new ReplacementRegistry()).registerBlock(176, new Replacement(63));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(177, new Replacement(68));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(193, new Replacement(64));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(194, new Replacement(64));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(195, new Replacement(64));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(196, new Replacement(64));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(197, new Replacement(64));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(77, 5, new Replacement(69, 6));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(77, 13, new Replacement(69, 14));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(77, 0, new Replacement(69, 0));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(77, 8, new Replacement(69, 8));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(143, 5, new Replacement(69, 6));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(143, 13, new Replacement(69, 14));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(143, 0, new Replacement(69, 0));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(143, 8, new Replacement(69, 8));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(178, new Replacement(151));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(182, 0, new Replacement(44, 1));
        ReplacementRegistry1_7_6_10to1_8.registry.registerBlock(182, 8, new Replacement(44, 9));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(425, new Replacement(323, "Banner"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(409, new Replacement(406, "Prismarine Shard"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(410, new Replacement(406, "Prismarine Crystal"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(416, new Replacement(280, "Armor Stand"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(423, new Replacement(363, "Raw Mutton"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(424, new Replacement(364, "Cooked Mutton"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(411, new Replacement(365, "Raw Rabbit"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(412, new Replacement(366, "Cooked Rabbit"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(413, new Replacement(282, "Rabbit Stew"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(414, new Replacement(375, "Rabbit's Foot"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(415, new Replacement(334, "Rabbit Hide"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(373, 8203, new Replacement(373, 0, "Potion of Leaping"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(373, 8235, new Replacement(373, 0, "Potion of Leaping"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(373, 8267, new Replacement(373, 0, "Potion of Leaping"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(373, 16395, new Replacement(373, 0, "Splash Potion of Leaping"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(373, 16427, new Replacement(373, 0, "Splash Potion of Leaping"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(373, 16459, new Replacement(373, 0, "Splash Potion of Leaping"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(383, 30, new Replacement(383, "Spawn ArmorStand"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(383, 67, new Replacement(383, "Spawn Endermite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(383, 68, new Replacement(383, "Spawn Guardian"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(383, 101, new Replacement(383, "Spawn Rabbit"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(19, 1, new Replacement(19, 0, "Wet Sponge"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItem(182, new Replacement(44, 1, "Red Sandstone Slab"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(166, new Replacement(20, "Barrier"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(167, new Replacement(96, "Iron Trapdoor"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(1, 1, new Replacement(1, 0, "Granite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(1, 2, new Replacement(1, 0, "Polished Granite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(1, 3, new Replacement(1, 0, "Diorite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(1, 4, new Replacement(1, 0, "Polished Diorite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(1, 5, new Replacement(1, 0, "Andesite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(1, 6, new Replacement(1, 0, "Polished Andesite"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(168, 0, new Replacement(1, 0, "Prismarine"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(168, 1, new Replacement(98, 0, "Prismarine Bricks"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(168, 2, new Replacement(98, 1, "Dark Prismarine"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(169, new Replacement(89, "Sea Lantern"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(165, new Replacement(95, 5, "Slime Block"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(179, 0, new Replacement(24, "Red Sandstone"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(179, 1, new Replacement(24, "Chiseled Red Sandstone"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(179, 2, new Replacement(24, "Smooth Sandstone"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(181, new Replacement(43, 1, "Double Red Sandstone Slab"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(180, new Replacement(128, "Red Sandstone Stairs"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(188, new Replacement(85, "Spruce Fence"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(189, new Replacement(85, "Birch Fence"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(190, new Replacement(85, "Jungle Fence"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(191, new Replacement(85, "Dark Oak Fence"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(192, new Replacement(85, "Acacia Fence"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(183, new Replacement(107, "Spruce Fence Gate"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(184, new Replacement(107, "Birch Fence Gate"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(185, new Replacement(107, "Jungle Fence Gate"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(186, new Replacement(107, "Dark Oak Fence Gate"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(187, new Replacement(107, "Acacia Fence Gate"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(427, new Replacement(324, "Spruce Door"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(428, new Replacement(324, "Birch Door"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(429, new Replacement(324, "Jungle Door"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(430, new Replacement(324, "Dark Oak Door"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(431, new Replacement(324, "Acacia Door"));
        ReplacementRegistry1_7_6_10to1_8.registry.registerItemBlock(157, new Replacement(28, "Activator Rail"));
    }
}

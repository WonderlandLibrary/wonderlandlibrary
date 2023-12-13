// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.items;

import de.gerrygames.viarewind.replacement.Replacement;
import de.gerrygames.viarewind.storage.BlockState;
import com.viaversion.viaversion.api.minecraft.item.Item;
import de.gerrygames.viarewind.replacement.ReplacementRegistry;

public class ReplacementRegistry1_8to1_9
{
    private static final ReplacementRegistry registry;
    
    public static Item replace(final Item item) {
        return ReplacementRegistry1_8to1_9.registry.replace(item);
    }
    
    public static int replace(final int raw) {
        final int data = BlockState.extractData(raw);
        final Replacement replace = ReplacementRegistry1_8to1_9.registry.replace(BlockState.extractId(raw), data);
        if (replace == null) {
            return raw;
        }
        return BlockState.stateToRaw(replace.getId(), replace.replaceData(data));
    }
    
    public static Replacement getReplacement(final int id, final int data) {
        return ReplacementRegistry1_8to1_9.registry.replace(id, data);
    }
    
    static {
        (registry = new ReplacementRegistry()).registerItem(198, new Replacement(50, 0, "End Rod"));
        ReplacementRegistry1_8to1_9.registry.registerItem(434, new Replacement(391, "Beetroot"));
        ReplacementRegistry1_8to1_9.registry.registerItem(435, new Replacement(361, "Beetroot Seeds"));
        ReplacementRegistry1_8to1_9.registry.registerItem(436, new Replacement(282, "Beetroot Soup"));
        ReplacementRegistry1_8to1_9.registry.registerItem(432, new Replacement(322, "Chorus Fruit"));
        ReplacementRegistry1_8to1_9.registry.registerItem(433, new Replacement(393, "Popped Chorus Fruit"));
        ReplacementRegistry1_8to1_9.registry.registerItem(437, new Replacement(373, "Dragons Breath"));
        ReplacementRegistry1_8to1_9.registry.registerItem(443, new Replacement(299, "Elytra"));
        ReplacementRegistry1_8to1_9.registry.registerItem(426, new Replacement(410, "End Crystal"));
        ReplacementRegistry1_8to1_9.registry.registerItem(442, new Replacement(425, "Shield"));
        ReplacementRegistry1_8to1_9.registry.registerItem(439, new Replacement(262, "Spectral Arrow"));
        ReplacementRegistry1_8to1_9.registry.registerItem(440, new Replacement(262, "Tipped Arrow"));
        ReplacementRegistry1_8to1_9.registry.registerItem(444, new Replacement(333, "Spruce Boat"));
        ReplacementRegistry1_8to1_9.registry.registerItem(445, new Replacement(333, "Birch Boat"));
        ReplacementRegistry1_8to1_9.registry.registerItem(446, new Replacement(333, "Jungle Boat"));
        ReplacementRegistry1_8to1_9.registry.registerItem(447, new Replacement(333, "Acacia Boat"));
        ReplacementRegistry1_8to1_9.registry.registerItem(448, new Replacement(333, "Dark Oak Boat"));
        ReplacementRegistry1_8to1_9.registry.registerItem(204, new Replacement(43, 7, "Purpur Double Slab"));
        ReplacementRegistry1_8to1_9.registry.registerItem(205, new Replacement(44, 7, "Purpur Slab"));
        ReplacementRegistry1_8to1_9.registry.registerBlock(209, new Replacement(119));
        ReplacementRegistry1_8to1_9.registry.registerBlock(198, 0, new Replacement(50, 5));
        ReplacementRegistry1_8to1_9.registry.registerBlock(198, 1, new Replacement(50, 5));
        ReplacementRegistry1_8to1_9.registry.registerBlock(198, 2, new Replacement(50, 4));
        ReplacementRegistry1_8to1_9.registry.registerBlock(198, 3, new Replacement(50, 3));
        ReplacementRegistry1_8to1_9.registry.registerBlock(198, 4, new Replacement(50, 2));
        ReplacementRegistry1_8to1_9.registry.registerBlock(198, 5, new Replacement(50, 1));
        ReplacementRegistry1_8to1_9.registry.registerBlock(204, new Replacement(43, 7));
        ReplacementRegistry1_8to1_9.registry.registerBlock(205, 0, new Replacement(44, 7));
        ReplacementRegistry1_8to1_9.registry.registerBlock(205, 8, new Replacement(44, 15));
        ReplacementRegistry1_8to1_9.registry.registerBlock(207, new Replacement(141));
        ReplacementRegistry1_8to1_9.registry.registerBlock(137, new Replacement(137, 0));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(199, new Replacement(35, 10, "Chorus Plant"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(200, new Replacement(35, 2, "Chorus Flower"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(201, new Replacement(155, 0, "Purpur Block"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(202, new Replacement(155, 2, "Purpur Pillar"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 0, new Replacement(156, 0, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 1, new Replacement(156, 1, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 2, new Replacement(156, 2, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 3, new Replacement(156, 3, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 4, new Replacement(156, 4, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 5, new Replacement(156, 5, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 6, new Replacement(156, 6, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 7, new Replacement(156, 7, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(203, 8, new Replacement(156, 8, "Purpur Stairs"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(206, new Replacement(121, 0, "Endstone Bricks"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(207, new Replacement(141, "Beetroot Block"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(208, new Replacement(2, 0, "Grass Path"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(209, new Replacement(90, "End Gateway"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(210, new Replacement(137, 0, "Repeating Command Block"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(211, new Replacement(137, 0, "Chain Command Block"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(212, new Replacement(79, 0, "Frosted Ice"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(214, new Replacement(87, 0, "Nether Wart Block"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(215, new Replacement(112, 0, "Red Nether Brick"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(217, new Replacement(166, 0, "Structure Void"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(255, new Replacement(137, 0, "Structure Block"));
        ReplacementRegistry1_8to1_9.registry.registerItemBlock(397, 5, new Replacement(397, 0, "Dragon Head"));
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemBlock;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public final class FastPlace extends Module
{
    private final NumberSetting ticks;
    private final BooleanSetting blocks;
    private final BooleanSetting projectiles;
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (this.canFastPlace()) {
            FastPlace.mc.rightClickDelayTimer = Math.min(FastPlace.mc.rightClickDelayTimer, this.ticks.getValue().intValue());
        }
    }
    
    @Override
    public void onDisable() {
        FastPlace.mc.rightClickDelayTimer = 4;
        super.onDisable();
    }
    
    private boolean canFastPlace() {
        if (FastPlace.mc.thePlayer == null || FastPlace.mc.thePlayer.getCurrentEquippedItem() == null || FastPlace.mc.thePlayer.getCurrentEquippedItem().getItem() == null) {
            return false;
        }
        final Item heldItem = FastPlace.mc.thePlayer.getCurrentEquippedItem().getItem();
        return (this.blocks.isEnabled() && heldItem instanceof ItemBlock) || (this.projectiles.isEnabled() && (heldItem instanceof ItemSnowball || heldItem instanceof ItemEgg));
    }
    
    public FastPlace() {
        super("FastPlace", "Fast Place", Category.PLAYER, "place blocks fast");
        this.ticks = new NumberSetting("Ticks", 0.0, 4.0, 0.0, 1.0);
        this.blocks = new BooleanSetting("Blocks", true);
        this.projectiles = new BooleanSetting("Projectiles", true);
        this.addSettings(this.ticks, this.blocks, this.projectiles);
    }
}

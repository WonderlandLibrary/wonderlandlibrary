// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import dev.tenacity.event.impl.player.MotionEvent;
import net.minecraft.util.AxisAlignedBB;
import dev.tenacity.event.impl.player.BoundingBoxEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Jesus extends Module
{
    private final ModeSetting mode;
    private boolean shouldJesus;
    
    public Jesus() {
        super("Jesus", "Jesus", Category.MOVEMENT, "Walks on water, like jesus");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Verus" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onBoundingBoxEvent(final BoundingBoxEvent event) {
        if (event.getBlock().getMaterial().isLiquid()) {
            final AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(-5.0, -1.0, -5.0, 5.0, 1.0, 5.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ());
            this.shouldJesus = true;
            event.setBoundingBox(axisAlignedBB);
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.mode.getMode());
        if (event.isPre()) {
            if (this.shouldJesus) {
                final String mode = this.mode.getMode();
                switch (mode) {
                    case "Verus": {
                        if (Jesus.mc.thePlayer.ticksExisted % 5 == 0) {
                            Jesus.mc.thePlayer.setPosition(Jesus.mc.thePlayer.posX, Jesus.mc.thePlayer.posY + 0.1, Jesus.mc.thePlayer.posZ);
                            break;
                        }
                        break;
                    }
                }
            }
            this.shouldJesus = false;
        }
    }
}

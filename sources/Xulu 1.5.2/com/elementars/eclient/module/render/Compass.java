// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.util.math.MathHelper;
import com.elementars.eclient.util.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Compass extends Module
{
    public final Value<Boolean> axis;
    public final Value<Integer> scale;
    public final Value<Float> position;
    public final Value<Integer> xposition;
    private static final double HALF_PI = 1.5707963267948966;
    
    public Compass() {
        super("Compass", "Credit to fr1kin", 0, Category.RENDER, true);
        this.axis = this.register(new Value<Boolean>("Axis", this, false));
        this.scale = this.register(new Value<Integer>("Scale", this, 3, 1, 10));
        this.position = this.register(new Value<Float>("Y Position", this, 8.0f, 0.0f, 10.0f));
        this.xposition = this.register(new Value<Integer>("X Position", this, 0, -500, 500));
    }
    
    @Override
    public void onRender() {
        GlStateManager.pushMatrix();
        final double centerX = Compass.mc.displayWidth / 4 + this.xposition.getValue();
        final double centerY = Compass.mc.displayHeight / 2 * (this.position.getValue() / 10.0f);
        for (final Direction dir : Direction.values()) {
            final double rad = getPosOnCompass(dir);
            Compass.mc.fontRenderer.drawStringWithShadow(((boolean)this.axis.getValue()) ? dir.getAlternate() : dir.name(), (float)(centerX + this.getX(rad)), (float)(centerY + this.getY(rad)), (dir == Direction.N) ? ColorUtils.Colors.RED : ColorUtils.Colors.WHITE);
        }
        GlStateManager.popMatrix();
    }
    
    private double getX(final double rad) {
        return Math.sin(rad) * (this.scale.getValue() * 10);
    }
    
    private double getY(final double rad) {
        final double epicPitch = MathHelper.clamp(Compass.mc.player.rotationPitch + 30.0f, -90.0f, 90.0f);
        final double pitchRadians = Math.toRadians(epicPitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (this.scale.getValue() * 10);
    }
    
    private static double getPosOnCompass(final Direction dir) {
        final double yaw = Math.toRadians(MathHelper.wrapDegrees(Compass.mc.player.rotationYaw));
        final int index = dir.ordinal();
        return yaw + index * 1.5707963267948966;
    }
    
    private enum Direction
    {
        N("-Z"), 
        W("-X"), 
        S("+Z"), 
        E("+X");
        
        private String alternate;
        
        private Direction(final String alternate) {
            this.alternate = alternate;
        }
        
        public String getAlternate() {
            return this.alternate;
        }
    }
}

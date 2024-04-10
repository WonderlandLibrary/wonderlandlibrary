package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.VecUtils;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.player.ScaffoldUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.ࡅ揩柿괠竁頉;
import static info.sigmaclient.sigma.gui.Sigma5LoadProgressGui.霥瀳놣㠠釒;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class CakeEater extends Module {
    public NumberValue range = new NumberValue("Range", 3, 0, 6, NumberValue.NUMBER_TYPE.INT);
    public NumberValue delay = new NumberValue("Delay", 0, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public BooleanValue rot = new BooleanValue("Rotations", false);
    public BooleanValue nw = new BooleanValue("NoSwing", false);
    public ColorValue colorValue = new ColorValue("Color", -1);
    public CakeEater() {
        super("CakeEater", Category.World, "Eat blocks.");
     registerValue(range);
     registerValue(delay);
     registerValue(rot);
     registerValue(nw);
     registerValue(colorValue);
    }
    ScaffoldUtils.BlockCache currentPos = null;
    ScaffoldUtils.BlockCache lastCurrentPos = null;
    float sb = 0;
    int delays = 0;
    public boolean isOkBlock(BlockPos block2){
        Block block = mc.world.getBlockState(block2).getBlock();
        if(mc.player.getDistance(block2.getX() + 0.5, block2.getY() + 0.5, block2.getZ() + 0.5) - 0.5f > range.getValue().floatValue() + 0.5f) return false;
        return block instanceof CakeBlock;
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if (this.currentPos != null) {
            final int 霥瀳놣㠠釒 = 霥瀳놣㠠釒(colorValue.getColorInt(), 0.4f);
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            RenderUtils.renderPos r = RenderUtils.getRenderPos();
            final double n = currentPos.getPosition().getX() - r.renderPosX;
            final double n2 = currentPos.getPosition().getY() - r.renderPosY;
            final double n3 = currentPos.getPosition().getZ() - r.renderPosZ;
            ࡅ揩柿괠竁頉(new AxisAlignedBB(n, n2, n3, n + 1, n2 + 1, n3 + 1), 霥瀳놣㠠釒);
            GL11.glEnable(2929);
            GL11.glPopMatrix();
        }
        super.onRender3DEvent(event);
    }

    public ScaffoldUtils.BlockCache getBlockCache(BlockPos pos, int range) {
        for(int x = -range; x < range; x ++){
            for(int y = -range; y < range; y ++){
                for(int z = -range; z < range; z ++){
                    BlockPos pos1 = pos.add(x,y,z);
                    if (isOkBlock(pos1))
                        return new ScaffoldUtils.BlockCache(pos1, Direction.UP);
                }
            }
        }
        return null;
    }
    public void stop(){
        sb = 0;
        delays = 0;
        if(lastCurrentPos != null) {
            lastCurrentPos = null;
        }
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            // LOL
            ScaffoldUtils.BlockCache blockCache = getBlockCache(new BlockPos(mc.player.getPositionVector()) ,range.getValue().intValue());
            boolean stop = false;
            currentPos = blockCache;
            if(blockCache == null){
                if(currentPos != null){
                    currentPos = null;
                    stop = true;
                }
            }
            if(currentPos != null && mc.player.getDistance(currentPos.getPosition().getX() + 0.5, currentPos.getPosition().getY() + 0.5, currentPos.getPosition().getZ() + 0.5) - 0.5f > range.getValue().floatValue() + 0.5f){
                stop = true;
                currentPos = null;
            }
            if(delays > 0){
                delays --;
                currentPos = null;
                stop = true;
            }
            if(stop || currentPos == null){
                stop();
                return;
            }
            BlockPos currentPos = this.currentPos.getPosition();
            if(rot.isEnable()){
                float[] rots = RotationUtils.scaffoldRots(currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, mc.player.prevRotationYaw, mc.player.prevRotationPitch, 180, 180, false);
                event.yaw = rots[0];
                event.pitch = rots[1];
            }
            if(lastCurrentPos == null) {
                lastCurrentPos = this.currentPos;
            }
            mc.playerController.processRightClickBlock(mc.player,mc.world,currentPos, blockCache.getFacing(),
                    VecUtils.blockPosRedirection(currentPos, blockCache.getFacing()),Hand.MAIN_HAND);
            if(!nw.isEnable())
                mc.player.swingArm(Hand.MAIN_HAND);


//            lastCurrentPos = currentPos;
        }
        super.onUpdateEvent(event);
    }
    @Override
    public void onEnable() {
        currentPos = null;
        sb = 0;
        delays = 0;
        stop();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(currentPos != null){
            stop();
            lastCurrentPos = null;
            currentPos = null;
        }
        super.onDisable();
    }
}

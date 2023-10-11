package markgg.modules.impl.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ibm.icu.text.Normalizer.Mode;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.util.MoveUtil;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "OldScaffold", category = Module.Category.MOVEMENT)
public class OldScaffold extends Module {
	
	public ModeSetting state = new ModeSetting("State", this, "Pre", "Pre", "Post");
	
	public BooleanSetting noSwing = new BooleanSetting("No Swing", this, false);
	public BooleanSetting tower = new BooleanSetting("Tower", this, true);
	public BooleanSetting sprint = new BooleanSetting("Sprint", this, true);
	
    private void doSprint() {
        if (!MoveUtil.isMoving() || mc.thePlayer.isSprinting()) return;

        if(sprint.getValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }
	
    private void doSwing() {
        if(noSwing.getValue()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        } else {
            mc.thePlayer.swingItem();
        }
    }

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (mc.theWorld == null || mc.thePlayer == null)
            return;
		
		doSprint();
		
		//Rotations
		e.setPitch(getScaffoldPitch());
		e.setYaw(mc.thePlayer.rotationYaw + 180);	
		mc.thePlayer.rotationYawHead = e.getYaw();
		mc.thePlayer.renderYawOffset = e.getPitch();

		// Tower
		if(tower.getValue()) {
			if(mc.gameSettings.keyBindJump.pressed && !mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)))
				mc.thePlayer.motionY = 0.4008F;
		}
		
        BlockPos playerBlock = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        BlockData data = findScaffoldBlock();

        if (data == null) {
            return;
        }
		
		
		if(e.getType() == MotionEvent.Type.PRE){
			if(state.getMode().equals("Pre"))
				placeBlock(data);
		}
		if(e.getType() == MotionEvent.Type.POST){
			if(state.getMode().equals("Post"))
				placeBlock(data);
		}
	};
	
	private void placeBlock(BlockData data) {
		doSwing();
		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), data.pos, data.face, new Vec3(data.pos.getX(), data.pos.getY(), data.pos.getZ()));
	}

	BlockData getBlockData(BlockPos pos) {
		if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air)
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP); 
		if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air)
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST); 
		if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air)
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST); 
		if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air)
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH); 
		if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air)
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		return null;
	}

	public class BlockData
	{
		public final BlockPos pos;
		public final EnumFacing face;

		BlockData(BlockPos pos, EnumFacing face) {
			this.pos = pos;
			this.face = face;
		}
	}

	private float getScaffoldPitch() {
		float random = (float) Math.random();
		float pitch = 81 + random;

		if(mc.gameSettings.keyBindJump.pressed)
			return 90;

		return pitch;
	}
	
    private BlockData findScaffoldBlock() {
        BlockData data = null;

        double yDif;
        for (double posY = mc.thePlayer.posY - 1.0D; posY > 0.0D; posY--) {
            BlockData newData = getBlockData(new BlockPos(mc.thePlayer.posX, posY, mc.thePlayer.posZ));
            if (newData != null) {
                yDif = mc.thePlayer.posY - posY;
                if (yDif <= 3.0D) {
                    data = newData;
                    break;
                }
            }
        }

        if (data == null) {
            return null;
        }

        if (Objects.equals(data.pos, new BlockPos(0, -1, 0))) {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        }

        return data;
    }

}

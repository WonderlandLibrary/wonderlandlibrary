package wtf.monsoon.impl.modules.player;

import net.minecraft.block.Block;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import wtf.monsoon.api.util.entity.SpeedUtil;
import wtf.monsoon.api.util.misc.MathUtils;
import net.minecraft.block.BlockAir;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.event.impl.EventReceivePacket;
import wtf.monsoon.api.event.impl.EventRender3D;
import wtf.monsoon.api.event.impl.EventRenderPlayer;
import wtf.monsoon.api.event.impl.EventSendPacket;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.entity.RotationUtils;
import wtf.monsoon.api.util.misc.PacketUtil;
import wtf.monsoon.api.util.misc.ServerUtil;
import wtf.monsoon.api.util.misc.Timer;
import wtf.monsoon.api.util.render.RenderUtil;
import wtf.monsoon.api.util.world.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import wtf.monsoon.api.Wrapper;


import static net.minecraft.util.EnumFacing.*;

public class Scaffold extends Module {

	public BooleanSetting sprint = new BooleanSetting("Allow Sprinting", false, this);
	public BooleanSetting safewalk = new BooleanSetting("Safewalk", true, this);
	public BooleanSetting timerBoost = new BooleanSetting("Timer Boost", false, this);
	public BooleanSetting slowDown = new BooleanSetting("Slowdown", true, this);
	public NumberSetting placeDelay = new NumberSetting("Delay", 70, 0, 200, 1, this);
	public NumberSetting timerAmount = new NumberSetting("Boost Amount", 1.15, 1, 6, 0.05, this, false);
	public ModeSetting swing = new ModeSetting("Swing", this, "NoSwing","NoSwing", "Client");
	public BooleanSetting stopOnPlace = new BooleanSetting("Stop Movment on Place", false, this);
	public BooleanSetting sneakOnPlace = new BooleanSetting("Sneak on Place", false, this);
	public BooleanSetting sprintOnPlace = new BooleanSetting("Toggle Sprint on place", false, this);
	public BooleanSetting jumpOnDisable = new BooleanSetting("Jump on Disable", false, this);
	public BooleanSetting spoofSneak = new BooleanSetting("Spoof Sneak", false, this);
	public BooleanSetting randomizeRots = new BooleanSetting("Randomize Rotations", true, this);


	public Scaffold() {
		super("Scaffold", "Place blocks under you", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(sprint,safewalk,timerBoost,timerAmount,placeDelay,slowDown, spoofSneak,swing,stopOnPlace,sneakOnPlace,sprintOnPlace,jumpOnDisable,randomizeRots);
	}
	public static transient BlockPos lastBlockPos = null;
	public static transient EnumFacing lastFacing = null;
	public static transient Timer timer = new Timer(), boosterTimer = new Timer(), towerTimer = new Timer();
	private static transient double keepPosY = 0;
	private BlockPos placing;

	private float renderYaw, renderPitch;

	int currentSlot;
	int currentItem;
	int lastSlot;


	public void onEnable() {
		super.onEnable();
		timer.reset();
		towerTimer.reset();
		//updateHotbarHypixel();
		boosterTimer.reset();
		lastSlot = -1;
		keepPosY = Wrapper.mc.thePlayer.posY - 1;

		BlockPos block = new BlockPos(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 1, Wrapper.mc.thePlayer.posZ);

		for (double i = Wrapper.mc.thePlayer.posY - 1; i > Wrapper.mc.thePlayer.posY - 5; i -= 0.5) {

			try {
				if (Wrapper.mc.theWorld.getBlockState(block).getBlock() != Blocks.air) {

					keepPosY = block.getY();
					break;

				}
			} catch (Exception e) {

			}
			block = block.add(0, -1, 0);

		}

		if(spoofSneak.isEnabled()) {
			PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}

	}

	public void onDisable() {
		super.onDisable();
		Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Wrapper.mc.thePlayer.inventory.currentItem));
		lastSlot = -1;
		Wrapper.mc.gameSettings.keyBindSneak.pressed = false;
		Wrapper.mc.timer.timerSpeed = 1F;
		if(mc.thePlayer.onGround && mc.gameSettings.keyBindForward.isKeyDown() && jumpOnDisable.isEnabled()) {
			mc.thePlayer.jump();
			SpeedUtil.setSpeed(0.35f);
		}

		if(spoofSneak.isEnabled()) {
			PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
	}
	
	@EventTarget
	public void onGetPacket(EventReceivePacket e) {
		if (Monsoon.INSTANCE.manager.killAura.isEnabled() && (Monsoon.INSTANCE.manager.killAura.target != null)) {
			return;
		}

		if (e.packet instanceof S2FPacketSetSlot) {
			lastSlot = ((S2FPacketSetSlot)((EventReceivePacket)e).packet).slot;
		}

		if(e.getPacket() instanceof S08PacketPlayerPosLook) {
			mc.timer.timerSpeed = 1.0f;
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket e) {
		if (Monsoon.INSTANCE.manager.killAura.isEnabled() && (Monsoon.INSTANCE.manager.killAura.target != null)) {
			return;
		}

		if (e.packet instanceof C09PacketHeldItemChange) {
			lastSlot = ((C09PacketHeldItemChange)e.packet).getSlotId();
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {
		
		if(timerBoost.isEnabled() && !Wrapper.mc.gameSettings.keyBindJump.isKeyDown())
			Wrapper.mc.timer.timerSpeed = (float) timerAmount.getValue();

		if(Wrapper.mc.gameSettings.keyBindJump.isKeyDown() && !(Wrapper.mc.theWorld.getBlockState(new BlockPos(Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY - 1, Wrapper.mc.thePlayer.posZ)) == Blocks.air)) {
			mc.timer.timerSpeed = 1.0f;
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.238, mc.thePlayer.posZ);
		}

		if(slowDown.isEnabled()) {
			if(ServerUtil.isHypixel()) {
				Wrapper.mc.thePlayer.motionX *= 0.79;
				Wrapper.mc.thePlayer.motionZ *= 0.79;
			}
			if(ServerUtil.isRedesky()) {
				Wrapper.mc.thePlayer.motionX *= 0.92;
				Wrapper.mc.thePlayer.motionZ *= 0.92;
			}
			if(ServerUtil.isMineplex()) {
				Wrapper.mc.thePlayer.motionX *= 0.77;
				Wrapper.mc.thePlayer.motionZ *= 0.77;
			}
		}
	
	}
	
	@EventTarget
	public void onRender3D(EventRender3D e) {
		BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (mc.thePlayer.movementInput.sneak ? 1 : 0), mc.thePlayer.posZ);
		if(Wrapper.mc.theWorld.getBlockState(playerPos).getBlock() == Blocks.air) {
			RenderUtil.drawBox(playerPos, 1, 0, 0, true);
		} else {
			RenderUtil.drawBox(playerPos, 0, 1, 0, true);
		}
	}
	
	@EventTarget
	@SuppressWarnings(value = { "all" })
	public void onPreMotion(EventPreMotion e) {
		BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (mc.thePlayer.movementInput.sneak ? 1 : 0), mc.thePlayer.posZ);
		BlockUtil.BlockData info;
		if(mc.theWorld.getBlockState(playerPos).getBlock() == Blocks.air) {
			info = getDiagonalBlockData(playerPos);
		} else {
			info = getBlockData(playerPos);
		}

		Wrapper.mc.thePlayer.setSprinting(sprint.isEnabled());

		ItemStack stackToPlace = setStackToPlace();

		if (playerPos == null || info == null) {
			return;
		}
		
		//Monsoon.sendMessage(e.getYaw() + " " + e.getPitch());

		float[] rots = getRotations(randomizeRots.getValue());
		e.setYaw(rots[0]);
		e.setPitch(rots[1]);

		renderYaw = e.getYaw();
		renderPitch = e.getPitch();

		//Monsoon.sendMessage(e.getYaw() + " " + e.getPitch());

		if(timer.hasTimeElapsed((long) placeDelay.getValue(), false) && canLegitPlaceBlock()) {
			Wrapper.mc.playerController.onPlayerRightClick(Wrapper.mc.thePlayer, Wrapper.mc.theWorld, stackToPlace, info.pos, info.facing, RotationUtils.getVectorForRotation(e.yaw,e.pitch));
			swingItem();
			if(stopOnPlace.isEnabled()) {
				mc.thePlayer.motionX *= 0;
				mc.thePlayer.motionZ *= 0;
			}
			if(sneakOnPlace.isEnabled()) {
				//PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
				//PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
				mc.thePlayer.setSneaking(true);
			}
			if(sprintOnPlace.isEnabled()) {
				mc.thePlayer.setSprinting(!mc.thePlayer.isSprinting());
			}
		}

	
	}
	
	@EventTarget
	public void onRenderPlayer(EventRenderPlayer e) {
		e.setYaw(renderYaw);
		e.setPitch(renderPitch);
	}

	public float[] getRotations(boolean random) {

		float yaw, pitch;

		if (this.mc.gameSettings.keyBindBack.isKeyDown() && this.mc.gameSettings.keyBindLeft.isKeyDown()) {
			if (this.mc.thePlayer.rotationYaw <= 135.0f) {
				yaw = (this.mc.thePlayer.rotationYaw + 45.0f);
			}
			else {
				yaw = (this.mc.thePlayer.rotationYaw - 315.0f);
			}
		}
		else if (this.mc.gameSettings.keyBindBack.isKeyDown() && this.mc.gameSettings.keyBindRight.isKeyDown()) {
			if (this.mc.thePlayer.rotationYaw >= -135.0f) {
				yaw = (this.mc.thePlayer.rotationYaw - 45.0f);
			}
			else {
				yaw = (this.mc.thePlayer.rotationYaw + 315.0f);
			}
		}
		else if (this.mc.gameSettings.keyBindLeft.isKeyDown() && this.mc.gameSettings.keyBindForward.isKeyDown()) {
			if (this.mc.thePlayer.rotationYaw <= 45.0f) {
				yaw = (this.mc.thePlayer.rotationYaw + 135.0f);
			}
			else {
				yaw = (this.mc.thePlayer.rotationYaw - 225.0f);
			}
		}
		else if (this.mc.gameSettings.keyBindRight.isKeyDown() && this.mc.gameSettings.keyBindForward.isKeyDown()) {
			if (this.mc.thePlayer.rotationYaw >= -45.0f) {
				yaw = (this.mc.thePlayer.rotationYaw - 135.0f);
			}
			else {
				yaw = (this.mc.thePlayer.rotationYaw + 225.0f);
			}
		}
		else if (this.mc.gameSettings.keyBindLeft.isKeyDown()) {
			if (this.mc.thePlayer.rotationYaw <= 90.0f) {
				yaw = (this.mc.thePlayer.rotationYaw + 90.0f);
			}
			else {
				yaw = (this.mc.thePlayer.rotationYaw - 270.0f);
			}
		}
		else if (this.mc.gameSettings.keyBindRight.isKeyDown()) {
			if (this.mc.thePlayer.rotationYaw >= -90.0f) {
				yaw = (this.mc.thePlayer.rotationYaw - 90.0f);
			}
			else {
				yaw = (this.mc.thePlayer.rotationYaw + 270.0f);
			}
		}
		else if (this.mc.gameSettings.keyBindBack.isKeyDown()) {
			yaw = (this.mc.thePlayer.rotationYaw);
		}
		else if (this.mc.thePlayer.rotationYaw >= 0.0f) {
			yaw = (this.mc.thePlayer.rotationYaw - 180.0f);
		}
		else {
			yaw = (this.mc.thePlayer.rotationYaw + 180.0f);
		}
		if(random) {
			pitch = ((float) MathUtils.randomNumber(96.3, 68.1f));
			yaw = (yaw + (float) MathUtils.randomNumber(23.1f, -23.1f));
		} else {
			pitch = 83.4f;
		}
		return new float[] { yaw, pitch };

	}

	private void swingItem() {
		if(swing.is("NoSwing")) {
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		} else if(swing.is("Client")) {
			Wrapper.mc.thePlayer.swingItem();
		}
	}

	public BlockUtil.BlockData getBlockData(BlockPos pos) {
		return (Wrapper.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) ?
				new BlockUtil.BlockData(pos.add(0, -1, 0), UP) : (
				(Wrapper.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) ?
						new BlockUtil.BlockData(pos.add(-1, 0, 0), EAST) : (
						(Wrapper.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) ?
								new BlockUtil.BlockData(pos.add(1, 0, 0), EnumFacing.WEST) : (
								(Wrapper.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) ?
										new BlockUtil.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH) : (
										(Wrapper.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) ?
												new BlockUtil.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null))));
	}

	public void updateHotbarHypixel() {
		ItemStack localItemStack = new ItemStack(Item.getItemById(261));
		try {
			for (int i = 36; i < 45; i++) {
				int j = i - 36;
				if ((!Container.canAddItemToSlot(Wrapper.mc.thePlayer.inventoryContainer.getSlot(i), localItemStack, true))
						&& ((Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) && (Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null)) {

					if (Wrapper.mc.thePlayer.inventory.currentItem == j) {
						break;
					}
					//mc.thePlayer.inventory.currentItem = j;
					this.currentItem = j;
					Wrapper.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(Wrapper.mc.thePlayer.inventory.currentItem));
					Wrapper.mc.playerController.updateController();
					break;
				}
			}
		} catch (Exception ignored) {
		}
	}
	
	public static void drawBox(BlockPos blockPos, int r, int g, int b, int a)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.5f);

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ, blockPos.getX() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosZ);
        
        RenderUtil.drawBox2(blockPos, r, g, b, true);
        RenderGlobal.drawOutlinedBoundingBox(axisAlignedBB, r, g, b, a);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

	public ItemStack setStackToPlace() {

		ItemStack block = Wrapper.mc.thePlayer.getCurrentEquippedItem();

		if (block != null && block.getItem() != null && !(block.getItem() instanceof ItemBlock)) {
			block = null;
		}

		int slot = Wrapper.mc.thePlayer.inventory.currentItem;

		for (short g = 0; g < 9; g++) {

			if (Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack()
					&& Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemBlock
					&& Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize != 0
					&& !((ItemBlock) Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock()
					.getLocalizedName().toLowerCase().contains("chest")
					&& !((ItemBlock) Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem()).getBlock()
					.getLocalizedName().toLowerCase().contains("table")
					&& (block == null
					|| (block.getItem() instanceof ItemBlock && Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize >= block.stackSize))) {

				//mc.thePlayer.inventory.currentItem = g;
				slot = g;
				block = Wrapper.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack();

			}

		}
		if (lastSlot != slot) {
			Wrapper.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot));
			lastSlot = slot;
		}
		return block;
	}

	public BlockUtil.BlockData getDiagonalBlockData(BlockPos pos) {
		BlockPos up = new BlockPos(0, -1, 0),
				east = new BlockPos(-1, 0, 0),
				west = new BlockPos(1, 0, 0),
				north = new BlockPos(0, 0, 1),
				south = new BlockPos(0, 0, -1);

		if (canPlaceAt(pos.add(up)))
			return new BlockUtil.BlockData(pos.add(up), UP);

		if (canPlaceAt(pos.add(east)))
			return new BlockUtil.BlockData(pos.add(east), EAST);

		if (canPlaceAt(pos.add(west)))
			return new BlockUtil.BlockData(pos.add(west), WEST);

		if (canPlaceAt(pos.add(south)))
			return new BlockUtil.BlockData(pos.add(south), SOUTH);

		if (canPlaceAt(pos.add(north)))
			return new BlockUtil.BlockData(pos.add(north), NORTH);

		BlockPos[] positions = {east, west, south, north};

		BlockUtil.BlockData data = null;

		for (BlockPos offset : positions) {
			if ((data = getBlockData(pos.add(offset))) != null)
				return data;
		}

		for (BlockPos offset1 : positions)
			for (BlockPos offset2 : positions)
				if ((data = getBlockData(pos.add(offset1).add(offset2))) != null)
					return data;

    	/*for(BlockPos offset1 : positions)
	    	for(BlockPos offset2 : positions)
		    	for(BlockPos offset3 : positions)
		        	if((data = getBlockData(pos.add(offset1).add(offset2).add(offset3))) != null)
		        		return data;*/


		return null;
	}

	public boolean canPlaceAt(BlockPos pos) {
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		return /*block.getMaterial().isSolid() && block.isOpaqueCube() && !block.getMaterial().isLiquid() && block.isVisuallyOpaque() && */block != Blocks.air;
	}

	private boolean canLegitPlaceBlock() {
		if (this.mc.thePlayer.motionX > 0.06 && this.mc.thePlayer.motionZ > 0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX - 0.125, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ - 0.125)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionX > 0.06 && this.mc.thePlayer.motionZ < -0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX - 0.125, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ + 0.125)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionX < -0.06 && this.mc.thePlayer.motionZ > 0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX + 0.125, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ - 0.125)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionX < -0.06 && this.mc.thePlayer.motionZ < -0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX + 0.125, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ + 0.125)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionX > 0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX - 0.12, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionX < -0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX + 0.12, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionZ > 0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ - 0.12)).getBlock() instanceof BlockAir;
		}
		if (this.mc.thePlayer.motionZ < -0.06) {
			return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ + 0.12)).getBlock() instanceof BlockAir;
		}
		return this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX + 0.12, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ + 0.12)).getBlock() instanceof BlockAir && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX - 0.12, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ - 0.12)).getBlock() instanceof BlockAir && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX - 0.12, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX + 0.12, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ + 0.12)).getBlock() instanceof BlockAir && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ - 0.12)).getBlock() instanceof BlockAir;
	}

}
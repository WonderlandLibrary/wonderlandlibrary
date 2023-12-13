package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.ScaffoldMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.module.modules.combat.KillAura;
import slavikcodd3r.rainbow.module.modules.player.SafeWalk;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.FontUtils;
import slavikcodd3r.rainbow.utils.MathUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.NetUtil;
import slavikcodd3r.rainbow.utils.TimeHelper;
import slavikcodd3r.rainbow.utils.Timer;
import slavikcodd3r.rainbow.utils.TimerDelay;
import slavikcodd3r.rainbow.utils.Vec3d;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.Vec3;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import java.awt.Color;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.List;

@Module.Mod(displayName = "Scaffold")
public class Scaffold extends Module
{
    @Option.Op(name = "Delay", min = 1, max = 20, increment = 1)
    public long delay;
    @Option.Op(name = "Expand", min = 0, max = 4, increment = 1)
    public double expand;
	private ScaffoldMode vanilla;
	private ScaffoldMode cubecraft;
	private ScaffoldMode hypixel;
	private ScaffoldMode aac;
	private ScaffoldMode mineplex;
	private ScaffoldMode aerox;
	private ScaffoldMode other;
    @Option.Op(name = "AutoBlock")
	private boolean autoblock;
    @Option.Op(name = "NoSwing")
	private boolean noswing;
    @Option.Op(name = "Tower")
	private boolean tower;
    @Option.Op(name = "Downwards")
	private boolean downwards;
	Minecraft mc = Minecraft.getMinecraft();
    private static List<Block> invalid;
    private Timer timer;
    private BlockData blockData;
    boolean placing;
    public TimerDelay time;
    private boolean sentineldisabled;
    int counter = 0;
    private double moveSpeed;
    private double lastDist;
    private double stage;
	public static FontUtils font;
    
    public Scaffold() {
        Scaffold.invalid = Arrays.asList(Blocks.ice, Blocks.packed_ice, Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
        this.time = new TimerDelay();
        this.timer = new Timer();
        this.delay = 1;
        this.expand = 0;
        this.vanilla = new ScaffoldMode("Vanilla", true, this);
        this.cubecraft = new ScaffoldMode("CubeCraft", false, this);
        this.hypixel = new ScaffoldMode("Hypixel", false, this);
        this.aac = new ScaffoldMode("AAC", false, this);
        this.mineplex = new ScaffoldMode("Mineplex", false, this);
        this.aerox = new ScaffoldMode("Aerox", false, this);
        this.other = new ScaffoldMode("Other", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.hypixel);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.other);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("CubeCraft")).toString());
        }
        else if (this.hypixel.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Hypixel")).toString());
        }
        else if (this.aac.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("AAC")).toString());
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Mineplex")).toString());
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Aerox")).toString());
        }
        else if (this.other.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Other")).toString());
        }
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    public void enable() {
    	this.moveSpeed = Speed.getBaseMoveSpeed();
        this.lastDist = 0.0;
        this.stage = 2.0;
    	this.counter = 0;
    	this.sentineldisabled = false;
    	if (this.cubecraft.getValue()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4, mc.thePlayer.posZ, false));
        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    	}
    	super.enable();
    }
    
    public void disable() {
    	this.moveSpeed = Speed.getBaseMoveSpeed();
        this.lastDist = 0.0;
        this.stage = 2.0;
    	this.counter = 0;
    	this.sentineldisabled = false;
        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        if (this.hypixel.getValue()) {
        	MoveUtils.setMotion(0.2);
        	mc.thePlayer.jumpMovementFactor = 0;
        }
    	super.disable();
    }
    
    @EventTarget
    private void onPacket(final PacketSendEvent event) {
    	if (!this.vanilla.getValue() && !this.hypixel.getValue() && !this.mineplex.getValue()) {
    	if (event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer p = (C03PacketPlayer)event.getPacket();
            	C03PacketPlayer.yaw += 180.0f;
            	mc.thePlayer.renderYawOffset += 180.0f;
                C03PacketPlayer.pitch = 82.0f;
                if (this.mc.thePlayer.moveForward > 0.0f) {
                    C03PacketPlayer.yaw = (float) (this.mc.thePlayer.rotationYaw + 180.0f + Math.random() * 0.1);
                    this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw + 180.0f;
                    this.mc.thePlayer.renderYawOffset = this.mc.thePlayer.rotationYaw + 180.0f;
                }
                else if (this.mc.thePlayer.moveForward < 0.0f) {
                    C03PacketPlayer.yaw = (float) (this.mc.thePlayer.rotationYaw + Math.random() * 0.1);
                    this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw;
                    this.mc.thePlayer.renderYawOffset = this.mc.thePlayer.rotationYaw + 180.0f;
                }
                else if (this.mc.thePlayer.moveStrafing > 0.0f) {
                    C03PacketPlayer.yaw = (float) (this.mc.thePlayer.rotationYaw + 90.0f + Math.random() * 0.1);
                    this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw + 90.0f;
                    this.mc.thePlayer.renderYawOffset = this.mc.thePlayer.rotationYaw + 90.0f;
                }
                else if (this.mc.thePlayer.moveStrafing < 0.0f) {
                    C03PacketPlayer.yaw = (float) (this.mc.thePlayer.rotationYaw - 90.0f + Math.random() * 0.1);
                    this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw - 90.0f;
                    this.mc.thePlayer.renderYawOffset = this.mc.thePlayer.rotationYaw + 90.0f;
                }
    	}
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.mineplex.getValue()) {
            final double motionx = mc.thePlayer.motionX;
            final double motionz = mc.thePlayer.motionZ;
    		double slow3 = 0.0;
            if (mc.thePlayer.onGround) {
                slow3 = 1.0 - Math.abs(motionx * motionz) * 80.0;
            }
            else {
                slow3 = 0.8;
            }
            mc.thePlayer.motionX *= slow3;
            mc.thePlayer.motionZ *= slow3;
    	}
    	if (this.tower) {
            if (ClientUtils.movementInput().jump && mc.thePlayer.ticksExisted % 2 == 0.0) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
                if (mc.thePlayer.ticksExisted % 3 == 0.0) {
                mc.thePlayer.motionY = 0.42;
                if (mc.thePlayer.ticksExisted % 4 == 0.0) {
                mc.thePlayer.motionY -= 1;
                }
                }
            }
    		}
    	if (mc.thePlayer.hurtTime > 0) {
    		this.sentineldisabled = true;
    	}
    	else if (this.cubecraft.getValue()) {
    		if (this.sentineldisabled) {
			event.setGround(false);
			mc.thePlayer.setSprinting(false);
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0;
			}
        	event.setY(event.getY() + 1.0E-12);
        if (mc.thePlayer.ticksExisted % 3 == 0) {
        	event.setY(event.getY() - 1.0E-12);
    		}
    		}
    	}
    	else if (this.aac.getValue()) {
			mc.thePlayer.setSprinting(false);
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0;
			}
    	}
    	else if (this.aerox.getValue()) {
			mc.thePlayer.setSprinting(false);
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0;
			}
    	}
    	if (this.cubecraft.getValue()) {
			if (this.sentineldisabled) {
    if (ClientUtils.movementInput().jump && mc.thePlayer.ticksExisted % 2 == 0.0) {
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
        if (mc.thePlayer.ticksExisted % 3 == 0.0) {
        mc.thePlayer.motionY = 0.42;
        if (mc.thePlayer.ticksExisted % 4 == 0.0) {
        mc.thePlayer.motionY = 0.47;
        }
        }
    }
			}
    	}
        	final double yaw2 = Math.toRadians(mc.thePlayer.rotationYaw);
        	final double x = -Math.sin(yaw2) * expand;
        	final double z = Math.cos(yaw2) * expand;
            this.blockData = null;
            if (mc.thePlayer.ticksExisted % delay == 0.0) {
            if (!ClientUtils.player().isSneaking()) {
            	if (this.downwards) {
                final BlockPos blockBelow1 = new BlockPos(ClientUtils.player().posX + x, ClientUtils.player().posY - 2, ClientUtils.player().posZ + z);
                    this.blockData = this.getBlockData(blockBelow1, this.invalid);
            	} else {
            		final BlockPos blockBelow = new BlockPos(ClientUtils.player().posX + x, ClientUtils.player().posY - 1, ClientUtils.player().posZ + z);
                    this.blockData = this.getBlockData(blockBelow, this.invalid);
            	}
            			if (!this.hypixel.getValue()) {
                        final float yaw = this.aimAtLocation(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face)[0];
                        final float pitch = this.aimAtLocation(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face)[1];              
                        if (this.vanilla.getValue() || this.mineplex.getValue()) {
                        C03PacketPlayer.yaw = yaw;
                        C03PacketPlayer.pitch = pitch;
                        event.setYaw(yaw);
                        event.setPitch(pitch);
                        mc.thePlayer.rotationYawHead = yaw;
                        mc.thePlayer.renderYawOffset = yaw;
                        }
                        }
            }

           
        if (this.blockData != null && this.timer.delay(75.0f)) {
            ClientUtils.mc().rightClickDelayTimer = 0;
            final int heldItem = ClientUtils.player().inventory.currentItem;
            for (int i = 0; i < 9; ++i) {
                if (ClientUtils.player().inventory.getStackInSlot(i) != null && ClientUtils.player().inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                    ClientUtils.player().inventory.currentItem = i;
                    if (!this.autoblock) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                    }
                    break;
                }
            }
            if (mc.playerController.func_178890_a(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getHeldItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()))) {
            	if (this.noswing) {
                ClientUtils.packet(new C0APacketAnimation());
            	} else {
            	mc.thePlayer.swingItem();
            	}
            }
            ClientUtils.player().inventory.currentItem = heldItem;
        }
            }
    	}
    
    @EventTarget
    private void onMove(final MoveEvent event) {
    	if (this.cubecraft.getValue()) {
    		if (!this.sentineldisabled) {
    			ClientUtils.setMoveSpeed(event, 0);
    		}
    	}
    }
    
    @EventTarget
    private void onRender2D(final Render2DEvent event) {
        if (font == null) {
            font = new FontUtils("Verdana", 18.0f);
        }
    	final int blockCount = this.getBlockCount();
    	font.drawString(String.valueOf(blockCount + ""), (float)(ScaledResolution.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(blockCount + "") / 2), (float)(ScaledResolution.getScaledHeight() / 2 - 25), Color.YELLOW.getRGB(), -16777216);
    }
    
    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && this.isValid(item)) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private boolean isValid(final Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        }
        final ItemBlock iBlock = (ItemBlock)item;
        final Block block = iBlock.getBlock();
        return !Scaffold.invalid.contains(block);
    }

	private float[] aimAtLocation(final double x, final double y, final double z, final EnumFacing facing) {
        final EntitySnowball temp = new EntitySnowball(ClientUtils.world());
        temp.posX = x + 0.5;
        temp.posY = y - 2.7035252353;
        temp.posZ = z + 0.5;
        final EntitySnowball entitySnowball5;
        final EntitySnowball entitySnowball10;
        final EntitySnowball entitySnowball4 = entitySnowball10 = (entitySnowball5 = temp);
        entitySnowball10.posX += facing.getDirectionVec().getX() * 0.25;
        final EntitySnowball entitySnowball7;
        final EntitySnowball entitySnowball11;
        final EntitySnowball entitySnowball6 = entitySnowball11 = (entitySnowball7 = temp);
        entitySnowball11.posY += facing.getDirectionVec().getY() * 0.25;
        final EntitySnowball entitySnowball9;
        final EntitySnowball entitySnowball12;
        final EntitySnowball entitySnowball8 = entitySnowball12 = (entitySnowball9 = temp);
        entitySnowball12.posZ += facing.getDirectionVec().getZ() * 0.25;
        return this.aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }
    
    private float[] aimAtLocation(final double positionX, final double positionY, final double positionZ) {
        final double x = positionX - ClientUtils.player().posX;
        final double y = positionY - ClientUtils.player().posY;
        final double z = positionZ - ClientUtils.player().posZ;
        final double distance = MathHelper.sqrt_double(x * x + z * z);
        return new float[] { (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f, (float)(-(Math.atan2(y, distance) * 180.0 / 3.141592653589793)) };
    }
    
    private BlockData getBlockData(final BlockPos pos, List<Block> invalid2) {
        if (this.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos2 = pos.add(-1, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos3 = pos.add(1, 0, 0);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos4 = pos.add(0, 0, 1);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos5 = pos.add(0, 0, -1);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos6 = pos.add(-2, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos7 = pos.add(2, 0, 0);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos8 = pos.add(0, 0, 2);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos9 = pos.add(0, 0, -2);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos10 = pos.add(0, -1, 0);
        if (this.isPosSolid(pos10.add(0, -1, 0))) {
            return new BlockData(pos10.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos10.add(-1, 0, 0))) {
            return new BlockData(pos10.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos10.add(1, 0, 0))) {
            return new BlockData(pos10.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos10.add(0, 0, 1))) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos10.add(0, 0, -1))) {
            return new BlockData(pos10.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos11 = pos10.add(1, 0, 0);
        if (this.isPosSolid(pos11.add(0, -1, 0))) {
            return new BlockData(pos11.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos11.add(-1, 0, 0))) {
            return new BlockData(pos11.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos11.add(1, 0, 0))) {
            return new BlockData(pos11.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos11.add(0, 0, 1))) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos11.add(0, 0, -1))) {
            return new BlockData(pos11.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos12 = pos10.add(-1, 0, 0);
        if (this.isPosSolid(pos12.add(0, -1, 0))) {
            return new BlockData(pos12.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos12.add(-1, 0, 0))) {
            return new BlockData(pos12.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos12.add(1, 0, 0))) {
            return new BlockData(pos12.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos12.add(0, 0, 1))) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos12.add(0, 0, -1))) {
            return new BlockData(pos12.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos13 = pos10.add(0, 0, 1);
        if (this.isPosSolid(pos13.add(0, -1, 0))) {
            return new BlockData(pos13.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos13.add(-1, 0, 0))) {
            return new BlockData(pos13.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos13.add(1, 0, 0))) {
            return new BlockData(pos13.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos13.add(0, 0, 1))) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos13.add(0, 0, -1))) {
            return new BlockData(pos13.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos14 = pos10.add(0, 0, -1);
        if (this.isPosSolid(pos14.add(0, -1, 0))) {
            return new BlockData(pos14.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos14.add(-1, 0, 0))) {
            return new BlockData(pos14.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos14.add(1, 0, 0))) {
            return new BlockData(pos14.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos14.add(0, 0, 1))) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos14.add(0, 0, -1))) {
            return new BlockData(pos14.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
    
    private boolean isPosSolid(final BlockPos pos) {
        final Block block = mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

	private class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        
        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}

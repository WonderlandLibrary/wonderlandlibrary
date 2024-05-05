package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.RegenMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Regen")
public class Regen extends Module
{
    @Op(name = "Packets", min = 0.0, max = 6000.0, increment = 100.0)
    private double packets;
    @Op(name = "Mineplex Packets", min = 0.0, max = 200.0, increment = 10.0)
    private double mineplexpackets;
    @Op(name = "NCP Packets", min = 0.0, max = 200.0, increment = 10.0)
    private double ncppackets;
    @Op(name = "Health", min = 0.0, max = 10.0, increment = 0.5)
    private double health;
	private RegenMode vanilla;
	private RegenMode packet;
	private RegenMode mineplex;
	private RegenMode ncp;
	private RegenMode guardian;
	private RegenMode comugamers;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Regen() {
        this.vanilla = new RegenMode("Vanilla", true, this);
        this.packet = new RegenMode("Packet", false, this);
        this.mineplex = new RegenMode("Mineplex", false, this);
        this.ncp = new RegenMode("NCP", false, this);
        this.guardian = new RegenMode("Guardian", false, this);
        this.comugamers = new RegenMode("ComuGamers", false, this);
        this.packets = 3000.0;
        this.mineplexpackets = 60.0;
        this.ncppackets = 60.0;
        this.health = 8.0;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.guardian);
        OptionManager.getOptionList().add(this.comugamers);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.packet.getValue()) {
        	this.setSuffix("Packet");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.guardian.getValue()) {
        	this.setSuffix("Guardian");
        }
        else if (this.comugamers.getValue()) {
        	this.setSuffix("ComuGamers");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onPacket(final PacketSendEvent event) {
    	if (this.packet.getValue()) {
    		final C03PacketPlayer player = (C03PacketPlayer)event.getPacket();
            if (ClientUtils.mc().thePlayer.getHealth() < ClientUtils.mc().thePlayer.getMaxHealth() && ClientUtils.mc().thePlayer.ticksExisted % 2 == 0) {
                ClientUtils.mc().getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(player.func_149466_j()));
            }
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.comugamers.getValue()) {
    		if (ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
                for (int i = 0; i < this.packets; ++i) {
                	 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, true));
                }
    		}
    	}
    	if (this.guardian.getValue()) {
    		if (ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
                for (int i = 0; i < this.packets; ++i) {
                	 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -999999999, mc.thePlayer.posZ, false));
                }
    		}
    	}
    	if (this.ncp.getValue()) {
    		if (!ClientUtils.player().isUsingItem() && ClientUtils.player().isPotionActive(Potion.regeneration) && ClientUtils.player().getActivePotionEffect(Potion.regeneration).getDuration() > 0 && ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
                for (int i = 0; i < this.ncppackets; ++i) {
                    ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
                    ClientUtils.packet(new C03PacketPlayer());
                }
    		}
    	}
    	if (this.mineplex.getValue()) {
    		if (!ClientUtils.player().isUsingItem() && ClientUtils.player().isPotionActive(Potion.regeneration) && ClientUtils.player().getActivePotionEffect(Potion.regeneration).getDuration() > 0 && ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
                for (int i = 0; i < this.mineplexpackets; ++i) {
                    ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
                    ClientUtils.packet(new C03PacketPlayer());
                }
    		}
    	}
    	if (this.vanilla.getValue()) {
    		if (ClientUtils.player().getHealth() <= this.health * 2.0 && (ClientUtils.player().isCollidedVertically || this.isInsideBlock()) && event.getState().equals(Event.State.POST)) {
                for (int i = 0; i < this.packets; ++i) {
                    ClientUtils.packet(new C03PacketPlayer());
                }
    		}
    	}
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(ClientUtils.player().boundingBox.minX); x < MathHelper.floor_double(ClientUtils.player().boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(ClientUtils.player().boundingBox.minY); y < MathHelper.floor_double(ClientUtils.player().boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(ClientUtils.player().boundingBox.minZ); z < MathHelper.floor_double(ClientUtils.player().boundingBox.maxZ) + 1; ++z) {
                    final Block block = ClientUtils.world().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(ClientUtils.world(), new BlockPos(x, y, z), ClientUtils.world().getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && ClientUtils.player().boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}

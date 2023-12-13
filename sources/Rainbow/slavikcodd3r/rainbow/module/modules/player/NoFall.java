package slavikcodd3r.rainbow.module.modules.player;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.NoFallMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.BlockData;
import slavikcodd3r.rainbow.utils.BlockUtils;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.NetUtil;

@Module.Mod(displayName = "NoFall")
public class NoFall extends Module
{
	private NoFallMode vanilla;
	private NoFallMode spoof;
	private NoFallMode noground;
	private NoFallMode packet;
	private NoFallMode packetspoof;
	private NoFallMode nogroundpacket;
	private NoFallMode oldaac;
	private NoFallMode aac;
	private NoFallMode spartan;
	private NoFallMode hypixel;
	private NoFallMode damage;
	private NoFallMode damagepacket;
	private NoFallMode unknownpos;
	private NoFallMode capabilities;
	private NoFallMode ncp;
	private NoFallMode ncp2;
	private NoFallMode cubecraft;
	private NoFallMode down;
	private NoFallMode verus;
	private NoFallMode dac;
	private NoFallMode cancel;
	private NoFallMode guardian;
	private NoFallMode aerox;
	private NoFallMode matrix;
	private NoFallMode mineplex;
	private NoFallMode hover;
	private NoFallMode spam;
	private NoFallMode mlg;
    private double fallStartY;
    private Timer timer;
    private BlockData blockBelowData;
    private boolean nextPlaceWater;
    private boolean nextRemoveWater;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public NoFall() {
        this.vanilla = new NoFallMode("Vanilla", true, this);
        this.spoof = new NoFallMode("Spoof", false, this);
        this.noground = new NoFallMode("NoGround", false, this);
        this.packet = new NoFallMode("Packet", false, this);
        this.packetspoof = new NoFallMode("PacketSpoof", false, this);
        this.nogroundpacket = new NoFallMode("NoGroundPacket", false, this);
        this.oldaac = new NoFallMode("OldAAC", false, this);
        this.aac = new NoFallMode("AAC", false, this);
        this.spartan = new NoFallMode("Spartan", false, this);
        this.hypixel = new NoFallMode("Hypixel", false, this);
        this.damage = new NoFallMode("Damage", false, this);
        this.damagepacket = new NoFallMode("DamagePacket", false, this);
        this.unknownpos = new NoFallMode("UnknownPos", false, this);
        this.capabilities = new NoFallMode("Capabilities", false, this);
        this.ncp = new NoFallMode("NCP", false, this);
        this.ncp2 = new NoFallMode("NCP2", false, this);
        this.cubecraft = new NoFallMode("CubeCraft", false, this);
        this.down = new NoFallMode("Down", false, this);
        this.verus = new NoFallMode("Verus", false, this);
        this.dac = new NoFallMode("DAC", false, this);
        this.cancel = new NoFallMode("Cancel", false, this);
        this.guardian = new NoFallMode("Guardian", false, this);
        this.aerox = new NoFallMode("Aerox", false, this);
        this.matrix = new NoFallMode("Matrix", false, this);
        this.mineplex = new NoFallMode("Mineplex", false, this);
        this.hover = new NoFallMode("Hover", false, this);
        this.spam = new NoFallMode("Spam", false, this);
        this.mlg = new NoFallMode("MLG", false, this);
        this.fallStartY = 0.0;
        this.nextPlaceWater = false;
        this.nextRemoveWater = false;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.spoof);
        OptionManager.getOptionList().add(this.noground);
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.packetspoof);
        OptionManager.getOptionList().add(this.nogroundpacket);
        OptionManager.getOptionList().add(this.oldaac);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.hypixel);
        OptionManager.getOptionList().add(this.damage);
        OptionManager.getOptionList().add(this.damagepacket);
        OptionManager.getOptionList().add(this.unknownpos);
        OptionManager.getOptionList().add(this.capabilities);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.ncp2);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.down);
        OptionManager.getOptionList().add(this.verus);
        OptionManager.getOptionList().add(this.dac);
        OptionManager.getOptionList().add(this.cancel);
        OptionManager.getOptionList().add(this.guardian);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.hover);
        OptionManager.getOptionList().add(this.spam);
        OptionManager.getOptionList().add(this.mlg);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.spoof.getValue()) {
        	this.setSuffix("Spoof");
        }
        else if (this.noground.getValue()) {
        	this.setSuffix("NoGround");
        }
        else if (this.packet.getValue()) {
        	this.setSuffix("Packet");
        }
        else if (this.packetspoof.getValue()) {
        	this.setSuffix("PacketSpoof");
        }
        else if (this.nogroundpacket.getValue()) {
        	this.setSuffix("NoGroundPacket");
        }
        else if (this.oldaac.getValue()) {
        	this.setSuffix("OldAAC");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.spartan.getValue()) {
        	this.setSuffix("Spartan");
        }
        else if (this.hypixel.getValue()) {
        	this.setSuffix("Hypixel");
        }
        else if (this.damage.getValue()) {
        	this.setSuffix("Damage");
        }
        else if (this.damagepacket.getValue()) {
        	this.setSuffix("DamagePacket");
        }
        else if (this.unknownpos.getValue()) {
        	this.setSuffix("UnknownPos");
        }
        else if (this.capabilities.getValue()) {
        	this.setSuffix("Capabilities");
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.ncp2.getValue()) {
        	this.setSuffix("NCP2");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.down.getValue()) {
        	this.setSuffix("Down");
        }
        else if (this.verus.getValue()) {
        	this.setSuffix("Verus");
        }
        else if (this.dac.getValue()) {
        	this.setSuffix("DAC");
        }
        else if (this.cancel.getValue()) {
        	this.setSuffix("Cancel");
        }
        else if (this.guardian.getValue()) {
        	this.setSuffix("Guardian");
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix("Aerox");
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix("Matrix");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.hover.getValue()) {
        	this.setSuffix("Hover");
        }
        else if (this.spam.getValue()) {
        	this.setSuffix("Spam");
        }
        else if (this.mlg.getValue()) {
        	this.setSuffix("MLG");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	Timer.timerSpeed = 1.0f;
    	if (this.capabilities.getValue()) {
		mc.thePlayer.capabilities.disableDamage = false;
    	}
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onPacket(final PacketReceiveEvent event) {
    	if (this.aerox.getValue()) {
            if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof S00PacketKeepAlive) {
                event.setCancelled(true);
            }
    	}
    }
    
    @EventTarget
    public void onPacket(final PacketSendEvent event) {
    	if (this.aerox.getValue()) {
       		if (event.getPacket() instanceof C13PacketPlayerAbilities) {
                final C13PacketPlayerAbilities packet = (C13PacketPlayerAbilities)event.getPacket();
                packet.allowFlying = true;
                packet.creativeMode = true;
                packet.flying = true;
                packet.setAllowFlying(true);
                packet.setCreativeMode(true);
                packet.setFlying(true);
    		}
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.vanilla.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.0f) {
    			event.setGround(true);
    		}
    	}
    	else if (this.spoof.getValue()) {
			event.setGround(true);
    	}
    	else if (this.noground.getValue()) {
			event.setGround(false);
    	}
    	else if (this.packet.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.0f) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    		}
    	}
    	else if (this.packetspoof.getValue()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    	}
    	else if (this.nogroundpacket.getValue()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
    	}
    	else if (this.oldaac.getValue()) {
			event.setGround(false);
    	}
    	else if (this.aac.getValue()) {
    		mc.thePlayer.motionY -= 0.1;
    		event.setGround(true);
    	}
    	else if (this.spartan.getValue()) {
    		if (!this.mc.thePlayer.onGround) {
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-10, this.mc.thePlayer.posZ);
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-10, this.mc.thePlayer.posZ, true));
    		}
    	}
    	else if (this.hypixel.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.damage.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
    			event.setGround(true);
    			mc.thePlayer.fallDistance = 0;
    		}
    	}
    	else if (this.damagepacket.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			mc.thePlayer.fallDistance = 0;
    		}
    	}
    	else if (this.unknownpos.getValue()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, true));
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    	}
    	else if (this.capabilities.getValue()) {
    		mc.thePlayer.capabilities.disableDamage = true;
    	}
    	else if (this.ncp.getValue()) {
    		if (mc.thePlayer.fallDistance > 1.0f) {
    			mc.theWorld.sendQuittingDisconnectingPacket();
    		}
    	}
    	else if (this.ncp2.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
    			for (int i = 0; i < 80.0 + 40.0 * (0.5 - 0.5); ++i) {
    	            ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY + 0.049, ClientUtils.player().posZ, false));
    	            ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, false));
    	        }
    	        ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, true));
    		}
    	}
    	else if (this.cubecraft.getValue()) {
    		if (mc.thePlayer.fallDistance > 2.5f) {
    			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, true));
            	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        		mc.thePlayer.fallDistance = 0;
    		}
    	}
    	else if (this.down.getValue()) {
    		mc.thePlayer.motionY = -6;
    	}
    	else if (this.verus.getValue()) {
    		if (mc.thePlayer.fallDistance >= 2.5f) {
                event.setGround(mc.thePlayer.ticksExisted % 2 == 0);
    		}
    	}
    	else if (this.dac.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
    			event.setGround(true);
    		}
    	}
    	else if (this.cancel.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.0f) {
        		event.setGround(true);
    			if (mc.thePlayer.ticksExisted % 2 == 0) {
    			event.setCancelled(true);
    			}
    		}
    	}
    	else if (this.guardian.getValue()) {
    		if (!event.shouldAlwaysSend()) {
                event.setGround(true);
            }
    	}
    	else if (this.aerox.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.matrix.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.mineplex.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.0f) {
    			event.setGround(true);
    		}
    	}
    	else if (this.spam.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
                for (int i = 0; i < 10; ++i) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    			}
    		}
    	}
    	else if (this.hover.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
        		event.setCancelled(true);
    		}
    	}
    	else if (this.mlg.getValue()) {
    		if (event.getState() == Event.State.PRE) {
                if (!mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0) {
                    if (this.fallStartY < mc.thePlayer.posY) {
                        this.fallStartY = mc.thePlayer.posY;
                    }
                    if (this.fallStartY - mc.thePlayer.posY > 2.0) {
                        final double x = mc.thePlayer.posX + mc.thePlayer.motionX * 1.25;
                        final double y = mc.thePlayer.posY - mc.thePlayer.getEyeHeight();
                        final double z = mc.thePlayer.posZ + mc.thePlayer.motionZ * 1.25;
                        final BlockPos blockBelow = new BlockPos(x, y, z);
                        final IBlockState blockState = mc.theWorld.getBlockState(blockBelow);
                        final IBlockState underBlockState = mc.theWorld.getBlockState(blockBelow.offsetDown());
                        if (underBlockState.getBlock().isSolidFullCube() && !mc.thePlayer.isSneaking() && (blockState.getBlock() == Blocks.air || blockState.getBlock() == Blocks.snow_layer || blockState.getBlock() == Blocks.tallgrass)) {
                            this.blockBelowData = this.getBlockData(blockBelow);
                            if (this.blockBelowData != null) {
                                this.nextPlaceWater = true;
                                this.nextRemoveWater = false;
                                final float[] rotations = MoveUtils.getRotationsBlock(this.blockBelowData.position, this.blockBelowData.face);
                                event.setYaw(rotations[0]);
                                event.setPitch(rotations[1]);
                            }
                        }
                    }
                }
                else {
                    this.fallStartY = mc.thePlayer.posY;
                }
                if (this.blockBelowData != null && mc.thePlayer.isInWater()) {
                    this.nextRemoveWater = true;
                    final float[] rotations2 = MoveUtils.getRotationsBlock(this.blockBelowData.position, this.blockBelowData.face);
                    event.setYaw(rotations2[0]);
                    event.setPitch(rotations2[1]);
                }
            }
            else if (this.blockBelowData != null && this.nextPlaceWater) {
                this.placeWater();
            }
            else if (this.blockBelowData != null && this.nextRemoveWater) {
                this.getWaterBack();
            }
    	}
    }
    
    private int swapToItem(final int item) {
        mc.rightClickDelayTimer = 2;
        final int currentItem = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(item - 36));
        mc.thePlayer.inventory.currentItem = item - 36;
        mc.playerController.updateController();
        return currentItem;
    }
    
    private void placeWater() {
        for (final Map.Entry<Integer, Item> item : this.getHotbarItems().entrySet()) {
            if (item.getValue().equals(Items.water_bucket)) {
                final int currentItem = this.swapToItem(item.getKey());
                mc.playerController.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                mc.thePlayer.inventory.currentItem = currentItem;
                mc.playerController.updateController();
                break;
            }
        }
        this.nextPlaceWater = false;
    }
    
    private void getWaterBack() {
        for (final Map.Entry<Integer, Item> item : this.getHotbarItems().entrySet()) {
            if (item.getValue().equals(Items.bucket)) {
                final int currentItem = this.swapToItem(item.getKey());
                mc.playerController.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                mc.thePlayer.inventory.currentItem = currentItem;
                mc.playerController.updateController();
                break;
            }
        }
        this.blockBelowData = null;
        this.nextRemoveWater = false;
    }
    
    private HashMap<Integer, Item> getHotbarItems() {
        final HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        for (int i = 36; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                items.put(i, itemStack.getItem());
            }
        }
        return items;
    }
    
    private BlockData getBlockData(final BlockPos pos) {
        if (!BlockUtils.getBlacklistedBlocks().contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        return null;
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}

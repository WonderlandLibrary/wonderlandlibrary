package xyz.cucumber.base.module.feat.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventBlockBreak;
import xyz.cucumber.base.events.ext.EventBlockDamage;
import xyz.cucumber.base.events.ext.EventLegitPlace;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.utils.Timer;

@ModuleInfo(category = Category.PLAYER, description = "Automatically switch to best tool", name = "Auto Tool")
public class AutoToolModule extends Mod{
	public int serverSideSlot = -1, lastServerSideSlot = -1;
    private int tool = -1;
    private boolean reset;
    
    private Timer timer = new Timer();
    
    public BooleanSettings silent = new BooleanSettings("Silent", true);
    
    public AutoToolModule() {
        this.addSettings(silent);
    }
    
    @EventListener
    public void onTick(EventTick e) {
    	setInfo(silent.isEnabled() ? "Silent" : "Normal");
    }
    
    @EventListener
    public void onBlockDamage(EventBlockDamage e) {
    	if (silent.isEnabled()) {
    		e.setPlayerRelativeBlockHardness(getPlayerRelativeBlockHardness(e.getPos(), serverSideSlot));
        }
    }
    @EventListener
    public void onBlockBreak(EventBlockBreak e) {
    	int slot = this.findTool(e.getLocation());

        if (slot != -1) {
            this.tool = slot;
        }
    }
    @EventListener
    public void onSendPacket(EventSendPacket e) {
    	Packet p = e.getPacket();
    	
    	if (p instanceof C08PacketPlayerBlockPlacement) {
            serverSideSlot = mc.thePlayer.inventory.currentItem;
            if(silent.isEnabled() && lastServerSideSlot != serverSideSlot) {
    			mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(serverSideSlot));
    			lastServerSideSlot = serverSideSlot;
    		}
        }

        if (!silent.isEnabled()) {
            return;
        }

        if (p instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging packet = (C07PacketPlayerDigging) p;
            int slot = this.findTool(packet.getPosition());

            if (slot != -1) {
                this.tool = slot;
                serverSideSlot = tool;
            }
        }

        if (p instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packet = (C09PacketHeldItemChange) p;
            serverSideSlot = packet.getSlotId();
            lastServerSideSlot = serverSideSlot;
        }
    }
    @EventListener
    public void onLegitPlace(EventLegitPlace e) {
    	if (mc.gameSettings.keyBindAttack.isKeyDown() && serverSideSlot != mc.thePlayer.inventory.currentItem) {
    		if(silent.isEnabled() && lastServerSideSlot != serverSideSlot) {
    			mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(serverSideSlot));
    			lastServerSideSlot = serverSideSlot;
    		}
        }

        if (!mc.gameSettings.keyBindAttack.isKeyDown() && timer.hasTimeElapsed(60, false)) {
            if (serverSideSlot != mc.thePlayer.inventory.currentItem) {
                serverSideSlot = mc.thePlayer.inventory.currentItem;
                reset = false;
                tool = -1;
                timer.reset();
                return;
            }
        }

        if (silent.isEnabled()) {
            return;
        }

        if (tool != -1) {
            if (!reset) {
                timer.reset();
                reset = true;
            }

            if (timer.hasTimeElapsed(0, false)) {
                if (silent.isEnabled()) {
                    if (serverSideSlot != tool) {
                        mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(tool));
                        serverSideSlot = tool;
                    }

                } else {
                    if (mc.thePlayer.inventory.currentItem != tool) {
                        mc.thePlayer.inventory.currentItem = tool;
                    }
                }

                reset = false;
                tool = -1;
            }
        }
    }
    private int findTool( BlockPos blockPos) {
        float bestSpeed = 1;
        int bestSlot = -1;

         IBlockState blockState = mc.theWorld.getBlockState(blockPos);

        for (int i = 0; i < 9; i++) {
             ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

             float speed = itemStack.getStrVsBlock(blockState.getBlock());

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        return bestSlot;
    }

    public ItemStack getCurrentItemInSlot( int slot) {
        return slot < 9 && slot >= 0 ? mc.thePlayer.inventory.mainInventory[slot] : null;
    }

    public float getStrVsBlock( Block blockIn,  int slot) {
        float f = 1.0F;

        if (mc.thePlayer.inventory.mainInventory[slot] != null) {
            f *= mc.thePlayer.inventory.mainInventory[slot].getStrVsBlock(blockIn);
        }
        return f;
    }

    public float getPlayerRelativeBlockHardness(BlockPos pos, int slot) {
         Block block = mc.theWorld.getBlockState(pos).getBlock();
         float f = block.getBlockHardness(mc.theWorld, pos);
        return f < 0.0F ? 0.0F : (!canHeldItemHarvest(block, slot) ? getToolDigEfficiency(block, slot) / f / 100.0F : getToolDigEfficiency(block, slot) / f / 30.0F);
    }

    public boolean canHeldItemHarvest( Block blockIn,  int slot) {
        if (blockIn.getMaterial().isToolNotRequired()) {
            return true;
        } else {
             ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(slot);
            return itemstack != null && itemstack.canHarvestBlock(blockIn);
        }
    }

    public float getToolDigEfficiency( Block blockIn,  int slot) {
        float f = getStrVsBlock(blockIn, slot);

        if (f > 1.0F) {
             int i = EnchantmentHelper.getEfficiencyModifier(mc.thePlayer);
             ItemStack itemstack = getCurrentItemInSlot(slot);

            if (i > 0 && itemstack != null) {
                f += (float) (i * i + 1);
            }
        }

        if (mc.thePlayer.isPotionActive(Potion.digSpeed)) {
            f *= 1.0F + (float) (mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (mc.thePlayer.isPotionActive(Potion.digSlowdown)) {
             float f1;

            switch (mc.thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;

                case 1:
                    f1 = 0.09F;
                    break;

                case 2:
                    f1 = 0.0027F;
                    break;

                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (mc.thePlayer.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(mc.thePlayer)) {
            f /= 5.0F;
        }

        if (!mc.thePlayer.onGround) {
            f /= 5.0F;
        }

        return f;
    }
}

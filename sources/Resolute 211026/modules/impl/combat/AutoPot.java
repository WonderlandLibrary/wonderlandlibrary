// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.potion.Potion;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import vip.Resolute.util.player.InventoryUtils;
import net.minecraft.item.ItemPotion;
import vip.Resolute.Resolute;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.modules.impl.movement.Scaffold;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventMove;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import vip.Resolute.util.misc.TimerUtil;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class AutoPot extends Module
{
    public NumberSetting healthProp;
    public NumberSetting slotProp;
    public NumberSetting delayProp;
    private static final byte HEALTH_BELOW = 1;
    private static final byte BETTER_THAN_CURRENT = 2;
    private String potionCounter;
    private static final PotionType[] VALID_POTIONS;
    private final C08PacketPlayerBlockPlacement THROW_POTION_PACKET;
    private final TimerUtil interactionTimer;
    private int prevSlot;
    private boolean potting;
    private int jumpTicks;
    private boolean jump;
    private boolean jumpNextTick;
    
    public AutoPot() {
        super("AutoPot", 0, "", Category.COMBAT);
        this.healthProp = new NumberSetting("Health", 6.0, 1.0, 10.0, 0.5);
        this.slotProp = new NumberSetting("Slot", 7.0, 1.0, 9.0, 1.0);
        this.delayProp = new NumberSetting("Delay", 400.0, 0.0, 1000.0, 50.0);
        this.THROW_POTION_PACKET = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
        this.interactionTimer = new TimerUtil();
        this.addSettings(this.healthProp, this.slotProp, this.delayProp);
    }
    
    @Override
    public void onEnable() {
        this.potionCounter = "0";
        this.prevSlot = -1;
        this.potting = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.potionCounter);
        if (e instanceof EventMove) {
            final EventMove event = (EventMove)e;
            if (this.jump && this.jumpTicks >= 0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
        if (e instanceof EventMotion) {
            final EventMotion event2 = (EventMotion)e;
            if (AutoPot.mc.currentScreen != null) {
                return;
            }
            if (Scaffold.enabled) {
                return;
            }
            if (event2.isPre()) {
                this.potionCounter = Integer.toString(this.getValidPotionsInInv());
                if (this.jumpNextTick) {
                    AutoPot.mc.thePlayer.setPosition(AutoPot.mc.thePlayer.posX, AutoPot.mc.thePlayer.posY + 1.2491999864578247, AutoPot.mc.thePlayer.posZ);
                    event2.setY(event2.getY() + 1.2491999864578247);
                    this.jumpNextTick = false;
                }
                if (this.jump) {
                    --this.jumpTicks;
                    if (MovementUtils.isOnGround()) {
                        this.jump = false;
                        this.jumpTicks = -1;
                    }
                }
                if (this.interactionTimer.hasElapsed((long)this.delayProp.getValue())) {
                    for (int slot = 9; slot < 45; ++slot) {
                        final ItemStack stack = Resolute.getStackInSlot(slot);
                        if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata()) && InventoryUtils.isBuffPotion(stack)) {
                            final ItemPotion itemPotion = (ItemPotion)stack.getItem();
                            boolean validEffects = false;
                            for (final PotionEffect effect : itemPotion.getEffects(stack.getMetadata())) {
                                for (final PotionType potionType : AutoPot.VALID_POTIONS) {
                                    if (potionType.potionId == effect.getPotionID()) {
                                        validEffects = true;
                                        if (this.hasFlag(potionType.requirementFlags, 1)) {
                                            validEffects = (AutoPot.mc.thePlayer.getHealth() < this.healthProp.getValue() * 2.0);
                                        }
                                        final boolean orIsLesserPresent = this.hasFlag(potionType.requirementFlags, 2);
                                        final PotionEffect activePotionEffect = AutoPot.mc.thePlayer.getActivePotionEffect(potionType.potionId);
                                        if (orIsLesserPresent && activePotionEffect != null) {
                                            validEffects &= (activePotionEffect.getAmplifier() < effect.getAmplifier());
                                        }
                                    }
                                }
                            }
                            if (validEffects) {
                                if (MovementUtils.isOverVoid()) {
                                    return;
                                }
                                this.prevSlot = AutoPot.mc.thePlayer.inventory.currentItem;
                                final double xDist = AutoPot.mc.thePlayer.posX - AutoPot.mc.thePlayer.lastTickPosX;
                                final double zDist = AutoPot.mc.thePlayer.posZ - AutoPot.mc.thePlayer.lastTickPosZ;
                                final double speed = Math.sqrt(xDist * xDist + zDist * zDist);
                                final boolean shouldPredict = speed > 0.38;
                                final boolean shouldJump = speed < 0.221;
                                final boolean onGround = MovementUtils.isOnGround();
                                if (shouldJump && onGround && !MovementUtils.isBlockAbove() && MovementUtils.getJumpBoostModifier() == 0) {
                                    this.jump = true;
                                    this.jumpTicks = 9;
                                    AutoPot.mc.thePlayer.motionX = 0.0;
                                    AutoPot.mc.thePlayer.motionZ = 0.0;
                                    event2.setPitch(-90.0f);
                                    AutoPot.mc.thePlayer.jump();
                                }
                                else {
                                    if (!shouldPredict && !onGround) {
                                        return;
                                    }
                                    event2.setYaw(MovementUtils.getMovementDirection());
                                    event2.setPitch(shouldPredict ? 0.0f : 45.0f);
                                }
                                KillAura.waitTicks = 2;
                                int potSlot;
                                if (slot >= 36) {
                                    potSlot = slot - 36;
                                }
                                else {
                                    final int potionSlot = (int)(this.slotProp.getValue() - 1.0);
                                    InventoryUtils.windowClick(slot, potionSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                    potSlot = potionSlot;
                                }
                                AutoPot.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(potSlot));
                                this.potting = true;
                                break;
                            }
                        }
                    }
                }
            }
            else if (this.potting && this.prevSlot != -1) {
                AutoPot.mc.getNetHandler().getNetworkManager().sendPacket(this.THROW_POTION_PACKET);
                AutoPot.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(this.prevSlot));
                this.interactionTimer.reset();
                this.prevSlot = -1;
                this.potting = false;
            }
        }
    }
    
    private int getValidPotionsInInv() {
        int count = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Resolute.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata()) && InventoryUtils.isBuffPotion(stack)) {
                final ItemPotion itemPotion = (ItemPotion)stack.getItem();
                for (final PotionEffect effect : itemPotion.getEffects(stack.getMetadata())) {
                    boolean breakOuter = false;
                    for (final PotionType type : AutoPot.VALID_POTIONS) {
                        if (type.potionId == effect.getPotionID()) {
                            ++count;
                            breakOuter = true;
                            break;
                        }
                    }
                    if (breakOuter) {
                        break;
                    }
                }
            }
        }
        return count;
    }
    
    private boolean hasFlag(final int flags, final int flagToCheck) {
        return (flags & flagToCheck) == flagToCheck;
    }
    
    static {
        VALID_POTIONS = new PotionType[] { PotionType.HEALTH, PotionType.REGEN, PotionType.SPEED };
    }
    
    private enum Items
    {
        HEADS, 
        POTIONS, 
        SOUPS;
    }
    
    private enum PotionType
    {
        SPEED(Potion.moveSpeed.id, 2), 
        REGEN(Potion.regeneration.id, 3), 
        HEALTH(Potion.heal.id, 1);
        
        private final int potionId;
        private final int requirementFlags;
        
        private PotionType(final int potionId, final int requirementFlags) {
            this.potionId = potionId;
            this.requirementFlags = requirementFlags;
        }
    }
}

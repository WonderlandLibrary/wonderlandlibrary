package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.player.WindowClickEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.player.Scaffold;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.InventoryUtils;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

import java.util.List;

@ModuleInfo(label = "Auto Potion", category = ModuleCategory.COMBAT)
public final class AutoPotion extends Module {

    private static final PotionType[] VALID_POTIONS = {PotionType.HEALTH, PotionType.REGEN, PotionType.SPEED};

    private final C08PacketPlayerBlockPlacement THROW_POTION_PACKET = new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);

    private final Property<Boolean> potionsProperty = new Property<>("Potions", true);
    private final Property<Boolean> headsProperty = new Property<>("Heads", true);

    private final Property<Boolean> jumpOnlyProperty = new Property<>("Force Jump", true,
            potionsProperty::getValue);
    private final DoubleProperty healthProperty = new DoubleProperty("Health",
            6.0, this::hasModeSelected, 1.0, 10.0, 0.5);
    private final DoubleProperty slotProperty = new DoubleProperty("Slot",
            7.0, this::hasModeSelected, 1.0, 9.0, 1);
    private final DoubleProperty delayProperty = new DoubleProperty("Delay",
            400.0, this::hasModeSelected, 0.0, 1000.0, 50,
            Representation.MILLISECONDS);

    private final Property<Boolean> distanceCheckProperty = new Property<>("Dist Check", true,
            potionsProperty::getValue);
    private final DoubleProperty minDistanceProperty = new DoubleProperty("Min Player Dist", 1.0D,
            () -> distanceCheckProperty.isAvailable() && distanceCheckProperty.getValue(),
            0.1D, 5.0D, 0.1D);

    private final TimerUtil interactionTimer = new TimerUtil();
    private int prevSlot;
    private boolean potting;
    private String potionCounter;

    private int jumpTicks;
    private boolean jump;

    @EventLink
    public final Listener<WindowClickEvent> onWindowClickEvent =
            event -> interactionTimer.reset();

    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<MoveEntityEvent> onMoveEntityEvent = event -> {
        if (jump && jumpTicks >= 0) {
            event.setX(0.0D);
            event.setZ(0.0D);
        }
    };

    @EventLink(EventBusPriorities.HIGHEST)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (potionsProperty.getValue()) {
            if (event.isPre()) {
                if (jump) {
                    jumpTicks--;
                    if (MovementUtils.isOnGround()) {
                        jump = false;
                        jumpTicks = -1;
                    }
                }

                if (ModuleManager.getInstance(Scaffold.class).isEnabled())
                    return;

                potionCounter = Integer.toString(getValidPotionsInInv());
                if (interactionTimer.hasElapsed(delayProperty.getValue().longValue())) {
                    float health = healthProperty.getValue().floatValue() * 2.0F;
                    for (int slot = InventoryUtils.EXCLUDE_ARMOR_BEGIN; slot < InventoryUtils.END; slot++) {
                        ItemStack stack = Wrapper.getStackInSlot(slot);
                        if (stack != null && stack.getItem() instanceof ItemPotion &&
                                ItemPotion.isSplash(stack.getMetadata()) && InventoryUtils.isBuffPotion(stack)) {
                            ItemPotion itemPotion = (ItemPotion) stack.getItem();
                            boolean validEffects = false;
                            // Use ItemPotion#getEffects(int) Note: The int parameter, the method is considerably faster
                            for (PotionEffect effect : itemPotion.getEffects(stack.getMetadata())) {
                                if (checkEffectAmplifier(stack, effect)) {
                                    for (PotionType potionType : VALID_POTIONS) {
                                        if (potionType.potionId == effect.getPotionID()) {
                                            validEffects = true;
                                            for (Requirement requirement : potionType.requirements) {
                                                if (!requirement.test(health, effect.getAmplifier(), potionType.potionId)) {
                                                    validEffects = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (validEffects) {
                                if (MovementUtils.isOverVoid())
                                    return;

                                if (Wrapper.getMinecraft().currentScreen != null)
                                    return;

                                if (distanceCheckProperty.getValue()) {
                                    if (isPlayerInRange(minDistanceProperty.getValue()))
                                        return;
                                }

                                prevSlot = Wrapper.getPlayer().inventory.currentItem;

                                double xDist = Wrapper.getPlayer().posX - Wrapper.getPlayer().lastTickPosX;
                                double zDist = Wrapper.getPlayer().posZ - Wrapper.getPlayer().lastTickPosZ;

                                double speed = StrictMath.sqrt(xDist * xDist + zDist * zDist);

                                boolean shouldPredict = speed > 0.38D;
                                boolean shouldJump = speed < MovementUtils.WALK_SPEED;
                                boolean onGround = MovementUtils.isOnGround();
                                boolean jumpOnly = jumpOnlyProperty.getValue();

                                if ((shouldJump || jumpOnly) && onGround && !MovementUtils.isBlockAbove() && MovementUtils.getJumpBoostModifier() == 0) {
                                    Wrapper.getPlayer().motionX = 0.0D;
                                    Wrapper.getPlayer().motionZ = 0.0D;
                                    event.setPitch(-90.0F);
                                    Wrapper.getPlayer().jump();
                                    jump = true;
                                    jumpTicks = 9;
                                } else if ((shouldPredict || onGround) && !jumpOnly) {
                                    event.setYaw(MovementUtils.getMovementDirection());
                                    event.setPitch(shouldPredict ? 0.0F : 45.0F);
                                } else return;

                                final int potSlot;
                                KillAura.waitTicks = 2;
                                if (slot >= 36) { // In hotbar
                                    potSlot = slot - 36;
                                } else { // Get it from inventory
                                    int potionSlot = slotProperty.getValue().intValue() - 1;
                                    InventoryUtils.windowClick(slot, potionSlot,
                                            InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                    potSlot = potionSlot;
                                }
                                Wrapper.sendPacketDirect(new C09PacketHeldItemChange(potSlot));
                                potting = true;
                                return;
                            }
                        }
                    }
                }
            } else if (potting && prevSlot != -1) {
                Wrapper.sendPacketDirect(THROW_POTION_PACKET);
                Wrapper.sendPacketDirect(new C09PacketHeldItemChange(prevSlot));
                interactionTimer.reset();
                prevSlot = -1;
                potting = false;
                return;
            }
        }

        if (headsProperty.getValue()) {
            if (event.isPre()) {
                if (interactionTimer.hasElapsed(delayProperty.getValue().longValue()) &&
                        !ModuleManager.getInstance(Scaffold.class).isEnabled() &&
                        Wrapper.getPlayer().getHealth() <= healthProperty.getValue() * 2) {

                    for (int i = 0; i < 45; i++) {
                        ItemStack stack = Wrapper.getStackInSlot(i);

                        if (stack != null && stack.getItem() instanceof ItemSkull && stack.getDisplayName().contains("Golden")) {
                            final int headSlot;
                            KillAura.waitTicks = 2;
                            if (i >= 36) {
                                headSlot = i - 36;
                            } else {
                                int desiredSlot = slotProperty.getValue().intValue() - 1;
                                InventoryUtils.windowClick(i, desiredSlot,
                                        InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                headSlot = desiredSlot;
                            }
                            int oldSlot = Wrapper.getPlayer().inventory.currentItem;
                            Wrapper.sendPacketDirect(new C09PacketHeldItemChange(headSlot));
                            Wrapper.sendPacketDirect(THROW_POTION_PACKET);
                            Wrapper.sendPacketDirect(new C09PacketHeldItemChange(oldSlot));
                            interactionTimer.reset();
                            return;
                        }
                    }
                }
            }
        }
    };

    private boolean checkEffectAmplifier(ItemStack stack, PotionEffect effectToCheck) {
        int bestAmplifier = -1;
        ItemStack bestStack = null;

        for (int i = InventoryUtils.EXCLUDE_ARMOR_BEGIN; i < InventoryUtils.END; i++) {
            ItemStack stackInSlot = Wrapper.getStackInSlot(i);

            if (stackInSlot != null && stackInSlot.getItem() instanceof ItemPotion) {
                ItemPotion itemPotion = (ItemPotion) stackInSlot.getItem();
                for (PotionEffect effect : itemPotion.getEffects(stackInSlot.getMetadata())) {
                    int amp = effect.getAmplifier();
                    if (effect.getPotionID() == effectToCheck.getPotionID() && amp > bestAmplifier) {
                        bestStack = stackInSlot;
                        bestAmplifier = amp;
                    }
                }
            }
        }

        return bestStack == stack;
    }

    public boolean isPlayerInRange(double distance) {
        Entity player = Wrapper.getPlayer();

        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        for (EntityPlayer entity : Wrapper.getLoadedPlayers()) {
            if (!entity.isSpectator() && entity instanceof EntityOtherPlayerMP) {
                double d1 = entity.getDistanceSq(x, y, z);

                if (d1 < distance * distance)
                    return true;
            }
        }

        return false;
    }

    public AutoPotion() {
        setSuffix(() -> potionCounter);
    }

    private boolean hasModeSelected() {
        return potionsProperty.getValue() || headsProperty.getValue();
    }

    @Override
    public void onEnable() {
        potionCounter = "0";
        prevSlot = -1;
        jump = false;
        jumpTicks = -1;
        potting = false;
    }

    private int getValidPotionsInInv() {
        int count = 0;
        for (int i = InventoryUtils.EXCLUDE_ARMOR_BEGIN; i < InventoryUtils.END; i++) {
            ItemStack stack = Wrapper.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof ItemPotion &&
                    ItemPotion.isSplash(stack.getMetadata()) && InventoryUtils.isBuffPotion(stack)) {
                ItemPotion itemPotion = (ItemPotion) stack.getItem();
                final List<PotionEffect> effects = itemPotion.getEffects(stack.getMetadata());
                if (effects != null) {
                    for (PotionEffect effect : effects) {
                        boolean breakOuter = false;
                        for (PotionType type : VALID_POTIONS) {
                            if (type.potionId == effect.getPotionID()) {
                                count++;
                                breakOuter = true;
                                break;
                            }
                        }
                        if (breakOuter)
                            break;
                    }
                }
            }
        }

        return count;
    }


    private enum Requirements {
        BETTER_THAN_CURRENT(new BetterThanCurrentRequirement()),
        HEALTH_BELOW(new HealthBelowRequirement());

        private final Requirement requirement;

        Requirements(Requirement requirement) {
            this.requirement = requirement;
        }

        public Requirement getRequirement() {
            return requirement;
        }
    }

    private enum PotionType {
        SPEED(Potion.moveSpeed.id, Requirements.BETTER_THAN_CURRENT.getRequirement()),
        REGEN(Potion.regeneration.id, Requirements.HEALTH_BELOW.getRequirement(), Requirements.BETTER_THAN_CURRENT.getRequirement()),
        HEALTH(Potion.heal.id, Requirements.HEALTH_BELOW.getRequirement());

        private final int potionId;
        private final Requirement[] requirements;

        PotionType(int potionId, Requirement... requirements) {
            this.potionId = potionId;
            this.requirements = requirements;
        }
    }

    private interface Requirement {
        boolean test(float healthTarget, int currentAmplifier, int potionId);
    }

    private static class HealthBelowRequirement implements Requirement {
        @Override
        public boolean test(float healthTarget, int currentAmplifier, int potionId) {
            return Wrapper.getPlayer().getHealth() < healthTarget;
        }
    }

    private static class BetterThanCurrentRequirement implements Requirement {
        @Override
        public boolean test(float healthTarget, int currentAmplifier, int potionId) {
            PotionEffect effect = Wrapper.getPlayer().getActivePotionEffect(potionId);
            return effect == null || effect.getAmplifier() < currentAmplifier;
        }
    }
}

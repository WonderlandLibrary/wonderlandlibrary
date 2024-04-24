package Method.Client.module.player;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;
import static Method.Client.module.combat.AutoArmor.isNullOrEmpty;
import static Method.Client.utils.BlockUtils.getState;

public class FoodMod extends Module {
    Setting Souphunger = setmgr.add(new Setting("Hunger", this, 10, 0, 20, true));
    Setting Soup = setmgr.add(new Setting("Soup", this, false));
    Setting AntiHunger = setmgr.add(new Setting("AntiHunger", this, false));
    Setting AutoEat = setmgr.add(new Setting("AutoEat", this, false));
    Setting SetFoodLevelMax = setmgr.add(new Setting("SetFoodLevelMax", this, false));

    int slotBefore = -1;
    int bestSlot = -1;
    int eating = 40;
    private int oldSlot = -1;


    public FoodMod() {
        super("FoodMod", Keyboard.KEY_NONE, Category.PLAYER, "FoodMod");
    }

    @Override
    public void onEnable() {
        this.oldSlot = -1;
        this.bestSlot = -1;
        super.onEnable();
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (side == Connection.Side.OUT && AntiHunger.getValBoolean()) {
            if (packet instanceof CPacketPlayer) {
                CPacketPlayer packet2 = (CPacketPlayer) packet;
                packet2.onGround = (mc.player.fallDistance >= 0.0F || mc.playerController.getIsHittingBlock());
            }
        }
        return true;
    }

    @Subscribe
    public void onClientTick(ClientTickEvent event) {
        if (SetFoodLevelMax.getValBoolean()) {
            mc.player.getFoodStats().setFoodLevel(20);
        }
        if (AutoEat.getValBoolean()) {
            if (this.oldSlot == -1) {
                if (this.canEat()) {
                    return;
                }

                float bestSaturation = 0.0F;

                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = mc.player.inventory.getStackInSlot(i);
                    if (this.isFood(stack)) {
                        ItemFood food = (ItemFood) stack.getItem();
                        float saturation = food.getSaturationModifier(stack);
                        if (saturation > bestSaturation) {
                            bestSaturation = saturation;
                            this.bestSlot = i;
                        }
                    }
                }

                if (this.bestSlot != -1) {
                    this.oldSlot = mc.player.inventory.currentItem;
                }
            } else {
                if (this.canEat()) {
                    this.stop();
                    return;
                }

                if (!this.isFood(mc.player.inventory.getStackInSlot(this.bestSlot))) {
                    this.stop();
                    return;
                }

                mc.player.inventory.currentItem = this.bestSlot;
                mc.gameSettings.keyBindUseItem.pressed = true;
            }


        }

        if (AntiHunger.getValBoolean()) {
            mc.player.onGround = false;
        }
        if (Soup.getValBoolean()) {
            for (int i = 0; i < 36; i++) {
                // filter out non-bowl items and empty bowl slot
                ItemStack stack =
                        mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() != Items.BOWL || i == 9)
                    continue;

                // check if empty bowl slot contains a non-bowl item
                ItemStack emptyBowlStack =
                        mc.player.inventory.getStackInSlot(9);
                boolean swap = !isNullOrEmpty(emptyBowlStack)
                        && emptyBowlStack.getItem() != Items.BOWL;

                // place bowl in empty bowl slot
                windowClick_PICKUP(i < 9 ? 36 + i : i);
                windowClick_PICKUP(9);

                // place non-bowl item from empty bowl slot in current slot
                if (swap)
                    windowClick_PICKUP(i < 9 ? 36 + i : i);
            }
            int soupInHotbar = findSoup(0, 9);

            // check if any soup was found
            if (soupInHotbar != -1) {
                // check if player should eat soup
                if (!shouldEatSoup()) {
                    stopIfEating();
                    return;
                }

                // save old slot
                if (oldSlot == -1)
                    oldSlot = mc.player.inventory.currentItem;

                // set slot
                mc.player.inventory.currentItem = soupInHotbar;

                // eat soup
                mc.gameSettings.keyBindUseItem.pressed = true;
                processRightClick();

                return;
            }

            stopIfEating();

            // search soup in inventory
            int soupInInventory = findSoup(9, 36);

            // move soup in inventory to hotbar
            if (soupInInventory != -1)
                windowClick_QUICK_MOVE(soupInInventory);
        }

        if (!Soup.getValBoolean()) {
            if (eating < 41) {
                eating++;
                if (eating <= 1) {
                    mc.player.inventory.currentItem = bestSlot;
                }
                mc.gameSettings.keyBindUseItem.pressed = true;
                if (eating >= 38) {
                    mc.gameSettings.keyBindUseItem.pressed = true;
                    if (slotBefore != -1) {
                        mc.player.inventory.currentItem = slotBefore;
                    }
                    slotBefore = -1;
                }
                return;
            }

            float bestRestoration = 0;
            bestSlot = -1;
            // Loop through hotbar
            int PrevSlot = mc.player.inventory.currentItem;
            for (int i = 0; i < 9; i++) {
                ItemStack item = mc.player.inventory.getStackInSlot(i);
                float restoration = 0;

                if (item.getItem() instanceof ItemFood) {
                    restoration = ((ItemFood) item.getItem()).getSaturationModifier(item);
                }

                if (restoration > bestRestoration) {
                    bestRestoration = restoration;
                    bestSlot = i;
                }
            }
            if (bestSlot == -1) {
                return;
            }
            if (!(mc.player.getFoodStats().getFoodLevel() < Souphunger.getValDouble())) {
                return;
            }
            slotBefore = mc.player.inventory.currentItem;
            if (slotBefore == -1) {
                return;
            }

            mc.player.inventory.currentItem = PrevSlot;

            mc.player.stopActiveHand();
            mc.player.inventory.currentItem = PrevSlot;

            eating = 0;


        }
    }

    private int findSoup(int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack =
                    mc.player.inventory.getStackInSlot(i);

            if (stack.getItem() instanceof ItemSoup)
                return i;
        }

        return -1;
    }

    private boolean canEat() {
        if (!mc.player.canEat(false)) {
            return true;
        } else {
            if (mc.objectMouseOver != null) {
                Entity entity = mc.objectMouseOver.entityHit;
                if (entity instanceof EntityVillager || entity instanceof EntityTameable) {
                    return true;
                }

                BlockPos pos = mc.objectMouseOver.getBlockPos();
                Block block = mc.world.getBlockState(pos).getBlock();
                return block instanceof BlockContainer || block instanceof BlockWorkbench;
            }

            return false;
        }
    }

    private boolean isFood(ItemStack stack) {
        return stack.getItem() instanceof ItemFood;
    }

    private void stop() {
        mc.gameSettings.keyBindUseItem.pressed = false;
        mc.player.inventory.currentItem = this.oldSlot;
        this.oldSlot = -1;
    }

    private boolean shouldEatSoup() {
        // check health
        if (mc.player.getHealth() > 6.5 * 2F)
            return false;

        // check screen
        if (Wrapper.mc.currentScreen != null)
            return false;

        // check for clickable objects
        if (Wrapper.mc.objectMouseOver != null) {
            // clickable entities
            Entity entity = Wrapper.mc.objectMouseOver.entityHit;
            if (entity instanceof EntityVillager
                    || entity instanceof EntityTameable)
                return false;

            // clickable blocks
            Wrapper.mc.objectMouseOver.getBlockPos();
            return !(getBlock(
                    Wrapper.mc.objectMouseOver.getBlockPos()) instanceof BlockContainer);
        }

        return true;
    }

    private void stopIfEating() {
        if (oldSlot == -1)
            return;
        mc.gameSettings.keyBindUseItem.pressed = true;
        mc.player.inventory.currentItem = oldSlot;
        oldSlot = -1;
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static ItemStack windowClick_PICKUP(int slot) {
        return getPlayerController().windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
    }

    private static PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    public static void processRightClick() {
        getPlayerController().processRightClick(mc.player,
                mc.player.world, EnumHand.MAIN_HAND);
    }

    public static ItemStack windowClick_QUICK_MOVE(int slot) {
        return getPlayerController().windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);
    }


}

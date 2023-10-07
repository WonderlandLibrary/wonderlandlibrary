/**
 * Time: 11:39:31 PM
 * Date: Dec 25, 2016
 * Creator: cool1
 */
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.NetUtil;
import info.sigmaclient.util.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ChestStealer extends Module {

    private String DELAY = "DELAY";
    private String DROP = "DROP";
    private String CLOSE = "CLOSE";
    private String CHESTAURA = "CHESTAURA";
    private String IGNORE = "IGNORE";
    private Timer timer = new Timer();
    private Timer stealTimer = new Timer();
    private boolean isStealing;

    public ChestStealer(ModuleData data) {
        super(data);
        settings.put(DELAY, new Setting<>(DELAY, 2, "Tick delay before grabbing next item.", 1, 1, 5));
        settings.put(DROP, new Setting<>(DROP, false, "Auto drop items."));
        settings.put(CLOSE, new Setting<>(CLOSE, true, "Auto closes chests when done."));
        settings.put(CHESTAURA, new Setting<>(CHESTAURA, false, "Auto opens chests near you."));
        settings.put(IGNORE, new Setting<>(IGNORE, true, "Ignores trash items for minigames."));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre() && ((Boolean) settings.get(CHESTAURA).getValue())) {
                if (stealTimer.delay(2000) && isStealing) {
                    stealTimer.reset();
                    isStealing = false;
                }
                for (Object o : mc.theWorld.loadedTileEntityList) {
                    if (o instanceof TileEntityChest) {
                        TileEntityChest chest = (TileEntityChest) o;
                        float x = chest.getPos().getX();
                        float y = chest.getPos().getY();
                        float z = chest.getPos().getZ();
                        if (!isStealing && !chest.isEmpty && mc.thePlayer.getDistance(x, y, z) < 4 && stealTimer.delay(1000) && mc.currentScreen == null) {
                            isStealing = true;
                            NetUtil.sendPacketNoEvents(new C08PacketPlayerBlockPlacement(chest.getPos(), getFacingDirection(chest.getPos()).getIndex(), mc.thePlayer.getCurrentEquippedItem(), x, y, z));
                            chest.isEmpty = true;
                            stealTimer.reset();
                            break;
                        }
                    }
                }
            } else if (mc.currentScreen instanceof GuiChest) {
                GuiChest guiChest = (GuiChest) mc.currentScreen;
                String name = guiChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
                String[] list = new String[] {"menu","selector","game","gui","server","inventory","play","teleporter","shop","melee","armor",
                "block","castle","mini","warp","teleport","user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe",
                "profile","tele","port","map","kit","select","lobby","vault","lock"};
                for(String str : list) {
                    if(name.contains(str))
                        return;
                }
                isStealing = true;
                boolean full = true;
                ItemStack[] arrayOfItemStack;
                int j = (arrayOfItemStack = mc.thePlayer.inventory.mainInventory).length;
                for (int i = 0; i < j; i++) {
                    ItemStack item = arrayOfItemStack[i];
                    if (item == null) {
                        full = false;
                        break;
                    }
                }
                boolean containsItems = false;
                if (!full) {
                    for (int index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); index++) {
                        ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(index);
                        if (stack != null && !isBad(stack)) {
                            containsItems = true;
                            break;
                        }
                    }
                    if (containsItems) {
                        for (int index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); index++) {
                            ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(index);
                            if (stack != null && timer.delay(50 * ((Number) settings.get(DELAY).getValue()).intValue()) && !isBad(stack)) {
                                mc.playerController.windowClick(guiChest.inventorySlots.windowId, index, 0, ((Boolean) settings.get(DROP).getValue()) ? 0 : 1, mc.thePlayer);
                                if (((Boolean) settings.get(DROP).getValue())) {
                                    mc.playerController.windowClick(guiChest.inventorySlots.windowId, -999, 0, 0, mc.thePlayer);
                                }
                                timer.reset();
                            }
                        }
                    } else if (((Boolean) settings.get(CLOSE).getValue())) {
                        mc.thePlayer.closeScreen();
                        isStealing = false;
                    }
                } else if (((Boolean) settings.get(CLOSE).getValue())) {
                    mc.thePlayer.closeScreen();
                    isStealing = false;
                }
            } else {
                isStealing = false;
            }
        }
    }

    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        }
        final MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null) {
            return rayResult.facing;
        }
        return direction;
    }

    private boolean isBad(ItemStack item) {
        if (!(Boolean) settings.get(IGNORE).getValue())
            return false;
        ItemStack is = null;
        float lastDamage = -1;
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is1 = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is1.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
                    if(lastDamage < getDamage(is1)) {
                        lastDamage = getDamage(is1);
                        is = is1;
                    }
                }
            }
        }


        if (is != null && is.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            float currentDamage = getDamage(is);
            float itemDamage = getDamage(item);
            if (itemDamage > currentDamage) {
                return false;
            }
        }




        return item != null &&
                ((item.getItem().getUnlocalizedName().contains("tnt")) ||
                        (item.getItem().getUnlocalizedName().contains("stick")) ||
                        (item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg")) ||
                        (item.getItem().getUnlocalizedName().contains("string")) ||
                        (item.getItem().getUnlocalizedName().contains("flint")) ||
                        (item.getItem().getUnlocalizedName().contains("compass")) ||
                        (item.getItem().getUnlocalizedName().contains("feather")) ||
                        (item.getItem().getUnlocalizedName().contains("bucket")) ||
                        (item.getItem().getUnlocalizedName().contains("snow")) ||
                        (item.getItem().getUnlocalizedName().contains("fish")) ||
                        (item.getItem().getUnlocalizedName().contains("enchant")) ||
                        (item.getItem().getUnlocalizedName().contains("exp")) ||
                        (item.getItem().getUnlocalizedName().contains("shears")) ||
                        (item.getItem().getUnlocalizedName().contains("anvil")) ||
                        (item.getItem().getUnlocalizedName().contains("torch")) ||
                        (item.getItem().getUnlocalizedName().contains("seeds")) ||
                        (item.getItem().getUnlocalizedName().contains("leather")) ||
                        ((item.getItem() instanceof ItemPickaxe)) ||
                        ((item.getItem() instanceof ItemGlassBottle)) ||
                        ((item.getItem() instanceof ItemTool)) ||
                        (item.getItem().getUnlocalizedName().contains("piston")) ||
                        ((item.getItem().getUnlocalizedName().contains("potion"))
                                && (isBadPotion(item))));
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword) stack.getItem()).getDamageGiven();
    }

}

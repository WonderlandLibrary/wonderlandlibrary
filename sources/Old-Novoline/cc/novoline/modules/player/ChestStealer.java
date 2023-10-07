package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.*;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.DoubleProperty;
import cc.novoline.modules.configurations.property.object.ListProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.enchantment.Enchantment.*;
import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;

public final class ChestStealer extends AbstractModule {

    private String[] list = new String[]{"mode", "delivery", "menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", //
            "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept",  //
            "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "anticheat", "travel", "settings", //
            "user", "preference", "compass", "cake", "wars", "buy", "upgrade", "ranged", "potions", "utility"};

    private List<Integer> containerSlots = new CopyOnWriteArrayList();
    private List<Integer> chestIds = new CopyOnWriteArrayList();
    private Timer timer = new Timer(), timerAura = new Timer();
    private boolean isStealing, slotsFilled;
    private int containerSize, windowID;

    @Property("auto_disable")
    private final ListProperty<String> auto_disable = PropertyFactory.createList("Death").acceptableValues("World Change", "Game End", "Death");
    @Property("delay")
    private final DoubleProperty delay = PropertyFactory.createDouble(100.0D).minimum(50.0D).maximum(150.0D);
    @Property("extra-packet")
    private final BooleanProperty extra_packet = PropertyFactory.booleanFalse();
    @Property("silent")
    private final BooleanProperty silent = PropertyFactory.booleanFalse();
    @Property("ignore")
    private final BooleanProperty ignore = PropertyFactory.booleanFalse();
    @Property("aura")
    private final BooleanProperty aura = PropertyFactory.booleanFalse();
    @Property("aura-range")
    private final DoubleProperty aura_range = PropertyFactory.createDouble(4.0D).minimum(1.5D).maximum(4.0D);

    public ChestStealer(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "ChestStealer", "Chest Stealer", EnumModuleType.PLAYER, "Steal items from chests");
        Manager.put(new Setting("CS_SILENT", "Silent", SettingType.CHECKBOX, this, silent));
        Manager.put(new Setting("CS_IGNORE", "Ignore", SettingType.CHECKBOX, this, ignore));
        Manager.put(new Setting("CS_EXTRA", "Extra Packet", SettingType.CHECKBOX, this, extra_packet));
        Manager.put(new Setting("CS_AURA", "Aura", SettingType.CHECKBOX, this, aura));
        Manager.put(new Setting("CS_AURA_RANGE", "Aura Range", SettingType.SLIDER, this, aura_range, 0.1D, aura::get));
        Manager.put(new Setting("CS_DELAY", "Delay", SettingType.SLIDER, this, delay, 5.0D, () -> !extra_packet.get()));
        Manager.put(new Setting("CS_AUTO_DISABLE", "Disable On", SettingType.SELECTBOX, this, auto_disable));
    }

    @Override
    public void onDisable() {
        slotsFilled = false;
        isStealing = false;
        containerSlots.clear();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                S2DPacketOpenWindow packet = (S2DPacketOpenWindow) event.getPacket();

                for (String blacklisted : list) {
                    if (packet.getWindowTitle().getUnformattedText().toLowerCase().contains(blacklisted)) {
                        isStealing = false;
                        return;
                    }
                }

                isStealing = packet.getGuiId().equals("minecraft:chest");

                if (isStealing) {
                    containerSize = packet.getSlotCount();
                    windowID = packet.getWindowId();
                    containerSlots.clear();
                    slotsFilled = false;
                }
            }

            if (isStealing) {
                if (event.getPacket() instanceof S30PacketWindowItems) {
                    S30PacketWindowItems packet = (S30PacketWindowItems) event.getPacket();

                    if (packet.getWindowID() == windowID) {
                        if (!slotsFilled) {
                            for (int i = 0; i < containerSize; i++) {
                                ItemStack stack = packet.getItemStacks()[i];

                                if (stack != null && (!ignore.get() || isNotBad(stack) && checkArmor(stack, packet) && checkTool(stack, packet) && checkSword(stack, packet) && checkBow(stack, packet))) {
                                    containerSlots.add(i);
                                }
                            }

                            slotsFilled = true;
                        }
                    }
                }
            }
        }
    }

    private List<TileEntityChest> tileEntityChestList() {
        return mc.world.getLoadedTileEntityList()
                .stream().filter(te -> te instanceof TileEntityChest)
                .map(te -> (TileEntityChest) te).filter(te -> mc.player.getDistance(te.getPos()) <= aura_range.get())
                .sorted(Comparator.comparing(o -> mc.player.getDistance(((TileEntityChest) o).getPos())).reversed()).collect(Collectors.toList());
    }

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (aura.get() && ServerUtils.inGameTicks() > 15) {
            if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
                if (!isStealing) {
                    for (TileEntityChest chest : tileEntityChestList()) {
                        int id = Integer.parseInt(StringUtils.digitString(chest.toString().replace("net.minecraft.tileentity.TileEntityChest@", "")));

                        if (!chestIds.contains(id) && timerAura.delay(300)) {
                            mc.playerController.onPlayerRightClick(mc.player, mc.world, mc.player.getHeldItem(), chest.getPos(), Block.getFacingDirection(chest.getPos()), MathHelper.getVec3(chest.getPos()));
                            chestIds.add(id);
                            timerAura.reset();
                        }
                    }

                } else {
                    timerAura.reset();
                }
            }
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        if (isStealing) {
            if (!containerSlots.isEmpty()) {
                Collections.reverse(containerSlots);

                for (int count = 0; count < containerSlots.size(); count++) {
                    if (extra_packet.get() || timer.delay(delay.get())) {
                        mc.playerController.windowClick(windowID, containerSlots.get(count), 0, 1, mc.player);
                        containerSlots.remove(containerSlots.get(count));
                        timer.reset();
                    }
                }

            } else {
                mc.player.closeScreen(silent.get() ? mc.currentScreen instanceof GuiInventory ? null : mc.currentScreen : null, windowID);
                isStealing = false;
            }
        }
    }

    @EventTarget
    public void onDisplayGuiChest(DisplayChestGuiEvent event) {
        if (silent.get() && event.getString().equals("minecraft:chest")) {
            mc.displayGuiScreen(mc.previousScreen instanceof GuiInventory ? null : mc.previousScreen);
        }
    }

    @EventTarget
    public void onKeyPress(KeyPressEvent event) {
        if (silent.get() && isStealing) {
            if (event.getKey() == -100 || event.getKey() == -99) {
                event.setCancelled(true);
            }
        }
    }

    private boolean checkArmor(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem().getUnlocalizedName().contains("helmet")) {
            return stack.equals(bestArmor(packet.getItemStacks(), Armor.HELMET)) && (bestArmor(Armor.HELMET) == null
                    || getProtection(bestArmor(packet.getItemStacks(), Armor.HELMET)) > getProtection(bestArmor(Armor.HELMET)));
        } else if (stack.getItem().getUnlocalizedName().contains("chestplate")) {
            return stack.equals(bestArmor(packet.getItemStacks(), Armor.CHESTPLATE)) && (bestArmor(Armor.CHESTPLATE) == null
                    || getProtection(bestArmor(packet.getItemStacks(), Armor.CHESTPLATE)) > getProtection(bestArmor(Armor.CHESTPLATE)));
        } else if (stack.getItem().getUnlocalizedName().contains("boots")) {
            return stack.equals(bestArmor(packet.getItemStacks(), Armor.BOOTS)) && (bestArmor(Armor.BOOTS) == null
                    || getProtection(bestArmor(packet.getItemStacks(), Armor.BOOTS)) > getProtection(bestArmor(Armor.BOOTS)));
        } else if (stack.getItem().getUnlocalizedName().contains("leggings")) {
            return stack.equals(bestArmor(packet.getItemStacks(), Armor.LEGGINS)) && (bestArmor(Armor.LEGGINS) == null
                    || getProtection(bestArmor(packet.getItemStacks(), Armor.LEGGINS)) > getProtection(bestArmor(Armor.LEGGINS)));
        }

        return true;
    }

    private boolean checkTool(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem() instanceof ItemPickaxe) {
            return stack.equals(bestTool(packet.getItemStacks(), Tool.PICKAXE)) && (bestTool(getInventory(), Tool.PICKAXE) == null
                    || getEfficiency(bestTool(packet.getItemStacks(), Tool.PICKAXE)) > getEfficiency(bestTool(getInventory(), Tool.PICKAXE)));
        } else if (stack.getItem() instanceof ItemAxe) {
            return stack.equals(bestTool(packet.getItemStacks(), Tool.AXE)) && (bestTool(getInventory(), Tool.AXE) == null
                    || getEfficiency(bestTool(packet.getItemStacks(), Tool.AXE)) > getEfficiency(bestTool(getInventory(), Tool.AXE)));
        } else if (stack.getItem() instanceof ItemSpade) {
            return stack.equals(bestTool(packet.getItemStacks(), Tool.SHOVEL)) && (bestTool(getInventory(), Tool.SHOVEL) == null
                    || getEfficiency(bestTool(packet.getItemStacks(), Tool.SHOVEL)) > getEfficiency(bestTool(getInventory(), Tool.SHOVEL)));
        }

        return true;
    }

    private boolean checkSword(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem() instanceof ItemSword) {
            return stack.equals(bestSword(packet.getItemStacks())) && (bestSword(getInventory()) == null || getDamage(bestSword(packet.getItemStacks())) > getDamage(bestSword(getInventory())));
        }

        return true;
    }

    private boolean checkBow(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem() instanceof ItemBow) {
            return stack.equals(bestBow(packet.getItemStacks())) && (bestBow(getInventory()) == null || getPower(bestBow(packet.getItemStacks())) > getPower(bestBow(getInventory())));
        }

        return true;
    }

    private boolean isNotBad(ItemStack item) {
        ItemStack stack = null;
        float lastDamage = -1;

        for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is1 = mc.player.inventoryContainer.getSlot(i).getStack();

                if (is1.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
                    if (lastDamage < getDamage(is1)) {
                        lastDamage = getDamage(is1);
                        stack = is1;
                    }
                }
            }
        }

        if (stack != null && stack.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            final float currentDamage = getDamage(stack);
            final float itemDamage = getDamage(item);

            if (itemDamage > currentDamage) return true;
        }

        return item == null || !item.getItem()
                .getUnlocalizedName().contains("stick") && (!item.getItem().getUnlocalizedName().contains("egg") || item
                .getItem().getUnlocalizedName().contains("leg")) && !item.getItem().getUnlocalizedName()
                .contains("string") && !item.getItem()
                .getUnlocalizedName().contains("compass") && !item.getItem().getUnlocalizedName()
                .contains("feather") && !item.getItem().getUnlocalizedName().contains("bucket") && !item.getItem()
                .getUnlocalizedName().contains("snow") && !item.getItem().getUnlocalizedName().contains("fish") && !item
                .getItem().getUnlocalizedName().contains("enchant") && !item.getItem().getUnlocalizedName()
                .contains("exp") && !item.getItem().getUnlocalizedName().contains("shears") && !item.getItem()
                .getUnlocalizedName().contains("anvil") && !item.getItem().getUnlocalizedName()
                .contains("torch") && !item.getItem().getUnlocalizedName().contains("seeds") && !item.getItem()
                .getUnlocalizedName().contains("leather") && !(item.getItem() instanceof ItemGlassBottle) && !item
                .getItem().getUnlocalizedName().contains("piston") && (!item.getItem().getUnlocalizedName()
                .contains("potion") || !isBadPotion(item));
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                return getModule(InvManager.class).isBadPotionEffect(stack, potion);
            }
        }

        return false;
    }

    private ItemStack[] getInventory() {
        return mc.player.inventory.mainInventory;
    }

    private ItemStack[] getArmorInventory() {
        return mc.player.inventory.armorInventory;
    }

    public ItemStack bestArmor(Armor armor) {
        if (hasArmor(getInventory(), armor) && hasArmor(getArmorInventory(), armor)) {
            return Stream.of(Arrays.stream(getInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(armorType(armor)))
                            .sorted((s1, s2) -> Float.compare(getProtection(s2), getProtection(s1))).collect(Collectors.toList()).stream().findFirst().get(),
                    Arrays.stream(getArmorInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(armorType(armor)))
                            .sorted((s1, s2) -> Float.compare(getProtection(s2), getProtection(s1))).collect(Collectors.toList())
                            .stream().findFirst().get()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(armorType(armor)))
                    .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                    .sorted((s1, s2) -> Float.compare(getProtection(s2), getProtection(s1))).collect(Collectors.toList()).stream().findFirst().get();
        } else if (hasArmor(getInventory(), armor)) {
            return Arrays.stream(getInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(armorType(armor)))
                    .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                    .sorted((s1, s2) -> Float.compare(getProtection(s2), getProtection(s1))).collect(Collectors.toList()).stream().findFirst().get();
        } else if (hasArmor(getArmorInventory(), armor)) {
            return Arrays.stream(getArmorInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(armorType(armor)))
                    .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                    .sorted((s1, s2) -> Float.compare(getProtection(s2), getProtection(s1))).collect(Collectors.toList()).stream().findFirst().get();
        }

        return null;
    }

    public ItemStack bestArmor(ItemStack[] container, Armor armor) {
        if (hasArmor(container, armor)) {
            return Arrays.stream(container).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(armorType(armor)))
                    .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                    .sorted((s1, s2) -> Float.compare(getProtection(s2), getProtection(s1))).sorted((s1, s2) -> Integer.compare(s2.getItemDamage(), s1.getItemDamage())).collect(Collectors.toList()).stream().findFirst().get();
        }

        return null;
    }

    public ItemStack bestTool(ItemStack[] container, Tool tool) {
        if (hasTool(container, tool)) {
            if (tool.equals(Tool.PICKAXE)) {
                return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemPickaxe)
                        .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                        .min((s1, s2) -> Float.compare(getEfficiency(s2), getEfficiency(s1))).get();
            } else if (tool.equals(Tool.SHOVEL)) {
                return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemSpade)
                        .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                        .min((s1, s2) -> Float.compare(getEfficiency(s2), getEfficiency(s1))).get();
            } else if (tool.equals(Tool.AXE)) {
                return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemAxe)
                        .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                        .min((s1, s2) -> Float.compare(getEfficiency(s2), getEfficiency(s1))).get();
            }
        }

        return null;
    }

    public ItemStack bestSword(ItemStack[] container) {
        if (hasSword(container)) {
            return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemSword)
                    .sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage()))
                    .sorted((s1, s2) -> Float.compare(getDamage(s2), getDamage(s1))).collect(Collectors.toList()).stream().findFirst().get();
        }

        return null;
    }

    public ItemStack bestBow(ItemStack[] container) {
        if (hasBow(container)) {
            return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemBow)
                    .sorted((s1, s2) -> Float.compare(getPower(s2), getPower(s1))).collect(Collectors.toList()).stream().findFirst().get();
        }

        return null;
    }

    private int containerContainsArmor(ItemStack[] container, Armor armor) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] != null && container[i].getItem().getUnlocalizedName().contains(armorType(armor))) {
                return i;
            }
        }

        return -1;
    }

    private int containerContainsTool(ItemStack[] container, Tool tool) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] != null && container[i].getItem().getUnlocalizedName().contains(toolType(tool))) {
                return i;
            }
        }

        return -1;
    }

    private int containerContainsSword(ItemStack[] container) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] != null && container[i].getItem() instanceof ItemSword) {
                return i;
            }
        }

        return -1;
    }

    private int containerContainsBow(ItemStack[] container) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] != null && container[i].getItem() instanceof ItemBow) {
                return i;
            }
        }

        return -1;
    }

    public boolean hasArmor(ItemStack[] container, Armor armor) {
        int i = this.containerContainsArmor(container, armor);
        return i >= 0;
    }

    public boolean hasTool(ItemStack[] container, Tool tool) {
        int i = this.containerContainsTool(container, tool);
        return i >= 0;
    }

    public boolean hasSword(ItemStack[] container) {
        int i = this.containerContainsSword(container);
        return i >= 0;
    }

    public boolean hasBow(ItemStack[] container) {
        int i = this.containerContainsBow(container);
        return i >= 0;
    }

    public String armorType(Armor armor) {
        if (armor.equals(Armor.LEGGINS)) {
            return "leggings";
        } else if (armor.equals(Armor.CHESTPLATE)) {
            return "chestplate";
        } else if (armor.equals(Armor.BOOTS)) {
            return "boots";
        } else if (armor.equals(Armor.HELMET)) {
            return "helmet";
        } else {
            return "";
        }
    }

    public String toolType(Tool tool) {
        if (tool.equals(Tool.AXE)) {
            return "hatchet";
        } else if (tool.equals(Tool.PICKAXE)) {
            return "pickaxe";
        } else if (tool.equals(Tool.SHOVEL)) {
            return "shovel";
        } else {
            return "";
        }
    }

    private float getPower(ItemStack stack) {
        return (float) (1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.flame.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack));
    }


    private float getDamage(@NonNull ItemStack stack) {
        float damage = 0;
        final Item item = stack.getItem();

        if (item instanceof ItemTool) {
            damage += ((ItemTool) item).getDamage();
        } else if (item instanceof ItemSword) {
            damage += ((ItemSword) item).getAttackDamage();
        }

        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;

        return damage;
    }

    private float getProtection(@NonNull ItemStack stack) {
        float protection = 0;

        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            protection += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
            protection += getEnchantmentLevel(blastProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(fireProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(thorns.effectId, stack) / 100d;
            protection += getEnchantmentLevel(unbreaking.effectId, stack) / 50d;
            protection += getEnchantmentLevel(projectileProtection.effectId, stack) / 100d;
        }

        return protection;
    }

    private float getEfficiency(@NonNull ItemStack stack) {
        final Item item = stack.getItem();

        if (!(item instanceof ItemTool)) {
            return 0;
        }

        final String name = item.getUnlocalizedName();
        final ItemTool tool = (ItemTool) item;
        float value;

        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);

            if (name.toLowerCase().contains("gold")) {
                value -= 6;
            }

        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);

            if (name.toLowerCase().contains("gold")) {
                value -= 6;
            }

        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);

            if (name.toLowerCase().contains("gold")) {
                value -= 6;
            }

        } else {
            return 1f;
        }

        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D;
        return value;
    }

    public List<Integer> getChestIds() {
        return chestIds;
    }

    public ListProperty<String> getAutoDisable() {
        return auto_disable;
    }

    public boolean isStealing() {
        return isStealing;
    }

    public enum Armor {
        CHESTPLATE,
        LEGGINS,
        HELMET,
        BOOTS
    }

    public enum Tool {
        PICKAXE,
        SHOVEL,
        AXE
    }
}

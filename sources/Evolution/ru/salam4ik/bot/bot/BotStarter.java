package ru.salam4ik.bot.bot;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.network.BotLoginClient;
import ru.salam4ik.bot.bot.network.BotNetwork;
import wtf.evolution.Main;
import wtf.evolution.bot.ProxyS;
import wtf.evolution.command.impl.BotCommand;
import wtf.evolution.notifications.NotificationType;

import javax.vecmath.Vector2f;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static wtf.evolution.protect.Protection.genKey;
import static wtf.evolution.protect.Protection.getHwid;

public class BotStarter {
    public static final Random random;
    public static int integer;
    public static ExecutorService exec;
    public static Minecraft mc;

    public static void init() {
        String hwid = getHwid();
        Document a = null;
        try {
            a = Jsoup.connect("http://89.107.10.34:7777?hwid=" + hwid).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!a.text().equals(genKey(hwid))) {
            System.exit(0);
        }
        exec.submit(() -> {
            while (true) {
                for (Bot bot : Bot.bots) {
                    if (bot.getBot().getHealth() <= 0.0f) {
                        bot.getBot().respawnPlayer();
                    }
                    bot.getController().updateController();
                    try {
                        bot.getWorld().tick();
                        bot.getWorld().updateEntities();


                        if (BotCommand.follow) {
                            Vector2f vector2f = BotStarter.getBlockAngles(BotStarter.mc.player.posX, BotStarter.mc.player.posY + 1.0, BotStarter.mc.player.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ);
                            bot.getBot().rotationYaw = BotStarter.normalizeYaw(vector2f.y);
                            bot.getBot().rotationPitch = BotStarter.normalizePitch(vector2f.x);
                            bot.getConnection().forward = true;

                        }
                        if (BotCommand.look) {
                            Vector2f vector2f = BotStarter.getBlockAngles(BotStarter.mc.player.posX, BotStarter.mc.player.posY + 1.0, BotStarter.mc.player.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ);
                            bot.getBot().rotationYaw = BotStarter.normalizeYaw(vector2f.y);
                            bot.getBot().rotationPitch = BotStarter.normalizePitch(vector2f.x);
                        }
                        if (BotCommand.mimic) {
                            bot.getConnection().forward = mc.gameSettings.keyBindForward.pressed;
                            bot.getConnection().backward = mc.gameSettings.keyBindBack.pressed;
                            bot.getConnection().left = mc.gameSettings.keyBindLeft.pressed;
                            bot.getConnection().right = mc.gameSettings.keyBindRight.pressed;
                            bot.getConnection().jump = mc.gameSettings.keyBindJump.pressed;
                            bot.getConnection().sneak = mc.gameSettings.keyBindSneak.pressed;
                            bot.getBot().rotationYaw = mc.player.rotationYaw;
                            bot.getBot().rotationPitch = mc.player.rotationPitch;

                        }
                        if (BotCommand.aura) {
                            Entity e = getFromName(BotCommand.target);
                            if (e != null && e != bot.getBot()) {
                                Vector2f vector2f = BotStarter.getBlockAngles(e.posX, e.posY + 0.5, e.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ);
                                bot.getBot().rotationYaw = BotStarter.normalizeYaw(vector2f.y) + wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(-3, 3);
                                bot.getBot().rotationPitch = BotStarter.normalizePitch(vector2f.x) + wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(-1, 1);
                                bot.getConnection().forward = !(bot.getBot().getDistance(e) <= 2);
                                if (bot.getBot().getDistance(e) <= 3.2) {
                                    if (bot.getBot().getCooledAttackStrength(1) >= 0.93) {

                                        
                                        bot.getController().attackEntity(bot.getBot(), e);
                                        bot.getBot().swingArm(EnumHand.MAIN_HAND);
                                        bot.getBot().resetCooldown();
                                    }
                                }
                            }
                        }
                        if (BotCommand.movingStrafe) {
                            float dst = mc.player.getDistance(bot.getBot());
                            double maxDst = 3;
                            float forward = 0f;
                            if (dst <= (maxDst - 0.3)) {
                                forward = -1;
                            } else if (dst > (maxDst)) {
                                forward = 1;
                            }

                            strafe(bot.getBot(), BotStarter.getBlockAngles(BotStarter.mc.player.posX, BotStarter.mc.player.posY + 1.0, BotStarter.mc.player.posZ, bot.getBot().posX, bot.getBot().posY, bot.getBot().posZ).y, 0.24D, forward, 1);
                        }
                        if (BotCommand.movingRandom) {
                            bot.getBot().rotationYaw += wtf.evolution.helpers.math.MathHelper.getRandomNumberBetween(-20, 20);
                            bot.getConnection().forward = true;
                        }


                            int i = 0;
                            for (ItemStack stack : bot.getBot().openContainer.getInventory()) {
                                i++;
                                
                                if (stack.getDisplayName().toLowerCase().contains("нажми") || stack.getDisplayName().toLowerCase().contains("click") || stack.getDisplayName().toLowerCase().contains("сюда") || stack.getDisplayName().toLowerCase().contains("here")) {
                                    if (bot.c.hasReached(400)) {
                                        System.out.println(i);
                                        bot.getConnection().getController().windowClick(bot.getBot().openContainer.windowId, i - 1, 0, ClickType.PICKUP, bot.getBot());
                                        Main.notify.call(bot.getBot().getName(), "Clicked " + (i - 1), NotificationType.INFO);
                                        bot.c.reset();
                                    }
                                }

                                if (bot.getBot().currentContainerName.toLowerCase().contains("зелье")) {
                                    if (PotionUtils.getColor(stack) == 255) {
                                        if (bot.c.hasReached(400)) {
                                            System.out.println(i);
                                            bot.getConnection().getController().windowClick(bot.getBot().openContainer.windowId, i - 1, 0, ClickType.PICKUP, bot.getBot());
                                            Main.notify.call(bot.getBot().getName(), "Clicked " + (i - 1), NotificationType.INFO);
                                            bot.c.reset();
                                        }
                                    }
                                }
                            }

                        if (BotCommand.sword) {
                            if (hasSword(bot))
                                bot.getBot().inventory.currentItem = getSword(bot);
                        }

                            if (BotCommand.armor) {
                                final InventoryPlayer inventory = bot.getBot().inventory;
                                final int[] ArmorSlots = new int[4];
                                final int[] ArmorValues = new int[4];
                                for (int type = 0; type < 4; ++type) {
                                    ArmorSlots[type] = -1;
                                    final ItemStack stack = inventory.armorItemInSlot(type);
                                    if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                                        final ItemArmor item = (ItemArmor) stack.getItem();
                                        ArmorValues[type] = getArmorValue(item, stack, bot);
                                    }
                                }
                                for (int slot = 0; slot < 36; ++slot) {
                                    final ItemStack stack = inventory.getStackInSlot(slot);
                                    if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                                        final ItemArmor item = (ItemArmor) stack.getItem();
                                        final int armorType = item.armorType.getIndex();
                                        final int armorValue = getArmorValue(item, stack, bot);
                                        if (armorValue > ArmorValues[armorType]) {
                                            ArmorSlots[armorType] = slot;
                                            ArmorValues[armorType] = armorValue;
                                        }
                                    }
                                }
                                final ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
                                Collections.shuffle(types);
                                for (final int i1 : types) {
                                    int j = ArmorSlots[i1];
                                    if (j == -1) {
                                        continue;
                                    }
                                    final ItemStack oldArmor = inventory.armorItemInSlot(i1);
                                    if (!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1) {
                                        continue;
                                    }
                                    if (j < 9) {
                                        j += 36;
                                    }
                                    if (bot.c.hasReached(2 * 100.0f)) {
                                        if (!isNullOrEmpty(oldArmor)) {
                                            bot.getController().windowClick(0, 8 - i1, 0, ClickType.QUICK_MOVE, bot.getBot());
                                        }
                                        bot.getController().windowClick(0, j, 0, ClickType.QUICK_MOVE, bot.getBot());
                                        bot.c.reset();
                                        break;
                                    }
                                    break;
                                }
                            }

                    }
                    catch (Exception exception) {
                        bot.getNetManager().closeChannel();
                        Bot.bots.remove(bot);
                    }
                }
                Thread.sleep(50L);
            }
        });
    }

    public static boolean hasSword(Bot bot) {
        for (ItemStack stack : bot.getBot().inventory.mainInventory) {
            if (stack.getItem() instanceof ItemSword) {
                return true;
            }
        }
        return false;
    }

    public static int getSword(Bot bot) {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = bot.getBot().inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemSword) {
                slot = i;
            }
        }
        return slot;
    }

    public static int getArmorValue(final ItemArmor item, final ItemStack stack, Bot bot) {
        final int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        final int armorToughness = (int) item.toughness;
        final int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        final Enchantment protection = Enchantments.PROTECTION;
        final int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        final DamageSource dmgSource = DamageSource.causePlayerDamage(bot.getBot());
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }

    public static boolean isNullOrEmpty(final ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    public static Entity getFromName(String name) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public static void strafe(BotPlayer player, float yaw, double speed, double forward, double direction) {
        if (forward != 0.0D) {
            if (direction > 0.0D) {
                yaw += (float) (forward > 0.0D ? -45 : 45);
            } else if (direction < 0.0D) {
                yaw += (float) (forward > 0.0D ? 45 : -45);
            }
            direction = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }

        double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
        double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
        double x = forward * speed * cos + direction * speed * sin;
        double z = forward * speed * sin - direction * speed * cos;

        player.motionX = x;
        player.motionZ = z;
    }


    public static float normalizeYaw(float f) {
        while (f > 360.0f) {
            f -= 360.0f;
        }
        while (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static float normalizePitch(float f) {
        while (f > 90.0f) {
            f -= 90.0f;
        }
        while (f < -90.0f) {
            f += 90.0f;
        }
        return f;
    }

    public static void run(String string, boolean use, String password) {
        new Thread(() -> {
                ProxyS.Proxy proxy = use ? Main.proxy.getProxy() : null;
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), string);
                try {
                    BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, proxy);
                    botNetwork.setNetHandler(new BotLoginClient(botNetwork));
                    botNetwork.sendPacket(new C00Handshake(GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
                    Thread.sleep(400);
                    botNetwork.sendPacket(new CPacketLoginStart(gameProfile));
                    botNetwork.password = password;
                }
                catch (Exception ignored) {

                }
        }).start();
    }



    static {
        random = new Random((long)((double)System.currentTimeMillis() * Math.random() + (double)System.nanoTime()));
        exec = Executors.newCachedThreadPool();
        mc = Minecraft.getMinecraft();
    }

    public static Vector2f getBlockAngles(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d - d4;
        double d8 = d3 - d6;
        double d9 = d2 - d5 - 1.0;
        double d10 = MathHelper.sqrt(d7 * d7 + d8 * d8);
        float f = (float)Math.toDegrees(-Math.atan(d7 / d8));
        float f2 = (float)(-Math.toDegrees(Math.atan(d9 / d10)));
        double d11 = Math.toDegrees(Math.atan(d8 / d7));
        if (d8 < 0.0 && d7 < 0.0) {
            f = (float)(90.0 + d11);
        } else if (d8 < 0.0 && d7 > 0.0) {
            f = (float)(-90.0 + d11);
        }
        return new Vector2f(f2, BotStarter.normalizeYaw(f));
    }

}
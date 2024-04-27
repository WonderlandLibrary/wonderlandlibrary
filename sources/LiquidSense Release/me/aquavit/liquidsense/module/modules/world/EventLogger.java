package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.block.PlaceInfo;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.event.events.WallDamageEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.exploit.Phase;
import me.aquavit.liquidsense.module.modules.misc.AntiBot;
import me.aquavit.liquidsense.module.modules.render.FreeCam;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Objects;

@ModuleInfo(name = "EventLogger", description = "log events", category = ModuleCategory.WORLD)
public class EventLogger extends Module {
    public final BoolValue hackerDetect = new BoolValue("HackerDetect", true);
    public final BoolValue lightningDetect = new BoolValue("LightningDetect", true);
    public final BoolValue explosionDefense = new BoolValue("ExplosionDefence", true);
    public final BoolValue antiObbyTrap = new BoolValue("AntiObbyTrap", true);

    //hackerDetect
    public static ArrayList<EntityPlayer> hackers = new ArrayList<>();
    private double noSlowVL;

    //explosionDefense
    private boolean blocked = false;

    //antiObbyTrap
    private BlockPos obspos = null;
    private boolean inWall, inWall2 = false;

    @Override
    public void onEnable() {
        super.onEnable();
        hackers.clear();

        obspos = null;
        inWall = false;
        inWall2 = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        hackers.clear();

        mc.thePlayer.deathTime = 0;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        // HackerDetect
        if (hackerDetect.get()) {
            if (mc.thePlayer.ticksExisted <= 50) {
                hackers.clear();
                return;
            }

            AntiBot ab = (AntiBot) LiquidSense.moduleManager.getModule(AntiBot.class);

            for (EntityPlayer player : mc.theWorld.playerEntities) {
                assert ab != null;
                if (player == mc.thePlayer || player.ticksExisted < 105 || hackers.contains(player)  || player.capabilities.isFlying || player.capabilities.isCreativeMode)
                    continue;

                //NoSlowDown
                if (player.isBlocking() && getBPS(player) >= 6) {
                    noSlowVL += 1;
                    if (noSlowVL >= 30) {
                        ClientUtils.displayChatMessage("§7[§c§lHackerDetect§7] §c" + player.getName() + " §7has §c§linvalid SlowDown");
                        LiquidSense.hud.addNotification(new Notification("HackerDetect", "§c" + player.getName() + "§r may used NoSlowDown", ColorType.WARNING, 1000, 500));
                        noSlowVL = 0;
                        hackers.add(player);
                    }
                }

                //OmniSprint
                if (player.isSprinting() && (player.moveForward < 0 || (player.moveForward == 0 && player.moveStrafing != 0))) {
                    ClientUtils.displayChatMessage("§7[§c§lHackerDetect§7] §c" + player.getName() + " §7must be using §cOmni Sprint");
                    LiquidSense.hud.addNotification(new Notification("HackerDetect", "§c" + player.getName() + "§r may used OmniSprint", ColorType.WARNING, 1000, 500));
                    hackers.add(player);
                }

                //Step
                double y = Math.abs((int) player.posY), lastY = Math.abs((int) player.lastTickPosY);
                double yDiff = y > lastY ? y - lastY : lastY - y;

                if (yDiff > 0 && mc.thePlayer.onGround && player.motionY == -0.0784000015258789) {
                    ClientUtils.displayChatMessage("§7[§c§lHackerDetect§7] §c" + player.getName() + " §7may using §cStep §7(" + yDiff + " block)");
                    LiquidSense.hud.addNotification(new Notification("HackerDetect", "§c" + player.getName() + "§r may used Step", ColorType.WARNING, 1000, 500));
                    hackers.add(player);
                }
            }
        }

        // ExplosionDefense
        if (explosionDefense.get()) {
            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityTNTPrimed && mc.thePlayer.getDistanceToEntity(entity) <= 10) {
                    final EntityTNTPrimed tntPrimed = (EntityTNTPrimed) entity;

                    if (tntPrimed.fuse <= 15) {
                        int slot = -1;
                        float bestDamage = 1F;

                        for (int i = 0; i < 9; i++) {
                            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);

                            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                                final float itemDamage = ((ItemSword) itemStack.getItem()).getDamageVsEntity() + 4F;

                                if (itemDamage > bestDamage) {
                                    bestDamage = itemDamage;
                                    slot = i;
                                }
                            }
                        }

                        if (slot != -1 && slot != mc.thePlayer.inventory.currentItem) {
                            mc.thePlayer.inventory.currentItem = slot;
                            mc.playerController.updateController();
                        }

                        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            mc.gameSettings.keyBindUseItem.pressed = true;
                            blocked = true;
                        }

                        return;
                    }
                }
            }

            if (blocked && !GameSettings.isKeyDown(mc.gameSettings.keyBindUseItem)) {
                mc.gameSettings.keyBindUseItem.pressed = false;
                LiquidSense.hud.addNotification(new Notification("TNTBlock", "autoblock when TNT explodes", ColorType.INFO, 750, 500));
                blocked = false;
            }
        }

        // AntiObbyTrap
        if (antiObbyTrap.get()) {
            if (LiquidSense.moduleManager.getModule(FreeCam.class).getState() || LiquidSense.moduleManager.getModule(Phase.class).getState())
                return;

            BlockPos sand = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ));
            Block sandBlock = mc.theWorld.getBlockState(sand).getBlock();
            BlockPos forge = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ));
            Block furnaceblock = mc.theWorld.getBlockState(forge).getBlock();
            BlockPos obsidianpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ));
            Block obsidianblock = mc.theWorld.getBlockState(obsidianpos).getBlock();

            if (obsidianblock == Block.getBlockById(49)) {
                updateTool(new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)));
                LiquidSense.hud.addNotification(new Notification("AntiObbyTrap", "saving u from Obsidian", ColorType.INFO, 1250, 500));
            }
            if (furnaceblock == Block.getBlockById(61)) {
                updateTool(new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)));
                LiquidSense.hud.addNotification(new Notification("AntiObbyTrap", "saving u from Furnace", ColorType.INFO, 1000, 500));
            }
            if (sandBlock == Block.getBlockById(12) || sandBlock == Block.getBlockById(13)) {
                updateTool(new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)));
                LiquidSense.hud.addNotification(new Notification("AntiObbyTrap", "saving u from Sand", ColorType.INFO, 750, 450));
            }
        }
    }

    @EventTarget
    public void onWallDamage(WallDamageEvent event) {
        if (antiObbyTrap.get()) {
            if (LiquidSense.moduleManager.getModule(FreeCam.class).getState() || LiquidSense.moduleManager.getModule(Phase.class).getState())
                return;

            inWall = true;
            obspos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);

            if (mc.gameSettings.keyBindSneak.isPressed()) {
                mc.theWorld.setBlockToAir(obspos);
                inWall2 = true;
                mc.thePlayer.deathTime = 10;
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (lightningDetect.get()) {
            if (packet instanceof S2CPacketSpawnGlobalEntity) {
                S2CPacketSpawnGlobalEntity packetIn = (S2CPacketSpawnGlobalEntity) packet;

                int x = (int) ((double) packetIn.func_149051_d() / 32.0D);
                int y = (int) ((double) packetIn.func_149050_e() / 32.0D);
                int z = (int) ((double) packetIn.func_149049_f() / 32.0D);

                float f = (float) (mc.thePlayer.posX - x);
                float f2 = (float) (mc.thePlayer.posZ - z);
                float distance = MathHelper.sqrt_float(f * f + f2 * f2);

                if (packetIn.func_149053_g() == 1) {
                    ClientUtils.displayChatMessage("§7[§e§lLightningDetect§7] §c x: §f§l" + x + " §c y: §f§l" + y + " §c z: §f§l" + z);
                }

                ClientUtils.displayChatMessage("§7§l[§8"+(int) distance + "blocks away§7]");
            }
        }

        if (antiObbyTrap.get()) {
            if (inWall || inWall2) {
                if (packet instanceof C03PacketPlayer || packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                    inWall = false;
                    if (!MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isPressed()) {
                        event.cancelEvent();
                    }
                    // leave Obsidian
                    if (inWall2) {
                        if ((int) mc.thePlayer.posX != obspos.getX() || (int) mc.thePlayer.posY != obspos.getY() - 1 || (int) mc.thePlayer.posZ != obspos.getZ()) {
                            inWall2 = false;
                            if (mc.theWorld.getBlockState(obspos).getBlock() instanceof BlockAir) {
                                mc.playerController.onPlayerRightClick(
                                        mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), obspos,
                                        Objects.requireNonNull(PlaceInfo.get(obspos)).getEnumFacing(),
                                        new Vec3(obspos.getX() + Math.random(), obspos.getY() + Math.random(), obspos.getZ() + Math.random()));
                            }

                            obspos = null;
                        }
                    }
                }
            }
        }
    }

    public static double getBPS(Entity entity) {
        double xDif = entity.posX - entity.prevPosX;
        double zDif = entity.posZ - entity.prevPosZ;
        double lastDist = Math.sqrt(xDif * xDif + zDif * zDif) * 20.0D;
        return Math.round(lastDist);
    }

    public static boolean isHacker(EntityPlayer entity) {
        return hackers.contains(entity) && entity != mc.thePlayer;
    }

    public static void updateTool(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0F;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null) {
                continue;
            }
            if ((itemStack.getStrVsBlock(block) > strength)) {
                strength = itemStack.getStrVsBlock(block);
                bestItemIndex = i;
            }
        }
        if (bestItemIndex != -1) {
            mc.thePlayer.inventory.currentItem = bestItemIndex;
        }
    }
}


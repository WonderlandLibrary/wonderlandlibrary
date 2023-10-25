/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.util.ArrayList;
import java.util.List;
import me.Tengoku.Terror.event.events.EventMotion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public class PlayerUtils {
    protected static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isBlockUnderneath(BlockPos blockPos) {
        int n = 0;
        while (n < blockPos.getY() + 1) {
            if (Minecraft.theWorld.getBlockState(new BlockPos(blockPos.getX(), n, blockPos.getZ())).getBlock().getMaterial() != Material.air) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public static double getBaseMovementSpeed() {
        double d = 0.29;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            d *= 1.0 + 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return d;
    }

    public static double getBaseSpeed() {
        double d = 0.2873;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            int n = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            d *= 1.0 + 0.2 * (double)(n + 1);
        }
        return d;
    }

    public static void damagePlayer(int n) {
        if (Minecraft.thePlayer != null && Minecraft.getMinecraft().getNetHandler() != null) {
            int n2 = 0;
            while ((double)n2 <= (double)(3 + n) / 0.0625) {
                NetworkManager networkManager = Minecraft.thePlayer.sendQueue.getNetworkManager();
                double d = Minecraft.thePlayer.posX;
                double d2 = Minecraft.thePlayer.posY + 0.0625;
                Minecraft.getMinecraft();
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(d, d2, Minecraft.thePlayer.posZ, false));
                networkManager = Minecraft.thePlayer.sendQueue.getNetworkManager();
                Minecraft.getMinecraft();
                d = Minecraft.thePlayer.posX;
                Minecraft.getMinecraft();
                d2 = Minecraft.thePlayer.posY;
                Minecraft.getMinecraft();
                networkManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(d, d2, Minecraft.thePlayer.posZ, (double)n2 == (double)(3 + n) / 0.0625));
                ++n2;
            }
        }
    }

    public static float getBPS() {
        float f = PlayerUtils.mc.timer.ticksPerSecond * PlayerUtils.mc.timer.timerSpeed;
        return (int)(Minecraft.thePlayer.getDistance(Minecraft.thePlayer.lastTickPosX, Minecraft.thePlayer.posY, Minecraft.thePlayer.lastTickPosZ) * (double)f);
    }

    public static double defaultSpeed() {
        double d = 0.3;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            int n = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            d *= 1.0 + 0.2 * (double)(n + 1);
        }
        return d;
    }

    public static float getMaxFallDist() {
        PotionEffect potionEffect = Minecraft.thePlayer.getActivePotionEffect(Potion.jump);
        int n = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0;
        return Minecraft.thePlayer.getMaxFallHeight() + n;
    }

    public static void setMotion(EventMotion eventMotion, double d, double d2) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double d3 = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double d4 = (double)MovementInput.moveStrafe * d2;
        float f = Minecraft.thePlayer.rotationYaw;
        if (d3 == 0.0 && d4 == 0.0) {
            if (eventMotion != null) {
                eventMotion.setX(0.0);
                eventMotion.setZ(0.0);
            } else {
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
            }
        } else {
            if (d3 != 0.0) {
                if (d4 > 0.0) {
                    f += (float)(d3 > 0.0 ? -45 : 45);
                } else if (d4 < 0.0) {
                    f += (float)(d3 > 0.0 ? 45 : -45);
                }
                d4 = 0.0;
                if (d3 > 0.0) {
                    d3 = 1.0;
                } else if (d3 < 0.0) {
                    d3 = -1.0;
                }
            }
            if (eventMotion != null) {
                eventMotion.setX(d3 * d * Math.cos(Math.toRadians(f + 90.0f)) + d4 * d * Math.sin(Math.toRadians(f + 90.0f)));
                eventMotion.setZ(d3 * d * Math.sin(Math.toRadians(f + 90.0f)) - d4 * d * Math.cos(Math.toRadians(f + 90.0f)));
            } else {
                Minecraft.thePlayer.motionX = d3 * d * Math.cos(Math.toRadians(f + 90.0f)) + d4 * d * Math.sin(Math.toRadians(f + 90.0f));
                Minecraft.thePlayer.motionZ = d3 * d * Math.sin(Math.toRadians(f + 90.0f)) - d4 * d * Math.cos(Math.toRadians(f + 90.0f));
            }
        }
    }

    public static void setMoveSpeed(EventMotion eventMotion, double d, String string) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double d2 = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double d3 = MovementInput.moveStrafe;
        float f = Minecraft.thePlayer.rotationYaw;
        if (d2 != 0.0) {
            if (d3 > 0.0) {
                f += (float)(d2 > 0.0 ? -42 : 42);
            } else if (d3 < 0.0) {
                f += (float)(d2 > 0.0 ? 42 : -42);
            }
            d3 = 0.0;
            if (d2 > 0.0) {
                d2 = 1.0;
            } else if (d2 < 0.0) {
                d2 = -1.0;
            }
        }
        switch (string) {
            case "event": {
                if (string == null) break;
                eventMotion.setX(d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f)));
                eventMotion.setZ(d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f)));
                break;
            }
            case "default": {
                if (string == null) break;
                Minecraft.thePlayer.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
                Minecraft.thePlayer.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
            }
        }
    }

    public static void isSameColor(EntityLivingBase entityLivingBase) {
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            String string = entity.getDisplayName().getFormattedText();
        }
    }

    public static void setMotion(EventMotion eventMotion, double d) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double d2 = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double d3 = MovementInput.moveStrafe;
        float f = Minecraft.thePlayer.rotationYaw;
        if (d2 == 0.0 && d3 == 0.0) {
            if (eventMotion != null) {
                eventMotion.setX(0.0);
                eventMotion.setZ(0.0);
            } else {
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
            }
        } else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += (float)(d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    f += (float)(d2 > 0.0 ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            if (eventMotion != null) {
                eventMotion.setX(d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 91.5f)));
                eventMotion.setZ(d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.25f)));
            } else {
                Minecraft.thePlayer.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 91.5f));
                Minecraft.thePlayer.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.25f));
            }
        }
    }

    public static boolean isMoving() {
        Minecraft.getMinecraft();
        if (!Minecraft.gameSettings.keyBindForward.isKeyDown()) {
            Minecraft.getMinecraft();
            if (!Minecraft.gameSettings.keyBindBack.isKeyDown()) {
                Minecraft.getMinecraft();
                if (!Minecraft.gameSettings.keyBindLeft.isKeyDown()) {
                    Minecraft.getMinecraft();
                    if (!Minecraft.gameSettings.keyBindRight.isKeyDown()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static Block getBlockUnderPlayer(EntityPlayer entityPlayer, double d) {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY - d, entityPlayer.posZ)).getBlock();
    }

    public static void setMotion(EventMotion eventMotion, double d, int n) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double d2 = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double d3 = MovementInput.moveStrafe;
        float f = Minecraft.thePlayer.rotationYaw;
        if (d2 == 0.0 && d3 == 0.0) {
            if (eventMotion != null) {
                eventMotion.setX(0.0);
                eventMotion.setZ(0.0);
            } else {
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
            }
        } else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += (float)(d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    f += (float)(d2 > 0.0 ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            if (eventMotion != null) {
                eventMotion.setX(d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 91.5f)));
                eventMotion.setZ(d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.25f)));
            } else {
                Minecraft.thePlayer.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 91.5f));
                Minecraft.thePlayer.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.25f));
            }
        }
    }

    public static void setMotionWithValues(EventMotion eventMotion, double d, float f, double d2, double d3) {
        if (d2 == 0.0 && d3 == 0.0) {
            if (eventMotion != null) {
                eventMotion.setX(0.0);
                eventMotion.setZ(0.0);
            } else {
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
            }
        } else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += (float)(d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    f += (float)(d2 > 0.0 ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            if (eventMotion != null) {
                eventMotion.setX(d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f)));
                eventMotion.setZ(d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f)));
            } else {
                Minecraft.thePlayer.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
                Minecraft.thePlayer.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
            }
        }
    }

    public static int getJumpEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public static void setStrafeMotion(float f, double d) {
        Minecraft.thePlayer.motionX = Minecraft.thePlayer.getDirection() * f;
        Minecraft.thePlayer.motionZ = Minecraft.thePlayer.getDirection() * f;
    }

    public static int getSpeedEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static boolean isOnGround(double d) {
        return !Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -d, 0.0)).isEmpty();
    }

    public static boolean isInLiquid() {
        return Minecraft.thePlayer.isInWater() || Minecraft.thePlayer.isInLava();
    }

    public static double getMinFallDist() {
        double d = 3.0;
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            d += (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);
        }
        return d;
    }

    public static boolean canStep(double d) {
        if (!Minecraft.thePlayer.isCollidedHorizontally || !PlayerUtils.isOnGround(0.001)) {
            return false;
        }
        return !Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().expand(0.01, 0.0, 0.01).offset(0.0, d - 0.01, 0.0)).isEmpty() && Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().expand(0.1, 0.0, 0.01).offset(0.0, d + 0.1, 0.0)).isEmpty();
    }

    public static List<EntityPlayer> getTabPlayerList() {
        Minecraft minecraft = Minecraft.getMinecraft();
        NetHandlerPlayClient netHandlerPlayClient = Minecraft.thePlayer.sendQueue;
        ArrayList<EntityPlayer> arrayList = new ArrayList<EntityPlayer>();
        List list = GuiPlayerTabOverlay.field_175252_a.sortedCopy(netHandlerPlayClient.getPlayerInfoMap());
        for (Object e : list) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)e;
            if (networkPlayerInfo == null) continue;
            arrayList.add(Minecraft.theWorld.getPlayerEntityByName(networkPlayerInfo.getGameProfile().getName()));
        }
        return arrayList;
    }

    public static boolean isOnGround(double d, EntityPlayer entityPlayer) {
        return !Minecraft.theWorld.getCollidingBoundingBoxes(entityPlayer, entityPlayer.getEntityBoundingBox().offset(0.0, -d, 0.0)).isEmpty();
    }
}


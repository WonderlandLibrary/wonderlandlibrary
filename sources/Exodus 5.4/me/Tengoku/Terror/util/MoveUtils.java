/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import me.Tengoku.Terror.event.events.EventMotion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public class MoveUtils {
    protected static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isOnGround() {
        return Minecraft.thePlayer.onGround && Minecraft.thePlayer.isCollidedVertically;
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

    public static int getJumpEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public static double getMinFallDist() {
        double d = 3.0;
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            d += (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);
        }
        return d;
    }

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

    public static Block getBlockUnderPlayer(EntityPlayer entityPlayer, double d) {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY - d, entityPlayer.posZ)).getBlock();
    }

    public static float getMaxFallDist() {
        PotionEffect potionEffect = Minecraft.thePlayer.getActivePotionEffect(Potion.jump);
        int n = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0;
        return Minecraft.thePlayer.getMaxFallHeight() + n;
    }

    public static boolean isInLiquid() {
        return Minecraft.thePlayer.isInWater() || Minecraft.thePlayer.isInLava();
    }

    public static int getSpeedEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
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

    public static double getBaseMovementSpeed() {
        double d = 0.29;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            d *= 1.0 + 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return d;
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
}


/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.utils.MC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;

public class FakeClientPlayer
extends EntityLivingBase
implements MC {
    public MovementInput movementInput;

    public FakeClientPlayer(World world) {
        super(world);
        this.movementInput = new MovementInputFromOptions(FakeClientPlayer.mc.gameSettings);
    }

    @Override
    public ItemStack getHeldItem() {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int slotIn) {
        return null;
    }

    @Override
    public ItemStack getCurrentArmor(int slotIn) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
    }

    @Override
    public ItemStack[] getInventory() {
        return new ItemStack[0];
    }

    public void update(float speed) {
        this.movementInput.updatePlayerMoveState();
        float forward = this.movementInput.moveForward;
        float side = this.movementInput.moveStrafe;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        this.lastTickPosY = this.posY;
        this.lastTickPosX = this.posX;
        this.lastTickPosZ = this.posZ;
        float yaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * FakeClientPlayer.mc.timer.renderPartialTicks;
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double motionX = (double)(forward * speed) * cos + (double)(side * speed) * sin;
        double motionZ = (double)(forward * speed) * sin - (double)(side * speed) * cos;
        double motionY = 0.0;
        if (this.movementInput.jump) {
            motionY += 1.0;
        }
        if (this.movementInput.sneak) {
            motionY -= 1.0;
        }
        this.setPosition(this.posX + motionX, this.posY + motionY, this.posZ + motionZ);
    }
}


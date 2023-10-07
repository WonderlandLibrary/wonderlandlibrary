/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 */
package net.ccbluex.liquidbounce.utils.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

@FunctionalInterface
public interface ICheck {
    public static final Minecraft mc = Minecraft.func_71410_x();

    public boolean validate(Entity var1);
}


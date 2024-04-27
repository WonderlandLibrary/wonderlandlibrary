/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.EnumWorldBlockLayer
 */
package net.dev.important.injection.access;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;

public class StaticStorage {
    private static final EnumFacing[] facings = EnumFacing.values();
    public static ScaledResolution scaledResolution;
    private static final EnumChatFormatting[] chatFormatting;
    private static final EnumParticleTypes[] particleTypes;
    private static final EnumWorldBlockLayer[] worldBlockLayers;

    public static EnumFacing[] facings() {
        return facings;
    }

    public static EnumChatFormatting[] chatFormatting() {
        return chatFormatting;
    }

    public static EnumParticleTypes[] particleTypes() {
        return particleTypes;
    }

    public static EnumWorldBlockLayer[] worldBlockLayers() {
        return worldBlockLayers;
    }

    static {
        chatFormatting = EnumChatFormatting.values();
        particleTypes = EnumParticleTypes.values();
        worldBlockLayers = EnumWorldBlockLayer.values();
    }
}


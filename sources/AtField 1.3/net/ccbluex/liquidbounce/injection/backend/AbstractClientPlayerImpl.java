/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IAbstractClientPlayer;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerImpl;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class AbstractClientPlayerImpl
extends EntityPlayerImpl
implements IAbstractClientPlayer {
    public AbstractClientPlayerImpl(AbstractClientPlayer abstractClientPlayer) {
        super((EntityPlayer)abstractClientPlayer);
    }
}


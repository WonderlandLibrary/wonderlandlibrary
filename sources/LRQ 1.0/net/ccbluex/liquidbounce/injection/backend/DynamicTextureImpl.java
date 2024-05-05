/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.DynamicTexture
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IDynamicTexture;
import net.ccbluex.liquidbounce.injection.backend.AbstractTextureImpl;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;

public final class DynamicTextureImpl<T extends DynamicTexture>
extends AbstractTextureImpl<T>
implements IDynamicTexture {
    public DynamicTextureImpl(T wrapped) {
        super((AbstractTexture)wrapped);
    }
}


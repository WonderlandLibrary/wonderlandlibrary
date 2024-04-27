/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.util.BlockPos
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={C08PacketPlayerBlockPlacement.class})
public class MixinC08PacketPlayerBlockPlacement {
    @Shadow
    private BlockPos field_179725_b;
    @Shadow
    private int field_149579_d;
    @Shadow
    private ItemStack field_149580_e;
    @Shadow
    public float field_149577_f;
    @Shadow
    public float field_149578_g;
    @Shadow
    public float field_149584_h;

    @Overwrite
    public void func_148840_b(PacketBuffer buf) {
        buf.func_179255_a(this.field_179725_b);
        buf.writeByte(this.field_149579_d);
        buf.func_150788_a(this.field_149580_e);
        buf.writeByte((int)this.field_149577_f);
        buf.writeByte((int)this.field_149578_g);
        buf.writeByte((int)this.field_149584_h);
    }

    @Overwrite
    public void func_148837_a(PacketBuffer buf) throws IOException {
        this.field_179725_b = buf.func_179259_c();
        this.field_149579_d = buf.readUnsignedByte();
        this.field_149580_e = buf.func_150791_c();
        this.field_149577_f = buf.readUnsignedByte();
        this.field_149578_g = buf.readUnsignedByte();
        this.field_149584_h = buf.readUnsignedByte();
    }
}


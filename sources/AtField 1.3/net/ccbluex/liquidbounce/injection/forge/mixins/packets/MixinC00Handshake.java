/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.packets;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={C00Handshake.class})
public class MixinC00Handshake {
    @Shadow
    private EnumConnectionState field_149597_d;
    @Shadow
    public int field_149599_c;
    @Shadow
    public String field_149598_b;
    @Shadow
    private int field_149600_a;

    @Overwrite
    public void func_148840_b(PacketBuffer packetBuffer) {
        packetBuffer.func_150787_b(this.field_149600_a);
        packetBuffer.func_180714_a(this.field_149598_b + "\u0000FML\u0000");
        packetBuffer.writeShort(this.field_149599_c);
        packetBuffer.func_150787_b(this.field_149597_d.func_150759_c());
    }
}


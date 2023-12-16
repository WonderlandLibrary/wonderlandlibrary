// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import java.util.List;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import shadersmod.client.SVertexBuilder;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optifine.Reflector;

public class WorldVertexBufferUploader
{
    public int draw(final WorldRenderer p_178177_1_, final int p_178177_2_) {
        if (p_178177_2_ > 0) {
            final VertexFormat var3 = p_178177_1_.func_178973_g();
            final int var4 = var3.func_177338_f();
            final ByteBuffer var5 = p_178177_1_.func_178966_f();
            final List var6 = var3.func_177343_g();
            Iterator var7 = var6.iterator();
            final boolean forgePreDrawExists = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
            final boolean forgePostDrawExists = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();
            while (var7.hasNext()) {
                final VertexFormatElement var8 = var7.next();
                final VertexFormatElement.EnumUseage var9 = var8.func_177375_c();
                if (forgePreDrawExists) {
                    Reflector.callVoid(var9, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, var8, var4, var5);
                }
                else {
                    final int var10 = var8.func_177367_b().func_177397_c();
                    final int wr = var8.func_177369_e();
                    switch (SwitchEnumUseage.field_178958_a[var9.ordinal()]) {
                        default: {
                            continue;
                        }
                        case 1: {
                            var5.position(var8.func_177373_a());
                            GL11.glVertexPointer(var8.func_177370_d(), var10, var4, var5);
                            GL11.glEnableClientState(32884);
                            continue;
                        }
                        case 2: {
                            var5.position(var8.func_177373_a());
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + wr);
                            GL11.glTexCoordPointer(var8.func_177370_d(), var10, var4, var5);
                            GL11.glEnableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            continue;
                        }
                        case 3: {
                            var5.position(var8.func_177373_a());
                            GL11.glColorPointer(var8.func_177370_d(), var10, var4, var5);
                            GL11.glEnableClientState(32886);
                            continue;
                        }
                        case 4: {
                            var5.position(var8.func_177373_a());
                            GL11.glNormalPointer(var10, var4, var5);
                            GL11.glEnableClientState(32885);
                            continue;
                        }
                    }
                }
            }
            if (p_178177_1_.isMultiTexture()) {
                p_178177_1_.drawMultiTexture();
            }
            else if (Config.isShaders()) {
                SVertexBuilder.drawArrays(p_178177_1_.getDrawMode(), 0, p_178177_1_.func_178989_h(), p_178177_1_);
            }
            else {
                GL11.glDrawArrays(p_178177_1_.getDrawMode(), 0, p_178177_1_.func_178989_h());
            }
            var7 = var6.iterator();
            while (var7.hasNext()) {
                final VertexFormatElement var8 = var7.next();
                final VertexFormatElement.EnumUseage var9 = var8.func_177375_c();
                if (forgePostDrawExists) {
                    Reflector.callVoid(var9, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, var8, var4, var5);
                }
                else {
                    final int var10 = var8.func_177369_e();
                    switch (SwitchEnumUseage.field_178958_a[var9.ordinal()]) {
                        default: {
                            continue;
                        }
                        case 1: {
                            GL11.glDisableClientState(32884);
                            continue;
                        }
                        case 2: {
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var10);
                            GL11.glDisableClientState(32888);
                            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                            continue;
                        }
                        case 3: {
                            GL11.glDisableClientState(32886);
                            GlStateManager.func_179117_G();
                            continue;
                        }
                        case 4: {
                            GL11.glDisableClientState(32885);
                            continue;
                        }
                    }
                }
            }
        }
        p_178177_1_.reset();
        return p_178177_2_;
    }
    
    static final class SwitchEnumUseage
    {
        static final int[] field_178958_a;
        
        static {
            field_178958_a = new int[VertexFormatElement.EnumUseage.values().length];
            try {
                SwitchEnumUseage.field_178958_a[VertexFormatElement.EnumUseage.POSITION.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumUseage.field_178958_a[VertexFormatElement.EnumUseage.UV.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumUseage.field_178958_a[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumUseage.field_178958_a[VertexFormatElement.EnumUseage.NORMAL.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}

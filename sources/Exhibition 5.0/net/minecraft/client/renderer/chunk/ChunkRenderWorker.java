// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.chunk;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.concurrent.CancellationException;
import java.util.List;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import net.minecraft.util.EnumWorldBlockLayer;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER;
    private final ChunkRenderDispatcher field_178477_b;
    private final RegionRenderCacheBuilder field_178478_c;
    private static final String __OBFID = "CL_00002459";
    
    public ChunkRenderWorker(final ChunkRenderDispatcher p_i46201_1_) {
        this(p_i46201_1_, null);
    }
    
    public ChunkRenderWorker(final ChunkRenderDispatcher p_i46202_1_, final RegionRenderCacheBuilder p_i46202_2_) {
        this.field_178477_b = p_i46202_1_;
        this.field_178478_c = p_i46202_2_;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                this.func_178474_a(this.field_178477_b.func_178511_d());
            }
        }
        catch (InterruptedException var4) {
            ChunkRenderWorker.LOGGER.debug("Stopping due to interrupt");
        }
        catch (Throwable var3) {
            final CrashReport var2 = CrashReport.makeCrashReport(var3, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(var2));
        }
    }
    
    protected void func_178474_a(final ChunkCompileTaskGenerator p_178474_1_) throws InterruptedException {
        p_178474_1_.func_178540_f().lock();
        Label_0097: {
            try {
                if (p_178474_1_.func_178546_a() == ChunkCompileTaskGenerator.Status.PENDING) {
                    p_178474_1_.func_178535_a(ChunkCompileTaskGenerator.Status.COMPILING);
                    break Label_0097;
                }
                if (!p_178474_1_.func_178537_h()) {
                    ChunkRenderWorker.LOGGER.warn("Chunk render task was " + p_178474_1_.func_178546_a() + " when I expected it to be pending; ignoring task");
                }
            }
            finally {
                p_178474_1_.func_178540_f().unlock();
            }
            return;
        }
        final Entity var2 = Minecraft.getMinecraft().func_175606_aa();
        if (var2 == null) {
            p_178474_1_.func_178542_e();
        }
        else {
            p_178474_1_.func_178541_a(this.func_178475_b());
            final float var3 = (float)var2.posX;
            final float var4 = (float)var2.posY + var2.getEyeHeight();
            final float var5 = (float)var2.posZ;
            final ChunkCompileTaskGenerator.Type var6 = p_178474_1_.func_178538_g();
            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                p_178474_1_.func_178536_b().func_178581_b(var3, var4, var5, p_178474_1_);
            }
            else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                p_178474_1_.func_178536_b().func_178570_a(var3, var4, var5, p_178474_1_);
            }
            p_178474_1_.func_178540_f().lock();
            try {
                if (p_178474_1_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING) {
                    if (!p_178474_1_.func_178537_h()) {
                        ChunkRenderWorker.LOGGER.warn("Chunk render task was " + p_178474_1_.func_178546_a() + " when I expected it to be compiling; aborting task");
                    }
                    this.func_178473_b(p_178474_1_);
                    return;
                }
                p_178474_1_.func_178535_a(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally {
                p_178474_1_.func_178540_f().unlock();
            }
            final CompiledChunk var7 = p_178474_1_.func_178544_c();
            final ArrayList var8 = Lists.newArrayList();
            if (var6 == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                for (final EnumWorldBlockLayer var12 : EnumWorldBlockLayer.values()) {
                    if (var7.func_178492_d(var12)) {
                        var8.add(this.field_178477_b.func_178503_a(var12, p_178474_1_.func_178545_d().func_179038_a(var12), p_178474_1_.func_178536_b(), var7));
                    }
                }
            }
            else if (var6 == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                var8.add(this.field_178477_b.func_178503_a(EnumWorldBlockLayer.TRANSLUCENT, p_178474_1_.func_178545_d().func_179038_a(EnumWorldBlockLayer.TRANSLUCENT), p_178474_1_.func_178536_b(), var7));
            }
            final ListenableFuture var13 = Futures.allAsList((Iterable)var8);
            p_178474_1_.func_178539_a(new Runnable() {
                private static final String __OBFID = "CL_00002458";
                
                @Override
                public void run() {
                    var13.cancel(false);
                }
            });
            Futures.addCallback(var13, (FutureCallback)new FutureCallback() {
                private static final String __OBFID = "CL_00002457";
                
                public void func_178481_a(final List p_178481_1_) {
                    ChunkRenderWorker.this.func_178473_b(p_178474_1_);
                    p_178474_1_.func_178540_f().lock();
                    Label_0132: {
                        try {
                            if (p_178474_1_.func_178546_a() == ChunkCompileTaskGenerator.Status.UPLOADING) {
                                p_178474_1_.func_178535_a(ChunkCompileTaskGenerator.Status.DONE);
                                break Label_0132;
                            }
                            if (!p_178474_1_.func_178537_h()) {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was " + p_178474_1_.func_178546_a() + " when I expected it to be uploading; aborting task");
                            }
                        }
                        finally {
                            p_178474_1_.func_178540_f().unlock();
                        }
                        return;
                    }
                    p_178474_1_.func_178536_b().func_178580_a(var7);
                }
                
                public void onFailure(final Throwable p_onFailure_1_) {
                    ChunkRenderWorker.this.func_178473_b(p_178474_1_);
                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException)) {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
                
                public void onSuccess(final Object p_onSuccess_1_) {
                    this.func_178481_a((List)p_onSuccess_1_);
                }
            });
        }
    }
    
    private RegionRenderCacheBuilder func_178475_b() throws InterruptedException {
        return (this.field_178478_c != null) ? this.field_178478_c : this.field_178477_b.func_178515_c();
    }
    
    private void func_178473_b(final ChunkCompileTaskGenerator p_178473_1_) {
        if (this.field_178478_c == null) {
            this.field_178477_b.func_178512_a(p_178473_1_.func_178545_d());
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

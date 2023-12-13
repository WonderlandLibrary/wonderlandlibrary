// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.chunk;

import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ChunkCompileTaskGenerator
{
    private final RenderChunk field_178553_a;
    private final ReentrantLock field_178551_b;
    private final List field_178552_c;
    private final Type field_178549_d;
    private RegionRenderCacheBuilder field_178550_e;
    private CompiledChunk field_178547_f;
    private Status field_178548_g;
    private boolean field_178554_h;
    private static final String __OBFID = "CL_00002466";
    
    public ChunkCompileTaskGenerator(final RenderChunk p_i46208_1_, final Type p_i46208_2_) {
        this.field_178551_b = new ReentrantLock();
        this.field_178552_c = Lists.newArrayList();
        this.field_178548_g = Status.PENDING;
        this.field_178553_a = p_i46208_1_;
        this.field_178549_d = p_i46208_2_;
    }
    
    public Status func_178546_a() {
        return this.field_178548_g;
    }
    
    public RenderChunk func_178536_b() {
        return this.field_178553_a;
    }
    
    public CompiledChunk func_178544_c() {
        return this.field_178547_f;
    }
    
    public void func_178543_a(final CompiledChunk p_178543_1_) {
        this.field_178547_f = p_178543_1_;
    }
    
    public RegionRenderCacheBuilder func_178545_d() {
        return this.field_178550_e;
    }
    
    public void func_178541_a(final RegionRenderCacheBuilder p_178541_1_) {
        this.field_178550_e = p_178541_1_;
    }
    
    public void func_178535_a(final Status p_178535_1_) {
        this.field_178551_b.lock();
        try {
            this.field_178548_g = p_178535_1_;
        }
        finally {
            this.field_178551_b.unlock();
        }
    }
    
    public void func_178542_e() {
        this.field_178551_b.lock();
        try {
            if (this.field_178549_d == Type.REBUILD_CHUNK && this.field_178548_g != Status.DONE) {
                this.field_178553_a.func_178575_a(true);
            }
            this.field_178554_h = true;
            this.field_178548_g = Status.DONE;
            for (final Runnable var2 : this.field_178552_c) {
                var2.run();
            }
        }
        finally {
            this.field_178551_b.unlock();
        }
    }
    
    public void func_178539_a(final Runnable p_178539_1_) {
        this.field_178551_b.lock();
        try {
            this.field_178552_c.add(p_178539_1_);
            if (this.field_178554_h) {
                p_178539_1_.run();
            }
        }
        finally {
            this.field_178551_b.unlock();
        }
    }
    
    public ReentrantLock func_178540_f() {
        return this.field_178551_b;
    }
    
    public Type func_178538_g() {
        return this.field_178549_d;
    }
    
    public boolean func_178537_h() {
        return this.field_178554_h;
    }
    
    public enum Status
    {
        PENDING("PENDING", 0, "PENDING", 0), 
        COMPILING("COMPILING", 1, "COMPILING", 1), 
        UPLOADING("UPLOADING", 2, "UPLOADING", 2), 
        DONE("DONE", 3, "DONE", 3);
        
        private static final Status[] $VALUES;
        private static final String __OBFID = "CL_00002465";
        private static final Status[] $VALUES$;
        
        private Status(final String p_i46391_1_, final int p_i46391_2_, final String p_i46207_1_, final int p_i46207_2_) {
        }
        
        static {
            $VALUES = new Status[] { Status.PENDING, Status.COMPILING, Status.UPLOADING, Status.DONE };
            $VALUES$ = new Status[] { Status.PENDING, Status.COMPILING, Status.UPLOADING, Status.DONE };
        }
    }
    
    public enum Type
    {
        REBUILD_CHUNK("REBUILD_CHUNK", 0, "REBUILD_CHUNK", 0), 
        RESORT_TRANSPARENCY("RESORT_TRANSPARENCY", 1, "RESORT_TRANSPARENCY", 1);
        
        private static final Type[] $VALUES;
        private static final String __OBFID = "CL_00002464";
        private static final Type[] $VALUES$;
        
        private Type(final String p_i46392_1_, final int p_i46392_2_, final String p_i46206_1_, final int p_i46206_2_) {
        }
        
        static {
            $VALUES = new Type[] { Type.REBUILD_CHUNK, Type.RESORT_TRANSPARENCY };
            $VALUES$ = new Type[] { Type.REBUILD_CHUNK, Type.RESORT_TRANSPARENCY };
        }
    }
}

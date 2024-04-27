/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.resources.ResourcePackRepository
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.comparator.LastModifiedFileComparator
 *  org.apache.commons.io.filefilter.IOFileFilter
 *  org.apache.commons.io.filefilter.TrueFileFilter
 *  org.apache.logging.log4j.Logger
 */
package net.dev.important.injection.forge.mixins.client;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.resources.ResourcePackRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={ResourcePackRepository.class})
public class MixinResourcePackRepository {
    @Shadow
    @Final
    private File field_148534_e;
    @Shadow
    @Final
    private static Logger field_177320_c;

    @Overwrite
    private void func_183028_i() {
        try {
            ArrayList lvt_1_1_ = Lists.newArrayList((Iterable)FileUtils.listFiles((File)this.field_148534_e, (IOFileFilter)TrueFileFilter.TRUE, null));
            Collections.sort(lvt_1_1_, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            int lvt_2_1_ = 0;
            for (File lvt_4_1_ : lvt_1_1_) {
                if (lvt_2_1_++ < 10) continue;
                field_177320_c.info("Deleting old server resource pack " + lvt_4_1_.getName());
                FileUtils.deleteQuietly((File)lvt_4_1_);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


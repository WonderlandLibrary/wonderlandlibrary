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
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
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
    private static Logger field_177320_c;
    @Shadow
    @Final
    private File field_148534_e;

    @Overwrite
    private void func_183028_i() {
        try {
            ArrayList resourcePacksInFolder = Lists.newArrayList((Iterable)FileUtils.listFiles((File)this.field_148534_e, (IOFileFilter)TrueFileFilter.TRUE, null));
            resourcePacksInFolder.sort(LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            int count = 0;
            for (File resourcePackFile : resourcePacksInFolder) {
                if (count++ < 10) continue;
                field_177320_c.info("Deleting old server resource pack " + resourcePackFile.getName());
                FileUtils.deleteQuietly((File)resourcePackFile);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


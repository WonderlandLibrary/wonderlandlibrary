// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import java.util.List;

public class SoundEventAccessor implements ISoundEventAccessor<Sound>
{
    private final List<ISoundEventAccessor<Sound>> accessorList;
    private final Random rnd;
    private final ResourceLocation location;
    private final ITextComponent subtitle;
    
    public SoundEventAccessor(final ResourceLocation locationIn, @Nullable final String subtitleIn) {
        this.accessorList = (List<ISoundEventAccessor<Sound>>)Lists.newArrayList();
        this.rnd = new Random();
        this.location = locationIn;
        this.subtitle = ((subtitleIn == null) ? null : new TextComponentTranslation(subtitleIn, new Object[0]));
    }
    
    @Override
    public int getWeight() {
        int i = 0;
        for (final ISoundEventAccessor<Sound> isoundeventaccessor : this.accessorList) {
            i += isoundeventaccessor.getWeight();
        }
        return i;
    }
    
    @Override
    public Sound cloneEntry() {
        final int i = this.getWeight();
        if (!this.accessorList.isEmpty() && i != 0) {
            int j = this.rnd.nextInt(i);
            for (final ISoundEventAccessor<Sound> isoundeventaccessor : this.accessorList) {
                j -= isoundeventaccessor.getWeight();
                if (j < 0) {
                    return isoundeventaccessor.cloneEntry();
                }
            }
            return SoundHandler.MISSING_SOUND;
        }
        return SoundHandler.MISSING_SOUND;
    }
    
    public void addSound(final ISoundEventAccessor<Sound> p_188715_1_) {
        this.accessorList.add(p_188715_1_);
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    @Nullable
    public ITextComponent getSubtitle() {
        return this.subtitle;
    }
}

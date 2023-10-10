// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import oshi.hardware.SoundCard;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractSoundCard;

@Immutable
final class AixSoundCard extends AbstractSoundCard
{
    AixSoundCard(final String kernelVersion, final String name, final String codec) {
        super(kernelVersion, name, codec);
    }
    
    public static List<SoundCard> getSoundCards(final Supplier<List<String>> lscfg) {
        final List<SoundCard> soundCards = new ArrayList<SoundCard>();
        for (final String line : lscfg.get()) {
            final String s = line.trim();
            if (s.startsWith("paud")) {
                final String[] split = ParseUtil.whitespaces.split(s, 3);
                if (split.length != 3) {
                    continue;
                }
                soundCards.add(new AixSoundCard("unknown", split[2], "unknown"));
            }
        }
        return soundCards;
    }
}

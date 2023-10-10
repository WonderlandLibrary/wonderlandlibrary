// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import oshi.util.ExecutingCommand;
import oshi.hardware.SoundCard;
import java.util.List;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractSoundCard;

@Immutable
final class OpenBsdSoundCard extends AbstractSoundCard
{
    private static final Pattern AUDIO_AT;
    private static final Pattern PCI_AT;
    
    OpenBsdSoundCard(final String kernelVersion, final String name, final String codec) {
        super(kernelVersion, name, codec);
    }
    
    public static List<SoundCard> getSoundCards() {
        final List<String> dmesg = ExecutingCommand.runNative("dmesg");
        final Set<String> names = new HashSet<String>();
        for (final String line : dmesg) {
            final Matcher m = OpenBsdSoundCard.AUDIO_AT.matcher(line);
            if (m.matches()) {
                names.add(m.group(1));
            }
        }
        final Map<String, String> nameMap = new HashMap<String, String>();
        final Map<String, String> codecMap = new HashMap<String, String>();
        final Map<String, String> versionMap = new HashMap<String, String>();
        String key = "";
        for (final String line2 : dmesg) {
            final Matcher i = OpenBsdSoundCard.PCI_AT.matcher(line2);
            if (i.matches() && names.contains(i.group(1))) {
                key = i.group(1);
                nameMap.put(key, i.group(2));
                versionMap.put(key, i.group(3));
            }
            else {
                if (key.isEmpty()) {
                    continue;
                }
                int idx = line2.indexOf("codec");
                if (idx >= 0) {
                    idx = line2.indexOf(58);
                    codecMap.put(key, line2.substring(idx + 1).trim());
                }
                key = "";
            }
        }
        final List<SoundCard> soundCards = new ArrayList<SoundCard>();
        for (final Map.Entry<String, String> entry : nameMap.entrySet()) {
            soundCards.add(new OpenBsdSoundCard(versionMap.get(entry.getKey()), entry.getValue(), codecMap.get(entry.getKey())));
        }
        return soundCards;
    }
    
    static {
        AUDIO_AT = Pattern.compile("audio\\d+ at (.+)");
        PCI_AT = Pattern.compile("(.+) at pci\\d+ dev \\d+ function \\d+ \"(.*)\" (rev .+):.*");
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import java.util.Iterator;
import java.util.Map;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import java.util.HashMap;
import oshi.hardware.SoundCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractSoundCard;

@Immutable
final class SolarisSoundCard extends AbstractSoundCard
{
    private static final String LSHAL = "lshal";
    private static final String DEFAULT_AUDIO_DRIVER = "audio810";
    
    SolarisSoundCard(final String kernelVersion, final String name, final String codec) {
        super(kernelVersion, name, codec);
    }
    
    public static List<SoundCard> getSoundCards() {
        final Map<String, String> vendorMap = new HashMap<String, String>();
        final Map<String, String> productMap = new HashMap<String, String>();
        final List<String> sounds = new ArrayList<String>();
        String key = "";
        for (String line : ExecutingCommand.runNative("lshal")) {
            line = line.trim();
            if (line.startsWith("udi =")) {
                key = ParseUtil.getSingleQuoteStringValue(line);
            }
            else {
                if (key.isEmpty() || line.isEmpty()) {
                    continue;
                }
                if (line.contains("info.solaris.driver =") && "audio810".equals(ParseUtil.getSingleQuoteStringValue(line))) {
                    sounds.add(key);
                }
                else if (line.contains("info.product")) {
                    productMap.put(key, ParseUtil.getStringBetween(line, '\''));
                }
                else {
                    if (!line.contains("info.vendor")) {
                        continue;
                    }
                    vendorMap.put(key, ParseUtil.getStringBetween(line, '\''));
                }
            }
        }
        final List<SoundCard> soundCards = new ArrayList<SoundCard>();
        for (final String s : sounds) {
            soundCards.add(new SolarisSoundCard(productMap.get(s) + " " + "audio810", vendorMap.get(s) + " " + productMap.get(s), productMap.get(s)));
        }
        return soundCards;
    }
}

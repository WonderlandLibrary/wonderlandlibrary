// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import java.util.ArrayList;
import oshi.hardware.SoundCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractSoundCard;

@Immutable
final class MacSoundCard extends AbstractSoundCard
{
    private static final String APPLE = "Apple Inc.";
    
    MacSoundCard(final String kernelVersion, final String name, final String codec) {
        super(kernelVersion, name, codec);
    }
    
    public static List<SoundCard> getSoundCards() {
        final List<SoundCard> soundCards = new ArrayList<SoundCard>();
        final String manufacturer = "Apple Inc.";
        String kernelVersion = "AppleHDAController";
        final String codec = "AppleHDACodec";
        boolean version = false;
        final String versionMarker = "<key>com.apple.driver.AppleHDAController</key>";
        for (final String checkLine : FileUtil.readFile("/System/Library/Extensions/AppleHDA.kext/Contents/Info.plist")) {
            if (checkLine.contains(versionMarker)) {
                version = true;
            }
            else {
                if (!version) {
                    continue;
                }
                kernelVersion = "AppleHDAController " + ParseUtil.getTextBetweenStrings(checkLine, "<string>", "</string>");
                version = false;
            }
        }
        soundCards.add(new MacSoundCard(kernelVersion, manufacturer, codec));
        return soundCards;
    }
}

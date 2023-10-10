// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import oshi.hardware.SoundCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractSoundCard;

@Immutable
final class WindowsSoundCard extends AbstractSoundCard
{
    private static final String REGISTRY_SOUNDCARDS = "SYSTEM\\CurrentControlSet\\Control\\Class\\{4d36e96c-e325-11ce-bfc1-08002be10318}\\";
    
    WindowsSoundCard(final String kernelVersion, final String name, final String codec) {
        super(kernelVersion, name, codec);
    }
    
    public static List<SoundCard> getSoundCards() {
        final List<SoundCard> soundCards = new ArrayList<SoundCard>();
        final String[] registryGetKeys;
        final String[] keys = registryGetKeys = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "SYSTEM\\CurrentControlSet\\Control\\Class\\{4d36e96c-e325-11ce-bfc1-08002be10318}\\");
        for (final String key : registryGetKeys) {
            final String fullKey = "SYSTEM\\CurrentControlSet\\Control\\Class\\{4d36e96c-e325-11ce-bfc1-08002be10318}\\" + key;
            try {
                if (Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, fullKey, "Driver")) {
                    soundCards.add(new WindowsSoundCard(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, fullKey, "Driver") + " " + Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, fullKey, "DriverVersion"), Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, fullKey, "ProviderName") + " " + Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, fullKey, "DriverDesc"), Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, fullKey, "DriverDesc")));
                }
            }
            catch (Win32Exception e) {
                if (e.getErrorCode() != 5) {
                    throw e;
                }
            }
        }
        return soundCards;
    }
}

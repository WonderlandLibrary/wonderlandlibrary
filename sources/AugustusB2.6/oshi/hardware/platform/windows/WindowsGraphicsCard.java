// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import oshi.util.tuples.Triplet;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.Util;
import oshi.util.ParseUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32VideoController;
import java.util.ArrayList;
import oshi.hardware.GraphicsCard;
import java.util.List;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractGraphicsCard;

@Immutable
final class WindowsGraphicsCard extends AbstractGraphicsCard
{
    private static final boolean IS_VISTA_OR_GREATER;
    
    WindowsGraphicsCard(final String name, final String deviceId, final String vendor, final String versionInfo, final long vram) {
        super(name, deviceId, vendor, versionInfo, vram);
    }
    
    public static List<GraphicsCard> getGraphicsCards() {
        final List<GraphicsCard> cardList = new ArrayList<GraphicsCard>();
        if (WindowsGraphicsCard.IS_VISTA_OR_GREATER) {
            final WbemcliUtil.WmiResult<Win32VideoController.VideoControllerProperty> cards = Win32VideoController.queryVideoController();
            for (int index = 0; index < cards.getResultCount(); ++index) {
                final String name = WmiUtil.getString(cards, Win32VideoController.VideoControllerProperty.NAME, index);
                final Triplet<String, String, String> idPair = ParseUtil.parseDeviceIdToVendorProductSerial(WmiUtil.getString(cards, Win32VideoController.VideoControllerProperty.PNPDEVICEID, index));
                String deviceId = (idPair == null) ? "unknown" : idPair.getB();
                String vendor = WmiUtil.getString(cards, Win32VideoController.VideoControllerProperty.ADAPTERCOMPATIBILITY, index);
                if (idPair != null) {
                    if (Util.isBlank(vendor)) {
                        deviceId = idPair.getA();
                    }
                    else {
                        vendor = vendor + " (" + idPair.getA() + ")";
                    }
                }
                String versionInfo = WmiUtil.getString(cards, Win32VideoController.VideoControllerProperty.DRIVERVERSION, index);
                if (!Util.isBlank(versionInfo)) {
                    versionInfo = "DriverVersion=" + versionInfo;
                }
                else {
                    versionInfo = "unknown";
                }
                final long vram = WmiUtil.getUint32asLong(cards, Win32VideoController.VideoControllerProperty.ADAPTERRAM, index);
                cardList.add(new WindowsGraphicsCard(Util.isBlank(name) ? "unknown" : name, deviceId, Util.isBlank(vendor) ? "unknown" : vendor, versionInfo, vram));
            }
        }
        return cardList;
    }
    
    static {
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
    }
}

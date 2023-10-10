// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import java.util.Locale;
import java.util.List;
import oshi.util.ParseUtil;
import java.time.format.DateTimeParseException;
import oshi.util.ExecutingCommand;
import oshi.driver.linux.Sysfs;
import oshi.driver.linux.Dmidecode;
import oshi.util.Memoizer;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import java.time.format.DateTimeFormatter;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractFirmware;

@Immutable
final class LinuxFirmware extends AbstractFirmware
{
    private static final DateTimeFormatter VCGEN_FORMATTER;
    private final Supplier<String> manufacturer;
    private final Supplier<String> description;
    private final Supplier<String> version;
    private final Supplier<String> releaseDate;
    private final Supplier<String> name;
    private final Supplier<VcGenCmdStrings> vcGenCmd;
    private final Supplier<Pair<String, String>> biosNameRev;
    
    LinuxFirmware() {
        this.manufacturer = Memoizer.memoize(this::queryManufacturer);
        this.description = Memoizer.memoize(this::queryDescription);
        this.version = Memoizer.memoize(this::queryVersion);
        this.releaseDate = Memoizer.memoize(this::queryReleaseDate);
        this.name = Memoizer.memoize(this::queryName);
        this.vcGenCmd = Memoizer.memoize(LinuxFirmware::queryVcGenCmd);
        this.biosNameRev = Memoizer.memoize(Dmidecode::queryBiosNameRev);
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer.get();
    }
    
    @Override
    public String getDescription() {
        return this.description.get();
    }
    
    @Override
    public String getVersion() {
        return this.version.get();
    }
    
    @Override
    public String getReleaseDate() {
        return this.releaseDate.get();
    }
    
    @Override
    public String getName() {
        return this.name.get();
    }
    
    private String queryManufacturer() {
        String result = null;
        if ((result = Sysfs.queryBiosVendor()) == null && (result = this.vcGenCmd.get().manufacturer) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String queryDescription() {
        String result = null;
        if ((result = Sysfs.queryBiosDescription()) == null && (result = this.vcGenCmd.get().description) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String queryVersion() {
        String result = null;
        if ((result = Sysfs.queryBiosVersion(this.biosNameRev.get().getB())) == null && (result = this.vcGenCmd.get().version) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String queryReleaseDate() {
        String result = null;
        if ((result = Sysfs.queryBiosReleaseDate()) == null && (result = this.vcGenCmd.get().releaseDate) == null) {
            return "unknown";
        }
        return result;
    }
    
    private String queryName() {
        String result = null;
        if ((result = this.biosNameRev.get().getA()) == null && (result = this.vcGenCmd.get().name) == null) {
            return "unknown";
        }
        return result;
    }
    
    private static VcGenCmdStrings queryVcGenCmd() {
        String vcReleaseDate = null;
        String vcManufacturer = null;
        String vcVersion = null;
        final List<String> vcgencmd = ExecutingCommand.runNative("vcgencmd version");
        if (vcgencmd.size() >= 3) {
            try {
                vcReleaseDate = DateTimeFormatter.ISO_LOCAL_DATE.format(LinuxFirmware.VCGEN_FORMATTER.parse(vcgencmd.get(0)));
            }
            catch (DateTimeParseException e) {
                vcReleaseDate = "unknown";
            }
            final String[] copyright = ParseUtil.whitespaces.split(vcgencmd.get(1));
            vcManufacturer = copyright[copyright.length - 1];
            vcVersion = vcgencmd.get(2).replace("version ", "");
            return new VcGenCmdStrings(vcReleaseDate, vcManufacturer, vcVersion, "RPi", "Bootloader");
        }
        return new VcGenCmdStrings((String)null, (String)null, (String)null, (String)null, (String)null);
    }
    
    static {
        VCGEN_FORMATTER = DateTimeFormatter.ofPattern("MMM d uuuu HH:mm:ss", Locale.ENGLISH);
    }
    
    private static final class VcGenCmdStrings
    {
        private final String releaseDate;
        private final String manufacturer;
        private final String version;
        private final String name;
        private final String description;
        
        private VcGenCmdStrings(final String releaseDate, final String manufacturer, final String version, final String name, final String description) {
            this.releaseDate = releaseDate;
            this.manufacturer = manufacturer;
            this.version = version;
            this.name = name;
            this.description = description;
        }
    }
}

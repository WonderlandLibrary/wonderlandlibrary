package best.azura.client.util.crypt;

import java.util.UUID;

public class HWIDUtil {

    private static String hwid;

    public static String getHwid() {
        if (hwid != null && !hwid.isEmpty()) return hwid;
        return hwid = UUID.nameUUIDFromBytes((System.getenv("USERNAME") + System.getenv("OS") +
                System.getenv("COMPUTERNAME") + System.getenv("NUMBER_OF_PROCESSORS")).getBytes()).toString();
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  com.sun.jna.win32.StdCallLibrary
 *  com.sun.jna.win32.StdCallLibrary$StdCallCallback
 */
package dev.sakura_starring.util.safe;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import dev.sakura_starring.util.web.WebUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QQUtils {
    private static final String QQ_WINDOW_TEXT_PRE = "qqexchangewnd_shortcut_prefix_";
    public static boolean verification;
    private static final User32 user32;
    public static String qqNumber;
    public static String qqName;

    private static boolean _filterQQInfo(String string) {
        return string.startsWith("qqexchangewnd_shortcut_prefix_");
    }

    public static String getQQNick() throws IOException {
        return QQUtils.getSubString(WebUtils.get("https://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + QQUtils.getSubString(String.valueOf(QQUtils.getLoginQQList()), "=", "}")), "0,\"", "\",0");
    }

    static User32 access$000() {
        return user32;
    }

    public static Map getLoginQQList() {
        HashMap hashMap = new HashMap(5);
        user32.EnumWindows(new User32.WNDENUMPROC(hashMap){
            final Map val$map;

            @Override
            public boolean callback(Pointer pointer, Pointer pointer2) {
                byte[] byArray = new byte[512];
                QQUtils.access$000().GetWindowTextA(pointer, byArray, 512);
                String string = Native.toString((byte[])byArray);
                if (QQUtils.access$100(string)) {
                    this.val$map.put(pointer.toString(), string.substring(string.indexOf("qqexchangewnd_shortcut_prefix_") + "qqexchangewnd_shortcut_prefix_".length()));
                }
                qqNumber = QQUtils.getSubString(String.valueOf(this.val$map), "=", "}");
                return true;
            }
            {
                this.val$map = map;
            }
        }, null);
        return hashMap;
    }

    static boolean access$100(String string) {
        return QQUtils._filterQQInfo(string);
    }

    public static boolean verification() {
        if (verification) {
            return true;
        }
        List list = QQUtils.getServerQQLib();
        for (int i = 0; list.size() != 0 && i <= list.size() - 1; ++i) {
            if (!Objects.equals(list.get(i), qqNumber)) continue;
            verification = true;
            return true;
        }
        return false;
    }

    public static String getSubString(String string, String string2, String string3) {
        int n;
        n = string2 == null || string2.isEmpty() ? 0 : ((n = string.indexOf(string2)) > -1 ? (n += string2.length()) : 0);
        int n2 = string.indexOf(string3, n);
        if (n2 < 0 || string3 == null || string3.isEmpty()) {
            n2 = string.length();
        }
        String string4 = string.substring(n, n2);
        return string4;
    }

    public static List getServerQQLib() {
        ArrayList<String> arrayList = new ArrayList<String>();
        String string = "";
        String string2 = "";
        try {
            String string3 = "https://gitcode.net/2301_76573194/at-field-sense/-/raw/master/qq.txt";
            string = WebUtils.get(string3);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        for (int i = 0; i <= string.getBytes().length - 1; ++i) {
            while (i <= string.getBytes().length - 1 && string.getBytes()[i] != 10) {
                string2 = string2 + (char)string.getBytes()[i];
                ++i;
            }
            arrayList.add(string2);
            string2 = "";
        }
        return arrayList;
    }

    static {
        qqNumber = "";
        qqName = "";
        verification = false;
        user32 = User32.INSTANCE;
    }

    public static interface User32
    extends StdCallLibrary {
        public static final User32 INSTANCE = (User32)Native.loadLibrary((String)"user32", User32.class);

        public int GetWindowTextA(Pointer var1, byte[] var2, int var3);

        public boolean EnumWindows(WNDENUMPROC var1, Pointer var2);

        public static interface WNDENUMPROC
        extends StdCallLibrary.StdCallCallback {
            public boolean callback(Pointer var1, Pointer var2);
        }
    }
}


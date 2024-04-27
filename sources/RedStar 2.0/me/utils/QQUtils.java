package me.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.utils.WebUtil;

public class QQUtils {
    private static final String QQ_WINDOW_TEXT_PRE = "qqexchangewnd_shortcut_prefix_";
    private static final User32 user32 = User32.INSTANCE;

    public static Map<String, String> getLoginQQList() {
        final HashMap<String, String> map = new HashMap<String, String>(5);
        user32.EnumWindows(new User32.WNDENUMPROC(){

            @Override
            public boolean callback(Pointer hWnd, Pointer userData) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);
                if (QQUtils._filterQQInfo(wText)) {
                    map.put(hWnd.toString(), wText.substring(wText.indexOf(QQUtils.QQ_WINDOW_TEXT_PRE) + QQUtils.QQ_WINDOW_TEXT_PRE.length()));
                }
                return true;
            }
        }, null);
        return map;
    }

    public static String getSubString(String text, String left, String right) {
        int zLen;
        zLen = left == null || left.isEmpty() ? 0 : ((zLen = text.indexOf(left)) > -1 ? (zLen += left.length()) : 0);
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        String result = text.substring(zLen, yLen);
        return result;
    }

    public static String getQQNick() throws IOException {
        return QQUtils.getSubString(WebUtil.get("https://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + QQUtils.getSubString(String.valueOf(QQUtils.getLoginQQList()), "=", "}")), "0,\"", "\",0");
    }

    private static boolean _filterQQInfo(String windowText) {
        return windowText.startsWith(QQ_WINDOW_TEXT_PRE);
    }

    public static interface User32
    extends StdCallLibrary {
        public static final User32 INSTANCE = Native.loadLibrary("user32", User32.class);

        public boolean EnumWindows(WNDENUMPROC var1, Pointer var2);

        public int GetWindowTextA(Pointer var1, byte[] var2, int var3);

        public static interface WNDENUMPROC
        extends StdCallLibrary.StdCallCallback {
            public boolean callback(Pointer var1, Pointer var2);
        }
    }
}

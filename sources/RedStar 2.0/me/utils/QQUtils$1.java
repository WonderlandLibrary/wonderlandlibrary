package me.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.util.Map;
import me.utils.QQUtils;

final class QQUtils$1
implements QQUtils.User32.WNDENUMPROC {
    final Map val$map;

    QQUtils$1(Map map) {
        this.val$map = map;
    }

    @Override
    public boolean callback(Pointer hWnd, Pointer userData) {
        byte[] windowText = new byte[512];
        user32.GetWindowTextA(hWnd, windowText, 512);
        String wText = Native.toString(windowText);
        if (QQUtils._filterQQInfo(wText)) {
            this.val$map.put(hWnd.toString(), wText.substring(wText.indexOf(QQUtils.QQ_WINDOW_TEXT_PRE) + QQUtils.QQ_WINDOW_TEXT_PRE.length()));
        }
        return true;
    }
}

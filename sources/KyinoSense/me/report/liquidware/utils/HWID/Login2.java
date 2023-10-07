/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils.HWID;

import java.awt.AWTException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.report.liquidware.utils.HWID.HWIDUtils;
import me.report.liquidware.utils.HWID.WebUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lme/report/liquidware/utils/HWID/Login2;", "", "()V", "sendWindowsMessageLogin", "", "KyinoClient"})
public final class Login2 {
    public static final Login2 INSTANCE;

    public final void sendWindowsMessageLogin() throws AWTException {
        String username = JOptionPane.showInputDialog("Please enter your name account");
        String password = JOptionPane.showInputDialog("Please enter your password");
        try {
            if (username == null) {
                JOptionPane.showMessageDialog(null, "The username cannot be empty!", "KyinoSense | HWID", 0);
                System.exit(0);
            } else if (password == null) {
                JOptionPane.showMessageDialog(null, "The password cannot be empty!", "KyinoSense | HWID", 0);
                System.exit(0);
            } else {
                String string = WebUtils.get("https://github.com/Reportsrc/dev/blob/main/testhwid.txt");
                Intrinsics.checkExpressionValueIsNotNull(string, "WebUtils.get(\"https://gi\u2026/blob/main/testhwid.txt\")");
                if (StringsKt.contains$default((CharSequence)string, "[" + username + "]" + HWIDUtils.getHWID() + ":" + password, false, 2, null)) {
                    JOptionPane.showMessageDialog(null, "Successfully logged in", "KyinoSense | HWID", 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed", "KyinoSense | HWID", 0);
                    JOptionPane.showInputDialog(null, "Please contact your ticket and give your HWID!", HWIDUtils.getHWID());
                    System.exit(0);
                }
            }
        }
        catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "TURN ON THE INTERNET FUCKING!!!");
            System.exit(0);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "TURN ON THE INTERNET FUCKING!!!");
            System.exit(0);
        }
    }

    private Login2() {
    }

    static {
        Login2 login2;
        INSTANCE = login2 = new Login2();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  qiriyou.verV3Z.Loader
 */
package Verify1;

import Verify1.GuiPasswordField;
import Verify1.GuiUserField;
import Verify1.HWIDUtils;
import java.util.Base64;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import qiriyou.verV3Z.Loader;

public class GuiLogin
extends WrappedGuiScreen {
    static final Base64.Encoder encoder;
    static final Base64.Decoder decoder;
    int alpha = 0;
    private boolean i = false;
    private boolean j = false;
    public String UserName = null;
    public String Password = null;
    public static GuiPasswordField password;
    public static GuiUserField username;
    public static boolean render;
    private float currentX;
    private float currentY;
    public boolean drag = false;
    public static boolean Passed;
    public static boolean UnDisCheck;
    String hwid = HWIDUtils.getHWID();
    public static boolean isload;
    public static String HWID;
    public static int LOVEU;
    public static String process;

    @Override
    public native void drawScreen(int var1, int var2, float var3);

    @Override
    public native void initGui();

    @Override
    public native void keyTyped(char var1, int var2);

    private native void verify();

    @Override
    public native void mouseClicked(int var1, int var2, int var3);

    @Override
    public native void onGuiClosed();

    @Override
    public native void updateScreen();

    public static native String decode(String var0);

    static {
        Loader.registerNativesForClass((int)0, GuiLogin.class);
        GuiLogin.$qiriyouLoader();
        GuiLogin.$qiriyouClinit();
    }

    public static native /* synthetic */ void $qiriyouLoader();

    private static native /* synthetic */ void $qiriyouClinit();
}


/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;

final class WindowsEventDebug {
    private WindowsEventDebug() {
    }

    static int printMessage(String msg, long wParam, long lParam) {
        System.out.println(msg + ": 0x" + Long.toHexString(wParam).toUpperCase() + " | " + Long.toHexString(lParam).toUpperCase());
        return 0;
    }

    static int printMessage(int msg, long wParam, long lParam) {
        System.out.print(LWJGLUtil.toHexString(msg) + ": ");
        switch (msg) {
            case 0: {
                return WindowsEventDebug.printMessage("WM_NULL", wParam, lParam);
            }
            case 1: {
                return WindowsEventDebug.printMessage("WM_CREATE", wParam, lParam);
            }
            case 2: {
                return WindowsEventDebug.printMessage("WM_DESTROY", wParam, lParam);
            }
            case 3: {
                return WindowsEventDebug.printMessage("WM_MOVE", wParam, lParam);
            }
            case 5: {
                return WindowsEventDebug.printMessage("WM_SIZE", wParam, lParam);
            }
            case 6: {
                return WindowsEventDebug.printMessage("WM_ACTIVATE", wParam, lParam);
            }
            case 7: {
                return WindowsEventDebug.printMessage("WM_SETFOCUS", wParam, lParam);
            }
            case 8: {
                return WindowsEventDebug.printMessage("WM_KILLFOCUS", wParam, lParam);
            }
            case 10: {
                return WindowsEventDebug.printMessage("WM_ENABLE", wParam, lParam);
            }
            case 11: {
                return WindowsEventDebug.printMessage("WM_SETREDRAW", wParam, lParam);
            }
            case 12: {
                return WindowsEventDebug.printMessage("WM_SETTEXT", wParam, lParam);
            }
            case 13: {
                return WindowsEventDebug.printMessage("WM_GETTEXT", wParam, lParam);
            }
            case 14: {
                return WindowsEventDebug.printMessage("WM_GETTEXTLENGTH", wParam, lParam);
            }
            case 15: {
                return WindowsEventDebug.printMessage("WM_PAINT", wParam, lParam);
            }
            case 16: {
                return WindowsEventDebug.printMessage("WM_CLOSE", wParam, lParam);
            }
            case 17: {
                return WindowsEventDebug.printMessage("WM_QUERYENDSESSION", wParam, lParam);
            }
            case 19: {
                return WindowsEventDebug.printMessage("WM_QUERYOPEN", wParam, lParam);
            }
            case 22: {
                return WindowsEventDebug.printMessage("WM_ENDSESSION", wParam, lParam);
            }
            case 18: {
                return WindowsEventDebug.printMessage("WM_QUIT", wParam, lParam);
            }
            case 20: {
                return WindowsEventDebug.printMessage("WM_ERASEBKGND", wParam, lParam);
            }
            case 21: {
                return WindowsEventDebug.printMessage("WM_SYSCOLORCHANGE", wParam, lParam);
            }
            case 24: {
                return WindowsEventDebug.printMessage("WM_SHOWWINDOW", wParam, lParam);
            }
            case 26: {
                return WindowsEventDebug.printMessage("WM_WININICHANGE", wParam, lParam);
            }
            case 27: {
                return WindowsEventDebug.printMessage("WM_DEVMODECHANGE", wParam, lParam);
            }
            case 28: {
                return WindowsEventDebug.printMessage("WM_ACTIVATEAPP", wParam, lParam);
            }
            case 29: {
                return WindowsEventDebug.printMessage("WM_FONTCHANGE", wParam, lParam);
            }
            case 30: {
                return WindowsEventDebug.printMessage("WM_TIMECHANGE", wParam, lParam);
            }
            case 31: {
                return WindowsEventDebug.printMessage("WM_CANCELMODE", wParam, lParam);
            }
            case 32: {
                return WindowsEventDebug.printMessage("WM_SETCURSOR", wParam, lParam);
            }
            case 33: {
                return WindowsEventDebug.printMessage("WM_MOUSEACTIVATE", wParam, lParam);
            }
            case 34: {
                return WindowsEventDebug.printMessage("WM_CHILDACTIVATE", wParam, lParam);
            }
            case 35: {
                return WindowsEventDebug.printMessage("WM_QUEUESYNC", wParam, lParam);
            }
            case 36: {
                return WindowsEventDebug.printMessage("WM_GETMINMAXINFO", wParam, lParam);
            }
            case 38: {
                return WindowsEventDebug.printMessage("WM_PAINTICON", wParam, lParam);
            }
            case 39: {
                return WindowsEventDebug.printMessage("WM_ICONERASEBKGND", wParam, lParam);
            }
            case 40: {
                return WindowsEventDebug.printMessage("WM_NEXTDLGCTL", wParam, lParam);
            }
            case 42: {
                return WindowsEventDebug.printMessage("WM_SPOOLERSTATUS", wParam, lParam);
            }
            case 43: {
                return WindowsEventDebug.printMessage("WM_DRAWITEM", wParam, lParam);
            }
            case 44: {
                return WindowsEventDebug.printMessage("WM_MEASUREITEM", wParam, lParam);
            }
            case 45: {
                return WindowsEventDebug.printMessage("WM_DELETEITEM", wParam, lParam);
            }
            case 46: {
                return WindowsEventDebug.printMessage("WM_VKEYTOITEM", wParam, lParam);
            }
            case 47: {
                return WindowsEventDebug.printMessage("WM_CHARTOITEM", wParam, lParam);
            }
            case 48: {
                return WindowsEventDebug.printMessage("WM_SETFONT", wParam, lParam);
            }
            case 49: {
                return WindowsEventDebug.printMessage("WM_GETFONT", wParam, lParam);
            }
            case 50: {
                return WindowsEventDebug.printMessage("WM_SETHOTKEY", wParam, lParam);
            }
            case 51: {
                return WindowsEventDebug.printMessage("WM_GETHOTKEY", wParam, lParam);
            }
            case 55: {
                return WindowsEventDebug.printMessage("WM_QUERYDRAGICON", wParam, lParam);
            }
            case 57: {
                return WindowsEventDebug.printMessage("WM_COMPAREITEM", wParam, lParam);
            }
            case 61: {
                return WindowsEventDebug.printMessage("WM_GETOBJECT", wParam, lParam);
            }
            case 65: {
                return WindowsEventDebug.printMessage("WM_COMPACTING", wParam, lParam);
            }
            case 68: {
                return WindowsEventDebug.printMessage("WM_COMMNOTIFY", wParam, lParam);
            }
            case 70: {
                return WindowsEventDebug.printMessage("WM_WINDOWPOSCHANGING", wParam, lParam);
            }
            case 71: {
                return WindowsEventDebug.printMessage("WM_WINDOWPOSCHANGED", wParam, lParam);
            }
            case 72: {
                return WindowsEventDebug.printMessage("WM_POWER", wParam, lParam);
            }
            case 74: {
                return WindowsEventDebug.printMessage("WM_COPYDATA", wParam, lParam);
            }
            case 75: {
                return WindowsEventDebug.printMessage("WM_CANCELJOURNAL", wParam, lParam);
            }
            case 78: {
                return WindowsEventDebug.printMessage("WM_NOTIFY", wParam, lParam);
            }
            case 80: {
                return WindowsEventDebug.printMessage("WM_INPUTLANGCHANGEREQUEST", wParam, lParam);
            }
            case 81: {
                return WindowsEventDebug.printMessage("WM_INPUTLANGCHANGE", wParam, lParam);
            }
            case 82: {
                return WindowsEventDebug.printMessage("WM_TCARD", wParam, lParam);
            }
            case 83: {
                return WindowsEventDebug.printMessage("WM_HELP", wParam, lParam);
            }
            case 84: {
                return WindowsEventDebug.printMessage("WM_USERCHANGED", wParam, lParam);
            }
            case 85: {
                return WindowsEventDebug.printMessage("WM_NOTIFYFORMAT", wParam, lParam);
            }
            case 123: {
                return WindowsEventDebug.printMessage("WM_CONTEXTMENU", wParam, lParam);
            }
            case 124: {
                return WindowsEventDebug.printMessage("WM_STYLECHANGING", wParam, lParam);
            }
            case 125: {
                return WindowsEventDebug.printMessage("WM_STYLECHANGED", wParam, lParam);
            }
            case 126: {
                return WindowsEventDebug.printMessage("WM_DISPLAYCHANGE", wParam, lParam);
            }
            case 127: {
                return WindowsEventDebug.printMessage("WM_GETICON", wParam, lParam);
            }
            case 128: {
                return WindowsEventDebug.printMessage("WM_SETICON", wParam, lParam);
            }
            case 129: {
                return WindowsEventDebug.printMessage("WM_NCCREATE", wParam, lParam);
            }
            case 130: {
                return WindowsEventDebug.printMessage("WM_NCDESTROY", wParam, lParam);
            }
            case 131: {
                return WindowsEventDebug.printMessage("WM_NCCALCSIZE", wParam, lParam);
            }
            case 132: {
                return WindowsEventDebug.printMessage("WM_NCHITTEST", wParam, lParam);
            }
            case 133: {
                return WindowsEventDebug.printMessage("WM_NCPAINT", wParam, lParam);
            }
            case 134: {
                return WindowsEventDebug.printMessage("WM_NCACTIVATE", wParam, lParam);
            }
            case 135: {
                return WindowsEventDebug.printMessage("WM_GETDLGCODE", wParam, lParam);
            }
            case 136: {
                return WindowsEventDebug.printMessage("WM_SYNCPAINT", wParam, lParam);
            }
            case 160: {
                return WindowsEventDebug.printMessage("WM_NCMOUSEMOVE", wParam, lParam);
            }
            case 161: {
                return WindowsEventDebug.printMessage("WM_NCLBUTTONDOWN", wParam, lParam);
            }
            case 162: {
                return WindowsEventDebug.printMessage("WM_NCLBUTTONUP", wParam, lParam);
            }
            case 163: {
                return WindowsEventDebug.printMessage("WM_NCLBUTTONDBLCLK", wParam, lParam);
            }
            case 164: {
                return WindowsEventDebug.printMessage("WM_NCRBUTTONDOWN", wParam, lParam);
            }
            case 165: {
                return WindowsEventDebug.printMessage("WM_NCRBUTTONUP", wParam, lParam);
            }
            case 166: {
                return WindowsEventDebug.printMessage("WM_NCRBUTTONDBLCLK", wParam, lParam);
            }
            case 167: {
                return WindowsEventDebug.printMessage("WM_NCMBUTTONDOWN", wParam, lParam);
            }
            case 168: {
                return WindowsEventDebug.printMessage("WM_NCMBUTTONUP", wParam, lParam);
            }
            case 169: {
                return WindowsEventDebug.printMessage("WM_NCMBUTTONDBLCLK", wParam, lParam);
            }
            case 171: {
                return WindowsEventDebug.printMessage("WM_NCXBUTTONDOWN", wParam, lParam);
            }
            case 172: {
                return WindowsEventDebug.printMessage("WM_NCXBUTTONUP", wParam, lParam);
            }
            case 173: {
                return WindowsEventDebug.printMessage("WM_NCXBUTTONDBLCLK", wParam, lParam);
            }
            case 254: {
                return WindowsEventDebug.printMessage("WM_INPUT_DEVICE_CHANGE", wParam, lParam);
            }
            case 255: {
                return WindowsEventDebug.printMessage("WM_INPUT", wParam, lParam);
            }
            case 256: {
                return WindowsEventDebug.printMessage("WM_KEYDOWN", wParam, lParam);
            }
            case 257: {
                return WindowsEventDebug.printMessage("WM_KEYUP", wParam, lParam);
            }
            case 258: {
                return WindowsEventDebug.printMessage("WM_CHAR", wParam, lParam);
            }
            case 259: {
                return WindowsEventDebug.printMessage("WM_DEADCHAR", wParam, lParam);
            }
            case 260: {
                return WindowsEventDebug.printMessage("WM_SYSKEYDOWN", wParam, lParam);
            }
            case 261: {
                return WindowsEventDebug.printMessage("WM_SYSKEYUP", wParam, lParam);
            }
            case 262: {
                return WindowsEventDebug.printMessage("WM_SYSCHAR", wParam, lParam);
            }
            case 263: {
                return WindowsEventDebug.printMessage("WM_SYSDEADCHAR", wParam, lParam);
            }
            case 265: {
                return WindowsEventDebug.printMessage("WM_UNICHAR", wParam, lParam);
            }
            case 65535: {
                return WindowsEventDebug.printMessage("UNICODE_NOCHAR", wParam, lParam);
            }
            case 264: {
                return WindowsEventDebug.printMessage("WM_KEYLAST", wParam, lParam);
            }
            case 269: {
                return WindowsEventDebug.printMessage("WM_IME_STARTCOMPOSITION", wParam, lParam);
            }
            case 270: {
                return WindowsEventDebug.printMessage("WM_IME_ENDCOMPOSITION", wParam, lParam);
            }
            case 271: {
                return WindowsEventDebug.printMessage("WM_IME_COMPOSITION", wParam, lParam);
            }
            case 272: {
                return WindowsEventDebug.printMessage("WM_INITDIALOG", wParam, lParam);
            }
            case 273: {
                return WindowsEventDebug.printMessage("WM_COMMAND", wParam, lParam);
            }
            case 274: {
                return WindowsEventDebug.printMessage("WM_SYSCOMMAND", wParam, lParam);
            }
            case 275: {
                return WindowsEventDebug.printMessage("WM_TIMER", wParam, lParam);
            }
            case 276: {
                return WindowsEventDebug.printMessage("WM_HSCROLL", wParam, lParam);
            }
            case 277: {
                return WindowsEventDebug.printMessage("WM_VSCROLL", wParam, lParam);
            }
            case 278: {
                return WindowsEventDebug.printMessage("WM_INITMENU", wParam, lParam);
            }
            case 279: {
                return WindowsEventDebug.printMessage("WM_INITMENUPOPUP", wParam, lParam);
            }
            case 281: {
                return WindowsEventDebug.printMessage("WM_GESTURE", wParam, lParam);
            }
            case 282: {
                return WindowsEventDebug.printMessage("WM_GESTURENOTIFY", wParam, lParam);
            }
            case 287: {
                return WindowsEventDebug.printMessage("WM_MENUSELECT", wParam, lParam);
            }
            case 288: {
                return WindowsEventDebug.printMessage("WM_MENUCHAR", wParam, lParam);
            }
            case 289: {
                return WindowsEventDebug.printMessage("WM_ENTERIDLE", wParam, lParam);
            }
            case 290: {
                return WindowsEventDebug.printMessage("WM_MENURBUTTONUP", wParam, lParam);
            }
            case 291: {
                return WindowsEventDebug.printMessage("WM_MENUDRAG", wParam, lParam);
            }
            case 292: {
                return WindowsEventDebug.printMessage("WM_MENUGETOBJECT", wParam, lParam);
            }
            case 293: {
                return WindowsEventDebug.printMessage("WM_UNINITMENUPOPUP", wParam, lParam);
            }
            case 294: {
                return WindowsEventDebug.printMessage("WM_MENUCOMMAND", wParam, lParam);
            }
            case 295: {
                return WindowsEventDebug.printMessage("WM_CHANGEUISTATE", wParam, lParam);
            }
            case 296: {
                return WindowsEventDebug.printMessage("WM_UPDATEUISTATE", wParam, lParam);
            }
            case 297: {
                return WindowsEventDebug.printMessage("WM_QUERYUISTATE", wParam, lParam);
            }
            case 306: {
                return WindowsEventDebug.printMessage("WM_CTLCOLORMSGBOX", wParam, lParam);
            }
            case 307: {
                return WindowsEventDebug.printMessage("WM_CTLCOLOREDIT", wParam, lParam);
            }
            case 308: {
                return WindowsEventDebug.printMessage("WM_CTLCOLORLISTBOX", wParam, lParam);
            }
            case 309: {
                return WindowsEventDebug.printMessage("WM_CTLCOLORBTN", wParam, lParam);
            }
            case 310: {
                return WindowsEventDebug.printMessage("WM_CTLCOLORDLG", wParam, lParam);
            }
            case 311: {
                return WindowsEventDebug.printMessage("WM_CTLCOLORSCROLLBAR", wParam, lParam);
            }
            case 312: {
                return WindowsEventDebug.printMessage("WM_CTLCOLORSTATIC", wParam, lParam);
            }
            case 481: {
                return WindowsEventDebug.printMessage("MN_GETHMENU", wParam, lParam);
            }
            case 512: {
                return WindowsEventDebug.printMessage("WM_MOUSEMOVE", wParam, lParam);
            }
            case 513: {
                return WindowsEventDebug.printMessage("WM_LBUTTONDOWN", wParam, lParam);
            }
            case 514: {
                return WindowsEventDebug.printMessage("WM_LBUTTONUP", wParam, lParam);
            }
            case 515: {
                return WindowsEventDebug.printMessage("WM_LBUTTONDBLCLK", wParam, lParam);
            }
            case 516: {
                return WindowsEventDebug.printMessage("WM_RBUTTONDOWN", wParam, lParam);
            }
            case 517: {
                return WindowsEventDebug.printMessage("WM_RBUTTONUP", wParam, lParam);
            }
            case 518: {
                return WindowsEventDebug.printMessage("WM_RBUTTONDBLCLK", wParam, lParam);
            }
            case 519: {
                return WindowsEventDebug.printMessage("WM_MBUTTONDOWN", wParam, lParam);
            }
            case 520: {
                return WindowsEventDebug.printMessage("WM_MBUTTONUP", wParam, lParam);
            }
            case 521: {
                return WindowsEventDebug.printMessage("WM_MBUTTONDBLCLK", wParam, lParam);
            }
            case 522: {
                return WindowsEventDebug.printMessage("WM_MOUSEWHEEL", wParam, lParam);
            }
            case 523: {
                return WindowsEventDebug.printMessage("WM_XBUTTONDOWN", wParam, lParam);
            }
            case 524: {
                return WindowsEventDebug.printMessage("WM_XBUTTONUP", wParam, lParam);
            }
            case 525: {
                return WindowsEventDebug.printMessage("WM_XBUTTONDBLCLK", wParam, lParam);
            }
            case 526: {
                return WindowsEventDebug.printMessage("WM_MOUSEHWHEEL", wParam, lParam);
            }
            case 528: {
                return WindowsEventDebug.printMessage("WM_PARENTNOTIFY", wParam, lParam);
            }
            case 529: {
                return WindowsEventDebug.printMessage("WM_ENTERMENULOOP", wParam, lParam);
            }
            case 530: {
                return WindowsEventDebug.printMessage("WM_EXITMENULOOP", wParam, lParam);
            }
            case 531: {
                return WindowsEventDebug.printMessage("WM_NEXTMENU", wParam, lParam);
            }
            case 532: {
                return WindowsEventDebug.printMessage("WM_SIZING", wParam, lParam);
            }
            case 533: {
                return WindowsEventDebug.printMessage("WM_CAPTURECHANGED", wParam, lParam);
            }
            case 534: {
                return WindowsEventDebug.printMessage("WM_MOVING", wParam, lParam);
            }
            case 536: {
                return WindowsEventDebug.printMessage("WM_POWERBROADCAST", wParam, lParam);
            }
            case 32787: {
                return WindowsEventDebug.printMessage("PBT_POWERSETTINGCHANGE", wParam, lParam);
            }
            case 537: {
                return WindowsEventDebug.printMessage("WM_DEVICECHANGE", wParam, lParam);
            }
            case 544: {
                return WindowsEventDebug.printMessage("WM_MDICREATE", wParam, lParam);
            }
            case 545: {
                return WindowsEventDebug.printMessage("WM_MDIDESTROY", wParam, lParam);
            }
            case 546: {
                return WindowsEventDebug.printMessage("WM_MDIACTIVATE", wParam, lParam);
            }
            case 547: {
                return WindowsEventDebug.printMessage("WM_MDIRESTORE", wParam, lParam);
            }
            case 548: {
                return WindowsEventDebug.printMessage("WM_MDINEXT", wParam, lParam);
            }
            case 549: {
                return WindowsEventDebug.printMessage("WM_MDIMAXIMIZE", wParam, lParam);
            }
            case 550: {
                return WindowsEventDebug.printMessage("WM_MDITILE", wParam, lParam);
            }
            case 551: {
                return WindowsEventDebug.printMessage("WM_MDICASCADE", wParam, lParam);
            }
            case 552: {
                return WindowsEventDebug.printMessage("WM_MDIICONARRANGE", wParam, lParam);
            }
            case 553: {
                return WindowsEventDebug.printMessage("WM_MDIGETACTIVE", wParam, lParam);
            }
            case 560: {
                return WindowsEventDebug.printMessage("WM_MDISETMENU", wParam, lParam);
            }
            case 561: {
                return WindowsEventDebug.printMessage("WM_ENTERSIZEMOVE", wParam, lParam);
            }
            case 562: {
                return WindowsEventDebug.printMessage("WM_EXITSIZEMOVE", wParam, lParam);
            }
            case 563: {
                return WindowsEventDebug.printMessage("WM_DROPFILES", wParam, lParam);
            }
            case 564: {
                return WindowsEventDebug.printMessage("WM_MDIREFRESHMENU", wParam, lParam);
            }
            case 576: {
                return WindowsEventDebug.printMessage("WM_TOUCH", wParam, lParam);
            }
            case 641: {
                return WindowsEventDebug.printMessage("WM_IME_SETCONTEXT", wParam, lParam);
            }
            case 642: {
                return WindowsEventDebug.printMessage("WM_IME_NOTIFY", wParam, lParam);
            }
            case 643: {
                return WindowsEventDebug.printMessage("WM_IME_CONTROL", wParam, lParam);
            }
            case 644: {
                return WindowsEventDebug.printMessage("WM_IME_COMPOSITIONFULL", wParam, lParam);
            }
            case 645: {
                return WindowsEventDebug.printMessage("WM_IME_SELECT", wParam, lParam);
            }
            case 646: {
                return WindowsEventDebug.printMessage("WM_IME_CHAR", wParam, lParam);
            }
            case 648: {
                return WindowsEventDebug.printMessage("WM_IME_REQUEST", wParam, lParam);
            }
            case 656: {
                return WindowsEventDebug.printMessage("WM_IME_KEYDOWN", wParam, lParam);
            }
            case 657: {
                return WindowsEventDebug.printMessage("WM_IME_KEYUP", wParam, lParam);
            }
            case 673: {
                return WindowsEventDebug.printMessage("WM_MOUSEHOVER", wParam, lParam);
            }
            case 675: {
                return WindowsEventDebug.printMessage("WM_MOUSELEAVE", wParam, lParam);
            }
            case 672: {
                return WindowsEventDebug.printMessage("WM_NCMOUSEHOVER", wParam, lParam);
            }
            case 674: {
                return WindowsEventDebug.printMessage("WM_NCMOUSELEAVE", wParam, lParam);
            }
            case 689: {
                return WindowsEventDebug.printMessage("WM_WTSSESSION_CHANGE", wParam, lParam);
            }
            case 704: {
                return WindowsEventDebug.printMessage("WM_TABLET_FIRST", wParam, lParam);
            }
            case 735: {
                return WindowsEventDebug.printMessage("WM_TABLET_LAST", wParam, lParam);
            }
            case 768: {
                return WindowsEventDebug.printMessage("WM_CUT", wParam, lParam);
            }
            case 769: {
                return WindowsEventDebug.printMessage("WM_COPY", wParam, lParam);
            }
            case 770: {
                return WindowsEventDebug.printMessage("WM_PASTE", wParam, lParam);
            }
            case 771: {
                return WindowsEventDebug.printMessage("WM_CLEAR", wParam, lParam);
            }
            case 772: {
                return WindowsEventDebug.printMessage("WM_UNDO", wParam, lParam);
            }
            case 773: {
                return WindowsEventDebug.printMessage("WM_RENDERFORMAT", wParam, lParam);
            }
            case 774: {
                return WindowsEventDebug.printMessage("WM_RENDERALLFORMATS", wParam, lParam);
            }
            case 775: {
                return WindowsEventDebug.printMessage("WM_DESTROYCLIPBOARD", wParam, lParam);
            }
            case 776: {
                return WindowsEventDebug.printMessage("WM_DRAWCLIPBOARD", wParam, lParam);
            }
            case 777: {
                return WindowsEventDebug.printMessage("WM_PAINTCLIPBOARD", wParam, lParam);
            }
            case 778: {
                return WindowsEventDebug.printMessage("WM_VSCROLLCLIPBOARD", wParam, lParam);
            }
            case 779: {
                return WindowsEventDebug.printMessage("WM_SIZECLIPBOARD", wParam, lParam);
            }
            case 780: {
                return WindowsEventDebug.printMessage("WM_ASKCBFORMATNAME", wParam, lParam);
            }
            case 781: {
                return WindowsEventDebug.printMessage("WM_CHANGECBCHAIN", wParam, lParam);
            }
            case 782: {
                return WindowsEventDebug.printMessage("WM_HSCROLLCLIPBOARD", wParam, lParam);
            }
            case 783: {
                return WindowsEventDebug.printMessage("WM_QUERYNEWPALETTE", wParam, lParam);
            }
            case 784: {
                return WindowsEventDebug.printMessage("WM_PALETTEISCHANGING", wParam, lParam);
            }
            case 785: {
                return WindowsEventDebug.printMessage("WM_PALETTECHANGED", wParam, lParam);
            }
            case 786: {
                return WindowsEventDebug.printMessage("WM_HOTKEY", wParam, lParam);
            }
            case 791: {
                return WindowsEventDebug.printMessage("WM_PRINT", wParam, lParam);
            }
            case 792: {
                return WindowsEventDebug.printMessage("WM_PRINTCLIENT", wParam, lParam);
            }
            case 793: {
                return WindowsEventDebug.printMessage("WM_APPCOMMAND", wParam, lParam);
            }
            case 794: {
                return WindowsEventDebug.printMessage("WM_THEMECHANGED", wParam, lParam);
            }
            case 797: {
                return WindowsEventDebug.printMessage("WM_CLIPBOARDUPDATE", wParam, lParam);
            }
            case 798: {
                return WindowsEventDebug.printMessage("WM_DWMCOMPOSITIONCHANGED", wParam, lParam);
            }
            case 799: {
                return WindowsEventDebug.printMessage("WM_DWMNCRENDERINGCHANGED", wParam, lParam);
            }
            case 800: {
                return WindowsEventDebug.printMessage("WM_DWMCOLORIZATIONCOLORCHANGED", wParam, lParam);
            }
            case 801: {
                return WindowsEventDebug.printMessage("WM_DWMWINDOWMAXIMIZEDCHANGE", wParam, lParam);
            }
            case 803: {
                return WindowsEventDebug.printMessage("WM_DWMSENDICONICTHUMBNAIL", wParam, lParam);
            }
            case 806: {
                return WindowsEventDebug.printMessage("WM_DWMSENDICONICLIVEPREVIEWBITMAP", wParam, lParam);
            }
            case 831: {
                return WindowsEventDebug.printMessage("WM_GETTITLEBARINFOEX", wParam, lParam);
            }
            case 856: {
                return WindowsEventDebug.printMessage("WM_HANDHELDFIRST", wParam, lParam);
            }
            case 863: {
                return WindowsEventDebug.printMessage("WM_HANDHELDLAST", wParam, lParam);
            }
            case 864: {
                return WindowsEventDebug.printMessage("WM_AFXFIRST", wParam, lParam);
            }
            case 895: {
                return WindowsEventDebug.printMessage("WM_AFXLAST", wParam, lParam);
            }
            case 896: {
                return WindowsEventDebug.printMessage("WM_PENWINFIRST", wParam, lParam);
            }
            case 911: {
                return WindowsEventDebug.printMessage("WM_PENWINLAST", wParam, lParam);
            }
            case 32768: {
                return WindowsEventDebug.printMessage("WM_APP", wParam, lParam);
            }
        }
        return WindowsEventDebug.printMessage("<UNKNOWN>", wParam, lParam);
    }
}


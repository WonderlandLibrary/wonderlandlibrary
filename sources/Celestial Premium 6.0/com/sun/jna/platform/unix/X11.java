/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.unix;

import com.sun.jna.Callback;
import com.sun.jna.FromNativeContext;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

public interface X11
extends Library {
    public static final X11 INSTANCE = Native.loadLibrary("X11", X11.class);
    public static final int XK_0 = 48;
    public static final int XK_9 = 57;
    public static final int XK_A = 65;
    public static final int XK_Z = 90;
    public static final int XK_a = 97;
    public static final int XK_z = 122;
    public static final int XK_Shift_L = 65505;
    public static final int XK_Shift_R = 65505;
    public static final int XK_Control_L = 65507;
    public static final int XK_Control_R = 65508;
    public static final int XK_CapsLock = 65509;
    public static final int XK_ShiftLock = 65510;
    public static final int XK_Meta_L = 65511;
    public static final int XK_Meta_R = 65512;
    public static final int XK_Alt_L = 65513;
    public static final int XK_Alt_R = 65514;
    public static final int VisualNoMask = 0;
    public static final int VisualIDMask = 1;
    public static final int VisualScreenMask = 2;
    public static final int VisualDepthMask = 4;
    public static final int VisualClassMask = 8;
    public static final int VisualRedMaskMask = 16;
    public static final int VisualGreenMaskMask = 32;
    public static final int VisualBlueMaskMask = 64;
    public static final int VisualColormapSizeMask = 128;
    public static final int VisualBitsPerRGBMask = 256;
    public static final int VisualAllMask = 511;
    public static final Atom XA_PRIMARY = new Atom(1L);
    public static final Atom XA_SECONDARY = new Atom(2L);
    public static final Atom XA_ARC = new Atom(3L);
    public static final Atom XA_ATOM = new Atom(4L);
    public static final Atom XA_BITMAP = new Atom(5L);
    public static final Atom XA_CARDINAL = new Atom(6L);
    public static final Atom XA_COLORMAP = new Atom(7L);
    public static final Atom XA_CURSOR = new Atom(8L);
    public static final Atom XA_CUT_BUFFER0 = new Atom(9L);
    public static final Atom XA_CUT_BUFFER1 = new Atom(10L);
    public static final Atom XA_CUT_BUFFER2 = new Atom(11L);
    public static final Atom XA_CUT_BUFFER3 = new Atom(12L);
    public static final Atom XA_CUT_BUFFER4 = new Atom(13L);
    public static final Atom XA_CUT_BUFFER5 = new Atom(14L);
    public static final Atom XA_CUT_BUFFER6 = new Atom(15L);
    public static final Atom XA_CUT_BUFFER7 = new Atom(16L);
    public static final Atom XA_DRAWABLE = new Atom(17L);
    public static final Atom XA_FONT = new Atom(18L);
    public static final Atom XA_INTEGER = new Atom(19L);
    public static final Atom XA_PIXMAP = new Atom(20L);
    public static final Atom XA_POINT = new Atom(21L);
    public static final Atom XA_RECTANGLE = new Atom(22L);
    public static final Atom XA_RESOURCE_MANAGER = new Atom(23L);
    public static final Atom XA_RGB_COLOR_MAP = new Atom(24L);
    public static final Atom XA_RGB_BEST_MAP = new Atom(25L);
    public static final Atom XA_RGB_BLUE_MAP = new Atom(26L);
    public static final Atom XA_RGB_DEFAULT_MAP = new Atom(27L);
    public static final Atom XA_RGB_GRAY_MAP = new Atom(28L);
    public static final Atom XA_RGB_GREEN_MAP = new Atom(29L);
    public static final Atom XA_RGB_RED_MAP = new Atom(30L);
    public static final Atom XA_STRING = new Atom(31L);
    public static final Atom XA_VISUALID = new Atom(32L);
    public static final Atom XA_WINDOW = new Atom(33L);
    public static final Atom XA_WM_COMMAND = new Atom(34L);
    public static final Atom XA_WM_HINTS = new Atom(35L);
    public static final Atom XA_WM_CLIENT_MACHINE = new Atom(36L);
    public static final Atom XA_WM_ICON_NAME = new Atom(37L);
    public static final Atom XA_WM_ICON_SIZE = new Atom(38L);
    public static final Atom XA_WM_NAME = new Atom(39L);
    public static final Atom XA_WM_NORMAL_HINTS = new Atom(40L);
    public static final Atom XA_WM_SIZE_HINTS = new Atom(41L);
    public static final Atom XA_WM_ZOOM_HINTS = new Atom(42L);
    public static final Atom XA_MIN_SPACE = new Atom(43L);
    public static final Atom XA_NORM_SPACE = new Atom(44L);
    public static final Atom XA_MAX_SPACE = new Atom(45L);
    public static final Atom XA_END_SPACE = new Atom(46L);
    public static final Atom XA_SUPERSCRIPT_X = new Atom(47L);
    public static final Atom XA_SUPERSCRIPT_Y = new Atom(48L);
    public static final Atom XA_SUBSCRIPT_X = new Atom(49L);
    public static final Atom XA_SUBSCRIPT_Y = new Atom(50L);
    public static final Atom XA_UNDERLINE_POSITION = new Atom(51L);
    public static final Atom XA_UNDERLINE_THICKNESS = new Atom(52L);
    public static final Atom XA_STRIKEOUT_ASCENT = new Atom(53L);
    public static final Atom XA_STRIKEOUT_DESCENT = new Atom(54L);
    public static final Atom XA_ITALIC_ANGLE = new Atom(55L);
    public static final Atom XA_X_HEIGHT = new Atom(56L);
    public static final Atom XA_QUAD_WIDTH = new Atom(57L);
    public static final Atom XA_WEIGHT = new Atom(58L);
    public static final Atom XA_POINT_SIZE = new Atom(59L);
    public static final Atom XA_RESOLUTION = new Atom(60L);
    public static final Atom XA_COPYRIGHT = new Atom(61L);
    public static final Atom XA_NOTICE = new Atom(62L);
    public static final Atom XA_FONT_NAME = new Atom(63L);
    public static final Atom XA_FAMILY_NAME = new Atom(64L);
    public static final Atom XA_FULL_NAME = new Atom(65L);
    public static final Atom XA_CAP_HEIGHT = new Atom(66L);
    public static final Atom XA_WM_CLASS = new Atom(67L);
    public static final Atom XA_WM_TRANSIENT_FOR;
    public static final Atom XA_LAST_PREDEFINED;
    public static final int None = 0;
    public static final int ParentRelative = 1;
    public static final int CopyFromParent = 0;
    public static final int PointerWindow = 0;
    public static final int InputFocus = 1;
    public static final int PointerRoot = 1;
    public static final int AnyPropertyType = 0;
    public static final int AnyKey = 0;
    public static final int AnyButton = 0;
    public static final int AllTemporary = 0;
    public static final int CurrentTime = 0;
    public static final int NoSymbol = 0;
    public static final int NoEventMask = 0;
    public static final int KeyPressMask = 1;
    public static final int KeyReleaseMask = 2;
    public static final int ButtonPressMask = 4;
    public static final int ButtonReleaseMask = 8;
    public static final int EnterWindowMask = 16;
    public static final int LeaveWindowMask = 32;
    public static final int PointerMotionMask = 64;
    public static final int PointerMotionHintMask = 128;
    public static final int Button1MotionMask = 256;
    public static final int Button2MotionMask = 512;
    public static final int Button3MotionMask = 1024;
    public static final int Button4MotionMask = 2048;
    public static final int Button5MotionMask = 4096;
    public static final int ButtonMotionMask = 8192;
    public static final int KeymapStateMask = 16384;
    public static final int ExposureMask = 32768;
    public static final int VisibilityChangeMask = 65536;
    public static final int StructureNotifyMask = 131072;
    public static final int ResizeRedirectMask = 262144;
    public static final int SubstructureNotifyMask = 524288;
    public static final int SubstructureRedirectMask = 0x100000;
    public static final int FocusChangeMask = 0x200000;
    public static final int PropertyChangeMask = 0x400000;
    public static final int ColormapChangeMask = 0x800000;
    public static final int OwnerGrabButtonMask = 0x1000000;
    public static final int KeyPress = 2;
    public static final int KeyRelease = 3;
    public static final int ButtonPress = 4;
    public static final int ButtonRelease = 5;
    public static final int MotionNotify = 6;
    public static final int EnterNotify = 7;
    public static final int LeaveNotify = 8;
    public static final int FocusIn = 9;
    public static final int FocusOut = 10;
    public static final int KeymapNotify = 11;
    public static final int Expose = 12;
    public static final int GraphicsExpose = 13;
    public static final int NoExpose = 14;
    public static final int VisibilityNotify = 15;
    public static final int CreateNotify = 16;
    public static final int DestroyNotify = 17;
    public static final int UnmapNotify = 18;
    public static final int MapNotify = 19;
    public static final int MapRequest = 20;
    public static final int ReparentNotify = 21;
    public static final int ConfigureNotify = 22;
    public static final int ConfigureRequest = 23;
    public static final int GravityNotify = 24;
    public static final int ResizeRequest = 25;
    public static final int CirculateNotify = 26;
    public static final int CirculateRequest = 27;
    public static final int PropertyNotify = 28;
    public static final int SelectionClear = 29;
    public static final int SelectionRequest = 30;
    public static final int SelectionNotify = 31;
    public static final int ColormapNotify = 32;
    public static final int ClientMessage = 33;
    public static final int MappingNotify = 34;
    public static final int LASTEvent = 35;
    public static final int ShiftMask = 1;
    public static final int LockMask = 2;
    public static final int ControlMask = 4;
    public static final int Mod1Mask = 8;
    public static final int Mod2Mask = 16;
    public static final int Mod3Mask = 32;
    public static final int Mod4Mask = 64;
    public static final int Mod5Mask = 128;
    public static final int ShiftMapIndex = 0;
    public static final int LockMapIndex = 1;
    public static final int ControlMapIndex = 2;
    public static final int Mod1MapIndex = 3;
    public static final int Mod2MapIndex = 4;
    public static final int Mod3MapIndex = 5;
    public static final int Mod4MapIndex = 6;
    public static final int Mod5MapIndex = 7;
    public static final int Button1Mask = 256;
    public static final int Button2Mask = 512;
    public static final int Button3Mask = 1024;
    public static final int Button4Mask = 2048;
    public static final int Button5Mask = 4096;
    public static final int AnyModifier = 32768;
    public static final int Button1 = 1;
    public static final int Button2 = 2;
    public static final int Button3 = 3;
    public static final int Button4 = 4;
    public static final int Button5 = 5;
    public static final int NotifyNormal = 0;
    public static final int NotifyGrab = 1;
    public static final int NotifyUngrab = 2;
    public static final int NotifyWhileGrabbed = 3;
    public static final int NotifyHint = 1;
    public static final int NotifyAncestor = 0;
    public static final int NotifyVirtual = 1;
    public static final int NotifyInferior = 2;
    public static final int NotifyNonlinear = 3;
    public static final int NotifyNonlinearVirtual = 4;
    public static final int NotifyPointer = 5;
    public static final int NotifyPointerRoot = 6;
    public static final int NotifyDetailNone = 7;
    public static final int VisibilityUnobscured = 0;
    public static final int VisibilityPartiallyObscured = 1;
    public static final int VisibilityFullyObscured = 2;
    public static final int PlaceOnTop = 0;
    public static final int PlaceOnBottom = 1;
    public static final int FamilyInternet = 0;
    public static final int FamilyDECnet = 1;
    public static final int FamilyChaos = 2;
    public static final int FamilyInternet6 = 6;
    public static final int FamilyServerInterpreted = 5;
    public static final int PropertyNewValue = 0;
    public static final int PropertyDelete = 1;
    public static final int ColormapUninstalled = 0;
    public static final int ColormapInstalled = 1;
    public static final int GrabModeSync = 0;
    public static final int GrabModeAsync = 1;
    public static final int GrabSuccess = 0;
    public static final int AlreadyGrabbed = 1;
    public static final int GrabInvalidTime = 2;
    public static final int GrabNotViewable = 3;
    public static final int GrabFrozen = 4;
    public static final int AsyncPointer = 0;
    public static final int SyncPointer = 1;
    public static final int ReplayPointer = 2;
    public static final int AsyncKeyboard = 3;
    public static final int SyncKeyboard = 4;
    public static final int ReplayKeyboard = 5;
    public static final int AsyncBoth = 6;
    public static final int SyncBoth = 7;
    public static final int RevertToNone = 0;
    public static final int RevertToPointerRoot = 1;
    public static final int RevertToParent = 2;
    public static final int Success = 0;
    public static final int BadRequest = 1;
    public static final int BadValue = 2;
    public static final int BadWindow = 3;
    public static final int BadPixmap = 4;
    public static final int BadAtom = 5;
    public static final int BadCursor = 6;
    public static final int BadFont = 7;
    public static final int BadMatch = 8;
    public static final int BadDrawable = 9;
    public static final int BadAccess = 10;
    public static final int BadAlloc = 11;
    public static final int BadColor = 12;
    public static final int BadGC = 13;
    public static final int BadIDChoice = 14;
    public static final int BadName = 15;
    public static final int BadLength = 16;
    public static final int BadImplementation = 17;
    public static final int FirstExtensionError = 128;
    public static final int LastExtensionError = 255;
    public static final int InputOutput = 1;
    public static final int InputOnly = 2;
    public static final int CWBackPixmap = 1;
    public static final int CWBackPixel = 2;
    public static final int CWBorderPixmap = 4;
    public static final int CWBorderPixel = 8;
    public static final int CWBitGravity = 16;
    public static final int CWWinGravity = 32;
    public static final int CWBackingStore = 64;
    public static final int CWBackingPlanes = 128;
    public static final int CWBackingPixel = 256;
    public static final int CWOverrideRedirect = 512;
    public static final int CWSaveUnder = 1024;
    public static final int CWEventMask = 2048;
    public static final int CWDontPropagate = 4096;
    public static final int CWColormap = 8192;
    public static final int CWCursor = 16384;
    public static final int CWX = 1;
    public static final int CWY = 2;
    public static final int CWWidth = 4;
    public static final int CWHeight = 8;
    public static final int CWBorderWidth = 16;
    public static final int CWSibling = 32;
    public static final int CWStackMode = 64;
    public static final int ForgetGravity = 0;
    public static final int NorthWestGravity = 1;
    public static final int NorthGravity = 2;
    public static final int NorthEastGravity = 3;
    public static final int WestGravity = 4;
    public static final int CenterGravity = 5;
    public static final int EastGravity = 6;
    public static final int SouthWestGravity = 7;
    public static final int SouthGravity = 8;
    public static final int SouthEastGravity = 9;
    public static final int StaticGravity = 10;
    public static final int UnmapGravity = 0;
    public static final int NotUseful = 0;
    public static final int WhenMapped = 1;
    public static final int Always = 2;
    public static final int IsUnmapped = 0;
    public static final int IsUnviewable = 1;
    public static final int IsViewable = 2;
    public static final int SetModeInsert = 0;
    public static final int SetModeDelete = 1;
    public static final int DestroyAll = 0;
    public static final int RetainPermanent = 1;
    public static final int RetainTemporary = 2;
    public static final int Above = 0;
    public static final int Below = 1;
    public static final int TopIf = 2;
    public static final int BottomIf = 3;
    public static final int Opposite = 4;
    public static final int RaiseLowest = 0;
    public static final int LowerHighest = 1;
    public static final int PropModeReplace = 0;
    public static final int PropModePrepend = 1;
    public static final int PropModeAppend = 2;
    public static final int GXclear = 0;
    public static final int GXand = 1;
    public static final int GXandReverse = 2;
    public static final int GXcopy = 3;
    public static final int GXandInverted = 4;
    public static final int GXnoop = 5;
    public static final int GXxor = 6;
    public static final int GXor = 7;
    public static final int GXnor = 8;
    public static final int GXequiv = 9;
    public static final int GXinvert = 10;
    public static final int GXorReverse = 11;
    public static final int GXcopyInverted = 12;
    public static final int GXorInverted = 13;
    public static final int GXnand = 14;
    public static final int GXset = 15;
    public static final int LineSolid = 0;
    public static final int LineOnOffDash = 1;
    public static final int LineDoubleDash = 2;
    public static final int CapNotLast = 0;
    public static final int CapButt = 1;
    public static final int CapRound = 2;
    public static final int CapProjecting = 3;
    public static final int JoinMiter = 0;
    public static final int JoinRound = 1;
    public static final int JoinBevel = 2;
    public static final int FillSolid = 0;
    public static final int FillTiled = 1;
    public static final int FillStippled = 2;
    public static final int FillOpaqueStippled = 3;
    public static final int EvenOddRule = 0;
    public static final int WindingRule = 1;
    public static final int ClipByChildren = 0;
    public static final int IncludeInferiors = 1;
    public static final int Unsorted = 0;
    public static final int YSorted = 1;
    public static final int YXSorted = 2;
    public static final int YXBanded = 3;
    public static final int CoordModeOrigin = 0;
    public static final int CoordModePrevious = 1;
    public static final int Complex = 0;
    public static final int Nonconvex = 1;
    public static final int Convex = 2;
    public static final int ArcChord = 0;
    public static final int ArcPieSlice = 1;
    public static final int GCFunction = 1;
    public static final int GCPlaneMask = 2;
    public static final int GCForeground = 4;
    public static final int GCBackground = 8;
    public static final int GCLineWidth = 16;
    public static final int GCLineStyle = 32;
    public static final int GCCapStyle = 64;
    public static final int GCJoinStyle = 128;
    public static final int GCFillStyle = 256;
    public static final int GCFillRule = 512;
    public static final int GCTile = 1024;
    public static final int GCStipple = 2048;
    public static final int GCTileStipXOrigin = 4096;
    public static final int GCTileStipYOrigin = 8192;
    public static final int GCFont = 16384;
    public static final int GCSubwindowMode = 32768;
    public static final int GCGraphicsExposures = 65536;
    public static final int GCClipXOrigin = 131072;
    public static final int GCClipYOrigin = 262144;
    public static final int GCClipMask = 524288;
    public static final int GCDashOffset = 0x100000;
    public static final int GCDashList = 0x200000;
    public static final int GCArcMode = 0x400000;
    public static final int GCLastBit = 22;
    public static final int FontLeftToRight = 0;
    public static final int FontRightToLeft = 1;
    public static final int FontChange = 255;
    public static final int XYBitmap = 0;
    public static final int XYPixmap = 1;
    public static final int ZPixmap = 2;
    public static final int AllocNone = 0;
    public static final int AllocAll = 1;
    public static final int DoRed = 1;
    public static final int DoGreen = 2;
    public static final int DoBlue = 4;
    public static final int CursorShape = 0;
    public static final int TileShape = 1;
    public static final int StippleShape = 2;
    public static final int AutoRepeatModeOff = 0;
    public static final int AutoRepeatModeOn = 1;
    public static final int AutoRepeatModeDefault = 2;
    public static final int LedModeOff = 0;
    public static final int LedModeOn = 1;
    public static final int KBKeyClickPercent = 1;
    public static final int KBBellPercent = 2;
    public static final int KBBellPitch = 4;
    public static final int KBBellDuration = 8;
    public static final int KBLed = 16;
    public static final int KBLedMode = 32;
    public static final int KBKey = 64;
    public static final int KBAutoRepeatMode = 128;
    public static final int MappingSuccess = 0;
    public static final int MappingBusy = 1;
    public static final int MappingFailed = 2;
    public static final int MappingModifier = 0;
    public static final int MappingKeyboard = 1;
    public static final int MappingPointer = 2;
    public static final int DontPreferBlanking = 0;
    public static final int PreferBlanking = 1;
    public static final int DefaultBlanking = 2;
    public static final int DisableScreenSaver = 0;
    public static final int DisableScreenInterval = 0;
    public static final int DontAllowExposures = 0;
    public static final int AllowExposures = 1;
    public static final int DefaultExposures = 2;
    public static final int ScreenSaverReset = 0;
    public static final int ScreenSaverActive = 1;
    public static final int HostInsert = 0;
    public static final int HostDelete = 1;
    public static final int EnableAccess = 1;
    public static final int DisableAccess = 0;
    public static final int StaticGray = 0;
    public static final int GrayScale = 1;
    public static final int StaticColor = 2;
    public static final int PseudoColor = 3;
    public static final int TrueColor = 4;
    public static final int DirectColor = 5;
    public static final int LSBFirst = 0;
    public static final int MSBFirst = 1;

    public Display XOpenDisplay(String var1);

    public int XGetErrorText(Display var1, int var2, byte[] var3, int var4);

    public int XDefaultScreen(Display var1);

    public Screen DefaultScreenOfDisplay(Display var1);

    public Visual XDefaultVisual(Display var1, int var2);

    public Colormap XDefaultColormap(Display var1, int var2);

    public int XDisplayWidth(Display var1, int var2);

    public int XDisplayHeight(Display var1, int var2);

    public Window XDefaultRootWindow(Display var1);

    public Window XRootWindow(Display var1, int var2);

    public int XAllocNamedColor(Display var1, int var2, String var3, Pointer var4, Pointer var5);

    public XSizeHints XAllocSizeHints();

    public void XSetWMProperties(Display var1, Window var2, String var3, String var4, String[] var5, int var6, XSizeHints var7, Pointer var8, Pointer var9);

    public int XFree(Pointer var1);

    public Window XCreateSimpleWindow(Display var1, Window var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9);

    public Pixmap XCreateBitmapFromData(Display var1, Window var2, Pointer var3, int var4, int var5);

    public int XMapWindow(Display var1, Window var2);

    public int XMapRaised(Display var1, Window var2);

    public int XMapSubwindows(Display var1, Window var2);

    public int XFlush(Display var1);

    public int XSync(Display var1, boolean var2);

    public int XEventsQueued(Display var1, int var2);

    public int XPending(Display var1);

    public int XUnmapWindow(Display var1, Window var2);

    public int XDestroyWindow(Display var1, Window var2);

    public int XCloseDisplay(Display var1);

    public int XClearWindow(Display var1, Window var2);

    public int XClearArea(Display var1, Window var2, int var3, int var4, int var5, int var6, int var7);

    public Pixmap XCreatePixmap(Display var1, Drawable var2, int var3, int var4, int var5);

    public int XFreePixmap(Display var1, Pixmap var2);

    public GC XCreateGC(Display var1, Drawable var2, NativeLong var3, XGCValues var4);

    public int XSetFillRule(Display var1, GC var2, int var3);

    public int XFreeGC(Display var1, GC var2);

    public int XDrawPoint(Display var1, Drawable var2, GC var3, int var4, int var5);

    public int XDrawPoints(Display var1, Drawable var2, GC var3, XPoint[] var4, int var5, int var6);

    public int XFillRectangle(Display var1, Drawable var2, GC var3, int var4, int var5, int var6, int var7);

    public int XFillRectangles(Display var1, Drawable var2, GC var3, XRectangle[] var4, int var5);

    public int XSetForeground(Display var1, GC var2, NativeLong var3);

    public int XSetBackground(Display var1, GC var2, NativeLong var3);

    public int XFillArc(Display var1, Drawable var2, GC var3, int var4, int var5, int var6, int var7, int var8, int var9);

    public int XFillPolygon(Display var1, Drawable var2, GC var3, XPoint[] var4, int var5, int var6, int var7);

    public int XQueryTree(Display var1, Window var2, WindowByReference var3, WindowByReference var4, PointerByReference var5, IntByReference var6);

    public boolean XQueryPointer(Display var1, Window var2, WindowByReference var3, WindowByReference var4, IntByReference var5, IntByReference var6, IntByReference var7, IntByReference var8, IntByReference var9);

    public int XGetWindowAttributes(Display var1, Window var2, XWindowAttributes var3);

    public int XChangeWindowAttributes(Display var1, Window var2, NativeLong var3, XSetWindowAttributes var4);

    public int XGetGeometry(Display var1, Drawable var2, WindowByReference var3, IntByReference var4, IntByReference var5, IntByReference var6, IntByReference var7, IntByReference var8, IntByReference var9);

    public boolean XTranslateCoordinates(Display var1, Window var2, Window var3, int var4, int var5, IntByReference var6, IntByReference var7, WindowByReference var8);

    public int XSelectInput(Display var1, Window var2, NativeLong var3);

    public int XSendEvent(Display var1, Window var2, int var3, NativeLong var4, XEvent var5);

    public int XNextEvent(Display var1, XEvent var2);

    public int XPeekEvent(Display var1, XEvent var2);

    public int XWindowEvent(Display var1, Window var2, NativeLong var3, XEvent var4);

    public boolean XCheckWindowEvent(Display var1, Window var2, NativeLong var3, XEvent var4);

    public int XMaskEvent(Display var1, NativeLong var2, XEvent var3);

    public boolean XCheckMaskEvent(Display var1, NativeLong var2, XEvent var3);

    public boolean XCheckTypedEvent(Display var1, int var2, XEvent var3);

    public boolean XCheckTypedWindowEvent(Display var1, Window var2, int var3, XEvent var4);

    public XWMHints XGetWMHints(Display var1, Window var2);

    public int XGetWMName(Display var1, Window var2, XTextProperty var3);

    public XVisualInfo XGetVisualInfo(Display var1, NativeLong var2, XVisualInfo var3, IntByReference var4);

    public Colormap XCreateColormap(Display var1, Window var2, Visual var3, int var4);

    public int XGetWindowProperty(Display var1, Window var2, Atom var3, NativeLong var4, NativeLong var5, boolean var6, Atom var7, AtomByReference var8, IntByReference var9, NativeLongByReference var10, NativeLongByReference var11, PointerByReference var12);

    public int XChangeProperty(Display var1, Window var2, Atom var3, Atom var4, int var5, int var6, Pointer var7, int var8);

    public int XDeleteProperty(Display var1, Window var2, Atom var3);

    public Atom XInternAtom(Display var1, String var2, boolean var3);

    public String XGetAtomName(Display var1, Atom var2);

    public int XCopyArea(Display var1, Drawable var2, Drawable var3, GC var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public XImage XCreateImage(Display var1, Visual var2, int var3, int var4, int var5, Pointer var6, int var7, int var8, int var9, int var10);

    public int XPutImage(Display var1, Drawable var2, GC var3, XImage var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public int XDestroyImage(XImage var1);

    public XErrorHandler XSetErrorHandler(XErrorHandler var1);

    public String XKeysymToString(KeySym var1);

    public KeySym XStringToKeysym(String var1);

    public byte XKeysymToKeycode(Display var1, KeySym var2);

    public KeySym XKeycodeToKeysym(Display var1, byte var2, int var3);

    public int XGrabKey(Display var1, int var2, int var3, Window var4, int var5, int var6, int var7);

    public int XUngrabKey(Display var1, int var2, int var3, Window var4);

    public int XChangeKeyboardMapping(Display var1, int var2, int var3, KeySym[] var4, int var5);

    public KeySym XGetKeyboardMapping(Display var1, byte var2, int var3, IntByReference var4);

    public int XDisplayKeycodes(Display var1, IntByReference var2, IntByReference var3);

    public int XSetModifierMapping(Display var1, XModifierKeymapRef var2);

    public XModifierKeymapRef XGetModifierMapping(Display var1);

    public XModifierKeymapRef XNewModifiermap(int var1);

    public XModifierKeymapRef XInsertModifiermapEntry(XModifierKeymapRef var1, byte var2, int var3);

    public XModifierKeymapRef XDeleteModifiermapEntry(XModifierKeymapRef var1, byte var2, int var3);

    public int XFreeModifiermap(XModifierKeymapRef var1);

    public int XChangeKeyboardControl(Display var1, NativeLong var2, XKeyboardControlRef var3);

    public int XGetKeyboardControl(Display var1, XKeyboardStateRef var2);

    public int XAutoRepeatOn(Display var1);

    public int XAutoRepeatOff(Display var1);

    public int XBell(Display var1, int var2);

    public int XQueryKeymap(Display var1, byte[] var2);

    static {
        XA_LAST_PREDEFINED = XA_WM_TRANSIENT_FOR = new Atom(68L);
    }

    public static class XKeyboardStateRef
    extends Structure
    implements Structure.ByReference {
        public int key_click_percent;
        public int bell_percent;
        public int bell_pitch;
        public int bell_duration;
        public NativeLong led_mask;
        public int global_auto_repeat;
        public byte[] auto_repeats = new byte[32];

        public String toString() {
            return "XKeyboardStateByReference{key_click_percent=" + this.key_click_percent + ", bell_percent=" + this.bell_percent + ", bell_pitch=" + this.bell_pitch + ", bell_duration=" + this.bell_duration + ", led_mask=" + this.led_mask + ", global_auto_repeat=" + this.global_auto_repeat + ", auto_repeats=" + this.auto_repeats + '}';
        }
    }

    public static class XKeyboardControlRef
    extends Structure
    implements Structure.ByReference {
        public int key_click_percent;
        public int bell_percent;
        public int bell_pitch;
        public int bell_duration;
        public int led;
        public int led_mode;
        public int key;
        public int auto_repeat_mode;

        public String toString() {
            return "XKeyboardControlByReference{key_click_percent=" + this.key_click_percent + ", bell_percent=" + this.bell_percent + ", bell_pitch=" + this.bell_pitch + ", bell_duration=" + this.bell_duration + ", led=" + this.led + ", led_mode=" + this.led_mode + ", key=" + this.key + ", auto_repeat_mode=" + this.auto_repeat_mode + '}';
        }
    }

    public static class XModifierKeymapRef
    extends Structure
    implements Structure.ByReference {
        public int max_keypermod;
        public Pointer modifiermap;
    }

    public static interface XErrorHandler
    extends Callback {
        public int apply(Display var1, XErrorEvent var2);
    }

    public static class XKeymapEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public byte[] key_vector = new byte[32];
    }

    public static class XErrorEvent
    extends Structure {
        public int type;
        public Display display;
        public XID resourceid;
        public NativeLong serial;
        public byte error_code;
        public byte request_code;
        public byte minor_code;
    }

    public static class XMappingEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int request;
        public int first_keycode;
        public int count;
    }

    public static class XColormapEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Colormap colormap;
        public int c_new;
        public int state;
    }

    public static class XSelectionEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window requestor;
        public Atom selection;
        public Atom target;
        public Atom property;
        public NativeLong time;
    }

    public static class XSelectionRequestEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window owner;
        public Window requestor;
        public Atom selection;
        public Atom target;
        Atom property;
        public NativeLong time;
    }

    public static class XSelectionClearEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Atom selection;
        public NativeLong time;
    }

    public static class XPropertyEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Atom atom;
        public NativeLong time;
        public int state;
    }

    public static class XCirculateRequestEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
        public int place;
    }

    public static class XCirculateEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int place;
    }

    public static class XConfigureRequestEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public Window above;
        public int detail;
        public NativeLong value_mask;
    }

    public static class XResizeRequestEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int width;
        public int height;
    }

    public static class XGravityEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int x;
        public int y;
    }

    public static class XConfigureEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public Window above;
        public int override_redirect;
    }

    public static class XReparentEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public Window parent;
        public int x;
        public int y;
        public int override_redirect;
    }

    public static class XMapRequestEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
    }

    public static class XMapEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int override_redirect;
    }

    public static class XUnmapEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int from_configure;
    }

    public static class XDestroyWindowEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
    }

    public static class XCreateWindowEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public int override_redirect;
    }

    public static class XVisibilityEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int state;
    }

    public static class XNoExposeEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Drawable drawable;
        public int major_code;
        public int minor_code;
    }

    public static class XGraphicsExposeEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Drawable drawable;
        public int x;
        public int y;
        public int width;
        public int height;
        public int count;
        public int major_code;
        public int minor_code;
    }

    public static class XExposeEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int count;
    }

    public static class XFocusOutEvent
    extends XFocusChangeEvent {
    }

    public static class XFocusInEvent
    extends XFocusChangeEvent {
    }

    public static class XFocusChangeEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int mode;
        public int detail;
    }

    public static class XLeaveWindowEvent
    extends XCrossingEvent {
    }

    public static class XEnterWindowEvent
    extends XCrossingEvent {
    }

    public static class XCrossingEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int mode;
        public int detail;
        public int same_screen;
        public int focus;
        public int state;
    }

    public static class XPointerMovedEvent
    extends XMotionEvent {
    }

    public static class XMotionEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int state;
        public byte is_hint;
        public int same_screen;
    }

    public static class XClientMessageEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Atom message_type;
        public int format;
        public Data data;

        public static class Data
        extends Union {
            public byte[] b = new byte[20];
            public short[] s = new short[10];
            public NativeLong[] l = new NativeLong[5];
        }
    }

    public static class XButtonReleasedEvent
    extends XButtonEvent {
    }

    public static class XButtonPressedEvent
    extends XButtonEvent {
    }

    public static class XButtonEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int state;
        public int button;
        public int same_screen;
    }

    public static class XKeyEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int state;
        public int keycode;
        public int same_screen;
    }

    public static class XAnyEvent
    extends Structure {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
    }

    public static class XEvent
    extends Union {
        public int type;
        public XAnyEvent xany;
        public XKeyEvent xkey;
        public XButtonEvent xbutton;
        public XMotionEvent xmotion;
        public XCrossingEvent xcrossing;
        public XFocusChangeEvent xfocus;
        public XExposeEvent xexpose;
        public XGraphicsExposeEvent xgraphicsexpose;
        public XNoExposeEvent xnoexpose;
        public XVisibilityEvent xvisibility;
        public XCreateWindowEvent xcreatewindow;
        public XDestroyWindowEvent xdestroywindow;
        public XUnmapEvent xunmap;
        public XMapEvent xmap;
        public XMapRequestEvent xmaprequest;
        public XReparentEvent xreparent;
        public XConfigureEvent xconfigure;
        public XGravityEvent xgravity;
        public XResizeRequestEvent xresizerequest;
        public XConfigureRequestEvent xconfigurerequest;
        public XCirculateEvent xcirculate;
        public XCirculateRequestEvent xcirculaterequest;
        public XPropertyEvent xproperty;
        public XSelectionClearEvent xselectionclear;
        public XSelectionRequestEvent xselectionrequest;
        public XSelectionEvent xselection;
        public XColormapEvent xcolormap;
        public XClientMessageEvent xclient;
        public XMappingEvent xmapping;
        public XErrorEvent xerror;
        public XKeymapEvent xkeymap;
        public NativeLong[] pad = new NativeLong[24];
    }

    public static class XGCValues
    extends Structure {
        public int function;
        public NativeLong plane_mask;
        public NativeLong foreground;
        public NativeLong background;
        public int line_width;
        public int line_style;
        public int cap_style;
        public int join_style;
        public int fill_style;
        public int fill_rule;
        public int arc_mode;
        public Pixmap tile;
        public Pixmap stipple;
        public int ts_x_origin;
        public int ts_y_origin;
        public Font font;
        public int subwindow_mode;
        public boolean graphics_exposures;
        public int clip_x_origin;
        public int clip_y_origin;
        public Pixmap clip_mask;
        public int dash_offset;
        public byte dashes;
    }

    public static class XRectangle
    extends Structure {
        public short x;
        public short y;
        public short width;
        public short height;

        public XRectangle() {
        }

        public XRectangle(short x, short y, short width, short height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public static class XPoint
    extends Structure {
        public short x;
        public short y;

        public XPoint() {
        }

        public XPoint(short x, short y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class XVisualInfo
    extends Structure {
        public Visual visual;
        public VisualID visualid;
        public int screen;
        public int depth;
        public int c_class;
        public NativeLong red_mask;
        public NativeLong green_mask;
        public NativeLong blue_mask;
        public int colormap_size;
        public int bits_per_rgb;
    }

    public static class XSetWindowAttributes
    extends Structure {
        public Pixmap background_pixmap;
        public NativeLong background_pixel;
        public Pixmap border_pixmap;
        public NativeLong border_pixel;
        public int bit_gravity;
        public int win_gravity;
        public int backing_store;
        public NativeLong backing_planes;
        public NativeLong backing_pixel;
        public boolean save_under;
        public NativeLong event_mask;
        public NativeLong do_not_propagate_mask;
        public boolean override_redirect;
        public Colormap colormap;
        public Cursor cursor;
    }

    public static class XWindowAttributes
    extends Structure {
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public int depth;
        public Visual visual;
        public Window root;
        public int c_class;
        public int bit_gravity;
        public int win_gravity;
        public int backing_store;
        public NativeLong backing_planes;
        public NativeLong backing_pixel;
        public boolean save_under;
        public Colormap colormap;
        public boolean map_installed;
        public int map_state;
        public NativeLong all_event_masks;
        public NativeLong your_event_mask;
        public NativeLong do_not_propagate_mask;
        public boolean override_redirect;
        public Screen screen;
    }

    public static class XSizeHints
    extends Structure {
        public NativeLong flags;
        public int x;
        public int y;
        public int width;
        public int height;
        public int min_width;
        public int min_height;
        public int max_width;
        public int max_height;
        public int width_inc;
        public int height_inc;
        public Aspect min_aspect;
        public Aspect max_aspect;
        public int base_width;
        public int base_height;
        public int win_gravity;

        public static class Aspect
        extends Structure {
            public int x;
            public int y;
        }
    }

    public static class XTextProperty
    extends Structure {
        public String value;
        public Atom encoding;
        public int format;
        public NativeLong nitems;
    }

    public static class XWMHints
    extends Structure {
        public NativeLong flags;
        public boolean input;
        public int initial_state;
        public Pixmap icon_pixmap;
        public Window icon_window;
        public int icon_x;
        public int icon_y;
        public Pixmap icon_mask;
        public XID window_group;
    }

    public static class XDeviceByReference
    extends Structure
    implements Structure.ByReference {
        public XID device_id;
        public int num_classes;
        public XInputClassInfoByReference classes;
    }

    public static class XInputClassInfoByReference
    extends Structure
    implements Structure.ByReference {
        public byte input_class;
        public byte event_type_base;
    }

    public static interface XTest
    extends Library {
        public static final XTest INSTANCE = Native.loadLibrary("Xtst", XTest.class);

        public boolean XTestQueryExtension(Display var1, IntByReference var2, IntByReference var3, IntByReference var4, IntByReference var5);

        public boolean XTestCompareCursorWithWindow(Display var1, Window var2, Cursor var3);

        public boolean XTestCompareCurrentCursorWithWindow(Display var1, Window var2);

        public int XTestFakeKeyEvent(Display var1, int var2, boolean var3, NativeLong var4);

        public int XTestFakeButtonEvent(Display var1, int var2, boolean var3, NativeLong var4);

        public int XTestFakeMotionEvent(Display var1, int var2, int var3, int var4, NativeLong var5);

        public int XTestFakeRelativeMotionEvent(Display var1, int var2, int var3, NativeLong var4);

        public int XTestFakeDeviceKeyEvent(Display var1, XDeviceByReference var2, int var3, boolean var4, IntByReference var5, int var6, NativeLong var7);

        public int XTestFakeDeviceButtonEvent(Display var1, XDeviceByReference var2, int var3, boolean var4, IntByReference var5, int var6, NativeLong var7);

        public int XTestFakeProximityEvent(Display var1, XDeviceByReference var2, boolean var3, IntByReference var4, int var5, NativeLong var6);

        public int XTestFakeDeviceMotionEvent(Display var1, XDeviceByReference var2, boolean var3, int var4, IntByReference var5, int var6, NativeLong var7);

        public int XTestGrabControl(Display var1, boolean var2);

        public void XTestSetVisualIDOfVisual(Visual var1, VisualID var2);

        public int XTestDiscard(Display var1);
    }

    public static interface Xevie
    extends Library {
        public static final Xevie INSTANCE = Native.loadLibrary("Xevie", Xevie.class);
        public static final int XEVIE_UNMODIFIED = 0;
        public static final int XEVIE_MODIFIED = 1;

        public boolean XevieQueryVersion(Display var1, IntByReference var2, IntByReference var3);

        public int XevieStart(Display var1);

        public int XevieEnd(Display var1);

        public int XevieSendEvent(Display var1, XEvent var2, int var3);

        public int XevieSelectInput(Display var1, NativeLong var2);
    }

    public static interface Xrender
    extends Library {
        public static final Xrender INSTANCE = Native.loadLibrary("Xrender", Xrender.class);
        public static final int PictTypeIndexed = 0;
        public static final int PictTypeDirect = 1;

        public XRenderPictFormat XRenderFindVisualFormat(Display var1, Visual var2);

        public static class XRenderPictFormat
        extends Structure {
            public PictFormat id;
            public int type;
            public int depth;
            public XRenderDirectFormat direct;
            public Colormap colormap;
        }

        public static class PictFormat
        extends NativeLong {
            private static final long serialVersionUID = 1L;

            public PictFormat(long value) {
                super(value);
            }

            public PictFormat() {
            }
        }

        public static class XRenderDirectFormat
        extends Structure {
            public short red;
            public short redMask;
            public short green;
            public short greenMask;
            public short blue;
            public short blueMask;
            public short alpha;
            public short alphaMask;
        }
    }

    public static interface Xext
    extends Library {
        public static final Xext INSTANCE = Native.loadLibrary("Xext", Xext.class);
        public static final int ShapeBounding = 0;
        public static final int ShapeClip = 1;
        public static final int ShapeInput = 2;
        public static final int ShapeSet = 0;
        public static final int ShapeUnion = 1;
        public static final int ShapeIntersect = 2;
        public static final int ShapeSubtract = 3;
        public static final int ShapeInvert = 4;

        public void XShapeCombineMask(Display var1, Window var2, int var3, int var4, int var5, Pixmap var6, int var7);
    }

    public static class XImage
    extends PointerType {
    }

    public static class GC
    extends PointerType {
    }

    public static class Screen
    extends PointerType {
    }

    public static class Visual
    extends PointerType {
        public NativeLong getVisualID() {
            if (this.getPointer() != null) {
                return this.getPointer().getNativeLong(Native.POINTER_SIZE);
            }
            return new NativeLong(0L);
        }

        public String toString() {
            return "Visual: VisualID=0x" + Long.toHexString(this.getVisualID().longValue());
        }
    }

    public static class Display
    extends PointerType {
    }

    public static class Pixmap
    extends Drawable {
        private static final long serialVersionUID = 1L;
        public static final Pixmap None = null;

        public Pixmap() {
        }

        public Pixmap(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new Pixmap(((Number)nativeValue).longValue());
        }
    }

    public static class WindowByReference
    extends ByReference {
        public WindowByReference() {
            super(XID.SIZE);
        }

        public Window getValue() {
            NativeLong value = this.getPointer().getNativeLong(0L);
            return value.longValue() == 0L ? Window.None : new Window(value.longValue());
        }
    }

    public static class Window
    extends Drawable {
        private static final long serialVersionUID = 1L;
        public static final Window None = null;

        public Window() {
        }

        public Window(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new Window(((Number)nativeValue).longValue());
        }
    }

    public static class Drawable
    extends XID {
        private static final long serialVersionUID = 1L;
        public static final Drawable None = null;

        public Drawable() {
        }

        public Drawable(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new Drawable(((Number)nativeValue).longValue());
        }
    }

    public static class KeySym
    extends XID {
        private static final long serialVersionUID = 1L;
        public static final KeySym None = null;

        public KeySym() {
        }

        public KeySym(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new KeySym(((Number)nativeValue).longValue());
        }
    }

    public static class Cursor
    extends XID {
        private static final long serialVersionUID = 1L;
        public static final Cursor None = null;

        public Cursor() {
        }

        public Cursor(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new Cursor(((Number)nativeValue).longValue());
        }
    }

    public static class Font
    extends XID {
        private static final long serialVersionUID = 1L;
        public static final Font None = null;

        public Font() {
        }

        public Font(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new Font(((Number)nativeValue).longValue());
        }
    }

    public static class Colormap
    extends XID {
        private static final long serialVersionUID = 1L;
        public static final Colormap None = null;

        public Colormap() {
        }

        public Colormap(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new Colormap(((Number)nativeValue).longValue());
        }
    }

    public static class AtomByReference
    extends ByReference {
        public AtomByReference() {
            super(XID.SIZE);
        }

        public Atom getValue() {
            NativeLong value = this.getPointer().getNativeLong(0L);
            return (Atom)new Atom().fromNative(value, null);
        }
    }

    public static class Atom
    extends XID {
        private static final long serialVersionUID = 1L;
        public static final Atom None = null;

        public Atom() {
        }

        public Atom(long id) {
            super(id);
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            long value = ((Number)nativeValue).longValue();
            if (value <= Integer.MAX_VALUE) {
                switch ((int)value) {
                    case 0: {
                        return None;
                    }
                    case 1: {
                        return XA_PRIMARY;
                    }
                    case 2: {
                        return XA_SECONDARY;
                    }
                    case 3: {
                        return XA_ARC;
                    }
                    case 4: {
                        return XA_ATOM;
                    }
                    case 5: {
                        return XA_BITMAP;
                    }
                    case 6: {
                        return XA_CARDINAL;
                    }
                    case 7: {
                        return XA_COLORMAP;
                    }
                    case 8: {
                        return XA_CURSOR;
                    }
                    case 9: {
                        return XA_CUT_BUFFER0;
                    }
                    case 10: {
                        return XA_CUT_BUFFER1;
                    }
                    case 11: {
                        return XA_CUT_BUFFER2;
                    }
                    case 12: {
                        return XA_CUT_BUFFER3;
                    }
                    case 13: {
                        return XA_CUT_BUFFER4;
                    }
                    case 14: {
                        return XA_CUT_BUFFER5;
                    }
                    case 15: {
                        return XA_CUT_BUFFER6;
                    }
                    case 16: {
                        return XA_CUT_BUFFER7;
                    }
                    case 17: {
                        return XA_DRAWABLE;
                    }
                    case 18: {
                        return XA_FONT;
                    }
                    case 19: {
                        return XA_INTEGER;
                    }
                    case 20: {
                        return XA_PIXMAP;
                    }
                    case 21: {
                        return XA_POINT;
                    }
                    case 22: {
                        return XA_RECTANGLE;
                    }
                    case 23: {
                        return XA_RESOURCE_MANAGER;
                    }
                    case 24: {
                        return XA_RGB_COLOR_MAP;
                    }
                    case 25: {
                        return XA_RGB_BEST_MAP;
                    }
                    case 26: {
                        return XA_RGB_BLUE_MAP;
                    }
                    case 27: {
                        return XA_RGB_DEFAULT_MAP;
                    }
                    case 28: {
                        return XA_RGB_GRAY_MAP;
                    }
                    case 29: {
                        return XA_RGB_GREEN_MAP;
                    }
                    case 30: {
                        return XA_RGB_RED_MAP;
                    }
                    case 31: {
                        return XA_STRING;
                    }
                    case 32: {
                        return XA_VISUALID;
                    }
                    case 33: {
                        return XA_WINDOW;
                    }
                    case 34: {
                        return XA_WM_COMMAND;
                    }
                    case 35: {
                        return XA_WM_HINTS;
                    }
                    case 36: {
                        return XA_WM_CLIENT_MACHINE;
                    }
                    case 37: {
                        return XA_WM_ICON_NAME;
                    }
                    case 38: {
                        return XA_WM_ICON_SIZE;
                    }
                    case 39: {
                        return XA_WM_NAME;
                    }
                    case 40: {
                        return XA_WM_NORMAL_HINTS;
                    }
                    case 41: {
                        return XA_WM_SIZE_HINTS;
                    }
                    case 42: {
                        return XA_WM_ZOOM_HINTS;
                    }
                    case 43: {
                        return XA_MIN_SPACE;
                    }
                    case 44: {
                        return XA_NORM_SPACE;
                    }
                    case 45: {
                        return XA_MAX_SPACE;
                    }
                    case 46: {
                        return XA_END_SPACE;
                    }
                    case 47: {
                        return XA_SUPERSCRIPT_X;
                    }
                    case 48: {
                        return XA_SUPERSCRIPT_Y;
                    }
                    case 49: {
                        return XA_SUBSCRIPT_X;
                    }
                    case 50: {
                        return XA_SUBSCRIPT_Y;
                    }
                    case 51: {
                        return XA_UNDERLINE_POSITION;
                    }
                    case 52: {
                        return XA_UNDERLINE_THICKNESS;
                    }
                    case 53: {
                        return XA_STRIKEOUT_ASCENT;
                    }
                    case 54: {
                        return XA_STRIKEOUT_DESCENT;
                    }
                    case 55: {
                        return XA_ITALIC_ANGLE;
                    }
                    case 56: {
                        return XA_X_HEIGHT;
                    }
                    case 57: {
                        return XA_QUAD_WIDTH;
                    }
                    case 58: {
                        return XA_WEIGHT;
                    }
                    case 59: {
                        return XA_POINT_SIZE;
                    }
                    case 60: {
                        return XA_RESOLUTION;
                    }
                    case 61: {
                        return XA_COPYRIGHT;
                    }
                    case 62: {
                        return XA_NOTICE;
                    }
                    case 63: {
                        return XA_FONT_NAME;
                    }
                    case 64: {
                        return XA_FAMILY_NAME;
                    }
                    case 65: {
                        return XA_FULL_NAME;
                    }
                    case 66: {
                        return XA_CAP_HEIGHT;
                    }
                    case 67: {
                        return XA_WM_CLASS;
                    }
                    case 68: {
                        return XA_WM_TRANSIENT_FOR;
                    }
                }
            }
            return new Atom(value);
        }
    }

    public static class XID
    extends NativeLong {
        private static final long serialVersionUID = 1L;
        public static final XID None = null;

        public XID() {
            this(0L);
        }

        public XID(long id) {
            super(id);
        }

        protected boolean isNone(Object o) {
            return o == null || o instanceof Number && ((Number)o).longValue() == 0L;
        }

        public Object fromNative(Object nativeValue, FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return None;
            }
            return new XID(((Number)nativeValue).longValue());
        }

        public String toString() {
            return "0x" + Long.toHexString(this.longValue());
        }
    }

    public static class VisualID
    extends NativeLong {
        private static final long serialVersionUID = 1L;

        public VisualID() {
        }

        public VisualID(long value) {
            super(value);
        }
    }
}


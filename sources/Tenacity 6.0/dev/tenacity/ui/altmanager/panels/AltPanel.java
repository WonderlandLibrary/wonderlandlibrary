// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager.panels;

import org.lwjgl.input.Keyboard;
import dev.tenacity.utils.server.ban.HypixelBan;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.objects.DoubleIconButton;
import java.awt.Color;
import dev.tenacity.ui.Screen;
import org.lwjgl.input.Mouse;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import dev.tenacity.utils.misc.HoveringUtil;
import java.util.Iterator;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.ui.altmanager.helpers.AltManagerUtils;
import dev.tenacity.ui.altmanager.helpers.Alt;
import dev.tenacity.utils.misc.IOUtils;
import net.minecraft.client.gui.GuiScreen;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.objects.Scroll;
import java.util.concurrent.CopyOnWriteArrayList;
import dev.tenacity.ui.altmanager.Panel;

public class AltPanel extends Panel
{
    private final CopyOnWriteArrayList<AltRect> altRects;
    private final CopyOnWriteArrayList<AltRect> visibleAltRects;
    private final Scroll scroll;
    private static boolean canDrag;
    private boolean altsSelected;
    private static boolean select;
    private static AltRect shiftClickStart;
    private static AltRect firstClickAlt;
    public static AltRect loadingAltRect;
    private static Pair<AltRect, AltRect> shiftClickRange;
    private static final TimerUtil doubleClickTimer;
    private boolean hoveringScrollBar;
    private boolean draggingScrollBar;
    public static boolean refreshing;
    private boolean needsScrollBar;
    private static AltRect hoveringAlt;
    
    public AltPanel() {
        this.altRects = new CopyOnWriteArrayList<AltRect>();
        this.visibleAltRects = new CopyOnWriteArrayList<AltRect>();
        this.scroll = new Scroll();
        this.altsSelected = false;
        this.hoveringScrollBar = false;
        this.draggingScrollBar = false;
        this.needsScrollBar = false;
    }
    
    @Override
    public void initGui() {
        this.refreshAlts();
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (Tenacity.INSTANCE.getAltManager().isTyping()) {
            return;
        }
        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            this.altsSelected = !this.altsSelected;
            this.visibleAltRects.forEach(altRect -> altRect.setSelected(this.altsSelected));
            return;
        }
        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            final String[] split2;
            final String[] lines = split2 = IOUtils.getClipboardString().split("\n");
            for (final String line : split2) {
                final String[] split = line.split(":");
                if (split.length == 2) {
                    final Alt alt = new Alt(split[0], split[1]);
                    AltManagerUtils.getAlts().add(alt);
                }
            }
            this.refreshAlts();
            return;
        }
        if (!this.altsSelected) {
            return;
        }
        if (GuiScreen.isKeyComboCtrlC(keyCode)) {
            final StringBuilder stringBuilder = new StringBuilder();
            int count = 0;
            for (final AltRect altRect2 : this.visibleAltRects) {
                if (!altRect2.isSelected()) {
                    continue;
                }
                final Alt alt2 = altRect2.alt;
                if (alt2.email == null) {
                    continue;
                }
                stringBuilder.append(alt2.email).append(":").append(alt2.password).append("\n");
                ++count;
            }
            NotificationManager.post(NotificationType.SUCCESS, "Success", "Copied " + count + " alts to clipboard");
            IOUtils.copy(stringBuilder.toString());
            return;
        }
        if (keyCode == 211) {
            int count2 = 0;
            for (final AltRect altRect3 : this.visibleAltRects) {
                if (!altRect3.isSelected()) {
                    continue;
                }
                final Alt alt3 = altRect3.alt;
                AltManagerUtils.removeAlt(alt3);
                ++count2;
            }
            AltManagerUtils.writeAlts();
            this.refreshAlts();
            NotificationManager.post(NotificationType.SUCCESS, "Success", "Removed " + count2 + " alts");
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: getstatic       dev/tenacity/ui/altmanager/panels/AltPanel.mc:Lnet/minecraft/client/Minecraft;
        //     7: invokespecial   net/minecraft/client/gui/ScaledResolution.<init>:(Lnet/minecraft/client/Minecraft;)V
        //    10: astore_3        /* sr */
        //    11: aload_0         /* this */
        //    12: aload_3         /* sr */
        //    13: invokevirtual   net/minecraft/client/gui/ScaledResolution.getScaledWidth:()I
        //    16: i2f            
        //    17: aload_0         /* this */
        //    18: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //    21: ldc             16.0
        //    23: fadd           
        //    24: fsub           
        //    25: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.setWidth:(F)V
        //    28: aload_0         /* this */
        //    29: aload_3         /* sr */
        //    30: invokevirtual   net/minecraft/client/gui/ScaledResolution.getScaledHeight:()I
        //    33: bipush          91
        //    35: isub           
        //    36: i2f            
        //    37: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.setHeight:(F)V
        //    40: aload_0         /* this */
        //    41: ldc             75.0
        //    43: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.setY:(F)V
        //    46: aload_0         /* this */
        //    47: iload_1         /* mouseX */
        //    48: iload_2         /* mouseY */
        //    49: invokespecial   dev/tenacity/ui/altmanager/Panel.drawScreen:(II)V
        //    52: aload_0         /* this */
        //    53: invokespecial   dev/tenacity/ui/altmanager/panels/AltPanel.performShiftClick:()V
        //    56: getstatic       dev/tenacity/Tenacity.INSTANCE:Ldev/tenacity/Tenacity;
        //    59: invokevirtual   dev/tenacity/Tenacity.getAltManager:()Ldev/tenacity/ui/altmanager/GuiAltManager;
        //    62: getfield        dev/tenacity/ui/altmanager/GuiAltManager.searchField:Ldev/tenacity/utils/objects/TextField;
        //    65: astore          searchField
        //    67: getstatic       dev/tenacity/Tenacity.INSTANCE:Ldev/tenacity/Tenacity;
        //    70: invokevirtual   dev/tenacity/Tenacity.getAltManager:()Ldev/tenacity/ui/altmanager/GuiAltManager;
        //    73: getfield        dev/tenacity/ui/altmanager/GuiAltManager.filterBanned:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //    76: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //    79: istore          filterBanned
        //    81: aload           searchField
        //    83: invokevirtual   dev/tenacity/utils/objects/TextField.isFocused:()Z
        //    86: ifne            103
        //    89: aload           searchField
        //    91: invokevirtual   dev/tenacity/utils/objects/TextField.getText:()Ljava/lang/String;
        //    94: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    97: invokevirtual   java/lang/String.isEmpty:()Z
        //   100: ifne            107
        //   103: iconst_1       
        //   104: goto            108
        //   107: iconst_0       
        //   108: istore          searching
        //   110: aload           searchField
        //   112: invokevirtual   dev/tenacity/utils/objects/TextField.getText:()Ljava/lang/String;
        //   115: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   118: astore          text
        //   120: aload_0         /* this */
        //   121: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.visibleAltRects:Ljava/util/concurrent/CopyOnWriteArrayList;
        //   124: invokevirtual   java/util/concurrent/CopyOnWriteArrayList.clear:()V
        //   127: aload_0         /* this */
        //   128: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.altRects:Ljava/util/concurrent/CopyOnWriteArrayList;
        //   131: invokevirtual   java/util/concurrent/CopyOnWriteArrayList.stream:()Ljava/util/stream/Stream;
        //   134: iload           filterBanned
        //   136: iload           searching
        //   138: aload           text
        //   140: invokedynamic   BootstrapMethod #1, test:(ZZLjava/lang/String;)Ljava/util/function/Predicate;
        //   145: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
        //   150: aload_0         /* this */
        //   151: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.visibleAltRects:Ljava/util/concurrent/CopyOnWriteArrayList;
        //   154: dup            
        //   155: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   158: pop            
        //   159: invokedynamic   BootstrapMethod #2, accept:(Ljava/util/concurrent/CopyOnWriteArrayList;)Ljava/util/function/Consumer;
        //   164: invokeinterface java/util/stream/Stream.forEach:(Ljava/util/function/Consumer;)V
        //   169: aload_0         /* this */
        //   170: aload_0         /* this */
        //   171: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.visibleAltRects:Ljava/util/concurrent/CopyOnWriteArrayList;
        //   174: invokevirtual   java/util/concurrent/CopyOnWriteArrayList.stream:()Ljava/util/stream/Stream;
        //   177: invokedynamic   BootstrapMethod #3, test:()Ljava/util/function/Predicate;
        //   182: invokeinterface java/util/stream/Stream.anyMatch:(Ljava/util/function/Predicate;)Z
        //   187: putfield        dev/tenacity/ui/altmanager/panels/AltPanel.altsSelected:Z
        //   190: getstatic       dev/tenacity/ui/altmanager/panels/AltPanel.refreshing:Z
        //   193: ifeq            197
        //   196: return         
        //   197: aload_0         /* this */
        //   198: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //   201: aload_0         /* this */
        //   202: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   205: aload_0         /* this */
        //   206: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getWidth:()F
        //   209: aload_0         /* this */
        //   210: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getHeight:()F
        //   213: iload_1         /* mouseX */
        //   214: iload_2         /* mouseY */
        //   215: invokestatic    dev/tenacity/utils/misc/HoveringUtil.isHovering:(FFFFII)Z
        //   218: ifeq            230
        //   221: aload_0         /* this */
        //   222: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   225: bipush          35
        //   227: invokevirtual   dev/tenacity/utils/objects/Scroll.onScroll:(I)V
        //   230: aload_0         /* this */
        //   231: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getWidth:()F
        //   234: ldc             30.0
        //   236: fsub           
        //   237: fstore          testWidth
        //   239: sipush          150
        //   242: istore          minWidth
        //   244: bipush          10
        //   246: istore          startX
        //   248: iconst_m1      
        //   249: istore          altRowCount
        //   251: iload           startX
        //   253: istore          i
        //   255: iload           i
        //   257: i2f            
        //   258: fload           testWidth
        //   260: fcmpg          
        //   261: ifgt            277
        //   264: iinc            altRowCount, 1
        //   267: iload           i
        //   269: iload           minWidth
        //   271: iadd           
        //   272: istore          i
        //   274: goto            255
        //   277: fload           testWidth
        //   279: iload           altRowCount
        //   281: iload           minWidth
        //   283: bipush          10
        //   285: iadd           
        //   286: imul           
        //   287: i2f            
        //   288: fsub           
        //   289: fstore          spaceLeft
        //   291: iload           minWidth
        //   293: i2f            
        //   294: fload           spaceLeft
        //   296: iload           altRowCount
        //   298: i2f            
        //   299: fdiv           
        //   300: fadd           
        //   301: fstore          altRectWidth
        //   303: ldc             40.0
        //   305: fstore          altRectHeight
        //   307: aload_0         /* this */
        //   308: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //   311: ldc             10.0
        //   313: fadd           
        //   314: fstore          altX
        //   316: aload_0         /* this */
        //   317: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   320: ldc             10.0
        //   322: fadd           
        //   323: fstore          altY
        //   325: aload_0         /* this */
        //   326: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.needsScrollBar:Z
        //   329: ifeq            522
        //   332: aload_0         /* this */
        //   333: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getHeight:()F
        //   336: ldc             10.0
        //   338: fsub           
        //   339: fstore          scrollHeight
        //   341: aload_0         /* this */
        //   342: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //   345: aload_0         /* this */
        //   346: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getWidth:()F
        //   349: fadd           
        //   350: ldc             15.0
        //   352: fsub           
        //   353: fstore          scrollX
        //   355: ldc             7.0
        //   357: fstore          scrollWidth
        //   359: fload           scrollX
        //   361: aload_0         /* this */
        //   362: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   365: ldc             5.0
        //   367: fadd           
        //   368: fload           scrollWidth
        //   370: fload           scrollHeight
        //   372: ldc             3.5
        //   374: bipush          37
        //   376: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(I)Ljava/awt/Color;
        //   379: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   382: aload_0         /* this */
        //   383: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   386: invokevirtual   dev/tenacity/utils/objects/Scroll.getScroll:()F
        //   389: aload_0         /* this */
        //   390: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   393: invokevirtual   dev/tenacity/utils/objects/Scroll.getMaxScroll:()F
        //   396: fdiv           
        //   397: fstore          percent
        //   399: aload_0         /* this */
        //   400: fload           scrollX
        //   402: aload_0         /* this */
        //   403: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   406: ldc             5.0
        //   408: fadd           
        //   409: fload           scrollHeight
        //   411: ldc             60.0
        //   413: fsub           
        //   414: fload           percent
        //   416: fmul           
        //   417: fsub           
        //   418: fload           scrollWidth
        //   420: ldc             60.0
        //   422: iload_1         /* mouseX */
        //   423: iload_2         /* mouseY */
        //   424: invokestatic    dev/tenacity/utils/misc/HoveringUtil.isHovering:(FFFFII)Z
        //   427: putfield        dev/tenacity/ui/altmanager/panels/AltPanel.hoveringScrollBar:Z
        //   430: aload_0         /* this */
        //   431: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.draggingScrollBar:Z
        //   434: ifeq            478
        //   437: fconst_1       
        //   438: fconst_0       
        //   439: iload_2         /* mouseY */
        //   440: i2f            
        //   441: aload_0         /* this */
        //   442: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   445: ldc             5.0
        //   447: fadd           
        //   448: fsub           
        //   449: fload           scrollHeight
        //   451: fdiv           
        //   452: invokestatic    java/lang/Math.max:(FF)F
        //   455: invokestatic    java/lang/Math.min:(FF)F
        //   458: fstore          dragPercent
        //   460: aload_0         /* this */
        //   461: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   464: aload_0         /* this */
        //   465: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   468: invokevirtual   dev/tenacity/utils/objects/Scroll.getMaxScroll:()F
        //   471: fneg           
        //   472: fload           dragPercent
        //   474: fmul           
        //   475: invokevirtual   dev/tenacity/utils/objects/Scroll.setRawScroll:(F)V
        //   478: fload           scrollX
        //   480: aload_0         /* this */
        //   481: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   484: ldc             5.0
        //   486: fadd           
        //   487: fload           scrollHeight
        //   489: ldc             60.0
        //   491: fsub           
        //   492: fload           percent
        //   494: fmul           
        //   495: fsub           
        //   496: fload           scrollWidth
        //   498: ldc             60.0
        //   500: ldc             3.5
        //   502: aload_0         /* this */
        //   503: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.hoveringScrollBar:Z
        //   506: ifeq            514
        //   509: bipush          65
        //   511: goto            516
        //   514: bipush          55
        //   516: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(I)Ljava/awt/Color;
        //   519: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   522: invokestatic    dev/tenacity/utils/render/StencilUtil.initStencilToWrite:()V
        //   525: aload_0         /* this */
        //   526: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //   529: aload_0         /* this */
        //   530: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   533: aload_0         /* this */
        //   534: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getWidth:()F
        //   537: aload_0         /* this */
        //   538: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getHeight:()F
        //   541: ldc             6.0
        //   543: getstatic       java/awt/Color.BLACK:Ljava/awt/Color;
        //   546: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   549: iconst_1       
        //   550: invokestatic    dev/tenacity/utils/render/StencilUtil.readStencilBuffer:(I)V
        //   553: aload_0         /* this */
        //   554: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.visibleAltRects:Ljava/util/concurrent/CopyOnWriteArrayList;
        //   557: invokevirtual   java/util/concurrent/CopyOnWriteArrayList.iterator:()Ljava/util/Iterator;
        //   560: astore          17
        //   562: aload           17
        //   564: invokeinterface java/util/Iterator.hasNext:()Z
        //   569: ifeq            741
        //   572: aload           17
        //   574: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   579: checkcast       Ldev/tenacity/ui/altmanager/panels/AltPanel$AltRect;
        //   582: astore          altRect
        //   584: aload           altRect
        //   586: fload           altRectWidth
        //   588: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.setWidth:(F)V
        //   591: aload           altRect
        //   593: fload           altRectHeight
        //   595: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.setHeight:(F)V
        //   598: fload           altX
        //   600: fload           altRectWidth
        //   602: fadd           
        //   603: aload_0         /* this */
        //   604: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //   607: aload_0         /* this */
        //   608: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getWidth:()F
        //   611: ldc             10.0
        //   613: fsub           
        //   614: fadd           
        //   615: fcmpl          
        //   616: ifle            641
        //   619: aload_0         /* this */
        //   620: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getX:()F
        //   623: ldc             10.0
        //   625: fadd           
        //   626: fstore          altX
        //   628: fload           altY
        //   630: aload           altRect
        //   632: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.getHeight:()F
        //   635: ldc             10.0
        //   637: fadd           
        //   638: fadd           
        //   639: fstore          altY
        //   641: aload           altRect
        //   643: fload           altX
        //   645: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.setX:(F)V
        //   648: aload           altRect
        //   650: fload           altY
        //   652: f2d            
        //   653: aload_0         /* this */
        //   654: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   657: invokevirtual   dev/tenacity/utils/objects/Scroll.getScroll:()F
        //   660: f2d            
        //   661: invokestatic    dev/tenacity/utils/misc/MathUtils.roundToHalf:(D)D
        //   664: dadd           
        //   665: d2f            
        //   666: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.setY:(F)V
        //   669: aload           altRect
        //   671: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.getY:()F
        //   674: aload           altRect
        //   676: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.getHeight:()F
        //   679: fadd           
        //   680: aload_0         /* this */
        //   681: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   684: fcmpl          
        //   685: ifle            722
        //   688: aload           altRect
        //   690: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.getY:()F
        //   693: aload_0         /* this */
        //   694: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   697: aload_0         /* this */
        //   698: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getHeight:()F
        //   701: fadd           
        //   702: fcmpg          
        //   703: ifge            722
        //   706: aload           altRect
        //   708: iconst_1       
        //   709: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.setClickable:(Z)V
        //   712: aload           altRect
        //   714: iload_1         /* mouseX */
        //   715: iload_2         /* mouseY */
        //   716: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.drawScreen:(II)V
        //   719: goto            728
        //   722: aload           altRect
        //   724: iconst_0       
        //   725: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel$AltRect.setClickable:(Z)V
        //   728: fload           altX
        //   730: fload           altRectWidth
        //   732: ldc             10.0
        //   734: fadd           
        //   735: fadd           
        //   736: fstore          altX
        //   738: goto            562
        //   741: aload_0         /* this */
        //   742: getfield        dev/tenacity/ui/altmanager/panels/AltPanel.scroll:Ldev/tenacity/utils/objects/Scroll;
        //   745: fconst_0       
        //   746: fload           altY
        //   748: aload_0         /* this */
        //   749: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   752: fsub           
        //   753: aload_0         /* this */
        //   754: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getHeight:()F
        //   757: fsub           
        //   758: fload           altRectHeight
        //   760: fadd           
        //   761: ldc             10.0
        //   763: fadd           
        //   764: invokestatic    java/lang/Math.max:(FF)F
        //   767: invokevirtual   dev/tenacity/utils/objects/Scroll.setMaxScroll:(F)V
        //   770: aload_0         /* this */
        //   771: fload           altY
        //   773: fload           altRectHeight
        //   775: fadd           
        //   776: aload_0         /* this */
        //   777: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getY:()F
        //   780: aload_0         /* this */
        //   781: invokevirtual   dev/tenacity/ui/altmanager/panels/AltPanel.getHeight:()F
        //   784: fadd           
        //   785: fcmpl          
        //   786: ifle            793
        //   789: iconst_1       
        //   790: goto            794
        //   793: iconst_0       
        //   794: putfield        dev/tenacity/ui/altmanager/panels/AltPanel.needsScrollBar:Z
        //   797: invokestatic    dev/tenacity/utils/render/StencilUtil.uninitStencilBuffer:()V
        //   800: return         
        //    StackMapTable: 00 12 FE 00 67 07 00 F9 07 00 FA 01 03 40 01 FD 00 58 01 07 00 D9 20 FF 00 18 00 0D 07 00 D7 01 01 07 00 F9 07 00 FA 01 01 07 00 D9 02 01 01 01 01 00 00 FA 00 15 FF 00 C8 00 15 07 00 D7 01 01 07 00 F9 07 00 FA 01 01 07 00 D9 02 01 01 01 02 02 02 02 02 02 02 02 02 00 00 FF 00 23 00 15 07 00 D7 01 01 07 00 F9 07 00 FA 01 01 07 00 D9 02 01 01 01 02 02 02 02 02 02 02 02 02 00 05 02 02 02 02 02 FF 00 01 00 15 07 00 D7 01 01 07 00 F9 07 00 FA 01 01 07 00 D9 02 01 01 01 02 02 02 02 02 02 02 02 02 00 06 02 02 02 02 02 01 FF 00 05 00 11 07 00 D7 01 01 07 00 F9 07 00 FA 01 01 07 00 D9 02 01 01 01 02 02 02 02 02 00 00 FC 00 27 07 00 DB FC 00 4E 07 00 DC FB 00 50 05 F9 00 0C 73 07 00 D7 FF 00 00 00 11 07 00 D7 01 01 07 00 F9 07 00 FA 01 01 07 00 D9 02 01 01 01 02 02 02 02 02 00 02 07 00 D7 01
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (!HoveringUtil.isHovering(this.getX(), this.getY(), this.getWidth(), this.getHeight(), mouseX, mouseY) && this.altsSelected) {
            this.visibleAltRects.forEach(altRect -> altRect.setSelected(false));
            this.altsSelected = false;
            return;
        }
        if (this.hoveringScrollBar) {
            this.draggingScrollBar = true;
        }
        this.visibleAltRects.forEach(altRect -> altRect.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.draggingScrollBar = false;
    }
    
    public void refreshAlts() {
        AltPanel.refreshing = true;
        this.altRects.clear();
        this.visibleAltRects.clear();
        for (final Alt alt : AltManagerUtils.getAlts()) {
            this.altRects.add(new AltRect(alt));
        }
        Collections.reverse(this.altRects);
        this.altRects.sort(Comparator.comparing(altRect -> altRect.getAlt().favorite).reversed());
        AltPanel.refreshing = false;
    }
    
    private static void dragSelection(final AltRect altRect, final boolean hovering) {
        final boolean dragging = Mouse.isButtonDown(0) && AltPanel.canDrag;
        if (!Mouse.isButtonDown(0)) {
            AltPanel.canDrag = false;
        }
        if (dragging && AltPanel.hoveringAlt != altRect && hovering) {
            altRect.selected = !altRect.selected;
            AltPanel.hoveringAlt = altRect;
        }
    }
    
    private void performShiftClick() {
        if (AltPanel.shiftClickRange != null) {
            final AltRect start = AltPanel.shiftClickRange.getFirst();
            final AltRect end = AltPanel.shiftClickRange.getSecond();
            for (int startIndex = this.visibleAltRects.indexOf(start), endIndex = this.visibleAltRects.indexOf(end), i = Math.min(startIndex, endIndex); i <= Math.max(startIndex, endIndex); ++i) {
                if (i != -1) {
                    this.visibleAltRects.get(i).setSelected(AltPanel.select);
                }
            }
            AltPanel.shiftClickRange = null;
        }
    }
    
    static {
        AltPanel.canDrag = false;
        AltPanel.select = false;
        doubleClickTimer = new TimerUtil();
        AltPanel.refreshing = false;
    }
    
    public static class AltRect implements Screen
    {
        private float x;
        private float y;
        private float width;
        private float height;
        private Color backgroundColor;
        private boolean selected;
        private boolean currentAccount;
        private boolean hovering;
        private boolean clickable;
        private boolean removeShit;
        private Alt alt;
        private final DoubleIconButton favoriteButton;
        private final Animation hoverAnimation;
        private final Animation selectAnimation;
        private boolean hoveringCreds;
        private boolean showCreds;
        private Animation credsAnimation;
        
        public AltRect(final Alt alt) {
            this.backgroundColor = ColorUtil.tripleColor(37);
            this.clickable = true;
            this.favoriteButton = new DoubleIconButton("F", "G");
            this.hoverAnimation = new DecelerateAnimation(250, 1.0);
            this.selectAnimation = new DecelerateAnimation(100, 1.0);
            this.hoveringCreds = false;
            this.showCreds = false;
            this.credsAnimation = new DecelerateAnimation(200, 1.0);
            this.alt = alt;
            if (alt != null) {
                this.favoriteButton.setEnabled(alt.favorite);
            }
        }
        
        @Override
        public void initGui() {
        }
        
        @Override
        public void keyTyped(final char typedChar, final int keyCode) {
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY) {
            this.hovering = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
            this.hoverAnimation.setDirection(this.hovering ? Direction.FORWARDS : Direction.BACKWARDS);
            this.hoverAnimation.setDuration(this.hovering ? 200 : 350);
            dragSelection(this, this.hovering);
            final Color rectColor = this.backgroundColor;
            this.selectAnimation.setDirection(this.selected ? Direction.FORWARDS : Direction.BACKWARDS);
            if (this.selected || !this.selectAnimation.isDone()) {
                final float outlineWidth = this.selectAnimation.getOutput().floatValue();
                final Color outlineColor = ColorUtil.interpolateColorC(rectColor.brighter().brighter().brighter(), rectColor.brighter().brighter().brighter(), this.hoverAnimation.getOutput().floatValue());
                RoundedUtil.drawRound(this.x - outlineWidth, this.y - outlineWidth, this.width + outlineWidth * 2.0f, this.height + outlineWidth * 2.0f, 6.0f, outlineColor);
            }
            RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 5.0f, ColorUtil.interpolateColorC(rectColor, rectColor.brighter(), this.hoverAnimation.getOutput().floatValue()));
            final float credsAnim = this.credsAnimation.getOutput().floatValue();
            final float altSize = this.height - 10.0f;
            this.drawAltHead(this.x + 3.0f, this.y + this.height / 2.0f - altSize / 2.0f, altSize);
            RenderUtil.scaleStart(this.x + this.width / 2.0f, this.y + this.height / 2.0f, 1.0f - 0.5f * credsAnim);
            if (!this.removeShit) {
                this.favoriteButton.setX(this.x + this.width - (this.favoriteButton.getWidth() + 5.0f));
                this.favoriteButton.setY(this.y + 6.0f);
                this.favoriteButton.setAccentColor(new Color(255, 186, 0));
                final float hoverShow = this.favoriteButton.isEnabled() ? 1.0f : this.favoriteButton.getHoverAnimation().getOutput().floatValue();
                this.favoriteButton.setAlpha((1.0f - credsAnim) * hoverShow);
                this.favoriteButton.drawScreen(mouseX, mouseY);
            }
            final int textColor = ColorUtil.applyOpacity(-1, 1.0f - credsAnim);
            String usernameText = "No username";
            if (this.alt.username != null) {
                usernameText = AltRect.tenacityBoldFont16.trimStringToWidth(this.alt.username, (int)(this.width - (altSize + 10.0f)));
            }
            float totalHeight = (float)(AltRect.tenacityBoldFont16.getHeight() + 6 + AltRect.tenacityFont14.getHeight());
            boolean hasBan = false;
            if (this.alt.hypixelBan != null) {
                final HypixelBan ban = this.alt.hypixelBan;
                final long diff = ban.getUnbanDate() - System.currentTimeMillis();
                if (diff > 0L || ban.getUnbanDate() == 0L) {
                    hasBan = true;
                    totalHeight += AltRect.tenacityBoldFont14.getHeight() + 4;
                }
            }
            float usernameY = this.y + this.getHeight() / 2.0f - totalHeight / 2.0f;
            RenderUtil.resetColor();
            AltRect.tenacityBoldFont16.drawString(usernameText, this.x + 3.0f + altSize + 3.5f, usernameY, textColor);
            final float typeX = this.x + 7.0f + altSize + AltRect.tenacityBoldFont16.getStringWidth(usernameText);
            final float size = (float)(AltRect.tenacityBoldFont16.getHeight() + 2);
            final String type = this.alt.getType();
            switch (type) {
                case "Microsoft": {
                    RenderUtil.drawMicrosoftLogo(typeX + 2.0f, usernameY - 1.0f, size, 1.0f, 1.0f - credsAnim);
                    break;
                }
                case "Mojang": {
                    RenderUtil.resetColor();
                    GLUtil.startBlend();
                    RenderUtil.color(-1, 1.0f - credsAnim);
                    AltRect.mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/mojang.png"));
                    Gui.drawModalRectWithCustomSizedTexture(typeX + 2.0f, usernameY - 1.0f, 0.0f, 0.0f, size, size, size, size);
                    RenderUtil.resetColor();
                    break;
                }
                case "Cracked": {
                    final Color red = Tenacity.INSTANCE.getSideGui().getRedBadColor();
                    AltRect.tenacityBoldFont14.drawString(" Cracked", typeX, usernameY + 1.0f, ColorUtil.applyOpacity(red, 1.0f - credsAnim));
                    break;
                }
                case "Not logged in": {
                    AltRect.tenacityBoldFont14.drawString("§bNot logged in", typeX, usernameY + 1.0f, ColorUtil.applyOpacity(-1, 1.0f - credsAnim));
                    break;
                }
            }
            this.drawBan(this.x + altSize + 6.5f, usernameY + AltRect.tenacityBoldFont16.getHeight() + 6.0f, 1.0f - credsAnim);
            if (hasBan) {
                usernameY += AltRect.tenacityBoldFont14.getHeight() + 6;
            }
            RenderUtil.scaleEnd();
            final String credsText = (this.showCreds ? "Hide" : "Show") + " credentials";
            this.hoveringCreds = HoveringUtil.isHovering(this.x + 3.0f + altSize + 1.5f, usernameY + AltRect.tenacityBoldFont16.getHeight() + 6.0f - 2.0f, AltRect.tenacityFont14.getStringWidth("§n" + credsText) + 4.0f, (float)(AltRect.tenacityFont14.getHeight() + 4), mouseX, mouseY);
            final float opacity = this.showCreds ? (this.hoveringCreds ? 0.75f : 0.5f) : (this.hoveringCreds ? 0.5f : 0.35f);
            AltRect.tenacityFont14.drawString("§n" + credsText, this.x + 3.0f + altSize + 3.5f, usernameY + AltRect.tenacityBoldFont16.getHeight() + 6.0f, ColorUtil.applyOpacity(-1, opacity));
            this.credsAnimation.setDirection(this.showCreds ? Direction.FORWARDS : Direction.BACKWARDS);
            if (this.showCreds || !this.credsAnimation.isDone()) {
                float yVal = this.y + 6.0f;
                final float xVal = this.x + 6.0f + altSize;
                RenderUtil.scaleStart(this.x + this.width / 2.0f, this.y + this.height / 2.0f, 1.3f - 0.3f * credsAnim);
                if (this.alt.email != null) {
                    AltRect.tenacityFont14.drawString("Email: " + this.alt.email, xVal, yVal, ColorUtil.applyOpacity(-1, credsAnim));
                    yVal += AltRect.tenacityFont14.getHeight() + 4;
                }
                AltRect.tenacityFont14.drawString("Password: " + this.alt.password, xVal, yVal, ColorUtil.applyOpacity(-1, credsAnim));
                RenderUtil.scaleEnd();
            }
            if (this.removeShit) {
                return;
            }
            if (Tenacity.INSTANCE.getAltManager().currentSessionAlt == this.alt) {
                final Color green = Tenacity.INSTANCE.getSideGui().getGreenEnabledColor();
                AltRect.tenacityBoldFont14.drawString("Logged in", this.x + this.width - (AltRect.tenacityBoldFont14.getStringWidth("Logged in") + 5.0f), this.y + this.height - (AltRect.tenacityBoldFont14.getHeight() + 5), green);
            }
            else if (AltPanel.loadingAltRect == this) {
                final float iconWidth = AltRect.iconFont16.getStringWidth("D");
                final float iconHeight = (float)AltRect.iconFont16.getHeight();
                final float iconX = this.x + this.width - (iconWidth + 5.0f);
                final float iconY = this.y + this.height - (iconHeight + 5.0f);
                RenderUtil.rotateStartReal(iconX + iconWidth / 2.0f, iconY + iconHeight / 2.0f - 1.0f, iconWidth, iconHeight, System.currentTimeMillis() % 1080L / 3.0f);
                AltRect.iconFont16.drawString("D", iconX, iconY, ColorUtil.applyOpacity(-1, 0.5f));
                RenderUtil.rotateEnd();
            }
            else {
                final Alt.AltState altState = this.alt.altState;
                if (altState == null) {
                    return;
                }
                final String text = altState.getIcon();
                AltRect.iconFont16.drawString(text, this.x + this.width - (AltRect.iconFont16.getStringWidth(text) + 5.0f), this.y + this.height - (AltRect.iconFont16.getHeight() + 5), ColorUtil.applyOpacity(-1, 0.5f));
            }
        }
        
        @Override
        public void mouseClicked(final int mouseX, final int mouseY, final int button) {
            if (this.clickable) {
                if (!this.showCreds && !this.removeShit) {
                    if (this.alt == null) {
                        return;
                    }
                    this.favoriteButton.mouseClicked(mouseX, mouseY, button);
                    this.alt.favorite = this.favoriteButton.isEnabled();
                }
                if (this.hoveringCreds) {
                    this.showCreds = !this.showCreds;
                    return;
                }
                if (!this.favoriteButton.isHovering() && this.hovering && button == 0) {
                    if (!AltPanel.doubleClickTimer.hasTimeElapsed(400L) && AltPanel.firstClickAlt == this) {
                        AltPanel.firstClickAlt.getAlt().loginAsync();
                        AltPanel.loadingAltRect = this;
                        AltPanel.firstClickAlt = null;
                        return;
                    }
                    AltPanel.firstClickAlt = this;
                    AltPanel.doubleClickTimer.reset();
                    this.selected = !this.selected;
                    AltPanel.canDrag = true;
                    AltPanel.hoveringAlt = this;
                    if (Keyboard.isKeyDown(42) && AltPanel.shiftClickStart != null) {
                        AltPanel.shiftClickRange = Pair.of(AltPanel.shiftClickStart, this);
                    }
                    AltPanel.select = this.selected;
                    AltPanel.shiftClickStart = ((AltPanel.shiftClickStart == this) ? null : this);
                }
            }
        }
        
        @Override
        public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        }
        
        public void drawAltHead(final float x, final float y, final float size) {
            Tenacity.INSTANCE.getAltManager().getUtils().getHead(this.alt);
            GLUtil.startBlend();
            RenderUtil.setAlphaLimit(0.0f);
            AltRect.mc.getTextureManager().bindTexture((this.alt.head != null) ? this.alt.head : new ResourceLocation("Tenacity/X-Steve.png"));
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, size, size, size, size);
            GLUtil.endBlend();
        }
        
        public void drawBan(final float x, final float y, final float alpha) {
            if (this.alt.hypixelBan != null) {
                final HypixelBan ban = this.alt.hypixelBan;
                final long diff = ban.getUnbanDate() - System.currentTimeMillis();
                if (diff > 0L) {
                    final long diffSeconds = diff / 1000L % 60L;
                    final long diffMinutes = diff / 60000L % 60L;
                    final long diffHours = diff / 3600000L % 24L;
                    final long diffDays = diff / 86400000L;
                    String str = diffSeconds + "s";
                    if (diffMinutes > 0L) {
                        str = diffMinutes + "m " + str;
                    }
                    if (diffHours > 0L) {
                        str = diffHours + "h " + str;
                    }
                    if (diffDays > 0L) {
                        str = diffDays + "d " + str;
                    }
                    AltRect.tenacityFont14.drawString("§lBanned §ron Hypixel for §l" + str, x, y, ColorUtil.applyOpacity(-1, alpha * 0.5f));
                }
                else if (ban.getUnbanDate() == 0L) {
                    AltRect.tenacityFont14.drawString("§lBanned §ron Hypixel permanently", x, y, ColorUtil.applyOpacity(-1, alpha * 0.5f));
                }
            }
        }
        
        public float getX() {
            return this.x;
        }
        
        public float getY() {
            return this.y;
        }
        
        public float getWidth() {
            return this.width;
        }
        
        public float getHeight() {
            return this.height;
        }
        
        public Color getBackgroundColor() {
            return this.backgroundColor;
        }
        
        public boolean isSelected() {
            return this.selected;
        }
        
        public boolean isCurrentAccount() {
            return this.currentAccount;
        }
        
        public boolean isHovering() {
            return this.hovering;
        }
        
        public boolean isClickable() {
            return this.clickable;
        }
        
        public boolean isRemoveShit() {
            return this.removeShit;
        }
        
        public Alt getAlt() {
            return this.alt;
        }
        
        public DoubleIconButton getFavoriteButton() {
            return this.favoriteButton;
        }
        
        public Animation getHoverAnimation() {
            return this.hoverAnimation;
        }
        
        public Animation getSelectAnimation() {
            return this.selectAnimation;
        }
        
        public boolean isHoveringCreds() {
            return this.hoveringCreds;
        }
        
        public boolean isShowCreds() {
            return this.showCreds;
        }
        
        public Animation getCredsAnimation() {
            return this.credsAnimation;
        }
        
        public void setX(final float x) {
            this.x = x;
        }
        
        public void setY(final float y) {
            this.y = y;
        }
        
        public void setWidth(final float width) {
            this.width = width;
        }
        
        public void setHeight(final float height) {
            this.height = height;
        }
        
        public void setBackgroundColor(final Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
        
        public void setSelected(final boolean selected) {
            this.selected = selected;
        }
        
        public void setCurrentAccount(final boolean currentAccount) {
            this.currentAccount = currentAccount;
        }
        
        public void setHovering(final boolean hovering) {
            this.hovering = hovering;
        }
        
        public void setClickable(final boolean clickable) {
            this.clickable = clickable;
        }
        
        public void setRemoveShit(final boolean removeShit) {
            this.removeShit = removeShit;
        }
        
        public void setAlt(final Alt alt) {
            this.alt = alt;
        }
        
        public void setHoveringCreds(final boolean hoveringCreds) {
            this.hoveringCreds = hoveringCreds;
        }
        
        public void setShowCreds(final boolean showCreds) {
            this.showCreds = showCreds;
        }
        
        public void setCredsAnimation(final Animation credsAnimation) {
            this.credsAnimation = credsAnimation;
        }
    }
}

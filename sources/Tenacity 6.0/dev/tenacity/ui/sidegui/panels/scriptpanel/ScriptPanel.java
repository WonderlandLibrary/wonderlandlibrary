// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.scriptpanel;

import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.intent.cloud.data.Votes;
import java.util.Comparator;
import java.util.Iterator;
import dev.tenacity.scripting.api.Script;
import dev.tenacity.intent.cloud.data.CloudScript;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.misc.Multithreading;
import java.util.ArrayList;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import dev.tenacity.utils.objects.Scroll;
import java.util.List;
import dev.tenacity.ui.sidegui.utils.ToggleButton;
import dev.tenacity.ui.sidegui.utils.CarouselButtons;
import dev.tenacity.ui.sidegui.utils.DropdownObject;
import dev.tenacity.ui.sidegui.panels.Panel;

public class ScriptPanel extends Panel
{
    private final DropdownObject sorting;
    private final CarouselButtons carouselButtons;
    private final ToggleButton scriptFilter;
    private final ToggleButton compactMode;
    private final List<CloudScriptRect> cloudScriptRects;
    private final List<LocalScriptRect> localScriptRects;
    private final Scroll cloudScriptScroll;
    private final Scroll localScriptScroll;
    private final List<ActionButton> actionButtons;
    private boolean refresh;
    private boolean firstRefresh;
    private String sortingSelection;
    private final TimerUtil voteRefreshTimer;
    
    public ScriptPanel() {
        this.sorting = new DropdownObject("Sort by", new String[] { "Relevance", "Alphabetical", "Top all time", "Recently updated" });
        this.carouselButtons = new CarouselButtons(new String[] { "Cloud", "Local" });
        this.scriptFilter = new ToggleButton("Only show scripts made by you");
        this.compactMode = new ToggleButton("Compact Mode");
        this.cloudScriptRects = new ArrayList<CloudScriptRect>();
        this.localScriptRects = new ArrayList<LocalScriptRect>();
        this.cloudScriptScroll = new Scroll();
        this.localScriptScroll = new Scroll();
        this.firstRefresh = true;
        this.sortingSelection = "Relevance";
        this.voteRefreshTimer = new TimerUtil();
        (this.actionButtons = new ArrayList<ActionButton>()).add(new ActionButton("Open documentation"));
        this.actionButtons.add(new ActionButton("Open folder"));
        Multithreading.runAsync(() -> {
            this.refresh();
            this.firstRefresh = false;
        });
    }
    
    @Override
    public void initGui() {
        this.sortingSelection = this.sorting.getSelection();
        this.sortScripts();
        this.cloudScriptScroll.setRawScroll(0.0f);
        this.localScriptScroll.setRawScroll(0.0f);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //     4: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getCurrentButton:()Ljava/lang/String;
        //     7: ldc             "Cloud"
        //     9: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    12: istore_3        /* isCloud */
        //    13: iload_3         /* isCloud */
        //    14: ifeq            45
        //    17: aload_0         /* this */
        //    18: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.voteRefreshTimer:Ldev/tenacity/utils/time/TimerUtil;
        //    21: ldc2_w          30000
        //    24: invokevirtual   dev/tenacity/utils/time/TimerUtil.hasTimeElapsed:(J)Z
        //    27: ifeq            45
        //    30: invokedynamic   BootstrapMethod #1, run:()Ljava/lang/Runnable;
        //    35: invokestatic    dev/tenacity/utils/misc/Multithreading.runAsync:(Ljava/lang/Runnable;)V
        //    38: aload_0         /* this */
        //    39: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.voteRefreshTimer:Ldev/tenacity/utils/time/TimerUtil;
        //    42: invokevirtual   dev/tenacity/utils/time/TimerUtil.reset:()V
        //    45: getstatic       dev/tenacity/Tenacity.INSTANCE:Ldev/tenacity/Tenacity;
        //    48: invokevirtual   dev/tenacity/Tenacity.getCloudDataManager:()Ldev/tenacity/intent/cloud/CloudDataManager;
        //    51: invokevirtual   dev/tenacity/intent/cloud/CloudDataManager.applyVotes:()V
        //    54: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.tenacityBoldFont40:Ldev/tenacity/utils/font/CustomFont;
        //    57: ldc             "Scripts"
        //    59: aload_0         /* this */
        //    60: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //    63: ldc             8.0
        //    65: fadd           
        //    66: aload_0         /* this */
        //    67: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getY:()F
        //    70: ldc             8.0
        //    72: fadd           
        //    73: aload_0         /* this */
        //    74: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getTextColor:()Ljava/awt/Color;
        //    77: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //    80: aload_0         /* this */
        //    81: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //    84: aload_0         /* this */
        //    85: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //    88: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setAlpha:(F)V
        //    91: aload_0         /* this */
        //    92: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //    95: ldc             50.0
        //    97: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setRectWidth:(F)V
        //   100: aload_0         /* this */
        //   101: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   104: ldc             18.0
        //   106: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setRectHeight:(F)V
        //   109: aload_0         /* this */
        //   110: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   113: bipush          55
        //   115: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(I)Ljava/awt/Color;
        //   118: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setBackgroundColor:(Ljava/awt/Color;)V
        //   121: aload_0         /* this */
        //   122: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   125: aload_0         /* this */
        //   126: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getY:()F
        //   129: ldc             72.0
        //   131: fadd           
        //   132: aload_0         /* this */
        //   133: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   136: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //   139: fsub           
        //   140: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setY:(F)V
        //   143: aload_0         /* this */
        //   144: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   147: aload_0         /* this */
        //   148: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   151: aload_0         /* this */
        //   152: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getWidth:()F
        //   155: fconst_2       
        //   156: fdiv           
        //   157: fadd           
        //   158: aload_0         /* this */
        //   159: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   162: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getTotalWidth:()F
        //   165: fconst_2       
        //   166: fdiv           
        //   167: fsub           
        //   168: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setX:(F)V
        //   171: aload_0         /* this */
        //   172: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   175: iload_1         /* mouseX */
        //   176: iload_2         /* mouseY */
        //   177: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.drawScreen:(II)V
        //   180: ldc             8.0
        //   182: fstore          spacing
        //   184: aload_0         /* this */
        //   185: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   188: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getY:()F
        //   191: aload_0         /* this */
        //   192: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   195: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //   198: fadd           
        //   199: fload           spacing
        //   201: fadd           
        //   202: fstore          backgroundY
        //   204: aload_0         /* this */
        //   205: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   208: fload           spacing
        //   210: fadd           
        //   211: fstore          backgroundX
        //   213: aload_0         /* this */
        //   214: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getWidth:()F
        //   217: fload           spacing
        //   219: fconst_2       
        //   220: fmul           
        //   221: fsub           
        //   222: fstore          backgroundWidth
        //   224: aload_0         /* this */
        //   225: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getHeight:()F
        //   228: fload           backgroundY
        //   230: aload_0         /* this */
        //   231: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getY:()F
        //   234: fsub           
        //   235: fconst_1       
        //   236: fadd           
        //   237: fload           spacing
        //   239: fadd           
        //   240: fsub           
        //   241: fstore          backgroundHeight
        //   243: iconst_0       
        //   244: istore          additionalSeparation
        //   246: aload_0         /* this */
        //   247: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.actionButtons:Ljava/util/List;
        //   250: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   255: astore          10
        //   257: aload           10
        //   259: invokeinterface java/util/Iterator.hasNext:()Z
        //   264: ifeq            395
        //   267: aload           10
        //   269: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   274: checkcast       Ldev/tenacity/ui/sidegui/utils/ActionButton;
        //   277: astore          button
        //   279: aload           button
        //   281: aload_0         /* this */
        //   282: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   285: ldc             10.0
        //   287: fadd           
        //   288: iload           additionalSeparation
        //   290: i2f            
        //   291: fadd           
        //   292: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setX:(F)V
        //   295: aload           button
        //   297: iload           additionalSeparation
        //   299: ifne            307
        //   302: ldc             90.0
        //   304: goto            309
        //   307: ldc             70.0
        //   309: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setWidth:(F)V
        //   312: aload           button
        //   314: ldc             15.0
        //   316: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setHeight:(F)V
        //   319: aload           button
        //   321: aload_0         /* this */
        //   322: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   325: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getY:()F
        //   328: aload_0         /* this */
        //   329: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   332: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //   335: fconst_2       
        //   336: fdiv           
        //   337: fadd           
        //   338: aload           button
        //   340: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.getHeight:()F
        //   343: fconst_2       
        //   344: fdiv           
        //   345: fsub           
        //   346: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setY:(F)V
        //   349: aload           button
        //   351: aload_0         /* this */
        //   352: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //   355: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setAlpha:(F)V
        //   358: aload           button
        //   360: aload           button
        //   362: invokedynamic   BootstrapMethod #2, run:(Ldev/tenacity/ui/sidegui/utils/ActionButton;)Ljava/lang/Runnable;
        //   367: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setClickAction:(Ljava/lang/Runnable;)V
        //   370: aload           button
        //   372: iload_1         /* mouseX */
        //   373: iload_2         /* mouseY */
        //   374: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.drawScreen:(II)V
        //   377: iload           additionalSeparation
        //   379: i2f            
        //   380: aload           button
        //   382: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.getWidth:()F
        //   385: fload           spacing
        //   387: fadd           
        //   388: fadd           
        //   389: f2i            
        //   390: istore          additionalSeparation
        //   392: goto            257
        //   395: aload_0         /* this */
        //   396: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   399: fload           spacing
        //   401: fadd           
        //   402: fload           backgroundY
        //   404: fload           backgroundWidth
        //   406: fload           backgroundHeight
        //   408: iload_1         /* mouseX */
        //   409: iload_2         /* mouseY */
        //   410: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   413: istore          hovering
        //   415: iload           hovering
        //   417: ifeq            456
        //   420: aload_0         /* this */
        //   421: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   424: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getCurrentButton:()Ljava/lang/String;
        //   427: ldc             "Cloud"
        //   429: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   432: ifeq            447
        //   435: aload_0         /* this */
        //   436: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.cloudScriptScroll:Ldev/tenacity/utils/objects/Scroll;
        //   439: bipush          35
        //   441: invokevirtual   dev/tenacity/utils/objects/Scroll.onScroll:(I)V
        //   444: goto            456
        //   447: aload_0         /* this */
        //   448: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.localScriptScroll:Ldev/tenacity/utils/objects/Scroll;
        //   451: bipush          35
        //   453: invokevirtual   dev/tenacity/utils/objects/Scroll.onScroll:(I)V
        //   456: iload_3         /* isCloud */
        //   457: ifeq            527
        //   460: aload_0         /* this */
        //   461: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   464: aload_0         /* this */
        //   465: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   468: aload_0         /* this */
        //   469: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getWidth:()F
        //   472: fadd           
        //   473: aload_0         /* this */
        //   474: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   477: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.getWH:()F
        //   480: ldc             12.0
        //   482: fadd           
        //   483: fsub           
        //   484: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setX:(F)V
        //   487: aload_0         /* this */
        //   488: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   491: fload           backgroundY
        //   493: aload_0         /* this */
        //   494: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   497: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.getWH:()F
        //   500: fload           spacing
        //   502: fadd           
        //   503: fsub           
        //   504: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setY:(F)V
        //   507: aload_0         /* this */
        //   508: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   511: aload_0         /* this */
        //   512: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //   515: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setAlpha:(F)V
        //   518: aload_0         /* this */
        //   519: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   522: iload_1         /* mouseX */
        //   523: iload_2         /* mouseY */
        //   524: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.drawScreen:(II)V
        //   527: aload_0         /* this */
        //   528: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   531: aload_0         /* this */
        //   532: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   535: ldc             69.0
        //   537: fadd           
        //   538: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setX:(F)V
        //   541: aload_0         /* this */
        //   542: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   545: aload_0         /* this */
        //   546: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getY:()F
        //   549: ldc             33.0
        //   551: fadd           
        //   552: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setY:(F)V
        //   555: aload_0         /* this */
        //   556: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   559: aload_0         /* this */
        //   560: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //   563: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setAlpha:(F)V
        //   566: aload_0         /* this */
        //   567: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   570: iload_1         /* mouseX */
        //   571: iload_2         /* mouseY */
        //   572: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.drawScreen:(II)V
        //   575: fload           backgroundX
        //   577: fload           backgroundY
        //   579: fload           backgroundWidth
        //   581: fload           backgroundHeight
        //   583: ldc             5.0
        //   585: bipush          27
        //   587: aload_0         /* this */
        //   588: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //   591: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //   594: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   597: aload_0         /* this */
        //   598: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.firstRefresh:Z
        //   601: ifeq            605
        //   604: return         
        //   605: aload_0         /* this */
        //   606: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.refresh:Z
        //   609: ifeq            617
        //   612: aload_0         /* this */
        //   613: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.refresh:()V
        //   616: return         
        //   617: getstatic       dev/tenacity/Tenacity.INSTANCE:Ldev/tenacity/Tenacity;
        //   620: invokevirtual   dev/tenacity/Tenacity.getCloudDataManager:()Ldev/tenacity/intent/cloud/CloudDataManager;
        //   623: invokevirtual   dev/tenacity/intent/cloud/CloudDataManager.isRefreshing:()Z
        //   626: ifeq            630
        //   629: return         
        //   630: iload_3         /* isCloud */
        //   631: ifeq            1108
        //   634: aload_0         /* this */
        //   635: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sortingSelection:Ljava/lang/String;
        //   638: aload_0         /* this */
        //   639: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //   642: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.getSelection:()Ljava/lang/String;
        //   645: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   648: ifne            670
        //   651: invokedynamic   BootstrapMethod #3, run:()Ljava/lang/Runnable;
        //   656: invokestatic    dev/tenacity/utils/misc/Multithreading.runAsync:(Ljava/lang/Runnable;)V
        //   659: aload_0         /* this */
        //   660: aload_0         /* this */
        //   661: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //   664: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.getSelection:()Ljava/lang/String;
        //   667: putfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sortingSelection:Ljava/lang/String;
        //   670: aload_0         /* this */
        //   671: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.scriptFilter:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   674: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //   677: istore          filter
        //   679: fload           backgroundWidth
        //   681: ldc             36.0
        //   683: fsub           
        //   684: ldc             3.0
        //   686: fdiv           
        //   687: fstore          scriptWidth
        //   689: aload_0         /* this */
        //   690: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   693: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //   696: ifeq            704
        //   699: ldc             38.0
        //   701: goto            706
        //   704: ldc             90.0
        //   706: fstore          scriptHeight
        //   708: invokestatic    dev/tenacity/utils/render/StencilUtil.initStencilToWrite:()V
        //   711: aload_0         /* this */
        //   712: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //   715: fload           spacing
        //   717: fadd           
        //   718: fload           backgroundY
        //   720: fload           backgroundWidth
        //   722: fload           backgroundHeight
        //   724: ldc             5.0
        //   726: bipush          27
        //   728: aload_0         /* this */
        //   729: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //   732: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //   735: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   738: iconst_1       
        //   739: invokestatic    dev/tenacity/utils/render/StencilUtil.readStencilBuffer:(I)V
        //   742: iconst_0       
        //   743: istore          count
        //   745: iconst_0       
        //   746: istore          rectXSeparation
        //   748: iconst_0       
        //   749: istore          rectYSeparation
        //   751: aload_0         /* this */
        //   752: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.cloudScriptRects:Ljava/util/List;
        //   755: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   760: astore          17
        //   762: aload           17
        //   764: invokeinterface java/util/Iterator.hasNext:()Z
        //   769: ifeq            997
        //   772: aload           17
        //   774: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   779: checkcast       Ldev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect;
        //   782: astore          cloudScriptRect
        //   784: iload           filter
        //   786: ifeq            819
        //   789: aload           cloudScriptRect
        //   791: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getScript:()Ldev/tenacity/intent/cloud/data/CloudScript;
        //   794: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getAuthor:()Ljava/lang/String;
        //   797: astore          configAuthor
        //   799: getstatic       dev/tenacity/Tenacity.INSTANCE:Ldev/tenacity/Tenacity;
        //   802: invokevirtual   dev/tenacity/Tenacity.getIntentAccount:()Ldev/tenacity/intent/api/account/IntentAccount;
        //   805: getfield        dev/tenacity/intent/api/account/IntentAccount.username:Ljava/lang/String;
        //   808: aload           configAuthor
        //   810: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   813: ifne            819
        //   816: goto            762
        //   819: aload           cloudScriptRect
        //   821: aload_0         /* this */
        //   822: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //   825: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setAlpha:(F)V
        //   828: aload           cloudScriptRect
        //   830: aload_0         /* this */
        //   831: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAccentColor:()Ljava/awt/Color;
        //   834: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setAccentColor:(Ljava/awt/Color;)V
        //   837: iload           count
        //   839: iconst_2       
        //   840: if_icmple       861
        //   843: iconst_0       
        //   844: istore          rectXSeparation
        //   846: iload           rectYSeparation
        //   848: i2f            
        //   849: fload           scriptHeight
        //   851: ldc             12.0
        //   853: fadd           
        //   854: fadd           
        //   855: f2i            
        //   856: istore          rectYSeparation
        //   858: iconst_0       
        //   859: istore          count
        //   861: aload           cloudScriptRect
        //   863: fload           backgroundX
        //   865: ldc             6.0
        //   867: fadd           
        //   868: iload           rectXSeparation
        //   870: i2f            
        //   871: fadd           
        //   872: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setX:(F)V
        //   875: aload           cloudScriptRect
        //   877: fload           backgroundY
        //   879: ldc             6.0
        //   881: fadd           
        //   882: iload           rectYSeparation
        //   884: i2f            
        //   885: fadd           
        //   886: aload_0         /* this */
        //   887: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.cloudScriptScroll:Ldev/tenacity/utils/objects/Scroll;
        //   890: invokevirtual   dev/tenacity/utils/objects/Scroll.getScroll:()F
        //   893: fadd           
        //   894: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setY:(F)V
        //   897: aload           cloudScriptRect
        //   899: fload           scriptWidth
        //   901: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setWidth:(F)V
        //   904: aload           cloudScriptRect
        //   906: fload           scriptHeight
        //   908: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setHeight:(F)V
        //   911: aload           cloudScriptRect
        //   913: aload_0         /* this */
        //   914: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   917: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //   920: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setCompact:(Z)V
        //   923: aload           cloudScriptRect
        //   925: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getY:()F
        //   928: aload           cloudScriptRect
        //   930: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getHeight:()F
        //   933: fadd           
        //   934: fload           backgroundY
        //   936: fcmpl          
        //   937: ifle            970
        //   940: aload           cloudScriptRect
        //   942: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getY:()F
        //   945: fload           backgroundY
        //   947: fload           backgroundHeight
        //   949: fadd           
        //   950: fcmpg          
        //   951: ifge            970
        //   954: aload           cloudScriptRect
        //   956: iconst_1       
        //   957: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setClickable:(Z)V
        //   960: aload           cloudScriptRect
        //   962: iload_1         /* mouseX */
        //   963: iload_2         /* mouseY */
        //   964: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.drawScreen:(II)V
        //   967: goto            976
        //   970: aload           cloudScriptRect
        //   972: iconst_0       
        //   973: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.setClickable:(Z)V
        //   976: iload           rectXSeparation
        //   978: i2f            
        //   979: aload           cloudScriptRect
        //   981: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getWidth:()F
        //   984: ldc             12.0
        //   986: fadd           
        //   987: fadd           
        //   988: f2i            
        //   989: istore          rectXSeparation
        //   991: iinc            count, 1
        //   994: goto            762
        //   997: aload_0         /* this */
        //   998: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.cloudScriptScroll:Ldev/tenacity/utils/objects/Scroll;
        //  1001: iload           rectYSeparation
        //  1003: i2f            
        //  1004: invokevirtual   dev/tenacity/utils/objects/Scroll.setMaxScroll:(F)V
        //  1007: invokestatic    dev/tenacity/utils/render/StencilUtil.uninitStencilBuffer:()V
        //  1010: aload_0         /* this */
        //  1011: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1014: ldc             120.0
        //  1016: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setWidth:(F)V
        //  1019: aload_0         /* this */
        //  1020: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1023: ldc             15.0
        //  1025: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setHeight:(F)V
        //  1028: aload_0         /* this */
        //  1029: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1032: aload_0         /* this */
        //  1033: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getY:()F
        //  1036: ldc             5.0
        //  1038: fadd           
        //  1039: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setY:(F)V
        //  1042: aload_0         /* this */
        //  1043: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1046: aload_0         /* this */
        //  1047: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //  1050: aload_0         /* this */
        //  1051: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getWidth:()F
        //  1054: fadd           
        //  1055: aload_0         /* this */
        //  1056: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1059: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.getWidth:()F
        //  1062: ldc             8.0
        //  1064: fadd           
        //  1065: fsub           
        //  1066: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setX:(F)V
        //  1069: aload_0         /* this */
        //  1070: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1073: invokestatic    dev/tenacity/module/impl/render/HUDMod.getClientColors:()Ldev/tenacity/utils/tuples/Pair;
        //  1076: invokevirtual   dev/tenacity/utils/tuples/Pair.getFirst:()Ljava/lang/Object;
        //  1079: checkcast       Ljava/awt/Color;
        //  1082: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setAccentColor:(Ljava/awt/Color;)V
        //  1085: aload_0         /* this */
        //  1086: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1089: aload_0         /* this */
        //  1090: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //  1093: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setAlpha:(F)V
        //  1096: aload_0         /* this */
        //  1097: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1100: iload_1         /* mouseX */
        //  1101: iload_2         /* mouseY */
        //  1102: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.drawScreen:(II)V
        //  1105: goto            1404
        //  1108: fload           backgroundWidth
        //  1110: ldc             36.0
        //  1112: fsub           
        //  1113: ldc             3.0
        //  1115: fdiv           
        //  1116: fstore          localScriptWidth
        //  1118: aload_0         /* this */
        //  1119: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //  1122: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //  1125: ifeq            1133
        //  1128: ldc             38.0
        //  1130: goto            1135
        //  1133: ldc             90.0
        //  1135: fstore          localScriptHeight
        //  1137: invokestatic    dev/tenacity/utils/render/StencilUtil.initStencilToWrite:()V
        //  1140: aload_0         /* this */
        //  1141: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getX:()F
        //  1144: fload           spacing
        //  1146: fadd           
        //  1147: fload           backgroundY
        //  1149: fload           backgroundWidth
        //  1151: fload           backgroundHeight
        //  1153: ldc             5.0
        //  1155: bipush          27
        //  1157: aload_0         /* this */
        //  1158: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //  1161: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //  1164: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //  1167: iconst_1       
        //  1168: invokestatic    dev/tenacity/utils/render/StencilUtil.readStencilBuffer:(I)V
        //  1171: iconst_0       
        //  1172: istore          count2
        //  1174: iconst_0       
        //  1175: istore          rectXSeparation2
        //  1177: iconst_0       
        //  1178: istore          rectYSeparation2
        //  1180: aload_0         /* this */
        //  1181: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.localScriptRects:Ljava/util/List;
        //  1184: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //  1189: astore          16
        //  1191: aload           16
        //  1193: invokeinterface java/util/Iterator.hasNext:()Z
        //  1198: ifeq            1391
        //  1201: aload           16
        //  1203: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1208: checkcast       Ldev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect;
        //  1211: astore          localScriptRect
        //  1213: aload           localScriptRect
        //  1215: aload_0         /* this */
        //  1216: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAlpha:()F
        //  1219: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setAlpha:(F)V
        //  1222: aload           localScriptRect
        //  1224: aload_0         /* this */
        //  1225: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.getAccentColor:()Ljava/awt/Color;
        //  1228: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setAccentColor:(Ljava/awt/Color;)V
        //  1231: iload           count2
        //  1233: iconst_2       
        //  1234: if_icmple       1255
        //  1237: iconst_0       
        //  1238: istore          rectXSeparation2
        //  1240: iload           rectYSeparation2
        //  1242: i2f            
        //  1243: fload           localScriptHeight
        //  1245: ldc             12.0
        //  1247: fadd           
        //  1248: fadd           
        //  1249: f2i            
        //  1250: istore          rectYSeparation2
        //  1252: iconst_0       
        //  1253: istore          count2
        //  1255: aload           localScriptRect
        //  1257: fload           backgroundX
        //  1259: ldc             6.0
        //  1261: fadd           
        //  1262: iload           rectXSeparation2
        //  1264: i2f            
        //  1265: fadd           
        //  1266: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setX:(F)V
        //  1269: aload           localScriptRect
        //  1271: fload           backgroundY
        //  1273: ldc             6.0
        //  1275: fadd           
        //  1276: iload           rectYSeparation2
        //  1278: i2f            
        //  1279: fadd           
        //  1280: aload_0         /* this */
        //  1281: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.localScriptScroll:Ldev/tenacity/utils/objects/Scroll;
        //  1284: invokevirtual   dev/tenacity/utils/objects/Scroll.getScroll:()F
        //  1287: fadd           
        //  1288: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setY:(F)V
        //  1291: aload           localScriptRect
        //  1293: fload           localScriptWidth
        //  1295: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setWidth:(F)V
        //  1298: aload           localScriptRect
        //  1300: fload           localScriptHeight
        //  1302: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setHeight:(F)V
        //  1305: aload           localScriptRect
        //  1307: aload_0         /* this */
        //  1308: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //  1311: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //  1314: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setCompact:(Z)V
        //  1317: aload           localScriptRect
        //  1319: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getY:()F
        //  1322: aload           localScriptRect
        //  1324: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getHeight:()F
        //  1327: fadd           
        //  1328: fload           backgroundY
        //  1330: fcmpl          
        //  1331: ifle            1364
        //  1334: aload           localScriptRect
        //  1336: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getY:()F
        //  1339: fload           backgroundY
        //  1341: fload           backgroundHeight
        //  1343: fadd           
        //  1344: fcmpg          
        //  1345: ifge            1364
        //  1348: aload           localScriptRect
        //  1350: iconst_1       
        //  1351: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setClickable:(Z)V
        //  1354: aload           localScriptRect
        //  1356: iload_1         /* mouseX */
        //  1357: iload_2         /* mouseY */
        //  1358: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.drawScreen:(II)V
        //  1361: goto            1370
        //  1364: aload           localScriptRect
        //  1366: iconst_0       
        //  1367: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.setClickable:(Z)V
        //  1370: iload           rectXSeparation2
        //  1372: i2f            
        //  1373: aload           localScriptRect
        //  1375: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getWidth:()F
        //  1378: ldc             12.0
        //  1380: fadd           
        //  1381: fadd           
        //  1382: f2i            
        //  1383: istore          rectXSeparation2
        //  1385: iinc            count2, 1
        //  1388: goto            1191
        //  1391: aload_0         /* this */
        //  1392: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/ScriptPanel.localScriptScroll:Ldev/tenacity/utils/objects/Scroll;
        //  1395: iload           rectYSeparation2
        //  1397: i2f            
        //  1398: invokevirtual   dev/tenacity/utils/objects/Scroll.setMaxScroll:(F)V
        //  1401: invokestatic    dev/tenacity/utils/render/StencilUtil.uninitStencilBuffer:()V
        //  1404: return         
        //    StackMapTable: 00 1D FC 00 2D 01 FF 00 D3 00 0B 07 01 24 01 01 01 02 02 02 02 02 01 07 01 25 00 00 FF 00 31 00 0C 07 01 24 01 01 01 02 02 02 02 02 01 07 01 25 07 01 26 00 01 07 01 26 FF 00 01 00 0C 07 01 24 01 01 01 02 02 02 02 02 01 07 01 25 07 01 26 00 02 07 01 26 02 F9 00 55 FC 00 33 01 08 FB 00 46 FB 00 4D 0B 0C 27 FD 00 21 01 02 41 02 FF 00 37 00 12 07 01 24 01 01 01 02 02 02 02 02 01 01 01 02 02 01 01 01 07 01 25 00 00 FC 00 38 07 01 27 29 FB 00 6C 05 F9 00 14 FF 00 6E 00 0B 07 01 24 01 01 01 02 02 02 02 02 01 01 00 00 FC 00 18 02 41 02 FF 00 37 00 11 07 01 24 01 01 01 02 02 02 02 02 01 01 02 02 01 01 01 07 01 25 00 00 FC 00 3F 07 01 28 FB 00 6C 05 F9 00 14 FF 00 0C 00 0B 07 01 24 01 01 01 02 02 02 02 02 01 01 00 00
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
        this.carouselButtons.mouseClicked(mouseX, mouseY, button);
        final boolean isCloud = this.carouselButtons.getCurrentButton().equals("Cloud");
        if (isCloud) {
            this.sorting.mouseClicked(mouseX, mouseY, button);
            if (this.sorting.isClosed()) {
                this.scriptFilter.mouseClicked(mouseX, mouseY, button);
            }
            this.cloudScriptRects.forEach(cloudScriptRect -> cloudScriptRect.mouseClicked(mouseX, mouseY, button));
        }
        else {
            this.localScriptRects.forEach(localScriptRect -> localScriptRect.mouseClicked(mouseX, mouseY, button));
        }
        this.compactMode.mouseClicked(mouseX, mouseY, button);
        this.actionButtons.forEach(actionButton -> actionButton.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void refresh() {
        this.cloudScriptRects.clear();
        for (final CloudScript cloudScript : Tenacity.INSTANCE.getCloudDataManager().getCloudScripts()) {
            this.cloudScriptRects.add(new CloudScriptRect(cloudScript));
        }
        Tenacity.INSTANCE.getScriptManager().reloadScripts();
        this.localScriptRects.clear();
        for (final Script script : Tenacity.INSTANCE.getScriptManager().getScripts()) {
            this.localScriptRects.add(new LocalScriptRect(script));
        }
        this.initGui();
        this.refresh = false;
    }
    
    public void sortScripts() {
        final String selection = this.sorting.getSelection();
        switch (selection) {
            case "Relevance": {
                this.cloudScriptRects.sort(this.relevanceSorting());
                break;
            }
            case "Alphabetical": {
                this.cloudScriptRects.sort(this.alphabeticalSorting());
                break;
            }
            case "Top all time": {
                this.cloudScriptRects.sort(this.allTimeSorting().reversed());
                break;
            }
            case "Recently updated": {
                this.cloudScriptRects.sort(this.recentlyUpdatedSorting());
                break;
            }
        }
    }
    
    public Comparator<CloudScriptRect> recentlyUpdatedSorting() {
        return Comparator.comparingInt(configRect -> configRect.getScript().minutesSinceLastUpdate());
    }
    
    public Comparator<CloudScriptRect> allTimeSorting() {
        return Comparator.comparingInt(configRect -> configRect.getScript().getVotes().getTotalVotes());
    }
    
    public Comparator<CloudScriptRect> alphabeticalSorting() {
        return Comparator.comparing(cloudConfigRect -> cloudConfigRect.getScript().getName().toLowerCase());
    }
    
    public Comparator<CloudScriptRect> relevanceSorting() {
        final CloudScript cloudConfig;
        final int recentlyUploadedWeight;
        final int voteRatioWeight;
        final int daysSinceLastUpload;
        final int recentlyUploadedScore;
        final Votes votes;
        int totalVotes;
        int voteRatioScore;
        final int totalWeight;
        final int score;
        return Comparator.comparingInt(configRect -> {
            cloudConfig = configRect.getScript();
            recentlyUploadedWeight = 20;
            voteRatioWeight = 80;
            daysSinceLastUpload = cloudConfig.daysSinceLastUpdate();
            recentlyUploadedScore = Math.max(0, 225 - daysSinceLastUpload * daysSinceLastUpload);
            votes = cloudConfig.getVotes();
            if (votes != null) {
                totalVotes = votes.getUpvotes() + votes.getDownvotes();
                voteRatioScore = votes.getUpvotes() / Math.max(1, totalVotes) * 100;
                if (totalVotes < 15) {
                    voteRatioScore *= (int)0.75;
                }
            }
            else {
                voteRatioScore = 0;
            }
            totalWeight = recentlyUploadedWeight + voteRatioWeight;
            score = recentlyUploadedScore * recentlyUploadedWeight + voteRatioScore * voteRatioWeight;
            return score / totalWeight;
        }).reversed();
    }
    
    public List<CloudScriptRect> getCloudScriptRects() {
        return this.cloudScriptRects;
    }
    
    public void setRefresh(final boolean refresh) {
        this.refresh = refresh;
    }
}

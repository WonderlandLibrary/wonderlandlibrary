// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.configpanel;

import dev.tenacity.ui.sidegui.forms.Form;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.intent.cloud.data.Votes;
import java.util.Iterator;
import java.util.Comparator;
import dev.tenacity.config.LocalConfig;
import dev.tenacity.config.ConfigManager;
import dev.tenacity.intent.cloud.data.CloudConfig;
import dev.tenacity.Tenacity;
import java.util.ArrayList;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.utils.objects.Scroll;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import dev.tenacity.ui.sidegui.utils.ToggleButton;
import java.util.List;
import dev.tenacity.ui.sidegui.utils.CarouselButtons;
import dev.tenacity.ui.sidegui.utils.DropdownObject;
import dev.tenacity.ui.sidegui.panels.Panel;

public class ConfigPanel extends Panel
{
    private final DropdownObject sorting;
    private final CarouselButtons carouselButtons;
    private List<ToggleButton> toggleButtons;
    private ToggleButton compactMode;
    private List<ActionButton> actionButtons;
    private final List<CloudConfigRect> cloudConfigRects;
    private final List<LocalConfigRect> localConfigRects;
    private final Scroll cloudConfigScroll;
    private final Scroll localConfigScroll;
    private String sortingSelection;
    private boolean refresh;
    private final TimerUtil voteRefreshTimer;
    
    public ConfigPanel() {
        this.sorting = new DropdownObject("Sort by", new String[] { "Relevance", "Alphabetical", "Top all time", "Recently updated" });
        this.carouselButtons = new CarouselButtons(new String[] { "Cloud", "Local" });
        this.compactMode = new ToggleButton("Compact Mode");
        this.cloudConfigRects = new ArrayList<CloudConfigRect>();
        this.localConfigRects = new ArrayList<LocalConfigRect>();
        this.cloudConfigScroll = new Scroll();
        this.localConfigScroll = new Scroll();
        this.sortingSelection = "Relevance";
        this.refresh = false;
        this.voteRefreshTimer = new TimerUtil();
        (this.toggleButtons = new ArrayList<ToggleButton>()).add(new ToggleButton("Load visuals"));
        this.toggleButtons.add(new ToggleButton("Only show configs from current version"));
        this.toggleButtons.add(new ToggleButton("Only show configs made by you"));
        (this.actionButtons = new ArrayList<ActionButton>()).add(new ActionButton("Upload config"));
        this.actionButtons.add(new ActionButton("Save current config"));
        this.refresh();
    }
    
    @Override
    public void initGui() {
        this.sortingSelection = this.sorting.getSelection();
        this.sortConfigs();
        this.cloudConfigScroll.setRawScroll(0.0f);
        this.localConfigScroll.setRawScroll(0.0f);
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
        //     3: ldc             "Configs"
        //     5: aload_0         /* this */
        //     6: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //     9: ldc             8.0
        //    11: fadd           
        //    12: aload_0         /* this */
        //    13: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getY:()F
        //    16: ldc             8.0
        //    18: fadd           
        //    19: aload_0         /* this */
        //    20: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getTextColor:()Ljava/awt/Color;
        //    23: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //    26: aload_0         /* this */
        //    27: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.toggleButtons:Ljava/util/List;
        //    30: iconst_1       
        //    31: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    36: checkcast       Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //    39: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //    42: istore_3        /* filterVersion */
        //    43: aload_0         /* this */
        //    44: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.toggleButtons:Ljava/util/List;
        //    47: iconst_2       
        //    48: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    53: checkcast       Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //    56: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //    59: istore          filterAuthor
        //    61: aload_0         /* this */
        //    62: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //    65: aload_0         /* this */
        //    66: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //    69: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setAlpha:(F)V
        //    72: aload_0         /* this */
        //    73: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //    76: ldc             50.0
        //    78: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setRectWidth:(F)V
        //    81: aload_0         /* this */
        //    82: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //    85: ldc             18.0
        //    87: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setRectHeight:(F)V
        //    90: aload_0         /* this */
        //    91: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //    94: bipush          55
        //    96: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(I)Ljava/awt/Color;
        //    99: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setBackgroundColor:(Ljava/awt/Color;)V
        //   102: aload_0         /* this */
        //   103: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   106: aload_0         /* this */
        //   107: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getY:()F
        //   110: ldc             72.0
        //   112: fadd           
        //   113: aload_0         /* this */
        //   114: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   117: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //   120: fsub           
        //   121: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setY:(F)V
        //   124: aload_0         /* this */
        //   125: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   128: aload_0         /* this */
        //   129: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   132: aload_0         /* this */
        //   133: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getWidth:()F
        //   136: fconst_2       
        //   137: fdiv           
        //   138: fadd           
        //   139: aload_0         /* this */
        //   140: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   143: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getTotalWidth:()F
        //   146: fconst_2       
        //   147: fdiv           
        //   148: fsub           
        //   149: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.setX:(F)V
        //   152: aload_0         /* this */
        //   153: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   156: iload_1         /* mouseX */
        //   157: iload_2         /* mouseY */
        //   158: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.drawScreen:(II)V
        //   161: ldc             8.0
        //   163: fstore          spacing
        //   165: iconst_0       
        //   166: istore          additionalSeparation
        //   168: aload_0         /* this */
        //   169: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.actionButtons:Ljava/util/List;
        //   172: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   177: astore          7
        //   179: aload           7
        //   181: invokeinterface java/util/Iterator.hasNext:()Z
        //   186: ifeq            307
        //   189: aload           7
        //   191: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   196: checkcast       Ldev/tenacity/ui/sidegui/utils/ActionButton;
        //   199: astore          button
        //   201: aload           button
        //   203: aload_0         /* this */
        //   204: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   207: ldc             10.0
        //   209: fadd           
        //   210: iload           additionalSeparation
        //   212: i2f            
        //   213: fadd           
        //   214: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setX:(F)V
        //   217: aload           button
        //   219: ldc             85.0
        //   221: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setWidth:(F)V
        //   224: aload           button
        //   226: ldc             15.0
        //   228: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setHeight:(F)V
        //   231: aload           button
        //   233: aload_0         /* this */
        //   234: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   237: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getY:()F
        //   240: aload_0         /* this */
        //   241: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   244: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //   247: fconst_2       
        //   248: fdiv           
        //   249: fadd           
        //   250: aload           button
        //   252: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.getHeight:()F
        //   255: fconst_2       
        //   256: fdiv           
        //   257: fsub           
        //   258: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setY:(F)V
        //   261: aload           button
        //   263: aload_0         /* this */
        //   264: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //   267: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setAlpha:(F)V
        //   270: aload           button
        //   272: aload           button
        //   274: invokedynamic   BootstrapMethod #0, run:(Ldev/tenacity/ui/sidegui/utils/ActionButton;)Ljava/lang/Runnable;
        //   279: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setClickAction:(Ljava/lang/Runnable;)V
        //   282: aload           button
        //   284: iload_1         /* mouseX */
        //   285: iload_2         /* mouseY */
        //   286: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.drawScreen:(II)V
        //   289: iload           additionalSeparation
        //   291: i2f            
        //   292: aload           button
        //   294: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.getWidth:()F
        //   297: fload           spacing
        //   299: fadd           
        //   300: fadd           
        //   301: f2i            
        //   302: istore          additionalSeparation
        //   304: goto            179
        //   307: aload_0         /* this */
        //   308: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   311: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getY:()F
        //   314: aload_0         /* this */
        //   315: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   318: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //   321: fadd           
        //   322: fload           spacing
        //   324: fadd           
        //   325: fstore          backgroundY
        //   327: aload_0         /* this */
        //   328: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   331: ldc             8.0
        //   333: fadd           
        //   334: fstore          backgroundX
        //   336: aload_0         /* this */
        //   337: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getWidth:()F
        //   340: fload           spacing
        //   342: fconst_2       
        //   343: fmul           
        //   344: fsub           
        //   345: fstore          backgroundWidth
        //   347: aload_0         /* this */
        //   348: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getHeight:()F
        //   351: fload           backgroundY
        //   353: aload_0         /* this */
        //   354: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getY:()F
        //   357: fsub           
        //   358: fconst_1       
        //   359: fadd           
        //   360: fload           spacing
        //   362: fadd           
        //   363: fsub           
        //   364: fstore          backgroundHeight
        //   366: aload_0         /* this */
        //   367: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   370: fload           spacing
        //   372: fadd           
        //   373: fload           backgroundY
        //   375: fload           backgroundWidth
        //   377: fload           backgroundHeight
        //   379: ldc             5.0
        //   381: bipush          27
        //   383: aload_0         /* this */
        //   384: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //   387: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //   390: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   393: aload_0         /* this */
        //   394: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   397: fload           spacing
        //   399: fadd           
        //   400: fload           backgroundY
        //   402: fload           backgroundWidth
        //   404: fload           backgroundHeight
        //   406: iload_1         /* mouseX */
        //   407: iload_2         /* mouseY */
        //   408: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   411: istore          hovering
        //   413: iload           hovering
        //   415: ifeq            454
        //   418: aload_0         /* this */
        //   419: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   422: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getCurrentButton:()Ljava/lang/String;
        //   425: ldc             "Cloud"
        //   427: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   430: ifeq            445
        //   433: aload_0         /* this */
        //   434: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.cloudConfigScroll:Ldev/tenacity/utils/objects/Scroll;
        //   437: bipush          35
        //   439: invokevirtual   dev/tenacity/utils/objects/Scroll.onScroll:(I)V
        //   442: goto            454
        //   445: aload_0         /* this */
        //   446: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.localConfigScroll:Ldev/tenacity/utils/objects/Scroll;
        //   449: bipush          35
        //   451: invokevirtual   dev/tenacity/utils/objects/Scroll.onScroll:(I)V
        //   454: aload_0         /* this */
        //   455: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.refresh:Z
        //   458: ifeq            466
        //   461: aload_0         /* this */
        //   462: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.refresh:()V
        //   465: return         
        //   466: aload_0         /* this */
        //   467: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //   470: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getCurrentButton:()Ljava/lang/String;
        //   473: astore          12
        //   475: iconst_m1      
        //   476: istore          13
        //   478: aload           12
        //   480: invokevirtual   java/lang/String.hashCode:()I
        //   483: lookupswitch {
        //          65203733: 508
        //          73592651: 524
        //          default: 537
        //        }
        //   508: aload           12
        //   510: ldc             "Cloud"
        //   512: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   515: ifeq            537
        //   518: iconst_0       
        //   519: istore          13
        //   521: goto            537
        //   524: aload           12
        //   526: ldc             "Local"
        //   528: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   531: ifeq            537
        //   534: iconst_1       
        //   535: istore          13
        //   537: iload           13
        //   539: lookupswitch {
        //                0: 564
        //                1: 1201
        //          default: 1554
        //        }
        //   564: iconst_0       
        //   565: istore          seperation
        //   567: aload_0         /* this */
        //   568: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.toggleButtons:Ljava/util/List;
        //   571: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   576: astore          15
        //   578: aload           15
        //   580: invokeinterface java/util/Iterator.hasNext:()Z
        //   585: ifeq            673
        //   588: aload           15
        //   590: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   595: checkcast       Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   598: astore          toggleButton
        //   600: aload           toggleButton
        //   602: aload_0         /* this */
        //   603: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   606: aload_0         /* this */
        //   607: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getWidth:()F
        //   610: fadd           
        //   611: aload           toggleButton
        //   613: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.getWH:()F
        //   616: ldc             12.0
        //   618: fadd           
        //   619: fsub           
        //   620: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setX:(F)V
        //   623: aload           toggleButton
        //   625: aload_0         /* this */
        //   626: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getY:()F
        //   629: ldc             35.0
        //   631: fadd           
        //   632: iload           seperation
        //   634: i2f            
        //   635: fadd           
        //   636: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setY:(F)V
        //   639: aload           toggleButton
        //   641: aload_0         /* this */
        //   642: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //   645: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setAlpha:(F)V
        //   648: aload           toggleButton
        //   650: iload_1         /* mouseX */
        //   651: iload_2         /* mouseY */
        //   652: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.drawScreen:(II)V
        //   655: iload           seperation
        //   657: i2f            
        //   658: aload           toggleButton
        //   660: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.getWH:()F
        //   663: ldc             4.0
        //   665: fadd           
        //   666: fadd           
        //   667: f2i            
        //   668: istore          seperation
        //   670: goto            578
        //   673: aload_0         /* this */
        //   674: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   677: aload_0         /* this */
        //   678: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   681: ldc             69.0
        //   683: fadd           
        //   684: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setX:(F)V
        //   687: aload_0         /* this */
        //   688: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   691: aload_0         /* this */
        //   692: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getY:()F
        //   695: ldc             33.0
        //   697: fadd           
        //   698: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setY:(F)V
        //   701: aload_0         /* this */
        //   702: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   705: aload_0         /* this */
        //   706: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //   709: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setAlpha:(F)V
        //   712: aload_0         /* this */
        //   713: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   716: iload_1         /* mouseX */
        //   717: iload_2         /* mouseY */
        //   718: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.drawScreen:(II)V
        //   721: aload_0         /* this */
        //   722: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sortingSelection:Ljava/lang/String;
        //   725: aload_0         /* this */
        //   726: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //   729: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.getSelection:()Ljava/lang/String;
        //   732: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   735: ifne            749
        //   738: aload_0         /* this */
        //   739: aload_0         /* this */
        //   740: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //   743: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.getSelection:()Ljava/lang/String;
        //   746: putfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sortingSelection:Ljava/lang/String;
        //   749: fload           backgroundWidth
        //   751: ldc             36.0
        //   753: fsub           
        //   754: ldc             3.0
        //   756: fdiv           
        //   757: fstore          configWidth
        //   759: aload_0         /* this */
        //   760: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //   763: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //   766: ifeq            774
        //   769: ldc             38.0
        //   771: goto            776
        //   774: ldc             90.0
        //   776: fstore          configHeight
        //   778: invokestatic    dev/tenacity/utils/render/StencilUtil.initStencilToWrite:()V
        //   781: aload_0         /* this */
        //   782: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //   785: fload           spacing
        //   787: fadd           
        //   788: fload           backgroundY
        //   790: fload           backgroundWidth
        //   792: fload           backgroundHeight
        //   794: ldc             5.0
        //   796: bipush          27
        //   798: aload_0         /* this */
        //   799: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //   802: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //   805: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   808: iconst_1       
        //   809: invokestatic    dev/tenacity/utils/render/StencilUtil.readStencilBuffer:(I)V
        //   812: iconst_0       
        //   813: istore          count
        //   815: iconst_0       
        //   816: istore          rectXSeparation
        //   818: iconst_0       
        //   819: istore          rectYSeparation
        //   821: aload_0         /* this */
        //   822: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.cloudConfigRects:Ljava/util/List;
        //   825: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   830: astore          20
        //   832: aload           20
        //   834: invokeinterface java/util/Iterator.hasNext:()Z
        //   839: ifeq            1090
        //   842: aload           20
        //   844: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   849: checkcast       Ldev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect;
        //   852: astore          cloudConfigRect
        //   854: iload           filterAuthor
        //   856: ifeq            889
        //   859: aload           cloudConfigRect
        //   861: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getConfig:()Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   864: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getAuthor:()Ljava/lang/String;
        //   867: astore          configAuthor
        //   869: getstatic       dev/tenacity/Tenacity.INSTANCE:Ldev/tenacity/Tenacity;
        //   872: invokevirtual   dev/tenacity/Tenacity.getIntentAccount:()Ldev/tenacity/intent/api/account/IntentAccount;
        //   875: getfield        dev/tenacity/intent/api/account/IntentAccount.username:Ljava/lang/String;
        //   878: aload           configAuthor
        //   880: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   883: ifne            889
        //   886: goto            832
        //   889: iload_3         /* filterVersion */
        //   890: ifeq            912
        //   893: aload           cloudConfigRect
        //   895: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getConfig:()Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   898: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getVersion:()Ljava/lang/String;
        //   901: ldc             "6.0"
        //   903: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   906: ifne            912
        //   909: goto            832
        //   912: aload           cloudConfigRect
        //   914: aload_0         /* this */
        //   915: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //   918: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setAlpha:(F)V
        //   921: aload           cloudConfigRect
        //   923: aload_0         /* this */
        //   924: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAccentColor:()Ljava/awt/Color;
        //   927: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setAccentColor:(Ljava/awt/Color;)V
        //   930: iload           count
        //   932: iconst_2       
        //   933: if_icmple       954
        //   936: iconst_0       
        //   937: istore          rectXSeparation
        //   939: iload           rectYSeparation
        //   941: i2f            
        //   942: fload           configHeight
        //   944: ldc             12.0
        //   946: fadd           
        //   947: fadd           
        //   948: f2i            
        //   949: istore          rectYSeparation
        //   951: iconst_0       
        //   952: istore          count
        //   954: aload           cloudConfigRect
        //   956: fload           backgroundX
        //   958: ldc             6.0
        //   960: fadd           
        //   961: iload           rectXSeparation
        //   963: i2f            
        //   964: fadd           
        //   965: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setX:(F)V
        //   968: aload           cloudConfigRect
        //   970: fload           backgroundY
        //   972: ldc             6.0
        //   974: fadd           
        //   975: iload           rectYSeparation
        //   977: i2f            
        //   978: fadd           
        //   979: aload_0         /* this */
        //   980: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.cloudConfigScroll:Ldev/tenacity/utils/objects/Scroll;
        //   983: invokevirtual   dev/tenacity/utils/objects/Scroll.getScroll:()F
        //   986: fadd           
        //   987: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setY:(F)V
        //   990: aload           cloudConfigRect
        //   992: fload           configWidth
        //   994: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setWidth:(F)V
        //   997: aload           cloudConfigRect
        //   999: fload           configHeight
        //  1001: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setHeight:(F)V
        //  1004: aload           cloudConfigRect
        //  1006: aload_0         /* this */
        //  1007: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.compactMode:Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //  1010: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //  1013: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setCompact:(Z)V
        //  1016: aload           cloudConfigRect
        //  1018: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getY:()F
        //  1021: aload           cloudConfigRect
        //  1023: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getHeight:()F
        //  1026: fadd           
        //  1027: fload           backgroundY
        //  1029: fcmpl          
        //  1030: ifle            1063
        //  1033: aload           cloudConfigRect
        //  1035: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getY:()F
        //  1038: fload           backgroundY
        //  1040: fload           backgroundHeight
        //  1042: fadd           
        //  1043: fcmpg          
        //  1044: ifge            1063
        //  1047: aload           cloudConfigRect
        //  1049: iconst_1       
        //  1050: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setClickable:(Z)V
        //  1053: aload           cloudConfigRect
        //  1055: iload_1         /* mouseX */
        //  1056: iload_2         /* mouseY */
        //  1057: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.drawScreen:(II)V
        //  1060: goto            1069
        //  1063: aload           cloudConfigRect
        //  1065: iconst_0       
        //  1066: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.setClickable:(Z)V
        //  1069: iload           rectXSeparation
        //  1071: i2f            
        //  1072: aload           cloudConfigRect
        //  1074: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getWidth:()F
        //  1077: ldc             12.0
        //  1079: fadd           
        //  1080: fadd           
        //  1081: f2i            
        //  1082: istore          rectXSeparation
        //  1084: iinc            count, 1
        //  1087: goto            832
        //  1090: aload_0         /* this */
        //  1091: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.cloudConfigScroll:Ldev/tenacity/utils/objects/Scroll;
        //  1094: iload           rectYSeparation
        //  1096: i2f            
        //  1097: invokevirtual   dev/tenacity/utils/objects/Scroll.setMaxScroll:(F)V
        //  1100: invokestatic    dev/tenacity/utils/render/StencilUtil.uninitStencilBuffer:()V
        //  1103: aload_0         /* this */
        //  1104: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1107: ldc             120.0
        //  1109: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setWidth:(F)V
        //  1112: aload_0         /* this */
        //  1113: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1116: ldc             15.0
        //  1118: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setHeight:(F)V
        //  1121: aload_0         /* this */
        //  1122: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1125: aload_0         /* this */
        //  1126: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getY:()F
        //  1129: ldc             5.0
        //  1131: fadd           
        //  1132: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setY:(F)V
        //  1135: aload_0         /* this */
        //  1136: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1139: aload_0         /* this */
        //  1140: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //  1143: aload_0         /* this */
        //  1144: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getWidth:()F
        //  1147: fadd           
        //  1148: aload_0         /* this */
        //  1149: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1152: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.getWidth:()F
        //  1155: ldc             8.0
        //  1157: fadd           
        //  1158: fsub           
        //  1159: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setX:(F)V
        //  1162: aload_0         /* this */
        //  1163: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1166: invokestatic    dev/tenacity/module/impl/render/HUDMod.getClientColors:()Ldev/tenacity/utils/tuples/Pair;
        //  1169: invokevirtual   dev/tenacity/utils/tuples/Pair.getFirst:()Ljava/lang/Object;
        //  1172: checkcast       Ljava/awt/Color;
        //  1175: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setAccentColor:(Ljava/awt/Color;)V
        //  1178: aload_0         /* this */
        //  1179: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1182: aload_0         /* this */
        //  1183: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //  1186: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.setAlpha:(F)V
        //  1189: aload_0         /* this */
        //  1190: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.sorting:Ldev/tenacity/ui/sidegui/utils/DropdownObject;
        //  1193: iload_1         /* mouseX */
        //  1194: iload_2         /* mouseY */
        //  1195: invokevirtual   dev/tenacity/ui/sidegui/utils/DropdownObject.drawScreen:(II)V
        //  1198: goto            1554
        //  1201: aload_0         /* this */
        //  1202: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.toggleButtons:Ljava/util/List;
        //  1205: iconst_0       
        //  1206: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  1211: checkcast       Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //  1214: astore          loadVisuals
        //  1216: aload           loadVisuals
        //  1218: aload_0         /* this */
        //  1219: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //  1222: aload_0         /* this */
        //  1223: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getWidth:()F
        //  1226: fadd           
        //  1227: aload           loadVisuals
        //  1229: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.getWH:()F
        //  1232: ldc             8.0
        //  1234: fadd           
        //  1235: fsub           
        //  1236: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setX:(F)V
        //  1239: aload           loadVisuals
        //  1241: aload_0         /* this */
        //  1242: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //  1245: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getY:()F
        //  1248: aload_0         /* this */
        //  1249: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.carouselButtons:Ldev/tenacity/ui/sidegui/utils/CarouselButtons;
        //  1252: invokevirtual   dev/tenacity/ui/sidegui/utils/CarouselButtons.getRectHeight:()F
        //  1255: fconst_2       
        //  1256: fdiv           
        //  1257: fadd           
        //  1258: aload           loadVisuals
        //  1260: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.getWH:()F
        //  1263: fconst_2       
        //  1264: fdiv           
        //  1265: fsub           
        //  1266: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setY:(F)V
        //  1269: aload           loadVisuals
        //  1271: aload_0         /* this */
        //  1272: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //  1275: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.setAlpha:(F)V
        //  1278: aload           loadVisuals
        //  1280: iload_1         /* mouseX */
        //  1281: iload_2         /* mouseY */
        //  1282: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.drawScreen:(II)V
        //  1285: fload           backgroundWidth
        //  1287: ldc             36.0
        //  1289: fsub           
        //  1290: ldc             3.0
        //  1292: fdiv           
        //  1293: fstore          localConfigWidth
        //  1295: ldc             38.0
        //  1297: fstore          loaclConfigHeight
        //  1299: invokestatic    dev/tenacity/utils/render/StencilUtil.initStencilToWrite:()V
        //  1302: aload_0         /* this */
        //  1303: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getX:()F
        //  1306: fload           spacing
        //  1308: fadd           
        //  1309: fload           backgroundY
        //  1311: fload           backgroundWidth
        //  1313: fload           backgroundHeight
        //  1315: ldc             5.0
        //  1317: bipush          27
        //  1319: aload_0         /* this */
        //  1320: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //  1323: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //  1326: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //  1329: iconst_1       
        //  1330: invokestatic    dev/tenacity/utils/render/StencilUtil.readStencilBuffer:(I)V
        //  1333: iconst_0       
        //  1334: istore          count2
        //  1336: iconst_0       
        //  1337: istore          rectXSeparation2
        //  1339: iconst_0       
        //  1340: istore          rectYSeparation2
        //  1342: aload_0         /* this */
        //  1343: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.localConfigRects:Ljava/util/List;
        //  1346: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //  1351: astore          26
        //  1353: aload           26
        //  1355: invokeinterface java/util/Iterator.hasNext:()Z
        //  1360: ifeq            1541
        //  1363: aload           26
        //  1365: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1370: checkcast       Ldev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect;
        //  1373: astore          localConfigRect
        //  1375: aload           localConfigRect
        //  1377: aload_0         /* this */
        //  1378: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAlpha:()F
        //  1381: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setAlpha:(F)V
        //  1384: aload           localConfigRect
        //  1386: aload_0         /* this */
        //  1387: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.getAccentColor:()Ljava/awt/Color;
        //  1390: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setAccentColor:(Ljava/awt/Color;)V
        //  1393: iload           count2
        //  1395: iconst_2       
        //  1396: if_icmple       1417
        //  1399: iconst_0       
        //  1400: istore          rectXSeparation2
        //  1402: iload           rectYSeparation2
        //  1404: i2f            
        //  1405: fload           loaclConfigHeight
        //  1407: ldc             12.0
        //  1409: fadd           
        //  1410: fadd           
        //  1411: f2i            
        //  1412: istore          rectYSeparation2
        //  1414: iconst_0       
        //  1415: istore          count2
        //  1417: aload           localConfigRect
        //  1419: fload           backgroundX
        //  1421: ldc             6.0
        //  1423: fadd           
        //  1424: iload           rectXSeparation2
        //  1426: i2f            
        //  1427: fadd           
        //  1428: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setX:(F)V
        //  1431: aload           localConfigRect
        //  1433: fload           backgroundY
        //  1435: ldc             6.0
        //  1437: fadd           
        //  1438: iload           rectYSeparation2
        //  1440: i2f            
        //  1441: fadd           
        //  1442: aload_0         /* this */
        //  1443: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.localConfigScroll:Ldev/tenacity/utils/objects/Scroll;
        //  1446: invokevirtual   dev/tenacity/utils/objects/Scroll.getScroll:()F
        //  1449: fadd           
        //  1450: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setY:(F)V
        //  1453: aload           localConfigRect
        //  1455: fload           localConfigWidth
        //  1457: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setWidth:(F)V
        //  1460: aload           localConfigRect
        //  1462: fload           loaclConfigHeight
        //  1464: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setHeight:(F)V
        //  1467: aload           localConfigRect
        //  1469: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.getY:()F
        //  1472: aload           localConfigRect
        //  1474: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.getHeight:()F
        //  1477: fadd           
        //  1478: fload           backgroundY
        //  1480: fcmpl          
        //  1481: ifle            1514
        //  1484: aload           localConfigRect
        //  1486: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.getY:()F
        //  1489: fload           backgroundY
        //  1491: fload           backgroundHeight
        //  1493: fadd           
        //  1494: fcmpg          
        //  1495: ifge            1514
        //  1498: aload           localConfigRect
        //  1500: iconst_1       
        //  1501: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setClickable:(Z)V
        //  1504: aload           localConfigRect
        //  1506: iload_1         /* mouseX */
        //  1507: iload_2         /* mouseY */
        //  1508: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.drawScreen:(II)V
        //  1511: goto            1520
        //  1514: aload           localConfigRect
        //  1516: iconst_0       
        //  1517: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.setClickable:(Z)V
        //  1520: iload           rectXSeparation2
        //  1522: i2f            
        //  1523: aload           localConfigRect
        //  1525: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.getWidth:()F
        //  1528: ldc             12.0
        //  1530: fadd           
        //  1531: fadd           
        //  1532: f2i            
        //  1533: istore          rectXSeparation2
        //  1535: iinc            count2, 1
        //  1538: goto            1353
        //  1541: aload_0         /* this */
        //  1542: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.localConfigScroll:Ldev/tenacity/utils/objects/Scroll;
        //  1545: iload           rectYSeparation2
        //  1547: i2f            
        //  1548: invokevirtual   dev/tenacity/utils/objects/Scroll.setMaxScroll:(F)V
        //  1551: invokestatic    dev/tenacity/utils/render/StencilUtil.uninitStencilBuffer:()V
        //  1554: aload_0         /* this */
        //  1555: getfield        dev/tenacity/ui/sidegui/panels/configpanel/ConfigPanel.toggleButtons:Ljava/util/List;
        //  1558: iconst_0       
        //  1559: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  1564: checkcast       Ldev/tenacity/ui/sidegui/utils/ToggleButton;
        //  1567: invokevirtual   dev/tenacity/ui/sidegui/utils/ToggleButton.isEnabled:()Z
        //  1570: putstatic       dev/tenacity/config/ConfigManager.loadVisuals:Z
        //  1573: return         
        //    StackMapTable: 00 1C FF 00 B3 00 08 07 01 35 01 01 01 01 02 01 07 01 36 00 00 FA 00 7F FF 00 89 00 0C 07 01 35 01 01 01 01 02 01 02 02 02 02 01 00 00 08 0B FD 00 29 07 01 37 01 0F 0C 1A FD 00 0D 01 07 01 36 FA 00 5E FB 00 4B FC 00 18 02 41 02 FF 00 37 00 15 07 01 35 01 01 01 01 02 01 02 02 02 02 01 07 01 37 01 01 02 02 01 01 01 07 01 36 00 00 FC 00 38 07 01 38 16 29 FB 00 6C 05 F9 00 14 FF 00 6E 00 0E 07 01 35 01 01 01 01 02 01 02 02 02 02 01 07 01 37 01 00 00 FF 00 97 00 1B 07 01 35 01 01 01 01 02 01 02 02 02 02 01 07 01 37 01 00 00 00 00 00 00 07 01 39 02 02 01 01 01 07 01 36 00 00 FC 00 3F 07 01 3A FB 00 60 05 F9 00 14 FF 00 0C 00 0C 07 01 35 01 01 01 01 02 01 02 02 02 02 01 00 00
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
        final boolean isCloud = this.carouselButtons.getCurrentButton().equals("Cloud");
        if (isCloud) {
            this.sorting.mouseClicked(mouseX, mouseY, button);
            if (this.sorting.isClosed()) {
                this.toggleButtons.forEach(toggleButton -> toggleButton.mouseClicked(mouseX, mouseY, button));
                this.compactMode.mouseClicked(mouseX, mouseY, button);
            }
            this.cloudConfigRects.forEach(cloudConfigRect -> cloudConfigRect.mouseClicked(mouseX, mouseY, button));
        }
        else {
            this.localConfigRects.forEach(localConfigRect -> localConfigRect.mouseClicked(mouseX, mouseY, button));
            this.toggleButtons.get(0).mouseClicked(mouseX, mouseY, button);
        }
        this.carouselButtons.mouseClicked(mouseX, mouseY, button);
        this.actionButtons.forEach(button1 -> button1.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void refresh() {
        this.cloudConfigRects.clear();
        for (final CloudConfig cloudConfig : Tenacity.INSTANCE.getCloudDataManager().getCloudConfigs()) {
            this.cloudConfigRects.add(new CloudConfigRect(cloudConfig));
        }
        Tenacity.INSTANCE.getConfigManager().collectConfigs();
        this.localConfigRects.clear();
        for (final LocalConfig localConfig : ConfigManager.localConfigs) {
            this.localConfigRects.add(new LocalConfigRect(localConfig));
        }
        this.localConfigRects.sort(Comparator.comparingLong(local -> local.getBfa().lastModifiedTime().toMillis()).reversed());
        this.initGui();
        this.refresh = false;
    }
    
    public void sortConfigs() {
        final String selection = this.sorting.getSelection();
        switch (selection) {
            case "Relevance": {
                this.cloudConfigRects.sort(this.relevanceSorting());
                break;
            }
            case "Alphabetical": {
                this.cloudConfigRects.sort(this.alphabeticalSorting());
                break;
            }
            case "Top all time": {
                this.cloudConfigRects.sort(this.allTimeSorting().reversed());
                break;
            }
            case "Recently updated": {
                this.cloudConfigRects.sort(this.recentlyUpdatedSorting());
                break;
            }
        }
    }
    
    public Comparator<CloudConfigRect> recentlyUpdatedSorting() {
        return Comparator.comparingInt(configRect -> configRect.getConfig().minutesSinceLastUpdate());
    }
    
    public Comparator<CloudConfigRect> allTimeSorting() {
        return Comparator.comparingInt(configRect -> configRect.getConfig().getVotes().getTotalVotes());
    }
    
    public Comparator<CloudConfigRect> alphabeticalSorting() {
        return Comparator.comparing(cloudConfigRect -> cloudConfigRect.getConfig().getName().toLowerCase());
    }
    
    public Comparator<CloudConfigRect> relevanceSorting() {
        final CloudConfig cloudConfig;
        final int serverSameWeight;
        final int recentlyUploadedWeight;
        final int versionSameWeight;
        final int voteRatioWeight;
        int serverScore;
        final int daysSinceLastUpload;
        final int recentlyUploadedScore;
        final int versionScore;
        final Votes votes;
        int totalVotes;
        int voteRatioScore;
        final int totalWeight;
        final int score;
        return Comparator.comparingInt(configRect -> {
            cloudConfig = configRect.getConfig();
            serverSameWeight = 10;
            recentlyUploadedWeight = 20;
            versionSameWeight = 35;
            voteRatioWeight = 35;
            serverScore = 0;
            if (!ConfigPanel.mc.isSingleplayer() && ConfigPanel.mc.getCurrentServerData() != null && cloudConfig.getServer().equals(ConfigPanel.mc.getCurrentServerData().serverIP)) {
                serverScore = 100;
            }
            daysSinceLastUpload = cloudConfig.daysSinceLastUpdate();
            recentlyUploadedScore = Math.max(0, 225 - daysSinceLastUpload * daysSinceLastUpload);
            versionScore = (cloudConfig.getVersion().equals("6.0") ? 100 : 0);
            votes = cloudConfig.getVotes();
            if (votes != null) {
                totalVotes = votes.getUpvotes() + votes.getDownvotes();
                voteRatioScore = votes.getUpvotes() / Math.max(1, votes.getUpvotes() + votes.getDownvotes()) * 100;
                if (totalVotes < 15) {
                    voteRatioScore *= (int)0.75;
                }
            }
            else {
                voteRatioScore = 0;
            }
            totalWeight = serverSameWeight + recentlyUploadedWeight + versionSameWeight + voteRatioWeight;
            score = serverScore * serverSameWeight + recentlyUploadedScore * recentlyUploadedWeight + versionScore * versionSameWeight + voteRatioScore * voteRatioWeight;
            return score / totalWeight;
        }).reversed();
    }
    
    public List<CloudConfigRect> getCloudConfigRects() {
        return this.cloudConfigRects;
    }
    
    public void setRefresh(final boolean refresh) {
        this.refresh = refresh;
    }
}

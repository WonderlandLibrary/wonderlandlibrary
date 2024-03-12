// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.scriptpanel;

import dev.tenacity.ui.sidegui.forms.Form;
import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.intent.cloud.CloudUtils;
import dev.tenacity.utils.misc.FileUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.ArrayList;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.scripting.api.Script;
import dev.tenacity.ui.sidegui.utils.IconButton;
import java.util.List;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import java.nio.file.attribute.BasicFileAttributes;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class LocalScriptRect implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private Color accentColor;
    private boolean clickable;
    private boolean compact;
    private BasicFileAttributes bfa;
    private final TooltipObject hoverInformation;
    private final List<IconButton> buttons;
    private final Script script;
    private final Animation hoverAnimation;
    
    public LocalScriptRect(final Script script) {
        this.clickable = true;
        this.compact = false;
        this.bfa = null;
        this.hoverInformation = new TooltipObject();
        this.buttons = new ArrayList<IconButton>();
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.script = script;
        Tenacity.INSTANCE.getSideGui().addTooltip(this.hoverInformation);
        this.buttons.add(new IconButton("C", "Upload this script"));
        this.buttons.add(new IconButton("q", "Delete this script"));
        try {
            this.bfa = Files.readAttributes(script.getFile().toPath(), BasicFileAttributes.class, new LinkOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
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
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: aload_0         /* this */
        //     4: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.alpha:F
        //     7: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //    10: astore_3        /* textColor */
        //    11: aload_0         /* this */
        //    12: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //    15: aload_0         /* this */
        //    16: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //    19: aload_0         /* this */
        //    20: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.width:F
        //    23: aload_0         /* this */
        //    24: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.height:F
        //    27: ldc             5.0
        //    29: bipush          37
        //    31: aload_0         /* this */
        //    32: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.alpha:F
        //    35: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //    38: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //    41: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityBoldFont26:Ldev/tenacity/utils/font/CustomFont;
        //    44: aload_0         /* this */
        //    45: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.script:Ldev/tenacity/scripting/api/Script;
        //    48: invokevirtual   dev/tenacity/scripting/api/Script.getName:()Ljava/lang/String;
        //    51: aload_0         /* this */
        //    52: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //    55: ldc             3.0
        //    57: fadd           
        //    58: aload_0         /* this */
        //    59: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //    62: ldc             3.0
        //    64: fadd           
        //    65: aload_3         /* textColor */
        //    66: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //    69: aload_0         /* this */
        //    70: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.compact:Z
        //    73: ifeq            81
        //    76: ldc             2.5
        //    78: goto            82
        //    81: fconst_2       
        //    82: fstore          yOffset
        //    84: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //    87: aload_0         /* this */
        //    88: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.script:Ldev/tenacity/scripting/api/Script;
        //    91: invokevirtual   dev/tenacity/scripting/api/Script.getAuthor:()Ljava/lang/String;
        //    94: aload_0         /* this */
        //    95: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //    98: ldc             3.0
        //   100: fadd           
        //   101: aload_0         /* this */
        //   102: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //   105: fload           yOffset
        //   107: fadd           
        //   108: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   111: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   114: i2f            
        //   115: fadd           
        //   116: aload_0         /* this */
        //   117: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.accentColor:Ljava/awt/Color;
        //   120: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   123: aload_0         /* this */
        //   124: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.bfa:Ljava/nio/file/attribute/BasicFileAttributes;
        //   127: ifnull          200
        //   130: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   133: aload_0         /* this */
        //   134: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.bfa:Ljava/nio/file/attribute/BasicFileAttributes;
        //   137: invokeinterface java/nio/file/attribute/BasicFileAttributes.lastModifiedTime:()Ljava/nio/file/attribute/FileTime;
        //   142: invokevirtual   java/nio/file/attribute/FileTime.toMillis:()J
        //   145: ldc2_w          1000
        //   148: ldiv           
        //   149: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //   152: invokestatic    dev/tenacity/ui/sidegui/utils/CloudDataUtils.getLastEditedTime:(Ljava/lang/String;)Ljava/lang/String;
        //   155: aload_0         /* this */
        //   156: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //   159: ldc             5.0
        //   161: fadd           
        //   162: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   165: aload_0         /* this */
        //   166: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.script:Ldev/tenacity/scripting/api/Script;
        //   169: invokevirtual   dev/tenacity/scripting/api/Script.getAuthor:()Ljava/lang/String;
        //   172: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   175: fadd           
        //   176: aload_0         /* this */
        //   177: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //   180: fload           yOffset
        //   182: fadd           
        //   183: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   186: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   189: i2f            
        //   190: fadd           
        //   191: aload_3         /* textColor */
        //   192: ldc             0.5
        //   194: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   197: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   200: aload_0         /* this */
        //   201: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //   204: aload_0         /* this */
        //   205: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //   208: aload_0         /* this */
        //   209: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.width:F
        //   212: aload_0         /* this */
        //   213: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.height:F
        //   216: iload_1         /* mouseX */
        //   217: iload_2         /* mouseY */
        //   218: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   221: istore          hovering
        //   223: aload_0         /* this */
        //   224: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   227: iload           hovering
        //   229: ifeq            238
        //   232: getstatic       dev/tenacity/utils/animations/Direction.FORWARDS:Ldev/tenacity/utils/animations/Direction;
        //   235: goto            241
        //   238: getstatic       dev/tenacity/utils/animations/Direction.BACKWARDS:Ldev/tenacity/utils/animations/Direction;
        //   241: invokevirtual   dev/tenacity/utils/animations/Animation.setDirection:(Ldev/tenacity/utils/animations/Direction;)Ldev/tenacity/utils/animations/Animation;
        //   244: pop            
        //   245: aload_0         /* this */
        //   246: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   249: iload           hovering
        //   251: ifeq            260
        //   254: sipush          150
        //   257: goto            263
        //   260: sipush          300
        //   263: invokevirtual   dev/tenacity/utils/animations/Animation.setDuration:(I)V
        //   266: aload_0         /* this */
        //   267: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.compact:Z
        //   270: ifne            349
        //   273: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   276: aload_0         /* this */
        //   277: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.script:Ldev/tenacity/scripting/api/Script;
        //   280: invokevirtual   dev/tenacity/scripting/api/Script.getDescription:()Ljava/lang/String;
        //   283: aload_0         /* this */
        //   284: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //   287: ldc             3.0
        //   289: fadd           
        //   290: aload_0         /* this */
        //   291: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //   294: ldc             6.0
        //   296: fadd           
        //   297: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   300: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   303: i2f            
        //   304: fadd           
        //   305: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   308: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   311: i2f            
        //   312: fadd           
        //   313: aload_3         /* textColor */
        //   314: invokevirtual   java/awt/Color.getRGB:()I
        //   317: ldc             0.5
        //   319: ldc             0.5
        //   321: aload_0         /* this */
        //   322: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   325: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   328: invokevirtual   java/lang/Double.floatValue:()F
        //   331: fmul           
        //   332: fadd           
        //   333: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(IF)I
        //   336: aload_0         /* this */
        //   337: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.width:F
        //   340: ldc             12.0
        //   342: fsub           
        //   343: ldc             3.0
        //   345: invokevirtual   dev/tenacity/utils/font/CustomFont.drawWrappedText:(Ljava/lang/String;FFIFF)F
        //   348: pop            
        //   349: iconst_0       
        //   350: istore          seperationX
        //   352: aload_0         /* this */
        //   353: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.buttons:Ljava/util/List;
        //   356: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   361: astore          7
        //   363: aload           7
        //   365: invokeinterface java/util/Iterator.hasNext:()Z
        //   370: ifeq            534
        //   373: aload           7
        //   375: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   380: checkcast       Ldev/tenacity/ui/sidegui/utils/IconButton;
        //   383: astore          button
        //   385: aload           button
        //   387: aload_0         /* this */
        //   388: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.x:F
        //   391: aload_0         /* this */
        //   392: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.width:F
        //   395: fadd           
        //   396: aload           button
        //   398: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   401: ldc             4.0
        //   403: fadd           
        //   404: iload           seperationX
        //   406: i2f            
        //   407: fadd           
        //   408: fsub           
        //   409: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setX:(F)V
        //   412: aload           button
        //   414: aload_0         /* this */
        //   415: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.y:F
        //   418: aload_0         /* this */
        //   419: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.height:F
        //   422: fadd           
        //   423: aload           button
        //   425: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getHeight:()F
        //   428: ldc             4.0
        //   430: fadd           
        //   431: fsub           
        //   432: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setY:(F)V
        //   435: aload           button
        //   437: aload_0         /* this */
        //   438: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.alpha:F
        //   441: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAlpha:(F)V
        //   444: aload           button
        //   446: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   449: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setIconFont:(Ldev/tenacity/utils/font/CustomFont;)V
        //   452: ldc             "q"
        //   454: aload           button
        //   456: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getIcon:()Ljava/lang/String;
        //   459: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   462: ifeq            487
        //   465: aload           button
        //   467: new             Ljava/awt/Color;
        //   470: dup            
        //   471: sipush          209
        //   474: bipush          56
        //   476: bipush          56
        //   478: invokespecial   java/awt/Color.<init>:(III)V
        //   481: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAccentColor:(Ljava/awt/Color;)V
        //   484: goto            496
        //   487: aload           button
        //   489: aload_0         /* this */
        //   490: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.accentColor:Ljava/awt/Color;
        //   493: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAccentColor:(Ljava/awt/Color;)V
        //   496: aload           button
        //   498: aload_0         /* this */
        //   499: aload           button
        //   501: invokedynamic   BootstrapMethod #0, run:(Ldev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect;Ldev/tenacity/ui/sidegui/utils/IconButton;)Ljava/lang/Runnable;
        //   506: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setClickAction:(Ljava/lang/Runnable;)V
        //   509: aload           button
        //   511: iload_1         /* mouseX */
        //   512: iload_2         /* mouseY */
        //   513: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.drawScreen:(II)V
        //   516: iload           seperationX
        //   518: i2f            
        //   519: ldc             8.0
        //   521: aload           button
        //   523: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   526: fadd           
        //   527: fadd           
        //   528: f2i            
        //   529: istore          seperationX
        //   531: goto            363
        //   534: aload_0         /* this */
        //   535: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.bfa:Ljava/nio/file/attribute/BasicFileAttributes;
        //   538: ifnull          651
        //   541: ldc             "§a"
        //   543: astore          formatCode
        //   545: aload_0         /* this */
        //   546: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   549: new             Ljava/lang/StringBuilder;
        //   552: dup            
        //   553: invokespecial   java/lang/StringBuilder.<init>:()V
        //   556: aload           formatCode
        //   558: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   561: ldc             "Last Modified§r: "
        //   563: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   566: aload_0         /* this */
        //   567: new             Ljava/util/Date;
        //   570: dup            
        //   571: aload_0         /* this */
        //   572: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.bfa:Ljava/nio/file/attribute/BasicFileAttributes;
        //   575: invokeinterface java/nio/file/attribute/BasicFileAttributes.lastModifiedTime:()Ljava/nio/file/attribute/FileTime;
        //   580: invokevirtual   java/nio/file/attribute/FileTime.toMillis:()J
        //   583: invokespecial   java/util/Date.<init>:(J)V
        //   586: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getCurrentTimeStamp:(Ljava/util/Date;)Ljava/lang/String;
        //   589: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   592: ldc             "\n"
        //   594: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   597: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   600: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setTip:(Ljava/lang/String;)V
        //   603: aload_0         /* this */
        //   604: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   607: aload_0         /* this */
        //   608: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.compact:Z
        //   611: ifeq            647
        //   614: new             Ljava/lang/StringBuilder;
        //   617: dup            
        //   618: invokespecial   java/lang/StringBuilder.<init>:()V
        //   621: aload           formatCode
        //   623: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   626: ldc             "Description§r: "
        //   628: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   631: aload_0         /* this */
        //   632: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.script:Ldev/tenacity/scripting/api/Script;
        //   635: invokevirtual   dev/tenacity/scripting/api/Script.getDescription:()Ljava/lang/String;
        //   638: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   641: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   644: goto            648
        //   647: aconst_null    
        //   648: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setAdditionalInformation:(Ljava/lang/String;)V
        //   651: aload_0         /* this */
        //   652: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getX:()F
        //   655: ldc             3.0
        //   657: fadd           
        //   658: aload_0         /* this */
        //   659: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getY:()F
        //   662: aload_0         /* this */
        //   663: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getHeight:()F
        //   666: fadd           
        //   667: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   670: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   673: iconst_3       
        //   674: iadd           
        //   675: i2f            
        //   676: fsub           
        //   677: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   680: ldc             "m"
        //   682: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   685: fconst_2       
        //   686: fadd           
        //   687: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   690: ldc             "Hover for more information"
        //   692: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   695: fadd           
        //   696: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   699: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   702: iconst_3       
        //   703: iadd           
        //   704: i2f            
        //   705: iload_1         /* mouseX */
        //   706: iload_2         /* mouseY */
        //   707: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   710: istore          hoveringInfo
        //   712: aload_0         /* this */
        //   713: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   716: iload           hoveringInfo
        //   718: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setHovering:(Z)V
        //   721: aload_0         /* this */
        //   722: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   725: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.getFadeInAnimation:()Ldev/tenacity/utils/animations/Animation;
        //   728: astore          hoverAnim
        //   730: ldc             0.65
        //   732: aload           hoverAnim
        //   734: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   737: invokevirtual   java/lang/Double.floatValue:()F
        //   740: fmul           
        //   741: fstore          additionalAlpha
        //   743: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   746: ldc             "m"
        //   748: aload_0         /* this */
        //   749: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getX:()F
        //   752: ldc             3.0
        //   754: fadd           
        //   755: aload_0         /* this */
        //   756: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getY:()F
        //   759: aload_0         /* this */
        //   760: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getHeight:()F
        //   763: fadd           
        //   764: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   767: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   770: iconst_3       
        //   771: iadd           
        //   772: i2f            
        //   773: fsub           
        //   774: aload_3         /* textColor */
        //   775: ldc             0.35
        //   777: fload           additionalAlpha
        //   779: fadd           
        //   780: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   783: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   786: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   789: ldc             "Hover for more information"
        //   791: aload_0         /* this */
        //   792: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getX:()F
        //   795: ldc             5.0
        //   797: fadd           
        //   798: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   801: ldc             "m"
        //   803: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   806: fadd           
        //   807: aload_0         /* this */
        //   808: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getY:()F
        //   811: aload_0         /* this */
        //   812: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.getHeight:()F
        //   815: fadd           
        //   816: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/LocalScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   819: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   822: iconst_3       
        //   823: iadd           
        //   824: i2f            
        //   825: fsub           
        //   826: aload_3         /* textColor */
        //   827: ldc             0.35
        //   829: fload           additionalAlpha
        //   831: fadd           
        //   832: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   835: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   838: return         
        //    StackMapTable: 00 0F FC 00 51 07 00 CF 40 02 FC 00 75 02 FF 00 25 00 06 07 00 B5 01 01 07 00 CF 02 01 00 01 07 00 D0 FF 00 02 00 06 07 00 B5 01 01 07 00 CF 02 01 00 02 07 00 D0 07 00 D1 52 07 00 D0 FF 00 02 00 06 07 00 B5 01 01 07 00 CF 02 01 00 02 07 00 D0 01 FB 00 55 FD 00 0D 01 07 00 D2 FC 00 7B 07 00 D3 08 F9 00 25 FF 00 70 00 08 07 00 B5 01 01 07 00 CF 02 01 01 07 00 D4 00 01 07 00 D5 FF 00 00 00 08 07 00 B5 01 01 07 00 CF 02 01 01 07 00 D4 00 02 07 00 D5 07 00 D4 FA 00 02
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
        if (this.clickable) {
            this.buttons.forEach(button1 -> button1.mouseClicked(mouseX, mouseY, button));
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public String getCurrentTimeStamp(final Date date) {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
        return sdfDate.format(date);
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
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public Color getAccentColor() {
        return this.accentColor;
    }
    
    public boolean isClickable() {
        return this.clickable;
    }
    
    public boolean isCompact() {
        return this.compact;
    }
    
    public BasicFileAttributes getBfa() {
        return this.bfa;
    }
    
    public TooltipObject getHoverInformation() {
        return this.hoverInformation;
    }
    
    public List<IconButton> getButtons() {
        return this.buttons;
    }
    
    public Script getScript() {
        return this.script;
    }
    
    public Animation getHoverAnimation() {
        return this.hoverAnimation;
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
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
    
    public void setAccentColor(final Color accentColor) {
        this.accentColor = accentColor;
    }
    
    public void setClickable(final boolean clickable) {
        this.clickable = clickable;
    }
    
    public void setCompact(final boolean compact) {
        this.compact = compact;
    }
    
    public void setBfa(final BasicFileAttributes bfa) {
        this.bfa = bfa;
    }
}

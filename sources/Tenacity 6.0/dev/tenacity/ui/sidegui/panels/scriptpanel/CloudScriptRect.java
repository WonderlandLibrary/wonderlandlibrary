// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.scriptpanel;

import dev.tenacity.ui.sidegui.forms.Form;
import com.google.gson.JsonObject;
import dev.tenacity.utils.misc.FileUtils;
import dev.tenacity.scripting.api.Script;
import dev.tenacity.ui.sidegui.forms.impl.EditForm;
import dev.tenacity.utils.misc.Multithreading;
import net.minecraft.client.Minecraft;
import dev.tenacity.intent.cloud.CloudUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import java.io.File;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.misc.IOUtils;
import org.lwjgl.input.Keyboard;
import dev.tenacity.Tenacity;
import dev.tenacity.intent.cloud.data.CloudData;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.ArrayList;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.intent.cloud.data.CloudScript;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import dev.tenacity.ui.sidegui.utils.IconButton;
import java.util.List;
import dev.tenacity.ui.sidegui.utils.VoteRect;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class CloudScriptRect implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private Color accentColor;
    private boolean compact;
    private int searchScore;
    private boolean clickable;
    private final VoteRect voteRect;
    private final List<IconButton> iconButtons;
    private final TooltipObject hoverInformation;
    private final CloudScript script;
    private final Animation hoverAnimation;
    
    public CloudScriptRect(final CloudScript script) {
        this.clickable = true;
        this.iconButtons = new ArrayList<IconButton>();
        this.hoverInformation = new TooltipObject();
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.script = script;
        this.voteRect = new VoteRect(script);
        Tenacity.INSTANCE.getSideGui().getTooltips().add(this.hoverInformation);
        this.iconButtons.add(new IconButton("E", "Download and add this script to local scripts"));
        this.iconButtons.add(new IconButton("A", "Edit this script"));
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
        //     4: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.alpha:F
        //     7: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //    10: astore_3        /* textColor */
        //    11: aload_0         /* this */
        //    12: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //    15: aload_0         /* this */
        //    16: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //    19: aload_0         /* this */
        //    20: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.width:F
        //    23: aload_0         /* this */
        //    24: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.height:F
        //    27: ldc             5.0
        //    29: bipush          37
        //    31: aload_0         /* this */
        //    32: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.alpha:F
        //    35: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //    38: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //    41: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityBoldFont26:Ldev/tenacity/utils/font/CustomFont;
        //    44: aload_0         /* this */
        //    45: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //    48: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getName:()Ljava/lang/String;
        //    51: aload_0         /* this */
        //    52: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //    55: ldc             3.0
        //    57: fadd           
        //    58: aload_0         /* this */
        //    59: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //    62: ldc             3.0
        //    64: fadd           
        //    65: aload_3         /* textColor */
        //    66: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //    69: aload_0         /* this */
        //    70: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.compact:Z
        //    73: ifeq            81
        //    76: ldc             2.5
        //    78: goto            82
        //    81: fconst_2       
        //    82: fstore          yOffset
        //    84: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //    87: aload_0         /* this */
        //    88: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //    91: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getAuthor:()Ljava/lang/String;
        //    94: aload_0         /* this */
        //    95: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //    98: ldc             3.0
        //   100: fadd           
        //   101: aload_0         /* this */
        //   102: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //   105: fload           yOffset
        //   107: fadd           
        //   108: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   111: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   114: i2f            
        //   115: fadd           
        //   116: aload_0         /* this */
        //   117: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.accentColor:Ljava/awt/Color;
        //   120: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   123: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   126: aload_0         /* this */
        //   127: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //   130: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getLastUpdated:()Ljava/lang/String;
        //   133: invokestatic    dev/tenacity/ui/sidegui/utils/CloudDataUtils.getLastEditedTime:(Ljava/lang/String;)Ljava/lang/String;
        //   136: aload_0         /* this */
        //   137: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //   140: ldc             5.0
        //   142: fadd           
        //   143: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   146: aload_0         /* this */
        //   147: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //   150: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getAuthor:()Ljava/lang/String;
        //   153: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   156: fadd           
        //   157: aload_0         /* this */
        //   158: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //   161: fload           yOffset
        //   163: fadd           
        //   164: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   167: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   170: i2f            
        //   171: fadd           
        //   172: aload_3         /* textColor */
        //   173: ldc             0.5
        //   175: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   178: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   181: aload_0         /* this */
        //   182: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //   185: aload_0         /* this */
        //   186: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //   189: aload_0         /* this */
        //   190: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.width:F
        //   193: aload_0         /* this */
        //   194: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.height:F
        //   197: iload_1         /* mouseX */
        //   198: iload_2         /* mouseY */
        //   199: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   202: istore          hovering
        //   204: aload_0         /* this */
        //   205: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   208: iload           hovering
        //   210: ifeq            219
        //   213: getstatic       dev/tenacity/utils/animations/Direction.FORWARDS:Ldev/tenacity/utils/animations/Direction;
        //   216: goto            222
        //   219: getstatic       dev/tenacity/utils/animations/Direction.BACKWARDS:Ldev/tenacity/utils/animations/Direction;
        //   222: invokevirtual   dev/tenacity/utils/animations/Animation.setDirection:(Ldev/tenacity/utils/animations/Direction;)Ldev/tenacity/utils/animations/Animation;
        //   225: pop            
        //   226: aload_0         /* this */
        //   227: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   230: iload           hovering
        //   232: ifeq            241
        //   235: sipush          150
        //   238: goto            244
        //   241: sipush          300
        //   244: invokevirtual   dev/tenacity/utils/animations/Animation.setDuration:(I)V
        //   247: aload_0         /* this */
        //   248: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.compact:Z
        //   251: ifne            330
        //   254: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   257: aload_0         /* this */
        //   258: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //   261: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getDescription:()Ljava/lang/String;
        //   264: aload_0         /* this */
        //   265: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //   268: ldc             3.0
        //   270: fadd           
        //   271: aload_0         /* this */
        //   272: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //   275: ldc             6.0
        //   277: fadd           
        //   278: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   281: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   284: i2f            
        //   285: fadd           
        //   286: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   289: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   292: i2f            
        //   293: fadd           
        //   294: aload_3         /* textColor */
        //   295: invokevirtual   java/awt/Color.getRGB:()I
        //   298: ldc             0.5
        //   300: ldc             0.5
        //   302: aload_0         /* this */
        //   303: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   306: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   309: invokevirtual   java/lang/Double.floatValue:()F
        //   312: fmul           
        //   313: fadd           
        //   314: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(IF)I
        //   317: aload_0         /* this */
        //   318: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.width:F
        //   321: ldc             12.0
        //   323: fsub           
        //   324: ldc             3.0
        //   326: invokevirtual   dev/tenacity/utils/font/CustomFont.drawWrappedText:(Ljava/lang/String;FFIFF)F
        //   329: pop            
        //   330: aload_0         /* this */
        //   331: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   334: aload_0         /* this */
        //   335: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getAlpha:()F
        //   338: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setAlpha:(F)V
        //   341: aload_0         /* this */
        //   342: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   345: aload_0         /* this */
        //   346: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //   349: aload_0         /* this */
        //   350: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.width:F
        //   353: fadd           
        //   354: aload_0         /* this */
        //   355: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   358: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.getWidth:()F
        //   361: ldc             4.0
        //   363: fadd           
        //   364: fsub           
        //   365: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setX:(F)V
        //   368: aload_0         /* this */
        //   369: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   372: aload_0         /* this */
        //   373: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //   376: ldc             4.0
        //   378: fadd           
        //   379: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setY:(F)V
        //   382: aload_0         /* this */
        //   383: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   386: aload_0         /* this */
        //   387: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getAccentColor:()Ljava/awt/Color;
        //   390: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setAccentColor:(Ljava/awt/Color;)V
        //   393: aload_0         /* this */
        //   394: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   397: iload_1         /* mouseX */
        //   398: iload_2         /* mouseY */
        //   399: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.drawScreen:(II)V
        //   402: aload_0         /* this */
        //   403: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.compact:Z
        //   406: ifeq            414
        //   409: ldc             20.0
        //   411: goto            416
        //   414: ldc             4.0
        //   416: fstore          buttonOffsetX
        //   418: aload_0         /* this */
        //   419: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.compact:Z
        //   422: ifeq            430
        //   425: ldc             3.0
        //   427: goto            432
        //   430: ldc             4.0
        //   432: fstore          buttonOffsetY
        //   434: iconst_0       
        //   435: istore          seperation
        //   437: aload_0         /* this */
        //   438: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.iconButtons:Ljava/util/List;
        //   441: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   446: astore          9
        //   448: aload           9
        //   450: invokeinterface java/util/Iterator.hasNext:()Z
        //   455: ifeq            626
        //   458: aload           9
        //   460: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   465: checkcast       Ldev/tenacity/ui/sidegui/utils/IconButton;
        //   468: astore          iconButton
        //   470: aload           iconButton
        //   472: aload_0         /* this */
        //   473: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.x:F
        //   476: aload_0         /* this */
        //   477: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.width:F
        //   480: fadd           
        //   481: aload           iconButton
        //   483: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   486: fload           buttonOffsetX
        //   488: fadd           
        //   489: iload           seperation
        //   491: i2f            
        //   492: fadd           
        //   493: fsub           
        //   494: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setX:(F)V
        //   497: aload           iconButton
        //   499: aload_0         /* this */
        //   500: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.y:F
        //   503: aload_0         /* this */
        //   504: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getHeight:()F
        //   507: fadd           
        //   508: aload           iconButton
        //   510: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getHeight:()F
        //   513: fload           buttonOffsetY
        //   515: fadd           
        //   516: fsub           
        //   517: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setY:(F)V
        //   520: aload           iconButton
        //   522: aload_0         /* this */
        //   523: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getAlpha:()F
        //   526: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAlpha:(F)V
        //   529: aload           iconButton
        //   531: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   534: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setIconFont:(Ldev/tenacity/utils/font/CustomFont;)V
        //   537: aload           iconButton
        //   539: aload_0         /* this */
        //   540: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getAccentColor:()Ljava/awt/Color;
        //   543: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAccentColor:(Ljava/awt/Color;)V
        //   546: aload           iconButton
        //   548: aload_0         /* this */
        //   549: aload           iconButton
        //   551: invokedynamic   BootstrapMethod #0, run:(Ldev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect;Ldev/tenacity/ui/sidegui/utils/IconButton;)Ljava/lang/Runnable;
        //   556: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setClickAction:(Ljava/lang/Runnable;)V
        //   559: aload           iconButton
        //   561: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getIcon:()Ljava/lang/String;
        //   564: ldc             "A"
        //   566: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   569: ifeq            601
        //   572: aload_0         /* this */
        //   573: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //   576: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.isOwnership:()Z
        //   579: ifeq            592
        //   582: aload           iconButton
        //   584: iload_1         /* mouseX */
        //   585: iload_2         /* mouseY */
        //   586: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.drawScreen:(II)V
        //   589: goto            608
        //   592: aload           iconButton
        //   594: iconst_0       
        //   595: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setClickable:(Z)V
        //   598: goto            608
        //   601: aload           iconButton
        //   603: iload_1         /* mouseX */
        //   604: iload_2         /* mouseY */
        //   605: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.drawScreen:(II)V
        //   608: iload           seperation
        //   610: i2f            
        //   611: aload           iconButton
        //   613: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   616: ldc             8.0
        //   618: fadd           
        //   619: fadd           
        //   620: f2i            
        //   621: istore          seperation
        //   623: goto            448
        //   626: ldc             "§a"
        //   628: astore          formatCode
        //   630: aload_0         /* this */
        //   631: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   634: new             Ljava/lang/StringBuilder;
        //   637: dup            
        //   638: invokespecial   java/lang/StringBuilder.<init>:()V
        //   641: aload           formatCode
        //   643: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   646: ldc             "Share Code§r: "
        //   648: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   651: aload_0         /* this */
        //   652: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //   655: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getShareCode:()Ljava/lang/String;
        //   658: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   661: ldc             "\n"
        //   663: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   666: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   669: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setTip:(Ljava/lang/String;)V
        //   672: aload_0         /* this */
        //   673: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   676: aload_0         /* this */
        //   677: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.compact:Z
        //   680: ifeq            716
        //   683: new             Ljava/lang/StringBuilder;
        //   686: dup            
        //   687: invokespecial   java/lang/StringBuilder.<init>:()V
        //   690: aload           formatCode
        //   692: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   695: ldc             "Description§r: "
        //   697: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   700: aload_0         /* this */
        //   701: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.script:Ldev/tenacity/intent/cloud/data/CloudScript;
        //   704: invokevirtual   dev/tenacity/intent/cloud/data/CloudScript.getDescription:()Ljava/lang/String;
        //   707: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   710: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   713: goto            717
        //   716: aconst_null    
        //   717: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setAdditionalInformation:(Ljava/lang/String;)V
        //   720: aload_0         /* this */
        //   721: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getX:()F
        //   724: ldc             3.0
        //   726: fadd           
        //   727: aload_0         /* this */
        //   728: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getY:()F
        //   731: aload_0         /* this */
        //   732: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getHeight:()F
        //   735: fadd           
        //   736: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   739: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   742: iconst_3       
        //   743: iadd           
        //   744: i2f            
        //   745: fsub           
        //   746: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   749: ldc             "m"
        //   751: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   754: fconst_2       
        //   755: fadd           
        //   756: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   759: ldc             "Hover for more information"
        //   761: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   764: fadd           
        //   765: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   768: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   771: iconst_3       
        //   772: iadd           
        //   773: i2f            
        //   774: iload_1         /* mouseX */
        //   775: iload_2         /* mouseY */
        //   776: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   779: istore          hoveringInfo
        //   781: aload_0         /* this */
        //   782: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   785: iload           hoveringInfo
        //   787: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setHovering:(Z)V
        //   790: aload_0         /* this */
        //   791: getfield        dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   794: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.getFadeInAnimation:()Ldev/tenacity/utils/animations/Animation;
        //   797: astore          hoverAnim
        //   799: ldc             0.65
        //   801: aload           hoverAnim
        //   803: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   806: invokevirtual   java/lang/Double.floatValue:()F
        //   809: fmul           
        //   810: fstore          additionalAlpha
        //   812: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   815: ldc             "m"
        //   817: aload_0         /* this */
        //   818: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getX:()F
        //   821: ldc             3.0
        //   823: fadd           
        //   824: aload_0         /* this */
        //   825: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getY:()F
        //   828: aload_0         /* this */
        //   829: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getHeight:()F
        //   832: fadd           
        //   833: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   836: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   839: iconst_3       
        //   840: iadd           
        //   841: i2f            
        //   842: fsub           
        //   843: aload_3         /* textColor */
        //   844: ldc             0.35
        //   846: fload           additionalAlpha
        //   848: fadd           
        //   849: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   852: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   855: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   858: ldc             "Hover for more information"
        //   860: aload_0         /* this */
        //   861: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getX:()F
        //   864: ldc             5.0
        //   866: fadd           
        //   867: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   870: ldc             "m"
        //   872: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   875: fadd           
        //   876: aload_0         /* this */
        //   877: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getY:()F
        //   880: aload_0         /* this */
        //   881: invokevirtual   dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.getHeight:()F
        //   884: fadd           
        //   885: getstatic       dev/tenacity/ui/sidegui/panels/scriptpanel/CloudScriptRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   888: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   891: iconst_3       
        //   892: iadd           
        //   893: i2f            
        //   894: fsub           
        //   895: aload_3         /* textColor */
        //   896: ldc             0.35
        //   898: fload           additionalAlpha
        //   900: fadd           
        //   901: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   904: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   907: return         
        //    StackMapTable: 00 12 FC 00 51 07 00 F8 40 02 FF 00 88 00 06 07 00 F9 01 01 07 00 F8 02 01 00 01 07 00 FA FF 00 02 00 06 07 00 F9 01 01 07 00 F8 02 01 00 02 07 00 FA 07 00 FB 52 07 00 FA FF 00 02 00 06 07 00 F9 01 01 07 00 F8 02 01 00 02 07 00 FA 01 FB 00 55 FB 00 53 41 02 FC 00 0D 02 41 02 FE 00 0F 02 01 07 00 FC FC 00 8F 07 00 FD 08 06 F9 00 11 FF 00 59 00 0A 07 00 F9 01 01 07 00 F8 02 01 02 02 01 07 00 FE 00 01 07 00 FF FF 00 00 00 0A 07 00 F9 01 01 07 00 F8 02 01 02 02 01 07 00 FE 00 02 07 00 FF 07 00 FE
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
            if (button == 0 && this.hoverInformation.isHovering() && Keyboard.isKeyDown(42)) {
                IOUtils.copy(this.script.getShareCode());
                NotificationManager.post(NotificationType.SUCCESS, "Success", "Script share-code copied to clipboard!");
                return;
            }
            this.voteRect.mouseClicked(mouseX, mouseY, button);
            this.iconButtons.forEach(iconButton -> iconButton.mouseClicked(mouseX, mouseY, button));
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void downloadScriptToFile(final File file, final String content) {
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            NotificationManager.post(NotificationType.SUCCESS, "Success", "Script downloaded to " + file.getPath(), 7.0f);
        }
        catch (IOException e) {
            e.printStackTrace();
            NotificationManager.post(NotificationType.DISABLE, "Error", "Could not download script to " + file.getAbsolutePath(), 7.0f);
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
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public Color getAccentColor() {
        return this.accentColor;
    }
    
    public boolean isCompact() {
        return this.compact;
    }
    
    public int getSearchScore() {
        return this.searchScore;
    }
    
    public boolean isClickable() {
        return this.clickable;
    }
    
    public VoteRect getVoteRect() {
        return this.voteRect;
    }
    
    public List<IconButton> getIconButtons() {
        return this.iconButtons;
    }
    
    public TooltipObject getHoverInformation() {
        return this.hoverInformation;
    }
    
    public CloudScript getScript() {
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
    
    public void setCompact(final boolean compact) {
        this.compact = compact;
    }
    
    public void setSearchScore(final int searchScore) {
        this.searchScore = searchScore;
    }
    
    public void setClickable(final boolean clickable) {
        this.clickable = clickable;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.configpanel;

import dev.tenacity.ui.sidegui.forms.Form;
import com.google.gson.JsonObject;
import dev.tenacity.ui.sidegui.forms.impl.EditForm;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.intent.cloud.CloudUtils;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.misc.IOUtils;
import org.lwjgl.input.Keyboard;
import dev.tenacity.Tenacity;
import dev.tenacity.intent.cloud.data.CloudData;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.ArrayList;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.intent.cloud.data.CloudConfig;
import dev.tenacity.ui.sidegui.utils.TooltipObject;
import dev.tenacity.ui.sidegui.utils.IconButton;
import java.util.List;
import dev.tenacity.ui.sidegui.utils.VoteRect;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class CloudConfigRect implements Screen
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
    private final CloudConfig config;
    private final Animation hoverAnimation;
    
    public CloudConfigRect(final CloudConfig config) {
        this.clickable = true;
        this.iconButtons = new ArrayList<IconButton>();
        this.hoverInformation = new TooltipObject();
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.config = config;
        this.voteRect = new VoteRect(config);
        Tenacity.INSTANCE.getSideGui().getTooltips().add(this.hoverInformation);
        this.iconButtons.add(new IconButton("t", "Load this config"));
        this.iconButtons.add(new IconButton("u", "Save this config to your local files"));
        this.iconButtons.add(new IconButton("A", "Edit this config"));
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
        //     4: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.alpha:F
        //     7: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //    10: astore_3        /* textColor */
        //    11: aload_0         /* this */
        //    12: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //    15: aload_0         /* this */
        //    16: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //    19: aload_0         /* this */
        //    20: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.width:F
        //    23: aload_0         /* this */
        //    24: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.height:F
        //    27: ldc             5.0
        //    29: bipush          37
        //    31: aload_0         /* this */
        //    32: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.alpha:F
        //    35: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //    38: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //    41: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityBoldFont26:Ldev/tenacity/utils/font/CustomFont;
        //    44: aload_0         /* this */
        //    45: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //    48: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getName:()Ljava/lang/String;
        //    51: aload_0         /* this */
        //    52: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //    55: ldc             3.0
        //    57: fadd           
        //    58: aload_0         /* this */
        //    59: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //    62: ldc             3.0
        //    64: fadd           
        //    65: aload_3         /* textColor */
        //    66: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //    69: aload_0         /* this */
        //    70: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.compact:Z
        //    73: ifeq            81
        //    76: ldc             2.5
        //    78: goto            82
        //    81: fconst_2       
        //    82: fstore          yOffset
        //    84: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //    87: aload_0         /* this */
        //    88: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //    91: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getAuthor:()Ljava/lang/String;
        //    94: aload_0         /* this */
        //    95: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //    98: ldc             3.0
        //   100: fadd           
        //   101: aload_0         /* this */
        //   102: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //   105: fload           yOffset
        //   107: fadd           
        //   108: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   111: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   114: i2f            
        //   115: fadd           
        //   116: aload_0         /* this */
        //   117: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.accentColor:Ljava/awt/Color;
        //   120: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   123: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   126: aload_0         /* this */
        //   127: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   130: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getLastUpdated:()Ljava/lang/String;
        //   133: invokestatic    dev/tenacity/ui/sidegui/utils/CloudDataUtils.getLastEditedTime:(Ljava/lang/String;)Ljava/lang/String;
        //   136: aload_0         /* this */
        //   137: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //   140: ldc             5.0
        //   142: fadd           
        //   143: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   146: aload_0         /* this */
        //   147: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   150: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getAuthor:()Ljava/lang/String;
        //   153: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   156: fadd           
        //   157: aload_0         /* this */
        //   158: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //   161: fload           yOffset
        //   163: fadd           
        //   164: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   167: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   170: i2f            
        //   171: fadd           
        //   172: aload_3         /* textColor */
        //   173: ldc             0.5
        //   175: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   178: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   181: aload_0         /* this */
        //   182: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //   185: aload_0         /* this */
        //   186: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //   189: aload_0         /* this */
        //   190: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.width:F
        //   193: aload_0         /* this */
        //   194: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.height:F
        //   197: iload_1         /* mouseX */
        //   198: iload_2         /* mouseY */
        //   199: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   202: istore          hovering
        //   204: aload_0         /* this */
        //   205: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   208: iload           hovering
        //   210: ifeq            219
        //   213: getstatic       dev/tenacity/utils/animations/Direction.FORWARDS:Ldev/tenacity/utils/animations/Direction;
        //   216: goto            222
        //   219: getstatic       dev/tenacity/utils/animations/Direction.BACKWARDS:Ldev/tenacity/utils/animations/Direction;
        //   222: invokevirtual   dev/tenacity/utils/animations/Animation.setDirection:(Ldev/tenacity/utils/animations/Direction;)Ldev/tenacity/utils/animations/Animation;
        //   225: pop            
        //   226: aload_0         /* this */
        //   227: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   230: iload           hovering
        //   232: ifeq            241
        //   235: sipush          150
        //   238: goto            244
        //   241: sipush          300
        //   244: invokevirtual   dev/tenacity/utils/animations/Animation.setDuration:(I)V
        //   247: aload_0         /* this */
        //   248: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.compact:Z
        //   251: ifne            330
        //   254: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   257: aload_0         /* this */
        //   258: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   261: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getDescription:()Ljava/lang/String;
        //   264: aload_0         /* this */
        //   265: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //   268: ldc             3.0
        //   270: fadd           
        //   271: aload_0         /* this */
        //   272: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //   275: ldc             6.0
        //   277: fadd           
        //   278: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   281: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   284: i2f            
        //   285: fadd           
        //   286: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   289: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   292: i2f            
        //   293: fadd           
        //   294: aload_3         /* textColor */
        //   295: invokevirtual   java/awt/Color.getRGB:()I
        //   298: ldc             0.5
        //   300: ldc             0.5
        //   302: aload_0         /* this */
        //   303: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverAnimation:Ldev/tenacity/utils/animations/Animation;
        //   306: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   309: invokevirtual   java/lang/Double.floatValue:()F
        //   312: fmul           
        //   313: fadd           
        //   314: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(IF)I
        //   317: aload_0         /* this */
        //   318: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.width:F
        //   321: ldc             12.0
        //   323: fsub           
        //   324: ldc             3.0
        //   326: invokevirtual   dev/tenacity/utils/font/CustomFont.drawWrappedText:(Ljava/lang/String;FFIFF)F
        //   329: pop            
        //   330: aload_0         /* this */
        //   331: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   334: aload_0         /* this */
        //   335: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getAlpha:()F
        //   338: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setAlpha:(F)V
        //   341: aload_0         /* this */
        //   342: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   345: aload_0         /* this */
        //   346: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //   349: aload_0         /* this */
        //   350: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.width:F
        //   353: fadd           
        //   354: aload_0         /* this */
        //   355: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   358: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.getWidth:()F
        //   361: ldc             4.0
        //   363: fadd           
        //   364: fsub           
        //   365: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setX:(F)V
        //   368: aload_0         /* this */
        //   369: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   372: aload_0         /* this */
        //   373: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //   376: ldc             4.0
        //   378: fadd           
        //   379: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setY:(F)V
        //   382: aload_0         /* this */
        //   383: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   386: aload_0         /* this */
        //   387: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getAccentColor:()Ljava/awt/Color;
        //   390: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.setAccentColor:(Ljava/awt/Color;)V
        //   393: aload_0         /* this */
        //   394: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.voteRect:Ldev/tenacity/ui/sidegui/utils/VoteRect;
        //   397: iload_1         /* mouseX */
        //   398: iload_2         /* mouseY */
        //   399: invokevirtual   dev/tenacity/ui/sidegui/utils/VoteRect.drawScreen:(II)V
        //   402: aload_0         /* this */
        //   403: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.compact:Z
        //   406: ifeq            414
        //   409: ldc             20.0
        //   411: goto            416
        //   414: ldc             4.0
        //   416: fstore          buttonOffsetX
        //   418: aload_0         /* this */
        //   419: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.compact:Z
        //   422: ifeq            430
        //   425: ldc             3.0
        //   427: goto            432
        //   430: ldc             4.0
        //   432: fstore          buttonOffsetY
        //   434: iconst_0       
        //   435: istore          seperationX
        //   437: aload_0         /* this */
        //   438: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.iconButtons:Ljava/util/List;
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
        //   473: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.x:F
        //   476: aload_0         /* this */
        //   477: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.width:F
        //   480: fadd           
        //   481: aload           iconButton
        //   483: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   486: fload           buttonOffsetX
        //   488: fadd           
        //   489: iload           seperationX
        //   491: i2f            
        //   492: fadd           
        //   493: fsub           
        //   494: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setX:(F)V
        //   497: aload           iconButton
        //   499: aload_0         /* this */
        //   500: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.y:F
        //   503: aload_0         /* this */
        //   504: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.height:F
        //   507: fadd           
        //   508: aload           iconButton
        //   510: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getHeight:()F
        //   513: fload           buttonOffsetY
        //   515: fadd           
        //   516: fsub           
        //   517: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setY:(F)V
        //   520: aload           iconButton
        //   522: aload_0         /* this */
        //   523: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getAlpha:()F
        //   526: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAlpha:(F)V
        //   529: aload           iconButton
        //   531: aload_0         /* this */
        //   532: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getAccentColor:()Ljava/awt/Color;
        //   535: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAccentColor:(Ljava/awt/Color;)V
        //   538: aload           iconButton
        //   540: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   543: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setIconFont:(Ldev/tenacity/utils/font/CustomFont;)V
        //   546: aload           iconButton
        //   548: aload_0         /* this */
        //   549: aload           iconButton
        //   551: invokedynamic   BootstrapMethod #0, run:(Ldev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect;Ldev/tenacity/ui/sidegui/utils/IconButton;)Ljava/lang/Runnable;
        //   556: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setClickAction:(Ljava/lang/Runnable;)V
        //   559: aload           iconButton
        //   561: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getIcon:()Ljava/lang/String;
        //   564: ldc             "A"
        //   566: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   569: ifeq            601
        //   572: aload_0         /* this */
        //   573: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   576: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.isOwnership:()Z
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
        //   608: iload           seperationX
        //   610: i2f            
        //   611: aload           iconButton
        //   613: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   616: ldc             7.0
        //   618: fadd           
        //   619: fadd           
        //   620: f2i            
        //   621: istore          seperationX
        //   623: goto            448
        //   626: ldc             "§a"
        //   628: astore          formatCode
        //   630: aload_0         /* this */
        //   631: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   634: new             Ljava/lang/StringBuilder;
        //   637: dup            
        //   638: invokespecial   java/lang/StringBuilder.<init>:()V
        //   641: aload           formatCode
        //   643: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   646: ldc             "Server IP§r: "
        //   648: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   651: aload_0         /* this */
        //   652: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   655: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getServer:()Ljava/lang/String;
        //   658: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   661: ldc             "\n"
        //   663: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   666: aload           formatCode
        //   668: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   671: ldc             "Client Version§r: "
        //   673: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   676: aload_0         /* this */
        //   677: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   680: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getVersion:()Ljava/lang/String;
        //   683: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   686: ldc             "\n"
        //   688: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   691: aload           formatCode
        //   693: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   696: ldc             "Share Code§r: "
        //   698: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   701: aload_0         /* this */
        //   702: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   705: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getShareCode:()Ljava/lang/String;
        //   708: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   711: ldc             "\n"
        //   713: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   716: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   719: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setTip:(Ljava/lang/String;)V
        //   722: aload_0         /* this */
        //   723: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   726: aload_0         /* this */
        //   727: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.compact:Z
        //   730: ifeq            766
        //   733: new             Ljava/lang/StringBuilder;
        //   736: dup            
        //   737: invokespecial   java/lang/StringBuilder.<init>:()V
        //   740: aload           formatCode
        //   742: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   745: ldc             "Description§r: "
        //   747: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   750: aload_0         /* this */
        //   751: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.config:Ldev/tenacity/intent/cloud/data/CloudConfig;
        //   754: invokevirtual   dev/tenacity/intent/cloud/data/CloudConfig.getDescription:()Ljava/lang/String;
        //   757: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   760: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   763: goto            767
        //   766: aconst_null    
        //   767: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setAdditionalInformation:(Ljava/lang/String;)V
        //   770: aload_0         /* this */
        //   771: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getX:()F
        //   774: ldc             3.0
        //   776: fadd           
        //   777: aload_0         /* this */
        //   778: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getY:()F
        //   781: aload_0         /* this */
        //   782: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getHeight:()F
        //   785: fadd           
        //   786: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   789: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   792: iconst_3       
        //   793: iadd           
        //   794: i2f            
        //   795: fsub           
        //   796: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   799: ldc             "m"
        //   801: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   804: fconst_2       
        //   805: fadd           
        //   806: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   809: ldc             "Hover for more information"
        //   811: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   814: fadd           
        //   815: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   818: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   821: iconst_3       
        //   822: iadd           
        //   823: i2f            
        //   824: iload_1         /* mouseX */
        //   825: iload_2         /* mouseY */
        //   826: invokestatic    dev/tenacity/ui/sidegui/SideGUI.isHovering:(FFFFII)Z
        //   829: istore          hoveringInfo
        //   831: aload_0         /* this */
        //   832: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   835: iload           hoveringInfo
        //   837: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.setHovering:(Z)V
        //   840: aload_0         /* this */
        //   841: getfield        dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.hoverInformation:Ldev/tenacity/ui/sidegui/utils/TooltipObject;
        //   844: invokevirtual   dev/tenacity/ui/sidegui/utils/TooltipObject.getFadeInAnimation:()Ldev/tenacity/utils/animations/Animation;
        //   847: astore          hoverAnim
        //   849: ldc             0.65
        //   851: aload           hoverAnim
        //   853: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   856: invokevirtual   java/lang/Double.floatValue:()F
        //   859: fmul           
        //   860: fstore          additionalAlpha
        //   862: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   865: ldc             "m"
        //   867: aload_0         /* this */
        //   868: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getX:()F
        //   871: ldc             3.0
        //   873: fadd           
        //   874: aload_0         /* this */
        //   875: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getY:()F
        //   878: aload_0         /* this */
        //   879: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getHeight:()F
        //   882: fadd           
        //   883: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   886: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   889: iconst_3       
        //   890: iadd           
        //   891: i2f            
        //   892: fsub           
        //   893: aload_3         /* textColor */
        //   894: ldc             0.35
        //   896: fload           additionalAlpha
        //   898: fadd           
        //   899: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   902: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   905: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   908: ldc             "Hover for more information"
        //   910: aload_0         /* this */
        //   911: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getX:()F
        //   914: ldc             5.0
        //   916: fadd           
        //   917: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.iconFont16:Ldev/tenacity/utils/font/CustomFont;
        //   920: ldc             "m"
        //   922: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   925: fadd           
        //   926: aload_0         /* this */
        //   927: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getY:()F
        //   930: aload_0         /* this */
        //   931: invokevirtual   dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.getHeight:()F
        //   934: fadd           
        //   935: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/CloudConfigRect.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   938: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   941: iconst_3       
        //   942: iadd           
        //   943: i2f            
        //   944: fsub           
        //   945: aload_3         /* textColor */
        //   946: ldc             0.35
        //   948: fload           additionalAlpha
        //   950: fadd           
        //   951: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   954: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   957: return         
        //    StackMapTable: 00 12 FC 00 51 07 00 E6 40 02 FF 00 88 00 06 07 00 E7 01 01 07 00 E6 02 01 00 01 07 00 E8 FF 00 02 00 06 07 00 E7 01 01 07 00 E6 02 01 00 02 07 00 E8 07 00 E9 52 07 00 E8 FF 00 02 00 06 07 00 E7 01 01 07 00 E6 02 01 00 02 07 00 E8 01 FB 00 55 FB 00 53 41 02 FC 00 0D 02 41 02 FE 00 0F 02 01 07 00 EA FC 00 8F 07 00 EB 08 06 F9 00 11 FF 00 8B 00 0A 07 00 E7 01 01 07 00 E6 02 01 02 02 01 07 00 EC 00 01 07 00 ED FF 00 00 00 0A 07 00 E7 01 01 07 00 E6 02 01 02 02 01 07 00 EC 00 02 07 00 ED 07 00 EC
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
                IOUtils.copy(this.config.getShareCode());
                NotificationManager.post(NotificationType.SUCCESS, "Success", "Config share-code copied to clipboard!");
                return;
            }
            this.voteRect.mouseClicked(mouseX, mouseY, button);
            this.iconButtons.forEach(iconButton -> iconButton.mouseClicked(mouseX, mouseY, button));
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
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
    
    public CloudConfig getConfig() {
        return this.config;
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

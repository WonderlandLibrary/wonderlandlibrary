// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.mainmenu;

import java.awt.Color;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.ui.altmanager.panels.LoginPanel;
import dev.tenacity.intent.cloud.Cloud;
import dev.tenacity.utils.misc.DiscordRPC;
import dev.tenacity.Tenacity;
import net.minecraft.util.Util;
import dev.tenacity.utils.misc.NetworkingUtils;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import dev.tenacity.ui.mainmenu.particles.ParticleEngine;
import net.minecraft.client.gui.GuiScreen;

public class CustomMainMenu extends GuiScreen
{
    private ParticleEngine particleEngine;
    public static boolean animatedOpen;
    private final List<MenuButton> buttons;
    private final List<TextButton> textButtons;
    private final ResourceLocation backgroundResource;
    private final ResourceLocation blurredRect;
    private final ResourceLocation hoverCircle;
    private static boolean firstInit;
    
    public CustomMainMenu() {
        this.buttons = (List<MenuButton>)new ArrayList() {
            {
                this.add(new MenuButton("Singleplayer"));
                this.add(new MenuButton("Multiplayer"));
                this.add(new MenuButton("Alt Manager"));
                this.add(new MenuButton("Settings"));
                this.add(new MenuButton("Exit"));
            }
        };
        this.textButtons = (List<TextButton>)new ArrayList() {
            {
                this.add(new TextButton("Scripting"));
                this.add(new TextButton("Discord"));
            }
        };
        this.backgroundResource = new ResourceLocation("Tenacity/MainMenu/funny.png");
        this.blurredRect = new ResourceLocation("Tenacity/MainMenu/rect-test.png");
        this.hoverCircle = new ResourceLocation("Tenacity/MainMenu/hover-circle.png");
    }
    
    @Override
    public void initGui() {
        if (!CustomMainMenu.firstInit) {
            NetworkingUtils.bypassSSL();
            if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                Tenacity.INSTANCE.setDiscordRPC(new DiscordRPC());
            }
            CustomMainMenu.firstInit = true;
        }
        if (this.particleEngine == null) {
            this.particleEngine = new ParticleEngine();
        }
        if (CustomMainMenu.mc.gameSettings.guiScale != 2) {
            Tenacity.prevGuiScale = CustomMainMenu.mc.gameSettings.guiScale;
            Tenacity.updateGuiScale = true;
            CustomMainMenu.mc.gameSettings.guiScale = 2;
            CustomMainMenu.mc.resize(CustomMainMenu.mc.displayWidth - 1, CustomMainMenu.mc.displayHeight);
            CustomMainMenu.mc.resize(CustomMainMenu.mc.displayWidth + 1, CustomMainMenu.mc.displayHeight);
        }
        this.buttons.forEach(MenuButton::initGui);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.mc:Lnet/minecraft/client/Minecraft;
        //     7: invokespecial   net/minecraft/client/gui/ScaledResolution.<init>:(Lnet/minecraft/client/Minecraft;)V
        //    10: astore          sr
        //    12: aload_0         /* this */
        //    13: aload           sr
        //    15: invokevirtual   net/minecraft/client/gui/ScaledResolution.getScaledWidth:()I
        //    18: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //    21: aload_0         /* this */
        //    22: aload           sr
        //    24: invokevirtual   net/minecraft/client/gui/ScaledResolution.getScaledHeight:()I
        //    27: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //    30: invokestatic    dev/tenacity/utils/render/RenderUtil.resetColor:()V
        //    33: aload_0         /* this */
        //    34: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.backgroundResource:Lnet/minecraft/util/ResourceLocation;
        //    37: fconst_0       
        //    38: fconst_0       
        //    39: aload_0         /* this */
        //    40: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //    43: i2f            
        //    44: aload_0         /* this */
        //    45: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //    48: i2f            
        //    49: invokestatic    dev/tenacity/utils/render/RenderUtil.drawImage:(Lnet/minecraft/util/ResourceLocation;FFFF)V
        //    52: aload_0         /* this */
        //    53: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.particleEngine:Ldev/tenacity/ui/mainmenu/particles/ParticleEngine;
        //    56: invokevirtual   dev/tenacity/ui/mainmenu/particles/ParticleEngine.render:()V
        //    59: ldc             277.0
        //    61: fstore          rectWidth
        //    63: ldc             275.5
        //    65: fstore          rectHeight
        //    67: invokestatic    dev/tenacity/utils/render/blur/GaussianBlur.startBlur:()V
        //    70: aload_0         /* this */
        //    71: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //    74: i2f            
        //    75: fconst_2       
        //    76: fdiv           
        //    77: fload           rectWidth
        //    79: fconst_2       
        //    80: fdiv           
        //    81: fsub           
        //    82: aload_0         /* this */
        //    83: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //    86: i2f            
        //    87: fconst_2       
        //    88: fdiv           
        //    89: fload           rectHeight
        //    91: fconst_2       
        //    92: fdiv           
        //    93: fsub           
        //    94: fload           rectWidth
        //    96: fload           rectHeight
        //    98: ldc             10.0
        //   100: getstatic       java/awt/Color.WHITE:Ljava/awt/Color;
        //   103: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   106: ldc             40.0
        //   108: fconst_2       
        //   109: invokestatic    dev/tenacity/utils/render/blur/GaussianBlur.endBlur:(FF)V
        //   112: ldc             344.0
        //   114: fstore          outlineImgWidth
        //   116: ldc             340.5
        //   118: fstore          outlineImgHeight
        //   120: invokestatic    dev/tenacity/utils/render/GLUtil.startBlend:()V
        //   123: iconst_m1      
        //   124: invokestatic    dev/tenacity/utils/render/RenderUtil.color:(I)V
        //   127: aload_0         /* this */
        //   128: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.blurredRect:Lnet/minecraft/util/ResourceLocation;
        //   131: aload_0         /* this */
        //   132: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //   135: i2f            
        //   136: fconst_2       
        //   137: fdiv           
        //   138: fload           outlineImgWidth
        //   140: fconst_2       
        //   141: fdiv           
        //   142: fsub           
        //   143: aload_0         /* this */
        //   144: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //   147: i2f            
        //   148: fconst_2       
        //   149: fdiv           
        //   150: fload           outlineImgHeight
        //   152: fconst_2       
        //   153: fdiv           
        //   154: fsub           
        //   155: fload           outlineImgWidth
        //   157: fload           outlineImgHeight
        //   159: invokestatic    dev/tenacity/utils/render/RenderUtil.drawImage:(Lnet/minecraft/util/ResourceLocation;FFFF)V
        //   162: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.animatedOpen:Z
        //   165: ifeq            168
        //   168: sipush          3042
        //   171: invokestatic    org/lwjgl/opengl/GL11.glEnable:(I)V
        //   174: invokestatic    dev/tenacity/utils/render/StencilUtil.initStencilToWrite:()V
        //   177: ldc             13.0
        //   179: invokestatic    dev/tenacity/utils/render/RenderUtil.setAlphaLimit:(F)V
        //   182: aload_0         /* this */
        //   183: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.buttons:Ljava/util/List;
        //   186: invokedynamic   BootstrapMethod #1, accept:()Ljava/util/function/Consumer;
        //   191: invokeinterface java/util/List.forEach:(Ljava/util/function/Consumer;)V
        //   196: fconst_0       
        //   197: invokestatic    dev/tenacity/utils/render/RenderUtil.setAlphaLimit:(F)V
        //   200: iconst_1       
        //   201: invokestatic    dev/tenacity/utils/render/StencilUtil.readStencilBuffer:(I)V
        //   204: ldc             87.0
        //   206: fstore          circleW
        //   208: ldc             70.0
        //   210: fstore          circleH
        //   212: new             Lnet/minecraft/util/ResourceLocation;
        //   215: dup            
        //   216: ldc             "Tenacity/MainMenu/circle-funny.png"
        //   218: invokespecial   net/minecraft/util/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   221: astore          rs
        //   223: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.mc:Lnet/minecraft/client/Minecraft;
        //   226: invokevirtual   net/minecraft/client/Minecraft.getTextureManager:()Lnet/minecraft/client/renderer/texture/TextureManager;
        //   229: aload           rs
        //   231: invokevirtual   net/minecraft/client/renderer/texture/TextureManager.bindTexture:(Lnet/minecraft/util/ResourceLocation;)V
        //   234: invokestatic    dev/tenacity/utils/render/GLUtil.startBlend:()V
        //   237: aload           rs
        //   239: iload_1         /* mouseX */
        //   240: i2f            
        //   241: fload           circleW
        //   243: fconst_2       
        //   244: fdiv           
        //   245: fsub           
        //   246: iload_2         /* mouseY */
        //   247: i2f            
        //   248: fload           circleH
        //   250: fconst_2       
        //   251: fdiv           
        //   252: fsub           
        //   253: fload           circleW
        //   255: fload           circleH
        //   257: invokestatic    dev/tenacity/utils/render/RenderUtil.drawImage:(Lnet/minecraft/util/ResourceLocation;FFFF)V
        //   260: invokestatic    dev/tenacity/utils/render/StencilUtil.uninitStencilBuffer:()V
        //   263: ldc             140.0
        //   265: fstore          buttonWidth
        //   267: ldc             25.0
        //   269: fstore          buttonHeight
        //   271: iconst_0       
        //   272: istore          count
        //   274: aload_0         /* this */
        //   275: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.buttons:Ljava/util/List;
        //   278: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   283: astore          15
        //   285: aload           15
        //   287: invokeinterface java/util/Iterator.hasNext:()Z
        //   292: ifeq            397
        //   295: aload           15
        //   297: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   302: checkcast       Ldev/tenacity/ui/mainmenu/MenuButton;
        //   305: astore          button
        //   307: aload           button
        //   309: aload_0         /* this */
        //   310: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //   313: i2f            
        //   314: fconst_2       
        //   315: fdiv           
        //   316: fload           buttonWidth
        //   318: fconst_2       
        //   319: fdiv           
        //   320: fsub           
        //   321: putfield        dev/tenacity/ui/mainmenu/MenuButton.x:F
        //   324: aload           button
        //   326: aload_0         /* this */
        //   327: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //   330: i2f            
        //   331: fconst_2       
        //   332: fdiv           
        //   333: fload           buttonHeight
        //   335: fconst_2       
        //   336: fdiv           
        //   337: fsub           
        //   338: ldc             25.0
        //   340: fsub           
        //   341: iload           count
        //   343: i2f            
        //   344: fadd           
        //   345: putfield        dev/tenacity/ui/mainmenu/MenuButton.y:F
        //   348: aload           button
        //   350: fload           buttonWidth
        //   352: putfield        dev/tenacity/ui/mainmenu/MenuButton.width:F
        //   355: aload           button
        //   357: fload           buttonHeight
        //   359: putfield        dev/tenacity/ui/mainmenu/MenuButton.height:F
        //   362: aload           button
        //   364: aload_0         /* this */
        //   365: aload           button
        //   367: invokedynamic   BootstrapMethod #2, run:(Ldev/tenacity/ui/mainmenu/CustomMainMenu;Ldev/tenacity/ui/mainmenu/MenuButton;)Ljava/lang/Runnable;
        //   372: putfield        dev/tenacity/ui/mainmenu/MenuButton.clickAction:Ljava/lang/Runnable;
        //   375: aload           button
        //   377: iload_1         /* mouseX */
        //   378: iload_2         /* mouseY */
        //   379: invokevirtual   dev/tenacity/ui/mainmenu/MenuButton.drawScreen:(II)V
        //   382: iload           count
        //   384: i2f            
        //   385: fload           buttonHeight
        //   387: ldc             5.0
        //   389: fadd           
        //   390: fadd           
        //   391: f2i            
        //   392: istore          count
        //   394: goto            285
        //   397: fconst_0       
        //   398: fstore          buttonCount
        //   400: aload_0         /* this */
        //   401: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.textButtons:Ljava/util/List;
        //   404: invokeinterface java/util/List.stream:()Ljava/util/stream/Stream;
        //   409: invokedynamic   BootstrapMethod #3, applyAsDouble:()Ljava/util/function/ToDoubleFunction;
        //   414: invokeinterface java/util/stream/Stream.mapToDouble:(Ljava/util/function/ToDoubleFunction;)Ljava/util/stream/DoubleStream;
        //   419: invokeinterface java/util/stream/DoubleStream.sum:()D
        //   424: d2f            
        //   425: fstore          buttonsWidth
        //   427: aload_0         /* this */
        //   428: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.textButtons:Ljava/util/List;
        //   431: invokeinterface java/util/List.size:()I
        //   436: istore          buttonsSize
        //   438: fload           buttonsWidth
        //   440: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   443: ldc             " | "
        //   445: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   448: iload           buttonsSize
        //   450: iconst_1       
        //   451: isub           
        //   452: i2f            
        //   453: fmul           
        //   454: fadd           
        //   455: fstore          buttonsWidth
        //   457: iconst_0       
        //   458: istore          buttonIncrement
        //   460: aload_0         /* this */
        //   461: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.textButtons:Ljava/util/List;
        //   464: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   469: astore          19
        //   471: aload           19
        //   473: invokeinterface java/util/Iterator.hasNext:()Z
        //   478: ifeq            698
        //   481: aload           19
        //   483: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   488: checkcast       Ldev/tenacity/ui/mainmenu/CustomMainMenu$TextButton;
        //   491: astore          button
        //   493: aload           button
        //   495: aload_0         /* this */
        //   496: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //   499: i2f            
        //   500: fconst_2       
        //   501: fdiv           
        //   502: fload           buttonsWidth
        //   504: fconst_2       
        //   505: fdiv           
        //   506: fsub           
        //   507: fload           buttonCount
        //   509: fadd           
        //   510: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.x:F
        //   513: aload           button
        //   515: aload_0         /* this */
        //   516: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //   519: i2f            
        //   520: fconst_2       
        //   521: fdiv           
        //   522: ldc             120.0
        //   524: fadd           
        //   525: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.y:F
        //   528: aload           button
        //   530: invokestatic    dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.access$000:(Ldev/tenacity/ui/mainmenu/CustomMainMenu$TextButton;)Ljava/lang/String;
        //   533: astore          21
        //   535: iconst_m1      
        //   536: istore          22
        //   538: aload           21
        //   540: invokevirtual   java/lang/String.hashCode:()I
        //   543: lookupswitch {
        //          -2041041129: 568
        //          -958933748: 584
        //          default: 597
        //        }
        //   568: aload           21
        //   570: ldc             "Scripting"
        //   572: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   575: ifeq            597
        //   578: iconst_0       
        //   579: istore          22
        //   581: goto            597
        //   584: aload           21
        //   586: ldc             "Discord"
        //   588: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   591: ifeq            597
        //   594: iconst_1       
        //   595: istore          22
        //   597: iload           22
        //   599: lookupswitch {
        //                0: 624
        //                1: 637
        //          default: 647
        //        }
        //   624: aload           button
        //   626: invokedynamic   BootstrapMethod #4, run:()Ljava/lang/Runnable;
        //   631: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.clickAction:Ljava/lang/Runnable;
        //   634: goto            647
        //   637: aload           button
        //   639: invokedynamic   BootstrapMethod #5, run:()Ljava/lang/Runnable;
        //   644: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.clickAction:Ljava/lang/Runnable;
        //   647: aload           button
        //   649: iload           buttonIncrement
        //   651: iload           buttonsSize
        //   653: iconst_1       
        //   654: isub           
        //   655: if_icmpeq       662
        //   658: iconst_1       
        //   659: goto            663
        //   662: iconst_0       
        //   663: putfield        dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.addToEnd:Z
        //   666: aload           button
        //   668: iload_1         /* mouseX */
        //   669: iload_2         /* mouseY */
        //   670: invokevirtual   dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.drawScreen:(II)V
        //   673: fload           buttonCount
        //   675: aload           button
        //   677: invokevirtual   dev/tenacity/ui/mainmenu/CustomMainMenu$TextButton.getWidth:()F
        //   680: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityFont14:Ldev/tenacity/utils/font/CustomFont;
        //   683: ldc             " | "
        //   685: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   688: fadd           
        //   689: fadd           
        //   690: fstore          buttonCount
        //   692: iinc            buttonIncrement, 1
        //   695: goto            471
        //   698: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityBoldFont80:Ldev/tenacity/utils/font/CustomFont;
        //   701: ldc             "Tenacity"
        //   703: aload_0         /* this */
        //   704: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //   707: i2f            
        //   708: fconst_2       
        //   709: fdiv           
        //   710: aload_0         /* this */
        //   711: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //   714: i2f            
        //   715: fconst_2       
        //   716: fdiv           
        //   717: ldc             110.0
        //   719: fsub           
        //   720: getstatic       java/awt/Color.WHITE:Ljava/awt/Color;
        //   723: invokevirtual   java/awt/Color.getRGB:()I
        //   726: invokevirtual   dev/tenacity/utils/font/CustomFont.drawCenteredString:(Ljava/lang/String;FFI)I
        //   729: pop            
        //   730: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityFont32:Ldev/tenacity/utils/font/CustomFont;
        //   733: ldc             "6.0"
        //   735: aload_0         /* this */
        //   736: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //   739: i2f            
        //   740: fconst_2       
        //   741: fdiv           
        //   742: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityBoldFont80:Ldev/tenacity/utils/font/CustomFont;
        //   745: ldc             "Tenacity"
        //   747: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   750: fconst_2       
        //   751: fdiv           
        //   752: fadd           
        //   753: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityFont32:Ldev/tenacity/utils/font/CustomFont;
        //   756: ldc             "6.0"
        //   758: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   761: fconst_2       
        //   762: fdiv           
        //   763: fsub           
        //   764: aload_0         /* this */
        //   765: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //   768: i2f            
        //   769: fconst_2       
        //   770: fdiv           
        //   771: ldc             113.0
        //   773: fsub           
        //   774: getstatic       java/awt/Color.WHITE:Ljava/awt/Color;
        //   777: invokevirtual   java/awt/Color.getRGB:()I
        //   780: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFI)I
        //   783: pop            
        //   784: getstatic       dev/tenacity/ui/mainmenu/CustomMainMenu.tenacityFont18:Ldev/tenacity/utils/font/CustomFont;
        //   787: ldc             "edited by Razzle"
        //   789: aload_0         /* this */
        //   790: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.width:I
        //   793: i2f            
        //   794: fconst_2       
        //   795: fdiv           
        //   796: aload_0         /* this */
        //   797: getfield        dev/tenacity/ui/mainmenu/CustomMainMenu.height:I
        //   800: i2f            
        //   801: fconst_2       
        //   802: fdiv           
        //   803: ldc             68.0
        //   805: fsub           
        //   806: getstatic       java/awt/Color.WHITE:Ljava/awt/Color;
        //   809: invokevirtual   java/awt/Color.getRGB:()I
        //   812: invokevirtual   dev/tenacity/utils/font/CustomFont.drawCenteredString:(Ljava/lang/String;FFI)I
        //   815: pop            
        //   816: return         
        //    StackMapTable: 00 0D FF 00 A8 00 09 07 00 C9 01 01 02 07 00 CA 02 02 02 02 00 00 FF 00 74 00 10 07 00 C9 01 01 02 07 00 CA 02 02 02 02 02 02 07 00 CB 02 02 01 07 00 CC 00 00 FA 00 6F FF 00 49 00 14 07 00 C9 01 01 02 07 00 CA 02 02 02 02 02 02 07 00 CB 02 02 01 02 02 01 01 07 00 CC 00 00 FE 00 60 07 00 CD 07 00 CE 01 0F 0C 1A 0C F9 00 09 4E 07 00 CD FF 00 00 00 15 07 00 C9 01 01 02 07 00 CA 02 02 02 02 02 02 07 00 CB 02 02 01 02 02 01 01 07 00 CC 07 00 CD 00 02 07 00 CD 01 F9 00 22
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
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        LoginPanel.cracked = (Cloud.getApiKey() == null);
        this.buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        this.textButtons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    @Override
    public void onGuiClosed() {
        if (Tenacity.updateGuiScale) {
            CustomMainMenu.mc.gameSettings.guiScale = Tenacity.prevGuiScale;
            Tenacity.updateGuiScale = false;
        }
    }
    
    static {
        CustomMainMenu.animatedOpen = false;
        CustomMainMenu.firstInit = false;
    }
    
    private static class TextButton implements Screen
    {
        public float x;
        public float y;
        private final float width;
        private final float height;
        public Runnable clickAction;
        private final String text;
        private final Animation hoverAnimation;
        public boolean addToEnd;
        
        public TextButton(final String text) {
            this.hoverAnimation = new DecelerateAnimation(150, 1.0);
            this.text = text;
            this.width = TextButton.tenacityFont16.getStringWidth(text);
            this.height = (float)TextButton.tenacityFont16.getHeight();
        }
        
        @Override
        public void initGui() {
        }
        
        @Override
        public void keyTyped(final char typedChar, final int keyCode) {
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY) {
            final boolean hovered = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
            this.hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
            TextButton.tenacityFont16.drawString(this.text, this.x, this.y - this.height / 2.0f * this.hoverAnimation.getOutput().floatValue(), Color.WHITE.getRGB());
            if (this.addToEnd) {
                TextButton.tenacityFont16.drawString(" | ", this.x + this.width, this.y, Color.WHITE.getRGB());
            }
        }
        
        @Override
        public void mouseClicked(final int mouseX, final int mouseY, final int button) {
            final boolean hovered = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
            if (hovered && button == 0) {
                this.clickAction.run();
            }
        }
        
        @Override
        public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        }
        
        public float getWidth() {
            return this.width;
        }
        
        public float getHeight() {
            return this.height;
        }
    }
}

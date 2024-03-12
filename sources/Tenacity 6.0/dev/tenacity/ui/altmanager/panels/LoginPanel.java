// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager.panels;

import dev.tenacity.ui.altmanager.helpers.AltManagerUtils;
import dev.tenacity.ui.altmanager.helpers.Alt;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.ArrayList;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.text.RandomStringGenerator;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.ui.sidegui.utils.ActionButton;
import java.util.List;
import dev.tenacity.ui.altmanager.Panel;

public class LoginPanel extends Panel
{
    private final List<ActionButton> actionButtons;
    public final List<TextField> textFields;
    RandomStringGenerator generator;
    public static boolean cracked;
    private boolean hoveringMicrosoft;
    private final Animation hoverMicrosoftAnim;
    
    public LoginPanel() {
        this.actionButtons = new ArrayList<ActionButton>();
        this.textFields = new ArrayList<TextField>();
        this.generator = new RandomStringGenerator.Builder().withinRange(97, 122).build();
        this.hoveringMicrosoft = false;
        this.hoverMicrosoftAnim = new DecelerateAnimation(250, 1.0);
        this.setHeight(200.0f);
        this.actionButtons.add(new ActionButton("Login"));
        this.actionButtons.add(new ActionButton("Add"));
        this.actionButtons.add(new ActionButton("Gen Cracked"));
        this.textFields.add(new TextField(LoginPanel.tenacityFont20));
        this.textFields.add(new TextField(LoginPanel.tenacityFont20));
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.textFields.forEach(textField -> textField.keyTyped(typedChar, keyCode));
        if (keyCode == 15) {
            final TextField username = this.textFields.get(0);
            final TextField pass = this.textFields.get(1);
            if (username.isFocused()) {
                username.setFocused(false);
                pass.setFocused(true);
                return;
            }
            if (pass.isFocused()) {
                pass.setFocused(false);
                username.setFocused(true);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iload_1         /* mouseX */
        //     2: iload_2         /* mouseY */
        //     3: invokespecial   dev/tenacity/ui/altmanager/Panel.drawScreen:(II)V
        //     6: aload_0         /* this */
        //     7: ldc             180.0
        //     9: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.setHeight:(F)V
        //    12: getstatic       dev/tenacity/ui/altmanager/panels/LoginPanel.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //    15: ldc             "Login"
        //    17: aload_0         /* this */
        //    18: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getX:()F
        //    21: aload_0         /* this */
        //    22: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getWidth:()F
        //    25: fconst_2       
        //    26: fdiv           
        //    27: fadd           
        //    28: aload_0         /* this */
        //    29: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getY:()F
        //    32: ldc             3.0
        //    34: fadd           
        //    35: iconst_m1      
        //    36: ldc             0.75
        //    38: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(IF)I
        //    41: invokevirtual   dev/tenacity/utils/font/CustomFont.drawCenteredString:(Ljava/lang/String;FFI)I
        //    44: pop            
        //    45: getstatic       java/awt/Color.WHITE:Ljava/awt/Color;
        //    48: fconst_0       
        //    49: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //    52: astore_3        /* noColor */
        //    53: iconst_0       
        //    54: istore          count
        //    56: bipush          8
        //    58: istore          spacing
        //    60: ldc             35.0
        //    62: fstore          diff
        //    64: aload_0         /* this */
        //    65: getfield        dev/tenacity/ui/altmanager/panels/LoginPanel.textFields:Ljava/util/List;
        //    68: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    73: astore          7
        //    75: aload           7
        //    77: invokeinterface java/util/Iterator.hasNext:()Z
        //    82: ifeq            217
        //    85: aload           7
        //    87: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    92: checkcast       Ldev/tenacity/utils/objects/TextField;
        //    95: astore          textField
        //    97: aload           textField
        //    99: aload_0         /* this */
        //   100: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getX:()F
        //   103: fload           diff
        //   105: fconst_2       
        //   106: fdiv           
        //   107: fadd           
        //   108: invokevirtual   dev/tenacity/utils/objects/TextField.setXPosition:(F)V
        //   111: aload           textField
        //   113: aload_0         /* this */
        //   114: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getY:()F
        //   117: ldc             35.0
        //   119: fadd           
        //   120: iload           count
        //   122: i2f            
        //   123: fadd           
        //   124: invokevirtual   dev/tenacity/utils/objects/TextField.setYPosition:(F)V
        //   127: aload           textField
        //   129: aload_0         /* this */
        //   130: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getWidth:()F
        //   133: fload           diff
        //   135: fsub           
        //   136: invokevirtual   dev/tenacity/utils/objects/TextField.setWidth:(F)V
        //   139: aload           textField
        //   141: ldc             22.0
        //   143: invokevirtual   dev/tenacity/utils/objects/TextField.setHeight:(F)V
        //   146: aload           textField
        //   148: iload           count
        //   150: ifne            158
        //   153: ldc             "Email or combo"
        //   155: goto            160
        //   158: ldc             "Password"
        //   160: invokevirtual   dev/tenacity/utils/objects/TextField.setBackgroundText:(Ljava/lang/String;)V
        //   163: aload           textField
        //   165: aload_3         /* noColor */
        //   166: invokevirtual   dev/tenacity/utils/objects/TextField.setOutline:(Ljava/awt/Color;)V
        //   169: aload           textField
        //   171: bipush          17
        //   173: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(I)Ljava/awt/Color;
        //   176: invokevirtual   dev/tenacity/utils/objects/TextField.setFill:(Ljava/awt/Color;)V
        //   179: aload           textField
        //   181: ldc             0.35
        //   183: invokevirtual   dev/tenacity/utils/objects/TextField.setTextAlpha:(F)V
        //   186: aload           textField
        //   188: bipush          60
        //   190: invokevirtual   dev/tenacity/utils/objects/TextField.setMaxStringLength:(I)V
        //   193: aload           textField
        //   195: invokevirtual   dev/tenacity/utils/objects/TextField.drawTextBox:()V
        //   198: iload           count
        //   200: i2f            
        //   201: aload           textField
        //   203: invokevirtual   dev/tenacity/utils/objects/TextField.getHeight:()F
        //   206: iload           spacing
        //   208: i2f            
        //   209: fadd           
        //   210: fadd           
        //   211: f2i            
        //   212: istore          count
        //   214: goto            75
        //   217: aload_0         /* this */
        //   218: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getY:()F
        //   221: ldc             98.0
        //   223: fadd           
        //   224: fstore          actionY
        //   226: ldc             90.0
        //   228: fstore          actionWidth
        //   230: ldc             10.0
        //   232: fstore          buttonSpacing
        //   234: aload_0         /* this */
        //   235: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getX:()F
        //   238: aload_0         /* this */
        //   239: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getWidth:()F
        //   242: fconst_2       
        //   243: fdiv           
        //   244: fadd           
        //   245: aload_0         /* this */
        //   246: getfield        dev/tenacity/ui/altmanager/panels/LoginPanel.actionButtons:Ljava/util/List;
        //   249: invokeinterface java/util/List.size:()I
        //   254: i2f            
        //   255: fload           actionWidth
        //   257: fmul           
        //   258: ldc             20.0
        //   260: fadd           
        //   261: fconst_2       
        //   262: fdiv           
        //   263: fsub           
        //   264: fstore          firstX
        //   266: iconst_0       
        //   267: istore          seperation
        //   269: aload_0         /* this */
        //   270: getfield        dev/tenacity/ui/altmanager/panels/LoginPanel.actionButtons:Ljava/util/List;
        //   273: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   278: astore          12
        //   280: aload           12
        //   282: invokeinterface java/util/Iterator.hasNext:()Z
        //   287: ifeq            399
        //   290: aload           12
        //   292: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   297: checkcast       Ldev/tenacity/ui/sidegui/utils/ActionButton;
        //   300: astore          actionButton
        //   302: aload           actionButton
        //   304: iconst_1       
        //   305: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setBypass:(Z)V
        //   308: aload           actionButton
        //   310: bipush          55
        //   312: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(I)Ljava/awt/Color;
        //   315: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setColor:(Ljava/awt/Color;)V
        //   318: aload           actionButton
        //   320: fconst_1       
        //   321: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setAlpha:(F)V
        //   324: aload           actionButton
        //   326: fload           firstX
        //   328: iload           seperation
        //   330: i2f            
        //   331: fadd           
        //   332: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setX:(F)V
        //   335: aload           actionButton
        //   337: fload           actionY
        //   339: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setY:(F)V
        //   342: aload           actionButton
        //   344: fload           actionWidth
        //   346: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setWidth:(F)V
        //   349: aload           actionButton
        //   351: ldc             20.0
        //   353: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setHeight:(F)V
        //   356: aload           actionButton
        //   358: getstatic       dev/tenacity/ui/altmanager/panels/LoginPanel.tenacityBoldFont22:Ldev/tenacity/utils/font/CustomFont;
        //   361: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setFont:(Ldev/tenacity/utils/font/CustomFont;)V
        //   364: aload           actionButton
        //   366: aload_0         /* this */
        //   367: aload           actionButton
        //   369: invokedynamic   BootstrapMethod #1, run:(Ldev/tenacity/ui/altmanager/panels/LoginPanel;Ldev/tenacity/ui/sidegui/utils/ActionButton;)Ljava/lang/Runnable;
        //   374: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.setClickAction:(Ljava/lang/Runnable;)V
        //   377: aload           actionButton
        //   379: iload_1         /* mouseX */
        //   380: iload_2         /* mouseY */
        //   381: invokevirtual   dev/tenacity/ui/sidegui/utils/ActionButton.drawScreen:(II)V
        //   384: iload           seperation
        //   386: i2f            
        //   387: fload           actionWidth
        //   389: fload           buttonSpacing
        //   391: fadd           
        //   392: fadd           
        //   393: f2i            
        //   394: istore          seperation
        //   396: goto            280
        //   399: fload           actionY
        //   401: ldc             35.0
        //   403: fadd           
        //   404: fstore          microsoftY
        //   406: ldc             240.0
        //   408: fstore          microWidth
        //   410: ldc             35.0
        //   412: fstore          microHeight
        //   414: aload_0         /* this */
        //   415: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getX:()F
        //   418: aload_0         /* this */
        //   419: invokevirtual   dev/tenacity/ui/altmanager/panels/LoginPanel.getWidth:()F
        //   422: fconst_2       
        //   423: fdiv           
        //   424: fadd           
        //   425: fload           microWidth
        //   427: fconst_2       
        //   428: fdiv           
        //   429: fsub           
        //   430: fstore          microX
        //   432: aload_0         /* this */
        //   433: fload           microX
        //   435: fconst_2       
        //   436: fsub           
        //   437: fload           microsoftY
        //   439: fconst_2       
        //   440: fsub           
        //   441: fload           microWidth
        //   443: ldc             4.0
        //   445: fadd           
        //   446: fload           microHeight
        //   448: ldc             4.0
        //   450: fadd           
        //   451: iload_1         /* mouseX */
        //   452: iload_2         /* mouseY */
        //   453: invokestatic    dev/tenacity/utils/misc/HoveringUtil.isHovering:(FFFFII)Z
        //   456: putfield        dev/tenacity/ui/altmanager/panels/LoginPanel.hoveringMicrosoft:Z
        //   459: aload_0         /* this */
        //   460: getfield        dev/tenacity/ui/altmanager/panels/LoginPanel.hoverMicrosoftAnim:Ldev/tenacity/utils/animations/Animation;
        //   463: aload_0         /* this */
        //   464: getfield        dev/tenacity/ui/altmanager/panels/LoginPanel.hoveringMicrosoft:Z
        //   467: ifeq            476
        //   470: getstatic       dev/tenacity/utils/animations/Direction.FORWARDS:Ldev/tenacity/utils/animations/Direction;
        //   473: goto            479
        //   476: getstatic       dev/tenacity/utils/animations/Direction.BACKWARDS:Ldev/tenacity/utils/animations/Direction;
        //   479: invokevirtual   dev/tenacity/utils/animations/Animation.setDirection:(Ldev/tenacity/utils/animations/Direction;)Ldev/tenacity/utils/animations/Animation;
        //   482: pop            
        //   483: getstatic       dev/tenacity/ui/altmanager/panels/LoginPanel.mc:Lnet/minecraft/client/Minecraft;
        //   486: invokevirtual   net/minecraft/client/Minecraft.getTextureManager:()Lnet/minecraft/client/renderer/texture/TextureManager;
        //   489: new             Lnet/minecraft/util/ResourceLocation;
        //   492: dup            
        //   493: ldc             "Tenacity/mc.png"
        //   495: invokespecial   net/minecraft/util/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   498: invokevirtual   net/minecraft/client/renderer/texture/TextureManager.bindTexture:(Lnet/minecraft/util/ResourceLocation;)V
        //   501: fload           microX
        //   503: fload           microsoftY
        //   505: fload           microWidth
        //   507: fload           microHeight
        //   509: ldc             5.0
        //   511: fconst_1       
        //   512: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRoundTextured:(FFFFFF)V
        //   515: fload           microX
        //   517: fload           microsoftY
        //   519: fload           microWidth
        //   521: fload           microHeight
        //   523: ldc             5.0
        //   525: getstatic       java/awt/Color.BLACK:Ljava/awt/Color;
        //   528: ldc             0.2
        //   530: ldc             0.25
        //   532: aload_0         /* this */
        //   533: getfield        dev/tenacity/ui/altmanager/panels/LoginPanel.hoverMicrosoftAnim:Ldev/tenacity/utils/animations/Animation;
        //   536: invokevirtual   dev/tenacity/utils/animations/Animation.getOutput:()Ljava/lang/Double;
        //   539: invokevirtual   java/lang/Double.floatValue:()F
        //   542: fmul           
        //   543: fadd           
        //   544: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   547: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //   550: getstatic       dev/tenacity/ui/altmanager/panels/LoginPanel.tenacityBoldFont26:Ldev/tenacity/utils/font/CustomFont;
        //   553: ldc             "Microsoft Login"
        //   555: fload           microX
        //   557: ldc             10.0
        //   559: fadd           
        //   560: fload           microsoftY
        //   562: ldc             4.0
        //   564: fadd           
        //   565: iconst_m1      
        //   566: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFI)I
        //   569: pop            
        //   570: getstatic       dev/tenacity/ui/altmanager/panels/LoginPanel.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //   573: ldc             "Login to your migrated account"
        //   575: fload           microX
        //   577: ldc             10.0
        //   579: fadd           
        //   580: fload           microsoftY
        //   582: ldc             23.0
        //   584: fadd           
        //   585: iconst_m1      
        //   586: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFI)I
        //   589: pop            
        //   590: ldc             22.0
        //   592: fstore          logoSize
        //   594: fload           microX
        //   596: fload           microWidth
        //   598: fadd           
        //   599: ldc             10.0
        //   601: fload           logoSize
        //   603: fadd           
        //   604: fsub           
        //   605: fload           microsoftY
        //   607: fload           microHeight
        //   609: fconst_2       
        //   610: fdiv           
        //   611: fadd           
        //   612: fload           logoSize
        //   614: fconst_2       
        //   615: fdiv           
        //   616: fsub           
        //   617: fload           logoSize
        //   619: ldc             1.5
        //   621: invokestatic    dev/tenacity/utils/render/RenderUtil.drawMicrosoftLogo:(FFFF)V
        //   624: return         
        //    StackMapTable: 00 08 FF 00 4B 00 08 07 00 C3 01 01 07 00 C4 01 01 02 07 00 C5 00 00 FF 00 52 00 09 07 00 C3 01 01 07 00 C4 01 01 02 07 00 C5 07 00 AB 00 01 07 00 AB FF 00 01 00 09 07 00 C3 01 01 07 00 C4 01 01 02 07 00 C5 07 00 AB 00 02 07 00 AB 07 00 C6 F9 00 38 FF 00 3E 00 0D 07 00 C3 01 01 07 00 C4 01 01 02 02 02 02 02 01 07 00 C5 00 00 FA 00 76 FF 00 4C 00 10 07 00 C3 01 01 07 00 C4 01 01 02 02 02 02 02 01 02 02 02 02 00 01 07 00 C7 FF 00 02 00 10 07 00 C3 01 01 07 00 C4 01 01 02 02 02 02 02 01 02 02 02 02 00 02 07 00 C7 07 00 C8
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
        this.textFields.forEach(textField -> textField.mouseClicked(mouseX, mouseY, button));
        this.actionButtons.forEach(actionButton -> actionButton.mouseClicked(mouseX, mouseY, button));
        if (this.hoveringMicrosoft && button == 0) {
            final TextField username = this.textFields.get(0);
            String email = username.getText();
            String password = this.textFields.get(1).getText();
            if (email.contains(":")) {
                final String[] split = email.split(":");
                if (split.length != 2) {
                    return;
                }
                email = split[0];
                password = split[1];
            }
            Tenacity.INSTANCE.getAltManager().getUtils().microsoftLoginAsync(email, password);
            this.resetTextFields();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    private void resetTextFields() {
        this.textFields.forEach(textField -> textField.setText(""));
    }
    
    static {
        LoginPanel.cracked = false;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.configpanel;

import dev.tenacity.ui.sidegui.forms.Form;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.misc.FileUtils;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.misc.IOUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import dev.tenacity.config.LocalConfig;
import dev.tenacity.ui.sidegui.utils.IconButton;
import java.util.List;
import java.nio.file.attribute.BasicFileAttributes;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class LocalConfigRect implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private Color accentColor;
    private boolean clickable;
    private BasicFileAttributes bfa;
    private final List<IconButton> buttons;
    private final LocalConfig config;
    
    public LocalConfigRect(final LocalConfig config) {
        this.clickable = true;
        this.bfa = null;
        this.buttons = new ArrayList<IconButton>();
        this.config = config;
        this.buttons.add(new IconButton("t", "Load this config"));
        this.buttons.add(new IconButton("u", "Update this config"));
        this.buttons.add(new IconButton("C", "Upload this config"));
        this.buttons.add(new IconButton("q", "Delete this config"));
        try {
            this.bfa = Files.readAttributes(config.getFile().toPath(), BasicFileAttributes.class, new LinkOption[0]);
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
        //     4: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.alpha:F
        //     7: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //    10: astore_3        /* textColor */
        //    11: aload_0         /* this */
        //    12: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.x:F
        //    15: aload_0         /* this */
        //    16: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.y:F
        //    19: aload_0         /* this */
        //    20: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.width:F
        //    23: aload_0         /* this */
        //    24: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.height:F
        //    27: ldc             5.0
        //    29: bipush          37
        //    31: aload_0         /* this */
        //    32: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.alpha:F
        //    35: invokestatic    dev/tenacity/utils/render/ColorUtil.tripleColor:(IF)Ljava/awt/Color;
        //    38: invokestatic    dev/tenacity/utils/render/RoundedUtil.drawRound:(FFFFFLjava/awt/Color;)V
        //    41: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.tenacityBoldFont26:Ldev/tenacity/utils/font/CustomFont;
        //    44: aload_0         /* this */
        //    45: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.config:Ldev/tenacity/config/LocalConfig;
        //    48: invokevirtual   dev/tenacity/config/LocalConfig.getName:()Ljava/lang/String;
        //    51: aload_0         /* this */
        //    52: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.x:F
        //    55: ldc             3.0
        //    57: fadd           
        //    58: aload_0         /* this */
        //    59: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.y:F
        //    62: ldc             3.0
        //    64: fadd           
        //    65: aload_3         /* textColor */
        //    66: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //    69: aload_0         /* this */
        //    70: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.bfa:Ljava/nio/file/attribute/BasicFileAttributes;
        //    73: ifnull          132
        //    76: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.tenacityFont16:Ldev/tenacity/utils/font/CustomFont;
        //    79: aload_0         /* this */
        //    80: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.bfa:Ljava/nio/file/attribute/BasicFileAttributes;
        //    83: invokeinterface java/nio/file/attribute/BasicFileAttributes.lastModifiedTime:()Ljava/nio/file/attribute/FileTime;
        //    88: invokevirtual   java/nio/file/attribute/FileTime.toMillis:()J
        //    91: ldc2_w          1000
        //    94: ldiv           
        //    95: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //    98: invokestatic    dev/tenacity/ui/sidegui/utils/CloudDataUtils.getLastEditedTime:(Ljava/lang/String;)Ljava/lang/String;
        //   101: aload_0         /* this */
        //   102: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.x:F
        //   105: ldc             4.0
        //   107: fadd           
        //   108: aload_0         /* this */
        //   109: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.y:F
        //   112: ldc             2.5
        //   114: fadd           
        //   115: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.tenacityBoldFont32:Ldev/tenacity/utils/font/CustomFont;
        //   118: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   121: i2f            
        //   122: fadd           
        //   123: aload_3         /* textColor */
        //   124: ldc             0.5
        //   126: invokestatic    dev/tenacity/utils/render/ColorUtil.applyOpacity:(Ljava/awt/Color;F)Ljava/awt/Color;
        //   129: invokevirtual   dev/tenacity/utils/font/CustomFont.drawString:(Ljava/lang/String;FFLjava/awt/Color;)V
        //   132: iconst_0       
        //   133: istore          seperationX
        //   135: aload_0         /* this */
        //   136: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.buttons:Ljava/util/List;
        //   139: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   144: astore          5
        //   146: aload           5
        //   148: invokeinterface java/util/Iterator.hasNext:()Z
        //   153: ifeq            317
        //   156: aload           5
        //   158: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   163: checkcast       Ldev/tenacity/ui/sidegui/utils/IconButton;
        //   166: astore          button
        //   168: aload           button
        //   170: aload_0         /* this */
        //   171: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.x:F
        //   174: aload_0         /* this */
        //   175: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.width:F
        //   178: fadd           
        //   179: aload           button
        //   181: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   184: ldc             4.0
        //   186: fadd           
        //   187: iload           seperationX
        //   189: i2f            
        //   190: fadd           
        //   191: fsub           
        //   192: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setX:(F)V
        //   195: aload           button
        //   197: aload_0         /* this */
        //   198: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.y:F
        //   201: aload_0         /* this */
        //   202: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.height:F
        //   205: fadd           
        //   206: aload           button
        //   208: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getHeight:()F
        //   211: ldc             4.0
        //   213: fadd           
        //   214: fsub           
        //   215: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setY:(F)V
        //   218: aload           button
        //   220: aload_0         /* this */
        //   221: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.alpha:F
        //   224: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAlpha:(F)V
        //   227: aload           button
        //   229: getstatic       dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.iconFont20:Ldev/tenacity/utils/font/CustomFont;
        //   232: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setIconFont:(Ldev/tenacity/utils/font/CustomFont;)V
        //   235: ldc             "q"
        //   237: aload           button
        //   239: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getIcon:()Ljava/lang/String;
        //   242: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   245: ifeq            270
        //   248: aload           button
        //   250: new             Ljava/awt/Color;
        //   253: dup            
        //   254: sipush          209
        //   257: bipush          56
        //   259: bipush          56
        //   261: invokespecial   java/awt/Color.<init>:(III)V
        //   264: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAccentColor:(Ljava/awt/Color;)V
        //   267: goto            279
        //   270: aload           button
        //   272: aload_0         /* this */
        //   273: getfield        dev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect.accentColor:Ljava/awt/Color;
        //   276: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setAccentColor:(Ljava/awt/Color;)V
        //   279: aload           button
        //   281: aload_0         /* this */
        //   282: aload           button
        //   284: invokedynamic   BootstrapMethod #0, run:(Ldev/tenacity/ui/sidegui/panels/configpanel/LocalConfigRect;Ldev/tenacity/ui/sidegui/utils/IconButton;)Ljava/lang/Runnable;
        //   289: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.setClickAction:(Ljava/lang/Runnable;)V
        //   292: aload           button
        //   294: iload_1         /* mouseX */
        //   295: iload_2         /* mouseY */
        //   296: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.drawScreen:(II)V
        //   299: iload           seperationX
        //   301: i2f            
        //   302: ldc             8.0
        //   304: aload           button
        //   306: invokevirtual   dev/tenacity/ui/sidegui/utils/IconButton.getWidth:()F
        //   309: fadd           
        //   310: fadd           
        //   311: f2i            
        //   312: istore          seperationX
        //   314: goto            146
        //   317: return         
        //    StackMapTable: 00 05 FC 00 84 07 00 A0 FD 00 0D 01 07 00 A1 FC 00 7B 07 00 A2 08 F9 00 25
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
    
    public void setBfa(final BasicFileAttributes bfa) {
        this.bfa = bfa;
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
    
    public List<IconButton> getButtons() {
        return this.buttons;
    }
    
    public LocalConfig getConfig() {
        return this.config;
    }
    
    public BasicFileAttributes getBfa() {
        return this.bfa;
    }
}

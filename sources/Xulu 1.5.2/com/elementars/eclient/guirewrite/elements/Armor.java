// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import net.minecraft.client.Minecraft;
import dev.xulu.settings.OnChangedValue;
import java.util.Iterator;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.ColourHolder;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class Armor extends Element
{
    private final Value<String> aligned;
    private final Value<Boolean> progress;
    private final Value<Integer> w;
    private final Value<Integer> h;
    private final Value<Boolean> cf;
    private final Value<Boolean> damage;
    private final Value<Boolean> fixed;
    private static RenderItem itemRender;
    
    public Armor() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "Armor"
        //     3: invokespecial   com/elementars/eclient/guirewrite/Element.<init>:(Ljava/lang/String;)V
        //     6: aload_0         /* this */
        //     7: aload_0         /* this */
        //     8: new             Ldev/xulu/settings/Value;
        //    11: dup            
        //    12: ldc             "Aligned"
        //    14: aload_0         /* this */
        //    15: ldc             "X-axis"
        //    17: iconst_2       
        //    18: anewarray       Ljava/lang/String;
        //    21: dup            
        //    22: iconst_0       
        //    23: ldc             "X-axis"
        //    25: aastore        
        //    26: dup            
        //    27: iconst_1       
        //    28: ldc             "Y-axis"
        //    30: aastore        
        //    31: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;[Ljava/lang/Object;)V
        //    34: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //    37: aload_0         /* this */
        //    38: invokedynamic   BootstrapMethod #0, accept:(Lcom/elementars/eclient/guirewrite/elements/Armor;)Ljava/util/function/Consumer;
        //    43: invokevirtual   dev/xulu/settings/Value.onChanged:(Ljava/util/function/Consumer;)Ldev/xulu/settings/Value;
        //    46: putfield        com/elementars/eclient/guirewrite/elements/Armor.aligned:Ldev/xulu/settings/Value;
        //    49: aload_0         /* this */
        //    50: aload_0         /* this */
        //    51: new             Ldev/xulu/settings/Value;
        //    54: dup            
        //    55: ldc             "Durab. Bar"
        //    57: aload_0         /* this */
        //    58: iconst_0       
        //    59: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    62: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;)V
        //    65: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //    68: putfield        com/elementars/eclient/guirewrite/elements/Armor.progress:Ldev/xulu/settings/Value;
        //    71: aload_0         /* this */
        //    72: aload_0         /* this */
        //    73: new             Ldev/xulu/settings/Value;
        //    76: dup            
        //    77: ldc             "Width"
        //    79: aload_0         /* this */
        //    80: iconst_0       
        //    81: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    84: iconst_0       
        //    85: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    88: bipush          100
        //    90: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    93: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    96: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //    99: putfield        com/elementars/eclient/guirewrite/elements/Armor.w:Ldev/xulu/settings/Value;
        //   102: aload_0         /* this */
        //   103: aload_0         /* this */
        //   104: new             Ldev/xulu/settings/Value;
        //   107: dup            
        //   108: ldc             "Height"
        //   110: aload_0         /* this */
        //   111: iconst_0       
        //   112: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   115: iconst_0       
        //   116: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   119: bipush          100
        //   121: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   124: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   127: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //   130: putfield        com/elementars/eclient/guirewrite/elements/Armor.h:Ldev/xulu/settings/Value;
        //   133: aload_0         /* this */
        //   134: aload_0         /* this */
        //   135: new             Ldev/xulu/settings/Value;
        //   138: dup            
        //   139: ldc             "Custom Font"
        //   141: aload_0         /* this */
        //   142: iconst_0       
        //   143: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   146: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;)V
        //   149: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //   152: putfield        com/elementars/eclient/guirewrite/elements/Armor.cf:Ldev/xulu/settings/Value;
        //   155: aload_0         /* this */
        //   156: aload_0         /* this */
        //   157: new             Ldev/xulu/settings/Value;
        //   160: dup            
        //   161: ldc             "Damage"
        //   163: aload_0         /* this */
        //   164: iconst_0       
        //   165: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   168: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;)V
        //   171: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //   174: putfield        com/elementars/eclient/guirewrite/elements/Armor.damage:Ldev/xulu/settings/Value;
        //   177: aload_0         /* this */
        //   178: aload_0         /* this */
        //   179: new             Ldev/xulu/settings/Value;
        //   182: dup            
        //   183: ldc             "Fixed"
        //   185: aload_0         /* this */
        //   186: iconst_0       
        //   187: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   190: invokespecial   dev/xulu/settings/Value.<init>:(Ljava/lang/String;Lcom/elementars/eclient/module/Module;Ljava/lang/Object;)V
        //   193: invokevirtual   com/elementars/eclient/guirewrite/elements/Armor.register:(Ldev/xulu/settings/Value;)Ldev/xulu/settings/Value;
        //   196: putfield        com/elementars/eclient/guirewrite/elements/Armor.fixed:Ldev/xulu/settings/Value;
        //   199: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    public void onEnable() {
        final String s = this.aligned.getValue();
        switch (s) {
            case "X-axis": {
                this.height = 16.0;
                this.width = 79.0;
                break;
            }
            case "Y-axis": {
                this.height = 72.0;
                this.width = 16.0;
                break;
            }
        }
    }
    
    @Override
    public void onRender() {
        GlStateManager.enableTexture2D();
        final ScaledResolution resolution = new ScaledResolution(Armor.mc);
        final int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        final int l = resolution.getScaledHeight() - 55 - (Armor.mc.player.isInWater() ? 10 : 0);
        for (final ItemStack is : Armor.mc.player.inventory.armorInventory) {
            ++iteration;
            int x_2 = 0;
            int y_2 = 0;
            final String s2 = this.aligned.getValue();
            switch (s2) {
                case "X-axis": {
                    x_2 = (int)this.x - 90 + (9 - iteration) * 20 + 2 - 12;
                    y_2 = (int)this.y;
                    if (this.fixed.getValue()) {
                        x_2 = i - 90 + (9 - iteration) * 20 + 2;
                        y_2 = l;
                        break;
                    }
                    break;
                }
                case "Y-axis": {
                    x_2 = (int)this.x;
                    y_2 = (int)this.y - (iteration - 1) * 18 + 54;
                    if (this.fixed.getValue()) {
                        x_2 = i;
                        y_2 = l - (iteration - 1) * 18 + 54;
                        break;
                    }
                    break;
                }
            }
            if (this.progress.getValue()) {
                final String s3 = this.aligned.getValue();
                switch (s3) {
                    case "X-axis": {
                        Gui.drawRect(x_2 - 1, y_2 + 16, x_2 + 19, y_2 - 51 + 16, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
                        break;
                    }
                    case "Y-axis": {
                        Gui.drawRect(x_2, y_2, x_2 + 69, y_2 + 18, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
                        break;
                    }
                }
                if (is.isEmpty()) {
                    continue;
                }
                final float percentBar = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - percentBar;
                final String s4 = this.aligned.getValue();
                switch (s4) {
                    case "X-axis": {
                        Gui.drawRect(x_2 - 1, y_2 + 16, x_2 + 19, (int)(y_2 - percentBar * 51.0f) + 16, ColorUtils.changeAlpha(ColourHolder.toHex((int)(red * 255.0f), (int)(percentBar * 255.0f), 0), 150));
                        break;
                    }
                    case "Y-axis": {
                        Gui.drawRect(x_2, y_2, (int)(x_2 + percentBar * 69.0f), y_2 + 18, ColorUtils.changeAlpha(ColourHolder.toHex((int)(red * 255.0f), (int)(percentBar * 255.0f), 0), 150));
                        break;
                    }
                }
            }
            else if (is.isEmpty()) {
                continue;
            }
            GlStateManager.enableDepth();
            Armor.itemRender.zLevel = 200.0f;
            Armor.itemRender.renderItemAndEffectIntoGUI(is, x_2, y_2);
            Armor.itemRender.renderItemOverlayIntoGUI(Armor.mc.fontRenderer, is, x_2, y_2, "");
            Armor.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            Armor.mc.fontRenderer.drawStringWithShadow(s, (float)(x_2 + 19 - 2 - Armor.mc.fontRenderer.getStringWidth(s)), (float)((int)this.y + 9), 16777215);
            if (this.damage.getValue()) {
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red2 = 1.0f - green;
                final int dmg = 100 - (int)(red2 * 100.0f);
                final String s5 = this.aligned.getValue();
                switch (s5) {
                    case "X-axis": {
                        if (this.cf.getValue()) {
                            Xulu.cFontRenderer.drawStringWithShadow(dmg + "", x_2 + 8 - Xulu.cFontRenderer.getStringWidth(dmg + "") / 2, y_2 - 11, ColourHolder.toHex((int)(red2 * 255.0f), (int)(green * 255.0f), 0));
                            continue;
                        }
                        Armor.fontRenderer.drawStringWithShadow(dmg + "", (float)(x_2 + 9 - Armor.fontRenderer.getStringWidth(dmg + "") / 2), (float)(y_2 - 11), ColourHolder.toHex((int)(red2 * 255.0f), (int)(green * 255.0f), 0));
                        continue;
                    }
                    case "Y-axis": {
                        if (this.cf.getValue()) {
                            Xulu.cFontRenderer.drawStringWithShadow(dmg + "", x_2 + 18, y_2 + 5, ColourHolder.toHex((int)(red2 * 255.0f), (int)(green * 255.0f), 0));
                            continue;
                        }
                        Armor.fontRenderer.drawStringWithShadow(dmg + "", (float)(x_2 + 18), (float)(y_2 + 5), ColourHolder.toHex((int)(red2 * 255.0f), (int)(green * 255.0f), 0));
                        continue;
                    }
                }
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
    
    static {
        Armor.itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}

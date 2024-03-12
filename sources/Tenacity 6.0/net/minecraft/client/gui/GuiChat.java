// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ChatComponentText;
import dev.tenacity.utils.font.CustomFont;
import dev.tenacity.utils.tuples.Pair;
import net.minecraft.util.IChatComponent;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.module.impl.render.HUDMod;
import java.util.Iterator;
import dev.tenacity.utils.font.AbstractFontRenderer;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.config.DragManager;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import dev.tenacity.utils.animations.Direction;
import com.google.common.collect.Lists;
import dev.tenacity.module.impl.render.ArrayListMod;
import dev.tenacity.module.impl.render.SpotifyMod;
import dev.tenacity.utils.animations.Animation;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class GuiChat extends GuiScreen
{
    private static final Logger logger;
    private String historyBuffer;
    private int sentHistoryCursor;
    private boolean playerNamesFound;
    private boolean waitingOnAutocomplete;
    private int autocompleteIndex;
    private List<String> foundPlayerNames;
    protected GuiTextField inputField;
    private String defaultInputFieldText;
    Animation resetButtonHover;
    SpotifyMod spotifyMod;
    ArrayListMod arraylistMod;
    public static Animation openingAnimation;
    
    public GuiChat() {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.foundPlayerNames = (List<String>)Lists.newArrayList();
        this.defaultInputFieldText = "";
    }
    
    public GuiChat(final String defaultText) {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.foundPlayerNames = (List<String>)Lists.newArrayList();
        this.defaultInputFieldText = "";
        this.defaultInputFieldText = defaultText;
    }
    
    @Override
    public void onGuiClosed() {
        GuiChat.openingAnimation.setDirection(Direction.BACKWARDS);
        Keyboard.enableRepeatEvents(false);
        this.mc2.ingameGUI.getChatGUI().resetScroll();
    }
    
    @Override
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.waitingOnAutocomplete = false;
        if (keyCode == 15) {
            this.autocompletePlayerNames();
        }
        else {
            this.playerNamesFound = false;
        }
        if (keyCode == 1) {
            GuiChat.openingAnimation.setDirection(Direction.BACKWARDS);
        }
        else if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208) {
                this.getSentHistory(1);
            }
            else if (keyCode == 201) {
                this.mc2.ingameGUI.getChatGUI().scroll(this.mc2.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209) {
                this.mc2.ingameGUI.getChatGUI().scroll(-this.mc2.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else {
            final String s = this.inputField.getText().trim();
            if (s.length() > 0) {
                this.sendChatMessage(s);
            }
            GuiChat.openingAnimation.setDirection(Direction.BACKWARDS);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }
            if (i < -1) {
                i = -1;
            }
            if (!GuiScreen.isShiftKeyDown()) {
                i *= 7;
            }
            this.mc2.ingameGUI.getChatGUI().scroll(i);
        }
    }
    
    @Override
    public void initGui() {
        if (this.spotifyMod == null) {
            this.spotifyMod = (SpotifyMod)Tenacity.INSTANCE.getModuleCollection().get(SpotifyMod.class);
            this.arraylistMod = (ArrayListMod)Tenacity.INSTANCE.getModuleCollection().get(ArrayListMod.class);
        }
        for (final Dragging dragging : DragManager.draggables.values()) {
            if (!dragging.hoverAnimation.getDirection().equals(Direction.BACKWARDS)) {
                dragging.hoverAnimation.setDirection(Direction.BACKWARDS);
            }
        }
        GuiChat.openingAnimation = new DecelerateAnimation(175, 1.0);
        this.resetButtonHover = new DecelerateAnimation(250, 1.0);
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc2.ingameGUI.getChatGUI().getSentMessages().size();
        (this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12)).setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (GuiChat.openingAnimation.finished(Direction.BACKWARDS)) {
            this.mc2.displayGuiScreen(null);
            return;
        }
        Gui.drawRect2(2.0, this.height - 14.0f * GuiChat.openingAnimation.getOutput().floatValue(), this.width - 4, 12.0, Integer.MIN_VALUE);
        AbstractFontRenderer abstractFontRenderer = this.mc2.fontRendererObj;
        if (HUDMod.customFont.isEnabled()) {
            abstractFontRenderer = FontUtil.tenacityFont20;
        }
        this.inputField.font = abstractFontRenderer;
        this.inputField.yPosition = this.height - 12.0f * GuiChat.openingAnimation.getOutput().floatValue();
        this.inputField.drawTextBox();
        final IChatComponent ichatcomponent = this.mc2.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }
        DragManager.draggables.values().forEach(dragging -> {
            if (dragging.getModule().isEnabled()) {
                if (dragging.getModule().equals(this.arraylistMod)) {
                    dragging.onDrawArraylist(this.arraylistMod, mouseX, mouseY);
                }
                else {
                    dragging.onDraw(mouseX, mouseY);
                }
            }
            return;
        });
        final HUDMod hudMod = (HUDMod)Tenacity.INSTANCE.getModuleCollection().get(HUDMod.class);
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        final boolean hovering = HoveringUtil.isHovering(this.width / 2.0f - 50.0f, 20.0f, 100.0f, 20.0f, mouseX, mouseY);
        this.resetButtonHover.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        final float alpha = (float)(0.5 + 0.5 * this.resetButtonHover.getOutput().floatValue());
        final Color color = ColorUtil.interpolateColorsBackAndForth(15, 1, colors.getFirst(), colors.getSecond(), false);
        RoundedUtil.drawRoundOutline(this.width / 2.0f - 50.0f, 20.0f, 100.0f, 20.0f, 10.0f, 2.0f, new Color(40, 40, 40, (int)(255.0f * alpha)), color);
        FontUtil.tenacityBoldFont20.drawCenteredString("Reset Draggables", this.width / 2.0f, 20.0f + FontUtil.tenacityBoldFont20.getMiddleOfBox(20.0f), -1);
        if (this.spotifyMod.isEnabled()) {
            float spacing = 0.0f;
            final Dragging spotifyDrag = DragManager.draggables.get("spotify");
            for (final String button : this.spotifyMod.buttons) {
                final CustomFont iconFont = FontUtil.FontType.ICON.size(20);
                final float x = spotifyDrag.getX();
                this.spotifyMod.getClass();
                final float x2 = x + 50.0f + 6.0f + spacing;
                final float y = spotifyDrag.getY();
                this.spotifyMod.getClass();
                final boolean hover = HoveringUtil.isHovering(x2, y + 50.0f - 19.0f, iconFont.getStringWidth(button), (float)iconFont.getHeight(), mouseX, mouseY);
                this.spotifyMod.buttonAnimations.get(button).setDirection(hover ? Direction.FORWARDS : Direction.BACKWARDS);
                spacing += 15.0f;
            }
            final SpotifyMod spotifyMod = this.spotifyMod;
            final float x3 = spotifyDrag.getX();
            final float y2 = spotifyDrag.getY();
            this.spotifyMod.getClass();
            final float width = 50.0f;
            this.spotifyMod.getClass();
            spotifyMod.hoveringPause = HoveringUtil.isHovering(x3, y2, width, 50.0f, mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifne            35
        //     4: aload_0         /* this */
        //     5: getfield        net/minecraft/client/gui/GuiChat.mc2:Lnet/minecraft/client/Minecraft;
        //     8: getfield        net/minecraft/client/Minecraft.ingameGUI:Lnet/minecraft/client/gui/GuiIngame;
        //    11: invokevirtual   net/minecraft/client/gui/GuiIngame.getChatGUI:()Lnet/minecraft/client/gui/GuiNewChat;
        //    14: invokestatic    org/lwjgl/input/Mouse.getX:()I
        //    17: invokestatic    org/lwjgl/input/Mouse.getY:()I
        //    20: invokevirtual   net/minecraft/client/gui/GuiNewChat.getChatComponent:(II)Lnet/minecraft/util/IChatComponent;
        //    23: astore          ichatcomponent
        //    25: aload_0         /* this */
        //    26: aload           ichatcomponent
        //    28: invokevirtual   net/minecraft/client/gui/GuiChat.handleComponentClick:(Lnet/minecraft/util/IChatComponent;)Z
        //    31: ifeq            35
        //    34: return         
        //    35: aload_0         /* this */
        //    36: getfield        net/minecraft/client/gui/GuiChat.inputField:Lnet/minecraft/client/gui/GuiTextField;
        //    39: iload_1         /* mouseX */
        //    40: iload_2         /* mouseY */
        //    41: iload_3         /* mouseButton */
        //    42: invokevirtual   net/minecraft/client/gui/GuiTextField.mouseClicked:(III)V
        //    45: aload_0         /* this */
        //    46: getfield        net/minecraft/client/gui/GuiChat.spotifyMod:Ldev/tenacity/module/impl/render/SpotifyMod;
        //    49: invokevirtual   dev/tenacity/module/impl/render/SpotifyMod.isEnabled:()Z
        //    52: ifeq            222
        //    55: fconst_0       
        //    56: fstore          spacing
        //    58: aload_0         /* this */
        //    59: getfield        net/minecraft/client/gui/GuiChat.spotifyMod:Ldev/tenacity/module/impl/render/SpotifyMod;
        //    62: getfield        dev/tenacity/module/impl/render/SpotifyMod.buttons:[Ljava/lang/String;
        //    65: astore          5
        //    67: aload           5
        //    69: arraylength    
        //    70: istore          6
        //    72: iconst_0       
        //    73: istore          7
        //    75: iload           7
        //    77: iload           6
        //    79: if_icmpge       203
        //    82: aload           5
        //    84: iload           7
        //    86: aaload         
        //    87: astore          button
        //    89: getstatic       dev/tenacity/config/DragManager.draggables:Ljava/util/HashMap;
        //    92: ldc             "spotify"
        //    94: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    97: checkcast       Ldev/tenacity/utils/objects/Dragging;
        //   100: astore          spotifyDrag
        //   102: getstatic       dev/tenacity/utils/font/FontUtil$FontType.ICON:Ldev/tenacity/utils/font/FontUtil$FontType;
        //   105: bipush          20
        //   107: invokevirtual   dev/tenacity/utils/font/FontUtil$FontType.size:(I)Ldev/tenacity/utils/font/CustomFont;
        //   110: astore          iconFont
        //   112: aload           spotifyDrag
        //   114: invokevirtual   dev/tenacity/utils/objects/Dragging.getX:()F
        //   117: aload_0         /* this */
        //   118: getfield        net/minecraft/client/gui/GuiChat.spotifyMod:Ldev/tenacity/module/impl/render/SpotifyMod;
        //   121: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   124: pop            
        //   125: ldc             50.0
        //   127: fadd           
        //   128: ldc             6.0
        //   130: fadd           
        //   131: fload           spacing
        //   133: fadd           
        //   134: aload           spotifyDrag
        //   136: invokevirtual   dev/tenacity/utils/objects/Dragging.getY:()F
        //   139: aload_0         /* this */
        //   140: getfield        net/minecraft/client/gui/GuiChat.spotifyMod:Ldev/tenacity/module/impl/render/SpotifyMod;
        //   143: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   146: pop            
        //   147: ldc             50.0
        //   149: fadd           
        //   150: ldc             19.0
        //   152: fsub           
        //   153: aload           iconFont
        //   155: aload           button
        //   157: invokevirtual   dev/tenacity/utils/font/CustomFont.getStringWidth:(Ljava/lang/String;)F
        //   160: aload           iconFont
        //   162: invokevirtual   dev/tenacity/utils/font/CustomFont.getHeight:()I
        //   165: i2f            
        //   166: iload_1         /* mouseX */
        //   167: iload_2         /* mouseY */
        //   168: invokestatic    dev/tenacity/utils/misc/HoveringUtil.isHovering:(FFFFII)Z
        //   171: ifeq            190
        //   174: iload_3         /* mouseButton */
        //   175: ifne            190
        //   178: aload_0         /* this */
        //   179: aload           button
        //   181: invokedynamic   BootstrapMethod #1, run:(Lnet/minecraft/client/gui/GuiChat;Ljava/lang/String;)Ljava/lang/Runnable;
        //   186: invokestatic    dev/tenacity/utils/misc/Multithreading.runAsync:(Ljava/lang/Runnable;)V
        //   189: return         
        //   190: fload           spacing
        //   192: ldc             15.0
        //   194: fadd           
        //   195: fstore          spacing
        //   197: iinc            7, 1
        //   200: goto            75
        //   203: aload_0         /* this */
        //   204: getfield        net/minecraft/client/gui/GuiChat.spotifyMod:Ldev/tenacity/module/impl/render/SpotifyMod;
        //   207: getfield        dev/tenacity/module/impl/render/SpotifyMod.hoveringPause:Z
        //   210: ifeq            222
        //   213: aload_0         /* this */
        //   214: invokedynamic   BootstrapMethod #2, run:(Lnet/minecraft/client/gui/GuiChat;)Ljava/lang/Runnable;
        //   219: invokestatic    dev/tenacity/utils/misc/Multithreading.runAsync:(Ljava/lang/Runnable;)V
        //   222: aload_0         /* this */
        //   223: getfield        net/minecraft/client/gui/GuiChat.width:I
        //   226: i2f            
        //   227: fconst_2       
        //   228: fdiv           
        //   229: ldc             100.0
        //   231: fsub           
        //   232: ldc             20.0
        //   234: ldc             200.0
        //   236: ldc             20.0
        //   238: iload_1         /* mouseX */
        //   239: iload_2         /* mouseY */
        //   240: invokestatic    dev/tenacity/utils/misc/HoveringUtil.isHovering:(FFFFII)Z
        //   243: istore          hoveringResetButton
        //   245: iload           hoveringResetButton
        //   247: ifeq            313
        //   250: iload_3         /* mouseButton */
        //   251: ifne            313
        //   254: getstatic       dev/tenacity/config/DragManager.draggables:Ljava/util/HashMap;
        //   257: invokevirtual   java/util/HashMap.values:()Ljava/util/Collection;
        //   260: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //   265: astore          5
        //   267: aload           5
        //   269: invokeinterface java/util/Iterator.hasNext:()Z
        //   274: ifeq            312
        //   277: aload           5
        //   279: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   284: checkcast       Ldev/tenacity/utils/objects/Dragging;
        //   287: astore          dragging
        //   289: aload           dragging
        //   291: aload           dragging
        //   293: getfield        dev/tenacity/utils/objects/Dragging.initialXVal:F
        //   296: invokevirtual   dev/tenacity/utils/objects/Dragging.setX:(F)V
        //   299: aload           dragging
        //   301: aload           dragging
        //   303: getfield        dev/tenacity/utils/objects/Dragging.initialYVal:F
        //   306: invokevirtual   dev/tenacity/utils/objects/Dragging.setY:(F)V
        //   309: goto            267
        //   312: return         
        //   313: getstatic       dev/tenacity/config/DragManager.draggables:Ljava/util/HashMap;
        //   316: invokevirtual   java/util/HashMap.values:()Ljava/util/Collection;
        //   319: aload_0         /* this */
        //   320: iload_1         /* mouseX */
        //   321: iload_2         /* mouseY */
        //   322: iload_3         /* mouseButton */
        //   323: invokedynamic   BootstrapMethod #3, accept:(Lnet/minecraft/client/gui/GuiChat;III)Ljava/util/function/Consumer;
        //   328: invokeinterface java/util/Collection.forEach:(Ljava/util/function/Consumer;)V
        //   333: aload_0         /* this */
        //   334: iload_1         /* mouseX */
        //   335: iload_2         /* mouseY */
        //   336: iload_3         /* mouseButton */
        //   337: invokespecial   net/minecraft/client/gui/GuiScreen.mouseClicked:(III)V
        //   340: return         
        //    Exceptions:
        //  throws java.io.IOException
        //    StackMapTable: 00 08 23 FF 00 27 00 08 07 01 1B 01 01 01 02 07 01 22 01 01 00 00 FE 00 72 07 00 F6 07 01 21 07 01 23 FF 00 0C 00 05 07 01 1B 01 01 01 02 00 00 FA 00 12 FD 00 2C 01 07 00 FE FA 00 2C 00
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
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        DragManager.draggables.values().forEach(dragging -> {
            if (dragging.getModule().isEnabled()) {
                dragging.onRelease(state);
            }
        });
    }
    
    @Override
    protected void setText(final String newChatText, final boolean shouldOverwrite) {
        if (shouldOverwrite) {
            this.inputField.setText(newChatText);
        }
        else {
            this.inputField.writeText(newChatText);
        }
    }
    
    public void autocompletePlayerNames() {
        if (this.playerNamesFound) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
            if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
                this.autocompleteIndex = 0;
            }
        }
        else {
            final int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            final String s = this.inputField.getText().substring(i).toLowerCase();
            final String s2 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(s2, s);
            if (this.foundPlayerNames.isEmpty()) {
                return;
            }
            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
        }
        if (this.foundPlayerNames.size() > 1) {
            final StringBuilder stringbuilder = new StringBuilder();
            for (final String s3 : this.foundPlayerNames) {
                if (stringbuilder.length() > 0) {
                    stringbuilder.append(", ");
                }
                stringbuilder.append(s3);
            }
            this.mc2.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
        }
        this.inputField.writeText(this.foundPlayerNames.get(this.autocompleteIndex++));
    }
    
    private void sendAutocompleteRequest(final String p_146405_1_, final String p_146405_2_) {
        if (p_146405_1_.length() >= 1) {
            BlockPos blockpos = null;
            if (this.mc2.objectMouseOver != null && this.mc2.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockpos = this.mc2.objectMouseOver.getBlockPos();
            }
            this.mc2.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }
    
    public void getSentHistory(final int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        final int j = this.mc2.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);
        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            }
            else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }
                this.inputField.setText(this.mc2.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }
    
    public void onAutocompleteResponse(final String[] p_146406_1_) {
        if (this.waitingOnAutocomplete) {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();
            for (final String s : p_146406_1_) {
                if (s.length() > 0) {
                    this.foundPlayerNames.add(s);
                }
            }
            final String s2 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            final String s3 = StringUtils.getCommonPrefix(p_146406_1_);
            if (s3.length() > 0 && !s2.equalsIgnoreCase(s3)) {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(s3);
            }
            else if (this.foundPlayerNames.size() > 0) {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    static {
        logger = LogManager.getLogger();
        GuiChat.openingAnimation = new DecelerateAnimation(175, 1.0, Direction.BACKWARDS);
    }
}

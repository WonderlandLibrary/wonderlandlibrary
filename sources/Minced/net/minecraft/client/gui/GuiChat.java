// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import javax.annotation.Nullable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import ru.tuskevich.util.drag.Dragging;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.MISC.Hider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import ru.tuskevich.util.math.Vec2i;
import ru.tuskevich.util.drag.DragManager;
import net.minecraft.client.Minecraft;
import ru.tuskevich.Minced;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.TabCompleter;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITabCompleter;

public class GuiChat extends GuiScreen implements ITabCompleter
{
    private static final Logger LOGGER;
    private String historyBuffer;
    private int sentHistoryCursor;
    private TabCompleter tabCompleter;
    protected GuiTextField inputField;
    private String defaultInputFieldText;
    
    public GuiChat() {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.defaultInputFieldText = "";
    }
    
    public GuiChat(final String defaultText) {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.defaultInputFieldText = "";
        this.defaultInputFieldText = defaultText;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = GuiChat.mc.ingameGUI.getChatGUI().getSentMessages().size();
        (this.inputField = new GuiTextField(0, this.fontRenderer, 4, this.height - 12, this.width - 4, 12)).setMaxStringLength(256);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
        this.tabCompleter = new ChatTabCompleter(this.inputField);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        GuiChat.mc.ingameGUI.getChatGUI().resetScroll();
    }
    
    @Override
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.tabCompleter.resetRequested();
        if (keyCode == 15) {
            this.tabCompleter.complete();
        }
        else {
            this.tabCompleter.resetDidComplete();
        }
        if (keyCode == 1) {
            GuiChat.mc.displayGuiScreen(null);
        }
        else if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208) {
                this.getSentHistory(1);
            }
            else if (keyCode == 201) {
                GuiChat.mc.ingameGUI.getChatGUI().scroll(GuiChat.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209) {
                GuiChat.mc.ingameGUI.getChatGUI().scroll(-GuiChat.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else {
            final String s = this.inputField.getText().trim();
            if (!s.isEmpty()) {
                this.sendChatMessage(s);
            }
            GuiChat.mc.displayGuiScreen(null);
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
            GuiChat.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final Vec2i scale = Minced.getInstance().scaleMath.getMouse(mouseX, mouseY, new ScaledResolution(Minecraft.getMinecraft()));
        if (mouseButton == 0) {
            final ITextComponent itextcomponent = GuiChat.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
            if (itextcomponent != null && this.handleComponentClick(itextcomponent)) {
                return;
            }
        }
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        final Vec2i vec2i;
        DragManager.draggables.values().forEach(dragging -> {
            if (dragging.getModule().state) {
                dragging.onClick(vec2i.getX(), vec2i.getY(), mouseButton);
            }
            return;
        });
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        DragManager.draggables.values().forEach(dragging -> {
            if (dragging.getModule().state) {
                dragging.onRelease(state);
            }
            return;
        });
        super.mouseReleased(mouseX, mouseY, state);
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
    
    public void getSentHistory(final int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        final int j = GuiChat.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp(i, 0, j);
        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            }
            else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }
                this.inputField.setText(GuiChat.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Vec2i scale = Minced.getInstance().scaleMath.getMouse(mouseX, mouseY, new ScaledResolution(Minecraft.getMinecraft()));
        Gui.drawRect(2.0f, (float)(this.height - 14), (float)(this.width - 2), (float)(this.height - 2), Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        final ITextComponent itextcomponent = GuiChat.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }
        if (Minced.getInstance().manager.getModule(Hider.class).state && ((this.inputField.getText().startsWith("/l") && this.inputField.getText().split(" ")[0].length() <= 6) || (this.inputField.getText().startsWith("/reg") && this.inputField.getText().split(" ")[0].length() <= 9) || (this.inputField.getText().startsWith("/warp") && this.inputField.getText().split(" ")[0].length() <= 9))) {
            RenderUtility.drawRectNotWH(GuiChat.mc.fontRenderer.getStringWidth(this.inputField.getText().split(" ")[0]) + 6, this.height - 14, GuiChat.mc.fontRenderer.getStringWidth(this.inputField.getText().split(" ")[0]) + 6 + GuiChat.mc.fontRenderer.getStringWidth(this.inputField.getText().substring(this.inputField.getText().split(" ")[0].length())), this.height - 2, Integer.MIN_VALUE);
        }
        final Vec2i vec2i;
        DragManager.draggables.values().forEach(dragging -> {
            if (dragging.getModule().state) {
                dragging.onDraw(vec2i.getX(), vec2i.getY());
            }
            return;
        });
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void setCompletions(final String... newCompletions) {
        this.tabCompleter.setCompletions(newCompletions);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class ChatTabCompleter extends TabCompleter
    {
        private final Minecraft clientInstance;
        
        public ChatTabCompleter(final GuiTextField p_i46749_1_) {
            super(p_i46749_1_, false);
            this.clientInstance = Minecraft.getMinecraft();
        }
        
        @Override
        public void complete() {
            super.complete();
            if (this.completions.size() > 1) {
                final StringBuilder stringbuilder = new StringBuilder();
                for (final String s : this.completions) {
                    if (stringbuilder.length() > 0) {
                        stringbuilder.append(", ");
                    }
                    stringbuilder.append(s);
                }
                this.clientInstance.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
            }
        }
        
        @Nullable
        @Override
        public BlockPos getTargetBlockPos() {
            BlockPos blockpos = null;
            if (this.clientInstance.objectMouseOver != null && this.clientInstance.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                blockpos = this.clientInstance.objectMouseOver.getBlockPos();
            }
            return blockpos;
        }
    }
}

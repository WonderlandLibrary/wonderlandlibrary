// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;

public abstract class TabCompleter
{
    protected final GuiTextField textField;
    protected final boolean hasTargetBlock;
    protected boolean didComplete;
    protected boolean requestedCompletions;
    protected int completionIdx;
    protected List<String> completions;
    
    public TabCompleter(final GuiTextField textFieldIn, final boolean hasTargetBlockIn) {
        this.completions = (List<String>)Lists.newArrayList();
        this.textField = textFieldIn;
        this.hasTargetBlock = hasTargetBlockIn;
    }
    
    public void complete() {
        if (this.didComplete) {
            this.textField.deleteFromCursor(0);
            this.textField.deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());
            if (this.completionIdx >= this.completions.size()) {
                this.completionIdx = 0;
            }
        }
        else {
            final int i = this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false);
            this.completions.clear();
            this.completionIdx = 0;
            final String s = this.textField.getText().substring(0, this.textField.getCursorPosition());
            this.requestCompletions(s);
            if (this.completions.isEmpty()) {
                return;
            }
            this.didComplete = true;
            this.textField.deleteFromCursor(i - this.textField.getCursorPosition());
        }
        this.textField.writeText(this.completions.get(this.completionIdx++));
    }
    
    private void requestCompletions(final String prefix) {
        if (prefix.length() >= 1) {
            Minecraft.getMinecraft();
            Minecraft.player.connection.sendPacket(new CPacketTabComplete(prefix, this.getTargetBlockPos(), this.hasTargetBlock));
            this.requestedCompletions = true;
        }
    }
    
    @Nullable
    public abstract BlockPos getTargetBlockPos();
    
    public void setCompletions(final String... newCompl) {
        if (this.requestedCompletions) {
            this.didComplete = false;
            this.completions.clear();
            for (final String s : newCompl) {
                if (!s.isEmpty()) {
                    this.completions.add(s);
                }
            }
            final String s2 = this.textField.getText().substring(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false));
            final String s3 = StringUtils.getCommonPrefix(newCompl);
            if (!s3.isEmpty() && !s2.equalsIgnoreCase(s3)) {
                this.textField.deleteFromCursor(0);
                this.textField.deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());
                this.textField.writeText(s3);
            }
            else if (!this.completions.isEmpty()) {
                this.didComplete = true;
                this.complete();
            }
        }
    }
    
    public void resetDidComplete() {
        this.didComplete = false;
    }
    
    public void resetRequested() {
        this.requestedCompletions = false;
    }
}

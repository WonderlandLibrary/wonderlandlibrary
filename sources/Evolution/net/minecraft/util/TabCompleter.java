package net.minecraft.util;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.TabCompleteEvent;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;
import wtf.evolution.Main;
import wtf.evolution.command.Command;

public abstract class TabCompleter
{
    /** The {@link GuiTextField} that is backing this {@link TabCompleter} */
    protected final GuiTextField textField;
    protected final boolean hasTargetBlock;
    protected boolean didComplete;
    protected boolean requestedCompletions;
    protected int completionIdx;
    protected List<String> completions = Lists.<String>newArrayList();
    public ArrayList<String> commands = new ArrayList<>();
    public TabCompleter(GuiTextField textFieldIn, boolean hasTargetBlockIn)
    {
        this.textField = textFieldIn;
        this.hasTargetBlock = hasTargetBlockIn;
        commands.add(".help");
        commands.add(".vclip");
        commands.add(".hclip");
        commands.add(".friend");
        commands.add(".config");
    }


    /**
     * Called when tab key pressed. If it's the first time we tried to complete this string, we ask the server for
     * completions. When the server responds, this method gets called again (via setCompletions).
     */
    public void complete()
    {
        if (this.didComplete)
        {
            this.textField.deleteFromCursor(0);
            this.textField.deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());

            if (this.completionIdx >= this.completions.size())
            {
                this.completionIdx = 0;
            }
        }
        else
        {
            int i = this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false);
            this.completions.clear();
            this.completionIdx = 0;
            String s = this.textField.getText().substring(0, this.textField.getCursorPosition());
            this.requestCompletions(s);

            if (this.completions.isEmpty())
            {
                return;
            }

            this.didComplete = true;
            this.textField.deleteFromCursor(i - this.textField.getCursorPosition());
        }

        this.textField.writeText(this.completions.get(this.completionIdx++));
    }

    private void requestCompletions(String prefix)
    {
        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();

        TabCompleteEvent event = new TabCompleteEvent(prefix);
        baritone.getGameEventHandler().onPreTabComplete(event);


        if (event.isCancelled()) {
            event.cancel();
            return;
        }
        if (prefix.length() >= 1)
        {

            if (prefix.startsWith("#"))
                completions.addAll(Arrays.asList(event.completions));
            else if (prefix.startsWith(".")) {
                for (Command c : Main.d.cmds) {
                    if (prefix.startsWith("." + c.command)) {
                        if (c.getSuggestions(prefix.split(" ")) != null) {
                            completions.addAll(c.getSuggestions(prefix.split(" ")));
                        }
                    }
                }
            }
            else {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketTabComplete(prefix, this.getTargetBlockPos(), this.hasTargetBlock));
                this.requestedCompletions = true;
            }

        }
    }

    @Nullable
    public abstract BlockPos getTargetBlockPos();

    /**
     * Only actually sets completions if they were requested (via requestCompletions)
     */
    public void setCompletions(String... newCompl)
    {
        if (this.requestedCompletions)
        {
            this.didComplete = false;
            this.completions.clear();

            for (String s : newCompl)
            {
                if (!s.isEmpty())
                {
                    this.completions.add(s);
                }
            }

            String s1 = this.textField.getText().substring(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false));
            String s2 = org.apache.commons.lang3.StringUtils.getCommonPrefix(newCompl);

            if (!s2.isEmpty() && !s1.equalsIgnoreCase(s2))
            {
                this.textField.deleteFromCursor(0);
                this.textField.deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());
                this.textField.writeText(s2);
            }
            else if (!this.completions.isEmpty())
            {
                this.didComplete = true;
                this.complete();
            }
        }
    }

    /**
     * Called when new text is entered, or backspace pressed
     */
    public void resetDidComplete()
    {
        this.didComplete = false;
    }

    public void resetRequested()
    {
        this.requestedCompletions = false;
    }
}

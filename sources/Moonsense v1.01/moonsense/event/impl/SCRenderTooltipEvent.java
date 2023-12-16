// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.gui.FontRenderer;
import java.util.List;
import net.minecraft.item.ItemStack;
import moonsense.event.SCEvent;

public abstract class SCRenderTooltipEvent extends SCEvent
{
    protected final ItemStack stack;
    protected final List<String> lines;
    protected int x;
    protected int y;
    protected FontRenderer fr;
    
    public SCRenderTooltipEvent(@Nullable final ItemStack stack, @NotNull final List<String> lines, final int x, final int y, @NotNull final FontRenderer fr) {
        this.stack = stack;
        this.lines = Collections.unmodifiableList((List<? extends String>)lines);
        this.x = x;
        this.y = y;
        this.fr = fr;
    }
    
    @Nullable
    public ItemStack getStack() {
        return this.stack;
    }
    
    @NotNull
    public List<String> getLines() {
        return this.lines;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    @NotNull
    public FontRenderer getFontRenderer() {
        return this.fr;
    }
    
    public static class PostBackground extends Post
    {
        public PostBackground(@Nullable final ItemStack stack, @NotNull final List<String> textLines, final int x, final int y, @NotNull final FontRenderer fr, final int width, final int height) {
            super(stack, textLines, x, y, fr, width, height);
        }
    }
    
    public static class PostText extends Post
    {
        public PostText(@Nullable final ItemStack stack, @NotNull final List<String> textLines, final int x, final int y, @NotNull final FontRenderer fr, final int width, final int height) {
            super(stack, textLines, x, y, fr, width, height);
        }
    }
    
    public static class Pre extends SCRenderTooltipEvent
    {
        private int screenWidth;
        private int screenHeight;
        private int maxWidth;
        
        public Pre(@Nullable final ItemStack stack, @NotNull final List<String> lines, final int x, final int y, final int screenWidth, final int screenHeight, final int maxWidth, @NotNull final FontRenderer fr) {
            super(stack, lines, x, y, fr);
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.maxWidth = maxWidth;
        }
        
        public int getScreenWidth() {
            return this.screenWidth;
        }
        
        public void setScreenWidth(final int screenWidth) {
            this.screenWidth = screenWidth;
        }
        
        public int getScreenHeight() {
            return this.screenHeight;
        }
        
        public void setScreenHeight(final int screenHeight) {
            this.screenHeight = screenHeight;
        }
        
        public int getMaxWidth() {
            return this.maxWidth;
        }
        
        public void setMaxWidth(final int maxWidth) {
            this.maxWidth = maxWidth;
        }
        
        public void setFontRenderer(@NotNull final FontRenderer fr) {
            this.fr = fr;
        }
        
        public void setX(final int x) {
            this.x = x;
        }
        
        public void setY(final int y) {
            this.y = y;
        }
    }
    
    protected abstract static class Post extends SCRenderTooltipEvent
    {
        private final int width;
        private final int height;
        
        public Post(@Nullable final ItemStack stack, @NotNull final List<String> textLines, final int x, final int y, @NotNull final FontRenderer fr, final int width, final int height) {
            super(stack, textLines, x, y, fr);
            this.width = width;
            this.height = height;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        public int getHeight() {
            return this.height;
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.intent.cloud.CloudUtils;
import dev.tenacity.intent.cloud.data.VoteType;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Animation;
import java.awt.Color;
import dev.tenacity.intent.cloud.data.CloudData;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.ui.Screen;

public class VoteRect implements Screen
{
    private final TimerUtil timer;
    private final CloudData cloudData;
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private Color accentColor;
    private final IconButton upvoteButton;
    private final IconButton downvoteButton;
    private int lastVote;
    private Animation voteAnimation;
    
    public VoteRect(final CloudData cloudData) {
        this.timer = new TimerUtil();
        this.upvoteButton = new IconButton("v");
        this.downvoteButton = new IconButton("x");
        this.cloudData = cloudData;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        if (this.cloudData.getVotes() == null) {
            return;
        }
        final int totalVotes = this.cloudData.getVotes().getTotalVotes();
        this.width = (float)(11 + ((totalVotes >= 100 || totalVotes <= -100) ? 5 : 0) + ((totalVotes >= 1000 || totalVotes <= -1000) ? 5 : 0));
        this.height = 30.0f;
        RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 4.0f, ColorUtil.tripleColor(27, this.alpha));
        final Color defaultTextColor = new Color(191, 191, 191);
        final Color greenColor = Tenacity.INSTANCE.getSideGui().getGreenEnabledColor();
        final Color redColor = new Color(209, 56, 56);
        this.upvoteButton.setX(this.x + this.width / 2.0f - this.upvoteButton.getWidth() / 2.0f);
        this.upvoteButton.setY(this.y + 4.0f);
        this.upvoteButton.setAlpha(this.alpha);
        this.upvoteButton.setAccentColor(greenColor);
        this.upvoteButton.setClickAction(() -> {
            if (this.timer.hasTimeElapsed(300L, true)) {
                Multithreading.runAsync(() -> CloudUtils.vote(VoteType.UP, this.cloudData));
            }
            return;
        });
        this.upvoteButton.setIcon(this.cloudData.isUpvoted() ? "w" : "v");
        this.upvoteButton.setTextColor(this.cloudData.isUpvoted() ? greenColor : defaultTextColor);
        this.upvoteButton.drawScreen(mouseX, mouseY);
        this.downvoteButton.setX(this.x + this.width / 2.0f - this.downvoteButton.getWidth() / 2.0f);
        this.downvoteButton.setY(this.y + this.height - (this.downvoteButton.getHeight() + 3.0f));
        this.downvoteButton.setAlpha(this.alpha);
        this.downvoteButton.setAccentColor(redColor);
        this.downvoteButton.setClickAction(() -> {
            if (this.timer.hasTimeElapsed(300L, true)) {
                Multithreading.runAsync(() -> CloudUtils.vote(VoteType.DOWN, this.cloudData));
            }
            return;
        });
        this.downvoteButton.setIcon(this.cloudData.isDownvoted() ? "y" : "x");
        this.downvoteButton.setTextColor(this.cloudData.isDownvoted() ? redColor : defaultTextColor);
        this.downvoteButton.drawScreen(mouseX, mouseY);
        Color voteColor = Color.WHITE;
        if (this.cloudData.isUpvoted()) {
            voteColor = greenColor;
        }
        else if (this.cloudData.isDownvoted()) {
            voteColor = redColor;
        }
        RenderUtil.scissorStart(this.x, this.y + this.height / 2.0f - (VoteRect.tenacityBoldFont16.getHeight() / 2.0f + 1.5), this.width, VoteRect.tenacityBoldFont16.getHeight() + 3);
        this.drawAnimatedVote(totalVotes, voteColor);
        RenderUtil.scissorEnd();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.cloudData.getVotes() == null) {
            return;
        }
        this.upvoteButton.mouseClicked(mouseX, mouseY, button);
        this.downvoteButton.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void drawAnimatedVote(final int vote, final Color color) {
        if (this.lastVote != vote) {
            final int additionalVotes = vote - this.lastVote;
            if (additionalVotes > 50 || additionalVotes < -50) {
                this.lastVote = vote;
                return;
            }
            if (this.voteAnimation == null) {
                this.voteAnimation = new DecelerateAnimation(Math.min(500, 200 * Math.abs(additionalVotes)), additionalVotes * (VoteRect.tenacityBoldFont16.getHeight() + 3));
            }
            final float animation = this.voteAnimation.getOutput().floatValue();
            int count = 0;
            if (additionalVotes > 0) {
                for (int i = this.lastVote; i < vote + 1; ++i) {
                    final int additionalY = count * (VoteRect.tenacityBoldFont16.getHeight() + 3);
                    final float voteY = this.y + VoteRect.tenacityBoldFont16.getMiddleOfBox(this.height) + additionalY;
                    VoteRect.tenacityBoldFont16.drawCenteredString(String.valueOf(i), this.x + this.width / 2.0f, voteY - animation, ColorUtil.applyOpacity(color, this.alpha));
                    ++count;
                }
            }
            else {
                for (int i = this.lastVote; i > vote - 1; --i) {
                    final int additionalY = count * (VoteRect.tenacityBoldFont16.getHeight() + 3);
                    final float voteY = this.y + VoteRect.tenacityBoldFont16.getMiddleOfBox(this.height) - additionalY;
                    VoteRect.tenacityBoldFont16.drawCenteredString(String.valueOf(i), this.x + this.width / 2.0f, voteY - animation, ColorUtil.applyOpacity(color, this.alpha));
                    ++count;
                }
            }
            if (this.voteAnimation.isDone()) {
                this.lastVote = vote;
                this.voteAnimation = null;
            }
        }
        else {
            VoteRect.tenacityBoldFont16.drawCenteredString(String.valueOf(vote), this.x + this.width / 2.0f, this.y + VoteRect.tenacityBoldFont16.getMiddleOfBox(this.height), ColorUtil.applyOpacity(color, this.alpha));
        }
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
    
    public void setLastVote(final int lastVote) {
        this.lastVote = lastVote;
    }
    
    public void setVoteAnimation(final Animation voteAnimation) {
        this.voteAnimation = voteAnimation;
    }
    
    public TimerUtil getTimer() {
        return this.timer;
    }
    
    public CloudData getCloudData() {
        return this.cloudData;
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
    
    public IconButton getUpvoteButton() {
        return this.upvoteButton;
    }
    
    public IconButton getDownvoteButton() {
        return this.downvoteButton;
    }
    
    public int getLastVote() {
        return this.lastVote;
    }
    
    public Animation getVoteAnimation() {
        return this.voteAnimation;
    }
}

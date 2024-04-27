package me.aquavit.liquidsense.injection.forge.mixins.gui;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.ChatEvent;
import me.aquavit.liquidsense.module.modules.client.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

import static net.minecraft.realms.RealmsMth.clamp;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat extends Gui{

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    public abstract int getLineCount();

    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getChatWidth();

    @Shadow
    private int scrollPos;

    @Shadow
    private boolean isScrolled;

    @Shadow
    public abstract void deleteChatLine(int id);

    @Shadow
    @Final
    private List<ChatLine> chatLines;

    @Shadow
    public abstract void scroll(int amount);

    @Shadow
    public abstract boolean getChatOpen();

    private float percentComplete = 0.0F;
    private int newLines;
    private long prevMillis = -1;
    private boolean configuring;

    private void updatePercentage(long diff) {
        if (percentComplete < 1) percentComplete += 0.004f * diff;
        percentComplete = clamp(percentComplete, 0, 1);
    }

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public void drawChat(int updateCounter) {
        final HUD hud = (HUD) LiquidSense.moduleManager.getModule(HUD.class);
        final FontRenderer fontRenderer = hud.getState() ? hud.chatFont.get() : mc.fontRendererObj;
        if (configuring) return;
        if (prevMillis == -1) {
            prevMillis = System.currentTimeMillis();
            return;
        }
        long current = System.currentTimeMillis();
        long diff = current - prevMillis;
        prevMillis = current;
        updatePercentage(diff);
        float t = percentComplete;
        float percent = 1 - (--t) * t * t * t;
        percent = clamp(percent, 0, 1);
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            int j = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (j > 0) {
                boolean flag = false;

                if (this.getChatOpen()) {
                    flag = true;
                }

                float f1 = this.getChatScale();
                int k = MathHelper.ceiling_double_int((float) this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                if (!this.isScrolled) GlStateManager.translate(2.0F, 8.0F  + (9 - 9*percent)*f1, 0.0F);
                else GlStateManager.translate(2.0F, 8.0F , 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);
                int l = 0;

                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);

                    if (chatline != null) {
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double) j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int) (255.0D * d0);

                            if (flag) {
                                l1 = 255;
                            }

                            l1 = (int) ((float) l1 * f);
                            ++l;
                            if (l1 > 3) {
                                int i2 = 0;
                                int j2 = -i1 * 9;
                                //drawRect(i2, j2 - 9, i2 + k + 4, j2, l1 / 2 << 24); 聊天框颜色
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                if (i1 <= newLines) {
                                    fontRenderer.drawStringWithShadow(s, 0.0F, (j2 - 8), 16777215 + ((int) (l1 * percent) << 24));
                                } else {
                                    fontRenderer.drawStringWithShadow(s, (float) i2, (float) (j2 - 8), 16777215 + (l1 << 24));
                                }
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (flag) {
                    int k2 = fontRenderer.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = this.scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
        ChatEvent event = new ChatEvent(chatComponent, this.drawnChatLines);
        LiquidSense.eventManager.callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        percentComplete = 0.0F;
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        LogManager.getLogger().info("[LiquidSense-CHAT] " + chatComponent.getUnformattedText());
    }

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        final HUD hud = (HUD) LiquidSense.moduleManager.getModule(HUD.class);

        final FontRenderer fontRenderer = hud.getState() ? hud.chatFont.get() : mc.fontRendererObj;
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, fontRenderer, false, false);
        boolean flag = this.getChatOpen();
        newLines = list.size() - 1;

        for (IChatComponent itextcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }

            this.drawnChatLines.add(0, new ChatLine(updateCounter, itextcomponent, chatLineId));
        }

        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }

        if (!displayOnly) {
            this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));

            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public IChatComponent getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_) {
        final HUD hud = (HUD) LiquidSense.moduleManager.getModule(HUD.class);
        final FontRenderer fontRenderer = hud.getState() ? hud.chatFont.get() : mc.fontRendererObj;
        if (!this.getChatOpen()) {
            return null;
        } else {
            ScaledResolution lvt_3_1_ = new ScaledResolution(this.mc);
            int lvt_4_1_ = lvt_3_1_.getScaleFactor();
            float lvt_5_1_ = this.getChatScale();
            int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
            int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
            lvt_6_1_ = MathHelper.floor_float((float) lvt_6_1_ / lvt_5_1_);
            lvt_7_1_ = MathHelper.floor_float((float) lvt_7_1_ / lvt_5_1_);
            if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                int lvt_8_1_ = Math.min(this.getLineCount(), this.drawnChatLines.size());
                if (lvt_6_1_ <= MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) && lvt_7_1_ < fontRenderer.FONT_HEIGHT * lvt_8_1_ + lvt_8_1_) {
                    int lvt_9_1_ = lvt_7_1_ / fontRenderer.FONT_HEIGHT + this.scrollPos;
                    if (lvt_9_1_ >= 0 && lvt_9_1_ < this.drawnChatLines.size()) {
                        ChatLine lvt_10_1_ = (ChatLine) this.drawnChatLines.get(lvt_9_1_);
                        int lvt_11_1_ = 0;
                        Iterator lvt_12_1_ = lvt_10_1_.getChatComponent().iterator();

                        while (lvt_12_1_.hasNext()) {
                            IChatComponent lvt_13_1_ = (IChatComponent) lvt_12_1_.next();
                            if (lvt_13_1_ instanceof ChatComponentText) {
                                lvt_11_1_ += fontRenderer.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) lvt_13_1_).getChatComponentText_TextValue(), false));
                                if (lvt_11_1_ > lvt_6_1_) {
                                    return lvt_13_1_;
                                }
                            }
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

}

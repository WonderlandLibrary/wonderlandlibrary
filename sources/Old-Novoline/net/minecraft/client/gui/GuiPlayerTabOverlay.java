package net.minecraft.client.gui;

import cc.novoline.Novoline;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
//import net.skidunion.irc.entities.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class GuiPlayerTabOverlay extends Gui {

    public static String names;
    private static final Ordering<NetworkPlayerInfo> ORDERING = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator());
    public static boolean showPingInTab = false;
    private final Minecraft mc;
    private final GuiIngame guiIngame;
    private IChatComponent footer;
    private IChatComponent header;
    /* TODO: Добавить возможность изменять настройки таба */
    private int tabColoumnLength = 20;
    private int tabSize = 80;
    private boolean tabTransparency = false;
    /**
     * The last time the player list was opened (went from not being rendered, to being rendered)
     */
    private long lastTimeOpened;

    /**
     * Weither or not the player list is currently being rendered
     */
    private boolean isBeingRendered;

    public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame guiIngameIn) {
        this.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }

    /**
     * Returns the name that should be rendered for the player supplied
     */
    public String getPlayerDisplayName(@NotNull NetworkPlayerInfo networkInfo) {
        String displayName = networkInfo.getDisplayName() != null ? networkInfo.getDisplayName().getFormattedText()
                : ScorePlayerTeam.formatPlayerName(networkInfo.getPlayerTeam(), networkInfo.getGameProfile().getName());

//        if (networkInfo.getDisplayName() == null) {
//            UserEntity user = Novoline.getInstance().getIRC().getUserManager().findByNickname(networkInfo.getGameProfile().getName());
//
//            if (!networkInfo.getGameProfile().getName().isEmpty() && user != null) {
//                displayName += " \u00A77(\u00A7b" + user.getUsername() + "\u00A77)\u00A7r";
//            }
//        }

        return displayName;
    }

    /**
     * Called by GuiIngame to update the information stored in the player list, does not actually render the list,
     * however.
     */
    public void updatePlayerList(boolean willBeRendered) {
        if (willBeRendered && !this.isBeingRendered) this.lastTimeOpened = Minecraft.getSystemTime();
        this.isBeingRendered = willBeRendered;
    }

    public List<NetworkPlayerInfo> getList() {
        return ORDERING.sortedCopy(this.mc.getNetHandler().getPlayerInfoMap());
    }

    /**
     * Renders the player list, its background, headers and footers.
     */
    public void renderPlayerList(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
        List<NetworkPlayerInfo> list = getList();
        int i = 0;
        int j = 0;

        for (final NetworkPlayerInfo networkplayerinfo : list) {
            int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerDisplayName(networkplayerinfo));
            i = Math.max(i, k);

            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }

        final int size_tab = Math.max(this.tabSize, 5);
        list = list.subList(0, Math.min(list.size(), size_tab));
        int i2;
        int l3;
        int column_length;
        int j2;

        for (l3 = i2 = list.size(), column_length = Math.max(this.tabColoumnLength, 5), j2 = 1; i2 > column_length; i2 = (l3 + j2 - 1) / j2) {
            ++j2;
        }

        final boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int m;

        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                m = 90;
            } else {
                m = j;
            }
        } else {
            m = 0;
        }

        final int i3 = Math.min(j2 * ((flag ? 9 : 0) + i + m + 13), width - 50) / j2;
        final int j3 = width / 2 - (i3 * j2 + (j2 - 1) * 5) / 2;
        int k2 = 10;
        int l4 = i3 * j2 + (j2 - 1) * 5;
        List<String> list2 = null;
        List<String> list3 = null;

        if (this.header != null) {
            list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

            for (final String s : list2) {
                l4 = Math.max(l4, this.mc.fontRendererObj.getStringWidth(s));
            }
        }

        if (this.footer != null) {
            list3 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for (final String s2 : list3) {
                l4 = Math.max(l4, this.mc.fontRendererObj.getStringWidth(s2));
            }
        }

        if (list2 != null) {
            drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + list2.size() * this.mc.fontRendererObj.getHeight(), this.tabTransparency ? 855638016 : Integer.MIN_VALUE);

            for (final String s3 : list2) {
                final int i4 = this.mc.fontRendererObj.getStringWidth(s3);
                this.mc.fontRendererObj.drawStringWithShadow(s3, (float) (width / 2 - i4 / 2), (float) k2, this.tabTransparency ? 1308622847 : -1);
                k2 += this.mc.fontRendererObj.getHeight();
            }

            ++k2;
        }

        drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + i2 * 9, this.tabTransparency ? 855638016 : Integer.MIN_VALUE);

        for (int k3 = 0; k3 < l3; ++k3) {
            final int l5 = k3 / i2;
            final int i5 = k3 % i2;
            int j4 = j3 + l5 * i3 + l5 * 5;
            final int k4 = k2 + i5 * 9;

            drawRect(j4, k4, j4 + i3, k4 + 8, 553648127);

            GlStateManager.color(1.0f, 1.0f, 1.0f, this.tabTransparency ? 0.3f : 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (k3 < list.size()) {
                final NetworkPlayerInfo networkInfo = list.get(k3);
                String s4 = this.getPlayerDisplayName(networkInfo);
                final GameProfile gameProfile = networkInfo.getGameProfile();

                if (flag) {
                    final EntityPlayer entityplayer = this.mc.world.getPlayerEntityByUUID(gameProfile.getId());
                    final boolean flag2 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));

                    this.mc.getTextureManager().bindTexture(networkInfo.getLocationSkin());

                    final int l6 = 8 + (flag2 ? 8 : 0);
                    final int i6 = 8 * (flag2 ? -1 : 1);

                    Gui.drawScaledCustomSizeModalRect(j4, k4, 8.0f, (float) l6, 8, i6, 8, 8, 64.0f, 64.0f);

                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                        final int j5 = 8 + (flag2 ? 8 : 0);
                        final int k5 = 8 * (flag2 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(j4, k4, 40.0f, (float) j5, 8, k5, 8, 8, 64.0f, 64.0f);
                    }

                    j4 += 9;
                }

                if (networkInfo.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    s4 = EnumChatFormatting.ITALIC + s4;
                    this.mc.fontRendererObj.drawStringWithShadow(s4, (float) j4, (float) k4, this.tabTransparency ? 872415231 : -1862270977);
                } else {
                    this.mc.fontRendererObj.drawStringWithShadow(s4, (float) j4, (float) k4, this.tabTransparency ? 1895825407 : -1);
                }

                if (scoreObjectiveIn != null && networkInfo.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    final int k6 = j4 + i + 1;
                    final int l7 = k6 + m;

                    if (l7 - k6 > 5) {
                        try {
                            drawScoreboardValues(scoreObjectiveIn, k4, gameProfile.getName(), k6, l7, networkInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                drawPing(i3, j4 - (flag ? 9 : 0), k4, networkInfo);
            }
        }
        if (list3 != null) {
            k2 = k2 + i2 * 9 + 1;
            drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + list3.size() * this.mc.fontRendererObj.getHeight(), this.tabTransparency ? 855638016 : Integer.MIN_VALUE);

            for (final String s5 : list3) {
                final int j6 = this.mc.fontRendererObj.getStringWidth(s5);
                this.mc.fontRendererObj.drawStringWithShadow(s5, (float) (width / 2 - j6 / 2), (float) k2, this.tabTransparency ? 1308622847 : -1);
                k2 += this.mc.fontRendererObj.getHeight();
            }
        }
    }

    protected void drawPing(final int p_175245_1_,
                            final int p_175245_2_,
                            final int p_175245_3_,
                            final NetworkPlayerInfo networkPlayerInfoIn) {
        int ping = networkPlayerInfoIn.getResponseTime();

        final ServerData currentServerData = this.mc.getCurrentServerData();

        this.zLevel += 100.0f;

        if (showPingInTab) {
            int color;

            if (ping > 500) {
                color = 0xAA0000;
            } else if (ping > 300) {
                color = 0xAAAA00;
            } else if (ping > 200) {
                color = 0xAACC00;
            } else if (ping > 135) {
                color = 0x207B00;
            } else if (ping > 70) {
                color = 0x009900;
            } else if (ping > 0) {
                color = 0x00BB00;
            } else {
                color = 0xAA0000;
            }

            if (ping > 0 && ping < 10_000) {
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.5f);

                final int x = p_175245_2_ + p_175245_1_ - (this.mc.fontRendererObj.getStringWidth(String.valueOf(ping)) >> 1) - 2;
                final int y = p_175245_3_ + (this.mc.fontRendererObj.getHeight() >> 2);
                this.mc.fontRendererObj.drawStringWithShadow(Integer.toString(ping), (float) (2 * x), (float) (2 * y), color);

                GL11.glScalef(2.0f, 2.0f, 2.0f);
                GL11.glPopMatrix();
            }
        } else {
            GlStateManager.color(1.0f, 1.0f, 1.0f, this.tabTransparency ? 0.3f : 1.0f);
            this.mc.getTextureManager().bindTexture(icons);
            int j;

            if (ping < 0) {
                j = 5;
            } else if (ping < 300) {
                j = 1;
            } else if (ping < 600) {
                j = 2;
            } else if (ping < 1000) {
                j = 3;
            } else {
                j = 4;
            }

            drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0, 176 + j * 8, 10, 8);
        }

        this.zLevel -= 100.0f;
    }

//	protected void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, @NotNull NetworkPlayerInfo networkInfo) {
//		final int ping = networkInfo.getResponseTime();
//		this.zLevel += 100.0f;
//
//		final int colour;
//
//		if(ping > 500) {
//			colour = 0xAA0000;
//		} else if(ping > 300) {
//			colour = 0xAAAA00;
//		} else if(ping > 200) {
//			colour = 0xAACC00;
//		} else if(ping > 135) {
//			colour = 0x207B00;
//		} else if(ping > 70) {
//			colour = 0x009900;
//		} else if(ping > 0) {
//			colour = 0x00BB00;
//		} else {
//			colour = 0xAA0000;
//		}
//
//		if(ping > 0 && ping < 10000) {
//			GL11.glPushMatrix();
//			GL11.glScalef(0.5f, 0.5f, 0.5f);
//
//			final float x = p_175245_2_ + p_175245_1_ - (this.mc.fontRendererObj.getStringWidth(ping + "") >> 1) - 2;
//			final float y = p_175245_3_ + (this.mc.fontRendererObj.FONT_HEIGHT >> 2);
//			this.mc.fontRendererObj.drawStringWithShadow(Integer.toString(ping), 2 * x, 2 * y, colour);
//
//			GL11.glScalef(2.0f, 2.0f, 2.0f);
//			GL11.glPopMatrix();
//		}
//
//		this.zLevel -= 100.0f;
//	}

    private void drawScoreboardValues(@NotNull ScoreObjective objective,
                                      int p_175247_2_,
                                      String p_175247_3_,
                                      int p_175247_4_,
                                      int p_175247_5_,
                                      NetworkPlayerInfo networkInfo) {
        int i = objective.getScoreboard().getValueFromObjective(p_175247_3_, objective).getScorePoints();

        if (objective.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            this.mc.getTextureManager().bindTexture(icons);

            if (this.lastTimeOpened == networkInfo.func_178855_p()) {
                if (i < networkInfo.func_178835_l()) {
                    networkInfo.func_178846_a(Minecraft.getSystemTime());
                    networkInfo.func_178844_b(this.guiIngame.getUpdateCounter() + 20);
                } else if (i > networkInfo.func_178835_l()) {
                    networkInfo.func_178846_a(Minecraft.getSystemTime());
                    networkInfo.func_178844_b(this.guiIngame.getUpdateCounter() + 10);
                }
            }

            if (Minecraft.getSystemTime() - networkInfo.func_178847_n() > 1000L || this.lastTimeOpened != networkInfo.func_178855_p()) {
                networkInfo.func_178836_b(i);
                networkInfo.func_178857_c(i);
                networkInfo.func_178846_a(Minecraft.getSystemTime());
            }

            networkInfo.func_178843_c(this.lastTimeOpened);
            networkInfo.func_178836_b(i);
            int j = MathHelper.ceiling_float_int((float) Math.max(i, networkInfo.func_178860_m()) / 2.0F);
            int k = Math.max(MathHelper.ceiling_float_int((float) (i / 2)), Math.max(MathHelper.ceiling_float_int((float) (networkInfo.func_178860_m() / 2)), 10));
            boolean flag = networkInfo.func_178858_o() > (long) this.guiIngame.getUpdateCounter() && (networkInfo.func_178858_o() - (long) this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;

            if (j > 0) {
                float f = Math.min((float) (p_175247_5_ - p_175247_4_ - 4) / (float) k, 9.0F);

                if (f > 3.0F) {
                    for (int l = j; l < k; ++l) {
                        drawTexturedModalRect((float) p_175247_4_ + (float) l * f, (float) p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }

                    for (int j1 = 0; j1 < j; ++j1) {
                        drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, flag ? 25 : 16, 0, 9, 9);

                        if (flag) {
                            if (j1 * 2 + 1 < networkInfo.func_178860_m()) {
                                drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, 70, 0, 9, 9);
                            }

                            if (j1 * 2 + 1 == networkInfo.func_178860_m()) {
                                drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, 79, 0, 9, 9);
                            }
                        }

                        if (j1 * 2 + 1 < i) {
                            drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9);
                        }

                        if (j1 * 2 + 1 == i) {
                            drawTexturedModalRect((float) p_175247_4_ + (float) j1 * f, (float) p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                } else {
                    final float f1 = MathHelper.clamp_float((float) i / 20.0F, 0.0F, 1.0F);
                    final int i1 = (int) ((1.0F - f1) * 255.0F) << 16 | (int) (f1 * 255.0F) << 8;
                    String s = "" + (float) i / 2.0F;

                    if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_) {
                        s += "hp";
                    }

                    this.mc.fontRendererObj.drawStringWithShadow(s, (float) ((p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2), (float) p_175247_2_, i1);
                }
            }
        } else {
            final String s1 = EnumChatFormatting.YELLOW + "" + i;
            this.mc.fontRendererObj.drawStringWithShadow(s1, (float) (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s1)), (float) p_175247_2_, 16777215);
        }
    }

    public void setFooter(IChatComponent footerIn) {
        this.footer = footerIn;
    }

    public void setHeader(IChatComponent headerIn) {
        this.header = headerIn;
    }

    public void func_181030_a() {
        this.header = null;
        this.footer = null;
    }

    public static class PlayerComparator implements Comparator<NetworkPlayerInfo> {

        public PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }

    }

}

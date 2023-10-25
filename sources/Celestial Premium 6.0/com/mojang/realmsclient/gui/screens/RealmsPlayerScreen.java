/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.gui.screens.RealmsActivityScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfirmScreen;
import com.mojang.realmsclient.gui.screens.RealmsInviteScreen;
import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class RealmsPlayerScreen
extends RealmsScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String OP_ICON_LOCATION = "realms:textures/gui/realms/op_icon.png";
    private static final String USER_ICON_LOCATION = "realms:textures/gui/realms/user_icon.png";
    private static final String CROSS_ICON_LOCATION = "realms:textures/gui/realms/cross_player_icon.png";
    private String toolTip;
    private final RealmsConfigureWorldScreen lastScreen;
    private final RealmsServer serverData;
    private InvitedSelectionList invitedSelectionList;
    private int column1_x;
    private int column_width;
    private int column2_x;
    private static final int BUTTON_BACK_ID = 0;
    private static final int BUTTON_INVITE_ID = 1;
    private static final int BUTTON_UNINVITE_ID = 2;
    private static final int BUTTON_ACTIVITY_ID = 3;
    private int selectedInvitedIndex = -1;
    private String selectedInvited;
    private boolean stateChanged;

    public RealmsPlayerScreen(RealmsConfigureWorldScreen lastScreen, RealmsServer serverData) {
        this.lastScreen = lastScreen;
        this.serverData = serverData;
    }

    @Override
    public void mouseEvent() {
        super.mouseEvent();
        if (this.invitedSelectionList != null) {
            this.invitedSelectionList.mouseEvent();
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void init() {
        this.column1_x = this.width() / 2 - 160;
        this.column_width = 150;
        this.column2_x = this.width() / 2 + 12;
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(RealmsPlayerScreen.newButton(1, this.column2_x, RealmsConstants.row(1), this.column_width + 10, 20, RealmsPlayerScreen.getLocalizedString("mco.configure.world.buttons.invite")));
        RealmsButton playerActivityButton = RealmsPlayerScreen.newButton(3, this.column2_x, RealmsConstants.row(3), this.column_width + 10, 20, RealmsPlayerScreen.getLocalizedString("mco.configure.world.buttons.activity"));
        playerActivityButton.active(false);
        this.buttonsAdd(playerActivityButton);
        this.buttonsAdd(RealmsPlayerScreen.newButton(0, this.column2_x + this.column_width / 2 + 2, RealmsConstants.row(12), this.column_width / 2 + 10 - 2, 20, RealmsPlayerScreen.getLocalizedString("gui.back")));
        this.invitedSelectionList = new InvitedSelectionList();
        this.invitedSelectionList.setLeftPos(this.column1_x);
    }

    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void buttonClicked(RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 0: {
                this.backButtonClicked();
                break;
            }
            case 1: {
                Realms.setScreen(new RealmsInviteScreen(this.lastScreen, this, this.serverData));
                break;
            }
            case 3: {
                Realms.setScreen(new RealmsActivityScreen(this, this.serverData));
                break;
            }
            default: {
                return;
            }
        }
    }

    @Override
    public void keyPressed(char ch, int eventKey) {
        if (eventKey == 1) {
            this.backButtonClicked();
        }
    }

    private void backButtonClicked() {
        if (this.stateChanged) {
            Realms.setScreen(this.lastScreen.getNewScreen());
        } else {
            Realms.setScreen(this.lastScreen);
        }
    }

    private void op(int index) {
        RealmsClient client = RealmsClient.createRealmsClient();
        String selectedInvite = this.serverData.players.get(index).getUuid();
        try {
            this.updateOps(client.op(this.serverData.id, selectedInvite));
        }
        catch (RealmsServiceException ignored) {
            LOGGER.error("Couldn't op the user");
        }
    }

    private void deop(int index) {
        RealmsClient client = RealmsClient.createRealmsClient();
        String selectedInvite = this.serverData.players.get(index).getUuid();
        try {
            this.updateOps(client.deop(this.serverData.id, selectedInvite));
        }
        catch (RealmsServiceException ignored) {
            LOGGER.error("Couldn't deop the user");
        }
    }

    private void updateOps(Ops ops) {
        for (PlayerInfo playerInfo : this.serverData.players) {
            playerInfo.setOperator(ops.ops.contains(playerInfo.getName()));
        }
    }

    private void uninvite(int index) {
        if (index >= 0 && index < this.serverData.players.size()) {
            PlayerInfo playerInfo = this.serverData.players.get(index);
            this.selectedInvited = playerInfo.getUuid();
            this.selectedInvitedIndex = index;
            RealmsConfirmScreen confirmScreen = new RealmsConfirmScreen(this, "Question", RealmsPlayerScreen.getLocalizedString("mco.configure.world.uninvite.question") + " '" + playerInfo.getName() + "' ?", 2);
            Realms.setScreen(confirmScreen);
        }
    }

    @Override
    public void confirmResult(boolean result, int id) {
        if (id == 2) {
            if (result) {
                RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    client.uninvite(this.serverData.id, this.selectedInvited);
                }
                catch (RealmsServiceException ignored) {
                    LOGGER.error("Couldn't uninvite user");
                }
                this.deleteFromInvitedList(this.selectedInvitedIndex);
            }
            this.stateChanged = true;
            Realms.setScreen(this);
        }
    }

    private void deleteFromInvitedList(int selectedInvitedIndex) {
        this.serverData.players.remove(selectedInvitedIndex);
    }

    @Override
    public void render(int xm, int ym, float a) {
        this.toolTip = null;
        this.renderBackground();
        if (this.invitedSelectionList != null) {
            this.invitedSelectionList.render(xm, ym, a);
        }
        int bottomBorder = RealmsConstants.row(12) + 20;
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tezzelator t = Tezzelator.instance;
        RealmsPlayerScreen.bind("textures/gui/options_background.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float s = 32.0f;
        t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
        t.vertex(0.0, this.height(), 0.0).tex(0.0, (float)(this.height() - bottomBorder) / 32.0f + 0.0f).color(64, 64, 64, 255).endVertex();
        t.vertex(this.width(), this.height(), 0.0).tex((float)this.width() / 32.0f, (float)(this.height() - bottomBorder) / 32.0f + 0.0f).color(64, 64, 64, 255).endVertex();
        t.vertex(this.width(), bottomBorder, 0.0).tex((float)this.width() / 32.0f, 0.0).color(64, 64, 64, 255).endVertex();
        t.vertex(0.0, bottomBorder, 0.0).tex(0.0, 0.0).color(64, 64, 64, 255).endVertex();
        t.end();
        this.drawCenteredString(RealmsPlayerScreen.getLocalizedString("mco.configure.world.players.title"), this.width() / 2, 17, 0xFFFFFF);
        if (this.serverData != null && this.serverData.players != null) {
            this.drawString(RealmsPlayerScreen.getLocalizedString("mco.configure.world.invited") + " (" + this.serverData.players.size() + ")", this.column1_x, RealmsConstants.row(0), 0xA0A0A0);
        } else {
            this.drawString(RealmsPlayerScreen.getLocalizedString("mco.configure.world.invited"), this.column1_x, RealmsConstants.row(0), 0xA0A0A0);
        }
        this.drawString(RealmsPlayerScreen.getLocalizedString("mco.configure.world.activityfeed.disabled"), this.column2_x, RealmsConstants.row(5), 0xA0A0A0);
        super.render(xm, ym, a);
        if (this.serverData == null) {
            return;
        }
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
    }

    protected void renderMousehoverTooltip(String msg, int x, int y) {
        if (msg == null) {
            return;
        }
        int rx = x + 12;
        int ry = y - 12;
        int width = this.fontWidth(msg);
        this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
        this.fontDrawShadow(msg, rx, ry, 0xFFFFFF);
    }

    private void drawRemoveIcon(int x, int y, int xm, int ym) {
        boolean hovered = xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < RealmsConstants.row(12) + 20 && ym > RealmsConstants.row(1);
        RealmsPlayerScreen.bind(CROSS_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, hovered ? 7.0f : 0.0f, 8, 7, 8.0f, 14.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsPlayerScreen.getLocalizedString("mco.configure.world.invites.remove.tooltip");
        }
    }

    private void drawOpped(int x, int y, int xm, int ym) {
        boolean hovered = xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < RealmsConstants.row(12) + 20 && ym > RealmsConstants.row(1);
        RealmsPlayerScreen.bind(OP_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, hovered ? 8.0f : 0.0f, 8, 8, 8.0f, 16.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsPlayerScreen.getLocalizedString("mco.configure.world.invites.ops.tooltip");
        }
    }

    private void drawNormal(int x, int y, int xm, int ym) {
        boolean hovered = xm >= x && xm <= x + 9 && ym >= y && ym <= y + 9 && ym < RealmsConstants.row(12) + 20 && ym > RealmsConstants.row(1);
        RealmsPlayerScreen.bind(USER_ICON_LOCATION);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, hovered ? 8.0f : 0.0f, 8, 8, 8.0f, 16.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsPlayerScreen.getLocalizedString("mco.configure.world.invites.normal.tooltip");
        }
    }

    private class InvitedSelectionList
    extends RealmsClickableScrolledSelectionList {
        public InvitedSelectionList() {
            super(RealmsPlayerScreen.this.column_width + 10, RealmsConstants.row(12) + 20, RealmsConstants.row(1), RealmsConstants.row(12) + 20, 13);
        }

        @Override
        public void customMouseEvent(int y0, int y1, int headerHeight, float yo, int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                int x0 = RealmsPlayerScreen.this.column1_x;
                int x1 = RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width;
                int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x1 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                }
            }
        }

        @Override
        public void itemClicked(int clickSlotPos, int slot, int xm, int ym, int width) {
            if (slot < 0 || slot > ((RealmsPlayerScreen)RealmsPlayerScreen.this).serverData.players.size() || RealmsPlayerScreen.this.toolTip == null) {
                return;
            }
            if (RealmsPlayerScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.configure.world.invites.ops.tooltip")) || RealmsPlayerScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.configure.world.invites.normal.tooltip"))) {
                if (((RealmsPlayerScreen)RealmsPlayerScreen.this).serverData.players.get(slot).isOperator()) {
                    RealmsPlayerScreen.this.deop(slot);
                } else {
                    RealmsPlayerScreen.this.op(slot);
                }
            } else if (RealmsPlayerScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.configure.world.invites.remove.tooltip"))) {
                RealmsPlayerScreen.this.uninvite(slot);
            }
        }

        @Override
        public void renderBackground() {
            RealmsPlayerScreen.this.renderBackground();
        }

        @Override
        public int getScrollbarPosition() {
            return RealmsPlayerScreen.this.column1_x + this.width() - 5;
        }

        @Override
        public int getItemCount() {
            return RealmsPlayerScreen.this.serverData == null ? 1 : ((RealmsPlayerScreen)RealmsPlayerScreen.this).serverData.players.size();
        }

        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 13;
        }

        @Override
        protected void renderItem(int i, int x, int y, int h, Tezzelator t, int mouseX, int mouseY) {
            if (RealmsPlayerScreen.this.serverData == null) {
                return;
            }
            if (i < ((RealmsPlayerScreen)RealmsPlayerScreen.this).serverData.players.size()) {
                this.renderInvitedItem(i, y);
            }
        }

        private void renderInvitedItem(int i, int y) {
            PlayerInfo invited = ((RealmsPlayerScreen)RealmsPlayerScreen.this).serverData.players.get(i);
            int inviteColor = !invited.getAccepted() ? 0xA0A0A0 : (invited.getOnline() ? 0x7FFF7F : 0xFFFFFF);
            RealmsPlayerScreen.this.drawString(invited.getName(), RealmsPlayerScreen.this.column1_x + 3 + 12, y + 1, inviteColor);
            if (invited.isOperator()) {
                RealmsPlayerScreen.this.drawOpped(RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width - 10, y + 1, this.xm(), this.ym());
            } else {
                RealmsPlayerScreen.this.drawNormal(RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width - 10, y + 1, this.xm(), this.ym());
            }
            RealmsPlayerScreen.this.drawRemoveIcon(RealmsPlayerScreen.this.column1_x + RealmsPlayerScreen.this.column_width - 22, y + 2, this.xm(), this.ym());
            RealmsTextureManager.bindFace(invited.getUuid());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(RealmsPlayerScreen.this.column1_x + 2 + 2, y + 1, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            RealmsScreen.blit(RealmsPlayerScreen.this.column1_x + 2 + 2, y + 1, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
        }
    }
}


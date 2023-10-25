/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.spectator;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.BaseSpectatorGroup;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class SpectatorMenu {
    private static final ISpectatorMenuObject CLOSE_ITEM = new EndSpectatorObject();
    private static final ISpectatorMenuObject SCROLL_LEFT = new MoveMenuObject(-1, true);
    private static final ISpectatorMenuObject SCROLL_RIGHT_ENABLED = new MoveMenuObject(1, true);
    private static final ISpectatorMenuObject SCROLL_RIGHT_DISABLED = new MoveMenuObject(1, false);
    public static final ISpectatorMenuObject EMPTY_SLOT = new ISpectatorMenuObject(){

        @Override
        public void selectItem(SpectatorMenu menu) {
        }

        @Override
        public ITextComponent getSpectatorName() {
            return new TextComponentString("");
        }

        @Override
        public void renderIcon(float p_178663_1_, int alpha) {
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    };
    private final ISpectatorMenuRecipient listener;
    private final List<SpectatorDetails> previousCategories = Lists.newArrayList();
    private ISpectatorMenuView category = new BaseSpectatorGroup();
    private int selectedSlot = -1;
    private int page;

    public SpectatorMenu(ISpectatorMenuRecipient p_i45497_1_) {
        this.listener = p_i45497_1_;
    }

    public ISpectatorMenuObject getItem(int p_178643_1_) {
        int i = p_178643_1_ + this.page * 6;
        if (this.page > 0 && p_178643_1_ == 0) {
            return SCROLL_LEFT;
        }
        if (p_178643_1_ == 7) {
            return i < this.category.getItems().size() ? SCROLL_RIGHT_ENABLED : SCROLL_RIGHT_DISABLED;
        }
        if (p_178643_1_ == 8) {
            return CLOSE_ITEM;
        }
        return i >= 0 && i < this.category.getItems().size() ? MoreObjects.firstNonNull(this.category.getItems().get(i), EMPTY_SLOT) : EMPTY_SLOT;
    }

    public List<ISpectatorMenuObject> getItems() {
        ArrayList<ISpectatorMenuObject> list = Lists.newArrayList();
        for (int i = 0; i <= 8; ++i) {
            list.add(this.getItem(i));
        }
        return list;
    }

    public ISpectatorMenuObject getSelectedItem() {
        return this.getItem(this.selectedSlot);
    }

    public ISpectatorMenuView getSelectedCategory() {
        return this.category;
    }

    public void selectSlot(int slotIn) {
        ISpectatorMenuObject ispectatormenuobject = this.getItem(slotIn);
        if (ispectatormenuobject != EMPTY_SLOT) {
            if (this.selectedSlot == slotIn && ispectatormenuobject.isEnabled()) {
                ispectatormenuobject.selectItem(this);
            } else {
                this.selectedSlot = slotIn;
            }
        }
    }

    public void exit() {
        this.listener.onSpectatorMenuClosed(this);
    }

    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    public void selectCategory(ISpectatorMenuView menuView) {
        this.previousCategories.add(this.getCurrentPage());
        this.category = menuView;
        this.selectedSlot = -1;
        this.page = 0;
    }

    public SpectatorDetails getCurrentPage() {
        return new SpectatorDetails(this.category, this.getItems(), this.selectedSlot);
    }

    static class MoveMenuObject
    implements ISpectatorMenuObject {
        private final int direction;
        private final boolean enabled;

        public MoveMenuObject(int p_i45495_1_, boolean p_i45495_2_) {
            this.direction = p_i45495_1_;
            this.enabled = p_i45495_2_;
        }

        @Override
        public void selectItem(SpectatorMenu menu) {
            menu.page = menu.page + this.direction;
        }

        @Override
        public ITextComponent getSpectatorName() {
            return this.direction < 0 ? new TextComponentTranslation("spectatorMenu.previous_page", new Object[0]) : new TextComponentTranslation("spectatorMenu.next_page", new Object[0]);
        }

        @Override
        public void renderIcon(float p_178663_1_, int alpha) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
            if (this.direction < 0) {
                Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 144.0f, 0.0f, 16.0f, 16.0f, 256.0f, 256.0f);
            } else {
                Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 160.0f, 0.0f, 16.0f, 16.0f, 256.0f, 256.0f);
            }
        }

        @Override
        public boolean isEnabled() {
            return this.enabled;
        }
    }

    static class EndSpectatorObject
    implements ISpectatorMenuObject {
        private EndSpectatorObject() {
        }

        @Override
        public void selectItem(SpectatorMenu menu) {
            menu.exit();
        }

        @Override
        public ITextComponent getSpectatorName() {
            return new TextComponentTranslation("spectatorMenu.close", new Object[0]);
        }

        @Override
        public void renderIcon(float p_178663_1_, int alpha) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
            Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 128.0f, 0.0f, 16.0f, 16.0f, 256.0f, 256.0f);
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}


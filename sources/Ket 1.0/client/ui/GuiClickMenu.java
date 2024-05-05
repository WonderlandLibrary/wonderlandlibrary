package client.ui;

import client.Client;
import client.module.Module;
import client.module.ModuleManager;
import client.util.ChatUtil;
import client.value.Mode;
import client.value.Value;
import client.value.impl.BooleanValue;
import client.value.impl.ModeValue;
import client.value.impl.NumberValue;
import client.value.impl.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiClickMenu extends GuiScreen {

    private final GuiScreen parent;
    private final List<GuiTextField> fieldList = new ArrayList<>();
    private Module module;
    private int moduleIndex;

    public GuiClickMenu(final GuiScreen parent) {
        this.parent = parent;
        allowUserInput = true;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        fieldList.forEach(guiTextField -> guiTextField.mouseClicked(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1) {
            for (int index = 0; index < buttonList.size(); index++) {
                final GuiButton button = buttonList.get(index);
                if (buttonList.get(index).mousePressed(mc, mouseX, mouseY)) {
                    if (button.id < Client.INSTANCE.getModuleManager().size()) {
                        module = Client.INSTANCE.getModuleManager().get(button.id);
                        moduleIndex = button.id;
                        buttonList.get(index).playPressSound(mc.getSoundHandler());
                        buttonList.clear();
                        final AtomicInteger valueIndex = new AtomicInteger();
                        module.getAllValues().forEach(value -> {
                            final int j = Client.INSTANCE.getModuleManager().size() + valueIndex.get(), x = width / 2 - 49, y = height / 2 - 12 * module.getAllValues().size() + 24 * valueIndex.get() + 2, w = 98, h = 20;
                            if (value instanceof BooleanValue) buttonList.add(new GuiButton(j, x, y, w, h, (((BooleanValue) value).getValue() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + value.getName()));
                            if (value instanceof ModeValue) buttonList.add(new GuiButton(j, x, y, w, h, value.getName() + ": " + ((ModeValue) value).getValue().getName()));
                            if (value instanceof NumberValue) {
                                fieldList.add(new GuiTextField(j, mc.fontRendererObj, x, y, w, h));
                                fieldList.stream().filter(guiTextField -> guiTextField.getId() == j).forEach(guiTextField -> guiTextField.setText(String.valueOf(((NumberValue) value).getValue().floatValue())));
                            }
                            if (value instanceof StringValue) {
                                fieldList.add(new GuiTextField(j, mc.fontRendererObj, x, y, w, h));
                                fieldList.stream().filter(guiTextField -> guiTextField.getId() == j).forEach(guiTextField -> guiTextField.setText(((StringValue) value).getValue()));
                            }
                            valueIndex.getAndIncrement();
                        });
                    } else {
                        final Value<?> value = module.getAllValues().get(button.id - Client.INSTANCE.getModuleManager().size());
                        if (value instanceof BooleanValue) ((BooleanValue) value).setValue(!((BooleanValue) value).getValue());
                        if (value instanceof ModeValue) {
                            final ModeValue modeValue = (ModeValue) value;
                            final List<Mode<?>> modeList = modeValue.getModes();
                            for (int i = 0; i < modeList.size(); i++) {
                                if (modeValue.getValue().equals(modeList.get(i))) {
                                    if (i - 1 < 0) modeValue.update(modeList.get(modeList.size() - 1)); else modeValue.update(modeList.get(i - 1));
                                    break;
                                }
                            }
                        }
                        final GuiClickMenu instance = new GuiClickMenu(parent);
                        mc.displayGuiScreen(instance);
                        for (int i = 0; i < instance.buttonList.size(); i++) {
                            final GuiButton guiButton = instance.buttonList.get(i);
                            if (guiButton.id == moduleIndex) instance.mouseClicked(guiButton.xPosition, guiButton.yPosition, 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        fieldList.forEach(GuiTextField::drawTextBox);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        fieldList.stream().filter(GuiTextField::isFocused).forEach(guiTextField -> guiTextField.textboxKeyTyped(typedChar, keyCode));
        switch (keyCode) {
            case 1: {
                if (module == null) super.keyTyped(typedChar, keyCode); else mc.displayGuiScreen(new GuiClickMenu(parent));
                break;
            }
            case 28: {
                fieldList.stream().filter(GuiTextField::isFocused).forEach(guiTextField -> {
                    final Value<?> value = module.getAllValues().get(guiTextField.getId() - Client.INSTANCE.getModuleManager().size());
                    if (value instanceof NumberValue) new Thread(() -> ((NumberValue) value).setValue(new Number() {
                        @Override
                        public int intValue() {
                            return Integer.parseInt(guiTextField.getText());
                        }

                        @Override
                        public long longValue() {
                            return Long.parseLong(guiTextField.getText());
                        }

                        @Override
                        public float floatValue() {
                            return Float.parseFloat(guiTextField.getText());
                        }

                        @Override
                        public double doubleValue() {
                            return Double.parseDouble(guiTextField.getText());
                        }
                    })).start();
                    if (value instanceof StringValue) ((StringValue) value).setValue(guiTextField.getText());
                });
                break;
            }
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
        int c = 0, e = 0, m = 0, o = 0, p = 0, r = 0;
        final AtomicInteger max = new AtomicInteger();
        moduleManager.forEach(module -> max.set(Math.max(moduleManager.get(module.getInfo().category()).size(), max.get())));
        for (int i = 0; i < moduleManager.size(); i++) {
            final Module module = moduleManager.get(i);
            switch (module.getInfo().category()) {
                case COMBAT: {
                    buttonList.add(new GuiButton(i, width / 2 - 304, height / 2 - 12 * max.get() + 24 * c + 2, 98, 20, (module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + module.getInfo().name()));
                    c++;
                    break;
                }
                case EXPLOIT: {
                    buttonList.add(new GuiButton(i, width / 2 - 202, height / 2 - 12 * max.get() + 24 * e + 2, 98, 20, (module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + module.getInfo().name()));
                    e++;
                    break;
                }
                case MOVEMENT: {
                    buttonList.add(new GuiButton(i, width / 2 - 100, height / 2 - 12 * max.get() + 24 * m + 2, 98, 20, (module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + module.getInfo().name()));
                    m++;
                    break;
                }
                case OTHER: {
                    buttonList.add(new GuiButton(i, width / 2 + 2, height / 2 - 12 * max.get() + 24 * o + 2, 98, 20, (module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + module.getInfo().name()));
                    o++;
                    break;
                }
                case PLAYER: {
                    buttonList.add(new GuiButton(i, width / 2 + 104, height / 2 - 12 * max.get() + 24 * p + 2, 98, 20, (module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + module.getInfo().name()));
                    p++;
                    break;
                }
                case RENDER: {
                    buttonList.add(new GuiButton(i, width / 2 + 206, height / 2 - 12 * max.get() + 24 * r + 2, 98, 20, (module.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + module.getInfo().name()));
                    r++;
                    break;
                }
            }
        }
    }

    @Override
    public void updateScreen() {
        fieldList.forEach(GuiTextField::updateCursorCounter);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id < Client.INSTANCE.getModuleManager().size()) {
            Client.INSTANCE.getModuleManager().get(button.id).toggle();
            mc.displayGuiScreen(new GuiClickMenu(parent));
        } else {
            final Value<?> value = module.getAllValues().get(button.id - Client.INSTANCE.getModuleManager().size());
            if (value instanceof BooleanValue) ((BooleanValue) value).setValue(!((BooleanValue) value).getValue());
            if (value instanceof ModeValue) {
                final ModeValue modeValue = (ModeValue) value;
                final List<Mode<?>> modeList = modeValue.getModes();
                for (int i = 0; i < modeList.size(); i++) {
                    if (modeValue.getValue().equals(modeList.get(i))) {
                        if (i + 1 < modeList.size()) modeValue.update(modeList.get(i + 1)); else modeValue.update(modeList.get(0));
                        break;
                    }
                }
            }
            final GuiClickMenu instance = new GuiClickMenu(parent);
            mc.displayGuiScreen(instance);
            for (int i = 0; i < instance.buttonList.size(); i++) {
                final GuiButton guiButton = instance.buttonList.get(i);
                if (guiButton.id == moduleIndex) instance.mouseClicked(guiButton.xPosition, guiButton.yPosition, 1);
            }
        }
    }
}

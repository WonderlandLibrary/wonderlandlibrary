package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SignInsert extends Screen {


    @Subscribe
    public void GuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiEditSign) {
            event.setGui(new BetterSignGui(((GuiEditSign) event.getGui()).tileSign));
        }
    }

    class BetterSignGui extends GuiScreen {
        private int focusedField = 0;
        public final TileEntitySign sign;
        private List<GuiTextField> textFields;
        private String[] defaultStrings;

        public BetterSignGui(TileEntitySign sign) {
            this.sign = sign;
        }

        @Override
        public void initGui() {
            buttonList.clear();
            Keyboard.enableRepeatEvents(true);

            textFields = new LinkedList<>();
            defaultStrings = new String[4];
            for (int i = 0; i < 4; i++) {
                GuiTextField field = new GuiTextField(i, fontRenderer, width / 2 + 4, 75 + i * 24, 120, 20);
                field.setValidator(this::validateText);
                field.setMaxStringLength(100);

                String text = sign.signText[i].getUnformattedText();
                defaultStrings[i] = text;
                field.setText(text);

                textFields.add(field);
            }
            textFields.get(focusedField).setFocused(true);

            addButton(new GuiButton(4, width / 2 + 5, height / 4 + 120, 120, 20, "Done"));

            addButton(new GuiButton(5, width / 2 - 125, height / 4 + 120, 120, 20, "Cancel"));

            addButton(new GuiButton(6, width / 2 - 41, 147, 40, 20, "Shift"));

            addButton(new GuiButton(7, width / 2 - 41, 123, 40, 20, "Clear"));

            addButton(new GuiButton(8, width / 2 + 130, 124, 40, 20, "Lag"));

            addButton(new GuiButton(9, width / 2 + 130, 99, 40, 20, "FillMax"));

            for (int i = 0; i < 22; i++) {
                addButton(new GuiButton(i + 11, width / 2 + 130 + ((i % 5) * 15), 150 + 65 - (i / 5) * 15, 15, 15, ColorfromInt(i) + "&A"));
            }


            sign.setEditable(false);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);

            textFields.forEach((field) -> {
                field.mouseClicked(mouseX, mouseY, mouseButton);
                if (field.isFocused())
                    focusedField = field.getId();
            });

            if (!textFields.get(focusedField).isFocused())
                textFields.get(focusedField).setFocused(true);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            switch (keyCode) {
                case 1: // Escape
                    exit();
                    break;
                case 15: // Tab
                    int change = isShiftKeyDown() ? -1 : 1;
                    tabFocus(change);
                    break;
                case 200: // Arrow Up
                    tabFocus(-1);
                    break;
                case 28:
                case 156:
                case 208: // Enter or Arrow Down
                    tabFocus(1);
                default:
                    textFields.forEach((field) -> field.textboxKeyTyped(typedChar, keyCode));
                    sign.signText[focusedField] = new TextComponentString(textFields.get(focusedField).getText());
            }
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);
            switch (button.id) {
                case 5: // Cancel
                    for (int i = 0; i < 4; i++)
                        sign.signText[i] = new TextComponentString(defaultStrings[i]);
                case 4: // Done
                    exit();
                    break;
                case 6: // Shift
                    String[] replacements = new String[4];
                    for (int i = 0; i < 4; i++) {
                        int change = isShiftKeyDown() ? 1 : -1;
                        int target = wrapLine(i + change);
                        replacements[i] = sign.signText[target].getUnformattedText();
                    }
                    applytext(replacements);
                    break;
                case 7: // Clear
                    textFields.forEach((field) -> {
                        int id = field.getId();
                        field.setText("");
                        sign.signText[id] = new TextComponentString("");
                    });
                    break;
                case 8:
                    final StringBuilder lagStringBuilder = new StringBuilder();
                    for (int j = 0; j < 500; ++j) {
                        lagStringBuilder.append("/(!§()%/§)=/(!§()%/§)=/(!§()%/§)=");
                    }
                    String[] Builder = new String[4];
                    for (int i = 0; i < 4; i++) {
                        Builder[i] = lagStringBuilder.toString();
                    }
                    applytext(Builder);
                    break;
                case 9:
                    String line = Random();
                    String[] rando = new String[4];
                    for (int i = 0; i < 4; i++) {
                        rando[i] = line.substring(i * 384, (i + 1) * 384);
                    }
                    applytext(rando);
                    break;
                default:
                    if (button.id < 27 && button.id > 10)
                        textFields.get(focusedField).text += "&" + Integer.toHexString(button.id - 11);
                    if (button.id == 27)
                        textFields.get(focusedField).text += "&k";
                    if (button.id == 28)
                        textFields.get(focusedField).text += "&l";
                    if (button.id == 29)
                        textFields.get(focusedField).text += "&m";
                    if (button.id == 30)
                        textFields.get(focusedField).text += "&n";
                    if (button.id == 31)
                        textFields.get(focusedField).text += "&o";
                    if (button.id == 32)
                        textFields.get(focusedField).text += "&r";
                    break;
            }
        }

        void applytext(String[] index) {
            textFields.forEach((field) -> {
                int id = field.getId();
                field.setText(index[id]);
                sign.signText[id] = new TextComponentString(index[id]);
            });

        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();
            drawCenteredString(fontRenderer, I18n.format("sign.edit"), width / 2, 40, 16777215);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) (width / 2) - 63, 0.0F, 50.0F);
            GlStateManager.scale(-93.75F, -93.75F, -93.75F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            Block block = sign.getBlockType();

            if (block == Blocks.STANDING_SIGN) {
                float f1 = (sign.getBlockMetadata() * 360) / 16.0F;
                GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.0625F, 0.0F);
            } else {
                int i = sign.getBlockMetadata();
                float f2 = i == 2 ? 180.0F : i == 4 ? 90.0F : i == 5 ? -90.0F : 0.0F;

                GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.7625F, 0.0F);
            }

            sign.lineBeingEdited = -1;
            TileEntityRendererDispatcher.instance.render(sign, -0.5D, -0.75D, -0.5D, 0.0F);


            GlStateManager.popMatrix();

            textFields.forEach(GuiTextField::drawTextBox);

            super.drawScreen(mouseX, mouseY, partialTicks);
        }


        void exit() {
            sign.markDirty();
            mc.displayGuiScreen(null);
        }

        private String Random() {
            final IntStream gen = new Random().ints(0x80, 0x10ffff - 0x800).map(i -> i < 0xd800 ? i : i + 0x800);
            return gen.limit(4 * 384).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        }

        @Override
        public void onGuiClosed() {
            String[] color = new String[4];
            for (int i = 0; i < 4; i++) {
                color[i] = sign.signText[i].getUnformattedText().replace("&", "\247" + "\247a");
            }
            applytext(color);
            Keyboard.enableRepeatEvents(false);
            NetHandlerPlayClient nethandlerplayclient = this.mc.getConnection();

            if (nethandlerplayclient != null)
                nethandlerplayclient.sendPacket(new CPacketUpdateSign(sign.getPos(), sign.signText));

            sign.setEditable(true);
        }

        void tabFocus(int change) {
            textFields.get(focusedField).setFocused(false);
            focusedField = wrapLine(focusedField + change);
            textFields.get(focusedField).setFocused(true);
        }

        int wrapLine(int line) {
            return line > 3 ? 0 : line < 0 ? 3 : line;
        }

        boolean validateText(String s) {
            if (fontRenderer.getStringWidth(s) > 90)
                return false;

            for (char c : s.toCharArray())
                if (!ChatAllowedCharacters.isAllowedCharacter(c))
                    return false;

            return true;
        }

    }


}

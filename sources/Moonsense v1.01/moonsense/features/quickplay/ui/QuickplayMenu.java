// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.quickplay.ui;

import moonsense.features.quickplay.QuickplayGameMode;
import java.util.stream.Stream;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.util.EnumChatFormatting;
import java.util.Locale;
import java.util.List;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import moonsense.ui.utils.GuiUtils;
import moonsense.utils.Rectangle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import org.lwjgl.input.Keyboard;
import moonsense.MoonsenseClient;
import moonsense.features.quickplay.QuickplayGame;
import moonsense.utils.CustomFontRenderer;
import moonsense.features.modules.QuickplayModule;
import net.minecraft.client.gui.GuiScreen;

public class QuickplayMenu extends GuiScreen
{
    private final QuickplayModule mod;
    private CustomFontRenderer font;
    private String query;
    private int selectedIndex;
    private boolean inAllGames;
    private QuickplayGame currentGame;
    private int maxScrolling;
    private int scroll;
    private boolean mouseDown;
    private boolean wasMouseDown;
    private int lastMouseX;
    private int lastMouseY;
    private int recentGamesScroll;
    private int allGamesScroll;
    private int nextScroll;
    
    public QuickplayMenu(final QuickplayModule mod) {
        this.font = MoonsenseClient.textRenderer;
        this.query = "";
        this.lastMouseX = -1;
        this.lastMouseY = -1;
        this.nextScroll = -1;
        this.mod = mod;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 50).getRGB());
        GlStateManager.enableBlend();
        Rectangle box = new Rectangle(0, 0, 200, 250);
        box = box.offset(this.width / 2 - box.getWidth() / 2, this.height / 2 - box.getHeight() / 2);
        GuiUtils.drawRoundedRect((float)box.getX(), (float)box.getY(), (float)(box.getX() + box.getWidth()), (float)(box.getY() + box.getHeight()), 3.0f, new Color(0, 0, 0, 140).getRGB());
        GuiUtils.drawRoundedOutline(box.getX(), box.getY(), box.getX() + box.getWidth(), box.getY() + box.getHeight(), 3.0f, 2.0f, new Color(125, 136, 142, 150).getRGB());
        this.font.drawString(this.query.isEmpty() ? "Search" : this.query, box.getX() + 10 + (this.query.isEmpty() ? 2 : 0), box.getY() + 10 + 1, this.query.isEmpty() ? -10066330 : -1);
        Gui.drawRect((int)(box.getX() + 10 + this.font.getWidth(this.query)), box.getY() + 10, (int)(box.getX() + 11 + this.font.getWidth(this.query)), box.getY() + 20, -1);
        this.drawHorizontalLine(box.getX(), box.getX() + box.getWidth() - 1, box.getY() + 30, new Color(105, 116, 122, 150).getRGB());
        final Rectangle entriesBox = new Rectangle(box.getX(), box.getY() + 31, box.getWidth(), box.getHeight() - 31);
        final Rectangle base = new Rectangle(entriesBox.getX(), entriesBox.getY(), entriesBox.getWidth(), 20);
        GL11.glEnable(3089);
        GuiUtils.scissorHelper(entriesBox.getX(), entriesBox.getY(), entriesBox.getX() + entriesBox.getWidth(), entriesBox.getY() + entriesBox.getHeight());
        final int x = box.getX();
        int y = 0;
        this.scroll = MathHelper.clamp_int(this.scroll, 0, this.maxScrolling);
        final List<QuickplayOption> options = this.getGames();
        if (this.selectedIndex > options.size() - 1) {
            this.selectedIndex = options.size() - 1;
        }
        if (this.selectedIndex < 0) {
            this.selectedIndex = 0;
        }
        for (int i = 0; i < options.size(); ++i) {
            final QuickplayOption game = options.get(i);
            final Rectangle gameBounds = base.offset(0, y - this.scroll);
            final boolean containsMouse = gameBounds.contains(mouseX, mouseY) && entriesBox.contains(mouseX, mouseY);
            if (this.selectedIndex == i) {
                gameBounds.fill(new Color(117, 125, 130, 150));
                if (containsMouse && this.mouseDown && !this.wasMouseDown) {
                    game.onClick(this, this.mod);
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
                }
            }
            if ((this.lastMouseX != mouseX || this.lastMouseY != mouseY) && this.lastMouseX != -1 && this.lastMouseY != -1 && containsMouse) {
                this.selectedIndex = i;
            }
            if (game.getIcon() != null) {
                RenderHelper.enableGUIStandardItemLighting();
                this.mc.getRenderItem().func_180450_b(game.getIcon(), x + 3, gameBounds.getY() + 1);
                RenderHelper.disableStandardItemLighting();
            }
            this.font.drawString(game.getText(), x + 25, gameBounds.getY() + 4 + 1, -1);
            y += gameBounds.getHeight();
        }
        this.maxScrolling = Math.max(0, y - entriesBox.getHeight());
        GL11.glDisable(3089);
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
        this.wasMouseDown = this.mouseDown;
        if (this.nextScroll != -1) {
            this.scroll = this.nextScroll;
            this.nextScroll = -1;
        }
    }
    
    private List<QuickplayOption> getGames() {
        List<QuickplayOption> result;
        if (this.inAllGames) {
            if (this.currentGame != null) {
                result = this.currentGame.getModeOptions();
            }
            else {
                result = this.mod.getGameOptions();
            }
            result.add(0, new BackOption());
        }
        else if (this.query.isEmpty()) {
            result = this.mod.getRecentlyPlayed();
            result.add(new AllGamesOption());
        }
        else {
            result = this.mod.getGames().stream().flatMap(entry -> entry.getModes().stream()).filter(mode -> EnumChatFormatting.getTextWithoutFormattingCodes(mode.getText().toLowerCase(Locale.ROOT)).contains(this.query.toLowerCase(Locale.ROOT))).sorted((o1, o2) -> Integer.compare(EnumChatFormatting.getTextWithoutFormattingCodes(o1.getText().toLowerCase()).startsWith(this.query.toLowerCase()) ? 0 : 1, EnumChatFormatting.getTextWithoutFormattingCodes(o2.getText().toLowerCase()).startsWith(this.query.toLowerCase()) ? 0 : 1)).collect((Collector<? super Object, ?, List<QuickplayOption>>)Collectors.toList());
        }
        return result;
    }
    
    private void clampIndex() {
        this.selectedIndex = MathHelper.clamp_int(this.selectedIndex, 0, this.getGames().size() - 1);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (Keyboard.isKeyDown(29) && keyCode == 30) {
            this.query = "";
        }
        if (typedChar > '\u001f' && typedChar != '\ufffd') {
            this.query = String.valueOf(this.query) + typedChar;
            this.inAllGames = false;
        }
        else if (typedChar == '\b' && !this.query.isEmpty()) {
            if (GuiScreen.isCtrlKeyDown()) {
                this.query = "";
            }
            else {
                this.query = this.query.substring(0, this.query.length() - 1);
            }
        }
        if (keyCode == 208) {
            ++this.selectedIndex;
            this.scroll += 20;
            this.clampIndex();
        }
        else if (keyCode == 200) {
            --this.selectedIndex;
            this.scroll -= 20;
            this.clampIndex();
        }
        else {
            if (keyCode != 28) {
                if (keyCode != 205) {
                    if (keyCode == 203) {
                        this.back();
                    }
                    return;
                }
            }
            try {
                this.getGames().get(this.selectedIndex).onClick(this, this.mod);
            }
            catch (IndexOutOfBoundsException ex) {}
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            this.mouseDown = true;
            final int n = 0;
            this.lastMouseY = n;
            this.lastMouseX = n;
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.mouseDown = false;
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            if (dWheel > 0) {
                dWheel = -1;
            }
            else if (dWheel < 0) {
                dWheel = 1;
            }
            this.scroll += 20 * dWheel;
            final int n = 0;
            this.lastMouseY = n;
            this.lastMouseX = n;
        }
    }
    
    public void openAllGames() {
        this.recentGamesScroll = this.scroll;
        this.inAllGames = true;
        this.currentGame = null;
        this.scroll = 0;
        this.selectedIndex = 1;
    }
    
    public void back() {
        if (this.currentGame != null) {
            this.selectedIndex = this.mod.getGames().indexOf(this.currentGame) + 1;
            this.currentGame = null;
            this.nextScroll = this.allGamesScroll;
        }
        else {
            if (!this.inAllGames) {
                this.mc.displayGuiScreen(null);
                return;
            }
            this.selectedIndex = this.mod.getRecentlyPlayed().size();
            this.inAllGames = false;
            this.nextScroll = this.recentGamesScroll;
        }
    }
    
    public void selectGame(final QuickplayGame game) {
        this.allGamesScroll = this.scroll;
        this.selectedIndex = 1;
        this.scroll = 0;
        this.currentGame = game;
    }
}

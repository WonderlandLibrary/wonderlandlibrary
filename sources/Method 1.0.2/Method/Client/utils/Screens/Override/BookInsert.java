package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookInsert extends Screen {
    @Subscribe
    public void GuiScreenEventInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiScreenBook) {
            event.getButtonList().add(new GuiButton(3334, event.getGui().width / 2 + 80, 50, 50, 20, "Fillmax"));
            event.getButtonList().add(new GuiButton(3335, event.getGui().width / 2 + 80, 70, 50, 20, "AutoSign"));
            event.getButtonList().add(new GuiButton(3336, event.getGui().width / 2 + 80, 90, 50, 20, "Copy"));
            event.getButtonList().add(new GuiButton(3337, event.getGui().width / 2 + 80, 110, 50, 20, "Fillbook"));
            event.getButtonList().add(new GuiButton(3338, event.getGui().width / 2 + 80, 130, 50, 20, "Clear"));
            event.getButtonList().add(new GuiButton(3339, event.getGui().width / 2 + 80, 150, 50, 20, "Author"));

            for (int i = 0; i < 22; i++) {
                event.getButtonList().add(new GuiButton(i + 11, event.getGui().width / 2 + 130 + ((i % 5) * 15), 105 - (i / 5) * 15, 15, 15, ColorfromInt(i) + "&A"));
            }
        }
    }


    @Subscribe
    public void GuiScreenEventPost(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiScreenBook) {
            GuiScreenBook Gui = (GuiScreenBook) event.getGui();
            switch (event.getButton().id) {
                case 3334:
                    RandomFill(Gui, Gui.currPage);
                    break;
                case 3335:
                    Gui.bookGettingSigned = true;
                    Gui.bookTitle = mc.player.getName();
                    try {
                        Gui.sendBookToServer(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mc.displayGuiScreen(null);
                    break;
                case 3336:
                    String s = pageGetCurrent(Gui);
                    NewPage(Gui);
                    pageSetCurrent(Gui, s);
                    break;
                case 3337:
                    for (int G = 0; G < Gui.bookTotalPages; G++) {
                        NewPage(Gui);
                    }
                    for (int G = 0; G < Gui.bookTotalPages; G++) {
                        RandomFill(Gui, G);
                    }
                    break;
                case 3338:
                    pageSetCurrent(Gui, "");
                    break;
                case 3339:
                    mc.displayGuiScreen(null);
                    mc.displayGuiScreen(new Authorinsert());
                    break;
                case 27:
                    checkandinstert(Gui, event.getButton().id, TextFormatting.OBFUSCATED);
                    break;
                case 28:
                    checkandinstert(Gui, event.getButton().id, TextFormatting.BOLD);
                    break;
                case 29:
                    checkandinstert(Gui, event.getButton().id, TextFormatting.STRIKETHROUGH);
                    break;
                case 30:
                    checkandinstert(Gui, event.getButton().id, TextFormatting.UNDERLINE);
                    break;
                case 31:
                    checkandinstert(Gui, event.getButton().id, TextFormatting.ITALIC);
                    break;
                case 32:
                    checkandinstert(Gui, event.getButton().id, TextFormatting.RESET);
                    break;
                default:
                    if (event.getButton().id < 27 && event.getButton().id > 10) {
                        String getCurrent = pageGetCurrent(Gui);
                        String s1 = getCurrent + TextFormatting.fromColorIndex(event.getButton().id - 11);
                        int wordWrappedHeight = Gui.fontRenderer.getWordWrappedHeight(s1 + "" + TextFormatting.BLACK + "_", 118);
                        if (wordWrappedHeight <= 128 && s1.length() < 256) {
                            pageSetCurrent(Gui, s1);
                        }
                    }
            }
        }
    }

    private void RandomFill(GuiScreenBook gui, int loc) {
        new Thread(() -> {
            String collect = randomget().substring(0, 254);
            gui.bookPages.set(loc, new NBTTagString(collect));
            gui.bookIsModified = true;
        }).start();
    }

    public String randomget() {
        final IntStream gen = new Random().ints(0x80, 0x10ffff - 0x800).map(i -> i < 0xd800 ? i : i + 0x800);
        return gen.limit(4 * 384).mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
    }

    private void NewPage(GuiScreenBook gui) {
        if (gui.bookIsUnsigned) {
            if (gui.bookPages != null && gui.bookPages.tagCount() < 50) {
                gui.bookPages.appendTag(new NBTTagString(""));
                ++gui.bookTotalPages;
                gui.bookIsModified = true;
            }
            if (gui.currPage < gui.bookTotalPages - 1) {
                ++gui.currPage;
            }
        }
    }

    void checkandinstert(GuiScreenBook gui, int id, TextFormatting d) {
        String s = pageGetCurrent(gui);
        String s1 = s + d;
        int i = gui.fontRenderer.getWordWrappedHeight(s1 + "" + TextFormatting.BLACK + "_", 118);
        if (i <= 128 && s1.length() < 256) {
            pageSetCurrent(gui, s1);
        }
    }

    public String pageGetCurrent(GuiScreenBook gui) {
        return gui.bookPages != null && gui.currPage >= 0 && gui.currPage < gui.bookPages.tagCount() ? gui.bookPages.getStringTagAt(gui.currPage) : "";
    }

    private void pageSetCurrent(GuiScreenBook gui, String s) {
        if (gui.bookPages != null && gui.currPage >= 0 && gui.currPage < gui.bookPages.tagCount()) {
            gui.bookPages.set(gui.currPage, new NBTTagString(s));
            gui.bookIsModified = true;
        }
    }

}

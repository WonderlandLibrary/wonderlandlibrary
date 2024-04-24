
package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Method.Client.Main.setmgr;

public class NbtView extends Module {

    /////////////////////
    /////////////////////

    public NbtView() {
        super("Nbt View", Keyboard.KEY_NONE, Category.MISC, "Show Nbt Data,Press alt to copy");
    }

    private static int line_scrolled = 0, time = 0;
    static Setting maxLinesShown;
    static Setting ticksBeforeScroll;
    static Setting showDelimiters;
    static Setting showSeparator;
    static Setting compress;

    private boolean readytocopy = false;

    private static final String FORMAT = TextFormatting.ITALIC.toString() + TextFormatting.DARK_GRAY;

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void setup() {
        setmgr.add(maxLinesShown = new Setting("maxLinesShown", this, 10, 1, 100, true));
        setmgr.add(ticksBeforeScroll = new Setting("ticksBeforeScroll", this, 20, 1, 400, true));
        setmgr.add(showDelimiters = new Setting("showDelimiters", this, true));
        setmgr.add(showSeparator = new Setting("showSeparator", this, true));
        setmgr.add(compress = new Setting("compress", this, true));
    }

    @Subscribe
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if ((event.getWorld().isRemote || !event.getWorld().isRemote)) {
            ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
            if (event.getWorld().getTileEntity(event.getPos()) != null && stack.getItem() == Items.ARROW) {
                ArrayList<String> tag = new ArrayList<>();
                unwrapTag(tag, Objects.requireNonNull(event.getWorld().getTileEntity(event.getPos())).writeToNBT(new NBTTagCompound()), "", "", "\t");
                final StringBuilder sb = new StringBuilder();
                tag.forEach(s -> {
                    sb.append(s);
                    sb.append('\n');
                });
                new InfoWindow(sb.toString(), event.getWorld().isRemote);
            }
        }

    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (GuiScreen.isAltKeyDown()) {
            readytocopy = true;
        }
        if (!GuiScreen.isShiftKeyDown()) {
            time++;
            if (time >= ticksBeforeScroll.getValDouble() / (GuiScreen.isAltKeyDown() ? 4 : 1)) {
                time = 0;
                line_scrolled++;
            }
        }
    }

    @Subscribe
    public void ItemTooltipEvent(ItemTooltipEvent event) {
        NBTTagCompound tag = event.getItemStack().getTagCompound();
        ArrayList<String> ttip = new ArrayList<>((int) maxLinesShown.getValDouble());
        if (tag != null) {
            if (readytocopy) {
                StringSelection selection = new StringSelection(tag.toString());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                readytocopy = false;
            }

            event.getToolTip().add("");

            if (showDelimiters.getValBoolean()) {
                ttip.add(TextFormatting.DARK_PURPLE + " - nbt start -");
            }
            if (compress.getValBoolean()) {
                ttip.add(FORMAT + tag.toString());
            } else {
                unwrapTag(ttip, tag, FORMAT, "", compress.getValBoolean() ? "" : "  ");
            }
            if (showDelimiters.getValBoolean()) {
                ttip.add(TextFormatting.DARK_PURPLE + " - nbt end -");
            }
            ttip = transformTtip(ttip);

            event.getToolTip().addAll(ttip);
        } else {
            event.getToolTip().add(FORMAT + "No NBT tag");
        }
    }

    private static ArrayList<String> transformTtip(ArrayList<String> ttip) {
        ArrayList<String> newttip = new ArrayList<>((int) maxLinesShown.getValDouble());
        if (showSeparator.getValBoolean()) {
            newttip.add("- NBTView -");
        }
        if (ttip.size() > maxLinesShown.getValDouble()) {
            if (maxLinesShown.getValDouble() + line_scrolled > ttip.size()) line_scrolled = 0;
            for (int i = 0; i < maxLinesShown.getValDouble(); i++) {
                newttip.add(ttip.get(i + line_scrolled));
            }
        } else {
            line_scrolled = 0;
            newttip.addAll(ttip);
        }
        return newttip;
    }

    static void unwrapTag(List<String> tooltip, NBTBase base, String pad, @Nonnull String tagName, String padIncrement) {
        if (base.getId() == 10) {
            NBTTagCompound tag = (NBTTagCompound) base;
            tag.getKeySet().forEach(s -> {
                boolean nested = tag.getTag(s).getId() == 10 || tag.getTag(s).getId() == 9;
                if (nested) {
                    tooltip.add(pad + s + ": {");
                    unwrapTag(tooltip, tag.getTag(s), pad + padIncrement, s, padIncrement);
                    tooltip.add(pad + "}");
                } else {
                    addValueToTooltip(tooltip, tag.getTag(s), s, pad);
                }
            });
        } else if (base.getId() == 9) {
            NBTTagList tag = (NBTTagList) base;
            int index = 0;
            for (NBTBase nbtnext : tag) {
                if (nbtnext.getId() == 10 || nbtnext.getId() == 9) {
                    tooltip.add(pad + "[" + index + "]: {");
                    unwrapTag(tooltip, nbtnext, pad + padIncrement, "", padIncrement);
                    tooltip.add(pad + "}");
                } else {
                    tooltip.add(pad + "[" + index + "] -> " + nbtnext.toString());
                }
                index++;
            }
        } else {
            addValueToTooltip(tooltip, base, tagName, pad);
        }
    }

    private static void addValueToTooltip(List<String> tooltip, NBTBase nbt, String name, String pad) {
        tooltip.add(pad + name + ": " + nbt.toString());
    }


    public static class InfoWindow extends Frame {

        private static final long serialVersionUID = 8935325049409596603L;


        private static InfoWindow client = null;
        private static InfoWindow server = null;

        protected boolean isRemote;

        public InfoWindow(String tag, boolean isRemote) {
            this.setSize(400, 300);
            this.setTitle(I18n.format("reader.window", I18n.format("reader.side_" + (isRemote ? "client" : "server"))));
            TextArea ta = new TextArea(tag);
            this.add(ta);
            this.isRemote = isRemote;
            this.setAutoRequestFocus(false);
            this.addWindowListener(new WindowListener() {

                @Subscribe
                public void windowOpened(WindowEvent e) {
                }

                @Subscribe
                public void windowIconified(WindowEvent e) {
                }

                @Subscribe
                public void windowDeiconified(WindowEvent e) {
                }

                @Subscribe
                public void windowDeactivated(WindowEvent e) {
                }

                @Subscribe
                public void windowClosing(WindowEvent e) {
                    InfoWindow.this.setVisible(false);
                    InfoWindow.this.dispose();
                    if (InfoWindow.this.isRemote) client = null;
                    else server = null;
                }

                @Subscribe
                public void windowClosed(WindowEvent e) {
                }

                @Subscribe
                public void windowActivated(WindowEvent e) {
                }
            });

            if (isRemote) {
                if (client != null && client.isValid()) {
                    this.setBounds(client.getX(), client.getY(), client.getWidth(), client.getHeight());
                    client.setVisible(false);
                    client.dispose();
                }
                client = this;
            } else {
                if (server != null && server.isValid()) {
                    this.setBounds(server.getX(), server.getY(), server.getWidth(), server.getHeight());
                    server.setVisible(false);
                    server.dispose();
                }
                server = this;
            }

            this.setVisible(true);
        }
    }
}

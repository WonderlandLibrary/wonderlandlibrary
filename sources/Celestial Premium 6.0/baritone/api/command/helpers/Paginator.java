/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.helpers;

import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandInvalidTypeException;
import baritone.api.utils.Helper;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class Paginator<E>
implements Helper {
    public final List<E> entries;
    public int pageSize = 8;
    public int page = 1;

    public Paginator(List<E> entries) {
        this.entries = entries;
    }

    public Paginator(E ... entries) {
        this.entries = Arrays.asList(entries);
    }

    public Paginator<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getMaxPage() {
        return (this.entries.size() - 1) / this.pageSize + 1;
    }

    public boolean validPage(int page) {
        return page > 0 && page <= this.getMaxPage();
    }

    public Paginator<E> skipPages(int pages) {
        this.page += pages;
        return this;
    }

    public void display(Function<E, ITextComponent> transform, String commandPrefix) {
        int offset;
        for (int i = offset = (this.page - 1) * this.pageSize; i < offset + this.pageSize; ++i) {
            if (i < this.entries.size()) {
                this.logDirect(transform.apply(this.entries.get(i)));
                continue;
            }
            this.logDirect("--", TextFormatting.DARK_GRAY);
        }
        boolean hasPrevPage = commandPrefix != null && this.validPage(this.page - 1);
        boolean hasNextPage = commandPrefix != null && this.validPage(this.page + 1);
        TextComponentString prevPageComponent = new TextComponentString("<<");
        if (hasPrevPage) {
            prevPageComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s %d", commandPrefix, this.page - 1))).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to view previous page")));
        } else {
            prevPageComponent.getStyle().setColor(TextFormatting.DARK_GRAY);
        }
        TextComponentString nextPageComponent = new TextComponentString(">>");
        if (hasNextPage) {
            nextPageComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s %d", commandPrefix, this.page + 1))).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to view next page")));
        } else {
            nextPageComponent.getStyle().setColor(TextFormatting.DARK_GRAY);
        }
        TextComponentString pagerComponent = new TextComponentString("");
        pagerComponent.getStyle().setColor(TextFormatting.GRAY);
        pagerComponent.appendSibling(prevPageComponent);
        pagerComponent.appendText(" | ");
        pagerComponent.appendSibling(nextPageComponent);
        pagerComponent.appendText(String.format(" %d/%d", this.page, this.getMaxPage()));
        this.logDirect(pagerComponent);
    }

    public void display(Function<E, ITextComponent> transform) {
        this.display(transform, null);
    }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Runnable pre, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        int page = 1;
        consumer.requireMax(1);
        if (consumer.hasAny() && !pagi.validPage(page = consumer.getAs(Integer.class).intValue())) {
            throw new CommandInvalidTypeException(consumer.consumed(), String.format("a valid page (1-%d)", pagi.getMaxPage()), consumer.consumed().getValue());
        }
        pagi.skipPages(page - pagi.page);
        if (pre != null) {
            pre.run();
        }
        pagi.display(transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Runnable pre, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        Paginator.paginate(consumer, new Paginator<T>(elems), pre, transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Runnable pre, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        Paginator.paginate(consumer, Arrays.asList(elems), pre, transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        Paginator.paginate(consumer, pagi, null, transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        Paginator.paginate(consumer, new Paginator<T>(elems), null, transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Function<T, ITextComponent> transform, String commandPrefix) throws CommandException {
        Paginator.paginate(consumer, Arrays.asList(elems), null, transform, commandPrefix);
    }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Runnable pre, Function<T, ITextComponent> transform) throws CommandException {
        Paginator.paginate(consumer, pagi, pre, transform, null);
    }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Runnable pre, Function<T, ITextComponent> transform) throws CommandException {
        Paginator.paginate(consumer, new Paginator<T>(elems), pre, transform, null);
    }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Runnable pre, Function<T, ITextComponent> transform) throws CommandException {
        Paginator.paginate(consumer, Arrays.asList(elems), pre, transform, null);
    }

    public static <T> void paginate(IArgConsumer consumer, Paginator<T> pagi, Function<T, ITextComponent> transform) throws CommandException {
        Paginator.paginate(consumer, pagi, null, transform, null);
    }

    public static <T> void paginate(IArgConsumer consumer, List<T> elems, Function<T, ITextComponent> transform) throws CommandException {
        Paginator.paginate(consumer, new Paginator<T>(elems), null, transform, null);
    }

    public static <T> void paginate(IArgConsumer consumer, T[] elems, Function<T, ITextComponent> transform) throws CommandException {
        Paginator.paginate(consumer, Arrays.asList(elems), null, transform, null);
    }
}


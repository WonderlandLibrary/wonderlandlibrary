// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.function.BiFunction;
import java.util.regex.Pattern;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import java.util.regex.Matcher;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.regex.MatchResult;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.renderer.ComponentRenderer;

final class TextReplacementRenderer implements ComponentRenderer<State>
{
    static final TextReplacementRenderer INSTANCE;
    
    private TextReplacementRenderer() {
    }
    
    @NotNull
    @Override
    public Component render(@NotNull final Component component, @NotNull final State state) {
        if (!state.running) {
            return component;
        }
        final boolean prevFirstMatch = state.firstMatch;
        state.firstMatch = true;
        final List<Component> oldChildren = component.children();
        final int oldChildrenSize = oldChildren.size();
        List<Component> children = null;
        Component modified = component;
        if (component instanceof TextComponent) {
            final String content = ((TextComponent)component).content();
            final Matcher matcher = state.pattern.matcher(content);
            int replacedUntil = 0;
            while (matcher.find()) {
                final PatternReplacementResult result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount);
                if (result == PatternReplacementResult.CONTINUE) {
                    continue;
                }
                if (result == PatternReplacementResult.STOP) {
                    state.running = false;
                    break;
                }
                if (matcher.start() == 0) {
                    if (matcher.end() == content.length()) {
                        final ComponentLike replacement = state.replacement.apply(matcher, ((ComponentBuilder<C, TextComponent.Builder>)Component.text().content(matcher.group())).style(component.style()));
                        modified = ((replacement == null) ? Component.empty() : replacement.asComponent());
                        modified = modified.style(modified.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
                        if (children == null) {
                            children = new ArrayList<Component>(oldChildrenSize + modified.children().size());
                            children.addAll(modified.children());
                        }
                    }
                    else {
                        modified = Component.text("", component.style());
                        final ComponentLike child = state.replacement.apply(matcher, Component.text().content(matcher.group()));
                        if (child != null) {
                            if (children == null) {
                                children = new ArrayList<Component>(oldChildrenSize + 1);
                            }
                            children.add(child.asComponent());
                        }
                    }
                }
                else {
                    if (children == null) {
                        children = new ArrayList<Component>(oldChildrenSize + 2);
                    }
                    if (state.firstMatch) {
                        modified = ((TextComponent)component).content(content.substring(0, matcher.start()));
                    }
                    else if (replacedUntil < matcher.start()) {
                        children.add(Component.text(content.substring(replacedUntil, matcher.start())));
                    }
                    final ComponentLike builder = state.replacement.apply(matcher, Component.text().content(matcher.group()));
                    if (builder != null) {
                        children.add(builder.asComponent());
                    }
                }
                ++state.replaceCount;
                state.firstMatch = false;
                replacedUntil = matcher.end();
            }
            if (replacedUntil < content.length() && replacedUntil > 0) {
                if (children == null) {
                    children = new ArrayList<Component>(oldChildrenSize);
                }
                children.add(Component.text(content.substring(replacedUntil)));
            }
        }
        else if (modified instanceof TranslatableComponent) {
            final List<Component> args = ((TranslatableComponent)modified).args();
            List<Component> newArgs = null;
            for (int i = 0, size = args.size(); i < size; ++i) {
                final Component original = args.get(i);
                final Component replaced = this.render(original, state);
                if (replaced != component && newArgs == null) {
                    newArgs = new ArrayList<Component>(size);
                    if (i > 0) {
                        newArgs.addAll(args.subList(0, i));
                    }
                }
                if (newArgs != null) {
                    newArgs.add(replaced);
                }
            }
            if (newArgs != null) {
                modified = ((TranslatableComponent)modified).args(newArgs);
            }
        }
        if (state.running) {
            final HoverEvent<?> event = modified.style().hoverEvent();
            if (event != null) {
                final HoverEvent<?> rendered = event.withRenderedValue(this, state);
                if (event != rendered) {
                    modified = modified.style(s -> s.hoverEvent(rendered));
                }
            }
            boolean first = true;
            for (int i = 0; i < oldChildrenSize; ++i) {
                final Component child2 = oldChildren.get(i);
                final Component replaced2 = this.render(child2, state);
                if (replaced2 != child2) {
                    if (children == null) {
                        children = new ArrayList<Component>(oldChildrenSize);
                    }
                    if (first) {
                        children.addAll(oldChildren.subList(0, i));
                    }
                    first = false;
                }
                if (children != null) {
                    children.add(replaced2);
                }
            }
        }
        else if (children != null) {
            children.addAll(oldChildren);
        }
        state.firstMatch = prevFirstMatch;
        if (children != null) {
            return modified.children(children);
        }
        return modified;
    }
    
    static {
        INSTANCE = new TextReplacementRenderer();
    }
    
    static final class State
    {
        final Pattern pattern;
        final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;
        final TextReplacementConfig.Condition continuer;
        boolean running;
        int matchCount;
        int replaceCount;
        boolean firstMatch;
        
        State(@NotNull final Pattern pattern, @NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement, final TextReplacementConfig.Condition continuer) {
            this.running = true;
            this.matchCount = 0;
            this.replaceCount = 0;
            this.firstMatch = true;
            this.pattern = pattern;
            this.replacement = replacement;
            this.continuer = continuer;
        }
    }
}

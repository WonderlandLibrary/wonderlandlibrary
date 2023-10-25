/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerUnlockRecipesPacket
extends MinecraftPacket {
    private UnlockRecipesAction action;
    private List<Integer> recipes;
    private List<Integer> alreadyKnownRecipes;
    private boolean openCraftingBook;
    private boolean activateFiltering;

    private ServerUnlockRecipesPacket() {
    }

    private ServerUnlockRecipesPacket(boolean openCraftingBook, boolean activateFiltering, List<Integer> recipes) {
        this.openCraftingBook = openCraftingBook;
        this.activateFiltering = activateFiltering;
        this.recipes = recipes;
    }

    public ServerUnlockRecipesPacket(boolean openCraftingBook, boolean activateFiltering, List<Integer> recipes, List<Integer> alreadyKnownRecipes) {
        this(openCraftingBook, activateFiltering, recipes);
        this.action = UnlockRecipesAction.INIT;
        this.alreadyKnownRecipes = alreadyKnownRecipes;
    }

    public ServerUnlockRecipesPacket(boolean openCraftingBook, boolean activateFiltering, List<Integer> recipes, UnlockRecipesAction action) {
        this(openCraftingBook, activateFiltering, recipes);
        if (action != UnlockRecipesAction.ADD && action != UnlockRecipesAction.REMOVE) {
            throw new IllegalArgumentException("action must be ADD or REMOVE");
        }
        this.action = action;
    }

    public UnlockRecipesAction getAction() {
        return this.action;
    }

    public List<Integer> getRecipes() {
        return this.recipes;
    }

    public List<Integer> getAlreadyKnownRecipes() {
        if (this.action != UnlockRecipesAction.INIT) {
            throw new IllegalStateException("alreadyKnownRecipes is only set if action is " + (Object)((Object)UnlockRecipesAction.INIT) + " but it was " + (Object)((Object)this.action));
        }
        return this.alreadyKnownRecipes;
    }

    public boolean getOpenCraftingBook() {
        return this.openCraftingBook;
    }

    public boolean getActivateFiltering() {
        return this.activateFiltering;
    }

    @Override
    public void read(NetInput in) throws IOException {
        int i;
        int size;
        this.action = MagicValues.key(UnlockRecipesAction.class, in.readVarInt());
        this.openCraftingBook = in.readBoolean();
        this.activateFiltering = in.readBoolean();
        if (this.action == UnlockRecipesAction.INIT) {
            size = in.readVarInt();
            this.alreadyKnownRecipes = new ArrayList<Integer>(size);
            i = 0;
            while (i < size) {
                this.alreadyKnownRecipes.add(in.readVarInt());
                ++i;
            }
        }
        size = in.readVarInt();
        this.recipes = new ArrayList<Integer>(size);
        i = 0;
        while (i < size) {
            this.recipes.add(in.readVarInt());
            ++i;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, (Object)this.action));
        out.writeBoolean(this.openCraftingBook);
        out.writeBoolean(this.activateFiltering);
        if (this.action == UnlockRecipesAction.INIT) {
            out.writeVarInt(this.alreadyKnownRecipes.size());
            for (Integer recipeId : this.alreadyKnownRecipes) {
                out.writeVarInt(recipeId);
            }
        }
        out.writeVarInt(this.recipes.size());
        for (Integer recipeId : this.recipes) {
            out.writeVarInt(recipeId);
        }
    }
}


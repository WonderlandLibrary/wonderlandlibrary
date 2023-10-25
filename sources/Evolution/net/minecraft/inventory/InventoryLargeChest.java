package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest implements ILockableContainer
{
    /** Name of the chest. */
    private final String name;

    /** Inventory object corresponding to double chest upper part */
    private final ILockableContainer upperChest;

    /** Inventory object corresponding to double chest lower part */
    private final ILockableContainer lowerChest;

    public InventoryLargeChest(String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn)
    {
        this.name = nameIn;

        if (upperChestIn == null)
        {
            upperChestIn = lowerChestIn;
        }

        if (lowerChestIn == null)
        {
            lowerChestIn = upperChestIn;
        }

        this.upperChest = upperChestIn;
        this.lowerChest = lowerChestIn;

        if (upperChestIn.isLocked())
        {
            lowerChestIn.setLockCode(upperChestIn.getLockCode());
        }
        else if (lowerChestIn.isLocked())
        {
            upperChestIn.setLockCode(lowerChestIn.getLockCode());
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    public boolean isEmpty()
    {
        return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
    }

    /**
     * Return whether the given inventory is part of this large chest.
     */
    public boolean isPartOfLargeChest(IInventory inventoryIn)
    {
        return this.upperChest == inventoryIn || this.lowerChest == inventoryIn;
    }

    /**
     * Gets the name of this thing. This method has slightly different behavior depending on the interface (for <a
     * href="https://github.com/ModCoderPack/MCPBot-Issues/issues/14">technical reasons</a> the same method is used for
     * both IWorldNameable and ICommandSender):
     *  
     * <dl>
     * <dt>{@link net.minecraft.util.INameable#getName() INameable.getName()}</dt>
     * <dd>Returns the name of this inventory. If this {@linkplain net.minecraft.inventory#hasCustomName() has a custom
     * name} then this <em>should</em> be a direct string; otherwise it <em>should</em> be a valid translation
     * string.</dd>
     * <dd>However, note that <strong>the translation string may be invalid</strong>, as is the case for {@link
     * net.minecraft.tileentity.TileEntityBanner TileEntityBanner} (always returns nonexistent translation code
     * <code>banner</code> without a custom name), {@link net.minecraft.block.BlockAnvil.Anvil BlockAnvil$Anvil} (always
     * returns <code>anvil</code>), {@link net.minecraft.block.BlockWorkbench.InterfaceCraftingTable
     * BlockWorkbench$InterfaceCraftingTable} (always returns <code>crafting_table</code>), {@link
     * InventoryCraftResult InventoryCraftResult} (always returns <code>Result</code>) and the
     * {@link net.minecraft.entity.item.EntityMinecart EntityMinecart} family (uses the entity definition). This is not
     * an exaustive list.</dd>
     * <dd>In general, this method should be safe to use on tile entities that implement IInventory.</dd>
     * <dt>{@link net.minecraft.command.ICommandSender#getName() ICommandSender.getName()} and {@link
     * net.minecraft.entity.Entity#getName() Entity.getName()}</dt>
     * <dd>Returns a valid, displayable name (which may be localized). For most entities, this is the translated version
     * of its translation string (obtained via {@link net.minecraft.entity.EntityList#getEntityString
     * EntityList.getEntityString}).</dd>
     * <dd>If this entity has a custom name set, this will return that name.</dd>
     * <dd>For some entities, this will attempt to translate a nonexistent translation string; see <a
     * href="https://bugs.mojang.com/browse/MC-68446">MC-68446</a>. For {@linkplain
     * EntityPlayer#getName() players} this returns the player's name. For {@linkplain
     * net.minecraft.entity.passive.EntityOcelot ocelots} this may return the translation of
     * <code>entity.Cat.name</code> if it is tamed. For {@linkplain net.minecraft.entity.item.EntityItem#getName() item
     * entities}, this will attempt to return the name of the item in that item entity. In all cases other than players,
     * the custom name will overrule this.</dd>
     * <dd>For non-entity command senders, this will return some arbitrary name, such as "Rcon" or "Server".</dd>
     * </dl>
     */
    public String getName()
    {
        if (this.upperChest.hasCustomName())
        {
            return this.upperChest.getName();
        }
        else
        {
            return this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name;
        }
    }

    /**
     * Checks if this thing has a custom name. This method has slightly different behavior depending on the interface
     * (for <a href="https://github.com/ModCoderPack/MCPBot-Issues/issues/14">technical reasons</a> the same method is
     * used for both IWorldNameable and Entity):
     *  
     * <dl>
     * <dt>{@link net.minecraft.util.INameable#hasCustomName() INameable.hasCustomName()}</dt>
     * <dd>If true, then {@link #getName()} probably returns a preformatted name; otherwise, it probably returns a
     * translation string. However, exact behavior varies.</dd>
     * <dt>{@link net.minecraft.entity.Entity#hasCustomName() Entity.hasCustomName()}</dt>
     * <dd>If true, then {@link net.minecraft.entity.Entity#getCustomNameTag() Entity.getCustomNameTag()} will return a
     * non-empty string, which will be used by {@link #getName()}.</dd>
     * </dl>
     */
    public boolean hasCustomName()
    {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }

    /**
     * Returns a displayable component representing this thing's name. This method should be implemented slightly
     * differently depending on the interface (for <a href="https://github.com/ModCoderPack/MCPBot-
     * Issues/issues/14">technical reasons</a> the same method is used for both IWorldNameable and ICommandSender), but
     * unlike {@link #getName()} this method will generally behave sanely.
     *  
     * <dl>
     * <dt>{@link net.minecraft.util.INameable#getDisplayName() INameable.getDisplayName()}</dt>
     * <dd>A normal component. Might be a translation component or a text component depending on the context. Usually
     * implemented as:</dd>
     * <dd><pre><code>return this.{@link net.minecraft.util.INameable#hasCustomName() hasCustomName()} ? new
     * TextComponentString(this.{@link #getName()}) : new TextComponentTranslation(this.{@link
     * #getName()});</code></pre></dd>
     * <dt>{@link net.minecraft.command.ICommandSender#getDisplayName() ICommandSender.getDisplayName()} and {@link
     * net.minecraft.entity.Entity#getDisplayName() Entity.getDisplayName()}</dt>
     * <dd>For most entities, this returns the result of {@link #getName()}, with {@linkplain
     * net.minecraft.scoreboard.ScorePlayerTeam#formatPlayerName scoreboard formatting} and a {@linkplain
     * net.minecraft.entity.Entity#getHoverEvent special hover event}.</dd>
     * <dd>For non-entity command senders, this will return the result of {@link #getName()} in a text component.</dd>
     * </dl>
     */
    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= this.upperChest.getSizeInventory())
        {
            this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
        }
        else
        {
            this.upperChest.setInventorySlotContents(index, stack);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return this.upperChest.getInventoryStackLimit();
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.upperChest.isUsableByPlayer(player) && this.lowerChest.isUsableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)
    {
        this.upperChest.openInventory(player);
        this.lowerChest.openInventory(player);
    }

    public void closeInventory(EntityPlayer player)
    {
        this.upperChest.closeInventory(player);
        this.lowerChest.closeInventory(player);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public boolean isLocked()
    {
        return this.upperChest.isLocked() || this.lowerChest.isLocked();
    }

    public void setLockCode(LockCode code)
    {
        this.upperChest.setLockCode(code);
        this.lowerChest.setLockCode(code);
    }

    public LockCode getLockCode()
    {
        return this.upperChest.getLockCode();
    }

    public String getGuiID()
    {
        return this.upperChest.getGuiID();
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    public void clear()
    {
        this.upperChest.clear();
        this.lowerChest.clear();
    }
}

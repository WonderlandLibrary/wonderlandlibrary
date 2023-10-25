package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender
{
    private final ICommandSender delegate;
    @Nullable
    private final Vec3d positionVector;
    @Nullable
    private final BlockPos position;
    @Nullable
    private final Integer permissionLevel;
    @Nullable
    private final Entity entity;
    @Nullable
    private final Boolean sendCommandFeedback;

    public CommandSenderWrapper(ICommandSender delegateIn, @Nullable Vec3d positionVectorIn, @Nullable BlockPos positionIn, @Nullable Integer permissionLevelIn, @Nullable Entity entityIn, @Nullable Boolean sendCommandFeedbackIn)
    {
        this.delegate = delegateIn;
        this.positionVector = positionVectorIn;
        this.position = positionIn;
        this.permissionLevel = permissionLevelIn;
        this.entity = entityIn;
        this.sendCommandFeedback = sendCommandFeedbackIn;
    }

    public static CommandSenderWrapper create(ICommandSender sender)
    {
        return sender instanceof CommandSenderWrapper ? (CommandSenderWrapper)sender : new CommandSenderWrapper(sender, (Vec3d)null, (BlockPos)null, (Integer)null, (Entity)null, (Boolean)null);
    }

    public CommandSenderWrapper withEntity(Entity entityIn, Vec3d p_193997_2_)
    {
        return this.entity == entityIn && Objects.equals(this.positionVector, p_193997_2_) ? this : new CommandSenderWrapper(this.delegate, p_193997_2_, new BlockPos(p_193997_2_), this.permissionLevel, entityIn, this.sendCommandFeedback);
    }

    public CommandSenderWrapper withPermissionLevel(int level)
    {
        return this.permissionLevel != null && this.permissionLevel.intValue() <= level ? this : new CommandSenderWrapper(this.delegate, this.positionVector, this.position, level, this.entity, this.sendCommandFeedback);
    }

    public CommandSenderWrapper withSendCommandFeedback(boolean sendCommandFeedbackIn)
    {
        return this.sendCommandFeedback == null || this.sendCommandFeedback.booleanValue() && !sendCommandFeedbackIn ? new CommandSenderWrapper(this.delegate, this.positionVector, this.position, this.permissionLevel, this.entity, sendCommandFeedbackIn) : this;
    }

    public CommandSenderWrapper computePositionVector()
    {
        return this.positionVector != null ? this : new CommandSenderWrapper(this.delegate, this.getPositionVector(), this.getPosition(), this.permissionLevel, this.entity, this.sendCommandFeedback);
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
     * net.minecraft.inventory.InventoryCraftResult InventoryCraftResult} (always returns <code>Result</code>) and the
     * {@link net.minecraft.entity.item.EntityMinecart EntityMinecart} family (uses the entity definition). This is not
     * an exaustive list.</dd>
     * <dd>In general, this method should be safe to use on tile entities that implement IInventory.</dd>
     * <dt>{@link ICommandSender#getName() ICommandSender.getName()} and {@link
     * Entity#getName() Entity.getName()}</dt>
     * <dd>Returns a valid, displayable name (which may be localized). For most entities, this is the translated version
     * of its translation string (obtained via {@link net.minecraft.entity.EntityList#getEntityString
     * EntityList.getEntityString}).</dd>
     * <dd>If this entity has a custom name set, this will return that name.</dd>
     * <dd>For some entities, this will attempt to translate a nonexistent translation string; see <a
     * href="https://bugs.mojang.com/browse/MC-68446">MC-68446</a>. For {@linkplain
     * net.minecraft.entity.player.EntityPlayer#getName() players} this returns the player's name. For {@linkplain
     * net.minecraft.entity.passive.EntityOcelot ocelots} this may return the translation of
     * <code>entity.Cat.name</code> if it is tamed. For {@linkplain net.minecraft.entity.item.EntityItem#getName() item
     * entities}, this will attempt to return the name of the item in that item entity. In all cases other than players,
     * the custom name will overrule this.</dd>
     * <dd>For non-entity command senders, this will return some arbitrary name, such as "Rcon" or "Server".</dd>
     * </dl>
     */
    public String getName()
    {
        return this.entity != null ? this.entity.getName() : this.delegate.getName();
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
     * <dt>{@link ICommandSender#getDisplayName() ICommandSender.getDisplayName()} and {@link
     * Entity#getDisplayName() Entity.getDisplayName()}</dt>
     * <dd>For most entities, this returns the result of {@link #getName()}, with {@linkplain
     * net.minecraft.scoreboard.ScorePlayerTeam#formatPlayerName scoreboard formatting} and a {@linkplain
     * Entity#getHoverEvent special hover event}.</dd>
     * <dd>For non-entity command senders, this will return the result of {@link #getName()} in a text component.</dd>
     * </dl>
     */
    public ITextComponent getDisplayName()
    {
        return this.entity != null ? this.entity.getDisplayName() : this.delegate.getDisplayName();
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void sendMessage(ITextComponent component)
    {
        if (this.sendCommandFeedback == null || this.sendCommandFeedback.booleanValue())
        {
            this.delegate.sendMessage(component);
        }
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canUseCommand(int permLevel, String commandName)
    {
        return this.permissionLevel != null && this.permissionLevel.intValue() < permLevel ? false : this.delegate.canUseCommand(permLevel, commandName);
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition()
    {
        if (this.position != null)
        {
            return this.position;
        }
        else
        {
            return this.entity != null ? this.entity.getPosition() : this.delegate.getPosition();
        }
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    public Vec3d getPositionVector()
    {
        if (this.positionVector != null)
        {
            return this.positionVector;
        }
        else
        {
            return this.entity != null ? this.entity.getPositionVector() : this.delegate.getPositionVector();
        }
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return this.entity != null ? this.entity.getEntityWorld() : this.delegate.getEntityWorld();
    }

    @Nullable

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    public Entity getCommandSenderEntity()
    {
        return this.entity != null ? this.entity.getCommandSenderEntity() : this.delegate.getCommandSenderEntity();
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback()
    {
        return this.sendCommandFeedback != null ? this.sendCommandFeedback.booleanValue() : this.delegate.sendCommandFeedback();
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        if (this.entity != null)
        {
            this.entity.setCommandStat(type, amount);
        }
        else
        {
            this.delegate.setCommandStat(type, amount);
        }
    }

    @Nullable

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return this.delegate.getServer();
    }
}

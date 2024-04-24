package Method.Client.modmaker.Windows.DragableBlockTypes;

import Method.Client.managers.Hole;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.MainMaker;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.io.Serializable;
import java.util.UUID;

public class heldObject extends DragableBlock implements Serializable {
    public MainBlockType blockType;

    public double value;

    public Boolean aBoolean = false;

    public TimerUtils timer;

    public World world;
    public Entity entity;
    public Gui gui;
    public Item item;
    public net.minecraft.block.Block block;
    public EnumFacing facing;
    public Chunk chunk;
    public BlockPos blockPos;
    public String string;
    public EnumHand enumHand;
    public UUID uuid;
    public ClickType clickType;
    public CPacketClientStatus.State clientStatus;
    public CPacketEntityAction.Action EntityAction;
    public CPacketPlayerDigging.Action playerdigging;
    public IBlockState iBlockState;
    public EnumHandSide enumHandSide;
    public Vec3d vec3d;
    public Hole hole;
    public Hole.HoleType holeType;
    public SPacketUpdateScore.Action ScoreAction;
    public EntityPlayer.EnumChatVisibility chatVisibility;
    public EnumConnectionState connectionState;
    public SoundCategory soundCategory;
    public SoundEvent soundEvent;
    public SPacketRecipeBook.State recipeBook;
    public SPacketCombatEvent.Event CombatEvent;
    public ItemStack itemStack;
    public EnumDifficulty enumDifficulty;
    public AxisAlignedBB BoundingBox;
    public EnumParticleTypes ParticleTypes;
    public CPacketResourcePackStatus.Action PackStatus;
    public SPacketTitle.Type TitleType;
    public Potion potion;
    public SPacketPlayerListItem.Action ListItem;
    public GameType gameType;
    public EntityEquipmentSlot EquipmentSlot;
    public CPacketUseEntity.Action useEntity;
    public RenderGameOverlayEvent.ElementType elementType;
    public RayTraceResult.Type rayTrace;
    public TileEntity tileEntity;
    public Utils.PlaceLocation placeLocation;

    public heldObject(Block block, MainBlockType blockType, MainMaker maker) {
        super(block, maker.module);

        if (blockType == MainBlockType.Timer)
            this.timer = new TimerUtils();
        this.blockType = blockType;
        this.x = 9000;
        this.y = 9000;
        this.isDragging = false;
        this.offsetx = 0;
    }

    public void setupTimer() {
        this.timer = new TimerUtils();
    }

    // Dont draw anything
    @Override
    public void drawScreen(int mouseX, int mouseY, MainMaker maker, boolean cancelOverride) {
    }

    @Override
    public void Blockdraw(int mouseX, int mouseY, MainMaker maker) {
        //this.dragableinput(thisblock.getBlockItems());
    }


}

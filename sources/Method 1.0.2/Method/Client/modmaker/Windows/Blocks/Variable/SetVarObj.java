package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.managers.Hole;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldObject;
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

import java.util.UUID;

public class SetVarObj extends Block {

    public SetVarObj(MainBlockType blockType) {
        super("SetVarObj", MainBlockType.Default, Tabs.Sub, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(blockType));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        MainBlockType blocktype = null;
        for (MainBlockType mainBlockType : this.typesAccepted.get(0)) {
            blocktype = mainBlockType;
        }
        if (blocktype != null)
            switch (blocktype) {
                case Timer:
                    ((heldObject) dragableBlock.blockPointer).timer = (TimerUtils) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Entity:
                    ((heldObject) dragableBlock.blockPointer).entity = (Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Worlds:
                    ((heldObject) dragableBlock.blockPointer).world = (World) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Gui:
                    ((heldObject) dragableBlock.blockPointer).gui = (Gui) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Items:
                    ((heldObject) dragableBlock.blockPointer).item = (Item) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Blocks:
                    ((heldObject) dragableBlock.blockPointer).block = (net.minecraft.block.Block) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Facing:
                    ((heldObject) dragableBlock.blockPointer).facing = (EnumFacing) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Chunk:
                    ((heldObject) dragableBlock.blockPointer).chunk = (Chunk) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case BlockPos:
                    ((heldObject) dragableBlock.blockPointer).blockPos = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case String:
                    ((heldObject) dragableBlock.blockPointer).string = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case EnumHand:
                    ((heldObject) dragableBlock.blockPointer).enumHand = (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case UUID:
                    ((heldObject) dragableBlock.blockPointer).uuid = (UUID) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ClickType:
                    ((heldObject) dragableBlock.blockPointer).clickType = (ClickType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case IBlockState:
                    ((heldObject) dragableBlock.blockPointer).iBlockState = (IBlockState) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ClientStatus:
                    ((heldObject) dragableBlock.blockPointer).clientStatus = (CPacketClientStatus.State) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case EntityAction:
                    ((heldObject) dragableBlock.blockPointer).EntityAction = (CPacketEntityAction.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case PlayerDigging:
                    ((heldObject) dragableBlock.blockPointer).playerdigging = (CPacketPlayerDigging.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case EnumHandSide:
                    ((heldObject) dragableBlock.blockPointer).enumHandSide = (EnumHandSide) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Vec3d:
                    ((heldObject) dragableBlock.blockPointer).vec3d = (Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Hole:
                    ((heldObject) dragableBlock.blockPointer).hole = (Hole) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case HoleType:
                    ((heldObject) dragableBlock.blockPointer).holeType = (Hole.HoleType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ScoreAction:
                    ((heldObject) dragableBlock.blockPointer).ScoreAction = (SPacketUpdateScore.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ChatVisibility:
                    ((heldObject) dragableBlock.blockPointer).chatVisibility = (EntityPlayer.EnumChatVisibility) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ConnectionState:
                    ((heldObject) dragableBlock.blockPointer).connectionState = (EnumConnectionState) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case SoundCategory:
                    ((heldObject) dragableBlock.blockPointer).soundCategory = (SoundCategory) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case SoundEvent:
                    ((heldObject) dragableBlock.blockPointer).soundEvent = (SoundEvent) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case RecipeBook:
                    ((heldObject) dragableBlock.blockPointer).recipeBook = (SPacketRecipeBook.State) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case CombatEvent:
                    ((heldObject) dragableBlock.blockPointer).CombatEvent = (SPacketCombatEvent.Event) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ItemStack:
                    ((heldObject) dragableBlock.blockPointer).itemStack = (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case EnumDifficulty:
                    ((heldObject) dragableBlock.blockPointer).enumDifficulty = (EnumDifficulty) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case BoundingBox:
                    ((heldObject) dragableBlock.blockPointer).BoundingBox = (AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ParticleTypes:
                    ((heldObject) dragableBlock.blockPointer).ParticleTypes = (EnumParticleTypes) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case PackStatus:
                    ((heldObject) dragableBlock.blockPointer).PackStatus = (CPacketResourcePackStatus.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case TitleType:
                    ((heldObject) dragableBlock.blockPointer).TitleType = (SPacketTitle.Type) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case Potion:
                    ((heldObject) dragableBlock.blockPointer).potion = (Potion) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ListItem:
                    ((heldObject) dragableBlock.blockPointer).ListItem = (SPacketPlayerListItem.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case GameType:
                    ((heldObject) dragableBlock.blockPointer).gameType = (GameType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case EquipmentSlot:
                    ((heldObject) dragableBlock.blockPointer).EquipmentSlot = (EntityEquipmentSlot) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case UseEntity:
                    ((heldObject) dragableBlock.blockPointer).useEntity = (CPacketUseEntity.Action) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case ElementType:
                    ((heldObject) dragableBlock.blockPointer).elementType = (RenderGameOverlayEvent.ElementType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case RayTraceResult:
                    ((heldObject) dragableBlock.blockPointer).rayTrace = (RayTraceResult.Type) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case PlaceLocation:
                    ((heldObject) dragableBlock.blockPointer).placeLocation = (Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
                case TileEntity:
                    ((heldObject) dragableBlock.blockPointer).tileEntity = (TileEntity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
                    break;
            }
        super.runCode(dragableBlock, event);
    }

}

package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldObject;

public class GetVarObj extends Block {

    public GetVarObj(MainBlockType blockType) {
        super("GetVarObj", blockType, Tabs.Sub);
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.thisblock.MainBlockTypeGiven) {
            case Timer:
                return ((heldObject) dragableBlock.blockPointer).timer;
            case Entity:
                return ((heldObject) dragableBlock.blockPointer).entity;
            case Worlds:
                return ((heldObject) dragableBlock.blockPointer).world;
            case Gui:
                return ((heldObject) dragableBlock.blockPointer).gui;
            case Items:
                return ((heldObject) dragableBlock.blockPointer).item;
            case Blocks:
                return ((heldObject) dragableBlock.blockPointer).block;
            case Facing:
                return ((heldObject) dragableBlock.blockPointer).facing;
            case Chunk:
                return ((heldObject) dragableBlock.blockPointer).chunk;
            case BlockPos:
                return ((heldObject) dragableBlock.blockPointer).blockPos;
            case String:
                return ((heldObject) dragableBlock.blockPointer).string;
            case EnumHand:
                return ((heldObject) dragableBlock.blockPointer).enumHand;
            case UUID:
                return ((heldObject) dragableBlock.blockPointer).uuid;
            case ClickType:
                return ((heldObject) dragableBlock.blockPointer).clickType;
            case IBlockState:
                return ((heldObject) dragableBlock.blockPointer).iBlockState;
            case ClientStatus:
                return ((heldObject) dragableBlock.blockPointer).clientStatus;
            case EntityAction:
                return ((heldObject) dragableBlock.blockPointer).EntityAction;
            case PlayerDigging:
                return ((heldObject) dragableBlock.blockPointer).playerdigging;
            case EnumHandSide:
                return ((heldObject) dragableBlock.blockPointer).enumHandSide;
            case Vec3d:
                return ((heldObject) dragableBlock.blockPointer).vec3d;
            case Hole:
                return ((heldObject) dragableBlock.blockPointer).hole;
            case HoleType:
                return ((heldObject) dragableBlock.blockPointer).holeType;
            case ScoreAction:
                return ((heldObject) dragableBlock.blockPointer).ScoreAction;
            case ChatVisibility:
                return ((heldObject) dragableBlock.blockPointer).chatVisibility;
            case ConnectionState:
                return ((heldObject) dragableBlock.blockPointer).connectionState;
            case SoundCategory:
                return ((heldObject) dragableBlock.blockPointer).soundCategory;
            case SoundEvent:
                return ((heldObject) dragableBlock.blockPointer).soundEvent;
            case RecipeBook:
                return ((heldObject) dragableBlock.blockPointer).recipeBook;
            case CombatEvent:
                return ((heldObject) dragableBlock.blockPointer).CombatEvent;
            case ItemStack:
                return ((heldObject) dragableBlock.blockPointer).itemStack;
            case EnumDifficulty:
                return ((heldObject) dragableBlock.blockPointer).enumDifficulty;
            case BoundingBox:
                return ((heldObject) dragableBlock.blockPointer).BoundingBox;
            case ParticleTypes:
                return ((heldObject) dragableBlock.blockPointer).ParticleTypes;
            case PackStatus:
                return ((heldObject) dragableBlock.blockPointer).PackStatus;
            case TitleType:
                return ((heldObject) dragableBlock.blockPointer).TitleType;
            case Potion:
                return ((heldObject) dragableBlock.blockPointer).potion;
            case ListItem:
                return ((heldObject) dragableBlock.blockPointer).ListItem;
            case GameType:
                return ((heldObject) dragableBlock.blockPointer).gameType;
            case EquipmentSlot:
                return ((heldObject) dragableBlock.blockPointer).EquipmentSlot;
            case UseEntity:
                return ((heldObject) dragableBlock.blockPointer).useEntity;
            case ElementType:
                return ((heldObject) dragableBlock.blockPointer).elementType;
            case RayTraceResult:
                return ((heldObject) dragableBlock.blockPointer).rayTrace;
            case PlaceLocation:
                return ((heldObject) dragableBlock.blockPointer).placeLocation;
            case TileEntity:
                return ((heldObject) dragableBlock.blockPointer).tileEntity;

        }
        return 0;
    }
}



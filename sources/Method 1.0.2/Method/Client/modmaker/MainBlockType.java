package Method.Client.modmaker;

public enum MainBlockType {
    Header,  // Starts a chain get from event
    PacketHeader,// Starts a chain for Packet

    // Block to be inserted into something else
    Array,
    Setting,
    Boolean,
    Numbers,
    Timer,
    Entity,
    Worlds,
    Gui,
    Items,
    Blocks,
    Facing,
    Chunk,
    BlockPos,
    String,
    EnumHand,
    UUID,
    ClickType,
    IBlockState,
    ClientStatus,
    EntityAction,
    PlayerDigging,
    EnumHandSide,
    Vec3d,
    Hole,
    HoleType,
    ScoreAction,
    ChatVisibility,
    ConnectionState,
    SoundCategory,
    SoundEvent,
    RecipeBook,
    CombatEvent,
    ItemStack,
    EnumDifficulty,
    BoundingBox,
    ParticleTypes,
    PackStatus,
    TitleType,
    Potion,
    ListItem,
    GameType,
    EquipmentSlot,
    UseEntity,
    ElementType,
    RayTraceResult,
    PlaceLocation,
    TileEntity,

    Wild,
    Contained, // Contained blocks
    Default,
    Null // used sparingly


}

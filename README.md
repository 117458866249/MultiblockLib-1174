# 1174's MultiblockLib 
This is a mod which custom multiblock structure  
How 2 use?
## 00 Config project
Add this mod's cursemaven to your build.gradle
## 01 Register Controller And Port

```java
public class Register {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MultiblockLib.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MultiblockLib.MOD_ID);
    
    // 1.Port
    public static final DeferredBlock<Block> TEST_ITEM_PORT_IN_B = BLOCKS.register(
            "test_item_port_in",
            //                         ↓You can use ItemPortBlock, FluidPortBlock, FEPortBlock to register different port types
            registryName -> new ItemPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    //  ↓The IO Mode of the port, INPUT, OUTPUT or BOTH
                    4, IOMode.INPUT
                 // ↑The size of the port
            ));

    public static final DeferredItem<Item> TEST_ITEM_PORT_IN =
            ITEMS.register("test_item_port_in",
                    registryName -> new PortBlockItem(TEST_ITEM_PORT_IN_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.INPUT
                    ));

    // If your port isn't directional, pls register like this
    public static final DeferredBlock<Block> TEST_WASTE_B = BLOCKS.register(
            "test_waste",
            registryName -> new ItemPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    1, IOMode.OUTPUT
            ) {
                @Override
                public boolean isDirectional() {
                    return false;
                }
            });
    
    // 2.Detector, which can make player know where need to place something
    public static final DeferredItem<Item> TEST_DETECTOR =
            ITEMS.register("test_detector",
                    registryName -> new MultiblockDetector(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                    ));
    
    // 3.Builder, which can build the structure automatically
    public static final DeferredItem<Item> TEST_BUILDER =
            ITEMS.register("test_builder",
                    registryName -> new MultiblockBuilder(new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                    ));
}
```
The hardest part is the Controller  
First, code Controller's BlockEntity
```java
public class TestControllerBlockEntity extends ControllerBlockEntity {
    public TestControllerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Register.TEST_CONTROLLER_BE.get(), worldPosition, blockState);
        //    ↑This is its blockentity type, we'll code them later
    }

    // Both of these values can be changed dynamically
    // Number of recipes the machine can handle at the same time
    @Override
    public int getParallels() {
        return 1;
    }

    // Speed. The bigger, the faster
    @Override
    public int getParseSpeed() {
        return 1;
    }
}
```
Then, code its Block
```java
public class TestControllerBlock extends ControllerBlock {
    public TestControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, Register.TEST_CONTROLLER_BE.get(), TestControllerBlockEntity::tick);
        //                              ↑This is its block register, we'll code them later
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(TestControllerBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TestControllerBlockEntity(blockPos, blockState);
    }
}
```
Last, go back into your register class
```java
public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MultiblockLib.MOD_ID);

public static final DeferredBlock<Block> TEST_CONTROLLER_B = BLOCKS.register(
        "test_controller",
        registryName -> new TestControllerBlock(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, registryName))
        ));

public static final DeferredItem<Item> TEST_CONTROLLER =
        ITEMS.register("test_controller",
                registryName -> new BlockItem(TEST_CONTROLLER_B.get(), new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, registryName))
                ));

public static final Supplier<BlockEntityType<TestControllerBlockEntity>> TEST_CONTROLLER_BE = BLOCK_ENTITY_TYPES.register(
        "test_controller",
        () -> new BlockEntityType<>(
                TestControllerBlockEntity::new,
                false,
                TEST_CONTROLLER_B.get()
        )
);
```
## 02 Structure
Build your structure in you world  
Use this command
```
/multiblocklibes structure -104 56 -61 -109 59 -64 -99 56 -61 TestExport
```
First BlockPos is the controller pos  
Second & third is block of two corners
The string is the file's name
Find your file at /GameFile/multiblockes/xxx.json  
It's a recipe json file  
```json5
{
    "type": "multiblocklibes:structure",
    // Structure's id
    "structure_id": "blast",
    // Controller's block id
    "controller_id": "multiblocklibes:test_controller",
    "pattern": [
        // Block can exist in somewhere, it's a json array
        [1, 0, 0, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [-1, 0, 0, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [1, 0, 1, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [-1, 0, 1, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [1, 0, 2, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [0, 0, 2, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [-1, 0, 2, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [0, 0, 1, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],

        [1, 3, 0, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [0, 3, 0, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [-1, 3, 0, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [1, 3, 1, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [-1, 3, 1, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [1, 3, 2, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [0, 3, 2, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],
        [-1, 3, 2, ["minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou", "multiblocklibes:test_fe_port"]],

        [0, 1, 0, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [1, 1, 0, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [-1, 1, 0, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [1, 1, 1, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [-1, 1, 1, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [1, 1, 2, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [0, 1, 2, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [-1, 1, 2, ["minecraft:diamond_block", "minecraft:emerald_block"]],

        [0, 2, 0, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [1, 2, 0, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [-1, 2, 0, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [1, 2, 1, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [-1, 2, 1, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [1, 2, 2, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [0, 2, 2, ["minecraft:diamond_block", "minecraft:emerald_block"]],
        [-1, 2, 2, ["minecraft:diamond_block", "minecraft:emerald_block"]],

        [0, 3, 1, ["multiblocklibes:test_waste"]]
    ],
    "requirements": [
        // The Structure Requirements
    ]
}
```
You can also add sth in "The Structure Requirements"
```json5
{
    // How many blocks in Structure
    "id": "right_count_blocks_structure_requirement",
    "property": [
        ["multiblocklibes:test_fe_port"],
        1
    ]
}
```
```json5
{
    "id": "max_blocks_structure_requirement",
    "property": [
        ["multiblocklibes:test_fe_port"],
        1
    ]
}
```
```json5
{
    "id": "min_blocks_structure_requirement",
    "property": [
        ["multiblocklibes:test_fe_port"],
        1
    ]
}
```
```json5
// Add Description to the Structure
// Translatable
{
    "id": "desc_structure_requirement",
    "property": [
        "tran",
        "key.modid.sukablyat"
    ]
},
// Literal
{
    "id": "desc_structure_requirement",
    "property": [
        "lite",
        "Also Trys War Thunder !!"
    ]
}
```
```json5
// Some block need be same
{
    "id": "same_block_structure_requirement",
    "property": [
        [0, 1, 0],
        [1, 1, 0],
        [-1, 1, 0],
        [1, 1, 1],
        [-1, 1, 1],
        [1, 1, 2],
        [0, 1, 2],
        [-1, 1, 2],

        [0, 2, 0],
        [1, 2, 0],
        [-1, 2, 0],
        [1, 2, 1],
        [-1, 2, 1],
        [1, 2, 2],
        [0, 2, 2],
        [-1, 2, 2]
    ]
}
```
## 03 Recipe
This is an example recipe
```json5
{
    "type": "multiblocklibes:recipe",
    // Recipe Time
    "time": 100,
    // Recipe ID
    "recipe_id": "blast_revipe_test",
    // Binding Structure
    "structure_id": "blast",
    "requirements": [
        // Recipe requirement
    ]
}
```
Recipe requirements:
```json5
// Item Requirement
{
    "id": "item_recipe_requirement",
    "property": [
        "input",
        "minecraft:iron_ingot",
        3
    ]
},
{
    "id": "item_recipe_requirement",
    "property": [
        "output",
        "minecraft:netherite_ingot",
        1,
        // If you want to make the item can only be output through ports of a specified type
        // Also can be use in fluid and FE requirement
        ["multiblocklibes:test_item_port_ou"]
    ]
}
```
```json5
{
    "id": "fluid_recipe_requirement",
    "property": [
        "output",
        "mod_id:some_fluid",
        2
    ]
}
```
```json5
{
    "id": "fe_recipe_requirement",
    "property": [
        "input",
        5000
    ]
}
```
```json5
{
    "id": "block_recipe_requirement",
    "property": [
        // It won't destroy block when input
        "input",
        ["modid:blovkkkkk"],
        0,3,0
    ]
}
```
```json5
{
    "id": "command_recipe_requirement",
    "property": [
        // When execute, input or output!
        "input",
        "summon modid:sth_mob",
        0,3,0
    ]
}
```
```json5
{
    "id": "desc_recipe_requirement",
    "property": [
        // When execute, input or output!
        "input",
        "tran",
        "key.modid.sukablyat"
    ]
}
```
```json5
{
    // How many blocks in Structure
    "id": "right_count_blocks_structure_requirement",
    "property": [
        ["modid:sth_block"],
        10
    ]
}
```
```json5
{
    "id": "min_count_blocks_structure_requirement",
    "property": [
        ["modid:sth_block"],
        10
    ]
}
```
```json5
{
    "id": "max_count_blocks_structure_requirement",
    "property": [
        ["modid:sth_block"],
        10
    ]
}
```
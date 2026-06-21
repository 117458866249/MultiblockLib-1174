package com.qwq117458866249.multiblocklib.common.recipes.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.qwq117458866249.multiblocklib.api.ModRecipeInput;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonStructure implements Recipe<ModRecipeInput> {
    public static ArrayList<JsonStructure> recipes = new ArrayList<>();

    public static final MapCodec<JsonStructure> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.list(Codec.PASSTHROUGH)
                                    .fieldOf("requirements")
                                    .forGetter(recipe -> {
                                        ArrayList<Dynamic<?>> list = new ArrayList<>();
                                        for (JsonObject obj : recipe.jsonObjects) {
                                            list.add(new Dynamic<>(com.mojang.serialization.JsonOps.INSTANCE, obj));
                                        }
                                        return list;
                                    }),

                            Codec.STRING.fieldOf("structure_id")
                                    .forGetter(recipe -> recipe.structureId),

                            Codec.STRING.fieldOf("controller_id")
                                    .forGetter(recipe -> recipe.controllerId),

                            Codec.list(Codec.PASSTHROUGH)
                                    .fieldOf("pattern")
                                    .forGetter(recipe -> {
                                        ArrayList<Dynamic<?>> list = new ArrayList<>();
                                        for (BlockPos pos : recipe.blocks.keySet()) {
                                            JsonArray obj = new JsonArray();
                                            obj.add(pos.getX());
                                            obj.add(pos.getY());
                                            obj.add(pos.getZ());
                                            obj.add(com.google.gson.JsonParser.parseString(recipe.blocks.get(pos).toString()));
                                            list.add(new Dynamic<>(com.mojang.serialization.JsonOps.INSTANCE, obj));
                                        }
                                        return list;
                                    })
                    ).apply(instance, (reqList, structureId, controllerId, patternList) -> {
                        ArrayList<JsonObject> jsonObjects = new ArrayList<>();
                        for (Dynamic<?> dyn : reqList) {
                            JsonElement elem = dyn.convert(com.mojang.serialization.JsonOps.INSTANCE).getValue();
                            jsonObjects.add(elem.getAsJsonObject());
                        }

                        HashMap<BlockPos, ArrayList<String>> blocks = new HashMap<>();
                        for (Dynamic<?> dyn : patternList) {
                            JsonElement elem = dyn.convert(com.mojang.serialization.JsonOps.INSTANCE).getValue();
                            JsonArray obj = elem.getAsJsonArray();
                            int x = obj.get(0).getAsInt();
                            int y = obj.get(1).getAsInt();
                            int z = obj.get(2).getAsInt();
                            ArrayList<String> blockList = new ArrayList<>();
                            obj.get(3).getAsJsonArray().forEach(e -> blockList.add(e.getAsString()));
                            blocks.put(new BlockPos(x, y, z), blockList);
                        }

                        return new JsonStructure(jsonObjects, blocks, structureId, controllerId);
                    })
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, JsonStructure> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        buf.writeUtf(recipe.structureId);
                        buf.writeUtf(recipe.controllerId);

                        buf.writeCollection(recipe.jsonObjects, (b, obj) ->
                                b.writeUtf(obj.toString())
                        );

                        buf.writeCollection(recipe.blocks.entrySet(), (b, entry) -> {
                            BlockPos pos = entry.getKey();
                            b.writeInt(pos.getX());
                            b.writeInt(pos.getY());
                            b.writeInt(pos.getZ());
                            b.writeCollection(entry.getValue(), (bb, str) -> bb.writeUtf(str));
                        });
                    },
                    buf -> {
                        String structureId = buf.readUtf();
                        String controllerId = buf.readUtf();

                        ArrayList<JsonObject> list = new ArrayList<>();
                        buf.readList(b -> {
                            String json = b.readUtf();
                            return JsonParser.parseString(json).getAsJsonObject();
                        }).forEach(list::add);

                        HashMap<BlockPos, ArrayList<String>> blocks = new HashMap<>();
                        buf.readList(b -> {
                            int x = b.readInt();
                            int y = b.readInt();
                            int z = b.readInt();
                            ArrayList<String> blockList = new ArrayList<>();
                            b.readList(bb -> bb.readUtf()).forEach(blockList::add);
                            return new java.util.AbstractMap.SimpleEntry<>(new BlockPos(x, y, z), blockList);
                        }).forEach(entry -> blocks.put(entry.getKey(), entry.getValue()));

                        return new JsonStructure(list, blocks, structureId, controllerId);
                    }
            );

    public ArrayList<JsonObject> jsonObjects;

    public HashMap<BlockPos, ArrayList<String>> blocks;

    public String structureId;

    public String controllerId;

    public JsonStructure(ArrayList<JsonObject> jsonObjects, HashMap<BlockPos, ArrayList<String>> blocks, String structureId, String controllerId) {
        this.jsonObjects = jsonObjects;
        this.blocks = blocks;
        this.structureId = structureId;
        this.controllerId = controllerId;
    }

    public static final RecipeSerializer<JsonStructure> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(ModRecipeInput modRecipeInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(ModRecipeInput modRecipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<ModRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<ModRecipeInput>> getType() {
        return Register.STRUCTURE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public String toString() {
        return structureId;
    }
}

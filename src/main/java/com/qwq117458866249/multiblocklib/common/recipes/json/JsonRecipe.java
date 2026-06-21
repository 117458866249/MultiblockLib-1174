package com.qwq117458866249.multiblocklib.common.recipes.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.qwq117458866249.multiblocklib.api.ModRecipeInput;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class JsonRecipe implements Recipe<ModRecipeInput> {
    public static ArrayList<JsonRecipe> recipes = new ArrayList<>();

    public static final MapCodec<JsonRecipe> CODEC =
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

                            Codec.INT.fieldOf("time")
                                    .forGetter(recipe -> recipe.time),

                            Codec.STRING.fieldOf("recipe_id")
                                    .forGetter(recipe -> recipe.recipeId),

                            Codec.STRING.fieldOf("structure_id")
                                    .forGetter(recipe -> recipe.structureId)
                    ).apply(instance, (reqList, time, recipeId, structureId) -> {

                        ArrayList<JsonObject> jsonObjects = new ArrayList<>();
                        for (Dynamic<?> dyn : reqList) {
                            JsonElement elem = dyn.convert(com.mojang.serialization.JsonOps.INSTANCE).getValue();
                            jsonObjects.add(elem.getAsJsonObject());
                        }

                        return new JsonRecipe(jsonObjects, time, recipeId, structureId);
                    })
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, JsonRecipe> STREAM_CODEC =
            StreamCodec.of(
                    (buf, recipe) -> {
                        buf.writeInt(recipe.time);
                        buf.writeUtf(recipe.recipeId);
                        buf.writeUtf(recipe.structureId);

                        buf.writeCollection(recipe.jsonObjects, (b, obj) ->
                                b.writeUtf(obj.toString())
                        );
                    },
                    buf -> {
                        int time = buf.readInt();
                        String recipeId = buf.readUtf();
                        String structureId = buf.readUtf();

                        ArrayList<JsonObject> list = new ArrayList<>();
                        buf.readList(b -> {
                            String json = b.readUtf();
                            return JsonParser.parseString(json).getAsJsonObject();
                        }).forEach(list::add);

                        return new JsonRecipe(list, time, recipeId, structureId);
                    }
            );

    public static final RecipeSerializer<JsonRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public ArrayList<JsonObject> jsonObjects;

    public int time;

    public String recipeId;

    public String structureId;

    public JsonRecipe(ArrayList<JsonObject> jsonObjects, int time, String recipeId, String structureId) {
        this.jsonObjects = jsonObjects;
        this.time = time;
        this.recipeId = recipeId;
        this.structureId = structureId;
    }

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
        return Register.RECIPE_TYPE.get();
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
        return recipeId;
    }
}

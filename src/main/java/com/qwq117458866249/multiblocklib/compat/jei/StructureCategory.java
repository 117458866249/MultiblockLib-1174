package com.qwq117458866249.multiblocklib.compat.jei;

import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.json.JsonStructure;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StructureCategory implements IRecipeCategory<JsonStructure> {
    public IGuiHelper helper;
    public JsonStructure currentRecipe;

    public StructureCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public IRecipeType<JsonStructure> getRecipeType() {
        return IRecipeType.create(Identifier.parse("multiblocklibes:structure"), JsonStructure.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("key.multiblock.structure");
    }

    @Override
    public int getWidth() {
        return 142;
    }

    @Override
    public int getHeight() {
        return 110;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.STRUCTURE_BLOCK));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JsonStructure recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addOutputSlot(0, 0)
                .add(new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(currentRecipe.controllerId)), 1));

        HashMap<ArrayList<Item>, AtomicInteger> items = new HashMap<>();
        ArrayList<Item> temp = new ArrayList<>();
        AtomicReference<Item> tempBlock = new AtomicReference<>(Items.AIR);
        AtomicInteger i = new AtomicInteger();
        ArrayList<ItemStack> stacks = new ArrayList<>();

        recipe.blocks.forEach((_, block) -> {
            block.forEach(b -> {
                tempBlock.set(BuiltInRegistries.BLOCK.getValue(Identifier.parse(b)).asItem());
                if (!tempBlock.get().equals(Items.AIR)) {
                    temp.add(tempBlock.get());
                }
            });

            if (items.get(temp) == null) {
                items.put((ArrayList<Item>) temp.clone(), new AtomicInteger(1));
            } else {
                items.get(temp).getAndIncrement();
            }

            tempBlock.set(Items.AIR);
            temp.clear();
        });

        items.forEach((stack, count) -> {
            stack.forEach(item -> stacks.add(new ItemStack(item, count.get())));

            builder.addInputSlot(i.get() % 6 * 20, 20 + i.get() / 6 * 20)
                    .addItemStacks(stacks);

            stacks.clear();
            i.getAndIncrement();
        });
    }

    @Override
    public void draw(JsonStructure recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        AtomicInteger line = new AtomicInteger();

        ArrayList<StructureRequirement> requirements = new ArrayList<>();
        recipe.jsonObjects.forEach(jsonObject -> requirements.add(StructureRequirement.fromJson(jsonObject)));

        guiGraphics.textRenderer().accept(20, 0, Component.translatable("multiblock_structure." + recipe.structureId));

        requirements.forEach(requirement -> {
            if (!requirement.getDesc().equals(Component.empty())) {
                guiGraphics.textRenderer().accept(0, 100 - line.get() * 10, requirement.getDesc());
                line.getAndIncrement();
            }
        });
    }
}

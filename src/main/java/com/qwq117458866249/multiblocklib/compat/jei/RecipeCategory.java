package com.qwq117458866249.multiblocklib.compat.jei;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.api.EmptyStructure;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.FluidRecipeRequirement;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.ItemRecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.json.JsonRecipe;
import com.qwq117458866249.multiblocklib.util.Info;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeCategory implements IRecipeCategory<JsonRecipe> {
    public IGuiHelper helper;
    public JsonRecipe currentRecipe;

    public RecipeCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public IRecipeType<JsonRecipe> getRecipeType() {
        return IRecipeType.create(Identifier.parse("multiblocklibes:recipe"), JsonRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("key.multiblockes.recipe");
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
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.COOKED_CHICKEN));
    }

    @Override
    @Info(m = "Sorry , because of NoClassFoundError , your recipe requirement have to mixin this to display in JEI QwQ")
    public void setRecipe(IRecipeLayoutBuilder builder, JsonRecipe recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        builder.addInputSlot(61, 27)
                .add(new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(Structure.allStructures.getOrDefault(currentRecipe.structureId,new EmptyStructure()).controllerId())),1));

        ArrayList<RecipeRequirement> requirements = new ArrayList<>();
        currentRecipe.jsonObjects.forEach(jsonObject -> requirements.add(RecipeRequirement.fromJson(jsonObject)));
        AtomicInteger input = new AtomicInteger();
        AtomicInteger output = new AtomicInteger();

        requirements.forEach(r -> {
            if (r instanceof ItemRecipeRequirement requirement) {
                if (requirement.isTag) {
                    ArrayList<ItemStack> stacks = new ArrayList<>();
                    BuiltInRegistries.ITEM.stream()
                            .filter(item -> new ItemStack(item).is(requirement.itemTagKey))
                            .toList()
                            .forEach(item -> stacks.add(new ItemStack(item, requirement.count)));
                    builder.addInputSlot(0 + input.get() % 3 * 20, 12 + input.get() / 3 * 20)
                            .addItemStacks(stacks);
                    input.getAndIncrement();
                } else {
                    if (!requirement.isOutput) {
                        builder.addInputSlot(0 + input.get() % 3 * 20, 12 + input.get() / 3 * 20)
                                .add(new ItemStack(requirement.item, requirement.count));
                        input.getAndIncrement();
                    } else {
                        builder.addOutputSlot(82 + output.get() % 3 * 20, 12 + output.get() / 3 * 20)
                                .add(new ItemStack(requirement.item, requirement.count));
                        output.getAndIncrement();
                    }
                }
            }
            if (r instanceof FluidRecipeRequirement requirement) {
                if (requirement.isTag) {
                    ArrayList<FluidStack> stacks = new ArrayList<>();
                    BuiltInRegistries.FLUID.stream()
                            .filter(fluid -> new FluidStack(fluid, 1).is(requirement.fluidTagKey))
                            .toList()
                            .forEach(fluid -> stacks.add(new FluidStack(fluid, requirement.count)));
                    builder.addInputSlot(0 + input.get() % 3 * 20, 12 + input.get() / 3 * 20)
                            .addIngredients(NeoForgeTypes.FLUID_STACK, stacks)
                            .setFluidRenderer(1, false, 18, 18);
                    input.getAndIncrement();
                } else {
                    if (!requirement.isOutput) {
                        builder.addInputSlot(0 + input.get() % 3 * 20, 12 + input.get() / 3 * 20)
                                .addIngredients(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(requirement.fluid, requirement.count)))
                                .setFluidRenderer(1, false, 18, 18);
                        input.getAndIncrement();
                    } else {
                        builder.addOutputSlot(82 + output.get() % 3 * 20, 12 + output.get() / 3 * 20)
                                .addIngredients(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(requirement.fluid, requirement.count)))
                                .setFluidRenderer(1, false, 18, 18);
                        output.getAndIncrement();
                    }
                }
            }
        });
    }

    @Override
    public void draw(JsonRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {

        // Arrow
        for (int x = 61; x <= 68; x++) {
            for (int y = 18; y <= 21; y++) {
                helper.drawableBuilder(Identifier.parse(MultiblockLib.MOD_ID + ":textures/gui/arrow.png"), 100, 100, 1, 1)
                        .build()
                        .draw(guiGraphics, x, y);
            }
        }

        for (int i = 1; i <= 6; i++) {
            for (int y = 13 + i; y <= 26 - i; y++) {
                helper.drawableBuilder(Identifier.parse(MultiblockLib.MOD_ID + ":textures/gui/arrow.png"), 100, 100, 1, 1)
                        .build()
                        .draw(guiGraphics, 68 + i, y);
            }
        }

        // Frame
        for (int x = 59; x <= 78; x++) {
            helper.drawableBuilder(Identifier.parse(MultiblockLib.MOD_ID + ":textures/gui/arrow.png"), 100, 100, 1, 1)
                    .build()
                    .draw(guiGraphics, x, 12);
            helper.drawableBuilder(Identifier.parse(MultiblockLib.MOD_ID + ":textures/gui/arrow.png"), 100, 100, 1, 1)
                    .build()
                    .draw(guiGraphics, x, 44);
        }

        for (int y = 13; y <= 43; y++) {
            helper.drawableBuilder(Identifier.parse(MultiblockLib.MOD_ID + ":textures/gui/arrow.png"), 100, 100, 1, 1)
                    .build()
                    .draw(guiGraphics, 59, y);
            helper.drawableBuilder(Identifier.parse(MultiblockLib.MOD_ID + ":textures/gui/arrow.png"), 100, 100, 1, 1)
                    .build()
                    .draw(guiGraphics, 78, y);
        }

        // Text
        AtomicInteger line = new AtomicInteger();
        ArrayList<RecipeRequirement> requirements = new ArrayList<>();

        recipe.jsonObjects.forEach(jsonObject -> requirements.add(RecipeRequirement.fromJson(jsonObject)));

        guiGraphics.textRenderer().accept(0, 0, Component.translatable("multiblock_structure." + recipe.structureId));

        requirements.forEach(requirement -> {
            if (!requirement.getDesc().equals(Component.empty())) {
                guiGraphics.textRenderer().accept(0, 100 - line.get() * 10, requirement.getDesc());
                line.getAndIncrement();
            }
        });
    }
}

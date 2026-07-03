package com.qwq117458866249.multiblocklib.compat.jei;

import com.qwq117458866249.multiblocklib.common.recipes.json.MultiblockJsonStructure;
import com.qwq117458866249.multiblocklib.util.Util;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class StructureViewCategory implements IRecipeCategory<MultiblockJsonStructure> {
    public IGuiHelper helper;
    public MultiblockJsonStructure currentRecipe;
    public int yLayer = 1;

    public StructureViewCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public IRecipeType<MultiblockJsonStructure> getRecipeType() {
        return IRecipeType.create(Identifier.parse("multiblocklibes:structure_view"), MultiblockJsonStructure.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("key.multiblock.structure_view");
    }

    @Override
    public int getWidth() {
        return 362;
    }

    @Override
    public int getHeight() {
        return 130;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.FILLED_MAP));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MultiblockJsonStructure recipe, IFocusGroup focuses) {
        this.currentRecipe = recipe;

        int xStart = 181 - 10 * recipe.view.getX();
        int zStart = 75 + 10 * recipe.view.getZ();

        ArrayList<ItemStack> stacks = new ArrayList<>();

        recipe.view.getBlocks().forEach((pos, blocks) -> {
            if (pos.getY() + 1 == yLayer) {
                blocks.forEach(block -> {
                    if (block.charAt(0) == '#') {
                        BuiltInRegistries.BLOCK
                                .stream()
                                .filter(filter -> filter.defaultBlockState().is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(Util.getPath(block)))))
                                .toList()
                                .forEach(item -> stacks.add(new ItemStack(item.asItem(), 1)));
                    } else {
                        stacks.add(new ItemStack(BuiltInRegistries.BLOCK.getValue(Identifier.parse(block)).asItem(), 1));
                    }
                });

                builder.addInputSlot(xStart + pos.getX() * 20, zStart - pos.getZ() * 20)
                        .addItemStacks((ArrayList<ItemStack>) stacks.clone());
                stacks.clear();
            }
        });
    }

    @Override
    public void draw(MultiblockJsonStructure recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {

        // Background
        helper.drawableBuilder(Identifier.parse("multiblocklibes:textures/gui/background2.png"), 0, 0, 362, 130).setTextureSize(362, 130).build().draw(guiGraphics);

        // Text
        guiGraphics.textRenderer().accept(0, 0, Component.translatable("multiblock_structure." + recipe.structureId));
        guiGraphics.textRenderer().accept(200, 0, Component.translatable("key.multiblockes.layer").append(yLayer + ""));

        // Layer Calc
        if (mouseX <= 361 && mouseX >= 346 && mouseY >= 0 && mouseY <= 129) {
            if (mouseY == 129) {
                yLayer = recipe.view.getY();
            } else {
                yLayer = ((Number) (mouseY * (recipe.view.getY() + 1) / 129 + 1)).intValue();
            }
        }

        // To Draw
        for (int i = 344; i <= 359; i++) {
            helper.drawableBuilder(Identifier.parse("multiblocklibes:textures/gui/to_draw.png"), 100, 100, 1, 1).setTextureSize(1, 1).build().draw(guiGraphics, i, 1);
            helper.drawableBuilder(Identifier.parse("multiblocklibes:textures/gui/to_draw.png"), 100, 100, 1, 1).setTextureSize(1, 1).build().draw(guiGraphics, i, 127);
        }

        for (int i = 1; i <= 126; i++) {
            if (((Number) (i * (recipe.view.getY() + 1) / 129)).intValue() != ((Number) ((i - 1) * (recipe.view.getY() + 1) / 129)).intValue()) {
                for (int j = 344; j <= 359; j++) {
                    helper.drawableBuilder(Identifier.parse("multiblocklibes:textures/gui/to_draw.png"), 100, 100, 1, 1).setTextureSize(1, 1).build().draw(guiGraphics, j, i);
                }
            }
        }
    }
}

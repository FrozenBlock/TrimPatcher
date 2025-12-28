/*
 * Copyright (C) 2025 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.trimpatcher.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.trimpatcher.TrimPatcherClient;
import net.frozenblock.trimpatcher.client.renderer.item.FakeUnbakedItemModel;
import net.frozenblock.trimpatcher.client.resources.model.ModelStateWrapper;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = BlockModelWrapper.Unbaked.class, priority = 898)
public abstract class BlockModelWrapperUnbakedMixin {

	@Shadow
	public abstract Identifier model();

	@Shadow
	public abstract ItemModel bake(ItemModel.BakingContext bakingContext);

	@Unique
	private boolean trimPatcher$generatingNewModel = false;
	@Unique
	private Material trimPatcher$material = null;
	@Unique
	private String trimPatcher$trimOverlayLayer = null;
	@Unique
	private String trimPatcher$materialName = null;

	@ModifyReturnValue(
		method = "bake",
		at = @At("RETURN")
	)
	public ItemModel trimPatcher$createAutoTrimmedArmors(
		ItemModel original,
		@Local(argsOnly = true) ItemModel.BakingContext context,
		@Local TextureSlots slots
	) {
		if (this.trimPatcher$generatingNewModel) return original;

		final Identifier id = this.model();
		final String path = id.getPath();
		if (!path.contains("item/")) return original;

		final String armorType = TrimPatcherClient.trimAutoModelEndingTerms().stream().filter(path::endsWith).findFirst().orElse(null);
		if (armorType == null) return original;

		final Identifier trimOverlayPrefix = TrimPatcherClient.armorToOverlayPrefix().get(armorType);
		if (trimOverlayPrefix == null) return original;

		final String armorMaterialGuess = path.replace("item/", "").replace("_" + armorType, "");
		final List<String> applicableOverlayMaterials = TrimPatcherClient.getApplicableOverlayMaterials(armorMaterialGuess);
		if (applicableOverlayMaterials.isEmpty()) return original;

		this.trimPatcher$trimOverlayLayer = slots.getMaterial("layer1") != null ? "layer2" : "layer1";
		List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> newTrimOverlays = new ArrayList<>();

		this.trimPatcher$generatingNewModel = true;
		for (String overlayMaterial : applicableOverlayMaterials) {
			this.trimPatcher$material = new Material(TextureAtlas.LOCATION_ITEMS, trimOverlayPrefix.withSuffix("_" + overlayMaterial));;
			this.trimPatcher$materialName = overlayMaterial;

			newTrimOverlays.add(
				ItemModelUtils.when(
					ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.withDefaultNamespace(overlayMaterial.replace("_darker", ""))),
					new FakeUnbakedItemModel(this.bake(context))
				)
			);
		}
		this.trimPatcher$generatingNewModel = false;
		this.trimPatcher$material = null;
		this.trimPatcher$trimOverlayLayer = null;
		this.trimPatcher$materialName = null;

		return ItemModelUtils.select(new TrimMaterialProperty(), new FakeUnbakedItemModel(original), newTrimOverlays).bake(context);
	}

	@ModifyExpressionValue(
		method = "bake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resources/model/ResolvedModel;getTopTextureSlots()Lnet/minecraft/client/renderer/block/model/TextureSlots;"
		)
	)
	public TextureSlots trimPatcher$useAutoTrimmedSlots(TextureSlots original) {
		if (!this.trimPatcher$generatingNewModel) return original;

		final Map<String, Material> newSlotsMap = new Object2ObjectLinkedOpenHashMap<>();
		newSlotsMap.putAll(original.resolvedValues);
		newSlotsMap.put(this.trimPatcher$trimOverlayLayer, this.trimPatcher$material);
		return new TextureSlots(ImmutableMap.copyOf(newSlotsMap));
	}

	@WrapOperation(
		method = "bake",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resources/model/ResolvedModel;bakeTopGeometry(Lnet/minecraft/client/renderer/block/model/TextureSlots;Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/resources/model/ModelState;)Lnet/minecraft/client/resources/model/QuadCollection;"
		)
	)
	public QuadCollection trimPatcher$useAutoTrimmedModelState(
		ResolvedModel instance, TextureSlots slots, ModelBaker baker, ModelState modelState, Operation<QuadCollection> original
	) {
		if (this.trimPatcher$generatingNewModel) return original.call(instance, slots, baker, ModelStateWrapper.create(this.trimPatcher$materialName, modelState));
		return original.call(instance, slots, baker, modelState);
	}

}

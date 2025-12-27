package net.frozenblock.trimpatcher.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.frozenblock.trimpatcher.TrimPatcherClient;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceList;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpriteSourceList.class)
public class SpriteSourceListMixin {

	@WrapOperation(
		method = "load",
		at = @At(
			value = "NEW",
			target = "(Ljava/util/List;)Lnet/minecraft/client/renderer/texture/atlas/SpriteSourceList;"
		)
	)
	private static SpriteSourceList trimPatcher$blendTrimSources(List<SpriteSource> list, Operation<SpriteSourceList> original) {
		final List<PalettedPermutations> trimSources = new ArrayList<>();
		final List<SpriteSource> nonTrimSources = new ArrayList<>();
		for (SpriteSource spriteSource : list) {
			if (spriteSource instanceof PalettedPermutations palettedPermutations && palettedPermutations.paletteKey().equals(TrimPatcherClient.TRIM_PALETTE_KEY)) {
				trimSources.add(palettedPermutations);
				continue;
			}
			nonTrimSources.add(spriteSource);
		}

		if (trimSources.isEmpty()) return original.call(list);

		final List<ResourceLocation> textures = new ArrayList<>();
		final Map<String, ResourceLocation> permutations = new Object2ObjectLinkedOpenHashMap<>();

		for (PalettedPermutations palettedPermutations : trimSources) {
			for (ResourceLocation texture : palettedPermutations.textures()) {
				if (!textures.contains(texture)) textures.add(texture);
			}

			for (Map.Entry<String, ResourceLocation> permutation : palettedPermutations.permutations().entrySet()) {
				final String key = permutation.getKey();
				if (!permutations.containsKey(key)) {
					final ResourceLocation texture = permutation.getValue();
					permutations.put(key, texture);
					TrimPatcherClient.addFoundOverlayMaterial(key);
				}
			}
		}

		final PalettedPermutations combinedTrims = new PalettedPermutations(textures, TrimPatcherClient.TRIM_PALETTE_KEY, permutations);
		final List<SpriteSource> finalSpriteSources = new ArrayList<>(nonTrimSources);
		finalSpriteSources.add(combinedTrims);
		return original.call(finalSpriteSources);
	}

}

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
import net.minecraft.resources.Identifier;
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

		final List<Identifier> textures = new ArrayList<>();
		final Map<String, Identifier> permutations = new Object2ObjectLinkedOpenHashMap<>();

		for (PalettedPermutations palettedPermutations : trimSources) {
			for (Identifier texture : palettedPermutations.textures()) {
				if (!textures.contains(texture)) textures.add(texture);
			}

			for (Map.Entry<String, Identifier> permutation : palettedPermutations.permutations().entrySet()) {
				final String key = permutation.getKey();
				if (!permutations.containsKey(key)) {
					final Identifier texture = permutation.getValue();
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

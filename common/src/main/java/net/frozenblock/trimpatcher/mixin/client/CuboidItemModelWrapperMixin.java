/*
 * Copyright (C) 2025-2026 FrozenBlock
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
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(CuboidItemModelWrapper.class)
public class CuboidItemModelWrapperMixin {
	@Unique
	private static final Set<Pair<Identifier, Identifier>> TRIMPATCHER$ATLAS_MISMATCHES = ConcurrentHashMap.newKeySet();

	@WrapOperation(
		method = "validateAtlasUsage",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resources/Identifier;equals(Ljava/lang/Object;)Z",
			ordinal = 0
		)
	)
	private static boolean trimPatcher$saferAtlasValidation(
		Identifier instance, Object o, Operation<Boolean> original
	) {
		final boolean equal = original.call(instance, o);
		if (!equal && o instanceof Identifier expectedAtlas) {
			final Pair<Identifier, Identifier> mismatch = Pair.of(instance, expectedAtlas);
			if (TRIMPATCHER$ATLAS_MISMATCHES.add(mismatch)) {
				LogUtils.getLogger().warn(
					"Multiple atlases used in model, expected {}, but also got {}",
					expectedAtlas,
					instance
				);
				LogUtils.getLogger().warn(
					"Further instances of this atlas mismatch have been suppressed for brevity"
				);
			}

			return true;
		}

		return equal;
	}
}

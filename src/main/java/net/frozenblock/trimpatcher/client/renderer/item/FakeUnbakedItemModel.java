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

package net.frozenblock.trimpatcher.client.renderer.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemModel;

@Environment(EnvType.CLIENT)
public record FakeUnbakedItemModel(ItemModel wrapped) implements ItemModel.Unbaked {

	@Override
	public MapCodec<? extends ItemModel.Unbaked> type() {
		return null;
	}

	@Override
	public ItemModel bake(ItemModel.BakingContext bakingContext) {
		return this.wrapped;
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
	}
}

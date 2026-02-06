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

package net.frozenblock.trimpatcher.client.resources.model;

import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public class ModelStateWrapper implements ModelState {
	private final String name;
	private final ModelState wrapped;

	public ModelStateWrapper(String name, ModelState modelState) {
		this.name = name;
		this.wrapped = modelState;
	}

	public static ModelStateWrapper create(String name, ModelState modelState) {
		return new ModelStateWrapper(name, modelState);
	}

	@Override
	public Transformation transformation() {
		return this.wrapped.transformation();
	}

	@Override
	public Matrix4fc faceTransformation(Direction direction) {
		return this.wrapped.faceTransformation(direction);
	}

	@Override
	public Matrix4fc inverseFaceTransformation(Direction direction) {
		return this.wrapped.inverseFaceTransformation(direction);
	}
}

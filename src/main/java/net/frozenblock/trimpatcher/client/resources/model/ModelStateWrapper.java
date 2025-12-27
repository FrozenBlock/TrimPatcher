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

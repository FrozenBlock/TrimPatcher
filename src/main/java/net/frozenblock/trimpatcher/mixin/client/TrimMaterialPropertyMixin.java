package net.frozenblock.trimpatcher.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(TrimMaterialProperty.class)
public class TrimMaterialPropertyMixin {

	@ModifyReturnValue(
		method = "get(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;ILnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/resources/ResourceKey;",
		at = @At("RETURN")
	)
	public ResourceKey<TrimMaterial> trimPatcher$useDefaultNamespace(ResourceKey<TrimMaterial> original) {
		if (original == null) return original;
		return ResourceKey.create(original.registryKey(), Identifier.withDefaultNamespace(original.identifier().getPath()));
	}

}

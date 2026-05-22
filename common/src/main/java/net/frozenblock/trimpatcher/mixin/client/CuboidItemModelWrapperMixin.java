package net.frozenblock.trimpatcher.mixin.client;

import com.ibm.icu.impl.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(CuboidItemModelWrapper.class)
public class CuboidItemModelWrapperMixin {

    @Unique
    private static final Set<Pair<Identifier, Identifier>> ATLAS_MISMATCHES = ConcurrentHashMap.newKeySet();

    @Inject(method = "validateAtlasUsage", at = @At("HEAD"), cancellable = true)
    private static void trimPatcher$saferAtlasValidation(List<BakedQuad> quads, CallbackInfo ci) {
        Iterator<BakedQuad> iterator = quads.iterator();

        if (!iterator.hasNext()) {
            ci.cancel();
            return;
        }

        Identifier expectedAtlas = iterator.next()
                .materialInfo()
                .sprite()
                .atlasLocation();

        while (iterator.hasNext()) {
            BakedQuad bakedQuad = iterator.next();

            Identifier actualAtlas = bakedQuad
                    .materialInfo()
                    .sprite()
                    .atlasLocation();

            if (!actualAtlas.equals(expectedAtlas)) {
                Pair<Identifier, Identifier> mismatch = Pair.of(actualAtlas, expectedAtlas);

                if (ATLAS_MISMATCHES.add(mismatch)) {
                    LogUtils.getLogger().warn(
                            "Multiple atlases used in model, expected {}, but also got {}",
                            expectedAtlas,
                            actualAtlas
                    );
                    LogUtils.getLogger().warn(
                            "Further instances of this atlas mismatch have been suppressed for brevity"
                    );
                }
            }
        }

        if (!expectedAtlas.equals(TextureAtlas.LOCATION_ITEMS) && !expectedAtlas.equals(TextureAtlas.LOCATION_BLOCKS)) {
            throw new IllegalArgumentException("Atlas " + expectedAtlas + " can't be used for item models");
        }

        ci.cancel();
    }
}

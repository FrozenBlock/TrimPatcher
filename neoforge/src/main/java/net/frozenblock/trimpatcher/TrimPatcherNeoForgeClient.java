package net.frozenblock.trimpatcher;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = TPConstants.MOD_ID, dist = Dist.CLIENT)
public class TrimPatcherNeoForgeClient {

    public TrimPatcherNeoForgeClient(IEventBus modEventBus) {
        TrimPatcherClient.init();
    }
}

package net.frozenblock.trimpatcher;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(value = TPConstants.MOD_ID, dist = Dist.CLIENT)
public class TrimPatcherNeoForgeClient {

    public TrimPatcherNeoForgeClient(IEventBus modEventBus) {
		TPConstants.UNSTABLE_LOGGING = !FMLLoader.getCurrent().isProduction();
        TrimPatcherClient.init();
    }
}

package net.frozenblock.trimpatcher;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class TrimPatcherFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
		TPConstants.UNSTABLE_LOGGING = FabricLoader.getInstance().isDevelopmentEnvironment();
        TrimPatcherClient.init();
    }
}

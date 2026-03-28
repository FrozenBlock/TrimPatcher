package net.frozenblock.trimpatcher;

import net.fabricmc.api.ClientModInitializer;

public class TrimPatcherFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TrimPatcherClient.init();
    }
}

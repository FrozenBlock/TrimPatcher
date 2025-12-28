package net.frozenblock.trimpatcher.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TrimPathLists {
	public static final List<String> TRIMMABLE_HELMET_PATHS = new ArrayList<>();
	public static final List<String> TRIMMABLE_CHESTPLATE_PATHS = new ArrayList<>();
	public static final List<String> TRIMMABLE_LEGGINGS_PATHS = new ArrayList<>();
	public static final List<String> TRIMMABLE_BOOTS_PATHS = new ArrayList<>();
	static {
		TRIMMABLE_HELMET_PATHS.add("helmet");
		TRIMMABLE_CHESTPLATE_PATHS.add("chestplate");
		TRIMMABLE_LEGGINGS_PATHS.add("leggings");
		TRIMMABLE_BOOTS_PATHS.add("boots");
	}

	public static void addHelmetPath(String path) {
		if (path != null && !path.isEmpty() && !TRIMMABLE_HELMET_PATHS.contains(path)) {
			TRIMMABLE_HELMET_PATHS.add(path);
		}
	}
	public static void addChestplatePath(String path) {
		if (path != null && !path.isEmpty() && !TRIMMABLE_CHESTPLATE_PATHS.contains(path)) {
			TRIMMABLE_CHESTPLATE_PATHS.add(path);
		}
	}
	public static void addLeggingsPath(String path) {
		if (path != null && !path.isEmpty() && !TRIMMABLE_LEGGINGS_PATHS.contains(path)) {
			TRIMMABLE_LEGGINGS_PATHS.add(path);
		}
	}
	public static void addBootsPath(String path) {
		if (path != null && !path.isEmpty() && !TRIMMABLE_BOOTS_PATHS.contains(path)) {
			TRIMMABLE_BOOTS_PATHS.add(path);
		}
	}
}

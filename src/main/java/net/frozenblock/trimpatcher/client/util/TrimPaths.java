/*
 * Copyright (C) 2025 FrozenBlock
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

package net.frozenblock.trimpatcher.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TrimPaths {
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

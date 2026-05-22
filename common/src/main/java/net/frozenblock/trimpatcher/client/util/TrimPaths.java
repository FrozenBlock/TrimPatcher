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

package net.frozenblock.trimpatcher.client.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.frozenblock.trimpatcher.TPConstants;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.resources.Identifier;

public class TrimPaths {
	public static List<String> TRIM_AUTO_MODEL_ENDING_TERMS = new ArrayList<>();
	public static Map<String, Identifier> ARMOR_TO_OVERLAY_PREFIX = new Object2ObjectLinkedOpenHashMap<>();

	static {
		addPath("helmet", ItemModelGenerators.TRIM_PREFIX_HELMET);
		addPath("chestplate", ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		addPath("leggings", ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
		addPath("boots", ItemModelGenerators.TRIM_PREFIX_BOOTS);
	}

	public synchronized static void addPath(String path, Identifier type) {
		if (path == null || path.isEmpty()) return;

		if (TRIM_AUTO_MODEL_ENDING_TERMS.contains(path)) {
			TPConstants.warn("Skipping duplicate trim path: " + path + ", for: " + type, true);
			TPConstants.warn("Currently registered category for " + path + " is: " + ARMOR_TO_OVERLAY_PREFIX.get(path), true);
			return;
		}

		TRIM_AUTO_MODEL_ENDING_TERMS.add(path);
		ARMOR_TO_OVERLAY_PREFIX.put(path, type);
	}
}

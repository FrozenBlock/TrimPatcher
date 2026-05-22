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

package net.frozenblock.trimpatcher;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class TrimPatcherClient {
	public static final Identifier TRIM_PALETTE_KEY = Identifier.withDefaultNamespace("trims/color_palettes/trim_palette");
	private static final Map<String, String> FOUND_OVERLAY_TEXTURES = new ConcurrentHashMap<>();
	private static final Map<String, String> FOUND_DARKER_OVERLAY_TEXTURES = new ConcurrentHashMap<>();

	public static void init() {}

	public synchronized static List<String> getApplicableOverlayMaterials(String guessedMaterial) {
		TPConstants.log("Armor material guess " + guessedMaterial, TPConstants.UNSTABLE_LOGGING);
		final String darkerSearchTerm = guessedMaterial + "_darker";
		final String darkerMatch = FOUND_DARKER_OVERLAY_TEXTURES.keySet().stream()
			.filter(darkerOverlay -> darkerOverlay.endsWith(darkerSearchTerm))
			.findFirst()
			.orElse(null);

		final List<String> foundOverlayMaterials = new ArrayList<>();
		if (darkerMatch != null) foundOverlayMaterials.add(darkerMatch);

		FOUND_OVERLAY_TEXTURES.keySet().stream()
			.filter(overlay -> darkerMatch == null || !overlay.endsWith(guessedMaterial))
			.forEach(foundOverlayMaterials::add);

		if (guessedMaterial.endsWith("en") || guessedMaterial.endsWith("ed")) {
			final String endRemoved = guessedMaterial.substring(0, guessedMaterial.length() - 2);
			getApplicableOverlayMaterials(endRemoved).stream()
				.filter(pair -> !foundOverlayMaterials.contains(pair))
				.forEach(foundOverlayMaterials::addFirst);
		}

		return ImmutableList.copyOf(foundOverlayMaterials);
	}

	public synchronized static void addFoundOverlayMaterial(String material) {
		TPConstants.log("Adding overlay material " + material, TPConstants.UNSTABLE_LOGGING);
		(material.endsWith("_darker") ? FOUND_DARKER_OVERLAY_TEXTURES : FOUND_OVERLAY_TEXTURES).put(material, material);
	}

}

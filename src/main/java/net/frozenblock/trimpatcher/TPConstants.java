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

package net.frozenblock.trimpatcher;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TPConstants {
	public static final String PROJECT_ID = "Trim Patcher";
	public static final String MOD_ID = "trimpatcher";
	public static final Logger LOGGER = LoggerFactory.getLogger(PROJECT_ID);
	/**
	 * Used for features that may be unstable and crash in public builds.
	 * <p>
	 * It's smart to use this for at least registries.
	 */
	public static boolean UNSTABLE_LOGGING = FabricLoader.getInstance().isDevelopmentEnvironment();

	// LOGGING
	public static void log(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.info(message);
	}

	public static void logWithModId(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.info(message + " " + MOD_ID);
	}

	public static void warn(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.warn(message);
	}

	public static void error(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.error(message);
	}

	public static void printStackTrace(String message, boolean shouldPrint) {
		if (shouldPrint) LOGGER.error(message, new Throwable(message).fillInStackTrace());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	public static String string(String path) {
		return id(path).toString();
	}

	public static String safeString(String path) {
		return MOD_ID + "_" + path;
	}
}

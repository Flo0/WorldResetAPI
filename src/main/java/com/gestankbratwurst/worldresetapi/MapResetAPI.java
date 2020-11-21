package com.gestankbratwurst.worldresetapi;

import java.util.UUID;
import java.util.function.IntConsumer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of PSSCore and was created at the 20.11.2020
 *
 * PSSCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MapResetAPI {

  private static MapResetManager mapResetManager;

  public static void init(final JavaPlugin javaPlugin) {
    if (MapResetAPI.mapResetManager != null) {
      throw new IllegalStateException("MapResetAPI can only be initialized once.");
    }
    MapResetAPI.mapResetManager = new MapResetManager(javaPlugin);
  }

  public static void saveChange(final BlockState state) {
    MapResetAPI.mapResetManager.saveChange(state);
  }

  public static WorldChange getOrCreateWorldChange(final UUID worldID) {
    return MapResetAPI.mapResetManager.getOrCreateWorldChange(worldID);
  }

  public static int resetWorldChanges(final UUID worldID) {
    return MapResetAPI.mapResetManager.resetWorldChanges(worldID);
  }

  public static int cleanUpAllWorlds() {
    return MapResetAPI.mapResetManager.cleanUpAllWorlds();
  }

  public static void cleanUpWorldAsync(final UUID worldUD, final IntConsumer resultConsumer, final int chunksPerTick) {
    mapResetManager.cleanUpWorldAsync(worldUD, resultConsumer, chunksPerTick);
  }

  public static void cleanUpAllWorldsAsync(final IntConsumer resultConsumer, final int chunksPerTick) {
    mapResetManager.cleanUpAllWorldsAsync(resultConsumer, chunksPerTick);
  }

}
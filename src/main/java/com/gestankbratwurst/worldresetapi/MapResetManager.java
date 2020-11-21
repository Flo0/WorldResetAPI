package com.gestankbratwurst.worldresetapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntConsumer;
import org.bukkit.Bukkit;
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
public class MapResetManager {

  private final Map<UUID, WorldChange> worldChangeMap;
  private final MapResetThread mapResetThread;

  public MapResetManager(final JavaPlugin javaPlugin) {
    this.worldChangeMap = new HashMap<>();
    this.mapResetThread = new MapResetThread();
    Bukkit.getScheduler().runTaskTimer(javaPlugin, this.mapResetThread, 1L, 1L);
  }

  public void saveChange(final BlockState state) {
    final UUID worldID = state.getWorld().getUID();
    final WorldChange worldChange = this.getOrCreateWorldChange(worldID);
    worldChange.saveChange(state);
  }

  public WorldChange getOrCreateWorldChange(final UUID worldID) {
    WorldChange worldChange = this.worldChangeMap.get(worldID);
    if (worldChange == null) {
      worldChange = new WorldChange(worldID);
      this.worldChangeMap.put(worldID, worldChange);
    }
    return worldChange;
  }

  public int resetWorldChanges(final UUID worldID) {
    final WorldChange worldChange = this.worldChangeMap.get(worldID);
    int fullAmount = 0;
    if (worldChange != null) {
      fullAmount = worldChange.getAmountOfChangedChunks();
      worldChange.resetChunkChanges(fullAmount);
    }
    return fullAmount;
  }

  public int cleanUpAllWorlds() {
    int restoredBlocks = 0;
    for (final UUID worldID : new ArrayList<>(this.worldChangeMap.keySet())) {
      restoredBlocks += this.resetWorldChanges(worldID);
    }
    return restoredBlocks;
  }

  public void cleanUpWorldAsync(final UUID worldID, final IntConsumer resultConsumer, final int chunksPerTick) {
    this.cleanUpWorldAsync(this.getOrCreateWorldChange(worldID), resultConsumer, chunksPerTick);
  }

  private void cleanUpWorldAsync(final WorldChange worldChange, final IntConsumer resultConsumer, final int chunksPerTick) {
    this.mapResetThread.createTask(worldChange, chunksPerTick, resultConsumer);
  }

  public void cleanUpAllWorldsAsync(final IntConsumer resultConsumer, final int chunksPerTick) {
    for (final WorldChange worldChange : this.worldChangeMap.values()) {
      this.cleanUpWorldAsync(worldChange, resultConsumer, chunksPerTick);
    }
  }

}

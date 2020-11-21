package com.gestankbratwurst.worldresetapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of PSSCore and was created at the 20.11.2020
 *
 * PSSCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ChunkChange {

  private final long chunkKey;
  private final UUID worldID;
  private final Map<Long, BlockData> originalBlockData;

  public ChunkChange(final UUID worldID, final long chunkKey) {
    this.chunkKey = chunkKey;
    this.worldID = worldID;
    this.originalBlockData = new HashMap<>();
  }

  public void addChange(final BlockState state) {
    final long blockKey = state.getBlock().getBlockKey();
    if (this.originalBlockData.containsKey(blockKey)) {
      return;
    }
    final BlockData data = state.getBlockData();
    this.originalBlockData.put(blockKey, data);
  }

  public int resetChanges() {
    final World world = Bukkit.getWorld(this.worldID);
    if (world == null) {
      throw new IllegalStateException("The world that should be regenerated is not loaded.");
    }
    final int changes = this.originalBlockData.size();
    final Chunk chunk = world.getChunkAt(this.chunkKey);
    chunk.setForceLoaded(true);

    for (final Entry<Long, BlockData> entry : this.originalBlockData.entrySet()) {
      final Long blockKey = entry.getKey();
      final BlockData blockData = entry.getValue();

      final BlockState changedBlock = world.getBlockAtKey(blockKey).getState();
      changedBlock.setBlockData(blockData);
      changedBlock.update(true);
    }

    chunk.setForceLoaded(false);
    return changes;
  }

}

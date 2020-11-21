package com.gestankbratwurst.worldresetapi;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of PSSCore and was created at the 20.11.2020
 *
 * PSSCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class WorldChange {

  private final UUID worldID;
  private final LinkedHashMap<Long, ChunkChange> chunkChangeMap;

  public WorldChange(final UUID worldID) {
    this.worldID = worldID;
    this.chunkChangeMap = new LinkedHashMap<>();
  }

  public void saveChange(final BlockState state) {
    final long chunkKey = state.getChunk().getChunkKey();
    final ChunkChange chunkChange = this.getOrCreateChunkChange(chunkKey);
    chunkChange.addChange(state);
  }

  public int getAmountOfChangedChunks() {
    return this.chunkChangeMap.size();
  }

  public ChunkChange getOrCreateChunkChange(final Long chunkKey) {
    ChunkChange chunkChange = this.chunkChangeMap.get(chunkKey);
    if (chunkChange == null) {
      chunkChange = new ChunkChange(this.worldID, chunkKey);
      this.chunkChangeMap.put(chunkKey, chunkChange);
    }
    return chunkChange;
  }

  public int resetChunkChanges(final int amount) {
    final Iterator<Entry<Long, ChunkChange>> changesIterator = this.chunkChangeMap.entrySet().iterator();
    int count = 0;
    int blockChanges = 0;
    while (changesIterator.hasNext()) {
      final Entry<Long, ChunkChange> entry = changesIterator.next();
      final ChunkChange chunkChange = entry.getValue();
      blockChanges += chunkChange.resetChanges();
      changesIterator.remove();
      if (++count == amount) {
        break;
      }
    }
    return blockChanges;
  }

}
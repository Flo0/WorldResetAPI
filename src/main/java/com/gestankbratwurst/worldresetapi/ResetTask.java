package com.gestankbratwurst.worldresetapi;

import java.util.function.IntConsumer;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of PSSCore and was created at the 20.11.2020
 *
 * PSSCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResetTask {

  private int blocksChanged = 0;
  private final int chunksPerTick;
  private final WorldChange worldChange;
  private final IntConsumer resultConsumer;

  public ResetTask(final WorldChange worldChange, final int chunksPerTick, final IntConsumer resultConsumer) {
    this.worldChange = worldChange;
    this.resultConsumer = resultConsumer;
    this.chunksPerTick = chunksPerTick;
  }

  public boolean progress() {
    this.blocksChanged += this.worldChange.resetChunkChanges(this.chunksPerTick);
    if (this.worldChange.getAmountOfChangedChunks() == 0) {
      this.resultConsumer.accept(this.blocksChanged);
      return true;
    }
    return false;
  }

}

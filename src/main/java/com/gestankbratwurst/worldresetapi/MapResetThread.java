package com.gestankbratwurst.worldresetapi;

import java.util.ArrayDeque;
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
public class MapResetThread implements Runnable {

  private final ArrayDeque<ResetTask> queuedTasks;

  public MapResetThread() {
    this.queuedTasks = new ArrayDeque<>();
  }

  public void createTask(final WorldChange worldChange, final int chunksPerTick, final IntConsumer resultConsumer) {
    this.queuedTasks.add(new ResetTask(worldChange, chunksPerTick, resultConsumer));
  }


  @Override
  public void run() {
    if (this.queuedTasks.isEmpty()) {
      return;
    }
    final ResetTask resetTask = this.queuedTasks.peek();
    if (resetTask.progress()) {
      this.queuedTasks.poll();
    }
  }
}
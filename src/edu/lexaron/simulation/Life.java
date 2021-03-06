package edu.lexaron.simulation;

import edu.lexaron.simulation.cells.Cell;
import edu.lexaron.simulation.world.Location;
import edu.lexaron.simulation.world.World;

/**
 * This class allows each live {@link Cell} to live by running each {@link Cell}´s {@link Cell#live(World)} method.
 * Apart from that, newborn {@link Cell}s are added into the {@link World} while corpses that were consumed are removed
 * from it.
 *
 * Author: Mirza Suljić <mirza.suljic.ba@gmail.com>
 * Date: 03.06.2016
 */
public class Life implements Runnable {
  private final World world;

  Life(World world) {
    this.world = world;
  }

  private void allLiveCellsHunt() {
    world.getAllCells().addAll(world.getNewBornCells());
    world.getNewBornCells().forEach(cell -> world.getNewWorld().get(Location.of(cell.getX(), cell.getY())).setCell(cell));
    world.getNewBornCells().clear();

    world.getAllCells().removeAll(world.getEatenCorpses());
    world.getEatenCorpses().forEach(cell -> world.getNewWorld().get(Location.of(cell.getX(), cell.getY())).setDeadCell(null));
    world.getEatenCorpses().clear();

    world.getAllCells().stream().filter(Cell::isAlive).forEach(cell -> cell.live(world));
  }

  @Override
  public void run() {
    synchronized (world) {
      allLiveCellsHunt();
    }
  }

}

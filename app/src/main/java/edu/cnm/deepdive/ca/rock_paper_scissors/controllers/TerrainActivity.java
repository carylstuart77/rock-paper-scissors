package edu.cnm.deepdive.ca.rock_paper_scissors.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.cnm.deepdive.ca.rock_paper_scissors.R;
import edu.cnm.deepdive.ca.rock_paper_scissors.models.Terrain;
import edu.cnm.deepdive.ca.rock_paper_scissors.views.TerrainView;

//
public class TerrainActivity extends AppCompatActivity {

  private static final int RUNNER_THREAD_REST = 40;
  private static final int RUNNER_THREAD_SLEEP = 50;
  //fields
  private boolean running = false;
  private boolean inForeground = false;
  private Terrain terrain = null;
  private TerrainView terrainView = null;
  private View terrainLayout;
  private Runner runner = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_terrain);
    initializeModel();
    initializeUserInterface();
  }

  @Override
  protected void onStart() {
    super.onStart();
    setInForeground(true);
  }

  @Override
  protected void onStop() {
    setInForeground(false);
    super.onStop();
  }

  //Host menu items.
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    //Update menu item state based on whether model is running.
    boolean running = isRunning();
    menu.findItem(R.id.run_item).setVisible(!running);
    menu.findItem(R.id.pause_item).setVisible(running);
    menu.findItem(R.id.reset_item).setEnabled(!running);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.run_item:
        // Start the model running
        setRunning(true);
        break;
      case R.id.pause_item:
        // Stop the model
        setRunning(false);
      case R.id.reset_item:
        //Reset the model
        setInForeground(false);
        initializeModel();
        setInForeground(true);
        break;
      default:
        return super.onOptionsItemSelected(item);
    }
    invalidateOptionsMenu();
    return true;

  }

  private void initializeModel() {
    terrain = new Terrain();
    terrain.initialize();

  }

  private void initializeUserInterface() {
    terrainLayout = findViewById(R.id.terrainLayout);
    terrainView = (TerrainView) findViewById(R.id.terrainView);
  }

  public synchronized boolean isRunning() {
    return running;
  }

  public synchronized void setRunning(boolean running) {
    this.running = running;
  }

  public synchronized boolean isInForeground() {
    return inForeground;
  }

  public synchronized void setInForeground(boolean inForeground) {
    if (inForeground) {
      this.inForeground = true;
      if (runner == null) {
        runner = new Runner();
        runner.start();
      }
      terrainLayout.post(new Runnable() {
        @Override
        public void run() {
          terrainView.setSource(terrain.getSnapshot());
        }
      });
    } else {
      this.inForeground = false;
      runner = null;
    }
  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (isInForeground()) {
        while (isRunning() && isInForeground()) {
          terrain.step();
          terrainView.setSource(terrain.getSnapshot());
          try {
            Thread.sleep(RUNNER_THREAD_REST);
          } catch (InterruptedException ex) {
            // Do nothing.
          }
        }
        try {
          Thread.sleep(RUNNER_THREAD_SLEEP);
        } catch (InterruptedException ex) {
          //Do nothing
        }
      }

    }
  }
}


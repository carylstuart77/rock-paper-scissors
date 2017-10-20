package edu.cnm.deepdive.ca.rock_paper_scissors.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import edu.cnm.deepdive.ca.rock_paper_scissors.models.Breed;

public class TerrainView extends View {

  //Initialization steps

  private Paint paint = new Paint();
  private Breed[][] source = null;
  private boolean drawing = false;

  {
    setWillNotDraw(false);
  }

  public TerrainView(Context context) {
    super(context);
  }

  public TerrainView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TerrainView(Context context, AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public TerrainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes); }
//DO above } correct?
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int contentSize = Math.max(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
    int width = resolveSizeAndState(getPaddingLeft() + getPaddingRight() + contentSize,
        widthMeasureSpec, 0);
    int height = resolveSizeAndState(getPaddingTop() + getPaddingBottom() + contentSize,
        heightMeasureSpec, 0);
    contentSize = Math.max(width - getPaddingLeft() - getPaddingRight(),
        height - getPaddingTop() - getPaddingBottom());
    width = resolveSizeAndState(getPaddingLeft() + getPaddingRight() + contentSize,
        widthMeasureSpec, 0);
    height = resolveSizeAndState(getPaddingTop() + getPaddingBottom() + contentSize,
        heightMeasureSpec, 0);
    setMeasuredDimension(width, height);

  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (!isDrawing() && source != null){
      setDrawing(true);
      Breed[][] cells= source;
      float cellSize = Math.min(1.0f * canvas.getHeight() / cells.length,
          1.0f * canvas.getWidth() / cells[0].length);
      for (int i = 0; i < cells.length; i++){
        for (int j = 0; j < cells[i].length; j++){
          switch (cells[i][j]) {
            case ROCK:
              paint.setColor(Color.RED);
              break;
            case PAPER:
              paint.setColor(Color.GREEN);
              break;
            case SCISSORS:
              paint.setColor(Color.BLUE);
              break;
          }
          canvas.drawOval(
              j * cellSize, i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, paint
          );
        }
      }
      setDrawing(false);

      }
    }

  public synchronized void setSource(Breed[][] source) {
    this.source = source;
    postInvalidate();
  }

  private synchronized boolean isDrawing() {
    return drawing;
  }

  private synchronized void setDrawing(boolean drawing) {
    this.drawing = drawing;
  }
}

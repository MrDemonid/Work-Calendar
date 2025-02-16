package mr.demonid.workcalendar.views.controls;

import android.graphics.Canvas;

/**
 * Отрисовка ячейки календаря.
 */
public interface CellRenderer {
    void render(AbstractCell cell, Canvas canvas);
}

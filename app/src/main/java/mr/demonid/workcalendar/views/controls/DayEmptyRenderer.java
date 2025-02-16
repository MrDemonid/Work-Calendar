package mr.demonid.workcalendar.views.controls;

import android.graphics.Canvas;
import android.graphics.Paint;

import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.PaintStore;

public class DayEmptyRenderer implements CellRenderer {

    @Override
    public void render(AbstractCell cell, Canvas canvas) {
        if (cell.type == DayType.EMPTY) {
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.EMPTY));
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.EMPTY));
        } else {
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBackgroundPaint(DayType.FREE_DAY));
            canvas.drawRect(cell.left, cell.top, cell.right, cell.bottom, PaintStore.getBorderPaint(DayType.FREE_DAY));
        }
    }
}

package mr.demonid.workcalendar.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mr.demonid.workcalendar.types.CellRendererStyle;
import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.controls.AbstractCell;
import mr.demonid.workcalendar.views.controls.CalendarCell;
import mr.demonid.workcalendar.views.controls.DayEmptyRenderer;
import mr.demonid.workcalendar.views.controls.LabelCell;
import mr.demonid.workcalendar.views.controls.DayCircleRenderer;
import mr.demonid.workcalendar.views.controls.DayFillCircleRenderer;
import mr.demonid.workcalendar.views.controls.DayRectRenderer;


/**
 * Превьюшка стилей ячеек календаря, для меню выбора стиля.
 */
public class StylePreviewCell extends View {

    private AbstractCell cell;
    private CellRendererStyle rendererStyle;


    public StylePreviewCell(Context context) {
        super(context);
        init();
    }

    public StylePreviewCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        cell = new CalendarCell(0f, 0f, getWidth(), getHeight(), DayType.DAY_SHIFT, "12");
        setRendererStyle(CellRendererStyle.CIRCLE);
    }

    /**
     * Установка отображаемого в превью стиля стиля.
     */
    public void setRendererStyle(CellRendererStyle rendererStyle) {
        this.rendererStyle = rendererStyle;
        switch (rendererStyle) {
            case CIRCLE:
                cell.setRenderer(new DayCircleRenderer());
                break;
            case FILL_CIRCLE:
                cell.setRenderer(new DayFillCircleRenderer());
                break;
            case FILL_RECT:
                cell.setRenderer(new DayRectRenderer());
                break;
            case LABEL:
                cell = new LabelCell(0f, 0f, getWidth(), getHeight(), "Пн");
                break;
            default:
                cell.setRenderer(new DayEmptyRenderer());
        }
        update();
    }

    public CellRendererStyle getRendererStyle() {
        return this.rendererStyle;
    }

    public void setType(DayType type) {
        cell.setType(type);
        update();
    }

    public void update() {
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        cell.setRect(0, 0, getWidth(), getHeight());
        cell.onDraw(canvas);
    }

}

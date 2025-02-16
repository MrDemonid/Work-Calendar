package mr.demonid.workcalendar.views.controls;

import mr.demonid.workcalendar.types.DayType;

/**
 * Разметка календаря
 */
public class LabelCell extends AbstractCell {

//    PaintStore paintStore;

    //region Конструкторы
    public LabelCell(float left, float top, float right, float bottom, String text) {
        super(left, top, right, bottom, DayType.LABEL, text, 3f, true);
        setId(-1);
        setRenderer(new LabelRenderer());
    }

    public LabelCell() {
        this(0, 0, 0, 0, "");
    }

    //endregion

}

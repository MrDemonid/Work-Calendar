package mr.demonid.workcalendar.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import mr.demonid.workcalendar.events.OnWorkTableClickListener;
import mr.demonid.workcalendar.events.OnWorkTableSwypeListener;
import mr.demonid.workcalendar.model.CalendarUtil;
import mr.demonid.workcalendar.model.WorkSchedule;
import mr.demonid.workcalendar.types.SwypeType;
import mr.demonid.workcalendar.views.controls.AbstractCell;
import mr.demonid.workcalendar.views.controls.CellRenderer;
import mr.demonid.workcalendar.views.controls.CalendarCell;
import mr.demonid.workcalendar.types.DayType;
import mr.demonid.workcalendar.views.controls.LabelCell;

/**
 * Виджет календаря.
 */
public class WorkCalendarView extends View {

    private ArrayList<AbstractCell> cells;  // ячейки календаря
    private WorkSchedule workSchedule;            // текущий график
    private int columns;                    // столбцов
    private int rows;                       // строк

    CalendarUtil calendar;

    OnWorkTableClickListener clickListener;
    OnWorkTableSwypeListener swypeListener;

    private GestureDetector gestureDetector; // Обработчик жестов


    //region Конструкторы
    public WorkCalendarView(Context context) {
        super(context);
        init(context);
    }

    public WorkCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /*
     * Инициализация класса.
     */
    private void init(Context context) {
        makeCells();
        calendar = CalendarUtil.getInstance();
        clickListener = null;
        swypeListener = null;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }
    //endregion


    /**
     * Переход к отображению следующего месяца.
     */
    public void nextMonth() {
        calendar.nextMonth();
        if (workSchedule != null)
            workSchedule.refresh();
        renameCells();
        invalidate();
    }

    /**
     * Переход к отображению предыдущего месяца.
     */
    public void prevMonth() {
        calendar.prevMonth();
        if (workSchedule != null)
            workSchedule.refresh();
        renameCells();
        invalidate();
    }

    /**
     * Смена отображаемого рабочего графика.
     */
    public void changeWorkTheme(WorkSchedule theme) {
        this.workSchedule = theme;
        if (workSchedule != null) {
            workSchedule.refresh();
        }
        renameCells();
        invalidate();
    }

    /**
     * Возвращает заголовок таблицы, сформированный из текущей даты.
     */
    public String getHeader() {
        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        return monthNames[calendar.getCurrentMonth()] + ", " + calendar.getCurrentYear();
    }

    public void setClickListener(OnWorkTableClickListener listener) {
        this.clickListener = listener;
    }

    public void setSwypeListener(OnWorkTableSwypeListener swypeListener) {
        this.swypeListener = swypeListener;
    }

    /**
     * Задает ячейкам рендер для отрисовки
     */
    public void setCellsStyle(CellRenderer renderer) {
        cells.forEach(e -> {
            if (e.getType() != DayType.LABEL) {
                e.setRenderer(renderer);
            }
        });
    }


    /**
     * Полная прорисовка календаря.
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        cells.forEach(c -> c.onDraw(canvas));
    }

    /**
     * Изменение размеров календаря.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resizeCells(w, h);
    }

    /**
     * Определяем на какую ячейку нажал пользователь и отсылаем уведомлеие подписчику.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // обрабатываем возможные свайпы
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        // обрабатываем нажатия на ячейки
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float cellWidth = (float) getWidth() / columns;
            float cellHeight = (float) getHeight() / rows;
            int cellX = (int) (event.getX() / cellWidth);
            int cellY = (int) (event.getY() / cellHeight); // это вместе с ячейками дней недели (y == 0)
            AbstractCell cell = cells.get(cellY * rows + cellX);

            if (clickListener != null && cell.getId() > 0) {
                clickListener.onCellClick(cell.getId());
            }
            return performClick();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }


    /*
    Создание ячеек таблицы календаря.
     */
    private void makeCells() {
        columns = 7;
        rows = 7;
        cells = new ArrayList<>(columns * rows);

        for (int col = 0; col < columns; col++) {
            cells.add(new LabelCell());   // создаем заголовки дней недели
        }
        for (int row = 1; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                cells.add(new CalendarCell());   // создаем ячейки
            }
        }
    }

    /*
     * Изменяет размеры ячеек в соответствии с размером родительского окна.
     * Так же задает типы ячейкам тела календаря.
     */
    private void resizeCells(float parentWidth, float parentHeight) {
        float cellWidth = parentWidth / columns;
        float cellHeight = parentHeight / rows;

        Log.d("WorkCalendar", "resize cells to (w = " + cellWidth + ", h = " + cellHeight + ")\n");

        int firstDay = (~calendar.getFirstDayOfWeek()) + 2 - 7;   // день недели первого числа текущего месяца
        int daysOfMonths = calendar.getDaysInCurrentMonth();

        for (int idx = 0, row = 0; row < rows; row++, idx += columns) {
            for (int col = 0; col < columns; col++) {
                float left = col * cellWidth;
                float top = row * cellHeight;
                float right = left + cellWidth;
                float bottom = top + cellHeight;
                AbstractCell cell = cells.get(idx + col);
                if (firstDay > 0 && firstDay <= daysOfMonths) {
                    cell.setText("" + firstDay);
                }
                firstDay++;
                cell.setRect(left, top, right, bottom);
            }
        }
    }

    /*
     * Назначает имена ячейкам в соответствии с текущей датой.
     */
    private void renameCells() {
        String[] weeks = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

        int firstDay = (~calendar.getFirstDayOfWeek()) + 2;   // день недели первого числа текущего месяца
        int numDays = calendar.getDaysInCurrentMonth();

        System.out.println(">> num days: %d" + numDays);
        for (int col = 0; col < columns; col++) {
            AbstractCell call = cells.get(col);
            call.setText(weeks[col]);
        }
        for (int idx = columns, row = 1; row < rows; row++, idx += columns) {
            for (int col = 0; col < columns; col++) {
                AbstractCell call = cells.get(idx + col);

                DayType type = workSchedule != null ? workSchedule.getDayType(firstDay) : DayType.FREE_DAY;
                if (firstDay > 0 && firstDay <= numDays) {
                    call.setId(firstDay);
                    call.setText(String.valueOf(firstDay));
                } else {
                    call.setId(0);
                    type = DayType.EMPTY;
                    call.setText("");
                }
                call.setType(type);
                firstDay++;
            }
        }
    }


    /*
     * Класс для обработки жестов.
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;             // минимальное расстояние свайпа (в пикселях)
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;    // минимальная скорость свайпа

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null && e2 != null) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

//                Log.d("Cal", String.format("-- diff X = %f, diff Y = %f, vel X = %f, vel Y = %f\n", diffX, diffY, velocityX, velocityY));
                if (swypeListener != null) {
                    if ((Math.abs(diffX) > SWIPE_THRESHOLD || Math.abs(diffY) > SWIPE_THRESHOLD)
                            && (Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD || Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)) {
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            // движение по горизонтали
                            SwypeType dir = diffX > 0 ? SwypeType.RIGHT : SwypeType.LEFT;
                            swypeListener.OnSwype(dir);
                        } else {
                            // движение по вертикали
                            SwypeType dir = diffY > 0 ? SwypeType.DOWN : SwypeType.UP;
                            swypeListener.OnSwype(dir);
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

}

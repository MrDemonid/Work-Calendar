package mr.demonid.workcalendar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.config.CalendarUnit;
import mr.demonid.workcalendar.model.WorkSchedulesManager;
import mr.demonid.workcalendar.model.WorkSchedule;
import mr.demonid.workcalendar.types.CellRendererStyle;
import mr.demonid.workcalendar.views.WorkCalendarView;
import mr.demonid.workcalendar.views.controls.DayCircleRenderer;
import mr.demonid.workcalendar.views.controls.DayEmptyRenderer;
import mr.demonid.workcalendar.views.controls.DayFillCircleRenderer;
import mr.demonid.workcalendar.views.controls.DayRectRenderer;

/**
 * Определение смещений для показа рабочих дней графиков.
 */
public class SettingSchedulesOffsetsActivity extends AppCompatActivity {

    private WorkCalendarView calendarTable;
    private WorkSchedulesManager workSchedulesManager;
    private AppSettings config;

    // исходные настройки
    private List<CalendarUnit> oldSettings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_modes);

        backup();

        workSchedulesManager = new WorkSchedulesManager();

        WorkSchedule currentSchedule = workSchedulesManager.getCurrentWorkSchedule();
        if (currentSchedule != null) {
            initCalendar(currentSchedule, config.getDayStyle());
            ((TextView) findViewById(R.id.titleCalendarTheme)).setText(calendarTable.getHeader());
            ((TextView) findViewById(R.id.textCurrentTheme)).setText(currentSchedule.getType().getDescription());

            initButtonsSwitchThemes();
            initButtonsAction();
        } else {
            finish();
        }
    }

    /*
     * Инициализация календаря
     */
    private void initCalendar(WorkSchedule theme, CellRendererStyle style) {
        calendarTable = findViewById(R.id.workCalendarTheme);
        calendarTable.changeWorkTheme(theme);
        switch (style) {
            case CIRCLE:
                calendarTable.setCellsStyle(new DayCircleRenderer());
                break;
            case FILL_CIRCLE:
                calendarTable.setCellsStyle(new DayFillCircleRenderer());
                break;
            case FILL_RECT:
                calendarTable.setCellsStyle(new DayRectRenderer());
                break;
            default:
                calendarTable.setCellsStyle(new DayEmptyRenderer());
        }
        calendarTable.setClickListener(this::setNewOffset);
    }

    /*
     * Задает новое смещение для текущего графика
     */
    private void setNewOffset(int day) {
        WorkSchedule theme = workSchedulesManager.getCurrentWorkSchedule();
        if (theme != null) {
            theme.setStartOffset(day);
            calendarTable.changeWorkTheme(theme);
        }
    }

    /*
     * Инициализация кнопок смены графиков.
     */
    private void initButtonsSwitchThemes() {
        ImageButton btnPrev = findViewById(R.id.btnPrevTheme);
        ImageButton btnNext = findViewById(R.id.btnNextTheme);

        btnPrev.setOnClickListener(v -> {
            calendarTable.changeWorkTheme(workSchedulesManager.prev());
            TextView themeName = findViewById(R.id.textCurrentTheme);
            themeName.setText(workSchedulesManager.getCurrentWorkSchedule().getType().getDescription());
        });
        btnNext.setOnClickListener(v -> {
            calendarTable.changeWorkTheme(workSchedulesManager.next());
            TextView themeName = findViewById(R.id.textCurrentTheme);
            themeName.setText(workSchedulesManager.getCurrentWorkSchedule().getType().getDescription());
        });
    }

    /*
     * Инициализация кнопок действий.
     */
    private void initButtonsAction() {
        Button btnSave = findViewById(R.id.btnSaveTheme);
        Button btnCancel = findViewById(R.id.btnCancelTheme);

        btnSave.setOnClickListener(v -> {
            save();
            finish();
        });
        btnCancel.setOnClickListener(v -> {
            restore();
            finish();
        });
    }

    /*
     * Сохраняем исходные настройки.
     */
    private void backup() {
        config = AppSettings.getInstance();
        config.load();
        // сохраняем исходные настройки
        oldSettings = new ArrayList<>();
        config.forEach(oldSettings::add);
    }

    /*
     * Восстанавливает исходные настройки.
     */
    private void restore() {
        oldSettings.forEach(e -> config.setSettingUnit(e));
        config.save();
    }

    /*
     * Применяет новые настройки.
     */
    private void save() {
        workSchedulesManager.forEach(e -> config.setOffsetFirstDay(e.getType(), e.getStartOffset()));
        config.save();
    }

}

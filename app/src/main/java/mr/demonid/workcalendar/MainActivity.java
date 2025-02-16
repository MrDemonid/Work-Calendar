package mr.demonid.workcalendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.config.CalendarUnit;
import mr.demonid.workcalendar.config.ColorSettings;
import mr.demonid.workcalendar.events.OnWorkTableSwypeListener;
import mr.demonid.workcalendar.model.WorkSchedulesManager;
import mr.demonid.workcalendar.model.WorkSchedule;
import mr.demonid.workcalendar.types.CellRendererStyle;
import mr.demonid.workcalendar.types.SwypeType;
import mr.demonid.workcalendar.views.PaintStore;
import mr.demonid.workcalendar.views.WorkCalendarView;
import mr.demonid.workcalendar.views.WorkScheduleAdapter;
import mr.demonid.workcalendar.views.controls.DayCircleRenderer;
import mr.demonid.workcalendar.views.controls.DayEmptyRenderer;
import mr.demonid.workcalendar.views.controls.DayFillCircleRenderer;
import mr.demonid.workcalendar.views.controls.DayRectRenderer;


/**
 * Основной экран приложения.
 */
public class MainActivity extends AppCompatActivity implements OnWorkTableSwypeListener {

    private TextView title;
    private WorkCalendarView calendarTable;
    private WorkSchedulesManager workSchedulesManager;
    private PaintStore paintStore;
    private AppSettings appConfig;
    private ColorSettings colorsConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appConfig = AppSettings.getInstance();
        colorsConfig = ColorSettings.getInstance();
        paintStore = PaintStore.getInstance();      // загружаем в PaintStore актуальные настройки

        updateSettings();

        /*
            Обработчики кнопок смены текущего месяца
         */
        Button buttonPrev = findViewById(R.id.buttonPrev);
        buttonPrev.setOnClickListener(v -> doPrevMonth());
        Button buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(v -> doNextMonth());
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshRadioButtons();
    }

    /**
     * Обновление данных класса
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WorkSchedule workSchedule = workSchedulesManager.getCurrentWorkSchedule();
        appConfig.setCurrentSchedule(workSchedule != null ? workSchedule.getId() : null);
        appConfig.save();
    }

    /**
     * Обработчик жестов календаря
     *
     * @param direction Направление жеста.
     */
    @Override
    public void OnSwype(SwypeType direction) {
        switch (direction) {
            case LEFT:
                doNextMonth();
                break;
            case RIGHT:
                doPrevMonth();
                break;
            case UP:
//            case DOWN:
                doSettings();
        }
    }

    /*
     * Обновление настроек приложения, чтобы на экране отображались актуальные данные.
     */
    private void updateSettings() {
        Log.d("Main.updateSettings()", "begin");
        appConfig.load();
        colorsConfig.load();
        paintStore.load();
        workSchedulesManager = new WorkSchedulesManager();
        Log.d("Main.updateSettings()", "current theme: " + workSchedulesManager.getCurrentWorkSchedule());
        updateCalendar(workSchedulesManager.getCurrentWorkSchedule(), appConfig.getDayStyle());
        title = findViewById(R.id.title);
        title.setText(calendarTable.getHeader());
        title.setTextColor(colorsConfig.getTitleColor());
        calendarTable.setSwypeListener(this);

        refreshRadioButtons();
    }

    /*
     * Обновление стиля календаря и графика работы
     */
    private void updateCalendar(WorkSchedule theme, CellRendererStyle style) {
        if (theme != null)
            Log.d("Main.UpdateCalendar", "Set theme: " + theme.getId() + ",   type = " + theme.getType().toString());
        else
            Log.d("Main.UpdateCalendar", "No theme!");

        calendarTable = findViewById(R.id.customTableView);
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
    }

    /*
     * Обновляет названия радио-кнопок выбора графика дежурств.
     */
    private void refreshRadioButtons() {
        Log.d("Main.refreshRadioButtons()", "begin");
        RecyclerView recyclerView = findViewById(R.id.recyclerThemes);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        // составляем список доступных графиков
        List<CalendarUnit> themes = new ArrayList<>();
        appConfig.forEach(themes::add);

        // задаем ориентацию списка графиков
        GridLayoutManager layoutManager;
        if (isLandscape) {
            layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, themes.size() > 2 ? 2 : 1, GridLayoutManager.HORIZONTAL, false);
        }
        recyclerView.setLayoutManager(layoutManager);


        if (recyclerView.getAdapter() != null && recyclerView.getAdapter() instanceof WorkScheduleAdapter) {
            // адаптер существует, обновляем данные
            WorkSchedule current = workSchedulesManager.getCurrentWorkSchedule();
            WorkScheduleAdapter workScheduleAdapter = (WorkScheduleAdapter) recyclerView.getAdapter();
            workScheduleAdapter.updateScheduleList(themes);
            workScheduleAdapter.setSelectedPosition(current != null ? current.getId() : null);
        } else {
            WorkScheduleAdapter workScheduleAdapter = new WorkScheduleAdapter(themes, theme -> {
                workSchedulesManager.setCurrentSchedule(theme.getId());
                calendarTable.changeWorkTheme(workSchedulesManager.getCurrentWorkSchedule());
//                Toast.makeText(this, theme.getName(), Toast.LENGTH_SHORT).show();
            });
            WorkSchedule current = workSchedulesManager.getCurrentWorkSchedule();
            recyclerView.setAdapter(workScheduleAdapter);
            workScheduleAdapter.setSelectedPosition(current != null ? current.getId() : null);
        }
    }

    /*
        Переход на предыдущий месяц календаря
     */
    private void doPrevMonth() {
        calendarTable.prevMonth();
        title.setText(calendarTable.getHeader());
    }

    /*
        Переход на следующий месяц календаря
     */
    private void doNextMonth() {
        calendarTable.nextMonth();
        title.setText(calendarTable.getHeader());
    }

    /*
        Переход на страницу настроек
     */
    private void doSettings() {
        calendarTable.setSwypeListener(null);
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_do_setup, menu);
        return true;
    }

    // Обрабатываем нажатие на кнопку "+"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.run_setup) {
            doSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
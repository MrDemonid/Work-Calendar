package mr.demonid.workcalendar;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.config.CalendarUnit;
import mr.demonid.workcalendar.types.WorkType;


/**
 * Добавление, удаление и переименование графиков.
 */
public class SettingSchedulesOnOffActivity extends AppCompatActivity {

    private AppSettings config;

    LinearLayout container;
    Map<UUID, CalendarUnit> workSchedules;
    Map<UUID, EditText> editControls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_descriptions);
        config = AppSettings.getInstance();
        container = findViewById(R.id.schedule_modes_container);
        init();

        /*
         * Настраиваем кнопки выхода
         */
        Button btnSave = findViewById(R.id.btnSaveDescs);
        btnSave.setOnClickListener(v -> {
            save();
            finish();
        });

        Button btnCancel = findViewById(R.id.btnCancelDesc);
        btnCancel.setOnClickListener(v -> {
            config.load();
            finish();
        });
    }

    // Подключаем меню в Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_add_schedule, menu);
        return true;
    }

    // Обрабатываем нажатие на кнопку "+"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showScheduleSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Всплывающее меню.
     * Добавляет новый график (из предложенных типов)
     */
    private void showScheduleSelectionDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_work_schedule, null);
        bottomSheetDialog.setContentView(view);

        // Обработчики нажатий
        view.findViewById(R.id.menuItem1).setOnClickListener(v -> {
            handleMenuSelection(WorkType.DAY_TWO_FREE);
            bottomSheetDialog.dismiss();
        });
        view.findViewById(R.id.menuItem2).setOnClickListener(v -> {
            handleMenuSelection(WorkType.DAY_THREE_FREE);
            bottomSheetDialog.dismiss();
        });
        view.findViewById(R.id.menuItem3).setOnClickListener(v -> {
            handleMenuSelection(WorkType.THREE_DAY_THREE_NIGHT_THREE_FREE);
            bottomSheetDialog.dismiss();
        });
        view.findViewById(R.id.menuItem4).setOnClickListener(v -> {
            handleMenuSelection(WorkType.TWO_DAY_TWO_NIGHT_TWO_FREE);
            bottomSheetDialog.dismiss();
        });
        view.findViewById(R.id.menuItem5).setOnClickListener(v -> {
            handleMenuSelection(WorkType.WEEK_DAY_WEEK_NIGHT);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void handleMenuSelection(WorkType type) {
        UUID id = UUID.randomUUID();
        CalendarUnit unit = new CalendarUnit(id, type, 0, type.getDescription());
        workSchedules.put(id, unit);
        addModeCard(unit);
    }

    /*
        Создаем и заполняем поля для существующих графиков работы
     */
    private void init() {
        workSchedules = new HashMap<>();
        editControls = new HashMap<>();
        toBackup();
        // создаем Card's
        config.forEach(this::addModeCard);
    }

    /*
        Делает копию текущих настроек.
     */
    private void toBackup() {
        config.load();
        config.forEach(e -> workSchedules.put(e.getId(), e));
    }

    /*
        Применяет новые настройки.
     */
    private void save() {
        editControls.forEach((key, value) -> setName(key, value.getText().toString()));
        workSchedules.forEach((key, value) -> config.setSettingUnit(value));
        config.save();
    }


    private void setName(UUID uuid, String name) {
        CalendarUnit unit = workSchedules.get(uuid);
        if (unit != null) {
            unit.setName(name);
        }
    }

    private String getName(UUID uuid) {
        CalendarUnit unit = workSchedules.get(uuid);
        if (unit != null)
            return unit.getName();
        return "Unknown";
    }


    /**
     * Создает новый объект CardView, с выбранным типом графика дежурств.
     * @param unit Данные о графике дежурств.
     */
    private void addModeCard(CalendarUnit unit) {
        UUID id = unit.getId();
        WorkType type = unit.getType();

        // Создаём CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(8f);
        cardView.setCardElevation(4f);
        cardView.setUseCompatPadding(true);

        // Внутренний контейнер
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);
        cardView.addView(layout);

        // Верхний контейнер (заголовок + кнопка удаления)
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setGravity(Gravity.CENTER_VERTICAL);
        // Заголовок
        TextView textView = new TextView(this);
        textView.setText(type.getDescription());
        textView.setTextSize(16f);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        headerLayout.addView(textView);

        // Кнопка удаления (иконка корзины)
        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.sharp_delete_forever_24);
        deleteButton.setBackground(null);
        deleteButton.setPadding(16, 16, 16, 16);
        // ставим обработчик нажатия на значок удаления графика
        deleteButton.setOnClickListener(v -> {
            container.removeView(cardView);
            workSchedules.remove(id);
            editControls.remove(id);
            config.removeSettingUnit(id);
        });
        headerLayout.addView(deleteButton);

        // Поле ввода пользовательского названия для графика дежурств
        EditText editText = new EditText(this);
        editText.setId(View.generateViewId());
        editText.setText(getName(id));
        editText.setHint("Введите название графика");
        editText.setSingleLine(true);

        // добавляем контролы в CardView
        layout.addView(headerLayout);
        layout.addView(editText);

        // Добавляем CardView в контейнер
        container.addView(cardView);

        editControls.put(id, editText);
    }

}
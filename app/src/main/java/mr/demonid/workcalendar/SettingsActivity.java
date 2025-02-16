package mr.demonid.workcalendar;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.config.ColorSettings;
import mr.demonid.workcalendar.views.PaintStore;


/**
 * Экран настроек приложения.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*
            Настраиваем кнопки
         */
        Button back = findViewById(R.id.btn_back_to_main);
        back.setOnClickListener(v -> finish());

        Button settingDesc = findViewById(R.id.btn_chart_names);
        settingDesc.setOnClickListener(v -> doActivity(SettingSchedulesOnOffActivity.class));

        Button settingMode = findViewById(R.id.btn_chart_settings);
        settingMode.setOnClickListener(v -> doActivity(SettingSchedulesOffsetsActivity.class));

        Button settingCalendarColor = findViewById(R.id.btn_chart_colors_calendar);
        settingCalendarColor.setOnClickListener(v -> doActivity(SettingsCellColorActivity.class));

        Button settingStyleCell = findViewById(R.id.btn_chart_cell_type);
        settingStyleCell.setOnClickListener(v -> doActivity(SettingsCellStyleActivity.class));

        Button resetAll = findViewById(R.id.btn_chart_reset);
        resetAll.setOnClickListener(v -> doReset());
    }


    private void doActivity(Class<?> clazz) {
        Intent intent = new Intent(SettingsActivity.this, clazz);
        startActivity(intent);
    }

    private void doReset() {
        new AlertDialog.Builder(this)
                .setTitle("Сброс настроек")
                .setMessage("Вы действительно хотите сбросить настройки?")
                .setPositiveButton("Да", (dialog, which) -> {
                    // Очистка настроек
                    AppSettings appSettings = AppSettings.getInstance();
                    appSettings.reset();
                    ColorSettings colorSettings = ColorSettings.getInstance();
                    colorSettings.reset();
                    PaintStore.getInstance().load();

                    Toast.makeText(this, "Настройки сброшены", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                .show();
    }

}

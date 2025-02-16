package mr.demonid.workcalendar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mr.demonid.workcalendar.config.AppSettings;
import mr.demonid.workcalendar.types.CellRendererStyle;
import mr.demonid.workcalendar.views.StylePreviewCell;


/**
 * Выбор стиля отображения рабочих дней в ячейках календаря.
 */
public class SettingsCellStyleActivity extends AppCompatActivity {
    private CellRendererStyle selectStyle;

    private RadioButton buttonCircle;
    private RadioButton buttonFillCircle;
    private RadioButton buttonFillRect;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_cells_style);

        AppSettings config = AppSettings.getInstance();
        selectStyle = config.getDayStyle();

        buttonCircle = initGroupControl(R.id.styleRadioButton1, R.id.stylePreview1, CellRendererStyle.CIRCLE);
        buttonFillCircle = initGroupControl(R.id.styleRadioButton2, R.id.stylePreview2, CellRendererStyle.FILL_CIRCLE);
        buttonFillRect = initGroupControl(R.id.styleRadioButton3, R.id.stylePreview3, CellRendererStyle.FILL_RECT);

        updateRadioButtons(selectStyle);

        /*
            Настраиваем кнопки выхода
         */
        Button save = findViewById(R.id.btn_style_save);
        save.setOnClickListener(v -> {
            config.setDayStyle(selectStyle);
            config.save();
            finish();
        });

        Button cancel = findViewById(R.id.btn_style_cancel);
        cancel.setOnClickListener(v -> finish());
    }

    /*
     * Обработчик нажатий на радио-кнопки
     */
    View.OnClickListener optionClickListener = v -> updateRadioButtons((CellRendererStyle) v.getTag());

    /*
     * Ручная перерисовка всех радиокнопок
     */
    private void updateRadioButtons(CellRendererStyle style) {
        buttonCircle.setChecked(style == CellRendererStyle.CIRCLE);
        buttonFillCircle.setChecked(style == CellRendererStyle.FILL_CIRCLE);
        buttonFillRect.setChecked(style == CellRendererStyle.FILL_RECT);
        selectStyle = style;
    }

    /*
     * Настройка радиокнопки
     */
    private RadioButton initGroupControl(int id, int previewId, CellRendererStyle style) {
        ((StylePreviewCell) findViewById(previewId)).setRendererStyle(style);
        RadioButton btn = findViewById(id);
        btn.setTag(style);
        btn.setOnClickListener(optionClickListener);
        return btn;
    }

}

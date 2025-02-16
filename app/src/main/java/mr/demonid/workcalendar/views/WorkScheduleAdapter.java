package mr.demonid.workcalendar.views;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import mr.demonid.workcalendar.R;
import mr.demonid.workcalendar.config.CalendarUnit;
import mr.demonid.workcalendar.events.OnThemeSelectedListener;


/**
 * По сути динамический RadioGroup для выбора графика дежурств.
 */
public class WorkScheduleAdapter extends RecyclerView.Adapter<WorkScheduleAdapter.ThemeViewHolder> {

    private final List<CalendarUnit> workSchedules;
    private int selectedPosition = -1;
    private final OnThemeSelectedListener listener;


    public WorkScheduleAdapter(List<CalendarUnit> workSchedules, OnThemeSelectedListener listener) {
        this.workSchedules = workSchedules;
        this.listener = listener;
    }

    /**
     * Обновляет список графиков дежурств.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateScheduleList(List<CalendarUnit> newThemes) {
        this.workSchedules.clear();
        this.workSchedules.addAll(newThemes);
        notifyDataSetChanged();             // перерисовываем список
    }

    /**
     * Задает выбранный переключаталь.
     */
    public void setSelectedPosition(UUID id) {
        int pos = IntStream.range(0, workSchedules.size())
                .filter(i -> workSchedules.get(i).getId().equals(id))
                .findFirst()
                .orElse(-1);
        if (pos >= 0) {
            selectedPosition = pos;
//            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        if (workSchedules.isEmpty() || position < 0 || position >= workSchedules.size())
            return;

        CalendarUnit theme = workSchedules.get(position);
        holder.radioButton.setText(theme.getName());

        // Устанавливаем состояние кнопки
        holder.radioButton.setChecked(position == selectedPosition);

        // Обработчик выбора
        holder.radioButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();

            if (adapterPosition != RecyclerView.NO_POSITION) {
                int previousSelected = selectedPosition;
                selectedPosition = adapterPosition;
                // Обновляем только изменившиеся элементы
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                // Отсылаем уведомление об изменении состояния
                if (listener != null) {
                    listener.onThemeSelected(theme);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return workSchedules.size();
    }


    public static class ThemeViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButtonTheme);
        }
    }
}

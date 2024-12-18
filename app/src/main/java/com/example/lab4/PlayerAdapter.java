package com.example.lab4;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private List<Player> playerList; // Список игроков для отображения
    private List<Player> selectedPlayers = new ArrayList<>(); // Список выбранных игроков

    // Конструктор адаптера, принимает список игроков
    public PlayerAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Создаем новый ViewHolder, на основе макета элемента списка
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Получаем игрока по текущей позиции в списке
        Player player = playerList.get(position);
        // Устанавливаем имя игрока в TextView
        holder.nameTextView.setText(player.getName());

        // Проверяем, выбран ли игрок, и меняем фон в зависимости от состояния
        holder.itemView.setBackgroundColor(player.isSelected() ? Color.LTGRAY : Color.TRANSPARENT);

        // Обработчик клика по элементу списка
        holder.itemView.setOnClickListener(v -> {
            player.setSelected(!player.isSelected()); // Переключаем состояние выбранности игрока
            notifyDataSetChanged(); // Уведомляем адаптер об изменениях данных
        });
    }

    @Override
    public int getItemCount() {
        // Возвращаем количество игроков в списке
        return playerList.size();
    }

    // Метод для получения списка выбранных игроков
    public List<Player> getSelectedPlayers() {
        selectedPlayers.clear(); // Очищаем текущий список выбранных игроков
        for (Player player : playerList) {
            // Проверяем каждого игрока и добавляем в список, если выбран
            if (player.isSelected()) {
                selectedPlayers.add(player);
            }
        }
        return selectedPlayers; // Возвращаем список выбранных игроков
    }

    // Класс ViewHolder для управления представлением одного элемента списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView; // Текстовое поле для отображения имени игрока

        public ViewHolder(View itemView) {
            super(itemView);
            // Находим TextView в макете элемента списка
            nameTextView = itemView.findViewById(R.id.player_name);
        }
    }
}

package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private List<Player> playerList = new ArrayList<>();
    private SharedPreferences preferences;
    private Button buttonEdit, buttonSaveEdit; // Ссылки на кнопки
    private EditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим RecyclerView из макета и инициализируем адаптер
        recyclerView = findViewById(R.id.recyclerView);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        adapter = new PlayerAdapter(playerList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Находим элементы интерфейса
        editTextName = findViewById(R.id.editTextName);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonSaveEdit = findViewById(R.id.buttonSaveEdit);

        buttonSaveEdit.setVisibility(View.GONE); // Кнопка сохранения изначально скрыта
        loadPlayers(); // Загружаем сохранённых игроков из SharedPreferences

        // Устанавливаем слушателей на кнопки
        findViewById(R.id.buttonAdd).setOnClickListener(v -> addPlayer());
        buttonEdit.setOnClickListener(v -> editPlayer());
        buttonSaveEdit.setOnClickListener(v -> saveEdit());
        findViewById(R.id.buttonDelete).setOnClickListener(v -> deletePlayer());
        findViewById(R.id.buttonDraw).setOnClickListener(v -> drawTeams());
    }

    // Метод для добавления нового игрока
    private void addPlayer() {
        String name = editTextName.getText().toString().trim();
        if (!name.isEmpty()) {
            playerList.add(new Player(name)); // Добавляем игрока в список
            adapter.notifyDataSetChanged(); // Уведомляем адаптер об изменениях
            savePlayers(); // Сохраняем игроков в SharedPreferences
            editTextName.setText(""); // Очищаем поле ввода
        }
    }

    // Метод для удаления выбранного игрока
    private void deletePlayer() {
        List<Player> selectedPlayers = adapter.getSelectedPlayers();
        if (selectedPlayers.size() != 1) {
            Toast.makeText(this, "Выберите одного участника для удаления", Toast.LENGTH_SHORT).show();
        } else {
            playerList.remove(selectedPlayers.get(0)); // Удаляем игрока
            adapter.notifyDataSetChanged();
            savePlayers(); // Сохраняем изменения
        }
    }

    // Метод для начала редактирования выбранного игрока
    private void editPlayer() {
        List<Player> selectedPlayers = adapter.getSelectedPlayers();
        if (selectedPlayers.size() != 1) {
            Toast.makeText(this, "Выберите одного участника для редактирования", Toast.LENGTH_SHORT).show();
            return;
        }

        editTextName.setText(selectedPlayers.get(0).getName()); // Заполняем поле ввода именем выбранного игрока
        buttonEdit.setVisibility(View.GONE); // Скрываем кнопку редактирования
        buttonSaveEdit.setVisibility(View.VISIBLE); // Показываем кнопку сохранения
    }

    // Метод для сохранения изменений в имени игрока
    private void saveEdit() {
        String newName = editTextName.getText().toString().trim();
        if (!newName.isEmpty()) {
            List<Player> selectedPlayers = adapter.getSelectedPlayers();
            if (selectedPlayers.size() == 1) {
                selectedPlayers.get(0).setName(newName); // Обновляем имя игрока
                adapter.notifyDataSetChanged(); // Обновляем интерфейс
                savePlayers(); // Сохраняем изменения
                Toast.makeText(this, "Имя игрока обновлено", Toast.LENGTH_SHORT).show();

                buttonSaveEdit.setVisibility(View.GONE);
                buttonEdit.setVisibility(View.VISIBLE);
                editTextName.clearFocus(); // Снимаем фокус с поля
                editTextName.setText(""); // Очищаем поле ввода
            }
        } else {
            Toast.makeText(this, "Имя не должно быть пустым", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для случайного разделения игроков на две команды
    private void drawTeams() {
        List<Player> selectedPlayers = adapter.getSelectedPlayers();
        if (selectedPlayers.size() < 2) {
            Toast.makeText(this, "Нужно выбрать хотя бы 2 участников для розыгрыша", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Player> shuffledPlayers = new ArrayList<>(selectedPlayers);
        Collections.shuffle(shuffledPlayers); // Перемешиваем игроков

        List<Player> redTeam = new ArrayList<>();
        List<Player> greenTeam = new ArrayList<>();

        for (int i = 0; i < shuffledPlayers.size(); i++) {
            if (i % 2 == 0) {
                redTeam.add(shuffledPlayers.get(i));
            } else {
                greenTeam.add(shuffledPlayers.get(i));
            }
        }

        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        intent.putParcelableArrayListExtra("redTeam", (ArrayList<? extends Parcelable>) redTeam);
        intent.putParcelableArrayListExtra("greenTeam", (ArrayList<? extends Parcelable>) greenTeam);
        startActivity(intent);
    }

    // Метод для сохранения списка игроков в SharedPreferences
    private void savePlayers() {
        SharedPreferences.Editor editor = preferences.edit();
        StringBuilder sb = new StringBuilder();
        for (Player player : playerList) {
            sb.append(player.getName()).append(","); // Преобразуем список игроков в строку
        }
        editor.putString("players", sb.toString());
        editor.apply();
    }

    // Метод для загрузки списка игроков из SharedPreferences
    private void loadPlayers() {
        String savedPlayers = preferences.getString("players", "");
        if (!savedPlayers.isEmpty()) {
            String[] names = savedPlayers.split(",");
            for (String name : names) {
                if (!name.isEmpty()) {
                    playerList.add(new Player(name)); // Восстанавливаем игроков
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
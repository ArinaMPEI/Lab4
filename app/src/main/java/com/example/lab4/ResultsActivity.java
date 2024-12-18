package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView redRecyclerView, greenRecyclerView; // Ссылки на RecyclerView для красной и зеленой команды
    private PlayerAdapter redAdapter, greenAdapter; // Адаптеры для отображения данных игроков в списках

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results); // Устанавливаем макет активности

        // Находим RecyclerView из макета
        redRecyclerView = findViewById(R.id.recyclerViewRed);
        greenRecyclerView = findViewById(R.id.recyclerViewGreen);

        // Получаем данные команд из переданных данных (Intent)
        List<Player> redTeam = getIntent().getParcelableArrayListExtra("redTeam");
        List<Player> greenTeam = getIntent().getParcelableArrayListExtra("greenTeam");

        // Инициализируем адаптеры для каждой команды
        redAdapter = new PlayerAdapter(redTeam);
        greenAdapter = new PlayerAdapter(greenTeam);

        // Устанавливаем адаптеры и менеджеры макетов для RecyclerView
        redRecyclerView.setAdapter(redAdapter);
        redRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        greenRecyclerView.setAdapter(greenAdapter);
        greenRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

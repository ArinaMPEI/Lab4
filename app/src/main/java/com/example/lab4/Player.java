package com.example.lab4;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
    private String name; // Имя игрока
    private boolean isSelected = false; // Статус выбранности игрока

    // Конструктор для создания нового игрока
    public Player(String name) {
        this.name = name;
    }

    // Конструктор для восстановления данных игрока из Parcel
    protected Player(Parcel in) {
        name = in.readString(); // Восстанавливаем имя из Parcel
    }

    // Метод для получения имени игрока
    public String getName() {
        return name;
    }

    // Метод для установки нового имени игрока
    public void setName(String name) {
        this.name = name; // Устанавливаем новое имя
    }

    // Метод для проверки статуса выбранности игрока
    public boolean isSelected() {
        return isSelected;
    }

    // Метод для установки статуса выбранности игрока
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    // Используется системой Android, чтобы преобразовать объект Player в последовательность байтов для передачи.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name); // Записываем имя игрока в Parcel
    }

    @Override
    public int describeContents() {
        return 0; // Возвращаем 0, так как в данном случае не используется специальный тип дескриптора
    }

    // Создаем объект CREATOR для поддержки Parcelable интерфейса
    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in); // Восстанавливаем игрока из Parcel
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size]; // Создаем массив игроков
        }
    };
}

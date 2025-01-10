package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.dao.ExcursionDAO;
import com.example.myapplication.dao.VacationDAO;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

@TypeConverters({Converters.class})
@Database(entities = {Vacation.class, Excursion.class}, version = 11, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
  public abstract VacationDAO vacationDAO();

  public abstract ExcursionDAO excursionDAO();

  private static volatile VacationDatabaseBuilder INSTANCE;

  static VacationDatabaseBuilder getDataBase(final Context context) {
    if (INSTANCE == null) {
      synchronized (VacationDatabaseBuilder.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                  VacationDatabaseBuilder.class, "MyVacationDatabase.db")
              .fallbackToDestructiveMigration()
              .allowMainThreadQueries()
              .build();
        }
      }
    }
    return INSTANCE;
  }
}

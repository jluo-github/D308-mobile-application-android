package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
  private Repository repository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vacation_list);
    FloatingActionButton fab = findViewById(R.id.floatingActionButton);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(VacationList.this, VacationDetails.class);
        startActivity(intent);
      }
    });

    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    repository = new Repository(getApplication());
    List<Vacation> allVacation = repository.getmAllVacations();
    final VacationAdapter productAdapter = new VacationAdapter(this);
    recyclerView.setAdapter(productAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    productAdapter.setVacations(allVacation);

  }// end on create

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      this.finish();
      return true;
    }

    if (id == R.id.addVacation) {
      Intent intent = new Intent(VacationList.this, VacationDetails.class);
      startActivity(intent);
    }


//    if (id == R.id.sample) {
//      repository = new Repository(getApplication());
////      Toast.makeText(VacationList.this, "put in sample code here", Toast.LENGTH_SHORT).show();
//      Vacation vacation = new Vacation(0, "title1", "hotel1");
//      repository.insert(vacation);
//      vacation = new Vacation(0, "title2", "hotel2");
//      repository.insert(vacation);
//
//      Excursion excursion = new Excursion(0, "excurison1", 1);
//      repository.insert(excursion);
//      excursion = new Excursion(0, "excurison2", 1);
//      repository.insert(excursion);
//
//      return true;
//    }

    return super.onOptionsItemSelected(item);
  } // end on options item selected

  @Override
  protected void onResume() {

    super.onResume();
    List<Vacation> allVacation = repository.getmAllVacations();
    RecyclerView recyclerView = findViewById(R.id.recyclerView);

    final VacationAdapter vacationAdapter = new VacationAdapter(this);
    recyclerView.setAdapter(vacationAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    vacationAdapter.setVacations(allVacation);

  } // end on resume
} // end class
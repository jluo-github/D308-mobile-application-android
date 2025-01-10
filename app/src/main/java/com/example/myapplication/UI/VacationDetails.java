package com.example.myapplication.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
  String title;
  String hotel;
  int vacationID;
  EditText editTitle;
  EditText editHotel;
  Repository repository;
  Vacation currentVacation;
  TextView editStartDate;
  TextView editEndDate;
  DatePickerDialog.OnDateSetListener startDate;
  Calendar myCalendarStart = Calendar.getInstance();
  DatePickerDialog.OnDateSetListener endDate;
  Calendar myCalendarEnd = Calendar.getInstance();
  int numExcursions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vacation_details);

    editTitle = findViewById(R.id.titleText);
    editHotel = findViewById(R.id.hotelText);

    vacationID = getIntent().getIntExtra("id", -1);
    title = getIntent().getStringExtra("title");
    hotel = getIntent().getStringExtra("hotel");

    editTitle.setText(title);
    editHotel.setText(hotel);

    editStartDate = findViewById(R.id.startDate);
    editEndDate = findViewById(R.id.endDate);

    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    long startDateMillis = getIntent().getLongExtra("startDate", 0); // Default value if not found
    long endDateMillis = getIntent().getLongExtra("endDate", 0); // Default value if not found

    if (startDateMillis != 0) {
      Date startDate = new Date(startDateMillis);
      SimpleDateFormat startDateSdf = new SimpleDateFormat(myFormat, Locale.US);
      String startDateStr = startDateSdf.format(startDate);
      editStartDate.setText(startDateStr);
    }

    if (endDateMillis != 0) {
      Date endDate = new Date(endDateMillis);
      SimpleDateFormat endDateSdf = new SimpleDateFormat(myFormat, Locale.US);
      String endDateStr = endDateSdf.format(endDate);
      editEndDate.setText(endDateStr);
    }


    startDate = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        myCalendarStart.set(Calendar.YEAR, i);
        myCalendarStart.set(Calendar.MONTH, i1);
        myCalendarStart.set(Calendar.DAY_OF_MONTH, i2);
        updateLabelStart();
      }
    };//end start date

    editStartDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DatePickerDialog datePickerDialog;

        Date date;
        String info = editStartDate.getText().toString();
        myCalendarStart = Calendar.getInstance();

        if (info.equals("")) {
          //          info = "10/01/23";
          datePickerDialog = new DatePickerDialog(
              VacationDetails.this,
              startDate,
              myCalendarStart.get(Calendar.YEAR),
              myCalendarStart.get(Calendar.MONTH),
              myCalendarStart.get(Calendar.DAY_OF_MONTH)
          );
        } else {
          try {
            Date currentDate = sdf.parse(info);
            myCalendarStart.setTime(currentDate);
          } catch (ParseException e) {
            e.printStackTrace();
          }
          datePickerDialog = new DatePickerDialog(
              VacationDetails.this,
              startDate,
              myCalendarStart.get(Calendar.YEAR),
              myCalendarStart.get(Calendar.MONTH),
              myCalendarStart.get(Calendar.DAY_OF_MONTH)
          );

        }
        datePickerDialog.show();

      }// end on click

    });//end edit start date

    endDate = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        myCalendarEnd.set(Calendar.YEAR, i);
        myCalendarEnd.set(Calendar.MONTH, i1);
        myCalendarEnd.set(Calendar.DAY_OF_MONTH, i2);
        updateLabelEnd();
      }
    };//end start date

    editEndDate.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        DatePickerDialog datePickerDialog;

        Date date;
        String info = editEndDate.getText().toString();
        myCalendarEnd = Calendar.getInstance();

        if (info.equals("")) {

//          info = "10/01/23";
          datePickerDialog = new DatePickerDialog(
              VacationDetails.this,
              endDate,
              myCalendarEnd.get(Calendar.YEAR),
              myCalendarEnd.get(Calendar.MONTH),
              myCalendarEnd.get(Calendar.DAY_OF_MONTH)

          );
        } else {
          try {
            Date currentDate = sdf.parse(info);

            myCalendarEnd.setTime(currentDate);
          } catch (ParseException e) {
            e.printStackTrace();
          }
          datePickerDialog = new DatePickerDialog(
              VacationDetails.this,
              endDate,
              myCalendarEnd.get(Calendar.YEAR),
              myCalendarEnd.get(Calendar.MONTH),
              myCalendarEnd.get(Calendar.DAY_OF_MONTH)
          );
        }
        datePickerDialog.show();
      } // end onClick
    }); // end editEndDate


    FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
        intent.putExtra("vacationID", vacationID);
        startActivity(intent);
      }
    });

    RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
    repository = new Repository(getApplication());

    final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
    recyclerView.setAdapter(excursionAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    List<Excursion> filteredExcursions = new ArrayList<>();
    for (Excursion excursion : repository.getmAllExcursions()) {
      if (excursion.getVacationID() == vacationID) {
        filteredExcursions.add(excursion);
      }
    } //end for
    excursionAdapter.setExcursions(filteredExcursions);


  } // end onCreate

  private void updateLabelStart() {
    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    editStartDate.setText(sdf.format(myCalendarStart.getTime()));
  }

  private void updateLabelEnd() {
    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
  }


  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
    return true;
  }


  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    if (id == android.R.id.home) {
      this.finish();
      return true;
    }

    if (id == R.id.addExcursions) {
      Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
      intent.putExtra("vacationID", vacationID);
      startActivity(intent);
    }


    if (id == R.id.vacationSave) {

      Vacation vacation;
      String startDateString = editStartDate.getText().toString();
      String endDateString = editEndDate.getText().toString();
      SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

      if (!isDateValid(startDateString, "MM/dd/yy")) {
        // Display an error message for start date
        Toast.makeText(
            VacationDetails.this, "Please use MM/dd/yy", Toast.LENGTH_LONG).show();
        return true; // Prevent saving
      }

      if (!isDateValid(endDateString, "MM/dd/yy")) {
        // Display an error message for end date
        Toast.makeText(
            VacationDetails.this, "Please use MM/dd/yy", Toast.LENGTH_LONG).show();
        return true; // Prevent saving
      }


      Date startDate = myCalendarStart.getTime();
      Date endDate = myCalendarEnd.getTime();

      // Calendar for start date and vacation start date
      Calendar calendarStartDate = Calendar.getInstance();
      calendarStartDate.setTime(startDate);

      Calendar calendarEndDate = Calendar.getInstance();
      calendarEndDate.setTime(endDate);

      // Set time components to zero
      calendarStartDate.set(Calendar.HOUR_OF_DAY, 0);
      calendarStartDate.set(Calendar.MINUTE, 0);
      calendarStartDate.set(Calendar.SECOND, 0);
      calendarStartDate.set(Calendar.MILLISECOND, 0);

      calendarEndDate.set(Calendar.HOUR_OF_DAY, 0);
      calendarEndDate.set(Calendar.MINUTE, 0);
      calendarEndDate.set(Calendar.SECOND, 0);
      calendarEndDate.set(Calendar.MILLISECOND, 0);

      if (calendarStartDate.after(calendarEndDate)) {
        Toast.makeText(
            VacationDetails.this, "End date must be after the start date!", Toast.LENGTH_LONG).show();
        return true;
      }

      if (vacationID == -1) {
        if (repository.getmAllVacations().size() == 0) {
          vacationID = 1;
        } else {
          vacationID = repository.getmAllVacations()
              .get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
        }
        if (editTitle.getText().toString().equals("")) {
          Toast.makeText(
              VacationDetails.this, "Please enter a title", Toast.LENGTH_LONG).show();
          return true;
        } else if (editHotel.getText().toString().equals("")) {
          Toast.makeText(
              VacationDetails.this, "Please enter a hotel", Toast.LENGTH_LONG).show();
          return true;
        } else {
          vacation = new Vacation(vacationID, editTitle.getText().toString(),
              editHotel.getText().toString(),
              myCalendarStart.getTime(), myCalendarEnd.getTime());
          repository.insert(vacation);
        }

      } else {
        try {
          if (editTitle.getText().toString().equals("")) {
            Toast.makeText(
                VacationDetails.this, "Please enter a title", Toast.LENGTH_LONG).show();
            return true;
          } else if (editHotel.getText().toString().equals("")) {
            Toast.makeText(
                VacationDetails.this, "Please enter a hotel", Toast.LENGTH_LONG).show();
            return true;
          } else {
            vacation = new Vacation(vacationID, editTitle.getText().toString(),
                editHotel.getText().toString(),
                myCalendarStart.getTime(), myCalendarEnd.getTime());
            repository.update(vacation);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }//end vacationID else
      Toast.makeText(
          VacationDetails.this, "Saved", Toast.LENGTH_LONG).show();
      return true;
    } // end if save

    if (id == R.id.vacationDelete) {
      for (Vacation vac : repository.getmAllVacations()) {
        if (vac.getVacationID() == vacationID) {
          currentVacation = vac;
        }
      }
      numExcursions = 0;
      for (Excursion exc : repository.getmAllExcursions()) {
        if (exc.getVacationID() == vacationID) ++numExcursions;
      }

      if (numExcursions == 0) {
        repository.delete(currentVacation);
        Toast.makeText(
            VacationDetails.this, currentVacation.getTitle() + " Vacation Deleted",
            Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(
            VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
      }
      Toast.makeText(
          VacationDetails.this, currentVacation.getTitle() + " Vacation Deleted",
          Toast.LENGTH_LONG).show();
      return true;

    } //end if delete

    if (id == R.id.share) {
      String shareText = "Title: " + editTitle.getText().toString() + "\n"
          + "Hotel: " + editHotel.getText().toString() + "\n"
          + "Start Date: " + editStartDate.getText().toString() + "\n"
          + "End Date: " + editEndDate.getText().toString();

      Intent sendIntent = new Intent();
      sendIntent.setAction(Intent.ACTION_SEND);
      sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
      sendIntent.putExtra(Intent.EXTRA_TITLE, "My Vacation Details");
      sendIntent.setType("text/plain");
      Intent shareIntent = Intent.createChooser(sendIntent, null);
      startActivity(shareIntent);
      return true;
    } // end if share

    if (id == R.id.notify) {
      String startDateScreen = editStartDate.getText().toString();
      String endDateScreen = editEndDate.getText().toString();
      String myFormat = "MM/dd/yy"; //In which you need put here
      SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
      Date myStartDate = null;
      Date myEndDate = null;

      try {
        myStartDate = sdf.parse(startDateScreen);
        myEndDate = sdf.parse(endDateScreen);

      } catch (ParseException e) {
        e.printStackTrace();
      }

      try {
        // Set up start date alert
        Long startTrigger = myStartDate.getTime();
        String startAlert = editTitle.getText().toString() + ": Vacation start date Alert!";
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", startAlert);

        PendingIntent startSender = PendingIntent.getBroadcast(
            VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        startAlarmManager.set(AlarmManager.RTC_WAKEUP, startTrigger, startSender);


        // Set up end date alert
        Long endTrigger = myEndDate.getTime();
        String endAlert = editTitle.getText().toString() + ": Vacation end date Alert!";
        Intent endIntent = new Intent(VacationDetails.this, MyReceiver.class);
        endIntent.putExtra("key", endAlert);

        PendingIntent endSender = PendingIntent.getBroadcast(
            VacationDetails.this, ++MainActivity.numAlert, endIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager endAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        endAlarmManager.set(AlarmManager.RTC_WAKEUP, endTrigger, endSender);


      } catch (Exception e) {
        e.printStackTrace();
      }


    }


    return super.onOptionsItemSelected(item);
  } // end onOptionsItemSelected

  private boolean isDateValid(String dateStr, String format) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

      dateFormat.setLenient(false);
      Date parsedDate = dateFormat.parse(dateStr);
      return parsedDate != null && dateFormat.format(parsedDate).equals(dateStr);

    } catch (ParseException e) {
      return false;
    }
  } // end isDateValid

  @Override
  protected void onResume() {

    super.onResume();

    RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
    final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
    recyclerView.setAdapter(excursionAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    List<Excursion> filteredExcursions = new ArrayList<>();
    for (Excursion excursion : repository.getmAllExcursions()) {
      if (excursion.getVacationID() == vacationID) {
        filteredExcursions.add(excursion);
      }
    } //end for
    excursionAdapter.setExcursions(filteredExcursions);
  } // end onResume

} // end class









package com.example.myapplication.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
  String name;
  int excursionID;
  int vacationID;
  EditText editName;
  EditText editNote;
  EditText editDate;
  Repository repository;
  Excursion currentExcursion;
  DatePickerDialog.OnDateSetListener startDate;
  Calendar myCalendarStart = Calendar.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_excursion_details);

    repository = new Repository(getApplication());

    editName = findViewById(R.id.excursionName);

    excursionID = getIntent().getIntExtra("id", -1);
    vacationID = getIntent().getIntExtra("vacationID", -1);
    name = getIntent().getStringExtra("name");
    editName.setText(name);

    editDate = findViewById(R.id.date);
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    long startDateMillis = getIntent().getLongExtra("date", 0); // Default value if not found

    if (startDateMillis != 0) {
      Date startDate = new Date(startDateMillis);
      SimpleDateFormat startDateSdf = new SimpleDateFormat(myFormat, Locale.US);
      String startDateStr = startDateSdf.format(startDate);
      editDate.setText(startDateStr);
    }


    ArrayList<Vacation> vacationArrayList = new ArrayList<>();
    vacationArrayList.addAll(repository.getmAllVacations());

    ArrayList<Integer> vacationIdList = new ArrayList<>();

    for (Vacation vacation : vacationArrayList) {
      vacationIdList.add(vacation.getVacationID());
    }

    startDate = new DatePickerDialog.OnDateSetListener() {

      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
        // TODO Auto-generated method stub

        myCalendarStart.set(Calendar.YEAR, year);
        myCalendarStart.set(Calendar.MONTH, monthOfYear);
        myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabelStart();
      }

    };//end of startDate

    editDate.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        DatePickerDialog datePickerDialog;

        Date date;
        String info = editDate.getText().toString();
        myCalendarStart = Calendar.getInstance();

        if (info.equals("")) {

//          info = "10/01/23";
          datePickerDialog = new DatePickerDialog(
              ExcursionDetails.this,
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
              ExcursionDetails.this,
              startDate,
              myCalendarStart.get(Calendar.YEAR),
              myCalendarStart.get(Calendar.MONTH),
              myCalendarStart.get(Calendar.DAY_OF_MONTH)

          );
        }
        datePickerDialog.show();
      }//end of onClick
    });//end of setOnClickListener

  } //end onCreate

  private void updateLabelStart() {
    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    editDate.setText(sdf.format(myCalendarStart.getTime()));
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      this.finish();
      return true;
    }

    if (item.getItemId() == R.id.excursionSave) {

      Excursion excursion;
      String startDateString = editDate.getText().toString();
      SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

      Vacation vacation = repository.getVacationByID(vacationID);

      // Check if there is no vacation
      if (vacation == null) {
        Toast.makeText(
            ExcursionDetails.this, "No vacation exists.", Toast.LENGTH_LONG).show();
        return true; // Prevent saving
      }

      if (!isDateValid(startDateString, "MM/dd/yy")) {
        // Display an error message for start date
        Toast.makeText(
            ExcursionDetails.this, "Please use MM/dd/yy", Toast.LENGTH_LONG).show();
        return true; // Prevent saving
      }


      Date startDate = myCalendarStart.getTime();

      Date vacationStartDate = repository.getVacationByID(vacationID).getStartDate();
      Date vacationEndDate = repository.getVacationByID(vacationID).getEndDate();

      Log.d("Debug", "startDate: " + startDate);
      Log.d("Debug", "VacationStartDate: " + vacationStartDate);
      Log.d("Debug", "VacationEndDate: " + vacationEndDate);

      // start date and vacation start date and vacation end date
      Calendar calendarStartDate = Calendar.getInstance();
      calendarStartDate.setTime(startDate);

      Calendar calendarVacationStartDate = Calendar.getInstance();
      calendarVacationStartDate.setTime(vacationStartDate);

      Calendar calendarVacationEndDate = Calendar.getInstance();
      calendarVacationEndDate.setTime(vacationEndDate);

      // Set time components to zero
      calendarStartDate.set(Calendar.HOUR_OF_DAY, 0);
      calendarStartDate.set(Calendar.MINUTE, 0);
      calendarStartDate.set(Calendar.SECOND, 0);
      calendarStartDate.set(Calendar.MILLISECOND, 0);

      calendarVacationStartDate.set(Calendar.HOUR_OF_DAY, 0);
      calendarVacationStartDate.set(Calendar.MINUTE, 0);
      calendarVacationStartDate.set(Calendar.SECOND, 0);
      calendarVacationStartDate.set(Calendar.MILLISECOND, 0);

      calendarVacationEndDate.set(Calendar.HOUR_OF_DAY, 0);
      calendarVacationEndDate.set(Calendar.MINUTE, 0);
      calendarVacationEndDate.set(Calendar.SECOND, 0);
      calendarVacationEndDate.set(Calendar.MILLISECOND, 0);

      if (calendarStartDate.before(calendarVacationStartDate) || calendarStartDate.after(calendarVacationEndDate)) {
        Toast.makeText(
            ExcursionDetails.this, "Please enter a date within the vacation date!", Toast.LENGTH_LONG).show();
        return true; // Prevent saving
      }

      if (excursionID == -1) {
        if (repository.getmAllExcursions().size() == 0) {
          excursionID = 1;
        } else {
          excursionID = repository.getmAllExcursions()
              .get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
        }
        if (editName.getText().toString().equals("")) {
          Toast.makeText(
              ExcursionDetails.this, "Please enter a name", Toast.LENGTH_LONG).show();
          return true;
        } else {
          excursion = new Excursion(excursionID, editName.getText().toString(),
              myCalendarStart.getTime(), vacationID);
          repository.insert(excursion);
        }

      } else {
        try {
          if (editName.getText().toString().equals("")) {
            Toast.makeText(
                ExcursionDetails.this, "Please enter a name", Toast.LENGTH_LONG).show();
            return true;
          } else {
            excursion = new Excursion(excursionID, editName.getText().toString(),
                myCalendarStart.getTime(), vacationID);
            repository.update(excursion);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } //end of if excursionID

      Toast.makeText(
          ExcursionDetails.this, "Saved", Toast.LENGTH_LONG).show();

      return true;
    } //end of if save

    if (item.getItemId() == R.id.excursionDelete) {

      for (Excursion excursion : repository.getmAllExcursions()) {
        if (excursion.getExcursionID() == excursionID) {
          currentExcursion = excursion;

          repository.delete(currentExcursion);
          Toast.makeText(
              ExcursionDetails.this, currentExcursion.getExcursionTitle() + " was deleted",
              Toast.LENGTH_LONG).show();
        }
      }
      Toast.makeText(
          ExcursionDetails.this, currentExcursion.getExcursionTitle() + " was deleted",
          Toast.LENGTH_LONG).show();
      return true;
    } //end of if delete


    if (item.getItemId() == R.id.notify) {
      String dateFromScreen = editDate.getText().toString();
      String myFormat = "MM/dd/yy"; //In which you need put here
      SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
      Date myDate = null;

      try {
        myDate = sdf.parse(dateFromScreen);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      try {
        Long trigger = myDate.getTime();
        Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);

        String alarmName = editName.getText().toString() + " is today!";
        intent.putExtra("key", alarmName);

        PendingIntent sender = PendingIntent.getBroadcast(
            ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

      } catch (Exception e) {
        e.printStackTrace();

      }
      return true;
    } //end of if notify

    return super.onOptionsItemSelected(item);
  }//end of onOptionsItemSelected

  private boolean isDateValid(String dateStr, String format) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
//      dateFormat.parse(dateStr);
//      return true;
      dateFormat.setLenient(false);
      Date parsedDate = dateFormat.parse(dateStr);
      return parsedDate != null && dateFormat.format(parsedDate).equals(dateStr);

    } catch (ParseException e) {
      return false;
    }
  } // end isDateValid


}//end of class



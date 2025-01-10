package com.example.myapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "vacations")
public class Vacation {

  @PrimaryKey(autoGenerate = true)
  private int vacationID;
  private String title;
  private String hotel;
  private Date startDate;
  private Date endDate;

  public Vacation() {
  }

  public Vacation(int vacationID, String title, String hotel) {
    this.vacationID = vacationID;
    this.title = title;
    this.hotel = hotel;
  }

  public Vacation(int vacationID, String title, String hotel, Date startDate, Date endDate) {
    this.vacationID = vacationID;
    this.title = title;
    this.hotel = hotel;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public int getVacationID() {
    return vacationID;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public void setVacationID(int vacationID) {
    this.vacationID = vacationID;
  }

  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public String getHotel() {
    return hotel;
  }

  public void setHotel(String hotel) {
    this.hotel = hotel;
  }

  //  public String toString() {
//    return title;
//  }
  @Override
  public String toString() {
    return "Vacation: " +
        "title: '" + title + '\'' +
        ", hotel: '" + hotel + '\'' +
        ", startDate: '" + startDate + '\'' +
        ", endDate: '" + endDate + '\'' +
        '.';
  }

}
//
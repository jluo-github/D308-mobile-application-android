package com.example.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
  private List<Vacation> mVacation;
  private final Context context;
  private final LayoutInflater mInflater;

  public VacationAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
    this.context = context;
  }

  public class VacationViewHolder extends RecyclerView.ViewHolder {
    private final TextView vacationItemView;

    public VacationViewHolder(@NonNull View itemView) {
      super(itemView);
      vacationItemView = itemView.findViewById(R.id.textView2);
      itemView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

          int position = getAdapterPosition();
          final Vacation current = mVacation.get(position);

          Intent intent = new Intent(context, VacationDetails.class);
          intent.putExtra("id", current.getVacationID());
          intent.putExtra("title", current.getTitle());
          intent.putExtra("hotel", current.getHotel());

          if (current.getStartDate() != null) {
            intent.putExtra("startDate", current.getStartDate().getTime());
          }
          if (current.getEndDate() != null) {
            intent.putExtra("endDate", current.getEndDate().getTime());
          }
          context.startActivity(intent);

        }
      });
    }
  } // end vacation view holder

  @NonNull
  @Override
  public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
    return new VacationViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
    if (mVacation != null) {
      Vacation current = mVacation.get(position);
      String name = current.getTitle();
      holder.vacationItemView.setText(name);
    } else {
      holder.vacationItemView.setText("No Vacation name!!!");
    }
  }

  @Override
  public int getItemCount() {
    if (mVacation != null)
      return mVacation.size();
    else
      return 0;
  }

  public void setVacations(List<Vacation> vacations) {
    mVacation = vacations;
    notifyDataSetChanged();
  }

}// end of class

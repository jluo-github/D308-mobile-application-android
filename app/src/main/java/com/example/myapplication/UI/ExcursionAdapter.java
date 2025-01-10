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
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
  private List<Excursion> mExcursions;
  private List<Vacation> mVacation;
  private final Context context;
  private final LayoutInflater mInflater;

  public ExcursionAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
    this.context = context;
  }

  public class ExcursionViewHolder extends RecyclerView.ViewHolder {
    private final TextView excursionItemView;
//    private final TextView excursionItemView2;


    public ExcursionViewHolder(@NonNull View itemView) {
      super(itemView);
      excursionItemView = itemView.findViewById(R.id.textView3);
//      excursionItemView2 = itemView.findViewById(R.id.textView4);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int position = getAdapterPosition();
          final Excursion current = mExcursions.get(position);

          Intent intent = new Intent(context, ExcursionDetails.class);
          intent.putExtra("id", current.getExcursionID());
          intent.putExtra("name", current.getExcursionTitle());
          intent.putExtra("vacationID", current.getVacationID());

          if (current.getDate() != null) {
            intent.putExtra("date", current.getDate().getTime());
          }

          context.startActivity(intent);
        }
      });
    }
  } // end excursion view holder

  @NonNull
  @Override
  public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
    return new ExcursionViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
    if (mExcursions != null) {
      Excursion current = mExcursions.get(position);
      String name = current.getExcursionTitle();
      int vacationID = current.getVacationID();
      holder.excursionItemView.setText(name);
//      holder.excursionItemView2.setText(Integer.toString(vacationID));
    } else {
      holder.excursionItemView.setText("No Excursion Name");
//      holder.excursionItemView2.setText("No Vacation ID");
    }
  } // end on bind view holder

  @Override
  public int getItemCount() {
    if (mExcursions != null)
      return mExcursions.size();
    else
      return 0;
  }

  public void setExcursions(List<Excursion> excursions) {
    mExcursions = excursions;
    notifyDataSetChanged();
  }

}//end of class
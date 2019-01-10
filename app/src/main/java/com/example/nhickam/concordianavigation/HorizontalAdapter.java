package com.example.nhickam.concordianavigation;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by nhickam on 5/31/2018.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private String[] daysOfWeek;
    private String[] cal_Day;
    private String[][] lunchContent;
    private Map<String, String>[][] lunchContent2;
    private String[] lunch;
    private String[] dinnerContent;


    public HorizontalAdapter(String[] daysOfWeek,String[] calDay,Map<String, String>[][] lunchContent2, String[] dinnerContent) {
        this.daysOfWeek = daysOfWeek;
        this.cal_Day = calDay;
        this.lunchContent2 = lunchContent2;
        this.dinnerContent = dinnerContent;
    }

    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_horizontal, viewGroup, false);
        return new HorizontalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalAdapter.ViewHolder holder, int position) {
        String day = daysOfWeek[position];
        String calDay = cal_Day[position];
        holder.day.setText(day);


        //ERROR: holder.theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lunchContent2[position]);
        holder.lunchArr.setAdapter(holder.theAdapter);

      //String lunch = lunchContent[position];
        String dinner = dinnerContent[position];
        holder.calDay.setText(calDay);
        holder.dinner.setText(dinner);
    }

    @Override
    public int getItemCount() {
        return daysOfWeek.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day;
        public TextView calDay;
        private TextView lunch;
        public ListAdapter theAdapter;
        public ListView lunchArr;
        public TextView dinner;

        ViewHolder(final View itemView) {
            super(itemView);
            this.day = (TextView) itemView.findViewById(R.id.content);
            this.calDay = (TextView) itemView.findViewById(R.id.calendarDay);
            this.lunchArr = (ListView) itemView.findViewById(R.id.lunch_array_text);
            this.lunchArr.setAdapter(theAdapter);
            this.dinner = (TextView) itemView.findViewById(R.id.dinner_text);
        }
    }
}
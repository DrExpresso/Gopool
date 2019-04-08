package firebase.gopool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import firebase.gopool.R;
import firebase.gopool.Utils.FirebaseMethods;
import firebase.gopool.models.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<Reminder> reminders;
    private FirebaseMethods mFirebaseMethods;

    public ReminderAdapter(Context c, ArrayList<Reminder> o, Activity a){
        this.mContext = c;
        this.mActivity = a;
        this.reminders = o;
        mFirebaseMethods = new FirebaseMethods(c);
    }

    public ReminderAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_notification, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String notificationText = reminders.get(position).getNotificationComment();
        final String date = String.valueOf(reminders.get(position).getDate());

        holder.date.setText(date);
        holder.notificationText.setText(notificationText);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseMethods.checkForReminder(holder.notificationText.getText().toString());

                mActivity.finish();
                mActivity.startActivity(mActivity.getIntent());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout view;
        TextView notificationText, date;
        ImageView deleteRide;

        public MyViewHolder(View itemView) {
            super(itemView);

            view = (LinearLayout) itemView.findViewById(R.id.view);
            notificationText = (TextView) itemView.findViewById(R.id.comment);
            date = (TextView) itemView.findViewById(R.id.date);
            deleteRide = (ImageView) itemView.findViewById(R.id.deleteNotificationBtn);
        }
    }




}
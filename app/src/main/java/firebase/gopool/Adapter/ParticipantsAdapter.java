package firebase.gopool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.gopool.R;
import firebase.gopool.dialogs.ViewRideCreatedDialog;
import firebase.gopool.models.Participants;
import firebase.gopool.models.Ride;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private ArrayList<Participants> participants;

    public ParticipantsAdapter(Context c, ArrayList<Participants> o){
        this.mContext = c;
        this.participants = o;
    }

    public ParticipantsAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ParticipantsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_participants, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String username = participants.get(position).getUsername();


        holder.username.setText(username);
        Picasso.get().load(participants.get(position).getUserProfilePhoto()).into(holder.profile_photo);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return participants.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout view;
        TextView username;
        ImageView profile_photo;

        public MyViewHolder(View itemView) {
            super(itemView);

            view = (LinearLayout) itemView.findViewById(R.id.view);
            username = (TextView) itemView.findViewById(R.id.user_id_2_username);
            profile_photo = (ImageView) itemView.findViewById(R.id.user_id_2);

        }
    }
    /**
     * parses date to a readable format
     * @param time
     * @return
     */
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yy";
        String outputPattern = "dd MMMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private String parseLocation(String location){
        if(location.contains(",")){
            location = location.replaceAll(",", "\n");
            location = location.replaceAll(" ", "");
            return location;
        } else {

        }

        return location;
    }
}
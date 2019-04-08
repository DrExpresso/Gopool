package firebase.gopool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import firebase.gopool.models.Ride;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private ArrayList<Ride> ride;

    public MyAdapter(Context c, ArrayList<Ride> o){
        this.mContext = c;
        this.ride = o;
    }

    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_ride_information, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String userID = ride.get(position).getUser_id();

        final String username = ride.get(position).getUsername();
        final String rides = String.valueOf(ride.get(position).getCompleteRides() + " Rides");
        final String seats = String.valueOf(ride.get(position).getSeatsAvailable() + " Seats Left!");
        String from = "From: " + ride.get(position).getCurrentLocation();
        String to = "To: " + ride.get(position).getDestination();
        final String date = parseDateToddMMyyyy(ride.get(position).getDateOfJourney()) + " - " + ride.get(position).getPickupTime() + " PM";
        final String cost = String.valueOf("£ " + ride.get(position).getCost()) + ".00";
        final Float rating = (float) ride.get(position).getUserRating();
        final String dateOnly = String.valueOf(ride.get(position).getPickupTime() + " PM");
        final String extraTime = String.valueOf(ride.get(position).getExtraTime() + " mins");
        final String fromOnly = parseLocation(ride.get(position).getCurrentLocation());
        final String toOnly = parseLocation(ride.get(position).getDestination());
        final String rideID = ride.get(position).getRideID();
        final String duration = ride.get(position).getDuration();
        final String completeRides = String.valueOf(ride.get(position).getCompleteRides());
        final String pickupLocation = ride.get(position).getPickupLocation();


        holder.rides.setText(rides);
        holder.seats.setText(seats);
        if (to.length() > 20){
            to = to.substring(0 , Math.min(to.length(), 19));
            to = to + "...";
            holder.to.setText(to);
        } else {
            holder.to.setText(to);
        }

        if (from.length() > 20){
            from = from.substring(0 , Math.min(from.length(), 19));
            from = from + "...";
            holder.from.setText(from);
        } else {
            holder.from.setText(from);
        }

        holder.date.setText(date);
        holder.costs.setText(cost);
        holder.ratingBar.setRating(rating);
        Picasso.get().load(ride.get(position).getProfile_picture()).into(holder.profile_photo);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewRideCreatedDialog dialog = new ViewRideCreatedDialog(mContext, rideID ,username, rides, seats, fromOnly, toOnly, date, cost, rating, dateOnly, extraTime, duration, completeRides, pickupLocation ,userID);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ride.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout view;
        TextView rides, from, to, date, seats, costs;
        CircleImageView profile_photo;
        RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            view = (LinearLayout) itemView.findViewById(R.id.view);
            rides = (TextView) itemView.findViewById(R.id.indivcompletedRidesTxt);
            from = (TextView) itemView.findViewById(R.id.fromTxt);
            to = (TextView) itemView.findViewById(R.id.toTxt);
            date = (TextView) itemView.findViewById(R.id.individualTimeTxt);
            seats = (TextView) itemView.findViewById(R.id.seatsTxt);
            costs = (TextView) itemView.findViewById(R.id.priceTxt);

            ratingBar = (RatingBar) itemView.findViewById(R.id.individualRatingBar);

            profile_photo = (CircleImageView) itemView.findViewById(R.id.indiviual_profile_picture);

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
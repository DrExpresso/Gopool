package firebase.gopool.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.gopool.Booked.BookedActivity;
import firebase.gopool.Pickup.PickupActivity;
import firebase.gopool.R;
import firebase.gopool.dialogs.BookRideDialog;
import firebase.gopool.models.BookingResults;
import firebase.gopool.models.Request;
import firebase.gopool.models.Ride;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private ArrayList<BookingResults> ride;

    public BookingAdapter(Context c, ArrayList<BookingResults> o){
        this.mContext = c;
        this.ride = o;
    }

    public BookingAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.individual_booking_information, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final boolean accepted = ride.get(position).getAccepted();
        final String username = ride.get(position).getUsername();
        final String userID = ride.get(position).getUser_id();
        final String rideID = ride.get(position).getRide_id();
        final String licencePlate = ride.get(position).getLicencePlate();
        final String pickupTime = ride.get(position).getPickupTime();
        String from = "From: " + ride.get(position).getLocation().replaceAll("\n", ", ");
        String to = "To: " + ride.get(position).getDestination().replaceAll("\n", ", ");
        final String cost = String.valueOf("£ " + ride.get(position).getCost()) + ".00";
        final String pickupLocation = ride.get(position).getPickupLocation();
        final String date = ride.get(position).getDateOfJourney() + " - " + ride.get(position).getPickupTime();

        if (accepted){
            holder.cardView.setCardBackgroundColor(Color.rgb(234, 255, 236));
            holder.bookingStatusTextview.setText("Booking accepted!");
            holder.bookingStatusTextview.setTextColor(Color.rgb(0, 160, 66));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PickupActivity.class);
                    intent.putExtra("pickupLocation", pickupLocation);
                    intent.putExtra("rideID", rideID);
                    intent.putExtra("userID", userID);
                    intent.putExtra("licencePlate", licencePlate);
                    intent.putExtra("pickupTime", pickupTime);
                    mContext.startActivity(intent);
                }
            });
        }

        if (to.length() > 20){
            to = to.substring(0 , Math.min(to.length(), 21));
            to = to + "...";
            holder.to.setText(to);
        } else {
            holder.to.setText(to);
        }

        if (from.length() > 20){
            from = from.substring(0 , Math.min(from.length(), 21));
            from = from + "...";
            holder.from.setText(from);
        } else {
            holder.from.setText(from);
        }

        holder.costs.setText(cost);

        Picasso.get().load(ride.get(position).getProfile_photo()).into(holder.profile_photo);

        holder.date.setText(date);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ride.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout view;
        TextView rides, from, to, date, seats, costs, bookingStatusTextview;
        CircleImageView profile_photo;
        RatingBar ratingBar;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.bookingCardView);
            view = (LinearLayout) itemView.findViewById(R.id.view);
            from = (TextView) itemView.findViewById(R.id.fromTxt);
            to = (TextView) itemView.findViewById(R.id.toTxt);
            date = (TextView) itemView.findViewById(R.id.individualDateTxt);
            costs = (TextView) itemView.findViewById(R.id.priceTxt);
            profile_photo = (CircleImageView) itemView.findViewById(R.id.indiviual_profile_picture);
            bookingStatusTextview = (TextView) itemView.findViewById(R.id.bookingStatusTextview);


        }
    }

}
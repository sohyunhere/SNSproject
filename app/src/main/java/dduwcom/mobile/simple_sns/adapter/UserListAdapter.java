package dduwcom.mobile.simple_sns.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;

import dduwcom.mobile.simple_sns.FirebaseHelper;
import dduwcom.mobile.simple_sns.PostInfo;
import dduwcom.mobile.simple_sns.R;
import dduwcom.mobile.simple_sns.UserInfo;
import dduwcom.mobile.simple_sns.activity.PostActivity;
import dduwcom.mobile.simple_sns.activity.WritePostActivity;
import dduwcom.mobile.simple_sns.listener.OnPostListener;
import dduwcom.mobile.simple_sns.view.ReadContentsView;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MainViewHolder> {

    private ArrayList<UserInfo> mDataset;
    private Activity activity;

    static class MainViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public MainViewHolder(CardView v) {
            super(v);
            cardView = v;

        }
    }

    public UserListAdapter(Activity activity, ArrayList<UserInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);

        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
            }
        });


        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView photoImageView = cardView.findViewById(R.id.photoImageView);
        TextView nameTextView = cardView.findViewById(R.id.nameTextView);
        TextView addressTextView = cardView.findViewById(R.id.addressTextView);

        UserInfo userInfo = mDataset.get(position);
        if(mDataset.get(position).getPhotoUrl() != null){
            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).centerCrop().override(500).into(photoImageView);

        }
        nameTextView.setText(userInfo.getName());
        addressTextView.setText(userInfo.getAddress());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

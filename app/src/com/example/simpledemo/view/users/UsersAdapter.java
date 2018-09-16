package com.example.simpledemo.view.users;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.utils.CircleTransform;
import com.example.simpledemo.utils.ListUtils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    public interface OnUserClickedListener {
        void onUserClicked(User user);
    }

    private List<User> users;
    private OnUserClickedListener onUserClickedListener;

    public UsersAdapter() {
        setHasStableIds(true);
    }

    public void replaceItems(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setOnUserClickedListener(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        holder.render(users.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ListUtils.count(users);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.initials) protected TextView initials;
        @BindView(R.id.profile_photo) protected ImageView profilePhoto;
        @BindView(R.id.name) protected TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        protected void onUserClicked() {
            User user = (User) itemView.getTag();
            if (onUserClickedListener != null) {
                onUserClickedListener.onUserClicked(user);
            }
        }

        public void render(User user) {
            if (user == null) return;

            itemView.setTag(user);
            initials.setText(user.getName().substring(0, 2));
            name.setText(user.getName());

            if (TextUtils.isEmpty(user.getPhotoUrl())) {
                initials.setVisibility(View.VISIBLE);
                profilePhoto.setVisibility(View.GONE);
            } else {
                initials.setVisibility(View.GONE);
                profilePhoto.setVisibility(View.VISIBLE);
                new Picasso.Builder(MainApplication.getInstance())
                        .loggingEnabled(true)
                        .downloader(new OkHttp3Downloader(MainApplication.getInstance().graph().getOkHttpClient()))
                        .build()
                        .load(user.getPhotoUrl())
                        .fit()
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(profilePhoto);
            }
        }
    }
}
package com.example.simpledemo.view.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.utils.ListUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    public interface OnEventClickedListener {
        void onEventClicked(Event event);
    }

    private List<Event> events;
    private OnEventClickedListener onEventClickedListener;

    public EventsAdapter() {
        setHasStableIds(true);
    }

    public void replaceItems(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.render(events.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ListUtils.count(events);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_name) protected TextView name;
        @BindView(R.id.event_location) protected TextView location;
        @BindView(R.id.event_date) protected TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        protected void onEventClicked() {
            Event event = (Event) itemView.getTag();
            if (onEventClickedListener != null) {
                onEventClickedListener.onEventClicked(event);
            }
        }

        public void render(Event event) {
            if (event == null) return;

            itemView.setTag(event);
            name.setText(event.getSubject());
            location.setText(" @ " + event.getLocation());

            SimpleDateFormat format = new SimpleDateFormat("MMM dd, HH:mm");
            String activityTime = format.format(event.getStartDateTime());
            activityTime += " - " + format.format(event.getEndDateTime());
            date.setText(activityTime);
        }
    }
}
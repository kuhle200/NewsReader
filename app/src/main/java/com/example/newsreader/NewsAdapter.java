package com.example.newsreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context context; // Reference to the Context of the Activity that is using the adapter
    private ArrayList<NewsItem> newsItems = new ArrayList<>(); // ArrayList of NewsItem objects

    // Define constructor for NewsAdapter
    public NewsAdapter(Context context){
        this.context = context;
    }

    // Define method to set the news items in the adapter
    public void setNewsItems(ArrayList<NewsItem> newsItems) {
        this.newsItems = newsItems;
        notifyDataSetChanged(); // Inform the RecyclerView that the data has changed
    }

    @NonNull
    @Override
    // Method to inflate the layout for each news item in the RecyclerView
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Set the title of the news item in the titleTextView
        holder.titleTextView.setText(newsItems.get(position).getTitle());

        // Set an OnClickListener on the parent view to launch a new activity(WebsiteActivity) when clicked
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebsiteActivity.class);
                intent.putExtra("url", newsItems.get(holder.getAdapterPosition()).getLink());
                context.startActivity(intent);

            }
        });
    }

    // Method to return the total number of news items
    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    // Hold references to the views that make up a single news item in the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView;    // TextView that displays the title of the news item
        private CardView parent;           // CardView that serves as the container for the news item view

        //Constructor for ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}

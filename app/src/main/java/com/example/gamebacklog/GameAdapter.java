package com.example.gamebacklog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {


    List<Game> mGamesList;

    public GameAdapter(List<Game> mGamesList) {
        this.mGamesList = mGamesList;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_gamesrow, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, int i) {
        final Game game = mGamesList.get(i);
        viewHolder.titleViewGame.setText(game.getGameTitle());
        viewHolder.platformView.setText(game.getPlatform());
        viewHolder.statusView.setText(game.getStatus());
        viewHolder.dateView.setText(game.getDate());

    }

    @Override
    public int getItemCount() {
        return mGamesList.size();
    }

    public void swapList (List<Game> newList) {
        mGamesList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleViewGame;
        TextView platformView;
        TextView statusView;
        TextView dateView;
        CardView cardViewGame;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleViewGame = itemView.findViewById(R.id.titleViewGame);
            platformView = itemView.findViewById(R.id.viewPlatform);
            statusView = itemView.findViewById(R.id.viewStatus);
            dateView = itemView.findViewById(R.id.viewDate);

            cardViewGame = itemView.findViewById(R.id.gameCardView);
        }
    }
}

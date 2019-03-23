package com.example.gamebacklog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private RecyclerView rvGames;
    private List<Game> mGamesList;
    private GameAdapter mGameAdapter;
    private GameRoomDatabase db;
    private GestureDetector mGestureDetector;
    private MainViewRepository mMainViewRepository;


    public static final String NEW_GAME = "Game";
    public static final String NEW_UPDATEGAME = "updateGameData";
    public static final int REQUESTCODE_ONE = 1;
    public static final int REQUESTCODE_TWO = 2;
    private int mModifyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initRecyclerView();



            FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddGame.class);
                startActivityForResult(intent, REQUESTCODE_ONE);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_item) {
            mMainViewRepository.deleteAllGames();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        if (mGameAdapter == null) {
            mGameAdapter = new GameAdapter(mGamesList);
            rvGames.setAdapter(mGameAdapter);
        } else {
            mGameAdapter.swapList(mGamesList);
        }
    }

    public void initRecyclerView(){

        rvGames = findViewById(R.id.rvGames);
        rvGames.setLayoutManager(new LinearLayoutManager(this));
        db = GameRoomDatabase.getDatabase(this);
        rvGames.setAdapter(new GameAdapter(mGamesList));

        mMainViewRepository = ViewModelProviders.of(this).get(MainViewRepository.class);
        mMainViewRepository.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                mGamesList = games;
                updateUI();
            }

        });

        mGamesList = new ArrayList<>();

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        rvGames.addOnItemTouchListener(this);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());

                        mMainViewRepository.delete(mGamesList.get(position));
                        mGamesList.remove(position);
                        mGameAdapter.notifyItemRemoved(position);
                        updateUI();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvGames);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE_ONE) {
            if (resultCode == RESULT_OK) {
                Game addGame = data.getParcelableExtra(MainActivity.NEW_GAME);
                mMainViewRepository.insert(addGame);
            }
        }

        if (requestCode == REQUESTCODE_TWO) {
            if (resultCode == RESULT_OK) {
                Game update = data.getParcelableExtra(MainActivity.NEW_UPDATEGAME);
                mMainViewRepository.update(update);
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        int mAdapterPosition = recyclerView.getChildAdapterPosition(child);

        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

            Intent intent = new Intent(MainActivity.this, AlterGame.class);
            mModifyPosition = mAdapterPosition;
            intent.putExtra(MainActivity.NEW_UPDATEGAME,  mGamesList.get(mAdapterPosition));
            startActivityForResult(intent, REQUESTCODE_TWO);

        }

        return false;
    }



    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}

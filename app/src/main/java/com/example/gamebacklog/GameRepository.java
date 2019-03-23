package com.example.gamebacklog;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRepository {
    private GameRoomDatabase gameDatabase;
    private GameDao mGameDao;
    private LiveData<List<Game>> mGamesList;
    private Executor mExecutor= Executors.newSingleThreadExecutor();

    public GameRepository(Context context) {
       gameDatabase=GameRoomDatabase.getDatabase(context);
       mGameDao=gameDatabase.gameDao();
       mGamesList=mGameDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return mGamesList;
    }

    public void insert(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.insert(game);
            }
        });
    }



    public void update(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.update(game);
            }
        });
    }

    public void deleteAllGames() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteAllGames();

                getAllGames();
            }
        });
    }

    public void delete(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.delete(game);
            }
        });
    }
}

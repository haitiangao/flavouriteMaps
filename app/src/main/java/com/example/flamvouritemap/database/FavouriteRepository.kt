package com.example.flamvouritemap.database

import android.app.Application
import com.example.flamvouritemap.database.FavouriteRoomDatabase.Companion.getDatabase
import com.example.flamvouritemap.model.FavouriteResult
import com.example.flamvouritemap.model.Result
import com.example.flamvouritemap.util.DebugLogger.logDebug
import com.example.flamvouritemap.util.DebugLogger.logError
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouriteRepository(application: Application?) {
    private val compositeDisposable = CompositeDisposable()
    private val favouriteDao: FavouriteDao
    private var favouritePlaces: MutableList<FavouriteResult>
    val allFavourites: MutableList<FavouriteResult>
        get() = favouritePlaces

    fun disposeDisposables() {
        compositeDisposable.dispose()
    }


    fun insertAFavourite(favouriteResult: FavouriteResult) {
        val disposable = Completable.fromAction {
            favouriteDao.insertAFavourite(favouriteResult)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logDebug(
                        "Insert Successful"
                    )
                    favouritePlaces = favouriteDao.favouritePlaces

                }
            ) { throwable: Throwable? ->
                logError(
                    throwable!!
                )
            }
        compositeDisposable.add(disposable)
        //compositeDisposable.dispose();
    }

    fun deleteAFavourite(favouriteResult: FavouriteResult) {
        val disposable = Completable.fromAction {
            favouriteDao.deleteAFavourite(favouriteResult.id)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logDebug(
                        "Delete Successful"
                    )
                    favouritePlaces = favouriteDao.favouritePlaces
                }
            ) { throwable: Throwable? ->
                logError(
                    throwable!!
                )
            }
        compositeDisposable.add(disposable)

    }


    init {
        val db = getDatabase(application!!)
        favouriteDao = db!!.bookDao()
        favouritePlaces = favouriteDao.favouritePlaces
    }
}
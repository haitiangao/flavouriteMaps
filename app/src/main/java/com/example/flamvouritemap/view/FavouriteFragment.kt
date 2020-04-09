package com.example.flamvouritemap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.flamvouritemap.R
import com.example.flamvouritemap.adapter.FavouriteAdapter
import com.example.flamvouritemap.model.FavouriteResult
import com.example.flamvouritemap.placeModel.Result
import com.example.flamvouritemap.util.DebugLogger
import com.example.flamvouritemap.viewmodel.MapViewModel
import io.reactivex.disposables.CompositeDisposable
import java.util.*


class FavouriteFragment(username: String?) : Fragment(), FavouriteAdapter.UserClickListener {
    private lateinit var mapViewModel: MapViewModel
    private var favouriteResults: MutableList<FavouriteResult> = ArrayList()
    private var resultList: MutableList<Result> = ArrayList()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var favouriteAdapter: FavouriteAdapter

    @BindView(R.id.favouritePOIRecycle)
    lateinit var favouritePOIRecycle: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        ButterKnife.bind(this,view)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel = ViewModelProvider(this).get<MapViewModel>(MapViewModel::class.java)
        val itemDecoration =
            DividerItemDecoration(context, RecyclerView.VERTICAL)
        favouritePOIRecycle.layoutManager = LinearLayoutManager(context)
        favouritePOIRecycle.adapter = context?.let { FavouriteAdapter(resultList, it, this) }
        favouritePOIRecycle.addItemDecoration(itemDecoration)
        favouriteResults.clear()

        populateResultList()
    }

    private fun populateResultList(){

        favouriteResults.addAll(mapViewModel.getAllFavourites())
        resultList.clear()
        DebugLogger.logDebug("result list size:" + favouriteResults.size)

        for(element in favouriteResults){
            DebugLogger.logDebug("Elementid:" + element.id)
            compositeDisposable.add(mapViewModel.getSpecificPlaceRx(element.id).subscribe({ resultLibrary ->

                resultList.add(resultLibrary.result)
                //DebugLogger.logDebug("result names:" + resultLibrary.result.name)

                favouritePOIRecycle.adapter = null
                favouriteAdapter = context?.let { FavouriteAdapter(resultList, it, this) }!!
                favouritePOIRecycle.adapter = favouriteAdapter
                favouriteAdapter.notifyDataSetChanged()

            },{throwable ->
                    DebugLogger.logError(throwable)
                })
            )
        }
    }

    private fun getNewFavourites(favouriteResults: List<FavouriteResult>){
        favouritePOIRecycle.adapter = null

        if(favouriteResults.isNotEmpty()) {
            for (element in favouriteResults) {

                compositeDisposable.add(
                    mapViewModel.getSpecificPlaceRx(element.id).subscribe({ resultLibrary ->

                        resultList.add(resultLibrary.result)
                        //DebugLogger.logDebug("result names:" + resultLibrary.result.name)

                        favouriteAdapter = context?.let { FavouriteAdapter(resultList, it, this) }!!
                        favouritePOIRecycle.adapter = favouriteAdapter
                        favouriteAdapter.notifyDataSetChanged()

                    }, { throwable ->
                        DebugLogger.logError(throwable)
                    })
                )
            }
        }
    }

    fun addToView(placeID:String){
        val favourite = FavouriteResult(placeID)
        favouriteResults.add(favourite)
        resultList.clear()
        getNewFavourites(favouriteResults)
    }

    override fun unfavouriteButton(result: Result) {
        val favouriteResult = FavouriteResult(result.placeId)
        mapViewModel.removeFavourite(favouriteResult)


        for (element in favouriteResults) {
            if (element.id==result.placeId) {
                favouriteResults.remove(element)
                break
            }
        }

        resultList.clear()
        getNewFavourites(favouriteResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}

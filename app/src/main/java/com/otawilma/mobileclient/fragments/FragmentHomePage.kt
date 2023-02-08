package com.otawilma.mobileclient.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otawilma.mobileclient.*
import com.otawilma.mobileclient.dataClasses.SchoolDay
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import java.time.LocalDate

class FragmentHomePage:Fragment(R.layout.fragment_home_page), OtawilmaNetworking {

    private lateinit var timeTableDayAdapter: TimeTableDayAdapter

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val coroutineScopeMain = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeTableDayAdapter = TimeTableDayAdapter()



        coroutineScope.launch {



            val schoolDayFlow : Flow<MutableList<SchoolDay>> = flow {

                var day = LocalDate.now()

                val listCache : MutableList<SchoolDay> = mutableListOf()
                while (listCache.size < sharedPreferences.homePageDays && day <= LocalDate.now().plusDays(20)) {
                    val element = dayRepository.getCached(day)
                    if (element != null && element.items.isNotEmpty()) {
                        listCache.add(element)
                        emit(listCache)
                    }
                    day = day.plusDays(1)
                }

                day = LocalDate.now()

                val listActual : MutableList<SchoolDay> = mutableListOf()
                while (listActual.size < sharedPreferences.homePageDays && day <= LocalDate.now().plusDays(20)) {
                    var token = waitUntilToken(context!!)

                    while (true) {
                        try {
                            val element = dayRepository.getFromServer(token, day)
                            if (element != null && element.items.isNotEmpty()) {
                                listActual.add(element)
                            }
                            day = day.plusDays(1)
                            break
                        } catch (e: InvalidTokenNetworkException) {
                            token = invalidateTokenAndGetNew(context!!)
                        } catch (e: SocketTimeoutException){
                            delay(100)
                        }
                    }
                }
                emit(listActual)
            }

            schoolDayFlow.collect {
                coroutineScopeMain.launch {
                    timeTableDayAdapter.submitItems(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManagerBetter = DayRecyclerLinearLayoutManager(context)
        layoutManagerBetter.orientation = LinearLayoutManager.HORIZONTAL
        view.findViewById<RecyclerView>(R.id.recyclerViewHomeSchedule)?.apply {

            // THIS IS BULLSHIT FUCK YOU DROID
            layoutManager = layoutManagerBetter
            isNestedScrollingEnabled = false
            adapter = timeTableDayAdapter

        }

    }

    class DayRecyclerLinearLayoutManager(context: Context?) : LinearLayoutManager(context){
        override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
            /* Useless for now
            if (lp != null) {
                lp.width = width/sharedPreferences.homePageDays
            }
             */
            return super.checkLayoutParams(lp)
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        coroutineScopeMain.cancel()
        super.onDestroy()
    }
}

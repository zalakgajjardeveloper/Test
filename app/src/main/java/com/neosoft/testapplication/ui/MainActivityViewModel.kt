package com.neosoft.testapplication.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neosoft.testapplication.R

class MainActivityViewModel : ViewModel() {

    var displayList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    var data = ArrayList<ArrayList<String>>()
    var sampleImages = intArrayOf(
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3,
        R.drawable.image_4,
        R.drawable.image_5
    )
    var currentPosition = 0
        set(value) {
            displayList.postValue(data[value])
            field = value
        }

    init {
        loadData()
        displayList.postValue(data[currentPosition])
    }


    private fun loadData() {
        data = ArrayList()
        for (i in 1..sampleImages.size) {
            val list = ArrayList<String>()
            for (j in 1..20) {
                list.add("Item $i $j")
            }
            data.add(list)
        }
    }


    fun filterData(newText: String) {
        val filterList = ArrayList<String>()
        for (item in data[currentPosition]) {
            if (item.contains(newText,true))
                filterList.add(item)
        }

        displayList.postValue(filterList)
    }
}

package com.chkan.iqtimer.domain.models

import com.github.mikephil.charting.data.BarEntry
import java.util.ArrayList

data class ChartModel(val data: ArrayList<BarEntry>, val titles: Array<String>, val default: Int)

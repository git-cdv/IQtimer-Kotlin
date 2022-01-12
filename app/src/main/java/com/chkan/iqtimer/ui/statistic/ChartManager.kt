package com.chkan.iqtimer.ui.statistic

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chkan.iqtimer.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.color.MaterialColors
import java.util.ArrayList
import javax.inject.Inject


class ChartManager @Inject constructor (private val ctx: Context) {

    private val colorPrimary =
        MaterialColors.getColor(ctx, R.attr.colorOnPrimary, Color.GRAY)

    fun getChartDays(chartDay: BarChart, list: ArrayList<BarEntry>, titles: Array<String>, defaultPlan: Int) : BarChart {

        //создаем через свой класс, где переопределен метод вывода цвета для бара
        val barDataSet1 = MyBarDataSet(list, ctx.resources.getString(R.string.stat_chart_description),defaultPlan)

        //назначаем цвета для баров
        barDataSet1.setColors(
            ContextCompat.getColor(
                ctx,
                R.color.brand_orange
            ), ContextCompat.getColor(ctx, R.color.brand_blue_600)
        )

        barDataSet1.valueTextColor = colorPrimary
        val barData = BarData()
        barData.addDataSet(barDataSet1)

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return titles[value.toInt()]
            }
        }
        //настройка оси Х (шаг и формат подписей)
        val xAxis: XAxis = chartDay.xAxis
        xAxis.granularity = 1f // minimum axis-step (interval) is 1

        xAxis.valueFormatter = formatter
        xAxis.textColor = colorPrimary

        //добавление "линии тренда" (план) и начала с 0
        val leftAxis: YAxis = chartDay.axisLeft
        val rightAxis: YAxis = chartDay.axisRight

        val ll = LimitLine(defaultPlan.toFloat())
        ll.lineColor = ContextCompat.getColor(ctx, R.color.brand_orange)
        //как пунктир
        ll.enableDashedLine(16f, 4f, 2f)
        ll.lineWidth = 1f
        leftAxis.addLimitLine(ll)
        //чтобы начиналось с 0
        leftAxis.axisMinimum = 0f
        rightAxis.axisMinimum = 0f
        leftAxis.textColor = colorPrimary
        rightAxis.textColor = colorPrimary

        chartDay.data = barData
        //устанавливает количество Баров для отображение, если больше - скролится
        chartDay.setVisibleXRangeMaximum(14f)
        //переводит начальный вид графиков в конец
        //chartDay.moveViewToX(it.data.size.toFloat())
        //убираем description
        val description: Description = chartDay.description
        description.isEnabled = false
        chartDay.isAutoScaleMinMaxEnabled = true
        chartDay.setNoDataTextColor(
            ContextCompat.getColor(
                ctx,
                R.color.brand_orange
            )
        )
        val legend: Legend = chartDay.legend
        legend.textColor = colorPrimary

        return chartDay
    }

    fun getChartMonth(chartMonth: BarChart, list: ArrayList<BarEntry>, titles: Array<String>) : BarChart {

        val stringDescription: String =
            ctx.resources.getString(R.string.stat_chart_description_month)

        //создаем через свой класс, где переопределен метод вывода цвета для бара
        val barDataSet1 = BarDataSet(list, stringDescription)
        //назначаем цвета для баров
        barDataSet1.setColors(ContextCompat.getColor(ctx, R.color.brand_blue_600))
        barDataSet1.valueTextColor = colorPrimary
        val barData = BarData()
        barData.addDataSet(barDataSet1)

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return titles[value.toInt()]
            }
        }
        //настройка оси Х (шаг и формат подписей)
        val xAxis: XAxis = chartMonth.xAxis

        xAxis.granularity = 1f // minimum axis-step (interval) is 1

        xAxis.valueFormatter = formatter
        xAxis.textColor = colorPrimary

        //чтобы начиналось с 0
        val leftAxisM: YAxis = chartMonth.axisLeft
        val rightAxisM: YAxis = chartMonth.axisRight
        leftAxisM.axisMinimum = 0f
        rightAxisM.axisMinimum = 0f
        leftAxisM.textColor = colorPrimary
        rightAxisM.textColor = colorPrimary

        chartMonth.data = barData
        //устанавливает количество Баров для отображение, если больше - скролится
        chartMonth.setVisibleXRangeMaximum(12f)
        //переводит начальный вид графиков в конец
        chartMonth.moveViewToX(list.size.toFloat())
        //убираем description
        val description: Description = chartMonth.description
        description.isEnabled = false
        val legend: Legend = chartMonth.legend
        legend.textColor = colorPrimary

        return chartMonth
    }

    inner class MyBarDataSet (list: List<BarEntry>, label: String, default: Int) :
        BarDataSet(list, label) {

        private var def : Int =0
        private var myList : List<BarEntry>

        init {
            def = default
            myList = list
        }

        override fun getEntryIndex(e: BarEntry?): Int {
            return myList.indexOf(e)
        }

        override fun getColor(index: Int): Int {
            return if (getEntryForIndex(index).y < def) {
                mColors[0]
            } else {
                mColors[1]
            }
        }
    }

}


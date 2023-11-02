package com.example.stepappv4.ui.DailyReport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepappv4.R;
import com.example.stepappv4.StepAppOpenHelper;
import com.example.stepappv4.databinding.FragmentDayBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DayFragment extends Fragment {

    AnyChartView anyChartView;

    Date cDate = new Date();

    public Map<String, Integer> stepsByDays = null;

    private FragmentDayBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create column chart
        anyChartView = root.findViewById(R.id.dayBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar));

        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Cartesian createColumnChart(){
        //***** Read data from SQLiteDatabase *********/
        stepsByDays = StepAppOpenHelper.loadStepsByDays(getContext());

        Map<String, Integer> graph_map = new TreeMap<>();

        for(int i =0; i<stepsByDays.size(); i++) {
            graph_map.put(Integer.toString(i), 0);
        }

        graph_map.putAll(stepsByDays);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        Column column = cartesian.column(data);

        column.fill("#1EB980");
        column.stroke("#1EB980");
        column.tooltip()
                .titleFormat("At days: {%X}")
                .format("{%Value} Steps")
                .anchor(Anchor.RIGHT_BOTTOM);

        column.tooltip()
                .position(Position.RIGHT_TOP) .offsetX(0d)
                .offsetY(5);

        // Modifying properties of cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);
        cartesian.yAxis(0).title("Number of steps");
        cartesian.xAxis(0).title("Days");
        cartesian.background().fill("#000000");
        cartesian.animation(true);

        return cartesian;
    }




}

package interfaceMagazinier.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class imDashboardController implements Initializable {
    class areaChartElement{
        String stringData;
        int intData;

        public void chartElement(String stringData, int intData){
            this.stringData = stringData;
            this.intData = intData;
        }
    }

    @FXML AreaChart<String, Number> areaChart;
    areaChartElement[] areaChartDayData = new areaChartElement[24];
    areaChartElement[] areaChartWeekData = new areaChartElement[7];
    areaChartElement[] areaChartMounthData = new areaChartElement[12];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        areaChartGetData();
        areaChartWeekData();
    }

    private void areaChartGetData() {

        //DayData
        //WeekData
        //MounthData
    }

    private void areaChartDayData() {
        areaChart.getData().clear();
        XYChart.Series<String, Number> daySeries = new XYChart.Series<>();
        //load Data
        //
        areaChart.getData().add(daySeries);
    }

    private void areaChartWeekData(){
        areaChart.getData().clear();
        XYChart.Series<String, Number> weekSeries = new XYChart.Series<String, Number>(); //Series are used to add data to the chart
        //Load Data
        weekSeries.getData().add(new XYChart.Data<String, Number>("Sunday", 40));
        weekSeries.getData().add(new XYChart.Data<String, Number>("Monday", 50));
        weekSeries.getData().add(new XYChart.Data<String, Number>("Tuesday", 30));
        weekSeries.getData().add(new XYChart.Data<String, Number>("Wednesday", 35));
        weekSeries.getData().add(new XYChart.Data<String, Number>("Thursday", 20));
        weekSeries.getData().add(new XYChart.Data<String, Number>("Friday", 80));
        weekSeries.getData().add(new XYChart.Data<String, Number>("Saturday", 10));
        //
        areaChart.getData().add(weekSeries);
    }

    private void areChartMounthData(){
        areaChart.getData().clear();
        XYChart.Series<String, Number> mountSeries = new XYChart.Series<>();
        //load Data

        //
        areaChart.getData().add(mountSeries);
    }



}

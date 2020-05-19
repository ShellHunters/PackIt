package interfaceMagazinier.dashboard;

import Connection.ConnectionClass;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.xml.bind.Marshaller;
import java.net.URL;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class imDashboardController implements Initializable {

    @FXML AreaChart<Number, Number> areaChart;
    @FXML NumberAxis areaChartXAxis;
    @FXML JFXDatePicker areaChartDatePicker;

    float[] areaChartDayData = new float[24];
    float[] areaChartMounthData = new float[31];
    float[] areaChartYearData = new float[12];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        areaChartXAxis.setAutoRanging(false);
        areaChartXAxis.setTickUnit(1);
        areaChartXAxis.setMinorTickVisible(false);

        areaChartDatePicker.setValue(LocalDate.now());
        areaChartDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                resetAreaChartTables();
                areaChartGetData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            areaChartGetData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        areaChartDayData();
    }

    private void areaChartGetData() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM sells";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()){
            //Need local date to access parts of date and time (day, mounth, year, hours, minutes and seconds)
            LocalDate sellDate = rs.getDate("sellDate").toLocalDate();
            LocalTime sellTime = rs.getTime("sellDate").toLocalTime();

            if (sellDate.isEqual(areaChartDatePicker.getValue()))
                areaChartDayData[sellTime.getHour()] += rs.getFloat("amount");

            if (sellDate.getMonth().equals(areaChartDatePicker.getValue().getMonth()) && sellDate.getYear() == areaChartDatePicker.getValue().getYear())
                areaChartMounthData[sellDate.getDayOfMonth()] += rs.getFloat("amount");

            if (sellDate.getYear() == areaChartDatePicker.getValue().getYear())
                areaChartYearData[sellDate.getMonth().getValue() - 1] += rs.getFloat("amount");
        }
    }

     @FXML private void areaChartDayData() {
        areaChart.getData().clear();
        areaChartXAxis.setUpperBound(23);
        areaChartXAxis.setLowerBound(0);

        XYChart.Series<Number, Number> daySeries = new XYChart.Series<>();
        for (int i = 0; i < areaChartDayData.length; i++){
            daySeries.getData().add(new XYChart.Data<>(i, areaChartDayData[i]));
        }
        areaChart.getData().add(daySeries);
    }

     @FXML private void areaChartMounthData(){
        areaChart.getData().clear();
        areaChartXAxis.setUpperBound(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear()));
        areaChartXAxis.setLowerBound(1);

        XYChart.Series<Number, Number> mounthSeries = new XYChart.Series<>();
        for (int i = 0; i < areaChartMounthData.length; i++){
            mounthSeries.getData().add(new XYChart.Data<>(i + 1, areaChartMounthData[i]));
        }
        areaChart.getData().add(mounthSeries);
    }

    @FXML private void areaChartYearData(){
        areaChart.getData().clear();
        areaChartXAxis.setUpperBound(12);
        areaChartXAxis.setLowerBound(1);

        XYChart.Series<Number , Number> yearSeries = new XYChart.Series<>(); //Series are used to add data to the chart
        //Load Data
        for (int i = 0; i < areaChartYearData.length; i++){
            yearSeries.getData().add(new XYChart.Data<>(i + 1, areaChartYearData[i]));
        }
        //
        areaChart.getData().add(yearSeries);
    }

    private void resetAreaChartTables(){
        for (int i = 0; i < areaChartMounthData.length; i ++){
            if (i < areaChartDayData.length) areaChartDayData[i] = 0;
            if (i < areaChartYearData.length) areaChartYearData[i] = 0;
            areaChartMounthData[i] = 0;
        }
    }

}

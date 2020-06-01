package interfaceMagazinier.dashboard;

import Connection.ConnectionClass;
import com.jfoenix.controls.JFXDatePicker;
import interfaceMagazinier.stock.imStockController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class imDashboardController implements Initializable {

    @FXML AreaChart<Number, Number> areaChart;
    @FXML NumberAxis areaChartXAxis;
    @FXML JFXDatePicker areaChartDatePicker;

    @FXML PieChart pieChart;

    @FXML Label totalProfit;
    @FXML Label todayProfit;
    @FXML Label totalProducts;
    @FXML Label totalProviders;

    float[] areaChartDayData = new float[24];
    float[] areaChartMounthData = new float[31];
    float[] areaChartYearData = new float[12];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        areaChartSetUp();
        pieChartSetUp();
        loadStatistique();
    }

    //region pieChart methods
    private void pieChartSetUp(){
        //get data from database
        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT * FROM stock ORDER BY numberOfSells DESC LIMIT 6";
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                pieChartData.add(new PieChart.Data(rs.getString("name"), rs.getInt("numberOfSells")));
            }
            pieChart.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region areaChart methods
    private void areaChartSetUp() {
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
            LocalDate sellDate = rs.getDate("sellTime").toLocalDate();
            LocalTime sellTime = rs.getTime("sellTime").toLocalTime();

            if (sellDate.isEqual(areaChartDatePicker.getValue()))
                areaChartDayData[sellTime.getHour()] += rs.getFloat("sellPrice") * rs.getInt("quantity");

            if (sellDate.getMonth().equals(areaChartDatePicker.getValue().getMonth()) && sellDate.getYear() == areaChartDatePicker.getValue().getYear())
                areaChartMounthData[sellDate.getDayOfMonth() - 1] += rs.getFloat("sellPrice") * rs.getInt("quantity");

            if (sellDate.getYear() == areaChartDatePicker.getValue().getYear())
                areaChartYearData[sellDate.getMonth().getValue() - 1] += rs.getFloat("sellPrice") * rs.getInt("quantity");
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
        daySeries.setName("Turnover across the day");
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
        mounthSeries.setName("Turnover across the mounth");
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
        yearSeries.setName("Turnover across the year");
        areaChart.getData().add(yearSeries);
    }

    private void resetAreaChartTables(){
        for (int i = 0; i < areaChartMounthData.length; i ++){
            if (i < areaChartDayData.length) areaChartDayData[i] = 0;
            if (i < areaChartYearData.length) areaChartYearData[i] = 0;
            areaChartMounthData[i] = 0;
        }
    }

    //endregion

    //region pane stats
    public void loadStatistique(){
        Connection connection = ConnectionClass.getConnection();
        try {
            String query = "Select count(barcode) from stock ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                String sum = rs.getString("count(barcode)");
                totalProducts.setText(sum);
                totalProviders.setText("5");
            }}
        catch (SQLException e){}
        try {
            String query = "Select sum(profit) from sells ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                String sum = rs.getString("sum(profit)");
                totalProfit.setText(sum);
            }}
        catch (SQLException e){}
        Calendar calendar= new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        String date=year+"-"+month+"-"+day;
        try {
            String query = "Select sum(profit) from sells WHERE sellDate=?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1 , date);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                String sum = rs.getString("sum(profit)");
                todayProfit.setText(sum);
            }}
        catch (SQLException e){}
    }

    //endregion
}

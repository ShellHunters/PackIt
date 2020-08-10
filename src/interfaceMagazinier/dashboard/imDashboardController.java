package interfaceMagazinier.dashboard;

import Connector.ConnectionClass;
import basicClasses.product;
import basicClasses.sell;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class imDashboardController implements Initializable {

    @FXML StackPane stackPane;

    @FXML AreaChart<Number, Number> areaChart;
    @FXML NumberAxis areaChartXAxis;
    @FXML AreaChart<Number, Number> areaChartTotal;
    @FXML NumberAxis areaChartXAxisTotal;
    @FXML JFXDatePicker areaChartDatePicker;
    @FXML JFXTextField objective;
    float[] areaChartDayData = new float[24];
    float[] areaChartMounthData = new float[31];
    float[] areaChartYearData = new float[12];

    float[] areaChartDayDataTotal = new float[24];
    float[] areaChartMounthDataTotal = new float[31];
    float[] areaChartYearDataTotal = new float[12];

    @FXML PieChart pieChart;

    @FXML Label totalProfit;
    @FXML Label todayProfit;
    @FXML Label totalProducts;
    @FXML Label totalProviders;

    @FXML Label lastSellAmount;
    @FXML Label lastSellProfit;
    @FXML Label lastSellNumberOfProducts;
    @FXML Label lastSellDateTime;

    public static ObservableList<sell> sells;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        areaChartSetUp();
        pieChartSetUp();
        loadStatistiqueFrames();
        loadHistory();
        setLastSale();
    }

    //region sellHistory methods
    private void loadHistory() {
        sells = FXCollections.observableArrayList();
        try {
            Connection connection = ConnectionClass.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM sells";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                int id =  rs.getInt("id");
                product soldProduct = new product(rs.getInt("productCode"), rs.getFloat("sellPrice"), rs.getFloat("profit"), rs.getInt("quantity"));
                if (sells.size() != 0 && sells.get(sells.size() - 1).getId() == id) {
                    sell lastSellAdded = sells.get(sells.size() - 1);
                    lastSellAdded.setTotalPrice(lastSellAdded.getTotalPrice() + soldProduct.getSellPrice());
                    lastSellAdded.setTotalProfit(lastSellAdded.getTotalProfit() + soldProduct.getSellPrice() - soldProduct.getBuyPrice());
                    if (soldProduct.getBarcode() != 0) lastSellAdded.getSoldProducts().add(soldProduct);
                    else lastSellAdded.setDiscountAmount(-1 * soldProduct.getSellPrice());
                }
                else {
                    sells.add(new sell(id));
                    sells.get(id).getSoldProducts().add(soldProduct);
                    sells.get(id).setTotalPrice(soldProduct.getSellPrice());
                    sells.get(id).setTotalProfit(soldProduct.getSellPrice() - soldProduct.getBuyPrice());
                    sells.get(id).setSellTime(LocalDateTime.of(rs.getDate("sellTime").toLocalDate(), rs.getTime("sellTime").toLocalTime()));
                    sells.get(id).setClientID(rs.getInt("clientid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setLastSale(){
        if (sells.size() == 0) return;
        sell lastSell = sells.get(sells.size()-1);
        lastSellAmount.setText(String.valueOf(lastSell.getTotalPrice()));
        lastSellProfit.setText(String.valueOf(lastSell.getTotalProfit()));
        lastSellNumberOfProducts.setText(String.valueOf(lastSell.getSoldProducts().size()));
        lastSellDateTime.setText(String.valueOf(lastSell.getSellTime()));
    }

    @FXML private void showHistory() throws IOException {
        Region root1 = FXMLLoader.load(getClass().getResource("history/history.fxml"));
        JFXDialog history = new JFXDialog(stackPane, root1, JFXDialog.DialogTransition.BOTTOM);
        history.show();
    }
    //endregion

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
            pieChart.setTitle("Most sold products");

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
                areaChartDayData();
                areaChartDayDataTotal();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        areaChartXAxisTotal.setAutoRanging(false);
        areaChartXAxisTotal.setTickUnit(1);
        areaChartXAxisTotal.setMinorTickVisible(false);

        try {
            areaChartGetData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        areaChartDayData();
        areaChartDayDataTotal();
    }

    private void areaChartGetData() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM sells";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
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

        areaChartDayDataTotal[0] = areaChartDayData[0];
        for (int i = 1; i < areaChartDayDataTotal.length; i++) areaChartDayDataTotal[i] = areaChartDayDataTotal[i - 1] + areaChartDayData[i];
        areaChartMounthDataTotal[0] = areaChartMounthData[0];
        for (int i = 1; i < areaChartMounthData.length; i++) areaChartMounthDataTotal[i] = areaChartMounthDataTotal[i - 1] + areaChartMounthData[i];
        areaChartYearDataTotal[0] = areaChartYearData[0];
        for (int i = 1; i < areaChartYearDataTotal.length; i++) areaChartYearDataTotal[i] = areaChartYearDataTotal[ i - 1] + areaChartYearData[i];
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
    @FXML private void areaChartDayDataTotal() {
        areaChartTotal.getData().clear();
        areaChartXAxisTotal.setUpperBound(23);
        areaChartXAxisTotal.setLowerBound(0);

        XYChart.Series<Number, Number> daySeries = new XYChart.Series<>();
        for (int i = 0; i < areaChartDayDataTotal.length; i++){
            daySeries.getData().add(new XYChart.Data<>(i, areaChartDayDataTotal[i]));
        }
        daySeries.setName("Turnover across the day");
        areaChartTotal.getData().add(daySeries);
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
    @FXML private void areaChartMounthDataTotal(){
        areaChartTotal.getData().clear();
        areaChartXAxisTotal.setUpperBound(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear()));
        areaChartXAxisTotal.setLowerBound(1);

        XYChart.Series<Number, Number> mounthSeries = new XYChart.Series<>();
        for (int i = 0; i < areaChartMounthDataTotal.length; i++){
            mounthSeries.getData().add(new XYChart.Data<>(i + 1, areaChartMounthDataTotal[i]));
        }
        mounthSeries.setName("Turnover across the mounth");
        areaChartTotal.getData().add(mounthSeries);
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
    @FXML private void setAreaChartYearDataTotal(){
        areaChartTotal.getData().clear();
        areaChartXAxisTotal.setUpperBound(12);
        areaChartXAxisTotal.setLowerBound(1);

        XYChart.Series<Number , Number> yearSeries = new XYChart.Series<>(); //Series are used to add data to the chart
        //Load Data
        for (int i = 0; i < areaChartYearDataTotal.length; i++){
            yearSeries.getData().add(new XYChart.Data<>(i + 1, areaChartYearDataTotal[i]));
        }
        yearSeries.setName("Turnover across the year");
        areaChartTotal.getData().add(yearSeries);
    }


    private void resetAreaChartTables(){
        for (int i = 0; i < areaChartMounthData.length; i ++){
            if (i < areaChartDayData.length) areaChartDayData[i] = areaChartDayDataTotal[i] = 0;
            if (i < areaChartYearData.length) areaChartYearData[i] = areaChartYearDataTotal[i] = 0;
            areaChartMounthData[i] = areaChartMounthDataTotal[i] = 0;
        }
    }

    //endregion

    //region pane stats
    public void loadStatistiqueFrames(){
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
            rs.next();
            String sum = rs.getString(1);
            totalProfit.setText(sum);
            }
        catch (SQLException e){}

        try {
            String query = "Select sum(profit) from sells WHERE sellTime > CURDATE()" ;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            String sum = rs.getString(1);
            if (sum == null) sum = "0";
            todayProfit.setText(sum);
            }
        catch (SQLException e){System.out.println(e);}
    }

    //endregion
}

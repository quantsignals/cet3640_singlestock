import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SingleStock {

    public static void main(String[] args) {
    	String start_date="2024-01-01"; 
    	String end_date="2024-12-31";
    	boolean verbose = false;
      RestService restService = new RestService(verbose);
      String url_string = "https://eodhd.com/api/eod/AAPL.US?api_token=demo&from="+start_date+"&to="+end_date+"&fmt=json";

		  // String json = restService.handleRequest(url_string);  // enable this to retrieve data from the API

	    // use the below for testing, remove when using the API 
      String json = "[{\"date\":\"2025-01-02\",\"open\":248.93,\"high\":249.1,\"low\":241.82,\"close\":243.85,\"adjusted_close\":243.5822,\"volume\":55740700},"
                + "{\"date\":\"2025-01-03\",\"open\":243.36,\"high\":244.18,\"low\":241.89,\"close\":243.36,\"adjusted_close\":243.0927,\"volume\":40244100},"
                + "{\"date\":\"2025-01-06\",\"open\":244.31,\"high\":247.33,\"low\":243.2,\"close\":245.0,\"adjusted_close\":244.7309,\"volume\":45045600}]";
    
        // Remove brackets from JSON
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        String[] daily_candles = json.split("\\},\\{");
        int number_days = daily_candles.length;
        if(verbose) System.out.printf("Found daily candles\n, starting with %s \n", daily_candles[0]);
        
        

        // Arrays to store extracted values
        LocalDate[] local_dates = new LocalDate[number_days];
        double[] open_values = new double[number_days];
        double[] high_values = new double[number_days];
        double[] low_values = new double[number_days];
        double[] close_values = new double[number_days];
        double[] adjusted_close_values = new double[number_days];
        long[] volume_values = new long[number_days];


        // Process each day's data
        for (int i = 0; i < number_days; i++) {
            String[] key_values = daily_candles[i].replace("{", "").replace("}", "").split(",");
            
            for (String key_value : key_values) {
                String[] pair = key_value.split(":");
                String key = pair[0].replaceAll("[\"{}]", "").trim();
                String value = pair[1].replaceAll("[\"{}]", "").trim();

                // Parse values based on key
                switch (key) {
                    case "date":
                        local_dates[i] = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
                        break;
                    case "open":
                        open_values[i] = Double.parseDouble(value);
                        break;
                    case "high":
                        high_values[i] = Double.parseDouble(value);
                        break;
                    case "low":
                        low_values[i] = Double.parseDouble(value);
                        break;
                    case "close":
                        close_values[i] = Double.parseDouble(value);
                        break;
                    case "adjusted_close":
                        adjusted_close_values[i] = Double.parseDouble(value);
                        break;
                    case "volume":
                        volume_values[i] = Long.parseLong(value);
                        break;
                }
            }
        }

        // Print extracted values
        System.out.println("Extracted Data:");
        for (int i = 0; i < number_days; i++) {
            System.out.printf("Date: %s | Open: %.2f | High: %.2f | Low: %.2f | Close: %.2f | Adjusted Close: %.4f | Volume: %d%n", 
                    local_dates[i], open_values[i], high_values[i], low_values[i], close_values[i], adjusted_close_values[i], volume_values[i]);
        }
        double close_min=close_values[0];
        double close_max=close_values[0];
        long volume_min = volume_values[0];
        long volume_max = volume_values[0];
        double close_at_volume_min = close_values[0];
        double close_at_volume_max = close_values[0];

        // add below to compute:
        // the overall minimum and maximum close prices, and volumes
        // the close price at the respective time the maximum or minimum volume occured

        for(int i=0;i<number_days;i++) {
 
        }        	
         	
        // results 
        System.out.printf("Close min: %.2f, close max: %.2f\n", close_min, close_max);
        System.out.printf("Volume min: %d, volume max: %d\n", volume_min, volume_max);
        System.out.printf("Close at volume min: %.2f, close at volume max: %.2f\n", close_at_volume_min, close_at_volume_max);
    }
}

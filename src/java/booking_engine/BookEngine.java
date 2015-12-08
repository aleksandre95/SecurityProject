package booking_engine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Booking;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BookEngine {
    
    private boolean valid;
    
    public boolean book(Booking b) throws IOException{
        JsonObject json = new JsonObject();
        
        json.addProperty("flightID", b.getFlightID());
        json.addProperty("numberOfSeats", b.getNumberOfSeats());
        json.addProperty("ReserveeName", b.getReserveeName());
        json.addProperty("ReservePhone", b.getReservePhone());
        json.addProperty("ReserveEmail", b.getReserveEmail());
        
        JsonArray jArray = new JsonArray();
        for(int i=0; i<b.getPassengers().size(); i++) {
            JsonObject passenger = new JsonObject();
            String[] split = b.getPassengers().get(i).split("_");
            passenger.addProperty("firstName", split[0]);
            passenger.addProperty("lastName", split[1]);
                    jArray.add(passenger);

        }
        json.add("Passsengers", jArray);
        
        String url = b.getAirline().getUrl() + "/api/flightreservation";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty("Content-Type", "application/json;");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Method", "POST");
        con.setDoOutput(true);
        PrintWriter pw = new PrintWriter(con.getOutputStream());
        try (OutputStream os = con.getOutputStream()) {
            os.write(json.toString().getBytes("UTF-8"));
        }
        int HttpResult = con.getResponseCode();
        InputStreamReader is = HttpResult < 400 ? new InputStreamReader(con.getInputStream(), "utf-8")
                : new InputStreamReader(con.getErrorStream(), "utf-8");
        Scanner responseReader = new Scanner(is);
        String response = "";
        while (responseReader.hasNext()) {
            response += responseReader.nextLine() + System.getProperty("line.separator");
        }
        System.out.println(response);
        System.out.println(con.getResponseCode());
        System.out.println(con.getResponseMessage());
        return valid = (con.getResponseCode()==200);
    }
}

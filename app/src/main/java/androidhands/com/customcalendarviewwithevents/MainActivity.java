package androidhands.com.customcalendarviewwithevents;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static JSONArray jsonArray;
    public static JSONObject jsonObject;
    DBOpenHelper dbOpenHelper;
    public static String dateStr;
    public static StringBuilder stringBuilder = new StringBuilder();

    CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateEvents();
        customCalendarView = findViewById(R.id.custom_calendar_view);

    }


    public void updateEvents() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://chennai.vit.ac.in/api/events";
        System.out.println("Came inside updateevents");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                            parseJson(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }


    private void parseJson(JSONObject jsonObject) throws JSONException {
        dbOpenHelper = new DBOpenHelper(getApplicationContext());
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        jsonArray = jsonObject.getJSONArray("Events");
        System.out.println("Came inside parse");
        System.out.println("array length = " + jsonArray.length());
        for (int i = 0; jsonArray.length() > i; i++) {
            System.out.println(i);
            System.out.println("hello");

            JSONObject ob = jsonArray.getJSONObject(i);
            String title = ob.getString("title");

            dateStr = ob.getString("start_date");
            System.out.println(title + " " + dateStr.substring(8) + " " + dateStr.substring(5, 7) + " " + dateStr.substring(0, 4));
            stringBuilder.append(title).append(" ").append(dateStr.substring(8)).append(" ").append(dateStr.substring(5, 7)).append(" ").append(dateStr.substring(0, 4)).append("\n");
            dbOpenHelper.SaveEvent(title.toLowerCase(), "00:00 AM", dateStr.substring(8), dateStr.substring(5, 7), dateStr.substring(0, 4), database);

        }

    }

}


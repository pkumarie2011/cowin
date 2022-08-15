import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*; 
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.awt.*;

public class cowin{
    public static void main(String[] args) {

LocalDateTime myDateObj = LocalDateTime.now();  
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
    String formattedDate = myDateObj.format(myFormatObj);  
    //{"district_id":290,"district_name":"Mandya"}, {"district_id":288,"district_name":"Tumkur"}, {"district_id":271,"district_name":"Chamarajanagar"}, {"district_id":277,"district_name":"Kolar"}, 
//{"district_id":294,"district_name":"BBMP"},{"district_id":276,"district_name":"Bangalore Rural"},{"district_id":265,"district_name":"Bangalore Urban"}

    String[] district_ids = {"294", "276", "265","277","271","288","290"};
//String[] district_ids = {"294"};
int k=0;
while(true)
{    
System.out.println("Searching in "+district_ids[k]); 
try{
TimeUnit.SECONDS.sleep(4);
String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+district_ids[k]+"&date="+formattedDate;

     URL obj = new URL(url);
     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
     // optional default is GET
     con.setRequestMethod("GET");
     //add request header
     con.setRequestProperty("User-Agent", "Mozilla/5.0");
     con.setRequestProperty("Cache-Control", "no-store");

     int responseCode = con.getResponseCode();
     if (responseCode!=200)
	{
	System.out.println("ResponseCode = "+responseCode);
	continue;
	}
     BufferedReader in = new BufferedReader(
             new InputStreamReader(con.getInputStream()));
     String inputLine;
     StringBuffer response = new StringBuffer();
     while ((inputLine = in.readLine()) != null) {
     	response.append(inputLine);
     }
     in.close();

String jsonString = response.toString(); //assign your JSON String here
JSONObject JSONobj = new JSONObject(jsonString);
JSONArray jsonArrayCenters =JSONobj.getJSONArray("centers");

for (int i=0;i<jsonArrayCenters.length();i++)
{
JSONObject JSONobjCenters = new JSONObject(jsonArrayCenters.get(i).toString());
JSONArray jsonArray=JSONobjCenters.getJSONArray("sessions");
if (jsonArray != null) 
	{    
            for (int j=0;j<jsonArray.length();j++)
	    {     
		JSONObject JSONobjSlot = new JSONObject(jsonArray.get(j).toString());
		String vaccine = JSONobjSlot.get("vaccine").toString();
		String min_age_limit =JSONobjSlot.get("min_age_limit").toString();
		String available_capacity_dose1 =JSONobjSlot.get("available_capacity_dose1").toString();
		

if(vaccine.equals("COVAXIN"))
	continue;
if(min_age_limit.equals("45"))
	continue;
if(available_capacity_dose1.equals("0") || available_capacity_dose1.equals("1"))
	continue;
else
	System.out.println("Available Capacity: "+available_capacity_dose1);


String date =JSONobjSlot.get("date").toString();
System.out.println("Date: "+date);
String district_name =JSONobjCenters.get("district_name").toString();
System.out.println("District Name: "+district_name);
String pincode =JSONobjCenters.get("pincode").toString();
System.out.println("Pincode: "+pincode);
String name =JSONobjCenters.get("name").toString();
System.out.println("Name: "+name);

LocalDateTime currentDateTime = LocalDateTime.now();  
DateTimeFormatter newDateTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");  
String formattedCurrentDateTime = currentDateTime.format(newDateTimeFormat);
System.out.println("----------------------Slot Found At Time: "+formattedCurrentDateTime+"----------------------\n");
Toolkit.getDefaultToolkit().beep();  
            }   
        }
}  

    }
    catch(Exception e)
    {
      System.out.println(e);
    }
if(k==district_ids.length-1)
      k=0;
else
      k++;
 }     
    //end
    }
}
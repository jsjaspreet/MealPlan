package appLogic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ExampleDiningHallURLConnection {
	
	public static enum LOCATIONS  { CROSSROADS };
	
	public void LocationScrape(){
		final String baseString = "http://services.housing.berkeley.edu/FoodPro/dining/static/DiningMenus.asp?dtCurDate=<date>&strCurLocation=01&strCurLocationName=<location>";
		
		String CKCmenuURL = baseString.replace("<date>", "9/5/2014");
		CKCmenuURL = CKCmenuURL.replace("<location>", LOCATIONS.CROSSROADS.toString());
		System.out.println(CKCmenuURL);
		
		
		try {
			Document doc = Jsoup.connect(CKCmenuURL).get();
			HashMap<String, String> menuItemsHash = new HashMap<String, String>();
			Elements menuItems = doc.getElementsByAttributeValueContaining("onmouseover", "javascript:openDescWin");
			for(Element e : menuItems){
				String href = e.attr("href").replaceAll("amp;", "");
				menuItemsHash.put(e.text(), href);
			}
			
			System.out.println(menuItemsHash.keySet());
			System.out.println(menuItemsHash.values());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String itemURL = "http://services.housing.berkeley.edu/FoodPro/dining/static/label.asp?locationNum=01&locationName=CROSSROADS&dtdate=9%2F5%2F2014&RecNumAndPort=201040*1";
		JSONObject itemInformation = new JSONObject();
		try{
			Document doc = Jsoup.connect(itemURL).get();
			Elements bElements = doc.getElementsByTag("b");
			
			//Item Name
			Element itemNameElement = doc.getElementsByAttributeValueContaining("style", "color:#0b4499;font-family:Arial").first();
			String name = itemNameElement.text().toString().replaceAll("\u00a0", "").trim();
			itemInformation.put("itemName", name);
			
			//Calorie
//			System.out.println(bElements.get(1));

			//Total Fat
			Element fat = bElements.get(6);
			String val = fat.parent().siblingElements().first().text();
			itemInformation.put("Fat", val);
			System.out.println(itemInformation);
					
			
			
		} catch (IOException e){
			
		}
		

	}

}

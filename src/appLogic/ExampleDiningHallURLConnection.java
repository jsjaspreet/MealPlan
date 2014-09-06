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
	private static HashMap<String, HashMap<String, String>> locationToMenu;
	
	private static HashMap<String, ArrayList<JSONObject>> locationToInfo;
	
	public static enum LOCATIONS {
		CROSSROADS, CAFE, FOOTHILL, CLARK
	};

	public static void LocationScrape(int month, int day, int year, String location) {
		final String baseString = "http://services.housing.berkeley.edu/FoodPro/dining/static/DiningMenus.asp?dtCurDate=<date>&strCurLocation=01&strCurLocationName=<location>";

		String date = month + "/" + day + "/" + year;
		
		String CKCmenuURL = baseString.replace("<date>", date);
		CKCmenuURL = CKCmenuURL.replace("<location>", location);
		//System.out.println(CKCmenuURL);

		try {
			Document doc = Jsoup.connect(CKCmenuURL).get();
			HashMap<String, String> menuItemsHash = new HashMap<String, String>();
			
			ArrayList<JSONObject> itemInfoList = new ArrayList<JSONObject>();
			
			Elements menuItems = doc.getElementsByAttributeValueContaining(
					"onmouseover", "javascript:openDescWin");
			//String stringDoc = doc.toString();
			//System.out.println("stringDoc: " + stringDoc);
			//Elements meals = 
			//System.out.println(meals);
			
			for (Element e : menuItems) {
				String href = e.attr("href").replaceAll("amp;", "");
				href = "http://services.housing.berkeley.edu/FoodPro/dining/static/" + href;
				menuItemsHash.put(e.text(), href);

				
				//System.out.println("href: " + href);
				
				JSONObject nutrition = ItemNutrition(href);
				//System.out.println("nutrition: " + nutrition);
				itemInfoList.add(nutrition);
				//System.out.println("itemInfoList: " + itemInfoList);
			}

			//System.out.println(menuItemsHash.entrySet());
			//System.out.println(itemInfoHash.entrySet());
			locationToMenu.put(location, menuItemsHash);
			locationToInfo.put(location, itemInfoList);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JSONObject ItemNutrition(String itemURL) {
		JSONObject itemInformation = new JSONObject();
		try {
			Document doc = Jsoup.connect(itemURL).get();
			Elements bElements = doc.getElementsByTag("b");

			// Item Name
			Element itemNameElement = doc
					.getElementsByAttributeValueContaining("style",
							"color:#0b4499;font-family:Arial").first();

			//System.out.println(bElements.toString());
			
			String name = itemNameElement.text().toString()
					.replaceAll("\u00a0", "").trim();
			itemInformation.put("itemName", name);

			if (bElements.size() <= 0) {
				return null;
			}
			
			// Calorie
			itemInformation.put("Calorie", bElements.get(1).toString().
						replaceAll("<b>Calories&nbsp;", "").replaceAll("</b>", ""));
			// System.out.println(bElements.get(1));

			// Total Fat
			Element fat = bElements.get(6);
			String val = fat.parent().siblingElements().first().text();
			itemInformation.put("Fat", val);

			// Total Carb
			Element carb = bElements.get(8);
			val = carb.parent().siblingElements().first().text();
			itemInformation.put("Carb", val);

			// Total Cholesterol
			Element chol = bElements.get(15);
			val = chol.parent().siblingElements().first().text();
			itemInformation.put("Cholesterol", val);

			// Total Protein
			Element protein = bElements.get(17);
			val = protein.parent().siblingElements().first().text();
			itemInformation.put("Protein", val);

			// Total Sodium
			Element sodium = bElements.get(19);
			val = sodium.parent().siblingElements().first().text();
			itemInformation.put("Sodium", val);
			//System.out.println(itemInformation);
			return itemInformation;

		} catch (IOException e) {
			e.printStackTrace();
			return null;	
		}
	}
	
	
	
	public static void main(String[] args) {
		locationToMenu = new HashMap<String, HashMap<String, String>>();
		locationToInfo = new HashMap<String, ArrayList<JSONObject>>();

		for (LOCATIONS location : LOCATIONS.values()) {
			LocationScrape(9,5,2014,location.toString());	  
		}
		
		

		System.out.println(locationToMenu);
		System.out.println(locationToInfo);
		
		String itemURL = "http://services.housing.berkeley.edu/FoodPro/dining/static/label.asp?locationNum=01&locationName=CROSSROADS&dtdate=9%2F5%2F2014&RecNumAndPort=201040*1";
		ItemNutrition(itemURL);
	}

}

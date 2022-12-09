package shopdackh.controller.api;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.commons.compress.utils.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CallApi_RS {
	// Hàm thêm parameter
	public static String addParameter(String URL, String name, String value) {

		return URL + name + value;
	}

	public static ArrayList<String> CallApi(int userId) throws IOException {

		try {

			// Viết cứng

			URL url = new URL(addParameter("http://127.0.0.1:12345", "?userId=", Integer.toString(userId)));
			// Tạo và thiết lập request method
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responseCode = conn.getResponseCode();

			// 200  là OK
			if (responseCode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			} else {
				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(url.openStream());

				while (scanner.hasNext()) {
					informationString.append(scanner.nextLine());
				}
				// Close the scanner scanner.close(); System.out.println(informationString);
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(informationString.toString());
				JSONObject jsonObject = (JSONObject) obj;
				ArrayList<String> itemsId = (ArrayList<String>) jsonObject.get("prediction");
				return itemsId;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

package com.xun.qianfanzhiche.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.xun.qianfanzhiche.common.Constant;

public class HttpUtil {

	// HttpUtil工具类，这里暂时只针对图灵的请求模式，所以把url直接写进去了
	public static String httpGetUtil(String query) {
		String requesturl = null;
		try {
			String INFO = URLEncoder.encode(query, "utf-8");
			// 如果在请求的时候已经拿到了地址就把地址扔到请求参数里面去，因为有些query需要地址信息
			requesturl = "http://www.tuling123.com/openapi/api?key=" + Constant.TURING_KEY + "&info=" + INFO;
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(requesturl);
			HttpResponse response = client.execute(request);

			// 200即正确的返回码
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, byte[] httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", Constant.SHITU_API_KEY);
			connection.setDoOutput(true);
			connection.getOutputStream().write(httpArg);
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

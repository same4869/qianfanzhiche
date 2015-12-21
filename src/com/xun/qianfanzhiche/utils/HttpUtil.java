package com.xun.qianfanzhiche.utils;

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
}

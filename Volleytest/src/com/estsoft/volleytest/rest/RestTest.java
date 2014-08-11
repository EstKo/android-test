package com.estsoft.volleytest.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estsoft.volleytest.ArcodeModel;

public class RestTest {
	private static RestTest sharedApi = null;	
	private RequestQueue mRequestQueue;
	
	public static RestTest getSharedApi(Context context)  {
		if (sharedApi == null) {
			sharedApi = new RestTest(context);
		}
		return sharedApi;
	}

	private RestTest(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
		mRequestQueue.start();

	}
	
	public void requestCode(String arname ,final Listener<ArrayList<ArcodeModel>> success, final ErrorListener fail) {
		
		try {
			arname = URLEncoder.encode(arname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String url = "http://api.childcare.go.kr/mediate/rest/cpmsapi020/cpmsapi020/request?key=3fbd781406194c27b6bb1ae2d6e4cc6f&arname=" + arname;
		
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				ArrayList<ArcodeModel> resultArray = new ArrayList<ArcodeModel>();
				ArcodeModel arcode = null;
				XmlPullParserFactory factory;
				try {
					factory = XmlPullParserFactory.newInstance();
					XmlPullParser parser = factory.newPullParser();
					parser.setInput(new StringReader(arg0));
					
					int eventType = parser.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case  XmlPullParser.END_DOCUMENT:
						break;
						case XmlPullParser.START_TAG:
							String tagName = parser.getName();
							
							if	(tagName.equals("item"))
								arcode = new ArcodeModel();
							if (tagName.equals("sidoname"))
								arcode.sidoname = parser.nextText();
							if (tagName.equals("sigunname"))
								arcode.sigunname = parser.nextText();
							if (tagName.equals("arcode"))
								arcode.arcode = parser.nextText();		
							break;
							
						case XmlPullParser.END_TAG:
							if	(arcode != null)
								resultArray.add(arcode);
							break;

						default:
							break;
						}
						eventType = parser.next();
					}
					success.onResponse(resultArray);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					fail.onErrorResponse(null);
				} catch (IOException e) {
					e.printStackTrace();
					fail.onErrorResponse(null);
				}
			}
		};

		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				fail.onErrorResponse(arg0);
			}
		};
		
		StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener );
		mRequestQueue.add(request);
	}
}

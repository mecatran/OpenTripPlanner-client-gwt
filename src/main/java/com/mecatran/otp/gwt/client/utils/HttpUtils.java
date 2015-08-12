/* -------------------------------------------------------------------------
    OpenTripPlanner GWT Client
    Copyright (C) 2015 Mecatran - info@mecatran.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   ------------------------------------------------------------------------- */
package com.mecatran.otp.gwt.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class HttpUtils {

	public static final int DEFAULT_TIMEOUT_MS = 10000;

	public interface DownloadListener<T> {

		public void onSuccess(T t);

		public void onFailure(String msg);
	};

	private interface DataConverter<T> {
		public T convert(String data) throws Exception;
	}

	public static <T extends JavaScriptObject> void downloadJson(String url,
			final DownloadListener<T> listener) {
		downloadJson(url, listener, DEFAULT_TIMEOUT_MS);
	}

	public static <T extends JavaScriptObject> void downloadJson(String url,
			final DownloadListener<T> listener, int timeoutMs) {
		downloadData(url, "application/json", listener, new DataConverter<T>() {
			@Override
			public T convert(String data) {
				return JsonUtils.<T> safeEval(data);
			}
		}, timeoutMs);
	}

	public static <T> void downloadData(String url, String contentType,
			final DownloadListener<T> listener,
			final DataConverter<T> converter, int timeoutMs) {
		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
			builder.setTimeoutMillis(timeoutMs);
			// TODO: Forcing the content-type make certain API fails...
			// builder.setHeader("Content-type", contentType);
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					listener.onFailure(exception.getLocalizedMessage());
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						try {
							T t = converter.convert(response.getText());
							listener.onSuccess(t);
						} catch (Exception e) {
							listener.onFailure(e.getLocalizedMessage());
						}
					} else {
						listener.onFailure(response.getStatusText());
					}
				}
			});
		} catch (RequestException e1) {
			listener.onFailure(e1.getLocalizedMessage());
		}
	}
}

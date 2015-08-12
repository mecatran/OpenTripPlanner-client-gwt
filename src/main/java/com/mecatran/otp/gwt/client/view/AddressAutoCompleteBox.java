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
package com.mecatran.otp.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy.GeocoderListener;

/**
 * An implementation of an AddressBox with auto-completion on input.
 * 
 * Drawback: load heavily the geocoder as up to one request can be made per user
 * keystroke.
 * 
 */
public class AddressAutoCompleteBox extends Composite implements AddressBox {

	private GeocodingOracle oracle;
	private SuggestBox suggestBox;
	private LocationBean location;
	private AddressSuggestBoxListener listener;

	public AddressAutoCompleteBox() {
		oracle = new GeocodingOracle();
		suggestBox = new SuggestBox(oracle);
		suggestBox.setAutoSelectEnabled(false);
		suggestBox.setText("");
		suggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (location != null)
					location.setLocation(null);
				// TODO ???
			}
		});
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				GeocodedAddressSuggestion geocodedSuggestion = (GeocodedAddressSuggestion) event
						.getSelectedItem();
				location = geocodedSuggestion.getLocation();
				listener.onLocationSelected(location);
			}
		});
		suggestBox.getValueBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				listener.onFocus();
			}
		});
		HorizontalPanel rootPanel = new HorizontalPanel();
		rootPanel.add(suggestBox);
		initWidget(rootPanel);
	}

	@Override
	public void setAddressSuggestBoxListener(AddressSuggestBoxListener listener) {
		this.listener = listener;
	}

	@Override
	public LocationBean getLocation() {
		if (location == null)
			location = new LocationBean();
		location.setAddress(suggestBox.getText());
		return location;
	}

	@Override
	public void setLocation(LocationBean location) {
		this.location = location;
		suggestBox.setText(location.getAddress());
	}

	@Override
	public void setGeocoderProxy(GeocoderProxy geocoderProxy) {
		oracle.setGeocoderProxy(geocoderProxy);
	}

	@Override
	public Widget getAsWidget() {
		return this;
	}

	private static class GeocodingOracle extends SuggestOracle {

		private GeocoderProxy geocoderProxy;

		public GeocodingOracle() {
		}

		public void setGeocoderProxy(GeocoderProxy geocoderProxy) {
			this.geocoderProxy = geocoderProxy;
		}

		@Override
		public void requestSuggestions(final Request request,
				final Callback callback) {
			if (geocoderProxy == null)
				return;
			geocoderProxy.geocode(request.getQuery(), new GeocoderListener() {

				@Override
				public void onGeocodingDone(List<LocationBean> locations) {
					Response response = new Response();
					List<Suggestion> suggestions = new ArrayList<Suggestion>();
					if (locations != null) {
						int n = 0;
						for (LocationBean location : locations) {
							if (n >= request.getLimit())
								break;
							suggestions.add(new GeocodedAddressSuggestion(
									location));
							n++;
						}
						if (locations.size() > n)
							response.setMoreSuggestionsCount(locations.size()
									- n);
					}
					response.setSuggestions(suggestions);
					callback.onSuggestionsReady(request, response);
				}
			});
		}
	}

	private static class GeocodedAddressSuggestion implements Suggestion {

		private LocationBean location;

		public GeocodedAddressSuggestion(LocationBean location) {
			this.location = location;
		}

		@Override
		public String getDisplayString() {
			return location.getAddress();
		}

		@Override
		public String getReplacementString() {
			return getDisplayString();
		}

		public LocationBean getLocation() {
			return location;
		}
	}

}

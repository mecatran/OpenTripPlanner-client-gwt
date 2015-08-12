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

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy.GeocoderListener;

/**
 * Implementation of an AddressBox which call the geocoder only on component
 * blur and/or input timeout. If multiple results are found ask for the best
 * one.
 * 
 */
public class AddressProposalBox extends Composite implements AddressBox {

	private TextBox textBox;
	private Button clearButton;
	private VerticalPanel proposalPanel;
	private DialogBox proposalDialog;
	private LocationBean location;
	private AddressSuggestBoxListener listener;
	private GeocoderProxy geocoderProxy;
	private int geocodeRecursiveMutex = 0;
	private Timer geocodeTimer;
	private boolean modifiedSinceGeocoding = true;

	public AddressProposalBox() {
		geocodeTimer = new Timer() {
			@Override
			public void run() {
				if (!modifiedSinceGeocoding)
					return;
				modifiedSinceGeocoding = false;
				if (!textBox.getText().isEmpty()
						&& location.getLocation() == null) {
					geocode();
				}
			}
		};

		location = new LocationBean();
		VerticalPanel rootPanel = new VerticalPanel();
		HorizontalPanel inputPanel = new HorizontalPanel();
		rootPanel.add(inputPanel);
		inputPanel.setCellWidth(inputPanel, "100%");
		textBox = new TextBox();
		textBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				location.setAddress(textBox.getText());
				location.setLocation(null);
				if (textBox.getText().isEmpty())
					listener.onLocationSelected(location);
			}
		});
		textBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				modifiedSinceGeocoding = true;
				location.setLocation(null);
				geocodeTimer.cancel();
				if (textBox.getText().length() > 10) {
					geocodeTimer.schedule(3000);
				}
			}
		});
		textBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				listener.onFocus();
			}
		});
		textBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				geocodeTimer.cancel();
				geocodeTimer.schedule(100);
			}
		});
		inputPanel.add(textBox);
		clearButton = new Button("X");
		inputPanel.setCellWidth(inputPanel, "auto");
		clearButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				geocodeTimer.cancel();
				textBox.setText("");
				location.setAddress("");
				location.setLocation(null);
				listener.onLocationSelected(location);
			}
		});
		inputPanel.add(clearButton);
		proposalPanel = new VerticalPanel();
		initWidget(rootPanel);
	}

	@Override
	public void setAddressSuggestBoxListener(AddressSuggestBoxListener listener) {
		this.listener = listener;
	}

	@Override
	public LocationBean getLocation() {
		location.setAddress(textBox.getText());
		return location;
	}

	@Override
	public void setLocation(LocationBean location) {
		this.location = location;
		if (location.getAddress() != null && !location.getAddress().isEmpty()) {
			textBox.setText(location.getAddress());
			if (location.getLocation() == null && geocodeRecursiveMutex == 0) {
				geocode();
			}
		}
	}

	@Override
	public void setGeocoderProxy(GeocoderProxy geocoderProxy) {
		this.geocoderProxy = geocoderProxy;
	}

	@Override
	public Widget getAsWidget() {
		return this;
	}

	private void geocode() {
		if (geocoderProxy == null)
			return;
		geocoderProxy.geocode(textBox.getText(), new GeocoderListener() {

			@Override
			public void onGeocodingDone(List<LocationBean> locations) {
				proposalPanel.clear();
				if (locations == null || locations.size() == 0) {
					// No answer
					// TODO Set special style name and message
					// Prevent from recursively geocoding for a null location
					geocodeRecursiveMutex++;
					location.setLocation(null);
					listener.onLocationSelected(location);
					geocodeRecursiveMutex--;
					Label noAddressFound = new Label(I18nUtils
							.tr("address.not.found"));
					noAddressFound.addStyleName("warn-icon");
					noAddressFound.addStyleName("no-address-found");
					proposalPanel.add(noAddressFound);
					showProposals();
				} else if (locations.size() == 1) {
					// One answer: take this
					location = locations.get(0);
					textBox.setText(location.getAddress());
					listener.onLocationSelected(location);
				} else {
					// Several answers: make a proposal
					// And wait for the user to select one.
					int n = 0;
					Label severalAddressFound = new Label(I18nUtils
							.tr("several.address.matches"));
					severalAddressFound.addStyleName("several-address-found");
					proposalPanel.add(severalAddressFound);
					for (LocationBean location : locations) {
						if (n >= 10) // TODO Make this parametrable
							break;
						GeocoderAddressProposal gap = new GeocoderAddressProposal(
								location);
						gap.setStyleName("geocoder-address-proposal");
						proposalPanel.add(gap);
						n++;
					}
					showProposals();
				}
			}
		});
	}

	private void showProposals() {
		proposalDialog = new DialogBox();
		proposalDialog.setAnimationEnabled(true);
		VerticalPanel rootPanel = new VerticalPanel();
		proposalDialog.add(rootPanel);
		rootPanel.add(proposalPanel);
		HorizontalPanel buttonPanel = new HorizontalPanel();
		rootPanel.add(buttonPanel);
		Button cancelButton = new Button(I18nUtils.tr("cancel"));
		buttonPanel.add(cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideProposals();
			}
		});
		proposalDialog.setAutoHideEnabled(true);
		proposalDialog.showRelativeTo(getAsWidget());
	}

	private void hideProposals() {
		if (proposalDialog != null) {
			proposalDialog.hide();
			proposalDialog = null;
		}
		proposalPanel.clear();
	}

	private void proposalSelected(LocationBean location) {
		this.location = location;
		textBox.setText(location.getAddress());
		listener.onLocationSelected(this.location);
		hideProposals();
	}

	private class GeocoderAddressProposal extends Composite {

		private LocationBean location;

		public GeocoderAddressProposal(LocationBean aLocation) {
			this.location = aLocation;
			SimplePanel rootPanel = new SimplePanel();
			Anchor link = new Anchor(location.getAddress());
			link.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					listener.onLocationHover(null);
					proposalSelected(GeocoderAddressProposal.this.location);
				}
			});
			link.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					listener.onLocationHover(location);
				}
			});
			link.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					listener.onLocationHover(null);
				}
			});
			rootPanel.add(link);
			initWidget(rootPanel);
		}
	}

}

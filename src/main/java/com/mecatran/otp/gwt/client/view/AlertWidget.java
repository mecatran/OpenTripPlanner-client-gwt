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

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.AlertBean;

/**
 * A widget displaying a single alert, with an optional close button.
 * 
 */
public class AlertWidget extends Composite {

	// TODO Do we really need to print the date range for an itinerary alert?
	private final boolean DISPLAY_ALERT_DATE = false;

	public AlertWidget(AlertBean alert) {
		VerticalPanel rootPanel = new VerticalPanel();

		FocusPanel headerPanel = new FocusPanel();
		rootPanel.add(headerPanel);
		headerPanel.addStyleName("alert-header");
		HorizontalPanel titleAndButtonPanel = new HorizontalPanel();
		headerPanel.add(titleAndButtonPanel);
		Label icon = new Label("");
		icon.addStyleName(alert.getLevel() == AlertBean.LEVEL_INFO ? "info-icon"
				: "warn-icon");
		titleAndButtonPanel.add(icon);
		Label alertTitle = new Label(alert.getTitle());
		alertTitle.addStyleName("alert-title");
		titleAndButtonPanel.add(alertTitle);

		final SimplePanel collapsibleOuterPanel = new SimplePanel();
		rootPanel.add(collapsibleOuterPanel);
		collapsibleOuterPanel.addStyleName("alert-details-outer");
		VerticalPanel collapsibleInnerPanel = new VerticalPanel();
		collapsibleOuterPanel.add(collapsibleInnerPanel);
		collapsibleInnerPanel.addStyleName("alert-details-inner");
		if (DISPLAY_ALERT_DATE && alert.isPublishActiveRange()
				&& (alert.getFrom() != null || alert.getTo() != null)) {
			Label dateRangeLabel = new Label(
					formatDateRange(alert.getFrom(), alert.getTo()));
			collapsibleInnerPanel.add(dateRangeLabel);
			dateRangeLabel.addStyleName("alert-datetime");
		}
		Label descriptionLabel = new Label(alert.getDescription());
		collapsibleInnerPanel.add(descriptionLabel);
		descriptionLabel.addStyleName("alert-description");
		if (alert.getUrl() != null && alert.getUrl().length() > 0) {
			final String url = alert.getUrl();
			Anchor moreInfoAnchor = new Anchor(I18nUtils.tr("more.info.alert"));
			moreInfoAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.open(url, "_blank", "");
				}
			});
			moreInfoAnchor.addStyleName("alert-url");
			collapsibleInnerPanel.add(moreInfoAnchor);
		}
		initWidget(rootPanel);
	}

	private String formatDateRange(Date from, Date to) {
		/*
		 * TODO Make this more human-readable (if from and to are on the same
		 * day, print only once the date; if from and to are today, do not print
		 * the date; etc...)
		 */
		// We assume the user will know the year... an alert is always nearby.
		DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("d MMM hh:mm");
		StringBuffer retval = new StringBuffer();
		if (from != null)
			retval.append(dateTimeFormat.format(from));
		else
			retval.append("...");
		if (from != null || to != null)
			retval.append(" → ");
		if (to != null)
			retval.append(dateTimeFormat.format(to));
		else
			retval.append("...");
		return retval.toString();
	}
}

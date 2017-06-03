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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;

public class AlertStackWidget extends Composite {

	private VerticalPanel alertsPanel;
	private Anchor showHideAnchor;
	private List<AlertWidget> alertWidgets;
	private boolean visible = true;

	public AlertStackWidget() {
		alertWidgets = new ArrayList<AlertWidget>();
		VerticalPanel rootPanel = new VerticalPanel();
		rootPanel.setWidth("100%");
		alertsPanel = new VerticalPanel();
		alertsPanel.setWidth("100%");
		rootPanel.add(alertsPanel);
		showHideAnchor = new Anchor(
				I18nUtils.tr(visible ? "hide.alerts" : "show.alerts"));
		showHideAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				visible = !visible;
				alertsPanel.setVisible(visible);
				showHideAnchor.setText(
						I18nUtils.tr(visible ? "hide.alerts" : "show.alerts"));
			}
		});
		showHideAnchor.addStyleName("alert-showhide-link");
		showHideAnchor.setVisible(false);
		rootPanel.add(showHideAnchor);
		initWidget(rootPanel);
	}

	public void addAlert(AlertWidget alertWidget) {
		alertsPanel.add(alertWidget);
		alertWidgets.add(alertWidget);
		showHideAnchor.setVisible(true);
	}

}

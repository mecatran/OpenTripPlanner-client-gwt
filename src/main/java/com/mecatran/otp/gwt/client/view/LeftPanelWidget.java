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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LeftPanelWidget extends Composite {

	private VerticalPanel verticalPanel;

	public LeftPanelWidget(PlannerFormWidget plannerForm,
			ItineraryStackWidget itineraryStack, AlertStackWidget alertStack,
			boolean useScrollPanel) {
		verticalPanel = new VerticalPanel();
		verticalPanel.add(plannerForm);
		if (useScrollPanel) {
			VerticalPanel innerScrollPanel = new VerticalPanel();
			innerScrollPanel.add(alertStack);
			innerScrollPanel.add(itineraryStack);
			ScrollPanel scrollPanel = new ScrollPanel(innerScrollPanel);
			scrollPanel.setHeight("100%");
			verticalPanel.add(scrollPanel);
			verticalPanel.setCellHeight(scrollPanel, "100%");
		} else {
			verticalPanel.add(alertStack);
			verticalPanel.add(itineraryStack);
		}
		initWidget(verticalPanel);
		setWidth("100%");
		if (useScrollPanel) {
			setHeight("100%");
		}
	}
}

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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ItineraryStackWidget extends Composite {

	private VerticalPanel rootPanel;
	private List<ItineraryWidget> itineraryWidgets;

	public ItineraryStackWidget() {
		itineraryWidgets = new ArrayList<ItineraryWidget>();
		rootPanel = new VerticalPanel();
		rootPanel.setWidth("100%");
		initWidget(rootPanel);
	}

	public void addItineraryWidget(ItineraryWidget itineraryWidget) {
		itineraryWidgets.add(itineraryWidget);
		// Add new results on top of the list.
		rootPanel.insert(itineraryWidget, 0);
	}

	public void removeItineraryWidget(ItineraryWidget itineraryWidget) {
		itineraryWidgets.remove(itineraryWidget);
		rootPanel.remove(itineraryWidget);
	}

	public void removeAllUnpinned() {
		// Hack: Duplicate list to prevent concurrent modification exception
		List<ItineraryWidget> itineraryWidgets2 = new ArrayList<ItineraryWidget>(
				itineraryWidgets);
		for (ItineraryWidget itineraryWidget : itineraryWidgets2) {
			if (!itineraryWidget.isPinned()) {
				removeItineraryWidget(itineraryWidget);
			}
		}
	}
}

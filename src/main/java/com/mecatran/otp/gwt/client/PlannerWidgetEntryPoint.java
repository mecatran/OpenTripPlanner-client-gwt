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
package com.mecatran.otp.gwt.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mecatran.otp.gwt.client.controller.PlannerController;
import com.mecatran.otp.gwt.client.controller.PlannerState;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;

public class PlannerWidgetEntryPoint implements EntryPoint {

	private PlannerWidgetConfig config;

	@Override
	public void onModuleLoad() {

		/* Check for config */
		config = getConfig();
		if (config == null) {
			Window.alert(
					"Unable to find JS object widget config 'otpPlannerWidgetConfig' in host page.");
			return;
		}

		/* Check for root panel where we want to insert the widget. */
		RootPanel mapPanel = RootPanel.get(config.getWidgetDivId());
		if (mapPanel == null) {
			Window.alert("Unable to find <div id='" + config.getWidgetDivId()
					+ "'> in host page.");
			return;
		}

		/* Load javascript libraries. */
		ScriptInjector
				.fromString(PlannerResources.INSTANCE.gwtOpenLayersUtilsJs()
						.getText())
				.setWindow(ScriptInjector.TOP_WINDOW).inject();

		/* Note: must load OSM after OpenLayers */
		ScriptInjector
				.fromString(PlannerResources.INSTANCE.gwtOpenStreetMapJs()
						.getText())
				.setWindow(ScriptInjector.TOP_WINDOW).inject();
		PlannerResources.INSTANCE.css().ensureInjected();

		mapPanel.clear();

		/* Create a controller and get it's widget onto the root panel */
		PlannerController controller = new PlannerController(getConfig());
		mapPanel.add(controller.getPlannerWidget());

		/* If parameters where provided, try to restore state */
		Map<String, List<String>> parameters = Window.Location
				.getParameterMap();
		if (parameters.size() > 0) {
			controller.restoreState(new PlannerState(parameters));
		}

		/* Display dialog box if config has set a message. */
		showIntroDialogBox();
	}

	private void showIntroDialogBox() {
		if (config.getIntroMessage() == null)
			return;
		final DialogBox dialogBox = new DialogBox(true, true);
		VerticalPanel dialogBoxContents = new VerticalPanel();
		dialogBox.setText(I18nUtils.tr("welcome"));
		HTML message = new HTML(config.getIntroMessage());
		Button button = new Button(I18nUtils.tr("ok"), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogBox.setWidth("400px");
		dialogBoxContents.add(message);
		dialogBoxContents.add(button);
		dialogBox.setWidget(dialogBoxContents);
		dialogBox.center();
	}

	private native PlannerWidgetConfig getConfig()
	/*-{
		return $wnd.otpPlannerWidgetConfig;
	}-*/;
}
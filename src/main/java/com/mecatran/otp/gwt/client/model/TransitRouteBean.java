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
package com.mecatran.otp.gwt.client.model;

public class TransitRouteBean {

	private String name;
	private String code;
	private String backgroundColor;
	private String foregroundColor;
	private String routeIconUrl;
	private String vehicleIconUrl;
	private String vehicleLocalIconUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		if (code == null)
			return name;
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBackgroundColor() {
		if (backgroundColor == null)
			return "#ffffff";
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getForegroundColor() {
		if (foregroundColor == null)
			return "#000000";
		return foregroundColor;
	}

	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public String getRouteIconUrl() {
		return routeIconUrl;
	}

	public void setRouteIconUrl(String routeIconUrl) {
		this.routeIconUrl = routeIconUrl;
	}

	public String getVehicleIconUrl() {
		return vehicleIconUrl;
	}

	public void setVehicleIconUrl(String vehicleIconUrl) {
		this.vehicleIconUrl = vehicleIconUrl;
	}

	public String getVehicleLocalIconUrl() {
		return vehicleLocalIconUrl;
	}

	public void setVehicleLocalIconUrl(String vehicleLocalIconUrl) {
		this.vehicleLocalIconUrl = vehicleLocalIconUrl;
	}

}

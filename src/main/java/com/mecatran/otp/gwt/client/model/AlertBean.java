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

import java.util.Date;
import java.util.List;

public class AlertBean implements Comparable<AlertBean> {

	public static final int LEVEL_INFO = 1;
	public static final int LEVEL_WARN = 2;
	public static final int LEVEL_CRIT = 3;

	private String id;
	private String title;
	private String description;
	private String url;
	private Date from;
	private Date to;
	private int level;
	private boolean publishActiveRange;

	private List<TransitAgencyBean> agencies;
	private List<TransitRouteBean> routes;
	private List<TransitStopBean> stops;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isPublishActiveRange() {
		return publishActiveRange;
	}

	public void setPublishActiveRange(boolean publishActiveRange) {
		this.publishActiveRange = publishActiveRange;
	}

	public List<TransitAgencyBean> getAgencies() {
		return agencies;
	}

	public void setAgencies(List<TransitAgencyBean> agencies) {
		this.agencies = agencies;
	}

	public List<TransitRouteBean> getRoutes() {
		return routes;
	}

	public void setRoutes(List<TransitRouteBean> routes) {
		this.routes = routes;
	}

	public List<TransitStopBean> getStops() {
		return stops;
	}

	public void setStops(List<TransitStopBean> stops) {
		this.stops = stops;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof AlertBean))
			return false;
		return getId().equals(((AlertBean) other).getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public int compareTo(AlertBean o) {
		int cmp = getLevel() - o.getLevel();
		if (cmp == 0) {
			cmp = getTitle().compareTo(o.getTitle());
		}
		return cmp;
	}

}

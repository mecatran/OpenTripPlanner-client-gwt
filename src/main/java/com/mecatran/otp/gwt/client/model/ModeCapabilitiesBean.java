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

public class ModeCapabilitiesBean {

	private boolean hasAdvancedOptions;
	private boolean hasWalkOnly;
	private boolean hasBikeOnly;
	private boolean hasTransit;
	private boolean hasBikeAndTransit;
	private boolean hasBikeRental;

	public ModeCapabilitiesBean() {
	}

	public boolean isHasAdvancedOptions() {
		return hasAdvancedOptions;
	}

	public void setHasAdvancedOptions(boolean hasAdvancedOptions) {
		this.hasAdvancedOptions = hasAdvancedOptions;
	}

	public boolean isHasWalkOnly() {
		return hasWalkOnly;
	}

	public void setHasWalkOnly(boolean hasWalkOnly) {
		this.hasWalkOnly = hasWalkOnly;
	}

	public boolean isHasBikeOnly() {
		return hasBikeOnly;
	}

	public void setHasBikeOnly(boolean hasBikeOnly) {
		this.hasBikeOnly = hasBikeOnly;
	}

	public boolean isHasTransit() {
		return hasTransit;
	}

	public void setHasTransit(boolean hasTransit) {
		this.hasTransit = hasTransit;
	}

	public boolean isHasBikeAndTransit() {
		return hasBikeAndTransit;
	}

	public void setHasBikeAndTransit(boolean hasBikeAndTransit) {
		this.hasBikeAndTransit = hasBikeAndTransit;
	}

	public boolean isHasBikeRental() {
		return hasBikeRental;
	}

	public void setHasBikeRental(boolean hasBikeRental) {
		this.hasBikeRental = hasBikeRental;
	}

}

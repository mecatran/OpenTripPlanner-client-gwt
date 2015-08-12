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
import java.util.Set;

import com.mecatran.otp.gwt.client.model.TransportMode;

public interface ModeSelectorWidget {

	public interface ModeSelectorListener {

		public void onTransportModeChange(Set<TransportMode> modes);

	}

	public abstract void setModeSelectorListener(
			ModeSelectorListener modeSelectorListener);

	public abstract Set<TransportMode> getSelectedModes();

	public abstract boolean isDateDeparture();

	public abstract void setDateDeparture(boolean dateDeparture);

	public abstract Date getDateTime();

	public abstract void setDateTime(Date dateTime);

	public abstract void setModes(Set<TransportMode> modes);

	public abstract void setWheelchairAccessible(boolean wheelchairAccessible);

	public abstract int getMaxWalkDistanceMeters();
	
	public abstract float getWalkReluctanceFactor();

	public abstract boolean isWheelchairAccessible();

	public abstract float getWalkSpeedKph();

	public abstract float getBikeSpeedKph();

	public abstract int getTransferPenaltySeconds();

	public abstract float getBikeSpeedFactor();

	public abstract float getBikeSafetyFactor();

	public abstract float getBikeComfortFactor();
}

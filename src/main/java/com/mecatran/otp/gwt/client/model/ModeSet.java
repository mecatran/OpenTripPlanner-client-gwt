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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ModeSet {

	private String humanName;
	private Set<TransportMode> modes;

	public ModeSet(String humanName, TransportMode... modes) {
		this.humanName = humanName;
		this.modes = new HashSet<TransportMode>();
		for (TransportMode mode : modes) {
			this.modes.add(mode);
		}
	}

	public void addMode(TransportMode mode) {
		this.modes.add(mode);
	}

	public void setModes(Collection<TransportMode> modes) {
		this.modes.clear();
		this.modes.addAll(modes);
	}

	public Set<TransportMode> getModes() {
		return modes;
	}

	public boolean hasMode(TransportMode mode) {
		return modes.contains(mode);
	}

	public void setHumanName(String humanName) {
		this.humanName = humanName;
	}

	public String getHumanName() {
		return humanName;
	}

}

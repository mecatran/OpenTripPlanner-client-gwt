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

import com.google.gwt.user.client.ui.ListBox;

class ValueItem<T> {
	private T value;
	private String description;

	public ValueItem(String description, T value) {
		this.value = value;
		this.description = description;
	}

	public T getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
}

/**
 * A ValueListBox, but easier to use (no need for renderer, user give a string
 * description for each value).
 * 
 * @param <T>
 *            The value class.
 */
public class ExtValueListBox<T> extends ListBox {

	private List<ValueItem<T>> valueItems;

	public ExtValueListBox() {
		valueItems = new ArrayList<ValueItem<T>>();
	}

	@Override
	public void addItem(String description) {
		throw new UnsupportedOperationException(
				"Please use addItem(String, T) instead.");
	}

	public void addItem(String description, T value) {
		valueItems.add(new ValueItem<T>(description, value));
		super.addItem(description);
	}

	public void setSelectedValue(T value) {
		int i = 0;
		for (ValueItem<T> item : valueItems) {
			if (value.equals(item.getValue())) {
				setSelectedIndex(i);
				return;
			}
			i++;
		}
	}

	public T getSelectedVal() {
		int index = super.getSelectedIndex();
		if (index >= 0 && index < valueItems.size())
			return valueItems.get(super.getSelectedIndex()).getValue();
		return null;
	}

	public T getExtValue(int index) {
		if (index >= 0 && index < valueItems.size())
			return valueItems.get(index).getValue();
		throw new IndexOutOfBoundsException(
				index + " out of bounds [0.." + valueItems.size() + "]");
	}

	@Override
	public void clear() {
		super.clear();
		valueItems.clear();
	}
}

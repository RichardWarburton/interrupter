/*
    Copyright 2009 Richard Warburton (richard.warburton@gmail.com)
    This file is part of Interrupter.

    Interrupter is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Interrupter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Interrupter.  If not, see <http://www.gnu.org/licenses/>.
 */
package interrupter;

import java.util.HashMap;
import java.util.Map;

public class Interrupter {

	private static Map<Integer, Integer> lookup = new HashMap<Integer, Integer>();

	public static synchronized void setLimit(final int id, final int value) {
		lookup.put(id, value);
	}

	public static void interrupt(final int id) {
		try {
			final int value = lookup.get(id) - 1;
			if (value <= 0) {
				throw new InterruptingError();
			}
			lookup.put(id, value);
		} catch (final NullPointerException e) {
			throw new UnknownIdException(id);
		}
	}

}

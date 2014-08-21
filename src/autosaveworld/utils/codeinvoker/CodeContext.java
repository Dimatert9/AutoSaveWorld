/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package autosaveworld.utils.codeinvoker;

import java.util.HashMap;
import java.util.LinkedList;

public class CodeContext {

	protected Class<?> usedclass;
	protected Object returnedobject;
	protected HashMap<String, Object> objectsrefs = new HashMap<String, Object>();

	protected Object[] getObjects(String objectsstring) {
		if (objectsstring == null) {
			return new Object[0];
		}
		LinkedList<Object> objects = new LinkedList<Object>();
		String[] split = objectsstring.split("[|]");
		for (String obj : split) {
			objects.add(parseObject(obj));
		}
		return objects.toArray();
	}

	private Object parseObject(String object) {
		String[] split = object.split("[:]");
		switch (split[0].toUpperCase()) {
			case "STRING": {
				return new String(split[1].replace("{VERTBAR}", "|").replace("{SPACE}", " ").replace("{COMMA}", ","));
			}
			case "LONG": {
				return Long.parseLong(split[1]);
			}
			case "INTEGER": {
				return Integer.parseInt(split[1]);
			}
			case "SHORT": {
				return Short.parseShort(split[1]);
			}
			case "BYTE": {
				return Byte.parseByte(split[1]);
			}
			case "DOUBLE": {
				return Double.parseDouble(split[1]);
			}
			case "FLOAT": {
				return Float.parseFloat(split[1]);
			}
			case "BOOLEAN": {
				return Boolean.parseBoolean(split[1]);
			}
			case "STATIC": {
				usedclass = getClassInfo(split[1]);
				return null;
			}
			case "CLASS": {
				return getClassInfo(split[1]);
			}
			case "CONTEXT": {
				return objectsrefs.get(split[1]);
			}
			case "NULL": {
				return null;
			}
			case "LAST": {
				return returnedobject;
			}
			default: {
				return new Object();
			}
		}
	}

	protected Class<?> getClassInfo(String classstring) {
		if (classstring.startsWith("CONTEXT:")) {
			return objectsrefs.get(classstring.split("[:]")[1]).getClass();
		}
		if (classstring.equals("LAST")) {
			return returnedobject.getClass();
		}
		switch (classstring) {
			case "long" : case "long.class": {
				return Long.TYPE;
			}
			case "int" : case "int.class": {
				return Integer.TYPE;
			}
			case "short" : case "short.class": {
				return Short.TYPE;
			}
			case "byte" : case "byte.class": {
				return Byte.TYPE;
			}
			case "double" : case "double.class": {
				return Double.TYPE;
			}
			case "float" : case "float.class": {
				return Float.TYPE;
			}
			case "boolean" : case "boolean.class": {
				return Boolean.TYPE;
			}
			default: {
				try {
					return Class.forName(classstring);
				} catch (Exception e) {
					throw new RuntimeException("Can't find class "+classstring);
				}
			}
		}
	}

}

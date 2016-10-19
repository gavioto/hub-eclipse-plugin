package com.blackducksoftware.integration.eclipseplugin.common.services;

import java.util.HashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class PreferenceStoreMock implements IPreferenceStore {

	private final HashMap<String, Boolean> booleanPrefs = new HashMap<String, Boolean>();
	private final HashMap<String, String> stringPrefs = new HashMap<String, String>();
	private final HashMap<String, Double> doublePrefs = new HashMap<String, Double>();
	private final HashMap<String, Integer> intPrefs = new HashMap<String, Integer>();
	private final HashMap<String, Long> longPrefs = new HashMap<String, Long>();
	private final HashMap<String, Float> floatPrefs = new HashMap<String, Float>();
	private final HashMap<String, Boolean> booleanDefaults = new HashMap<String, Boolean>();
	private final HashMap<String, String> stringDefaults = new HashMap<String, String>();
	private final HashMap<String, Double> doubleDefaults = new HashMap<String, Double>();
	private final HashMap<String, Integer> intDefaults = new HashMap<String, Integer>();
	private final HashMap<String, Long> longDefaults = new HashMap<String, Long>();
	private final HashMap<String, Float> floatDefaults = new HashMap<String, Float>();

	@Override
	public void addPropertyChangeListener(final IPropertyChangeListener arg0) {

	}

	@Override
	public boolean contains(final String arg0) {
		return false;
	}

	@Override
	public void firePropertyChangeEvent(final String arg0, final Object arg1, final Object arg2) {

	}

	@Override
	public boolean getBoolean(final String arg0) {
		if (booleanPrefs.containsKey(arg0)) {
			return booleanPrefs.get(arg0);
		} else if (booleanDefaults.containsKey(arg0)) {
			return booleanDefaults.get(arg0);
		} else {
			return false;
		}
	}

	@Override
	public boolean getDefaultBoolean(final String arg0) {
		if (booleanDefaults.containsKey(arg0)) {
			return booleanDefaults.get(arg0);
		} else {
			return false;
		}
	}

	@Override
	public double getDefaultDouble(final String arg0) {
		if (doubleDefaults.containsKey(arg0)) {
			return doubleDefaults.get(arg0);
		} else {
			return Double.MAX_VALUE;
		}
	}

	@Override
	public float getDefaultFloat(final String arg0) {
		if (floatDefaults.containsKey(arg0)) {
			return floatDefaults.get(arg0);
		} else {
			return Float.MAX_VALUE;
		}
	}

	@Override
	public int getDefaultInt(final String arg0) {
		if (intDefaults.containsKey(arg0)) {
			return intDefaults.get(arg0);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public long getDefaultLong(final String arg0) {
		if (longDefaults.containsKey(arg0)) {
			return longDefaults.get(arg0);
		} else {
			return Long.MAX_VALUE;
		}
	}

	@Override
	public String getDefaultString(final String arg0) {
		if (stringDefaults.containsKey(arg0)) {
			return stringDefaults.get(arg0);
		} else {
			return null;
		}
	}

	@Override
	public double getDouble(final String arg0) {
		if (doublePrefs.containsKey(arg0)) {
			return doublePrefs.get(arg0);
		} else if (doubleDefaults.containsKey(arg0)) {
			return doubleDefaults.get(arg0);
		} else {
			return Double.MAX_VALUE;
		}
	}

	@Override
	public float getFloat(final String arg0) {
		if (floatPrefs.containsKey(arg0)) {
			return floatPrefs.get(arg0);
		} else if (floatDefaults.containsKey(arg0)) {
			return floatDefaults.get(arg0);
		} else {
			return Float.MAX_VALUE;
		}
	}

	@Override
	public int getInt(final String arg0) {
		if (intPrefs.containsKey(arg0)) {
			return intPrefs.get(arg0);
		} else if (intDefaults.containsKey(arg0)) {
			return intDefaults.get(arg0);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public long getLong(final String arg0) {
		if (longPrefs.containsKey(arg0)) {
			return longPrefs.get(arg0);
		} else if (longDefaults.containsKey(arg0)) {
			return longDefaults.get(arg0);
		} else {
			return Long.MAX_VALUE;
		}
	}

	@Override
	public String getString(final String arg0) {
		if (stringPrefs.containsKey(arg0)) {
			return stringPrefs.get(arg0);
		} else if (stringDefaults.containsKey(arg0)) {
			return stringDefaults.get(arg0);
		} else {
			return null;
		}
	}

	@Override
	public boolean isDefault(final String arg0) {
		if (booleanDefaults.containsKey(arg0)) {
			if (booleanPrefs.containsKey(arg0)) {
				return booleanPrefs.get(arg0).equals(booleanDefaults.get(arg0));
			}
			return true;
		}
		if (intDefaults.containsKey(arg0)) {
			if (intPrefs.containsKey(arg0)) {
				return intPrefs.get(arg0).equals(intDefaults.get(arg0));
			}
			return true;
		}
		if (longDefaults.containsKey(arg0)) {
			if (longPrefs.containsKey(arg0)) {
				return longPrefs.get(arg0).equals(longDefaults.get(arg0));
			}
			return true;
		}
		if (floatDefaults.containsKey(arg0)) {
			if (floatPrefs.containsKey(arg0)) {
				return floatPrefs.get(arg0).equals(floatDefaults.get(arg0));
			}
			return true;
		}
		if (doubleDefaults.containsKey(arg0)) {
			if (doublePrefs.containsKey(arg0)) {
				return doublePrefs.get(arg0).equals(doubleDefaults.get(arg0));
			}
			return true;
		}
		if (stringDefaults.containsKey(arg0)) {
			if (stringPrefs.containsKey(arg0)) {
				return stringPrefs.get(arg0).equals(stringDefaults.get(arg0));
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean needsSaving() {
		return false;
	}

	@Override
	public void putValue(final String arg0, final String arg1) {
		stringPrefs.put(arg0, arg1);

	}

	@Override
	public void removePropertyChangeListener(final IPropertyChangeListener arg0) {

	}

	@Override
	public void setDefault(final String arg0, final double arg1) {
		doubleDefaults.put(arg0, arg1);

	}

	@Override
	public void setDefault(final String arg0, final float arg1) {
		floatDefaults.put(arg0, arg1);

	}

	@Override
	public void setDefault(final String arg0, final int arg1) {
		intDefaults.put(arg0, arg1);

	}

	@Override
	public void setDefault(final String arg0, final long arg1) {
		longDefaults.put(arg0, arg1);

	}

	@Override
	public void setDefault(final String arg0, final String arg1) {
		stringDefaults.put(arg0, arg1);

	}

	@Override
	public void setDefault(final String arg0, final boolean arg1) {
		booleanDefaults.put(arg0, arg1);

	}

	@Override
	public void setToDefault(final String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(final String arg0, final double arg1) {
		doublePrefs.put(arg0, arg1);

	}

	@Override
	public void setValue(final String arg0, final float arg1) {
		floatPrefs.put(arg0, arg1);

	}

	@Override
	public void setValue(final String arg0, final int arg1) {
		intPrefs.put(arg0, arg1);

	}

	@Override
	public void setValue(final String arg0, final long arg1) {
		longPrefs.put(arg0, arg1);

	}

	@Override
	public void setValue(final String arg0, final String arg1) {
		stringPrefs.put(arg0, arg1);

	}

	@Override
	public void setValue(final String arg0, final boolean arg1) {
		booleanPrefs.put(arg0, arg1);

	}

}

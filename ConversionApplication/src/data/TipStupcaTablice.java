package data;

public class TipStupcaTablice {
	private String field;
	private String type;
	private Boolean canBeNull;
	private KeyType kType;
	private String defaultValue;
	private String extra;

	public enum KeyType {
		Primary, Foreign, Super, Random
	}

	/**
	 * 
	 * @param field
	 *            Naziv stupca
	 * @param type
	 *            Tip stupca
	 * @param canBeNull
	 *            Može biti null
	 * @param kType
	 *            kType
	 * @param defaultValue
	 *            Vrijednost ako se ništa ne unese
	 * @param extra
	 *            Dodatno
	 */
	public TipStupcaTablice(String field, String type, Boolean canBeNull, KeyType kType, String defaultValue,
			String extra) {
		super();
		this.field = field;
		this.type = type;
		this.canBeNull = canBeNull;
		this.kType = kType;
		this.defaultValue = defaultValue;
		this.extra = extra;
	}

	public KeyType getkType() {
		return kType;
	}

	public void setkType(KeyType kType) {
		this.kType = kType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getCanBeNull() {
		return canBeNull;
	}

	public void setCanBeNull(Boolean canBeNull) {
		this.canBeNull = canBeNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}

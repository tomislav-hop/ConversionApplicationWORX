package data;

public class Index {
	// Table Non_unique Key_name Seq_in_index Column_name Collation Cardinality
	// Sub_part Packed Null Index_type Comment Index_comment

	private String table;
	private String non_unique;
	private String key_name;
	private String seq_in_index;
	private String column_name;
	private String collation;
	private String cardinality;
	private String sub_part;
	private String packed;
	private String isNull;
	private String index_type;
	private String comment;
	private String index_comment;

	public Index(String table, String non_unique, String key_name, String seq_in_index, String column_name,
			String collation, String cardinality, String sub_part, String packed, String isNull, String index_type,
			String comment, String index_comment) {
		super();
		this.table = table;
		this.non_unique = non_unique;
		this.key_name = key_name;
		this.seq_in_index = seq_in_index;
		this.column_name = column_name;
		this.collation = collation;
		this.cardinality = cardinality;
		this.sub_part = sub_part;
		this.packed = packed;
		this.isNull = isNull;
		this.index_type = index_type;
		this.comment = comment;
		this.index_comment = index_comment;
	}

	public void ispisIndexa() {
		System.out.println(table + "\t" + non_unique + "\t" + key_name + "\t" + seq_in_index + "\t" + column_name + "\t"
				+ collation + "\t" + cardinality + "\t" + sub_part + "\t" + packed + "\t" + isNull + "\t" + index_type
				+ "\t" + comment + "\t" + index_comment);
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getNon_unique() {
		return non_unique;
	}

	public void setNon_unique(String non_unique) {
		this.non_unique = non_unique;
	}

	public String getKey_name() {
		return key_name;
	}

	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}

	public String getSeq_in_index() {
		return seq_in_index;
	}

	public void setSeq_in_index(String seq_in_index) {
		this.seq_in_index = seq_in_index;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}

	public String getCardinality() {
		return cardinality;
	}

	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public String getSub_part() {
		return sub_part;
	}

	public void setSub_part(String sub_part) {
		this.sub_part = sub_part;
	}

	public String getPacked() {
		return packed;
	}

	public void setPacked(String packed) {
		this.packed = packed;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getIndex_type() {
		return index_type;
	}

	public void setIndex_type(String index_type) {
		this.index_type = index_type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIndex_comment() {
		return index_comment;
	}

	public void setIndex_comment(String index_comment) {
		this.index_comment = index_comment;
	}

}

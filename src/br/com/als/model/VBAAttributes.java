package br.com.als.model;

import br.com.als.vba.util.VBALangUtils;

public class VBAAttributes {
	private String name;
	private String type;

	private String attrAsType;
	private String attrGetter;
	private String attrSetter;

	public VBAAttributes() {
	}

	public VBAAttributes(String name, String type) {
		setName(name);
		setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		createAttrType();
		createAttrGetter();
		createAttrSetter();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		createAttrType();
		createAttrGetter();
		createAttrSetter();
	}

	public String getAttrAsType() {
		return attrAsType;
	}

	public String getAttrGetter() {
		return attrGetter;
	}

	public String getAttrSetter() {
		return attrSetter;
	}

	private void createAttrType() {
		String attrAsType = getName() + " As " + getType() + "\n";
		this.attrAsType = attrAsType;
	}

	private void createAttrGetter() {
		String attrGetterBegin = "Property Get " + getName() + "() As " + getType() + "\n";
		String equals = getName() + " = " + "This." + getName() + "\n";
		String attrGetterEnd = "End Property\n";

		StringBuffer sb = new StringBuffer();
		sb.append(attrGetterBegin);
		sb.append(VBALangUtils.indent());
		sb.append(equals);
		sb.append(attrGetterEnd);

		this.attrGetter = sb.toString();
	}

	private void createAttrSetter() {
		String attrSetterBegin = "Property Let " + getName() + "(Value As " + getType() + ")\n";
		String equals = "This." + getName() + " = Value\n";
		String attrSetterEnd = "End Property\n\n";

		StringBuffer sb = new StringBuffer();
		sb.append(attrSetterBegin);
		sb.append(VBALangUtils.indent());
		sb.append(equals);
		sb.append(attrSetterEnd);

		this.attrSetter = sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VBAAttributes other = (VBAAttributes) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VBAAttributes [name=" + name + ", type=" + type + "]";
	}
}

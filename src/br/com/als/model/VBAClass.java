package br.com.als.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.als.vba.util.VBALangUtils;

public class VBAClass {

	private String name;
	private List<VBAAttributes> attributes = new ArrayList<>();
	private Map<String, String> statements = new HashMap<>();

	public VBAClass() {
	}

	public VBAClass(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VBAAttributes> getAttributes() {
		return attributes;
	}

	public String writeClass() {
		String header = 
				"Attribute VB_Name = \"" + getName() + "\"\r\n"
				+ "Attribute VB_GlobalNameSpace = False\r\n"
				+ "Attribute VB_Creatable = False\r\n"
				+ "Attribute VB_PredeclaredId = False\r\n"
				+ "Attribute VB_Exposed = False\r\n";
		
		statements.put("header", header);
		statements.put("optionExplicit", "Option Explicit\n\n");
		statements.put("mainTypeBegin", "Private Type ClassType\n");
		statements.put("mainTypeEnd", "End Type\n\n");
		statements.put("privateThis", "Private This As ClassType\n\n");

		String attrs = getAttributes().stream()
				.map(attr -> (VBALangUtils.indent() + attr.getAttrAsType()))
				.reduce((attr1, attr2) -> attr1 + attr2).get();
		statements.put("attrsAsType", attrs);
		
		String gettersAndSetters = getAttributes().stream()
				.map(attr -> (attr.getAttrGetter() + attr.getAttrSetter()))
				.reduce((attr1, attr2) -> attr1 + attr2).get();
		statements.put("gettersAndSetters", gettersAndSetters);		

		StringBuffer classAssembler = new StringBuffer();
		classAssembler.append(statements.get("header"));
		classAssembler.append(statements.get("optionExplicit"));
		classAssembler.append(statements.get("mainTypeBegin"));
		classAssembler.append(statements.get("attrsAsType"));
		classAssembler.append(statements.get("mainTypeEnd"));
		classAssembler.append(statements.get("privateThis"));
		classAssembler.append(statements.get("gettersAndSetters"));

		return classAssembler.toString().trim();
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
		VBAClass other = (VBAClass) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}

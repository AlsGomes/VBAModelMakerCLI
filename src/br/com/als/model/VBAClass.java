package br.com.als.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.als.vba.util.VBALangUtils;

public class VBAClass {

	private String name;
	private List<VBAAttribute> attributes = new ArrayList<>();
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

	public List<VBAAttribute> getAttributes() {
		return attributes;
	}

	public String writeClass() {
		String header = 
				"\nAttribute VB_Name = \"" + getName() + "\"\r\n"
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
				.map(attr -> (attr.getAttrGetter() + attr.getAttrSetter() + attr.getAttrGetterJsonName()))
				.reduce((attr1, attr2) -> attr1 + attr2).get();
		statements.put("gettersAndSetters", gettersAndSetters);	
		
		statements.put("toString", getToString());
		
		statements.put("assemblyWith", getAssemblyWith());

		StringBuffer classAssembler = new StringBuffer();
		classAssembler.append(statements.get("header"));
		classAssembler.append(statements.get("optionExplicit"));
		classAssembler.append(statements.get("mainTypeBegin"));
		classAssembler.append(statements.get("attrsAsType"));
		classAssembler.append(statements.get("mainTypeEnd"));
		classAssembler.append(statements.get("privateThis"));
		classAssembler.append(statements.get("gettersAndSetters"));
		classAssembler.append(statements.get("assemblyWith"));
		classAssembler.append(statements.get("toString"));

		return classAssembler.toString().trim();
	}

	private String getAssemblyWith() {
		String assemblyWithBegin = "Public Sub AssemblyWith(Data As Dictionary, PrefixOfKey As String)\n";
		
		String attrAssemblyNotDoubleAndNotString = this.attributes.stream()
				.filter(attr -> (!attr.getType().equalsIgnoreCase("Double") && !attr.getType().equalsIgnoreCase("String")))
				.map(attr -> VBALangUtils.indent() + attr.getName() + " = Data.Item(PrefixOfKey & " + attr.getName() + "JsonName)\n")
				.reduce((attr1, attr2) -> attr1 + attr2)
				.orElse("");

		String attrAssemblyDouble = this.attributes.stream()
				.filter(attr -> attr.getType().equalsIgnoreCase("Double"))
				.map(attr -> assemblyWithAsDouble(attr.getName()))
				.reduce((attr1, attr2) -> attr1 + attr2)
				.orElse("");
		
		String attrAssemblyString = this.attributes.stream()
				.filter(attr -> attr.getType().equalsIgnoreCase("String"))
				.map(attr -> VBALangUtils.indent() + attr.getName() + " = ReformatString(Data.Item(PrefixOfKey & " + attr.getName() + "JsonName))\n")
				.reduce((attr1, attr2) -> attr1 + attr2)
				.orElse("");	
		attrAssemblyString = "\n" + attrAssemblyString;

		String assemblyWithEnd = "End Sub\n";
		
		String assemblyFn = assemblyWithBegin + attrAssemblyNotDoubleAndNotString + attrAssemblyString + attrAssemblyDouble + assemblyWithEnd;
		return assemblyFn;
	}
	
	private String assemblyWithAsDouble(String attrName) {
		String assembly = "\n";
						
		var attrNameAsString = attrName + "String";
		assembly += VBALangUtils.indent() + "Dim " + attrNameAsString + " As String\n";
		assembly += VBALangUtils.indent() + attrNameAsString  + " = Data.Item(PrefixOfKey & " + attrName + "JsonName)\n";
		assembly += VBALangUtils.indent() + attrNameAsString  + " = Replace(" + attrNameAsString + ", \".\", \",\")\n";
		assembly += VBALangUtils.indent() + attrName  + " = CDbl(" + attrNameAsString + ")\n";
		
		return assembly;
	}

	private String getToString() {
		String toStringBegin = 
				"Public Function ToString() As String\n"
				+ VBALangUtils.indent() 
				+ "Dim str As String\n\n"
				+ VBALangUtils.indent()
				+ "str = \"\"\n";
		
		String attrToString = this.attributes.stream()
			.map(attr -> VBALangUtils.indent() + "str = str & \"" + attr.getName() + ":\" & " + attr.getName() + " & vbCr\n")
			.reduce((attr1, attr2) -> attr1 + attr2)
			.get();
		
		String toStringEnd =				
				"\n" 
				+ VBALangUtils.indent() 
				+ "str = ReformatString(str)\n"
				+ VBALangUtils.indent()
				+ "ToString = str\n"
				+ "End Function\n";
		
		String toStringFn = toStringBegin + attrToString + toStringEnd;
	
		return toStringFn;
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

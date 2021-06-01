package io.github.cloudadc.sslo;

public class Topology {
	
	private String name;
	
	private String vlan;
	
	public Topology() {
		super();
	}

	public Topology(String name, String vlan) {
		super();
		this.name = name;
		this.vlan = vlan;
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public String toString() {
		return "Topology [name=" + name + ", vlan=" + vlan + "]";
	}
	

}

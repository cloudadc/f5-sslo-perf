package io.github.cloudadc.sslo;

import java.util.List;

public class Topologies {
	
	private List<Topology> topologies;
	
	private Integer interval;
	
	public Topologies() {
		super();
	}

	public Topologies(List<Topology> topologies, Integer interval) {
		super();
		this.topologies = topologies;
		this.interval = interval;
	}

	public List<Topology> getTopologies() {
		return topologies;
	}

	public void setTopologies(List<Topology> topologies) {
		this.topologies = topologies;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

}

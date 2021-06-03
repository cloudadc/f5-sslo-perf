package io.github.cloudadc.sslo;

import java.util.List;

public class Topologies {
	
	private List<Topology> topologies;
	
	private Integer interval;
	
	private Integer tokenRefreshTimes;
	
	private String ssloHost;
	
	private String biqHost;
	
	private String ssloLoginUser;
	
	private String ssloLoginPassword;
	
	private String biqLoginUser;
	
	private String biqLoginPassword;
	
	public Topologies() {
		super();
	}

	public Topologies(List<Topology> topologies, Integer interval, Integer tokenRefreshTimes, String ssloHost, String biqHost, String ssloLoginUser, String ssloLoginPassword, String biqLoginUser, String biqLoginPassword) {
		super();
		this.topologies = topologies;
		this.interval = interval;
		this.tokenRefreshTimes = tokenRefreshTimes;
		this.ssloHost = ssloHost;
		this.biqHost = biqHost;
		this.ssloLoginUser = ssloLoginUser;
		this.ssloLoginPassword = ssloLoginPassword;
		this.biqLoginUser = biqLoginUser;
		this.biqLoginPassword = biqLoginPassword;
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

	public Integer getTokenRefreshTimes() {
		return tokenRefreshTimes;
	}

	public void setTokenRefreshTimes(Integer tokenRefreshTimes) {
		this.tokenRefreshTimes = tokenRefreshTimes;
	}

	public String getSsloHost() {
		return ssloHost;
	}

	public void setSsloHost(String ssloHost) {
		this.ssloHost = ssloHost;
	}

	public String getBiqHost() {
		return biqHost;
	}

	public void setBiqHost(String biqHost) {
		this.biqHost = biqHost;
	}

	public String getSsloLoginUser() {
		return ssloLoginUser;
	}

	public void setSsloLoginUser(String ssloLoginUser) {
		this.ssloLoginUser = ssloLoginUser;
	}

	public String getSsloLoginPassword() {
		return ssloLoginPassword;
	}

	public void setSsloLoginPassword(String ssloLoginPassword) {
		this.ssloLoginPassword = ssloLoginPassword;
	}

	public String getBiqLoginUser() {
		return biqLoginUser;
	}

	public void setBiqLoginUser(String biqLoginUser) {
		this.biqLoginUser = biqLoginUser;
	}

	public String getBiqLoginPassword() {
		return biqLoginPassword;
	}

	public void setBiqLoginPassword(String biqLoginPassword) {
		this.biqLoginPassword = biqLoginPassword;
	}

}

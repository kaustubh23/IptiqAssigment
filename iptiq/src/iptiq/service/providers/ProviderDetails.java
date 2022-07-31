package iptiq.service.providers;

public class ProviderDetails {

	public Integer health;
	public String response;
	public String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer healthCheckCount=0;


	public ProviderDetails(Integer health, String response) {
		this.health = health;
		this.response = response;
	}

	public Integer getHealth() {
		return health;
	}

	public void setHealth(Integer health) {
		this.health = health;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	public Integer getHealthCheckCount() {
		return healthCheckCount;
	}

	public void setHealthCheckCount(Integer healthCheckCount) {
		this.healthCheckCount = healthCheckCount;
	}

}

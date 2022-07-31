package iptiq.service.loadbalancer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import iptiq.service.loadbalancer.LoadBalancer;
import iptiq.service.providers.ProviderDetails;
import iptiq.service.providers.Providers;

public class RoundRobin implements LoadBalancer {
	private static Integer position = 0;
	private static Integer max_concurrent_calls = 50;

	@Override
	public String get() {
		if (Providers.providers != null && Providers.providers.size() > 0) {
			max_concurrent_calls = max_concurrent_calls * Providers.providers.size();
		}
		Set<String> servers = Providers.providers.keySet();
		List<String> serverList = new ArrayList<>();
		serverList.addAll(servers);
		String target = null;
		String response = "";
		if (!serverList.isEmpty()) {
			Semaphore semaphore = new Semaphore(max_concurrent_calls);

			ProviderDetails providerDetails = null;
			try {
				semaphore.acquire();
				if (position >= serverList.size()-1) {
					position = 0;
				System.out.println("set zero");
				}

				target = serverList.get(position);
				System.out.println("postition "+position);
				position++;
				
				providerDetails = Providers.providers.get(target);
				if (providerDetails != null) {

					response = providerDetails.getResponse();
				}

				semaphore.release();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return response;

	}

	public void check() {

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {

				Providers.providers.forEach((key, value) -> {
					if (value.getHealth() == 0) {
						removeUnhealthyServer(key);

					}
				});

				Providers.removedProviders.forEach((key, value) -> {
					if (value.getHealth() == 1) {
						if (Providers.removedProviders.containsKey(key)) {
							ProviderDetails details = Providers.removedProviders.get(key);
							if (details.getHealthCheckCount() == 2) {
								addToProvidersList(key, details);
								removeActiveServer(key);
							} else {
								details.setHealthCheckCount(details.getHealthCheckCount() + 1);
								Providers.removedProviders.put(key, details);
							}

						}

					}
				});
				printList(3);

			}
		}, 0, 20000);
	}

	public boolean registerProvider(String key, ProviderDetails details) {
		int totalServer = Providers.providers.size() + Providers.removedProviders.size();
		if (totalServer >= 10) {
			return false;
		} else {

			Providers.providers.put(key, details);
			return true;
		}

	}

	public boolean addToProvidersList(String key, ProviderDetails details) {
		if (Providers.providers.size() < 10) {
			Providers.providers.put(key, details);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeUnhealthyServer(String key) {
		if (Providers.providers.containsKey(key)) {
			Providers.removedProviders.put(key, Providers.providers.get(key));
			Providers.providers.remove(key);
			return true;
		} else {
			return false;
		}

	}

	public boolean removeActiveServer(String key) {
		if (Providers.removedProviders.containsKey(key)) {
			Providers.removedProviders.remove(key);
			return true;
		} else {
			return false;
		}

	}

	public boolean removeServer(String key) {
		if (Providers.providers.containsKey(key)) {
			Providers.removedProviders.put(key, Providers.providers.get(key));
			Providers.providers.remove(key);
			return true;
		} else if (Providers.removedProviders.containsKey(key)) {
			// Providers.removedProviders.put(key, Providers.providers.get(key));
			Providers.removedProviders.remove(key);
			return true;

		} else {
			return false;
		}

	}

	public boolean changeServerStatus(String key) {
		if (Providers.removedProviders.containsKey(key)) {
			ProviderDetails details = Providers.removedProviders.get(key);
			details.setHealth(1);
			Providers.removedProviders.put(key, details);
			return true;
		} else {
			return false;
		}

	}

	public boolean setConcurrentCallsLimit(int limit) {
		max_concurrent_calls = limit;
		return true;

	}

	public void printList(int type) {
		if (Providers.providers.size() > 0 || Providers.removedProviders.size() > 0) {
			if (type == 1 || type == 3) {
				String leftAlignFormat = "| %-4s  | %-36s | %-6s  |  %-10s  %n";
				System.out.format("+-------+-------------Active Servers---------+%n");
				System.out.format("+-------+--------------------------------------+---------+---------%n");
				System.out.format("| Key   |          response                    |  Health | Name    |%n");
				System.out.format("+----------------------------------------------+---------+---------|%n");
				Providers.providers.forEach((key, value) -> {

					System.out.format(leftAlignFormat, key, value.getResponse(),
							((value.getHealth() == 1) ? "Active" : "Down"), value.getName());
					System.out.format("+-------+--------------------------------------+---------+---------|%n");

				});

			}
			if (type == 2 || type == 3) {
				String leftAlignFormat2 = "| %-4s  | %-36s | %-6s  |  %-10s  %n";
				System.out.format("+-------+-------------Inactive/Removed Servers---------+%n");
				System.out.format("+-------+--------------------------------------+---------+---------|%n");
				System.out.format("| Key   |          response                    |  Health |  Name   |%n");
				System.out.format("+----------------------------------------------+---------+---------|%n");
				Providers.removedProviders.forEach((key, value) -> {

					System.out.format(leftAlignFormat2, key, value.getResponse(),
							((value.getHealth() == 1) ? "Active" : "Down"), value.getName());
					System.out.format("+-------+--------------------------------------+---------+---------|%n");

				});

			}
		}

	}
}

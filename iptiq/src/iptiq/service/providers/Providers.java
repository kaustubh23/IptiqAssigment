package iptiq.service.providers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Providers {
	public static Map<String, ProviderDetails> providers = new ConcurrentHashMap<>();
	public static Map<String, ProviderDetails> removedProviders = new ConcurrentHashMap<>();
	public static int total_providers = 0;


}
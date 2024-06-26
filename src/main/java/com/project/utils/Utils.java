package com.project.utils;

import com.google.gson.Gson;

public class Utils {

	public static Gson JSON_MAPPER = new Gson();

	public static class TestUtils {
		public static void printSeparator() {
			System.out.println("---------------------------------------------------------------------------------------");
		}
	}
	public static class TimeUtils {
		public static long createLifetimeInSeconds(int seconds) { return createLifetime(seconds,0,0,0); }
		public static long createLifetimeInMinutes(int minutes) {  return createLifetime(0,minutes,0,0); }
		public static long createLifetimeInHours(int hours) { return createLifetime(0,0,hours,0); }
		public static long createLifetimeInDays(int days) { return createLifetime(0,0,0,days); }
		public static long createLifetime(int seconds, int minutes, int hours, int days) {
			long lifetime = 0;
			lifetime += seconds * 1000L;
			lifetime += minutes * 1000L * 60L;
			lifetime += hours * 1000L * 60L * 60L;
			lifetime += days * 1000L * 60L * 60L * 24L;
			return lifetime;
		}
	}
}

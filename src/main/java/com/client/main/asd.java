package com.client.main;

public class asd {

	public static void main(String[] args) {
		String s = "010";
		System.out.println(isNumber(s));
	}

	private static boolean isNumber(String input) {

		char[] inputArray = input.toCharArray();

		for (int idx = 0; idx < inputArray.length; idx++) {
			if ('0' > inputArray[idx] || inputArray[idx] > '9') {
				return false;
			}
		}

		return true;
	}

}

package myjarfiles;

import java.io.IOException;

import myjarfiles.network.Google;

public class Test {

	public static void main(String[] args) throws IOException {
		System.out.println(Google.CalendarList.list());
	}

}

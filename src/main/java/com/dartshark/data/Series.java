package com.dartshark.data;

import java.util.ArrayList;
import java.util.List;

public class Series {
	List<Throw> seriesThrows = new ArrayList<Throw>();
	public Series() {
		
	}
	
	public void addThrow(Throw t) {
		seriesThrows.add(t);
	}
	
}

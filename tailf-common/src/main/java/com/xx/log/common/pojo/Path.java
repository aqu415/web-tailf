package com.xx.log.common.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Path implements Comparable<Path>{
	
	private String name;

	private String path;

	private String isParent;

	@Override
	public int compareTo(Path o) {
		return this.getPath().compareTo(o.getPath());
	}
}

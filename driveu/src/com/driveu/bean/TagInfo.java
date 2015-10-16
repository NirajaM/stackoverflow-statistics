package com.driveu.bean;

/**
 * 
 * @author niraja
 *
 */
public class TagInfo {

	private String tagName;
	private int totalCount;
	private int unansweredCount;
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getUnansweredCount() {
		return unansweredCount;
	}
	
	public void setUnansweredCount(int unansweredCount) {
		this.unansweredCount = unansweredCount;
	}
	
	@Override
	public String toString() {
		return "TagInfo [tagName=" + tagName + ", totalCount=" + totalCount + ", unansweredCount=" + unansweredCount
				+ "]";
	}
}

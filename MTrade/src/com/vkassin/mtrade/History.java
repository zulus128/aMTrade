package com.vkassin.mtrade;

public interface History extends Comparable<History> {

	public String getInstr();
	public String getDirect();
	public String getPrice();
	public String getQty();
	public String getStatus();
	public String getDTime();
	public String getOperationType();
	public Long getLongDTime();
	public Long getSerial();


}

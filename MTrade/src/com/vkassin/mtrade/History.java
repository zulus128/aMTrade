package com.vkassin.mtrade;

import java.util.Date;

public interface History extends Comparable<History> {

	public String getInstr();
	public String getDirect();
	public String getPrice();
	public String getRest();
	public Double getPriceD();
	public String getQty();
	public String getStatus();
	public String getDTime();
	public Date getDTimeD();
	public String getOperationType();
	public Long getLongDTime();
	public Long getSerial();
	public Long getOrdSerial();
	public int getColor();
	public boolean canBeDeleted();

}

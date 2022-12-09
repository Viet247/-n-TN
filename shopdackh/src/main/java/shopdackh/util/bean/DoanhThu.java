package shopdackh.util.bean;


public class DoanhThu {
	
	private String month;
	private long sales;
	
	//Hàm dựng
	public DoanhThu() {
		
	}
	
	public DoanhThu(String month, long sales) {
		super();
		this.month = month;
		this.sales = sales;
	}

	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getSales() {
		return sales;
	}
	public void setSales(long sales) {
		this.sales = sales;
	}

	

}

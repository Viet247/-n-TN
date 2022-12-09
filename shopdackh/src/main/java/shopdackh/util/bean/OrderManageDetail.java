package shopdackh.util.bean;
import shopdackh.model.Product;


public class OrderManageDetail {
	private Product product;
	private int quantity;
	private int price;
	
	//Hàm dựng
	public OrderManageDetail() {
		
	}
	public OrderManageDetail(Product product, int quantity, int price) {
		super();
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}
	
	//Hàm Getter và Setter
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	

}

package shopdackh.util.bean;

import shopdackh.model.Product;

public class CartDetail {

	private Product product;
	private int quantity;

	// Hàm dựng
	public CartDetail() {

	}

	public CartDetail(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

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

}

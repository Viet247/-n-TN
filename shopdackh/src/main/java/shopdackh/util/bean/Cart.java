package shopdackh.util.bean;

import java.util.List;

public class Cart {
	private int userId;
	private List<CartDetail> listCartDetail;
	private int totalQuantity;
	private int totalPrice;

	// Hàm dựng
	public Cart() {

	}

	public Cart(int userId, List<CartDetail> listCartDetail, int totalQuantity, int totalPrice) {
		super();
		this.userId = userId;
		this.listCartDetail = listCartDetail;
		this.totalQuantity = totalQuantity;
		this.totalPrice = totalPrice;
	}

	// Hàm Getter và Setter
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<CartDetail> getListCartDetail() {
		return listCartDetail;
	}

	public void setListCartDetail(List<CartDetail> listCartDetail) {
		this.listCartDetail = listCartDetail;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}

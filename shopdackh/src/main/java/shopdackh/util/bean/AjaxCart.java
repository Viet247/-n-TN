package shopdackh.util.bean;

public class AjaxCart {

	// dùng trong hàm ajax của giỏ hàng: thêm, tăng, giảm, xoá
	private int quantity; // số lượng 1 sản phẩm
	private int totalQuantity; // tổng số lượng sản phẩm
	private int price; // giá bán 1 sản phẩm (theo số lượng)
	private int totalPrice; // tổng giá
	private int error; // 0: không lỗi, 1: có lỗi
	private String errorContent; // nội dung thông báo lỗi (nếu có)

	// Hàm dựng mặc định
	public AjaxCart() {

	}

	// Hàm dựng có tham số

	public AjaxCart(int error) {
		super();
		this.error = error;
	}

	public AjaxCart(int quantity, int totalQuantity, int price, int totalPrice, int error, String errorContent) {
		super();
		this.quantity = quantity;
		this.totalQuantity = totalQuantity;
		this.price = price;
		this.totalPrice = totalPrice;
		this.error = error;
		this.errorContent = errorContent;
	}

	// Hàm Getter và Setter
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getErrorContent() {
		return errorContent;
	}

	public void setErrorContent(String errorContent) {
		this.errorContent = errorContent;
	}

}

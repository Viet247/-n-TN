package shopdackh.util.bean;


public class AjaxStatus {
	// dùng trong các hàm ajax (admin)
	private int code; // 0: thành công, 1: lỗi
	private String content; // nội dung trả về cho view
	
	//Hàm dựng
	public AjaxStatus() {
		
	}
	public AjaxStatus(int code, String content) {
		super();
		this.code = code;
		this.content = content;
	}
	
	//Hàm Getter và Setter
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}

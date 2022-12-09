package shopdackh.util;

public class CheckoutUtil {
public CheckoutUtil() {
	
}
	public static float usd(int vnd) {
		float kq = (float) vnd / 22715;
		return (float) Math.round(kq * 100) / 100;
	}

}

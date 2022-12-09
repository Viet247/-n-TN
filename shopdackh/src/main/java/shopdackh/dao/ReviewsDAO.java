package shopdackh.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.opencsv.CSVWriter;

import shopdackh.constant.GlobalConstant;
import shopdackh.model.Product;
import shopdackh.model.Reviews;
import shopdackh.model.User;

@Repository
public class ReviewsDAO extends AbstractDAO<Reviews> {

	public List<Reviews> getList(int offset, int rowCount) {
		String sql = "SELECT * FROM reviews AS r INNER JOIN product AS p ON r.productId = p.productId"
				+ " INNER JOIN users u ON r.userId = u.userId ORDER BY reviewsId DESC LIMIT ?,?";
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Reviews>>() {
			List<Reviews> list = new ArrayList<Reviews>();

			@Override
			public List<Reviews> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					list.add(new Reviews(rs.getInt("reviewsId"),
							new User(rs.getInt("u.userId"), rs.getString("userFullname"), rs.getString("avatar"),
									rs.getString("username")),
							new Product(rs.getInt("p.productId"), rs.getString("productName")),
							rs.getString("reviewsContent"), rs.getInt("reviewsRating"), rs.getInt("reviewsStatus"),
							rs.getTimestamp("r.createAt"), rs.getTimestamp("r.updateAt")));
				}
				return list;
			}
		}, offset, rowCount);
	}

	public int totalRow() {
		String sql = "SELECT COUNT(*) FROM reviews AS r INNER JOIN product AS p ON r.productId = p.productId"
				+ " INNER JOIN users u ON r.userId = u.userId";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public Reviews findById(int id) {
		try {
			String sql = "SELECT * FROM reviews AS r INNER JOIN product AS p ON r.productId = p.productId"
					+ " INNER JOIN users u ON r.userId = u.userId WHERE reviewsId = ?";
			return jdbcTemplate.query(sql, new ResultSetExtractor<Reviews>() {

				@Override
				public Reviews extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return new Reviews(rs.getInt("reviewsId"),
								new User(rs.getInt("u.userId"), rs.getString("userFullname"), rs.getString("avatar"),
										rs.getString("username")),
								new Product(rs.getInt("p.productId"), rs.getString("productName")),
								rs.getString("reviewsContent"), rs.getInt("reviewsRating"), rs.getInt("reviewsStatus"),
								rs.getTimestamp("r.createAt"), rs.getTimestamp("r.updateAt"));
					}
					return null;
				}
			}, id);
		} catch (Exception e) {
			System.err.println("Error: findById ReviewsDAO");
		}
		return null;
	}

	@Override
	public int save(Reviews reviews) {
		// Ghi thêm user_id, product_id, rating vào file csv đã có
		int u = reviews.getUser().getUserId();
		int p = reviews.getProduct().getProductId();
		int r = reviews.getReviewsRating();
		String csv = "C:/users/84399/eclipse-workspace/kien_thuc-co_ban/data5.csv";

		try {
			FileWriter pw;
			pw = new FileWriter(csv, true);
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(u));
			sb.append(",");
			sb.append(Integer.toString(p));
			sb.append(",");
			sb.append(Integer.toString(r));
			sb.append("\r\n");
			pw.write(sb.toString());
			pw.close();
			System.out.println("finished");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "INSERT INTO reviews(userId,productId,reviewsContent,reviewsRating) VALUES (?,?,?,?)";

		System.out.println(u + " và " + p + " và " + r);
		return jdbcTemplate.update(sql, reviews.getUser().getUserId(), reviews.getProduct().getProductId(),
				reviews.getReviewsContent(), reviews.getReviewsRating());
	}

	@Override
	public int update(Reviews reviews) {
		String sql = "UPDATE reviews SET reviewsContent = ?, reviewsRating = ? WHERE reviewsId = ?";
		return jdbcTemplate.update(sql, reviews.getReviewsContent(), reviews.getReviewsRating(),
				reviews.getReviewsId());
	}

	public int updateStatus(Reviews reviews) {
		String sql = "UPDATE reviews SET reviewsStatus = ? WHERE reviewsId = ?";
		return jdbcTemplate.update(sql, reviews.getReviewsStatus(), reviews.getReviewsId());
	}

	@Override
	public int del(int id) {
		String sql = "DELETE FROM reviews WHERE reviewsId = ?";
		return jdbcTemplate.update(sql, id);
	}

	public int delByProductId(int productId) {
		String sql = "DELETE FROM reviews WHERE productId = ?";
		return jdbcTemplate.update(sql, productId);
	}

	public List<Reviews> findByProductId(int productId, int offset, int rowCount) {
		String sql = "SELECT * FROM reviews r INNER JOIN product p ON r.productId = p.productId"
				+ " INNER JOIN users u ON r.userId = u.userId"
				+ " WHERE r.productId = ? AND reviewsStatus = 1 ORDER BY reviewsId DESC LIMIT ?,?";
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Reviews>>() {
			List<Reviews> list = new ArrayList<Reviews>();

			@Override
			public List<Reviews> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					list.add(new Reviews(rs.getInt("reviewsId"),
							new User(rs.getInt("u.userId"), rs.getString("userFullname"), rs.getString("avatar"),
									rs.getString("username")),
							new Product(rs.getInt("p.productId"), rs.getString("productName")),
							rs.getString("reviewsContent"), rs.getInt("reviewsRating"), rs.getInt("reviewsStatus"),
							rs.getTimestamp("r.createAt"), rs.getTimestamp("r.updateAt")));
				}
				return list;
			}
		}, productId, offset, rowCount);
	}

	public int totalRowByProductId(int productId) {
		String sql = "SELECT COUNT(*) FROM reviews r INNER JOIN product p ON r.productId = p.productId"
				+ " INNER JOIN users u ON r.userId = u.userId WHERE r.productId = ? AND reviewsStatus = 1";
		return jdbcTemplate.queryForObject(sql, Integer.class, productId);
	}

	// trung bình rating của sản phẩm
	public float getRatingAverageByProductId(int productId) {
		try {
			String sql = "SELECT AVG(reviewsRating) FROM reviews WHERE productId = ? AND reviewsStatus = 1 GROUP BY productId";
			return jdbcTemplate.queryForObject(sql, Float.class, productId);
		} catch (Exception e) {
			System.err.println("Error: getRatingAverageByProductId ReviewsDAO");
		}
		return 0.0f;
	}

	// đếm theo rating của sản phẩm (VD: có bao nhiêu rating 5 sao...)
	public int ratingCount(int rating, int productId) {
		try {
			String sql = "SELECT COUNT(*) FROM reviews WHERE productId = ? AND reviewsRating = ? AND reviewsStatus = 1";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, rating);
		} catch (Exception e) {
			System.err.println("Error: ratingCount ReviewsDAO");
		}
		return 0;
	}

	// search
	public List<Reviews> search(String productName, int rating, int status, int offset, int rowCount) {
		List<Object> list = new ArrayList<Object>();
		String sql = "SELECT * FROM reviews r INNER JOIN product p ON r.productId = p.productId INNER JOIN users u ON r.userId = u.userId WHERE 1";
		if (!productName.equals(GlobalConstant.EMPTY)) {
			sql += " AND p.productName LIKE ?";
			list.add("%" + productName + "%");
		}
		if (rating != -1) { // có tìm kiếm theo rating
			sql += " AND reviewsRating = ?";
			list.add(rating);
		}
		if (status != -1) { // có tìm kiếm theo status
			sql += " AND reviewsStatus = ?";
			list.add(status);
		}
		sql += " ORDER BY reviewsId DESC LIMIT ?,?";
		list.add(offset);
		list.add(rowCount);
		Object[] arrObj = new Object[list.size()];
		arrObj = list.toArray();
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Reviews>>() {
			List<Reviews> list = new ArrayList<Reviews>();

			@Override
			public List<Reviews> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					list.add(new Reviews(rs.getInt("reviewsId"),
							new User(rs.getInt("u.userId"), rs.getString("userFullname"), rs.getString("avatar"),
									rs.getString("username")),
							new Product(rs.getInt("p.productId"), rs.getString("productName")),
							rs.getString("reviewsContent"), rs.getInt("reviewsRating"), rs.getInt("reviewsStatus"),
							rs.getTimestamp("r.createAt"), rs.getTimestamp("r.updateAt")));
				}
				return list;
			}
		}, arrObj);
	}

	public int totalRowSearch(String productName, int rating, int status) {
		List<Object> list = new ArrayList<Object>();
		String sql = "SELECT COUNT(*) FROM reviews AS r INNER JOIN product AS p ON r.productId = p.productId INNER JOIN users u ON r.userId = u.userId WHERE 1";
		if (!productName.equals(GlobalConstant.EMPTY)) {
			sql += " AND p.productName LIKE ?";
			list.add("%" + productName + "%");
		}
		if (rating != -1) { // có tìm kiếm theo rating
			sql += " AND reviewsRating = ?";
			list.add(rating);
		}
		if (status != -1) { // có tìm kiếm theo status
			sql += " AND reviewsStatus = ?";
			list.add(status);
		}
		Object[] arrObj = new Object[list.size()];
		arrObj = list.toArray();
		return jdbcTemplate.queryForObject(sql, Integer.class, arrObj);
	}

}

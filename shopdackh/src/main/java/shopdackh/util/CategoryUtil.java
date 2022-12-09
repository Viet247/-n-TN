package shopdackh.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import shopdackh.constant.GlobalConstant;
import shopdackh.model.Category;
import shopdackh.service.CategoryService;

@Component
public class CategoryUtil {

	@Autowired
	private CategoryService categoryService;
	
	//Hàm dựng
	public CategoryUtil() {
		
	}

	public CategoryUtil(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	// Lấy danh sách id danh mục đa cấp
	public List<Integer> findCatIdByParentId(List<Integer> list, int catId) {
		List<Category> listCatCon = categoryService.findCatByParentId(catId, GlobalConstant.DELETE_STATUS_0);
		if (listCatCon.size() > 0) {
			for (Category category : listCatCon) {
				list.add(category.getCatId());
				findCatIdByParentId(list, category.getCatId());
			}
		}
		return list;
	}

	// hiển thị tên danh mục cha (trang admin cat index)
	public String getCatParent(String rs, int parentId) {
		if (parentId == 0) {
			return rs;
		} else {
			Category category = categoryService.findById(parentId, GlobalConstant.DELETE_STATUS_0);
			rs = category.getCatName() + " - " + rs;
			return getCatParent(rs, category.getCatParentId());
		}
	}

	// hiển thị tên danh mục cha (trang admin cat add, update)
	public Category getCatParent(int parentId) {
		Category category = categoryService.findById(parentId, GlobalConstant.DELETE_STATUS_0);
		String catParentName = getCatParent(category.getCatName(), category.getCatParentId());
		category.setCatName(catParentName);
		return category;
	}

	// lấy danh sách danh mục đa cấp
	public List<Category> getListCatParent(List<Category> list, int parentId) {
		if (parentId == 0) {
			return list;
		} else {
			Category category = categoryService.findById(parentId, GlobalConstant.DELETE_STATUS_0);
			if (category != null) {
				list.add(0, category);
			}
			return getListCatParent(list, category.getCatParentId());
		}
	}

	// lấy danh sách danh mục đa cấp => hiển thị public (Vd: Điện thoại, oppo,
	// realme)
	public List<Category> getListCatParent(int catId, Model model) {
		List<Category> listCat = new ArrayList<Category>();
		Category category = categoryService.findById(catId, GlobalConstant.DELETE_STATUS_0);
		model.addAttribute("objCat", category);
		listCat.add(category);
		if (category != null) {
			return getListCatParent(listCat, category.getCatParentId());
		}
		return null;
	}

}

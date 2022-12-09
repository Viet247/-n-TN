package shopdackh.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shopdackh.constant.GlobalConstant;
import shopdackh.constant.URLConstant;
import shopdackh.model.Category;
import shopdackh.service.CategoryService;
import shopdackh.util.CategoryUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(URLConstant.API_CAT)
public class CategoryAPIController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryUtil categoryUtil;

	// API lấy tất cả danh mục
	@GetMapping(GlobalConstant.EMPTY)
	public ResponseEntity<List<Category>> getAll() {
		List<Category> listCat = categoryService.getAll();
		if (listCat.isEmpty()) {
			return new ResponseEntity<List<Category>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Category>>(listCat, HttpStatus.OK);
	}

	// API lấy danh mục cha của 1 danh mục nào đó
	@GetMapping(URLConstant.API_CAT_PARENT)
	public ResponseEntity<Category> getCatParent(@PathVariable int parentId) {
		Category category = categoryUtil.getCatParent(parentId);
		if (category == null) {
			return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}

	// API lấy danh sách danh mục con của danh mục cha => hiển thị danh mục đa cấp
	@GetMapping(URLConstant.API_CAT_BY_PARENT_ID)
	public ResponseEntity<List<Category>> getListCatByParentId(@PathVariable int parentId) {
		List<Category> listCat = categoryService.findCatByParentId(parentId, GlobalConstant.DELETE_STATUS_0);
		if (listCat.isEmpty()) {
			return new ResponseEntity<List<Category>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Category>>(listCat, HttpStatus.OK);
	}

}

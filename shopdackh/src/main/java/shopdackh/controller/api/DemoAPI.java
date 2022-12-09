package shopdackh.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import shopdackh.util.bean.DoanhThu;

@RestController
public class DemoAPI {

	@GetMapping("api/demo")
	public ResponseEntity<List<DoanhThu>> getAll() {
		List<DoanhThu> list = new ArrayList<DoanhThu>();
		list.add(new DoanhThu("Th�ng 1", 24000000));
		list.add(new DoanhThu("Th�ng 2", 200000000));
		list.add(new DoanhThu("Th�ng 3", 120000000));
		list.add(new DoanhThu("Th�ng 4", 90000000));
		list.add(new DoanhThu("Th�ng 5", 60000000));
		list.add(new DoanhThu("Th�ng 6", 45000000));
		list.add(new DoanhThu("Th�ng 7", 85000000));
		list.add(new DoanhThu("Th�ng 8", 70000000));
		list.add(new DoanhThu("Th�ng 9", 124000000));
		list.add(new DoanhThu("Th�ng 10", 300000000));
		list.add(new DoanhThu("Th�ng 11", 40000000));
		list.add(new DoanhThu("Th�ng 12", 65000000));
		if (list.isEmpty()) {
			return new ResponseEntity<List<DoanhThu>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<DoanhThu>>(list, HttpStatus.OK);
	}

}

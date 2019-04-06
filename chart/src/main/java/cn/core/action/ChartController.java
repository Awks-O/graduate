package cn.core.action;

import cn.core.chartbeans.LineBean;
import cn.core.common.beans.ResultBean;
import cn.core.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  上传日志并分析成图表
 *
 * @author 肖文杰 https://github.com/xwjie/
 *
 */
@RequestMapping("/chart")
@RestController
@CrossOrigin
public class ChartController {

	@Autowired
	ChartService chartService;

	@GetMapping("/line")
	public ResultBean<LineBean> line(long uploadRecordId){
		return new ResultBean<LineBean>(chartService.line(uploadRecordId));
	}

}

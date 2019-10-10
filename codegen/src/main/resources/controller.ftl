package ${basePackage}.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;

import com.joyin.ticm.bean.ResultData;
import com.joyin.ticm.page.Pager;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.dto.${modelNameUpperCamel}Dto;


@Controller("${modelNameFirstLower}Action")
@Scope("prototype")
public class ${modelNameUpperCamel}Action extends ActionBase{
	private static final long serialVersionUID = 1L;
	
}


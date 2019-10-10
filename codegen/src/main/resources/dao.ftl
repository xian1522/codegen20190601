package ${basePackage}.dao;

import java.util.List;

import com.joyin.ticm.bean.ResultData;
import com.joyin.ticm.dao.DaoException;
import com.joyin.ticm.page.PageInfo;
import com.joyin.ticm.page.Pager;

import ${basePackage}.model.${upperCamelModel};


public interface ${upperCamelModel}Dao{
	
	public ResultData find${upperCamelModel}OfPage(${upperCamelModel} ${firstLowerModel}, Pager pager,
			String optype, List<String> slDealReqids,List<String> pList) throws DaoException;
}


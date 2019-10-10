package ${basePackage}.service;

import java.util.List;

import com.joyin.ticm.bean.ResultData;
import com.joyin.ticm.page.PageInfo;
import com.joyin.ticm.page.Pager;
import com.joyin.ticm.service.ServiceException;

import ${basePackage}.model.${upperCamelModel};


public interface ${upperCamelModel}Service{
	
	public ${upperCamelModel} findById(String reqid) throws ServiceException;
	
	public ResultData find${upperCamelModel}OfPage(${upperCamelModel} ${firstLowerModel}, Pager pager,
			String optype, List<String> ${firstLowerModel}Reqids,List<String> pList) throws ServiceException;
			
	public ResultData saveAndSubmit(${upperCamelModel} ${firstLowerModel}, boolean isSubmit)
			throws ServiceException;
	
	public ResultData updateAndSubmit(${upperCamelModel} ${firstLowerModel}, boolean isSubmit)
			throws ServiceException;
	
	public ResultData rejectFlow(${upperCamelModel} ${firstLowerModel}) throws ServiceException;
	
	public ResultData saveAccountView(${upperCamelModel} ${firstLowerModel}) throws Exception;
	
	public ResultData delete${upperCamelModel}(List<${upperCamelModel}> ${firstLowerModel}List) throws ServiceException;
	
}


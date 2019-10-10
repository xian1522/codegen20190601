package ${basePackage}.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.joyin.ticm.service.ServiceBase;

import com.joyin.ticm.accmn.kaconfig.service.KaConfigService;
import ${basePackage}.service.${upperCamelModel}Service;
import com.joyin.ticm.common.util.CommonUtil;
import com.joyin.ticm.common.constant.Constant;
import com.joyin.ticm.common.constant.Constant.FlowDoType;
import com.joyin.ticm.common.constant.Constant.FlowStateType;
import com.joyin.ticm.accmn.kamn.kacomm.KaConstant;
import com.joyin.ticm.service.ServiceException;
import com.joyin.ticm.dao.DaoException;
import com.joyin.ticm.bean.ResultData;
import com.joyin.ticm.dao.BaseDao;
import com.joyin.ticm.page.PageInfo;
import com.joyin.ticm.page.Pager;

import ${basePackage}.dao.${upperCamelModel}Dao;
import ${basePackage}.model.${upperCamelModel};

import com.joyin.ticm.workflow.service.FlowProcessService;

@Service("${firstLowerModel}Service")
public class ${upperCamelModel}ServiceImpl extends ServiceBase implements ${upperCamelModel}Service {
	
	@Resource
	private ${upperCamelModel}Dao ${firstLowerModel}Dao;
	@Resource
	private FlowProcessService flowProcessService;
	@Resource
	private KaConfigService kaConfigService;
	@Resource
	private BaseDao baseDao;
	
	@Override
	public ${upperCamelModel} findById(String reqid) throws ServiceException {
		String methodName = "findById";
		info(methodName, "param[reqid]: " + reqid);

		try {
			${upperCamelModel} ${firstLowerModel} = new ${upperCamelModel}();
			${firstLowerModel} = baseDao.findById(${upperCamelModel}.class, reqid);
			return ${firstLowerModel};
		}
		catch (DaoException ex) {
			String message = "查询${noteName}交易错误";
			throw processException(methodName, message, ex);
		}
	}
	
	@Override
	public ResultData find${upperCamelModel}OfPage(${upperCamelModel} ${firstLowerModel}, Pager pager,
			String optype, List<String> ${firstLowerModel}Reqids,List<String> pList) throws ServiceException {
		String methodName = "find${upperCamelModel}OfPage ";
		info(methodName, "查询${noteName}交易信息" + ",params[${firstLowerModel}]=" + ${firstLowerModel}
				+ ",[pager]=" + pager + ",[optype]=" + optype
				+ ",[${firstLowerModel}Reqids]=" + ${firstLowerModel}Reqids);
		ResultData rs = new ResultData();
		if (CommonUtil.isEmpty(pager)) {
			rs.setSuccess(false);
			rs.setResultMessage("分页参数错误");
			return rs;
		}
		if (CommonUtil.isEmpty(${firstLowerModel}) || CommonUtil.isEmpty(optype)) {
			rs.setSuccess(false);
			rs.setResultMessage("查询参数错误");
			return rs;
		}

		try {
			rs = ${firstLowerModel}Dao.find${upperCamelModel}OfPage(${firstLowerModel}, pager, optype, ${firstLowerModel}Reqids, pList);
		}
		catch (DaoException e) {
			throw processException(methodName, e.getMessage(), e);
		}
		return rs;
	}
	
	
	@Override
	public ResultData saveAndSubmit(${upperCamelModel} ${firstLowerModel}, boolean isSubmit)
			throws ServiceException {
		String methodName = "saveOrSubmit";
		info(methodName, "param[${firstLowerModel}]: " + ${firstLowerModel} + " param[isSubmit]: "
				+ isSubmit);

		ResultData rst = new ResultData();
		if (CommonUtil.isEmpty(${firstLowerModel}.getReqid())) {
			String reqid = this.getBusinessId(Constant.BusinessIdType.SL_DEAL,
					${firstLowerModel}.getOrgid());
			${firstLowerModel}.setReqid(reqid);
		}
		${firstLowerModel}.setLinkid(${firstLowerModel}.getReqid());

		// 保存数据
		try {
			if (isSubmit) {
				// 新建并提交流程
				rst = flowProcessService.startProcessTask(
						Constant.FlowKey.SLDEALFLOW, null, ${firstLowerModel});
				if (rst.isSuccess() == false) {
					return rst;
				}
			}
			else {
				// 新建流程
				rst = flowProcessService.startByKey(
						Constant.FlowKey.SLDEALFLOW, null, ${firstLowerModel});
				if (rst.isSuccess() == false) {
					return rst;
				}
			}

			baseDao.saveOrUpdate(${firstLowerModel});
			
			rst.setSuccess(true);
			rst.setObject(${firstLowerModel});
		}
		catch (DaoException ex) {
			String msg = "保存${noteName}交易错误";
			throw processException(methodName, msg, ex);
		}
		catch (ServiceException ex) {
			throw processException(methodName, ex.getMessage(), ex);
		}
		return rst;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResultData updateAndSubmit(${upperCamelModel} ${firstLowerModel}, boolean isSubmit)
			throws ServiceException {
		String methodName = "updateOrSubmit";
		info(methodName, "param[${firstLowerModel}]: " + ${firstLowerModel} + " param[isSubmit]: "
				+ isSubmit);

		ResultData rst = new ResultData();

		// 更新数据
		try {
			if (isSubmit) {
				// 提交流程
				rst = flowProcessService.complateTask(null, ${firstLowerModel});
				if (rst.isSuccess() == false) {
					return rst;
				}
				if (CommonUtil.isNotEmpty(rst.getMap())) {
					String processid = rst.getMap().get(
							FlowDoType.PROCESSID).toString();
					${firstLowerModel}.setJbpmProcessid(processid);
				}
			}
			else {
				// 更新流程名称
				rst = flowProcessService.updateFlowStateName(${firstLowerModel});
				if (rst.isSuccess() == false) {
					return rst;
				}
			}

			// 流程执行后状态
			int flowRstStatus = FlowStateType.NOT_OVER;
			if (null != rst
					&& CommonUtil.isNotEmpty(rst.getResultStatus())) {
				flowRstStatus = rst.getResultStatus();
			}
			
			// 流程结束
			if (flowRstStatus == FlowStateType.FLOW_OVER) {

				// 查询质押品列表
				ResultData resultData = new ResultData();

				${firstLowerModel}.setEffectflag(Constant.EffectFlag.E);


				// 记账
				rst = keepAccount(${firstLowerModel}, KaConstant.VIEW.NOT_VIEW);
				rst.setTaskOrder(FlowStateType.FLOW_OVER);
				
			}

			// 更新业务信息
			baseDao.saveOrUpdate(${firstLowerModel});
			

			rst.setSuccess(true);
		}
		catch (DaoException ex) {
			String msg = "更新并提交${noteName}交易错误.";
			throw processException(methodName, msg, ex);
		}
		catch (ServiceException ex) {
			throw processException(methodName, ex.getMessage(), ex);
		}
		catch (Exception e) {
			throw processException(methodName, e.getMessage(), e);
		}

		return rst;
	}
	
	@Override
	public ResultData rejectFlow(${upperCamelModel} ${firstLowerModel}) throws ServiceException {
		String methodName = "rejectFlow";
		info(methodName, "param[${firstLowerModel}]: " + ${firstLowerModel});

		// 定义返回信息
		ResultData rtData = new ResultData();
		// 更新数据
		try {
			// 流程退回
			ResultData rstFlow = flowProcessService.reject(${firstLowerModel});
			if (rstFlow.isSuccess() == false) {
				return rstFlow;
			}

			baseDao.saveOrUpdate(${firstLowerModel});
			// 成功
			rtData.setSuccess(true);
		}
		catch (DaoException e) {
			String msg = "更新业务数据失败";
			throw processException(methodName, msg, e);
		}
		catch (Exception ex) {
			throw processException(methodName, ex.getMessage(), ex);
		}
		return rtData;
	}
	
	public ResultData keepAccount(${upperCamelModel} ${firstLowerModel}, String isView)
			throws ServiceException {
		String methedName = "keepAccount";
		info(methedName, "param[${firstLowerModel}]" + ${firstLowerModel} + " param[isView]" + isView);

		try {
			ResultData rs = new ResultData();
			
			// 是否预览
			String ifView = KaConstant.VIEW.NOT_VIEW;
			
			/**
			// 账务处理
			${upperCamelModel}KeepAccount ${firstLowerModel}Ka = new ${upperCamelModel}KeepAccount();

			// 记账
			SysKeepAccount sysKeepAccount = new SysKeepAccount();
			sysKeepAccount = ${firstLowerModel}Ka
					.getSysKeepAccount(kaConfigService, ${firstLowerModel});

			// 调用创建方法进行记账
			rs = keepAccountService.createLocal(sysKeepAccount, ifView);

			if (!rs.isSuccess()) {
				throw new ServiceException(
						ServiceException.SYS_KEEPACCOUNT_EXCEPTION, rs
								.getMessageCode());
			}
			*/
			return rs;
		}
		catch (ServiceException e) {
			throw processException(methedName, e.getMessage(), e);
		}
	}
	
	@Override
	public ResultData saveAccountView(${upperCamelModel} ${firstLowerModel})
			throws Exception {
		String methodName = "saveAccountView";
		ResultData rsData = new ResultData();
		if (CommonUtil.isEmpty(${firstLowerModel}.getReqid())) {
			rsData.setSuccess(false);
			rsData.setResultMessage("业务ID为空");
			return rsData;
		}

		try {

			// 是否预览
			String ifView = KaConstant.VIEW.IS_VIEW;
			/**
			// 账务处理
			${upperCamelModel}KeepAccount ${firstLowerModel}Ka = new ${upperCamelModel}KeepAccount();

			// 记账
			SysKeepAccount sysKeepAccount = new SysKeepAccount();
			sysKeepAccount = ${firstLowerModel}Ka
					.getSysKeepAccount(kaConfigService, ${firstLowerModel});

			// 调用创建方法进行记账
			rsData = keepAccountService.createLocal(sysKeepAccount, ifView);
			*/

		}
		catch (ServiceException e) {
			throw processException(methodName, e.getMessage(), e);
		}

		return rsData;
	}
	
	@Override
	public ResultData delete${upperCamelModel}(List<${upperCamelModel}> ${firstLowerModel}List)
			throws ServiceException {
		String methodName = "delete${upperCamelModel}";
		info(methodName, "param[${firstLowerModel}List]: " + ${firstLowerModel}List);

		// 定义返回信息
		ResultData rtData = new ResultData();

		if (null == ${firstLowerModel}List) {
			rtData.setSuccess(false);
			rtData.setResultMessage("参数为空");
			return rtData;
		}
		try {
			for (${upperCamelModel} ${firstLowerModel} : ${firstLowerModel}List) {
				// 调用删除流程
				ResultData rstFlow = flowProcessService
						.deleteProcessInstance(${firstLowerModel});
				if (rstFlow.isSuccess() == false) {
					return rstFlow;
				}

				baseDao.saveOrUpdate(${firstLowerModel});
				rtData.setSuccess(true);
			}
		}
		catch (DaoException ex) {
			throw processException(methodName, "删除${noteName}交易错误", ex);
		}
		catch (ServiceException ex) {
			throw processException(methodName, ex.getMessage(), ex);
		}

		return rtData;
	}
	
	@SuppressWarnings("unchecked")
	public ResultData rubAccountByBusinessNo(String businessNo) throws ServiceException {
        String methodName = "rubAccountByBusinessNo";
        ResultData rd = new ResultData();
        
        
        ${upperCamelModel} ${firstLowerModel} = null;
        try {
        	//查询交易信息
        	${firstLowerModel} = baseDao.findById(${upperCamelModel}.class, businessNo);
        }
    	catch (DaoException e) {
            processException(methodName, "获取交易信息异常..", e);
        }
        
    	//验证是否有后续操作
    	/**
        rd = validateTradeDaily(${firstLowerModel});
		if(!rd.isSuccess()){
			return rd;
		}
        */
        
    	//回滚交易数据
    	${firstLowerModel}.setEffectflag(Constant.EffectFlag.A);
    	try {
			baseDao.update(${firstLowerModel});
		}
		catch (DaoException e) {
			processException(methodName, "更新交易信息错误", e);
		}
    	
        return rd;
    }
}


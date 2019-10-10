package ${basePackage}.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.joyin.ticm.bean.ResultData;
import com.joyin.ticm.common.constant.Constant;
import com.joyin.ticm.common.util.CommonUtil;
import com.joyin.ticm.dao.BaseDao;
import com.joyin.ticm.dao.DaoException;
import com.joyin.ticm.dao.impl.AbstractDao;
import com.joyin.ticm.page.PageInfo;
import com.joyin.ticm.page.Pager;

import ${basePackage}.model.${upperCamelModel};
import ${basePackage}.dao.${upperCamelModel}Dao;



public class ${upperCamelModel}DaoImpl extends AbstractDao implements ${upperCamelModel}Dao{
	
	@Resource
	private BaseDao baseDao;
	
	@Override
	public ResultData find${upperCamelModel}OfPage(${upperCamelModel} ${firstLowerModel},
			Pager pager, String optype, List<String> ${firstLowerModel}Reqids,List<String> pList)
			throws DaoException {
		String methodName = "find${upperCamelModel}OfPage";
		info(methodName, "params: "+ ${firstLowerModel} +" - "+ pager + " - " +optype+ " - "+${firstLowerModel}Reqids);
        try {
            String hql = "from ${upperCamelModel} ${firstLowerModel} where 1 = 1 ";
            String strWhere = "";
            List<Object> paramValues = new ArrayList<Object>();
            //机构号查询
            if (CommonUtil.isNotEmpty(${firstLowerModel}.getOrgid())) {
                strWhere += " and ${firstLowerModel}.orgid in (" +${firstLowerModel}.getOrgid() +")";
            }
			//查询条件 todo
			
			
			
            //有效性查询
            if (!Constant.EffectFlag.ALL.equals(${firstLowerModel}.getEffectflag())) {
                if (CommonUtil.isNotEmpty(${firstLowerModel}.getEffectflag())) {
                    strWhere += " and ${firstLowerModel}.effectflag = ? ";
                    paramValues.add(${firstLowerModel}.getEffectflag());
                }
            }
            
            //查询需要办理的数据
            if (Constant.OptionType.DEAL.equals(optype)) {
                hql+=" AND ${firstLowerModel}.reqid IN (" + CommonUtil.listToSqlStr(${firstLowerModel}Reqids)
                        + ")";
            }
            
            //判断是否选中投组条件
			if(CommonUtil.isNotEmpty(${firstLowerModel}.getPortfolioid())){
				strWhere += " and ${firstLowerModel}.portfolioid = ?";
				paramValues.add(${firstLowerModel}.getPortfolioid());
			}else{
				if (CommonUtil.isNotEmpty(pList)) {
					strWhere += " AND ${firstLowerModel}.portfolioid IN (" + CommonUtil.listToSqlStr(pList)+")";
				}
			}
            
            if (CommonUtil.isNotEmpty(pager.getSort())
                    && CommonUtil.isNotEmpty(pager.getDirection())) {

                strWhere += " order by ${firstLowerModel}." + pager.getSort() + " "
                        + pager.getDirection();
            }

            hql = hql + strWhere;

            PageInfo pageInfo = baseDao.findByParamPageQuery(hql, paramValues,
                    pager.getPageSize(), pager.getPageNo());

            ResultData rstData = new ResultData();
            if (CommonUtil.isNotEmpty(pageInfo)) {
                if (CommonUtil.isNotEmpty(pageInfo.getPageData())) {
                    rstData.setList(pageInfo.getPageData());
                }
                if (CommonUtil.isNotEmpty(pageInfo.getPager())) {
                    rstData.setPager(pageInfo.getPager());
                }
            }
            return rstData;
        }
        catch (JDBCException ex) {
			error(methodName, "Error find ${firstLowerModel}List", ex);
			throw new DaoException(DaoException.ERROR_GENERIC_JDBC_EXCEPTION,
					ex);
		}
		catch (HibernateException ex) {
			error(methodName, "Error find ${firstLowerModel}List", ex);
			throw new DaoException(DaoException.ERROR_HIBERNATE, ex);
		}
	}
	
}


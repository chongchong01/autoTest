package com.dcits.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.business.system.bean.GlobalSetting;
import com.opensymphony.xwork2.ActionContext;

/**
 * struts2 工具类
 * @author Administrator
 *
 */
public class StrutsUtils{
	@SuppressWarnings("unchecked")
	/*
	 * 获取requestMap
	 */
	public static Map<String,Object> getRequestMap(){
		return (Map<String, Object>)ActionContext.getContext().get("request");
	}
	
	/*
	 * 获取sessionMap
	 */
	public static Map<String,Object> getSessionMap(){
		return ActionContext.getContext().getSession();
	}
	
	/*
	 * 获取applicationMap
	 */
	public static Map<String,Object> getApplicationMap(){
		return ActionContext.getContext().getApplication();
	}
	
	/*
	 * 获取parameterMap
	 */
	public static Map<String,Object> getParametersMap(){
		//((String[])parameters.get("name"))[0]
		return ActionContext.getContext().getParameters();
	}
	
	/*
	 * 获取DT发送的参数
	 * 
	 */
	public static Map<String,Object> getDTParameters(){
		Map<String,Object> returnMap = new HashMap<String,Object>();		
		//排序的那一列位置
		String orderColumnNum = ServletActionContext.getRequest().getParameter("order[0][column]");
		//排序方式 asc或者desc
		String orderType = ServletActionContext.getRequest().getParameter("order[0][dir]");
		//全局搜索条件
		String searchValue = ServletActionContext.getRequest().getParameter("search[value]");
		//需要排序的那一列属性名称
		String orderDataName = ServletActionContext.getRequest().getParameter("columns["+orderColumnNum+"][data]");
		//获取当前所有的展示字段
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		List<String> dataParams = new ArrayList<String>();
		for(Map.Entry<String, String[]> entry:params.entrySet()){
			if(entry.getKey().indexOf("][data]")!=-1){
				String a = (params.get(entry.getKey()))[0];
				if(!a.equals("")){
					dataParams.add(a);
				}
			}
		}
		returnMap.put("orderDataName", orderDataName);
		returnMap.put("orderType", orderType);
		returnMap.put("searchValue", searchValue);
		returnMap.put("dataParams", dataParams);
		return returnMap;
	}
	
	/*
	 * 获取actionName
	 */
	public static String getActionName(){
		return ActionContext.getContext().getName();
	}
	
	/*
	 * 获取spring上下文
	 * 
	 */
	public static ApplicationContext getApplicationContext(){
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext sc=request.getSession().getServletContext();
		return WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
	}
	
	/*
	 * 获取全局设置项
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static String getSettingValue(String settingKey){
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsUtils.getApplicationMap().get("settingMap");
		GlobalSetting setting = settingMap.get(settingKey);
		if(setting!=null){
			return setting.getSettingValue()==null?setting.getDefaultValue():setting.getSettingValue();
		}else{
			return null;
		}
		
	}
	
	/*
	 * 获取项目根路径
	 * 
	 */
	public static String getProjectPath(){
		ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
        return sc.getRealPath("");
	}
}

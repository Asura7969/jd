/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2017-03-18 09:11:43 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class item_002dparam_002dadd_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<table cellpadding=\"5\" style=\"margin-left: 30px\" id=\"itemParamAddTable\"\r\n");
      out.write("\tclass=\"itemParam\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>商品类目:</td>\r\n");
      out.write("\t\t<td><a href=\"javascript:void(0)\"\r\n");
      out.write("\t\t\tclass=\"easyui-linkbutton selectItemCat\">选择类目</a> <input type=\"hidden\"\r\n");
      out.write("\t\t\tname=\"cid\" style=\"width: 280px;\"></input></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr class=\"hide addGroupTr\">\r\n");
      out.write("\t\t<td>规格参数:</td>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t\t<ul>\r\n");
      out.write("\t\t\t\t<li><a href=\"javascript:void(0)\"\r\n");
      out.write("\t\t\t\t\tclass=\"easyui-linkbutton addGroup\">添加分组</a></li>\r\n");
      out.write("\t\t\t</ul>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td></td>\r\n");
      out.write("\t\t<td><a href=\"javascript:void(0)\" class=\"easyui-linkbutton submit\">提交</a>\r\n");
      out.write("\t\t\t<a href=\"javascript:void(0)\" class=\"easyui-linkbutton close\">关闭</a></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("</table>\r\n");
      out.write("<div class=\"itemParamAddTemplate\" style=\"display: none;\">\r\n");
      out.write("\t<li class=\"param\">\r\n");
      out.write("\t\t<ul>\r\n");
      out.write("\t\t\t<li><input class=\"easyui-textbox\" style=\"width: 150px;\"\r\n");
      out.write("\t\t\t\tname=\"group\" />&nbsp;<a href=\"javascript:void(0)\"\r\n");
      out.write("\t\t\t\tclass=\"easyui-linkbutton addParam\" title=\"添加参数\"\r\n");
      out.write("\t\t\t\tdata-options=\"plain:true,iconCls:'icon-add'\"></a></li>\r\n");
      out.write("\t\t\t<li><span>|-------</span><input style=\"width: 150px;\"\r\n");
      out.write("\t\t\t\tclass=\"easyui-textbox\" name=\"param\" />&nbsp;<a\r\n");
      out.write("\t\t\t\thref=\"javascript:void(0)\" class=\"easyui-linkbutton delParam\"\r\n");
      out.write("\t\t\t\ttitle=\"删除\" data-options=\"plain:true,iconCls:'icon-cancel'\"></a></li>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t</li>\r\n");
      out.write("</div>\r\n");
      out.write("<script style=\"\">\r\n");
      out.write("\t$(function() {\r\n");
      out.write("\t\tTAOTAO.initItemCat({\r\n");
      out.write("\t\t\tfun : function(node) {\r\n");
      out.write("\t\t\t\t$(\".addGroupTr\").hide().find(\".param\").remove();\r\n");
      out.write("\t\t\t\t//  判断选择的目录是否已经添加过规格\r\n");
      out.write("\t\t\t\t/* $.getJSON(\"/rest/item/param/\" + node.id,function(data){\r\n");
      out.write("\t\t\t\t  if(data){\r\n");
      out.write("\t\t\t\t\t  $.messager.alert(\"提示\", \"该类目已经添加，请选择其他类目。\", undefined, function(){\r\n");
      out.write("\t\t\t\t\t\t $(\"#itemParamAddTable .selectItemCat\").click();\r\n");
      out.write("\t\t\t\t\t  });\r\n");
      out.write("\t\t\t\t\t  return ;\r\n");
      out.write("\t\t\t\t  }\r\n");
      out.write("\t\t\t\t  $(\".addGroupTr\").show();\r\n");
      out.write("\t\t\t\t}); */\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\t\ttype : \"GET\",\r\n");
      out.write("\t\t\t\t\turl : \"/rest/item/param/\" + node.id,\r\n");
      out.write("\t\t\t\t\tstatusCode : {\r\n");
      out.write("\t\t\t\t\t\t200 : function() {\r\n");
      out.write("\t\t\t\t\t\t\t$.messager.alert(\"提示\", \"该类目已经添加，请选择其他类目。\",\r\n");
      out.write("\t\t\t\t\t\t\t\t\tundefined, function() {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t$(\"#itemParamAddTable .selectItemCat\")\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t.click();\r\n");
      out.write("\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\t\t404 : function() {\r\n");
      out.write("\t\t\t\t\t\t\t$(\".addGroupTr\").show();\r\n");
      out.write("\t\t\t\t\t\t},\r\n");
      out.write("\t\t\t\t\t\t500 : function() {\r\n");
      out.write("\t\t\t\t\t\t\talert(\"error\");\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t$(\".addGroup\").click(function() {\r\n");
      out.write("\t\t\tvar temple = $(\".itemParamAddTemplate li\").eq(0).clone();\r\n");
      out.write("\t\t\t$(this).parent().parent().append(temple);\r\n");
      out.write("\t\t\ttemple.find(\".addParam\").click(function() {\r\n");
      out.write("\t\t\t\tvar li = $(\".itemParamAddTemplate li\").eq(2).clone();\r\n");
      out.write("\t\t\t\tli.find(\".delParam\").click(function() {\r\n");
      out.write("\t\t\t\t\t$(this).parent().remove();\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\tli.appendTo($(this).parentsUntil(\"ul\").parent());\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\ttemple.find(\".delParam\").click(function() {\r\n");
      out.write("\t\t\t\t$(this).parent().remove();\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t$(\"#itemParamAddTable .close\").click(function() {\r\n");
      out.write("\t\t\t$(\".panel-tool-close\").click();\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t$(\"#itemParamAddTable .submit\").click(\r\n");
      out.write("\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\tvar params = [];\r\n");
      out.write("\t\t\t\t\tvar groups = $(\"#itemParamAddTable [name=group]\");\r\n");
      out.write("\t\t\t\t\tgroups.each(function(i, e) {\r\n");
      out.write("\t\t\t\t\t\tvar p = $(e).parentsUntil(\"ul\").parent().find(\r\n");
      out.write("\t\t\t\t\t\t\t\t\"[name=param]\");\r\n");
      out.write("\t\t\t\t\t\tvar _ps = [];\r\n");
      out.write("\t\t\t\t\t\tp.each(function(_i, _e) {\r\n");
      out.write("\t\t\t\t\t\t\tvar _val = $(_e).siblings(\"input\").val();\r\n");
      out.write("\t\t\t\t\t\t\tif ($.trim(_val).length > 0) {\r\n");
      out.write("\t\t\t\t\t\t\t\t_ps.push(_val);\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\tvar _val = $(e).siblings(\"input\").val();\r\n");
      out.write("\t\t\t\t\t\tif ($.trim(_val).length > 0 && _ps.length > 0) {\r\n");
      out.write("\t\t\t\t\t\t\tparams.push({\r\n");
      out.write("\t\t\t\t\t\t\t\t\"group\" : _val,\r\n");
      out.write("\t\t\t\t\t\t\t\t\"params\" : _ps\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\tvar url = \"/rest/item/param/\"\r\n");
      out.write("\t\t\t\t\t\t\t+ $(\"#itemParamAddTable [name=cid]\").val();\r\n");
      out.write("\t\t\t\t\t$.post(url, {\r\n");
      out.write("\t\t\t\t\t\t\"paramData\" : JSON.stringify(params)\r\n");
      out.write("\t\t\t\t\t}, function(data) {\r\n");
      out.write("\t\t\t\t\t\t$.messager.alert('提示', '新增商品规格成功!', undefined,\r\n");
      out.write("\t\t\t\t\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t$(\".panel-tool-close\").click();\r\n");
      out.write("\t\t\t\t\t\t\t\t\t$(\"#itemParamList\").datagrid(\"reload\");\r\n");
      out.write("\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("</script>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

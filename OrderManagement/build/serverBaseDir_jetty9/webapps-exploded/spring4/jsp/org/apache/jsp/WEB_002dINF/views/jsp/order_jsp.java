package org.apache.jsp.WEB_002dINF.views.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class order_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("<script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-latest.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\">\r\n");
      out.write("\r\n");
      out.write("    var CONTEXT_PATH = '");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/';\r\n");
      out.write("\r\n");
      out.write("    $(document).ready(function () {\r\n");
      out.write("\r\n");
      out.write("        $(\"input[name='order-action']\", $('#radioBtnDiv')).change(\r\n");
      out.write("                function (e) {\r\n");
      out.write("                    var myRadio = $('input[name=order-action]');\r\n");
      out.write("                    var checkedValue = myRadio.filter(':checked').val();\r\n");
      out.write("                    if (checkedValue == 'create') {\r\n");
      out.write("                        $('#result').html('');\r\n");
      out.write("                        $('#createDiv').show();\r\n");
      out.write("                        $('#lookupDiv').hide();\r\n");
      out.write("                    }\r\n");
      out.write("                    else {\r\n");
      out.write("                        $('#result').html('');\r\n");
      out.write("                        $('#createDiv').hide();\r\n");
      out.write("                        $('#lookupDiv').show();\r\n");
      out.write("                    }\r\n");
      out.write("                });\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("    function createOrder() {\r\n");
      out.write("\r\n");
      out.write("        var file = document.getElementById(\"xml-data\");\r\n");
      out.write("\r\n");
      out.write("        $('#result').html('');\r\n");
      out.write("\r\n");
      out.write("        var oMyForm = new FormData();\r\n");
      out.write("        oMyForm.append(\"file\", file.files[0]);\r\n");
      out.write("\r\n");
      out.write("        $.ajax({\r\n");
      out.write("            url: CONTEXT_PATH + 'createOrder.do',\r\n");
      out.write("            data: oMyForm,\r\n");
      out.write("            dataType: 'text',\r\n");
      out.write("            processData: false,\r\n");
      out.write("            contentType: false,\r\n");
      out.write("            type: 'POST',\r\n");
      out.write("            success: function (data) {\r\n");
      out.write("                $('#result').html(data);\r\n");
      out.write("            },\r\n");
      out.write("            error: function (data) {\r\n");
      out.write("                alert(\"error: \" + data);\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    function lookupOrder() {\r\n");
      out.write("        var query = document.getElementById(\"orderId\").value;\r\n");
      out.write("\r\n");
      out.write("        $(document).ready(function () {\r\n");
      out.write("            $.ajax({\r\n");
      out.write("                url: CONTEXT_PATH + 'searchOrder.do?q=' + query,\r\n");
      out.write("                //beforeSend: function() { $('#wait').show(); },\r\n");
      out.write("                //complete: function() { $('#wait').hide(); },\r\n");
      out.write("                type: 'GET',\r\n");
      out.write("                success: function (data) {\r\n");
      out.write("                    $('#result').html(data);\r\n");
      out.write("                },\r\n");
      out.write("                error: function (data) {\r\n");
      out.write("                    alert(\"error: \" + data);\r\n");
      out.write("                }\r\n");
      out.write("            });\r\n");
      out.write("        });\r\n");
      out.write("\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("<form:form class=\"form-horizontal\" action=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/order\" method=\"post\">\r\n");
      out.write("    <table width=\"500px\">\r\n");
      out.write("        <tr>\r\n");
      out.write("            <td>\r\n");
      out.write("                <table width=\"100%\">\r\n");
      out.write("                    <tr>\r\n");
      out.write("                        <td>\r\n");
      out.write("                            <table width=\"100%\" border=\"1\">\r\n");
      out.write("                                <tr>\r\n");
      out.write("                                    <td>\r\n");
      out.write("                                        Action:\r\n");
      out.write("                                    </td>\r\n");
      out.write("                                </tr>\r\n");
      out.write("                                <tr>\r\n");
      out.write("                                    <td>\r\n");
      out.write("                                        <div id=\"radioBtnDiv\">\r\n");
      out.write("                                            <table width=\"100%\">\r\n");
      out.write("                                                <tr>\r\n");
      out.write("                                                    <td>Create Order <input name=\"order-action\" type=\"radio\"\r\n");
      out.write("                                                                            value=\"create\"/></td>\r\n");
      out.write("                                                </tr>\r\n");
      out.write("                                                <tr>\r\n");
      out.write("                                                    <td>Look Order <input name=\"order-action\" type=\"radio\"\r\n");
      out.write("                                                                          value=\"lookup\"/></td>\r\n");
      out.write("                                                </tr>\r\n");
      out.write("                                            </table>\r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </td>\r\n");
      out.write("                                </tr>\r\n");
      out.write("                                <tr>\r\n");
      out.write("                                    <td>\r\n");
      out.write("                                        <table border=\"1\" width=\"100%\">\r\n");
      out.write("                                            <tr>\r\n");
      out.write("                                                <td>\r\n");
      out.write("                                                    <div id=\"createDiv\">\r\n");
      out.write("                                                        XML to Upload: <input type=\"file\" id=\"xml-data\"> <br>\r\n");
      out.write("                                                        <input type=\"button\" id=\"upload\" value=\"Create\"\r\n");
      out.write("                                                               onclick=\"createOrder()\"/>\r\n");
      out.write("\r\n");
      out.write("                                                    </div>\r\n");
      out.write("                                                    <div id=\"lookupDiv\" style=\"display:none\">\r\n");
      out.write("                                                        Order Id: <input type=\"text\" id=\"orderId\"><br>\r\n");
      out.write("                                                        <input type=\"button\" id=\"Lookup\" value=\"Lookup\"\r\n");
      out.write("                                                               onclick=\"lookupOrder()\"/>\r\n");
      out.write("\r\n");
      out.write("                                                    </div>\r\n");
      out.write("                                                </td>\r\n");
      out.write("                                            </tr>\r\n");
      out.write("                                            <tr>\r\n");
      out.write("                                                <td>\r\n");
      out.write("                                                    Result:<div id=\"result\"></div>\r\n");
      out.write("                                                </td>\r\n");
      out.write("                                            </tr>\r\n");
      out.write("                                        </table>\r\n");
      out.write("                                    </td>\r\n");
      out.write("                                </tr>\r\n");
      out.write("                            </table>\r\n");
      out.write("\r\n");
      out.write("                        </td>\r\n");
      out.write("                    </tr>\r\n");
      out.write("                </table>\r\n");
      out.write("\r\n");
      out.write("            </td>\r\n");
      out.write("        </tr>\r\n");
      out.write("    </table>\r\n");
      out.write("</form:form>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

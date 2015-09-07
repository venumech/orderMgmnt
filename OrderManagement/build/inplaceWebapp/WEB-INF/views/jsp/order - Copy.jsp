<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" src="resources/core/js/tree_list.js"></script>

<!-- <link rel="stylesheet" type="text/css" href="resources/core/tree_list.css">
-->

<style type="text/css">

	/*------------------*/
	/* EXPANDABLE LIST  */
	/*------------------*/

	#listContainer{
	  margin-top:15px;
	}

	#expList ul, li {
	    list-style: none;
	    margin:0;
	    padding:0;
	    cursor: pointer;
	}

	#expList li {
	    line-height:140%;
	    text-indent:0px;
	    background-position: 1px 8px;
	    padding-left: 20px;
	    background-repeat: no-repeat;
	}

	/* Collapsed state for list element */
	#expList .collapsed {
	    background-image: url(resources/core/images/expand.png);
	    /* http://localhost:9999/venu/resources/core/images/expand.png */
	}
	/* Expanded state for list element
	/* This class must be located UNDER the collapsed one */
	#expList .expanded {
	    background-image: url(resources/core/images/collapsed.png);
	    /* http://localhost:9999/venu/resources/core/images/collapsed.png */
	}
	#expList {
	    clear: both;
	}

	.listControl{
	  margin-bottom: 15px;
	}
	
</style>
<script language="JavaScript">

    var CONTEXT_PATH = '${pageContext.request.contextPath}/';

    $(document).ready(function () {

        $("input[name='order-action']", $('#radioBtnDiv')).change(
                function (e) {
                    var myRadio = $('input[name=order-action]');
                    var checkedValue = myRadio.filter(':checked').val();
                    if (checkedValue == 'create') {
                        $('#result').html('');
                        $('#createDiv').show();
                        $('#lookupDiv').hide();
                    }
                    else {
                        $('#result').html('');
                        $('#createDiv').hide();
                        $('#lookupDiv').show();
                    }
                });
    });

    function createOrder() {

        var file = document.getElementById("xml-data");

        $('#result').html('');

        var oMyForm = new FormData();
        oMyForm.append("file", file.files[0]);

        $.ajax({
            url: CONTEXT_PATH + 'createOrder.do',
            data: oMyForm,
            dataType: 'text',
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (data) {
                $('#result').html("Submission Status : " + data);
            },
            error: function (data) {
                alert("error: " + data);
            }
        });
    }

    function lookupOrder() {
        var query = document.getElementById("orderId").value;

        $(document).ready(function () {
            $.ajax({
                url: CONTEXT_PATH + 'searchOrder.do?q=' + query,
                //beforeSend: function() { $('#wait').show(); },
                //complete: function() { $('#wait').hide(); },
                type: 'GET',
                success: function (data) {
                //data ='{"id":27336,"from":{"city":"NEW YORK","state":"NY","zip":"10001"},"to":{"city":"WASHINGTON","state":"DC","zip":"20001"},"lines":[{"weight":1000.1,"volume":1.0,"hazard":true,"product":"petrol"},{"weight":2000.0,"volume":2.0,"hazard":false,"product":"water"}],"instructions":"here be dragons"}';
   	
	   	var output="";
		if (data.indexOf("ERROR") == 0){
		   output = data;
		   alert (data);

                } else {
                var json_obj = $.parseJSON(data);//parse JSON
		            output+="<div id='listContainer'>";
		            output+="<ul id='expList'><li> <font color='blue'><b>Order ID </b></font> "+ json_obj.id +"</li>";
		            output+= "<li> <font color='blue'><b>From: </b></font><ul>";
		            output+= "<li> City: " + json_obj.from.city +"</li>";
		            output+= "<li> State: " + json_obj.from.state +"</li>";
			    output+= "<li> Zip: " + json_obj.from.zip +"</li>";
			    output+= "</ul> </li>";
		            output+= "<li><font color='blue'><b>To: </b></font><ul>";
		            output+= "<li> City: " + json_obj.to.city +"</li>";
		            output+= "<li> State: " + json_obj.to.state +"</li>";
			    output+= "<li> Zip: " + json_obj.to.zip +"</li>";
			    output+= "</ul> </li>";
			    
			    output+= "<li> <font color='blue'><b>Lines </b></font><ul>";
			    
		            for (var i in json_obj.lines) {
		                output+="<li> Line - " + i +"<ul> <li> Product: "+ json_obj.lines[i].product +"</li> <li> Weight:" + json_obj.lines[i].weight +"</li> <li> Volume: " + json_obj.lines[i].volume +"</li> <li> Hazard:"+ json_obj.lines[i].hazard +"</li> </ul> </li>";

		            }
		            
		         output+="</ul> </li>";
		         
		         output+="<li> Instructions: "+ json_obj.instructions +" </li> </ul>";
		                 output+="</div>";
			}
		  
                    $('#result').html(output);
                    prepareList();
                },
                error: function (data) {
                    alert("error: " + data);
                }
            });
        });

    }


</script>
<form:form class="form-horizontal" action="/order" method="post">
	<br><center><h1><font color="blue">Order Process</font></h1></center><br>
    <table width="500px">
        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <td>
                            <table width="100%" border="1">
                                <tr>
                                    <td>
                                        Action:
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div id="radioBtnDiv">
                                            <table width="100%">
                                                <tr>
                                                    <td>Create Order <input name="order-action" type="radio"
                                                                            value="create"/></td>
                                                </tr>
                                                <tr>
                                                    <td>Look Order <input name="order-action" type="radio"
                                                                          value="lookup"/></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table border="1" width="100%">
                                            <tr>
                                                <td>
                                                    <div id="createDiv">
                                                        XML to Upload: <input type="file" id="xml-data"> <br>
                                                        <input type="button" id="upload" value="Create"
                                                               onclick="createOrder()"/>

                                                    </div>
                                                    <div id="lookupDiv" style="display:none">
                                                        Order Id: <input type="text" id="orderId" value="27309"><br>
                                                        <input type="button" id="Lookup" value="Lookup"
                                                               onclick="lookupOrder()"/>

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="result"> Sample data:
<textarea rows="14" cols="100">
"<?xml version="1.0" encoding="utf-8"?>
<order>
    <from zip="80817" state="CO" city="COLORADAO SPRINGS"/>
    <to zip="96821" state="HI" city="Honolulu"/>
    <lines>
        <line weight="10000.1" volume="14" hazard="false" product="Engine Block"/>
        <line weight="20000.55" volume="8" hazard="true" product="Liquid Nitrogen"/>
    </lines>
    <instructions>Transport in secure container</instructions>
</order>
</textarea>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                        </td>
                    </tr>
                </table>

            </td>
        </tr>
    </table>
</form:form>

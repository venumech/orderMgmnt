<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" src="resources/core/js/tree_list.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"> <!-- not to show clutter such as 'No search results found'  -->
<!-- <link rel="stylesheet" type="text/css" href="resources/core/tree_list.css">
-->

<style type="text/css">
	/*------------------*/
	/* EXPANDABLE LIST  */
	/*------------------*/
	#outerBox{
	  margin-top:15px;
	}
	#stretchItems ul, li {
	    list-style: none;
	    margin:0;
	    padding:0;
	    cursor: pointer;
	}
	#stretchItems li {
	    line-height:140%;
	    text-indent:0px;
	    background-position: 1px 8px;
	    padding-left: 20px;
	    background-repeat: no-repeat;
	}
	/* Collapsed state for list element */
	#stretchItems .collapsed {
	    background-image: url(resources/core/images/expand.png);
	    /* http://localhost:9999/venu/resources/core/images/expand.png */
	}
	/* Expanded state for list element
	/* This class must be located UNDER the collapsed one */
	#stretchItems .expanded {
	    background-image: url(resources/core/images/collapsed.png);
	    /* http://localhost:9999/venu/resources/core/images/collapsed.png */
	}
	#stretchItems {
	    clear: both;
	}
	.listControl{
	  margin-bottom: 15px;
	}
	
</style>
<script type="text/javascript"> var CONTEXT_PATH = '/venu/';</script>

<script language="JavaScript">
    //var CONTEXT_PATH = '${pageContext.request.contextPath}/';
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
            //url: CONTEXT_PATH + 'createOrder.do',
            url: 'http://localhost:9990/OrderProcessRestService/createOrder/',
            data: oMyForm,
            dataType: 'text',
            processData: false,
            contentType: false,
            type: 'POST',
            success: function (data) {
            	
            	alert(data);
                var json_obj;

   	      		var objectConstructor = {}.constructor;
   	      		if( data.constructor === objectConstructor){ //check if the 'data' is a JSON object
	   	    		alert("json object!");
   	      			json_obj = data; 
   	      		} else {
   	      			alert("not json object!");
              		json_obj = $.parseJSON(data);//parse the string to JSON	   	    	  
   	      		}
                if (json_obj.error){
                	$('#result').html("<div id='outerBox'> <font color='red'>"+ json_obj.errorMsg + "</font></div");
                	return;
                } else {
                    $('#result').html("Submission Status : Order Id successfully created. Id:" + json_obj.id);
                }
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
                //url: CONTEXT_PATH + 'searchOrder.do?q=' + query,
                url: 'http://localhost:9990/OrderProcessRestService/search/' + query,
                //beforeSend: function() { $('#wait').show(); },
                //complete: function() { $('#wait').hide(); },
                type: 'GET',
                success: function (data) {
                	//data ='{"id":27336,"from":{"city":"NEW YORK","state":"NY","zip":"10001"},"to":{"city":"WASHINGTON","state":"DC","zip":"20001"},"lines":[{"weight":1000.1,"volume":1.0,"hazard":true,"product":"petrol"},{"weight":2000.0,"volume":2.0,"hazard":false,"product":"water"}],"instructions":"here be dragons"}';
   					
	   	            var output="";

	   	      		var objectConstructor = {}.constructor;
	   	      		var json_obj;
	   	      		if( data.constructor === objectConstructor){ //check if the 'data' is a JSON object
	   	      			alert("json object!");
	   	      			json_obj = data; 
	   	      		} else {
	   	      			alert("not json object!");
                  		json_obj = $.parseJSON(data);//parse the string to JSON	   	    	  
	   	      		}

	   	      		if (json_obj.hasOwnProperty('error')){
                    	//alert (json_obj.errorMsg);
                    	$('#result').html("<div id='outerBox'> <font color='red'>"+ json_obj.errorMsg + "</font></div");
                    	return;
                    } else {
                    	output+="<div id='outerBox'>";
                    	output+="<ul id='stretchItems'><li> <font color='blue'><b>Order ID </b></font> "+ json_obj.id +"</li>";
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
                    	
                    	var json_array= json_obj.lines;
                    	json_array.sort(sort_by('weight', false, parseInt));
                    	//json_array.sort(sort_by('product', false, function(a){return a.toUpperCase()}));
                    	for (var i in json_array) {
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
                    //alert("error: " + data.statusText);
                    $('#result').html("<font color='red'> Error: The requested resource not exists in the system. <b>[Code: "+data.status + "; Reason:"+ data.statusText +"]</b></font>");
                }
            });
        });
    }
    
/*
 * Auto complete feature added to show up the 5 of the most recent order ids 
 *  look for server side property, "ui.autocomplete.items")
 */
    $(function() {      
        $("#orderId").autocomplete({
            source: function (request, response) {
                //$.getJSON(CONTEXT_PATH + "getMatchedIds.do", {
                $.getJSON("http://localhost:9990/OrderProcessRestService/findMatchedIds.do", {                	
                    term: request.term
                }, response);
            }
        });
    });
    
    
    /*
     * sort function
     */
var sort_by = function(field, reverse, primer){
   var key = primer ? 
       function(x) {
          return primer(x[field])} : function(x) {
                                         return x[field]
                                     };
 
          reverse = !reverse ? 1 : -1;
          return function (a, b) {
                 return a = key(a), b = key(b), reverse * ((a > b) - (b > a));
     } 
}
/*
  Tree list event handler
 */
function prepareList() {
    $('#stretchItems').find('li:has(ul)')
    .click( function(event) {
        if (this == event.target) {
            $(this).toggleClass('expanded');
            $(this).children('ul').toggle('medium');
        }
        return false;
    })
    .addClass('collapsed')
    .children('ul').hide();
}
</script>
    <p class="bg-primary">
	<img src="resources/core/images/order.JPG" class="img-responsive pull-right" />
	</p>
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
                                                    <td> <input name="order-action" type="radio"
                                                                            value="create"/> Create Order </td>
                                                </tr>
                                                <tr>
                                                    <td> <input name="order-action" type="radio"
                                                                          value="lookup"/> Look up Order </td>
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
                                                        Order Id: <input type="text" id="orderId"><br>
                                                        <br /><input type="button" id="Lookup" value="Lookup"
                                                               onclick="lookupOrder()"/>

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div id="result"> Sample data for xml file upload:
<textarea rows="14" cols="100">
<?xml version="1.0" encoding="utf-8"?>
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
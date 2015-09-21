<!DOCTYPE html>
<html lang="en">


<head>
   <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.1.1/css/bootstrap.no-icons.min.css" rel="stylesheet">
   <link href="http://netdna.bootstrapcdn.com/font-awesome/2.0/css/font-awesome.css" rel="stylesheet">
   <link rel="stylesheet" type="text/css" href="resources/core/css/order.css">
   <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>

</head>

<body ng-app="myapp">
    <script type="text/javascript">
        var sortingOrder = 'product';
    </script>
    
<script type="text/javascript"> var CONTEXT_PATH = '${pageContext.request.contextPath}/';</script>
    <h1> Order Process System</h1>

      
    <div ng-controller="MyController">    
    <div id="radioBtnDiv">
        <table width="100%">
           <tr>
              <td> <input name="order-action" type="radio" value="create" ng-model="action.flag"  ng-change = "createOrder()"/> Create Order </td>
           </tr>
           <tr>
           </tr>
           <tr>
              <td> <input name="order-action" type="radio" value="lookup" ng-model="action.flag" /> Look up Order </td>
           </tr>
        </table>
    </div>
<!--     
    <div  id="createDiv" class=" centered text-center" ng-show="action.flag == 'create'"> 
       XML to Upload: <input type="file" id="xml-data"> <br>
       <input type="button" id="upload" value="Create" onclick="createOrder()"/>
       
    </div>
 -->   
 <br />
      <div ng-controller = "fileUploadController"  id="createDiv" class=" centered text-center" ng-show="action.flag == 'create'"> 
    <input type="file" file-model="myFile"/>
    <button ng-click="uploadFile()">Save Order</button>
    	<br />
		<div ng-show="orderdata.order.id > 0">
	            <p class="label label-success"  style="width: 400px;" >Order is saved successfully. Order ID: {{orderdata.order.id}}</p>
		</div>
    <div ng-show="action.flag == 'create'"> Sample XML file data:<br />
        <textarea rows="11" cols="500">
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
	<br />
    
	<!-- dtat1 = {{xxx}} -->
	<br />

	<!--  orderdata.order = {{orderdata.order}} -->

	<!--  orderdata.order.id = {{orderdata.order.id}} -->
	</div>
    <!--     <tt>action = {{action.flag | json}}</tt><br/> -->
    
    <div  id="lookupDiv" class=" centered text-center" ng-show="action.flag == 'lookup'"> 

        Enter Order Id: <input class="input-large search-query" type="text" id="orderId"  placeholder="1442604643249" value="1442604643249"> &nbsp;&nbsp;&nbsp;<!-- 27309 --> 

    Search System : <button ng-click="myData.doClick(item, $event)">Send AJAX Request</button>
    </div>
   <!--  <p class="add-on" ng-click="myData.doClick(item, $event)"><i class="icon-search"></i></p>
     -->   
     <!-- <div class="container"> -->
        <div class=" centered text-center" ng-show="dataLoaded == true && action.flag == 'lookup'"> 
            <span class="label label-success"  style="width: 400px;">Order details retrieved for Order ID: {{myData.id}}</span>

    <!--  </div> -->
        <hr />
        <div ng-show="dataLoaded == true && action.flag == 'lookup'"> 
    <table class="table table-striped table-condensed table-hover" style="max-width: 400px;">
     <caption class="label label-info">Order Shipment Details</caption>
                <thead>
                    <tr>
                        <th class="id">From:&nbsp;</a></th>
                        <th class="To">To:&nbsp;</th>
					</tr>
                </thead>
                <tbody>
                <tr>
                <td>{{fromAddress.city}}
                  <br />{{fromAddress.state}} - {{fromAddress.zip}}
                </td>
                <td>{{toAddress.city}}
                  <br />{{toAddress.state}} - {{toAddress.zip}}
                </td>
                </tr>
                </tbody>
</table>
Shipping Instructions: {{instructions}}
    
             <table class="table-bordered table-striped table-condensed table-hover" >
                  <caption class="label label-info">Order Item Details</caption>
             <thead>
                    <tr>
                        <th class="number">Item Number&nbsp;</th>
                        <th class="product" style="min-width: 150px;">Product&nbsp;<a ng-click="sort_by('product')"><i class="icon-sort"></i></a></th>
                        <th class="weight">Weight&nbsp;<a ng-click="sort_by('weight')"><i class="icon-sort"></i></a></th>
                        <th class="volume">Volume&nbsp;<a ng-click="sort_by('volume')"><i class="icon-sort"></i></a></th>
                        <th class="hazard">Hazard&nbsp;<a ng-click="sort_by('hazard')"><i class="icon-sort"></i></a></th>
                    </tr>
            </thead>
            <tfoot>
                    <td colspan="5">
                        <div class="pagination pull-right">
                            <ul>
                                <li ng-class="{disabled: currentPage == 0}">
                                    <a href ng-click="prevPage()">&laquo; Previous</a>
                                </li>
                                <li ng-repeat="n in range(pagedItems.length)"
                                    ng-class="{active: n == currentPage}"
                                ng-click="setPage()">
                                    <a href ng-bind="n + 1">1</a>
                                </li>
                                <li ng-class="{disabled: currentPage == pagedItems.length - 1}">
                                    <a href ng-click="nextPage()">Next »</a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tfoot>
                <tbody>
            <tr ng-repeat = "line in pagedItems[currentPage] | orderBy:sortingOrder:reverse"">
               <td>{{ $index + 1 }}</td>
               <!-- <td ng-if="$odd" style="background-color:#f1f1f1">{{ line.product }}</td>
               <td ng-if="$even">{{ line.product }}</td>
               -->
               <td>{{ line.product }}</td>
               <td>{{ line.weight}}</td>
               <td>{{ line.volume }}</td>
               <td>{{ line.hazard }}</td>
            </tr>
            </tbody>
         </table>
        <hr />
         <p>
<button ng-disabled="mySwitch">Process Next Order</button>
</p>


<input type="checkbox" ng-model="mySwitch">Button
        </div>
</div>
<div ng-show="errors == true">
{{data.ERROR}} - {{data.message}}
</div>
    <script type="text/javascript" src="resources/core/js/pagination.js"></script>


</body>

</html>

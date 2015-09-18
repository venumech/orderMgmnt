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
<h1> Order Search Process</h1>
  <div ng-controller="MyController" >
   Enter Order Id: <input class="input-large search-query" type="text" id="orderId" value="1442604643249"> &nbsp;&nbsp;&nbsp;<!-- 27309 --> 

    Search System : <button ng-click="myData.doClick(item, $event)">Send AJAX Request</button>
   <!--  <p class="add-on" ng-click="myData.doClick(item, $event)"><i class="icon-search"></i></p>
     -->   
        <hr />
        <div ng-hide="dataLoaded != true"> 
    Order ID: {{myData.id}}
    <table class="table table-striped table-condensed table-hover">
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
    
             <table>
             <thead>
                    <tr>
                        <th class="number">Item Number&nbsp;</th>
                        <th class="product">Product&nbsp;<a ng-click="sort_by('product')"><i class="icon-sort"></i></a></th>
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
                                    <a href ng-click="prevPage()">« Previous</a>
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
            <tr ng-repeat = "line in myData.lines">
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
<div ng-show="errors == true">
{{data.ERROR}} - {{data.message}}
</div>
    <script type="text/javascript" src="resources/core/js/pagination.js"></script>


</body>

</html>

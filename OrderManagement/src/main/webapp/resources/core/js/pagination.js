 var myApp = angular.module("myapp", []);
 /*
 myApp.config(function($routeProvider){
	 $routeProvider
	 		.when('/',
	 				{controller: 'MyController',
	 				  templateUrl:'create.jsp'
	 				})
	 			.when('/create',
	 		 			{controller: 'MyController',
	 		 			  templateUrl: 'search.jsp'
	 		 			})
	 		 		.otherwise({redirectTo: 'create.html'});
	 			 					 			
 });
 */
        myApp.controller("MyController", ['$scope', '$http', '$filter', function($scope, $http, $filter) {
            $scope.myData = {};
            $scope.myData.doClick = function(item, event) {

                var query = document.getElementById("orderId").value;
                // alert(query);
               // var query = 1442158639469;
                var url =  'searchOrder.do?q=' + query;

                var responsePromise = $http.get(url);

                responsePromise.success(function(data, status, headers, config) {

                	if (data.hasOwnProperty('ERROR')) $scope.errors=true;
                	else   	$scope.dataLoaded = true;
                	                	
                    $scope.myData = data;
                    $scope.lines = data.lines;
                    $scope.fromAddress =data.from;
                    $scope.toAddress =data.to;
                    $scope.instructions = data.instructions;
<!------------- ------------------------------------------------------- -->


$scope.sortingOrder = sortingOrder;
$scope.filteredItems = [];
$scope.reverse = false;
$scope.groupedItems = [];
$scope.itemsPerPage = 5;
$scope.pagedItems = [];
$scope.currentPage = 0;

// init the filtered items
$scope.init = function () {

    $scope.filteredItems = $scope.lines;
    //alert($scope.filteredItems.length);
    // take care of the sorting order
    if ($scope.sortingOrder !== '') {
        $scope.filteredItems = $filter('orderBy')($scope.filteredItems, $scope.sortingOrder, $scope.reverse);
    }
    
    $scope.currentPage = 0;
    // now group by pages
    $scope.groupToPages();
};

// calculate page in place
$scope.groupToPages = function () {
    $scope.pagedItems = [];

    for (var i = 0; i < $scope.filteredItems.length; i++) {
        //alert(i + "...  " + $scope.filteredItems[i].product);
    	if (i % $scope.itemsPerPage === 0) {
            $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.filteredItems[i] ];
        } else {
            $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.filteredItems[i]);
        }
    }
    //alert('pagedItems.length=' + $scope.pagedItems.length);
    //alert('pagedItems.length=' + $scope.pagedItems[0].length);
    //alert('pagedItems.length=' + $scope.pagedItems[1].length);
};

$scope.range = function (start, end) {
    var ret = [];
    if (!end) {
        end = start;
        start = 0;
    }
    for (var i = start; i < end; i++) {
        ret.push(i);
    }
    return ret;
};

$scope.prevPage = function () {
    if ($scope.currentPage > 0) {
        $scope.currentPage--;
    }
};

$scope.nextPage = function () {
    if ($scope.currentPage < $scope.pagedItems.length - 1) {
        $scope.currentPage++;
    }
};

$scope.setPage = function () {
    $scope.currentPage = this.n;
};

// functions have been describe process the data for display
$scope.init();

// change sorting order
$scope.sort_by = function(newSortingOrder) {
    if ($scope.sortingOrder == newSortingOrder)
        $scope.reverse = !$scope.reverse;

    $scope.sortingOrder = newSortingOrder;

  //after changing the sort order reset the entire array aligned to the new sort order or sort element orderby
    $scope.init();
};



<!------------- ------------------------------------------------------- -->
                    
                });
                responsePromise.error(function(data, status, headers, config) {
                	$scope.dataLoaded = false;
                	$scope.isError = true;
                	$scope.errorList=data;
                	alert(data);
                	alert("AJAX failed!");
                });
            }


        }]);
 //myApp.MyController.$inject = ['$scope', '$filter'];
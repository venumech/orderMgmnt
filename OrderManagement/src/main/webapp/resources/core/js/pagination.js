 angular.module("myapp", [])
        .controller("MyController", function($scope, $http) {
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
$scope.reverse = false;
$scope.groupedItems = [];
$scope.itemsPerPage = 5;
$scope.pagedItems = [];
$scope.currentPage = 0;

// init the filtered items
$scope.init = function () {

    $scope.currentPage = 0;
    // now group by pages
    $scope.groupToPages();
};

// calculate page in place
$scope.groupToPages = function () {
    $scope.pagedItems = [];

    for (var i = 0; i < $scope.lines.length; i++) {
        //alert(i + "...  " + $scope.lines[i].product);
    	if (i % $scope.itemsPerPage === 0) {
            $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.lines[i] ];
        } else {
            $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.lines[i]);
        }
    }
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

    // icon setup
    $('th i').each(function(){
        // icon reset
        $(this).removeClass().addClass('icon-sort');
    });
    if ($scope.reverse)
        $('th.'+new_sorting_order+' i').removeClass().addClass('icon-chevron-up');
    else
        $('th.'+new_sorting_order+' i').removeClass().addClass('icon-chevron-down');
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


        } );
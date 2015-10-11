 var myApp = angular.module("orderApp", []);
 
 var controllers = {};
 /*
 myApp.config(function($routeProvider){
	 $routeProvider
	 		.when('/',
	 				{controller: 'SearchController',
	 				  templateUrl:'create.jsp'
	 				})
	 			.when('/create',
	 		 			{controller: 'SearchController',
	 		 			  templateUrl: 'search.jsp'
	 		 			})
	 		 		.otherwise({redirectTo: 'create.html'});
	 			 					 			
 });
 */
 
 /*-----------------------------------file upload controller------------------------------------*/
 myApp.directive('fileModel', ['$parse', function ($parse) {
	    return {
	        restrict: 'A',
	        link: function(scope, element, attrs) {
	            var model = $parse(attrs.fileModel);
	            var modelSetter = model.assign;

	        	//alert(attrs.fileModel);
	            element.bind('change', function(){
	                scope.$apply(function(){
	                    modelSetter(scope, element[0].files[0]);
	                });
	            });
	        }
	    };
	}]);

	myApp.service('fileUpload', ['$http', function ($http) {
	    var fileUpload = {};

	    fileUpload.order = {};
	    fileUpload.errordata ={};
		
		var that = this;
		this.datafile1=12345;
		this.datafile;
	    this.uploadFileToUrl = function(file, uploadUrl){
	        var fd = new FormData();
	        fd.append('file', file);
	        $http.post(uploadUrl, fd, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        })
	        .success(function(data){
	            //alert(data.id);
	            //alert('data.error=' + data.error);
	            fileUpload.order = data;
	            return fileUpload.order;
	            
	        })
	        .error(function(error_data){
	            alert('Error occured while saving the order into the system!');
	            alert(error_data);
	            fileUpload.errordata = {"error":true,
	            	"errorMsg":"Error occured while saving the order into the system!"};
	            return fileUpload.errordata;

	        });
	        
	        return fileUpload;
	    }
	}]);
	
	myApp.controller('fileUploadController', ['$scope', 'fileUpload', function($scope, fileUpload){
	    

        $scope.xxx =  fileUpload.datafile1;
        $scope.orderdata={};
        $scope.errors=false;
	    $scope.uploadFile = function(){
	    	//alert('entered');
	        var file = $scope.myFile;
	        console.log('file is ' );
	        console.dir(file);
	        var uploadUrl = CONTEXT_PATH + 'createOrder.do';
	        $scope.orderdata =  fileUpload.uploadFileToUrl(file, uploadUrl);

	        /*
	        if ($scope.orderdata.error != undefined) {
	        	alert('! undefined');
	        	$scope.errors=true;
	        }
	        if ($scope.orderdata.error){
	        	alert("error happened . $scope.orderdata.error=" + $scope.orderdata.error);
	        }
	        if ($scope.orderdata['error']) {
	        	alert('undefined');
	        	alert('scope.orderdata.error = ' + $scope.orderdata.error);

        		$scope.errors = true;

	        	alert('scope.errors='+ $scope.errors);
	        }
	        if ($scope.orderdata.hasOwnProperty('error')) {
	        	alert('errro');
        		$scope.errors=true;
        	} else $scope.errors=false;
*/	
	    };
	    

	    
	}]);
	
	/*---------------------------------------order search controller----------------------------------------*/
	myApp.controller("SearchController", ['$scope', '$http', '$filter', function($scope, $http, $filter) {
            $scope.myData = {};
            $scope.action = {
                    flag: true
                  };
            $scope.myData.doClick = function(item, event) {
        		$scope.errors=false;
                var query = document.getElementById("orderId").value;
                // alert(query);
               // var query = 1442158639469;
                var url =  'searchOrder.do?q=' + query;

                var responsePromise = $http.get(url);

                responsePromise.success(function(data, status, headers, config) {

                	if (data.hasOwnProperty('error')) {
                		$scope.errors=true;
                		$scope.error=data;
                		$scope.dataLoaded = false;
                	}else   	{
                		$scope.dataLoaded = true;
                	}
                	$scope.orderid = data.id;
                    $scope.lines = data.lines;
                    $scope.fromAddress =data.from;
                    $scope.toAddress =data.to;
                    $scope.instructions = data.instructions;

$scope.sortingOrder = sortingOrder;
$scope.sortedItems = [];
$scope.reverse = false;
$scope.groupedItems = [];
$scope.rowsPerPage = document.getElementById("pagesize").value;
$scope.pagedItems = [];
$scope.currentPage = 0;

// init the filtered items
$scope.init = function () {

	if ( $scope.errors) return;
	
    $scope.sortedItems = $scope.lines;
    //alert($scope.sortedItems.length);
    // take care of the sorting order
    if ($scope.sortingOrder !== '') {
        $scope.sortedItems = $filter('orderBy')($scope.sortedItems, $scope.sortingOrder, $scope.reverse);
    }
    
    $scope.currentPage = 0;
    // now group by pages based on the page size value, as in $scope.rowsPerPage
    $scope.groupToPages();
};

// calculate page in place
$scope.groupToPages = function () {
    $scope.pagedItems = [];

    for (var i = 0; i < $scope.sortedItems.length; i++) {
        //alert(i + "...  " + $scope.sortedItems[i].product);
    	if (i % $scope.rowsPerPage === 0) {
            $scope.pagedItems[Math.floor(i / $scope.rowsPerPage)] = [ $scope.sortedItems[i] ];
        } else {
            $scope.pagedItems[Math.floor(i / $scope.rowsPerPage)].push($scope.sortedItems[i]);
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

// functions have been described to process the data for display
$scope.init();

// change sorting order
$scope.sort_by = function(newSortingOrder) {
    if ($scope.sortingOrder == newSortingOrder)
        $scope.reverse = !$scope.reverse;

    $scope.sortingOrder = newSortingOrder;

  // After changing the sort order reset the entire array aligned 
  // to the new sort order or sort element orderby
    $scope.init();
};


                    
                });
                responsePromise.error(function(data, status, headers, config) {
                	$scope.dataLoaded = false;
                	$scope.isError = true;
                	$scope.errorList=data;
                	alert(data);
                	alert("AJAX failed!");
                });
            };

            $scope.myData.createOrder = function(item, event) {
            	alert('radio button clicked');
            }
        }]);
	
	myApp.controller(controllers);
 //myApp.SearchController.$inject = ['$scope', '$filter'];
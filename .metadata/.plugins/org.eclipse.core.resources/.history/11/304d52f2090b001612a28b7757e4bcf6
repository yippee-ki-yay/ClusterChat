(function() {
	angular.module("chatApp").controller("registerController", registerController);
	
	registerController.$inject = ['$scope', '$http', '$location'];
	
	function registerController($scope, $http, $location) {
		
		$scope.username = '';
		$scope.password = '';
		
		$scope.register = function() {
			var content = {"username": $scope.username, "password": $scope.password};
			
			$http.post('rest/users/register/', content).
				then(function(resp) {
					if (resp.data == "true") {
						$location.path("/login/");
					}else {
						$scope.errorMsg = "The username is already in use";
					}
				},
				function(resp) {
					console.log(resp);
				});
		}
		
	}
})();
(function() {
	angular.module("chatApp").controller("registerController", registerController);
	
	registerController.$inject = ['$scope', '$http', '$location', 'userService'];
	
	function registerController($scope, $http, $location, userService) {
		
		$scope.username = '';
		$scope.password = '';
		
		if(userService.getUser() != undefined) {
			$location.path("/chat/");
		}
		
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
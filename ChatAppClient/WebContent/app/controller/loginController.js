(function() {
	angular.module("chatApp").controller("loginController", loginController);
		
	loginController.$inject = ['$scope', '$http', '$location', "userService"];
	
	function loginController($scope, $http, $location, userService) {
		
		$scope.username = '';
		$scope.password = '';
		
		$scope.login = function() {
			var content = {"username": $scope.username, "password": $scope.password};
			
			$http.post('rest/users/login/', content).
				then(function(resp) {
					console.log(resp);
					if (resp.data != "false") {
						userService.login(resp.data);
						$location.path("/chat/");
					}else {
						$scope.errorMsg = "The username/password are incorrect or you are not registered";
					}
				},
				function(resp) {
					console.log(resp);
				}
				);
		}
		
	}
})();
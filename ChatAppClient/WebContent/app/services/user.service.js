(function() {

    angular
        .module('chatApp')
        .service('userService', userService);

    userService.$inject = ['$cookies'];

    function userService($cookies) {
    	
    	//just a temp function keeps track of the name
    	function login(user) {
    		$cookies.username = user;
    	}
    	
    	function getUser() {
    		return $cookies.username;
    	}
    	
    	function logout() {
    		$cookies.remove("username");
    		$cookies.username = undefined;
    	}
    	
    	return {
    		login : login,
    		getUser : getUser,
    		logout : logout
    	}
    	
    }
    
})();
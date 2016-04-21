(function() {

    angular
        .module('chatApp')
        .service('userService', userService);

    //userService.$inject = ['$http'];

    function userService() {
    	
    	var username;
    	
    	//just a temp function keeps track of the name
    	function login(user) {
    		username = user;
    	}
    	
    	function getUser() {
    		return username;
    	}
    	
    	function logout() {
    		username = "";
    	}
    	
    	return {
    		login : login,
    		getUser : getUser,
    		logout : logout
    	}
    	
    }
    
})();
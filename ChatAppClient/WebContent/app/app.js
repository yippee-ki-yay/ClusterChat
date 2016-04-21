var chatApp = angular.module("chatApp", ["ui.bootstrap", "ngRoute", "ngWebSocket"]);

chatApp.config(function($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl: "app/views/promo.html",
	})
	.when('/chat', {
		templateUrl: "app/views/chat.html",
		controller: "chatController"
	})
	.when('/login', {
		templateUrl: "app/views/login.html",
		controller: "loginController"
	})
	.when('/register', {
		templateUrl: "app/views/register.html",
		controller: "registerController"
	});
});

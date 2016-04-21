(function() {
	angular.module("chatApp").controller("chatController", chatController);
	
	chatController.$inject = ['$scope', '$websocket', 'userService'];
	
	function chatController($scope, $websocket, userService) {
	
		$scope.msgList = [];
		$scope.userList = [];
		
		var dataStream = $websocket('ws://localhost:8080/ChatAppClient/websocket');
		
		dataStream.onMessage(function(message) {
			
			var msg = JSON.parse(message.data);
			
			console.log(msg.type);
			
			if(msg.type == "msg") {
				$scope.msgList.push(msg);
			} else if(msg.type == "user") {
				$scope.userList.push(msg.name);
			}
			
			
		});
		
		$scope.sendMsg = function()
		{
			var privateMsg = false;
			var toUser = "";
			
			findUserInMsg(user, privateMsg, toUser);
			
			var msgData = {"type" : "msg", "content" : $scope.msgText, "from": userService.getUser()};
			
			if(privateMsg) {
				msgData.to = toUser;
			}
			
			dataStream.send(msgData);
			$scope.msgText = "";
		}
		
		$scope.sendUserLogin = (function() {
			var userData = {"type" : "user", "name" : userService.getUser()};
			
			dataStream.send(userData)
			
		})();
		
		function findUserInMsg(user, privateMsg, toUser) {
			for(var i = 0; i < $scope.userList.length; ++i) {
				var index = str.search($scope.userList[i].name + ":");
				if(index != -1){
					privateMsg = true;
					toUser = $scope.userList[i].name.substr(0, $scope.userList[i].name.length-1);
					return $scope.userList[i].name.substr(index, $scope.userList[i].name.length-1);
				} else {
					privateMsg = false;
					return $scope.userList[i].name;
				}
			}
		}
		
	};
})();

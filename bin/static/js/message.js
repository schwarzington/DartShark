
function ApplicationModel(stompClient) {
	var self = this;

	self.friends = ko.observableArray();
	self.onlineUsers = ko.observableArray();
	self.dartBoardUrl = ko.observable("dartboard.svg");
	self.username = ko.observable();
	self.firstName = ko.observable();
	self.conversation = ko.observable(new ImConversationModel(stompClient,this.username));
	self.notifications = ko.observableArray();
	self.csrfToken = ko.computed(function() {
		return JSON.parse($.ajax({
			type: 'GET',
			url: 'csrf',
			dataType: 'json',
			success: function() { },
			data: {},
			async: false
		}).responseText);
	}, this);

	$.ajax({
		type: 'GET',
			url: 'player/all',
			dataType: 'json',
			success: function(data) { 
				self.allUsers = ko.observableArray(data);
			},
			data: {},
			async: false
		})
	self.connect = function() {
		var headers = {};
		var csrf = self.csrfToken();
		headers[csrf.headerName] = csrf.token;
		stompClient.connect(headers, function(frame) {

			console.log('Connected ' + frame);
			self.username(frame.headers['user-name']);

			stompClient.subscribe("/user/queue/errors", function(message) {
					self.pushNotification("Error " + message.body);
			});
			stompClient.subscribe("/app/users", function(message) {
					console.log(message.body);
					var friends = JSON.parse(message.body);

					for(var i=0;i<friends.length;i++) {
						self.friendSignin({"username": friends[i]});
					}
			});
			stompClient.subscribe("/topic/friends/signin", function(message) {
					console.log("SignIn: " + message.body);
					var friends = JSON.parse(message.body);

					for(var i=0;i<friends.length;i++) {
							self.friendSignin(friends[i]);
					}
			});
			stompClient.subscribe("/topic/friends/signout", function(message) {
					console.log("SignOut: " + message.body);
					var friends = JSON.parse(message.body);

					for(var i=0;i<friends.length;i++) {
							self.friendSignout(friends[i]);
					}
			});
			stompClient.subscribe("/user/queue/messages", function(message) {
					self.conversation().receiveMessage(JSON.parse(message.body));
			});
		}, function(error) {
			self.pushNotification(error)
			console.log("STOMP protocol error " + error);
		});
	}
	
	self.partitioned = function (observableArray, count) {
		var rows, partIdx, i, j, arr;

		arr = observableArray();

		rows = [];
		for (i = 0, partIdx = 0; i < arr.length; i += count, partIdx += 1) {
			rows[partIdx] = [];
			for (j = 0; j < count; j += 1) {
				if (i + j >= arr.length) {
					break;
				}
				rows[partIdx].push(arr[i + j]);
			}
		}
		return rows;
	};

	self.online = function(username){
		if(onlineUsers.indexOf(username) > -1) {
			return true;
		} else {
			return false;
		}
	}

	self.pushNotification = function(text) {
		self.notifications.push({notification: text});
		if (self.notifications().length > 5) {
			self.notifications.shift();
		}
	}

	self.logout = function() {
		stompClient.disconnect(function() {
			window.location.href = "../login?logout"; }
		);
		
	}

	self.friendSignin = function(friend) {
		if(friend.username.username === self.username()){
			console.log("Hey this is me!");
			friend.username.primary = true;
		} else {
			friend.username.primary = false;
		}
		self.onlineUsers.push(friend.username);
		self.friends.push(friend);
	}

	self.friendSignout = function(friend) {
		self.onlineUsers.remove(friend.username);
		var r = self.friends.remove(
			function(item) {
				item.username == friend.username
			}
		);
		self.friends(r);
	}
}

function ImFriend(data) {
	var self = this;

	self.username = data.username;
	self.firstName = data.first_name;
}

function ImConversationModel(stompClient,from) {
	var self = this;
	self.stompClient = stompClient;
	self.from = from;
	self.to = ko.observable(new ImFriend('null'));
	self.draft = ko.observable('')

	self.messages = ko.observableArray();

	self.receiveMessage = function(message) {
		var elem = $('#chat');
		var isFromSelf = self.from() == message.from;
		var isFromTo = self.to().username == message.from;
		if(!(isFromTo || isFromSelf)) {
				self.chat(new ImFriend({"username":message.from}))
		}

		var atBottom = (elem[0].scrollHeight - elem.scrollTop() == elem.outerHeight());

		self.messages.push(new ImModel(message));

		if (atBottom)
				elem.scrollTop(elem[0].scrollHeight);
	};

	self.chat = function(to) {
		self.to(to);
		self.draft('');
		self.messages.removeAll()
		$('#trade-dialog').modal();
	}

	self.send = function() {
		var data = {
			"created" : new Date(),
			"from" : self.from(),
			"to" : self.to().username,
			"message" : self.draft()
		};
		var destination = "/app/im"; // /queue/messages-user1
		stompClient.send(destination, {}, JSON.stringify(data));
		self.draft('');
	}
};

function ImModel(data) {
	var self = this;

	self.created = new Date(data.created);
	self.to = data.to;
	self.message = data.message;
	self.from = data.from;
	self.messageFormatted = ko.computed(function() {
			return self.created.getHours() + ":" + self.created.getMinutes() + ":" + self.created.getSeconds() + " - " + self.from + " - " + self.message;
	})
};


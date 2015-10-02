/**
 * http://usejsdoc.org/
 */

(function () {
	var app = angular.module("comments", []);

	//Used to reload data
	var update = null;
	
	app.directive("commentList", function() {
		return {
			restrict: 'E',
			templateUrl: '/templates/comment-list.html',
			controller: [ '$http', function ($http) {
				var controller = this; 
				this.comments = [];
				this.error = null;
				this.getComments = function () {
						$http.get("/api/comment/").then(function(res){
							controller.error = null;
							controller.comments = res.data;
						}, function(error) {
							controller.error = "Could not retrieve comments";
						});	
				};
			
				update = function() {
					controller.getComments();	
				};
			
				this.delete = function (id) {
					$http.delete('/api/comment/' + id).then(function(res) {
						controller.getComments();
					}, function(res){
						controller.error = "Could not delete comment";
					});
				}
				this.getComments();
				
			}],
			controllerAs: 'commentCtrl'
			
		};
	});
	
	app.directive("commentForm", function () {
		return {
			restrict: 'E',
			templateUrl: '/templates/comment-form.html',
			controller: [ '$http', function($http) {
				var controller = this;
				this.error = null;
				this.comment = {};
				this.submit = function() {
					$http.post('/api/comment/', JSON.stringify(this.comment)).then(function(res) {
						controller.error = null;
						controller.comment = {};
						if (update)
							update();
					}, function(res){
						controller.error = "Could not create comment.";
					});
				}
			}],	
			controllerAs: 'formCtrl'
		};
	});
	
	
})();

angular.module('app.genericUpload', ['angularFileUpload']);
angular.module('app').requires.push('app.genericUpload');
angular.module('app.genericUpload').directive('genericUpload', function() {
	return {
		restrict : 'E',
		templateUrl : 'app/common/directives/upload/upload.tpl.html',
		transclude: false,
		scope : {
			options : '=',
			onSuccess : '&'
		},
		controller : function($scope, $attrs, $rootScope, FileUploader, eduOverlayFactory) {
			// GESTION DE ARCHIVOS
			$scope.uploader = new FileUploader($scope.options);
			
			$scope.uploader.onErrorItem = function(item, response, status, headers){
		    	$rootScope.overlay.hideOverlay();
		    	$rootScope.overlay.errorMessage(response);
		    	console.log(response);
			};
			
			$scope.uploader.onSuccessItem = function(item, response, status, headers) {
		    	$rootScope.overlay.hideOverlay();
		    	console.log("@@@@@@@@@");
		    	$scope.onSuccess();
			};
		}
	};
});


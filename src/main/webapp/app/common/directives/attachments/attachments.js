angular.module('app.genericAttachments', []);
angular.module('app').requires.push('app.genericAttachments');
angular.module('app.genericAttachments').directive('genericAttachments', function() {
	return {
		restrict : 'E',
		templateUrl : 'app/common/directives/attachments/attachments.tpl.html',
		transclude: false,
		scope : {
			data: '=',
			options : '=',
			onSuccess : '&',
		},
		controller : function($scope, $attrs, $rootScope, FileUploader, dataFactoryApp) {
			var apiAttNames = dataFactoryApp(appConfig.urlBaseAttach+'/getAllAttachmentsNames/:dbid/:id', '');
			var apiAttNamesDel = dataFactoryApp(appConfig.urlBaseAttach+'/deleteAttachment/:dbid/:id', '');
			var apiAttNamesView = dataFactoryApp(appConfig.urlBaseAttach+'/viewAttachment/:dbid/:id', '');
			var apiAttNamesUpdate = dataFactoryApp(appConfig.urlBaseAttach+'/updateAttachment/:dbid/:id', '');
			var apiDoc = dataFactoryApp(appConfig.urlBaseApp+'/getPDF/:dbid/:id', '');
			
			$scope.options.view = $scope.options.view!=undefined?$scope.options.view:true;
			$scope.options.edit = $scope.options.edit!=undefined?$scope.options.edit:true;
			$scope.options.update = $scope.options.update!=undefined?$scope.options.update:true;
			$scope.options.delete = $scope.options.delete!=undefined?$scope.options.delete:true;
			$scope.options.pdf = $scope.options.pdf!=undefined?$scope.options.pdf:true;
			
			$scope.eliminarAttachment = function (att) {
				$rootScope.overlay.okCancelMessage('¿Realmente quiere eliminar este adjunto? '+att,function(){
					$rootScope.overlay.showOverlay();
					var dbid=$scope.options.dbid;
					var id =$scope.options.unid;
					apiAttNamesDel.remove({dbid:dbid,id:id,name:att}, function(data){
						$rootScope.overlay.hideOverlay();
						$scope.onSuccess();
					}, $rootScope.overlay.errorManager);
				});
			}
			
			$scope.verAttachment = function (att) {
				var dbid=$scope.options.dbid;
				var id =$scope.options.unid;
				window.open(appConfig.urlBaseAttach+'/viewAttachment/'+dbid+'/'+id+'?name='+att);
			}

			$scope.editarAttachment = function (att) {
				var dbid=$scope.options.dbid;
				var id =$scope.options.unid;
				window.open(appConfig.urlBaseAttach+'/editAttachment/'+dbid+'/'+id+'?name='+encodeURIComponent(att));
				$rootScope.overlay.showMessage("Compruebe que se ha abierto open office en la barra de tareas. Y no olvide pulsar 'actualizar' despues");
			}

			$scope.actualizarAttachment = function (att) {
				$rootScope.overlay.showOverlay();
				var dbid=$scope.options.dbid;
				var id =$scope.options.unid;
				apiAttNamesUpdate.get({dbid:dbid,id:id,name:att}, function(data){
					$rootScope.overlay.hideOverlay();
					$rootScope.overlay.successMessage("Correcto");
					$scope.onSuccess();
				}, $rootScope.overlay.errorManager);

				//window.open(appConfig.urlBaseAttach+'/updateAttachment/'+dbid+'/'+id+'?name='+att);
			}	

			$scope.getPDF = function (att) {
				$rootScope.overlay.showOverlay();
				var dbid=$scope.options.dbid;
				var id =$scope.options.unid;
				var table =$scope.options.table;
				var addPDF =$scope.options.addPDF;
				apiDoc.execute({dbid:dbid,id:id,name:att,table:table,addPDF:addPDF},{}, function(data){
					$rootScope.overlay.hideOverlay();
					$rootScope.overlay.successMessage("Correcto");
					$scope.onSuccess();
				}, $rootScope.overlay.errorManager);

				//window.open(appConfig.urlBaseAttach+'/updateAttachment/'+dbid+'/'+id+'?name='+att);
			}	
		}
	};
});


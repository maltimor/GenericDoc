angular.module('app.database', []);
angular.module('app').requires.push('app.database');
angular.module('app.database')
.config(function config($routeProvider) {
	$routeProvider.when('/database', {
		controller: 'DatabaseCtrl',
		templateUrl: 'app/partials/database/database.html'
	});
})
.controller('DatabaseCtrl', function($rootScope,$scope,$http,$location,$route,
		overlayFactory,dataFactoryApp,paginationFactory,factoryCRUD,factoryPagCrud) {
	
	//compruebo que tengo el rol requerido para dicha pantalla
	if (!$rootScope.checkAnyPerfil(appConfig.adminProfile)) return;

	var apiAttNames = dataFactoryApp(appConfig.urlBaseAttach+'/getAllAttachmentsNames/:dbid/:unid', '');

	$scope.apiApp = dataFactoryApp(appConfig.urlBase+'/DATABASE/:id', '');
	$scope.pagination = paginationFactory();
	$scope.apiCrud = factoryCRUD($scope.apiApp, $rootScope.overlay);
	$scope.apiPagCrudFn = factoryPagCrud(1, $scope.apiCrud, $scope.pagination);
	$scope.data=[];
	$scope.dataSel={};
	$scope.showCategoria=false;
	$scope.insert=false;

	// GESTION DE ARCHIVOS
	$scope.uoptions={
		url:appConfig.urlBaseAttach+'/addAttachment',
		alias:'attach',
		//filters:['pdf'],
		autoUpload:false,
		removeAfterUpload: true,
		queueLimit: 1,
		withCredentials: true,
		formData: [{ dbid:'',
			unid:'',
			attach:''
		}]
	};
	
	$scope.attOptions={
		dbid:'',
		unid:''
	};
	
	//GESTION DEL LISTADO
	$scope.genOptions = {
			limit:10,
			orderby:'ID',
			order:'ASC',
			offset: 0,
			filter: '',
			fields:'*'
		};
	
	function mostrarDatos() {
		$rootScope.overlay.showOverlay();
		$scope.apiPagCrudFn.setOptions($scope.genOptions);
		$scope.apiPagCrudFn.getPage();
	}
	$scope.$on('pageLoaded', function(event, data) {
		$rootScope.overlay.hideOverlay();
		if (data.id==1) $scope.data = data.data;
	});
	$scope.ver = function(item) {
		console.log(item);
		$scope.dataSel=item;
		$scope.showCategoria=true;
		$scope.insert=false;
		//no cambio de hoja
		$scope.refreshAttachments();
	}
	$scope.volver = function() {
		$scope.dataSel={};
		$scope.showCategoria=false;
		mostrarDatos();
		//no cambio de hoja
	}
	$scope.sortSrc = function(param){
		$scope.apiPagCrudFn.setSort(param);
	}
	$scope.setPageSrc = function() {
		$rootScope.overlay.showOverlay();
		$scope.apiPagCrudFn.setPage();
	}
	
	$scope.newItem=function(){
		$scope.dataSel={};
		$scope.showCategoria=true;
		$scope.insert=true;
	}
	
	$scope.grabar = function () {
		$rootScope.overlay.showOverlay();

		if ($scope.insert){
			$scope.apiCrud.insert($scope.dataSel, function(data){
				$rootScope.overlay.hideOverlay();
				$scope.insert=false;
				$scope.dataSel=data;
			}, $rootScope.overlay.errorManager);
		} else {
			$scope.apiCrud.update({id:$scope.dataSel.ID}, $scope.dataSel, function(data){
				$rootScope.overlay.hideOverlay();
				$scope.insert=false;
				$scope.dataSel=data;
			}, $rootScope.overlay.errorManager);
		};
	}
	
	
	$scope.eliminar = function (ID) {
		$scope.overlay.okCancelMessage('¿Realmente quiere eliminar este elemento?',function(){
			$rootScope.overlay.showOverlay();
			$scope.apiCrud.remove({id:ID}, function(data){
				$rootScope.overlay.hideOverlay();
				mostrarDatos();
			}, $rootScope.overlay.errorManager);
		});
	}
	
	$scope.refreshAttachments = function(){
		console.log("#########");
		var dbid=$scope.dataSel.ID;
		var unid =$scope.dataSel.ID;

		$scope.uoptions.formData[0].dbid=dbid;
		$scope.uoptions.formData[0].unid=unid;

		$scope.attOptions.dbid=dbid;
		$scope.attOptions.unid=unid;
	
		//cargo los adjuntos del registro
		apiAttNames.getAll({dbid:dbid,unid:unid},function(data){
			$scope.dataSel.attNames=data;
			console.log(data);
		}, $rootScope.overlay.errorManager);
	}
	
	mostrarDatos();
	
});


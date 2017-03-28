angular.module('app.documentos', []);
angular.module('app').requires.push('app.documentos');
angular.module('app.documentos')
.config(function config($routeProvider) {
	$routeProvider.when('/documentos', {
		controller: 'DocumentosCtrl',
		templateUrl: 'app/partials/documentos/documentos.html'
	}).when('/documentos/:dbid/:unid', {
		controller: 'DocumentosCtrl',
		templateUrl: 'app/partials/documentos/documentos.html'
	})
})
.controller('DocumentosCtrl', function($rootScope,$scope,$http,$location,$route,lupaFactory,$sce,$routeParams,
		eduOverlayFactory,dataFactoryApp,paginationFactory,factoryCRUD,factoryPagCrud) {
	
	//compruebo que tengo el rol requerido para dicha pantalla
	if (!$rootScope.checkAnyPerfil(appConfig.adminProfile)) return;
	
	var apiAttNames = dataFactoryApp(appConfig.urlBaseAttach+'/getAllAttachmentsNames/:dbid/:id', '');
	var apiDocs = dataFactoryApp(appConfig.urlBase+'/DOC/:id', '');
	var apiSecciones = dataFactoryApp(appConfig.urlBase+'/SECCION/:id', '');
	var apiModeloSeccion = dataFactoryApp(appConfig.urlBase+'/MODELO_SECCION/:id', '');
	var apiViewModeloSeccion = dataFactoryApp(appConfig.urlBase+'/VIEW_MODELO_SECCION/:id', '');
	var apiModeloActualizar = dataFactoryApp(appConfig.urlBaseApp+'/actualizaModelo/:dbid/:unid', '');
	
	$scope.params=$routeParams;
	
	$scope.apiApp = dataFactoryApp(appConfig.urlBase+'/VIEW_DOCUMENTOS/:id', '');
	$scope.pagination = paginationFactory();
	$scope.apiCrud = factoryCRUD($scope.apiApp, $rootScope.overlay);
	$scope.apiPagCrudFn = factoryPagCrud(1, $scope.apiCrud, $scope.pagination);
	
	$scope.data=[];
	$scope.dataExample=[];
	$scope.dataSel={};
	$scope.dataSelSeccion={};
	$scope.secciones=[];
	$scope.databaseSel=undefined;
	$scope.showCategoria=false;
	$scope.insert=false;

	// GESTION DE ARCHIVOS
	$scope.attOptions={
		view:true,
		edit:false,
		delete:false,
		update:false,
		pdf:true,
		table: 'DOC',
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
		//actualizo el uploader con los ids
		$scope.refreshAttachments();
	}
	$scope.volver = function() {
		if ($scope.params.url!=undefined) {
			var url = $scope.params.url;
			console.log(url);
			
			var query=window.location.href;
			var i1=query.indexOf('?');
			if (i1>0) url+=query.substr(i1);

			console.log(url);
			console.log($location.search());
		}
		
		
/*		if ($scope.params.url!=undefined) {
			window.location.href=$scope.params.url;
			return;
		}
		
		$scope.dataSel={};
		$scope.showCategoria=false;
		mostrarDatos();*/
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
		if ($scope.databaseSel==undefined){
			$scope.overlay.errorMessage('No se ha seleccionado una base de datos');
			return;
		}

		$scope.dataSel={};
		$scope.secciones=[];
		$scope.dataSel.ID_DB=$scope.databaseSel.ID;
		$scope.dataSel.DB_FILENAME=$scope.databaseSel.FILENAME;
		$scope.showCategoria=true;
		$scope.insert=true;
	}
	
	$scope.grabar = function () {
		$rootScope.overlay.showOverlay();

		if ($scope.insert){
			apiDocs.insert($scope.dataSel, function(data){
				$rootScope.overlay.hideOverlay();
				$scope.insert=false;
				$scope.dataSel=data;
			}, $rootScope.overlay.errorManager);
		} else {
			apiDocs.update({id:$scope.dataSel.ID}, $scope.dataSel, function(data){
				$rootScope.overlay.hideOverlay();
				$scope.insert=false;
				$scope.dataSel=data;
			}, $rootScope.overlay.errorManager);
		};
	}
	
	
	$scope.eliminar = function (ID) {
		$scope.overlay.okCancelMessage('¿Realmente quiere eliminar este elemento?',function(){
			$rootScope.overlay.showOverlay();
			apiDocs.remove({id:ID}, function(data){
				$rootScope.overlay.hideOverlay();
				mostrarDatos();
			}, $rootScope.overlay.errorManager);
		});
	}

	$scope.$watch('user.attr',function(){
		if (!$rootScope.user || !$rootScope.user.attr) return;
		
		if ($rootScope.user.attr.DATABASE) $scope.databaseSel = $rootScope.user.attr.DATABASE;
	});	

	$scope.refreshAttachments = function(){
		console.log("#########");
		var dbid=$scope.dataSel.ID_DB;
		var id =$scope.dataSel.ID;

		$scope.attOptions.dbid=dbid;
		$scope.attOptions.unid=id;
	
		//cargo los adjuntos del registro
		apiAttNames.getAll({dbid:dbid,id:id},function(data){
			$scope.dataSel.attNames=data;
			console.log(data);
			$scope.refreshClobs();
		}, $rootScope.overlay.errorManager);
	}

	//LUPAS
	var lupaModelos={
			title:'Seleccione un docuento',
			url:appConfig.urlBase+'/VIEW_MODELOS/:id',
			orderby:'FILENAME',
			fields:'ID_DB,ID,DB_FILENAME,FILENAME',
			size:'lg'
	}
	$scope.openLupaModelos=function(){
		lupaFactory().open(lupaModelos,function(data){
			$location.url('/newDoc/'+data.ID_DB+'/'+data.ID);
		},function(data){
		});
	}
	
	$scope.refreshClobs = function(){
		console.log("#########");
		var id =$scope.dataSel.ID;
		apiDocs.get({id:id},function(data){
			$scope.TXT=data.TXT;
			$scope.HTML=$sce.trustAsHtml(data.HTML);
			console.log(data);
		}, $rootScope.overlay.errorManager);
	}
	
	if (!$routeParams.unid) mostrarDatos()
	else {
		console.log('===============DOCUMENTOS========================');
		console.log(window.location);
		console.log(window.location.href);
		console.log(window.location.hash);
		console.log($location.search());
		console.log($location.path());
		console.log('=======================================');
		
		//caso especial donde routeParams tiene valor
		$scope.apiApp.get({id:$routeParams.unid},function(data){
			$scope.ver(data);
		}, $rootScope.overlay.errorManager);
	}
});


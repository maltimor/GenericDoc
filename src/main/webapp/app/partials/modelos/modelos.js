angular.module('app.modelos', []);
angular.module('app').requires.push('app.modelos');
angular.module('app.modelos')
.config(function config($routeProvider) {
	$routeProvider.when('/modelos', {
		controller: 'ModelosCtrl',
		templateUrl: 'app/partials/modelos/modelos.html'
	});
})
.controller('ModelosCtrl', function($rootScope,$scope,$http,$location,$route,lupaFactory,$sce,
		eduOverlayFactory,dataFactoryApp,paginationFactory,factoryCRUD,factoryPagCrud) {
	
	//compruebo que tengo el rol requerido para dicha pantalla
	if (!$rootScope.checkAnyPerfil(appConfig.adminProfile)) return;
	
	var apiAttNames = dataFactoryApp(appConfig.urlBaseAttach+'/getAllAttachmentsNames/:dbid/:id', '');
	var apiModelos = dataFactoryApp(appConfig.urlBase+'/MODELO/:id', '');
	var apiSecciones = dataFactoryApp(appConfig.urlBase+'/SECCION/:id', '');
	var apiModeloSeccion = dataFactoryApp(appConfig.urlBase+'/MODELO_SECCION/:id', '');
	var apiViewModeloSeccion = dataFactoryApp(appConfig.urlBase+'/VIEW_MODELO_SECCION/:id', '');
	var apiModeloActualizar = dataFactoryApp(appConfig.urlBaseApp+'/actualizaModelo/:dbid/:unid', '');
	
	$scope.apiApp = dataFactoryApp(appConfig.urlBase+'/VIEW_MODELOS/:id', '');
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
		table: 'MODELO',
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
		$scope.refreshSecciones();
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
			apiModelos.insert($scope.dataSel, function(data){
				$rootScope.overlay.hideOverlay();
				$scope.insert=false;
				$scope.dataSel=data;
			}, $rootScope.overlay.errorManager);
		} else {
			apiModelos.update({id:$scope.dataSel.ID}, $scope.dataSel, function(data){
				$rootScope.overlay.hideOverlay();
				$scope.insert=false;
				$scope.dataSel=data;
			}, $rootScope.overlay.errorManager);
		};
	}
	
	
	$scope.eliminar = function (ID) {
		$scope.overlay.okCancelMessage('¿Realmente quiere eliminar este elemento?',function(){
			$rootScope.overlay.showOverlay();
			apiModelos.remove({id:ID}, function(data){
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
	var lupaSecciones={
			title:'Seleccione una seccion',
			url:appConfig.urlBase+'/VIEW_SECCIONES/:id',
			orderby:'FILENAME',
			fields:'DB_FILENAME,FILENAME',
			size:'lg'
	}
	$scope.openLupaSecciones=function(){
		//lupaSecciones.prefilter=pre;
		
		lupaFactory().open(lupaSecciones,function(data){
			$scope.addSeccion(data);
		},function(data){
		});
	}
	
	$scope.addSeccion = function(item){
		var orden = 1;
		if ($scope.secciones && $scope.secciones.length>0) orden = $scope.secciones[$scope.secciones.length-1].ORDEN+1
		var reg = {
			ID_DB:$scope.dataSel.ID_DB,
			ID_MODELO:$scope.dataSel.ID,
			ID_SECCION:item.ID,
			ORDEN: orden
		};
		apiModeloSeccion.insert(reg,function(data){
			$scope.refreshSecciones();
		}, $rootScope.overlay.errorManager);
	}
	
	$scope.eliminarSeccion = function(ID){
		apiModeloSeccion.remove({id:ID},function(data){
			$scope.refreshSecciones();
		}, $rootScope.overlay.errorManager);
	}

	$scope.subir = function(index){
		console.log(index);
		if (index>0) {
			var item1=$scope.secciones[index-1];
			var item2=$scope.secciones[index];
			var aux = item1.ORDEN;
			item1.ORDEN=item2.ORDEN;
			item2.ORDEN=aux;
			apiModeloSeccion.update({id:item1.ID},item1,function(data){
				apiModeloSeccion.update({id:item2.ID},item2,function(data){
					$scope.refreshSecciones();
				}, $rootScope.overlay.errorManager);
			}, $rootScope.overlay.errorManager);
		}
	}

	$scope.bajar = function(index){
		console.log(index);
		if (index<$scope.secciones.length-1) {
			var item1=$scope.secciones[index];
			var item2=$scope.secciones[index+1];
			var aux = item1.ORDEN;
			item1.ORDEN=item2.ORDEN;
			item2.ORDEN=aux;
			apiModeloSeccion.update({id:item1.ID},item1,function(data){
				apiModeloSeccion.update({id:item2.ID},item2,function(data){
					$scope.refreshSecciones();
				}, $rootScope.overlay.errorManager);
			}, $rootScope.overlay.errorManager);
		}
	}
	
	$scope.actualizarModelo = function(){
		apiModeloActualizar.execute({dbid:$scope.dataSel.ID_DB,unid:$scope.dataSel.ID,name:$scope.dataSel.FILENAME},{},function(data){
			$scope.refreshAttachments();
		}, $rootScope.overlay.errorManager);
	}

	$scope.refreshSecciones = function(){
		var opts = {
				limit:100,
				orderby:'ORDEN',
				order:'ASC',
				offset: 0,
				filter: '[ID_DB]=='+$scope.databaseSel.ID+' AND [ID_MODELO]=='+$scope.dataSel.ID,
				fields:'*'
			};
		$scope.secciones = [];
		apiViewModeloSeccion.getAll(opts,function(data){
			$scope.secciones = data;
		}, $rootScope.overlay.errorManager);
	}
	
	$scope.refreshClobs = function(){
		console.log("#########");
		var id =$scope.dataSel.ID;
		apiModelos.get({id:id},function(data){
			$scope.TXT=data.TXT;
			$scope.HTML=$sce.trustAsHtml(data.HTML);
			console.log(data);
		}, $rootScope.overlay.errorManager);
	}
		
	mostrarDatos();
	
});


angular.module('app.envio', ['angularFileUpload']);
angular.module('app').requires.push('app.envio');
angular.module('app.envio')
.config(function config($routeProvider) {
	$routeProvider.when('/envio', {
		controller: 'EnvioCtrl',
		templateUrl: 'app/partials/envio/envio.html'
	});
})
.controller('EnvioCtrl', function($rootScope,$scope,$http,$location,FileUploader,
		eduOverlayFactory,dataFactoryApp,paginationFactory,factoryCRUD,factoryPagCrud) {
	
	$scope.showPanel=false;
	$scope.apiApp = dataFactoryApp(appConfig.urlBase+'/solicitudes/:id', '');
	$scope.pagination = paginationFactory();
	$scope.apiCrud = factoryCRUD($scope.apiApp, $rootScope.overlay);
	$scope.apiPagCrudFn = factoryPagCrud(1, $scope.apiCrud, $scope.pagination);
	$scope.data=[];
	var i =0;

	$scope.archivos=[];
	$scope.addArchivo=function() {


		$scope.archivos.push({archivo:''});
//		var h = document.getElementById("prueba");
//		//console.log(h.id)
//
//		h.id = "prueba"+i;
//		
//
//		console.log(h.id)
//	
//		i++;
	}
	$scope.deleteArchivo=function(a){
		var i = $scope.archivos.indexOf(a);
		if (i>-1) $scope.archivos.splice(i,1);
	//	if ($scope.archivos.length==0) $scope.uoptions.formData[0].rechazo='N';
	}
	$scope.destinatarios=[];
	$scope.addDestinatario=function(){
		if ($scope.destinatarios.length==0) $scope.uoptions.formData[0].rechazo='N';
		$scope.destinatarios.push({destinatario:''});
		
//		var h = document.getElementById("prueba");
//		//console.log(h.id)
//
//		h.id = "prueba"+i;
//		
//
//		console.log(h.id)
//	
//		i++;
	}
	$scope.deleteDestinatario=function(d){
		var i = $scope.destinatarios.indexOf(d);
		if (i>-1) $scope.destinatarios.splice(i,1);
		if ($scope.destinatarios.length==0) $scope.uoptions.formData[0].rechazo='N';
	}
	
		
	$scope.uploader = new FileUploader();
	$scope.uoptions={
		url:'services/portafirmasService/uploadfile',
		filters:['pdf'],
		autoUpload:false,
		removeAfterUpload: true,
		queueLimit: 2,
		withCredentials: true,
		formData: [{ destinatario:'',
			descripcion:'',
			archivo:''
		}]
	};
	
	$scope.uploader.onCompleteAll = function(){
		$rootScope.overlay.hideOverlay();
	};
	
	$scope.uploader.onErrorItem = function(item, response, status, headers){
    	$rootScope.overlay.hideOverlay();
    	var error = response.error+"<br>"+response.descripcion;
    	error = error.replace(/(?:\r\n|\r|\n)/g, '<br />');
    	$rootScope.overlay.errorMessage(error);
    	console.log(response);
    	$location.url('/');
	};
	
	$scope.uploader.onSuccessItem = function(item, response, status, headers) {
    	$rootScope.overlay.hideOverlay();
    	var msg = "idSolicitud:"+response.idSolicitud+"<br>validaci&oacute;nId"+response.validacionId
    	msg=msg.replace(/(?:\r\n|\r|\n)/g, '<br />');
    	$rootScope.overlay.successMessage(msg);
    	console.log(response);
    	
    	console.log(item.file.name);
    	
    	console.log(item);
    	$location.url('/');
	};

	$scope.enviar=function(){
		
		
//		var l = $scope.uoptions.queueLimit;
//		
//		var q = $scope.uploader.queue;
//		
//		if (q.length==0) {
//
//
//
//			$rootScope.overlay.errorMessage('Debe seleccionar un fichero');
//
//			return;
//		}
//		else if (q.length > l) {
//
//
//			$rootScope.overlay.errorMessage('El máximo de ficheros permitidos es 2');
//			return;
//			
//		}
		var dest=$scope.uoptions.formData[0].destinatario;
		/*if (dest==''){
			$rootScope.overlay.errorMessage('El campo destinatario no puede estar vac&iacute;o');
			return;
		}*/
		//caso especial para los destinatarios
		/*var d2 = $scope.destinatarios;
		console.log(d2.length);
		if (d2.length>0){
			var todosLlenos=true;
			for(var i=0;i<d2.length;i++){
				if (d2[i].destinatario=='') todosLlenos=false;
				else dest+="~"+d2[i].destinatario;
			}
			if (!todosLlenos){
				$rootScope.overlay.errorMessage('Todos los destintarios deben completarse');
				return;
			}
		}*/
			
		if ($scope.uoptions.formData[0].descripcion==''){
			$rootScope.overlay.errorMessage('El campo asunto no puede estar vac&iacute;o');
			return;
		}
		

		//legadoas aui dest contiene una lista de destinatarios ordenada
		$scope.uoptions.formData[0].destinatario=dest;

		//$rootScope.overlay.showOverlay();
		//$scope.uploader.uploadAll();
		
	//	var j = document.getElementById("prueba1");
		//console.log(j).input;

	
//		var allElements = document.getElementsByTagName("*");
//		var allIds = [];
//		for (var i = 0, n = allElements.length; i < n; ++i) {
//		  var el = allElements[i];
//		  console.log(el.id);
//		  if (el.id) { allIds.push(el.id); }
//		}
//		console.log(allIds);
//		
//		 var file = document.getElementById('prueba').files[0];
//		  if (file) {
//		    var fileSize = 0;
//		    if (file.size > 1024 * 1024)
//		      fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
//		    else
//		      fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
//		          
//		    console.log(file.name)
//		   console.log(fileSize) 
//		    console.log(file.type) 
//
//		
//		  }
	
		  var f= document.getElementById("kk");
		f.action='services/portafirmasService/uploadfile';
		f.method="POST"
		f.submit();
		  
		  
	}

	//GESTION DEL LISTADO
	$scope.muestraListado=function(){
		if ($scope.showPanel){
			$scope.showPanel=false;
		} else {
			$scope.showPanel=true;
			mostrarDatos();
		}
	}
	
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
		$scope.data = data.data;
	});
	$scope.ver = function(item) {
		console.log(item);
		$location.url('solicitud/' + item.ID);
	}
	$scope.sortSrc = function(param){
		$scope.apiPagCrudFn.setSort(param);
	}
	$scope.setPageSrc = function() {
		$rootScope.overlay.showOverlay();
		$scope.apiPagCrudFn.setPage();
	}
	
/*	
	
	$scope.internalControl.upload = function (idxFile) {
        console.log('llamada a file upload file:' + idxFile);
        if ($scope.options.type == 'upload') {
          $scope.uploader.queue[idxFile - 1].upload();
        }
      };
      $scope.internalControl.filesInQueue = function () {
        if ($scope.options.type == 'upload') {
          return $scope.uploader.queue.length;
        } else {
          return 0;
        }
      };
      
      
   // CONTROL TYPE= uploader
      // ---
      // create a uploader with options
      var uploader = $scope.uploader = new FileUploader({ url: $scope.options.url });
      // FILTERS
      uploader.filters.push({
        name: 'customFilter',
        fn: function (item, options) {
          return this.queue.length < 10;
        }
      });
      // CALLBACKS
      uploader.onWhenAddingFileFailed = function (item, filter, options) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onWhenAddingFileFailed == 'function') {
          $scope.options.fieldListeners.onWhenAddingFileFailed(item, filter, options);
        }
      };
      uploader.onAfterAddingFile = function (fileItem) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onAfterAddingFile == 'function') {
          $scope.options.fieldListeners.onAfterAddingFile(fileItem);
        }
      };
      uploader.onAfterAddingAll = function (addedFileItems) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onAfterAddingAll == 'function') {
          $scope.options.fieldListeners.onAfterAddingAll(addedFileItems);
        }
      };
      uploader.onBeforeUploadItem = function (item) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onBeforeUploadItem == 'function') {
          $scope.options.fieldListeners.onBeforeUploadItem(item);
        }
      };
      uploader.onProgressItem = function (fileItem, progress) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onProgressItem == 'function') {
          $scope.options.fieldListeners.onProgressItem(fileItem, progress);
        }
      };
      uploader.onProgressAll = function (progress) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onProgressAll == 'function') {
          $scope.options.fieldListeners.onProgressAll(progress);
        }
      };
      uploader.onSuccessItem = function (fileItem, response, status, headers) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onSuccessItem == 'function') {
          $scope.options.fieldListeners.onSuccessItem(fileItem, response, status, headers);
        }
      };
      uploader.onErrorItem = function (fileItem, response, status, headers) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onErrorItem == 'function') {
          $scope.options.fieldListeners.onErrorItem(fileItem, response, status, headers);
        }
      };
      uploader.onCancelItem = function (fileItem, response, status, headers) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onCancelItem == 'function') {
          $scope.options.fieldListeners.onCancelItem(fileItem, response, status, headers);
        }
      };
      uploader.onCompleteItem = function (fileItem, response, status, headers) {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onCompleteItem == 'function') {
          $scope.options.fieldListeners.onCompleteItem(fileItem, response, status, headers);
        }
      };
      uploader.onCompleteAll = function () {
        if ($scope.options.hasOwnProperty('fieldListeners') && typeof $scope.options.fieldListeners.onCompleteAll == 'function') {
          $scope.options.fieldListeners.onCompleteAll();
        }
      };
      
      
      $templateCache.put('directives/edu-field-upload-tpl-2.html', '<div class="form-group {{options.col}}" ng-class="{\'has-error\':  $invalid, \'has-success\': !$invalid && $dirty}" style=position:relative><label for={{id}}>{{options.label}} {{options.required ? \'*\' : \'\'}}</label><div class={{options.col}}><input ng-if=options.multiple nv-file-select="" uploader=uploader type=file multiple> <input ng-if=!options.multiple nv-file-select="" uploader=uploader type="file"></div><div class={{options.col}} style="margin-bottom: 40px"><p>Archivos en cola: <strong ng-repeat="item in uploader.queue">{{ item.file.name }}</strong></p><table class=table><thead><tr ng-show=options.showbuttons><th width=50%>Nombre</th><th ng-show=uploader.isHTML5>Tama\xf1o</th><th>Estado</th><th>Acciones</th></tr></thead><tbody><tr ng-repeat="item in uploader.queue" ng-show=options.showbuttons><td><strong>{{ item.file.name }}</strong></td><td ng-show=uploader.isHTML5 nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td><td class=text-center><span ng-show=item.isSuccess><i class="glyphicon glyphicon-ok"></i></span> <span ng-show=item.isCancel><i class="glyphicon glyphicon-ban-circle"></i></span> <span ng-show=item.isError><i class="glyphicon glyphicon-remove"></i></span></td><td nowrap><button type=button class="btn btn-success btn-xs" ng-click=item.upload() ng-disabled="item.isReady || item.isUploading || item.isSuccess"><span class="glyphicon glyphicon-upload"></span> Subir</button> <button type=button class="btn btn-warning btn-xs" ng-click=item.cancel() ng-disabled=!item.isUploading><span class="glyphicon glyphicon-ban-circle"></span> Cancelar</button> <button type=button class="btn btn-danger btn-xs" ng-click=item.remove()><span class="glyphicon glyphicon-trash"></span> Eliminar</button></td></tr></tbody></table><table ng-show=options.showprogressbar style=width:100%><tr><td>Progreso:</td></tr><tr><td><div class=progress><div class=progress-bar role=progressbar ng-style="{ \'width\': uploader.progress + \'%\' }"></div></div></td></tr></table></div><div class="help-block has-error" ng-show=$invalid style=position:absolute;top:50px><small class=error ng-show=$invalidRequired>Campo obligatorio. Introduzca un valor</small></div></div>');
      $templateCache.put('directives/edu-field-upload-tpl.html', '<div class="form-group {{options.col}}" ng-class="{\'has-error\':  $invalid, \'has-success\': !$invalid && $dirty}" style=position:relative><label for={{id}}>{{options.label}} {{options.required ? \'*\' : \'\'}}</label><div class={{options.col}}><input ng-if=!options.multiple type=file class=jfilestyle nv-file-select="" uploader=uploader id=jfilestyle-2 style="position: fixed; left: -500px"> <input ng-if=options.multiple multiple type=file class=jfilestyle nv-file-select="" uploader=uploader data-buttontext="Find file" id=jfilestyle-2 style="position: fixed; left: -500px"><div class=jquery-filestyle style="display: inline"><input disabled value="{{uploader.queue[0].file.name}}"><label for=jfilestyle-2><i class="fa fa-folder-open"></i> <span>{{options.label}}</span></label></div></div><div class={{options.col}} style="margin-bottom: 40px"><table class=table><thead><tr ng-show=options.showbuttons><th width=50%>Nombre</th><th ng-show=uploader.isHTML5>Tama\xf1o</th><th>Estado</th><th>Acciones</th></tr></thead><tbody><tr ng-repeat="item in uploader.queue" ng-show=options.showbuttons><td><strong>{{ item.file.name }}</strong></td><td ng-show=uploader.isHTML5 nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td><td class=text-center><span ng-show=item.isSuccess><i class="glyphicon glyphicon-ok"></i></span> <span ng-show=item.isCancel><i class="glyphicon glyphicon-ban-circle"></i></span> <span ng-show=item.isError><i class="glyphicon glyphicon-remove"></i></span></td><td nowrap><button type=button class="btn btn-success btn-xs" ng-click=item.upload() ng-disabled="item.isReady || item.isUploading || item.isSuccess"><span class="glyphicon glyphicon-upload"></span> Subir</button> <button type=button class="btn btn-warning btn-xs" ng-click=item.cancel() ng-disabled=!item.isUploading><span class="glyphicon glyphicon-ban-circle"></span> Cancelar</button> <button type=button class="btn btn-danger btn-xs" ng-click=item.remove()><span class="glyphicon glyphicon-trash"></span> Eliminar</button></td></tr></tbody></table><table ng-show=options.showprogressbar style=width:100%><tr><td>Progreso:</td></tr><tr><td><div class=progress><div class=progress-bar role=progressbar ng-style="{ \'width\': uploader.progress + \'%\' }"></div></div></td></tr></table></div><div class="help-block has-error" ng-show=$invalid style=position:absolute;top:50px><small class=error ng-show=$invalidRequired>Campo obligatorio. Introduzca un valor</small></div></div>');
      
	*/
});


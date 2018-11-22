
var appConfig={
	nombre: 'GenericDoc',
	version: '1.0',
	copyright: 'todos los derechos reservados',
	showView: 'authorized',
	user: { login:'', roles:[]},
	home: '/home',
	urlName: 'genericDoc',
	urlBase: 'services/genericRestService',
	urlBaseApp: 'services/genericDocService',
	urlBaseAttach: 'services/attachmentService',
	urlCasLogOut: 'URL_TO_LOGOUT',
	adminProfile: 'ADMIN',
	menu:[
	  	{
	  		"id":"1",
	  		"nombre":"Menu Admin",
	  		"proceso":"ADMIN",
	  		"submenu":[
	  		           {
	  			  		"id":"1",
	  			  		"nombre":"Menu1",
	  			  		"proceso":"ADMIN",
	  			  		"url":"",
	  			  		"submenu":[]
	  		           }
	  		           ]
	  	},
	  	{
	  		"id":"1",
	  		"nombre":"Menu Gestor",
	  		"proceso":"GESTOR",
	  		"submenu":[
	  		           {
		  			  		"id":"1",
		  			  		"nombre":"Menu2",
		  			  		"proceso":"GESTOR",
		  			  		"url":"",
		  			  		"submenu":[]
		  		           }
	  		           ]
	  	},
	  	{
	  		"id":"1",
	  		"nombre":"Administracion Global",
	  		"proceso":"ADMIN",
	  		"submenu":[
	  			  	{
	  			  		"id":"14",
	  			  		"nombre":"Servicios",
	  			  		"proceso":"ADMIN",
	  			  		"url":"servicios",
	  			  		"submenu":[]
	  			  	},
	  			  	{
	  			  		"id":"14",
	  			  		"nombre":"Databases",
	  			  		"proceso":"ADMIN",
	  			  		"url":"database",
	  			  		"submenu":[]
	  			  	},
	  			  	{
	  			  		"id":"14",
	  			  		"nombre":"Secciones",
	  			  		"proceso":"ADMIN",
	  			  		"url":"secciones",
	  			  		"submenu":[]
	  			  	},
	  			  	{
	  			  		"id":"14",
	  			  		"nombre":"Modelos",
	  			  		"proceso":"ADMIN",
	  			  		"url":"modelos",
	  			  		"submenu":[]
	  			  	},
	  			  	{
	  			  		"id":"14",
	  			  		"nombre":"Documentos",
	  			  		"proceso":"ADMIN",
	  			  		"url":"documentos",
	  			  		"submenu":[]
	  			  	}
  			  	]
	  	},
 	  	{
		  		"id":"1",
		  		"nombre":"Datos del Usuario",
		  		"proceso":"ADMIN,USER",
		  		"url":"datosUsuario",
		  		"submenu":[]
		 }
	  	]
};


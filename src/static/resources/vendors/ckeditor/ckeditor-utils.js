var CKEDITOR_UTILS = {
	create: (id) => {
		CKEDITOR.replace(id, {
			on: {
					instanceReady: function( ev1 ) {	        	
						ev1.editor.on('contentDom', function() {
							ev1.editor.document.on('drop', function(ev2) {	                	
								ev2.data.preventDefault(true); 
							});
							ev1.editor.document.on('dragover', function(ev2) {	                	
								ev2.data.preventDefault(true); 
							});
							ev1.editor.document.on('dragenter', function(ev2) {						
								ev2.data.preventDefault(true); 
							});             
						});
					}	    
				}
			}
		);		
	},
	getContent: (id) => {
		return CKEDITOR.instances['editor'].getData();
	},
	setContent: (id, content) => {
		CKEDITOR.instances['editor'].setData(content);	
	}
}

CKEDITOR.on('dialogDefinition', function(ev) {
    const dialogName = ev.data.name;
    const dialogDefinition = ev.data.definition;
    if ( dialogName == 'table' ) {
		const info = dialogDefinition.getContents('info');
        info.get('txtWidth')['default'] = '100%';
        
        const advanced = dialogDefinition.getContents('advanced');
        advanced.get("advCSSClasses")['default'] = "table table-bordered"
    }
});
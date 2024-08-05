var dialogWindowCallbacks = top.dialogWindowCallbacks || new Map();

/** 客製化 SetTimeout **/
var TimeoutUtils = top.TimeoutUtils || {
	setTimeout: (callback, interval) => {
		return setTimeout(callback, interval);
	},
	clearTimeout: (id) => {
		clearTimeout(id);
	}
}

/** 彈跳視窗 **/
var DialogUtils = top.DialogUtils || {
	/**
	 * 提醒視窗
	 * 參數:
	 * title = 視窗的 Title，預設為空白。
	 * text = 警示的文字，預設為空白。
	 * okText = 確認的文字，預設為"OK"。
	 * callback = 提醒視窗關閉時的Callback。
	 */
	alert: (options = {}) => {
		let title = options.title || "";
		let text = options.text || "";
		let okText = options.okText || "OK";
		let callback = options.callback || (() => { });

		let html = `
			<div class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
			  <div class="modal-dialog modal-dialog-centered">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">${title}</h5>
			        <button data-title-close type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			      </div>
			      <div class="modal-body">
			        ${text}
			      </div>
			      <div class="modal-footer modal-footer-center">
			        <button data-footer-close type="button" class="btn btn-warning" data-bs-dismiss="modal">${okText}</button>
			      </div>
			    </div>
			  </div>
			</div>
		`;
		let e = ElementUtils.createElement(html);

		e.addEventListener('hidden.bs.modal', () => {
			DocumentUtils.removeFromTop(e);
		});
		e.querySelector("[data-title-close]").addEventListener('click', () => {
			callback();
		});
		e.querySelector("[data-footer-close]").addEventListener('click', () => {
			callback();
		});

		DocumentUtils.appendToTop(e);

		new DocumentUtils.bootstrap.Modal(e).show();
	},
	/**
	 * 確認視窗
	 * 參數:
	 * title = 視窗的 Title，預設為空白。
	 * text = 警示的文字，預設為空白。
	 * cancelText = 取消的文字，預設為"Cancel"。
	 * confirmText = 確認的文字，預設為"Confirm"。
	 * onCancel = 點選取消時的Callback。
	 * onConfirm = 點選確認時的Callback。
	 */
	confirm: (options = {}) => {
		let title = options.title || "";
		let text = options.text || "";
		let cancelText = options.cancelText || "Cancel";
		let confirmText = options.confirmText || "Confirm";
		let onCancel = options.onCancel || (() => { });
		let onConfirm = options.onConfirm || (() => { });

		let html = `
			<div class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
			  <div class="modal-dialog modal-dialog-centered">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">${title}</h5>
			        <button data-title-close type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			      </div>
			      <div class="modal-body">
			        ${text}
			      </div>
			      <div class="modal-footer modal-footer-between">
			        <button data-footer-cancel type="button" class="btn btn-secondary" data-bs-dismiss="modal">${cancelText}</button>
        			<button data-footer-confirm type="button" class="btn btn-success" data-bs-dismiss="modal">${confirmText}</button>
			      </div>
			    </div>
			  </div>
			</div>
		`;

		let e = ElementUtils.createElement(html);

		e.addEventListener('hidden.bs.modal', function() {
			DocumentUtils.removeFromTop(e);
		});
		e.querySelector("[data-title-close]").addEventListener('click', () => {
			onCancel();
		});
		e.querySelector("[data-footer-cancel]").addEventListener('click', () => {
			onCancel();
		});
		e.querySelector("[data-footer-confirm]").addEventListener('click', () => {
			onConfirm();
		});

		DocumentUtils.appendToTop(e);

		new DocumentUtils.bootstrap.Modal(e).show();
	},
	/**
	 * 網頁視窗
	 * title = 視窗的 Title，預設為空白。
	 * data = 傳入的參數，會自動轉換為 Query String (localhost/?a=1&b=2)
	 * url = 要開啟的頁面網址
	 * callback = 視窗關閉時的事件。如有使用 DialogUtils.closeWindow() 方法則可以取得回傳參數。
	 *            範例: callback: (rtnVal) => {} 或 callback: function(rtnVal){}
	 * width = 視窗寬度，非必填，設定指定大小 "100px" or "100vw"
	 * height = 視窗高度，非必填，設定指定大小 "100px" or "100vh"
	 */
	window: (options = {}) => {
		let title = options.title || "";
		let data = options.data || {};
		let url = options.url || "";
		let callback = options.callback || (() => { });
		
		let width = options.width;
		let height = options.height;
		
		url = url += TextUtils.objectToQuerystring(data);
		let id = TextUtils.randonString(16);

		let html = `
			<div id="modal-${id}" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
			  <div class="modal-dialog modal-lg modal-dialog-centered modal-window" style="${ width ? 'max-width: ' + width : ''}">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">${title}</h5>
			        <button data-title-close type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			      </div>
			      <div class="modal-body modal-body-window" style="${height ? 'min-height: ' + height : ''}">
			      	<iframe id="${id}" class="modal-window-iframe" src="${url}"></iframe>
			      </div>
			    </div>
			  </div>
			</div>
		`;

		let e = ElementUtils.createElement(html);

		e.addEventListener('hidden.bs.modal', function() {
			DocumentUtils.removeFromTop(e);
			dialogWindowCallbacks.delete(id);
		});

		e.querySelector("[data-title-close]").addEventListener('click', () => {
			callback();
		});

		DocumentUtils.appendToTop(e);

		let windowModal = new DocumentUtils.bootstrap.Modal(e);
		windowModal.show();

		dialogWindowCallbacks.set(id, callback);

	},
	/**
	 * 使用 post 上傳檔案並開啟視窗
	 * title = 視窗的 Title，預設為空白。
	 * data = 傳入的參數，會自動轉換為 Query String (localhost/?a=1&b=2)
	 * files = 上傳的檔案陣列，型別為 HTML input file DOM 物件，
	 * url = 要開啟的頁面網址
	 * callback = 視窗關閉時的事件。如有使用 DialogUtils.closeWindow() 方法則可以取得回傳參數。
	 *            範例: callback: (rtnVal) => {} 或 callback: function(rtnVal){}
	 * width = 視窗寬度，非必填，設定指定大小 "100px" or "100vw"
	 * height = 視窗高度，非必填，設定指定大小 "100px" or "100vh"
	 */
	uploadFileToWindow(options = {}){
		let title = options.title || "";
		let data = options.data || {};
		let files = options.files || [];
		let url = options.url || "";
		let callback = options.callback || (() => { });
		
		let width = options.width;
		let height = options.height;
		
		url = url += TextUtils.objectToQuerystring(data);
		let id = TextUtils.randonString(16);

		let html = `
			<div id="modal-${id}" class="modal fade" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
			  <div class="modal-dialog modal-lg modal-dialog-centered modal-window" style="${ width ? 'max-width: ' + width : ''}">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">${title}</h5>
			        <button data-title-close type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			      </div>
			      <div class="modal-body modal-body-window" style="${height ? 'min-height: ' + height : ''}">
			      	<iframe id="${id}" name="${id}" class="modal-window-iframe"></iframe>
			      </div>
			    </div>
			  </div>
			</div>
		`;

		let e = ElementUtils.createElement(html);

		e.addEventListener('hidden.bs.modal', function() {
			DocumentUtils.removeFromTop(e);
			dialogWindowCallbacks.delete(id);
		});

		e.querySelector("[data-title-close]").addEventListener('click', () => {
			callback();
		});

		DocumentUtils.appendToTop(e);

		let windowModal = new DocumentUtils.bootstrap.Modal(e);
		windowModal.show();

		dialogWindowCallbacks.set(id, callback);

		let $form = ElementUtils.createElement(`
			<form class="d-none" action="${url}" method="post" target="${id}" enctype="multipart/form-data" />
		`) 

		for (let i = 0; i < files.length; i++) {
			const $file = files[i].cloneNode(true);
			$file.name = "files";
			$form.appendChild($file)
		}
		$form.appendChild(ElementUtils.createElement(`
			<input type="hidden" name="data" value='${JSON.stringify(data)}' />
		`));

		ElementUtils.showScreenMask();
		
		DocumentUtils.appendToTop($form);
		$form.submit();
		DocumentUtils.removeFromTop($form);
		
		
	},
	/**
	 * 關閉視窗，關閉 DialogUtils.window() 開啟後的視窗
	 * 參數:
	 * e = 固定放 window 系統變數
	 * data = 要回傳的資料，預設為 null，會觸發 DialogUtils.window() 內的 callback 參數
	 * 
	 */
	closeWindow(e, data) {
		let id = e.frameElement.id;
		let windowModal = DocumentUtils.bootstrap.Modal.getInstance(top.document.getElementById("modal-" + id));
		dialogWindowCallbacks.get(id)(data);
		windowModal.hide();
	}
}

/** 核心功能 */
var DocumentUtils = {
	appendToTop: (e) => {
		top.document.body.appendChild(e);
	},
	removeFromTop: (e) => {
		top.document.body.removeChild(e);
	},
	bootstrap: top.bootstrap,
	ready: (callback = () => { }) => {
		window.onload = callback;
	},
	isFunction: (functionToCheck) => {
		return functionToCheck && {}.toString.call(functionToCheck) === '[object Function]';
	},
	isNode: (o) => {
		return (
			typeof Node === "object" ? o instanceof Node :
				o && typeof o === "object" && typeof o.nodeType === "number" && typeof o.nodeName === "string"
		);
	},
	isElement: (o) => {
		return (
			typeof HTMLElement === "object" ? o instanceof HTMLElement : //DOM2
				o && typeof o === "object" && o !== null && o.nodeType === 1 && typeof o.nodeName === "string"
		);
	},
	setSideBarActived: (target) => {
		const $item = $(`[data-id=${target}]`, "#ap-side-bar");
		$item[0].classList.remove("text-white")
		$item[0].classList.add("active")
	}
}

/** 元件套件 */
var ElementUtils = top.ElementUtils || {
	screenMaskCount: 0,
	screenMaskElement: null,
	/** 顯示畫面遮擋 */
	showScreenMask: () => {
		if(ElementUtils.screenMaskElement === null){
			ElementUtils.screenMaskElement = ElementUtils.createElement(`
				<div>
					<div class="screen-mask"></div>
					<div class="spinner-border screen-mask-spanner"></div>
					<button class="btn-close screen-mask-close-btn"></button>
				</div>
			`)
			ElementUtils.screenMaskElement.getElementsByClassName("screen-mask-close-btn")[0].addEventListener("click", () => {
				ElementUtils.closeScreenMask(ElementUtils.screenMaskCount);
			})
		}
		if(ElementUtils.screenMaskCount <= 0){
			ElementUtils.screenMaskCount = 0;
			DocumentUtils.appendToTop(ElementUtils.screenMaskElement);
		}
		ElementUtils.screenMaskCount += 1;
	},
	/** 關閉畫面遮擋 */
	closeScreenMask: (count = 1) => {
		ElementUtils.screenMaskCount -= count;
		if(ElementUtils.screenMaskCount <= 0){
			ElementUtils.screenMaskCount = 0;
			DocumentUtils.removeFromTop(ElementUtils.screenMaskElement);
		}
	},
	/** 建立 DOM 物件 */
	createElement: (html) => {
		let template = document.createElement('template');
		html = html.trim();
		template.innerHTML = html;
		return template.content.firstChild;
	},
	/** 
	 * 建立 select option 物件
	 * 參數:
	 * select = HTML Select DOM 物件
	 * options = 選項陣列 [ { text:"文字", value:"數值" } ]
	 * selectedValue = 預設選擇的選項值
	 */
	selectOptions: (select, options = [], selectedValue) => {
		let buffer = []
		options.forEach(option => {
			let selected = option.value == selectedValue ? "selected" : "";
			buffer.push(`<option ${selected} value="${option.value}">${option.text}</option>`);
		});
		select.innerHTML = buffer.join("");
	},
	/**
	 * 渲染 Table
	 * 參數:
	 * table = HTML Table DOM 物件
	 * captions = 表格的標題，範例: [ { side:"top", text:"內容" }]
	 * data = 資料陣列
	 * thead = 欄位標頭設定，使用範例:
	 *         thead: [
	 *            { html: "顯示的文字", clazz: "使用的class", style: "套用的style", attrs: "額外的參數"} 
	 *         ]
	 * 
	 * tbody = 欄位內容設定，可以使用"@key"來直接mapping傳入data item的資料，使用範例:
	 *         tbody: [
	 *            { html: "@vendorName", clazz: "使用的class", style: "套用的style", attrs: "額外的參數"},
	 *            { html: "@vendorCode", clazz: "使用的class", style: "套用的style", attrs: "額外的參數"}
	 *         ]
	 * 
	 * theach = 當thead 的 th 渲染時的 callback，範例: theach: ($th, index) => {}
	 * treach = 當tbody 的 tr 渲染時的 callback，範例: treach: (item, $tr, index) = {}
	 */
	renderTable: (options = {}) => {
		let $table = options.table || ElementUtils.createElement("<table></table>");
		let captions = options.captions || [];
		let data = options.data || [];
		let thead = options.thead || [];
		let tbody = options.tbody || [];
		let theach = options.theach || (() => { });
		let treach = options.treach || (() => { });

		let captionsBuffer = []
		captions.forEach(item => {
			captionsBuffer.push(`<caption style="caption-side: ${item.side}">${item.text}</caption>`);
		});
		$table.innerHTML = captionsBuffer.join("");
		if(thead.custom){
			$table.appendChild(thead.element);
		}else{
			if(DocumentUtils.isFunction(theach)){
				let $thead = ElementUtils.createElement("<thead></thead>");
				let $tr = ElementUtils.createElement("<tr></tr>");
				$thead.appendChild($tr);
				thead.forEach((item, index) => {
					let _clazz = item.clazz ? `class="${item.clazz}"` : "";
					let _style = item.style ? `style="${item.style}"` : "";
					let _attrs = item.attrs ? item.attrs : "";
					let $th = ElementUtils.createElement(`<th ${_attrs} ${_clazz} ${_style}>${item.html}</th>`);
					theach($th, index);
					$tr.appendChild($th);
				});
				$table.appendChild($thead);
			} else {
				let theadBuffer = [];
				theadBuffer.push("<thead>");
				theadBuffer.push("<tr>");
				thead.forEach(item => {
					let _clazz = item.clazz ? `class="${item.clazz}"` : "";
					let _style = item.style ? `style="${item.style}"` : "";
					let _attrs = item.attrs ? item.attrs : "";
					theadBuffer.push(`<th ${_attrs} ${_clazz} ${_style}>${item.html}</th>`);
				});
				theadBuffer.push("</tr>");
				theadBuffer.push("</thead>");
				$table.insertAdjacentHTML('beforeend', theadBuffer.join(""));
			}
		}
		
		let $tbody = ElementUtils.createElement("<tbody></tbody>");
		if(!data || data.length === 0){
			let $tr = ElementUtils.createElement(`
				<tr>
					<td colspan="${tbody.length}">No Data</td>
				</tr>
			`);
			$tbody.appendChild($tr);
		}else{
			data.forEach((item, index) => {
				let $tr = ElementUtils.createElement("<tr></tr>");
				tbody.forEach((_item) => {
					let _clazz = _item.clazz ? `class="${_item.clazz}"` : "";
					let _style = _item.style ? `style="${_item.style}"` : "";
					let _attrs = _item.attrs ? _item.attrs : "";
					let $td = ElementUtils.createElement(TextUtils.tranPattern(`<td ${_attrs} ${_clazz} ${_style}></td>`, item));
					if (_item.html) {
						$td.innerHTML = TextUtils.tranPattern(_item.html, item);
					} else if (DocumentUtils.isFunction(_item.parse)) {
						let parsed = _item.parse(item);
						if (DocumentUtils.isElement(parsed) || DocumentUtils.isNode(parsed)) {
							$td.appendChild(parsed);
						} else {
							$td.innerHTML = parsed;
						}
					}
					$tr.appendChild($td);
				});
				treach(item, $tr, index);
				$tbody.appendChild($tr);
			});
		}
		$table.appendChild($tbody);
	},
	/**
	 * 產生 Table Pager 頁面元件
	 * 參數:
	 * div = HTML Div DOM 物件
	 * onEvent = 當換頁時的 callback，範例: onEvent: (pageNum, pageSize) = > {}
	 * pageNum = 設定第幾頁
	 * pageSize = 設定一頁幾筆
	 * totalPages = 設定最大頁數
	 * totalSize = 設定總筆數
	 */
	renderPager: (options = {}) => {
		let $div = options.div || ElementUtils.createElement("<div style='display: flex; justify-content: space-between;'></div>");
		let onEvent = options.onEvent || (() => { });
		let pageNum = options.pageNum || 0;
		let pageSize = options.pageSize || 10;
		let totalPages = options.totalPages || 0;
		let totalSize = options.totalSize || 0;

		$div.innerHTML = '';

		let $pagination = ElementUtils.createElement('<ul class="pagination"></ul>');

		let $firstBtn = ElementUtils.createElement(
			`
				<li class="page-item ${pageNum === 0 ? 'disabled' : 'clickable'}">
			      <a class="page-link none-select">
			        <span aria-hidden="true">&laquo</span>
			      </a>
			    </li>
			`
		);

		let $previousBtn = ElementUtils.createElement(
			`
				<li class="page-item ${pageNum === 0 ? 'disabled' : 'clickable'}">
			      <a class="page-link none-select">
			        <span aria-hidden="true">&lsaquo;</span>
			      </a>
			    </li>
			`
		);
		if (pageNum != 0) {
			$firstBtn.addEventListener('click', () => {
				onEvent(0, pageSize)
			});
			$previousBtn.addEventListener('click', () => {
				onEvent(pageNum - 1, pageSize)
			});
		}
		let $nextBtn = ElementUtils.createElement(
			`
				<li class="page-item ${pageNum + 1 >= totalPages ? 'disabled' : 'clickable'}">
			      <a class="page-link none-select">
			        <span aria-hidden="true">&rsaquo;</span>
			      </a>
			    </li>
			`
		);
		let $lastBtn = ElementUtils.createElement(
			`
				<li class="page-item ${pageNum + 1 >= totalPages ? 'disabled' : 'clickable'}">
			      <a class="page-link none-select">
			        <span aria-hidden="true">&raquo</span>
			      </a>
			    </li>
			`
		);
		if (pageNum + 1 < totalPages) {
			$nextBtn.addEventListener('click', () => {
				onEvent(pageNum + 1, pageSize)
			});
			$lastBtn.addEventListener('click', () => {
				onEvent(totalPages - 1, pageSize)
			});
		}

		let $pageBtns = [];

		const getPages = (totalPages, currentPage) => {
			if (totalPages <= 5) return Array.from(Array(totalPages).keys()).map(r => { return r + 1 });
			let diff = 0;
			const result = [currentPage - 2, currentPage - 1, currentPage, currentPage + 1, currentPage + 2];
			if (result[0] < 3) {
				diff = 1 - result[0];
			}
			if (result.slice(-1) > totalPages - 2) {
				diff = totalPages - result.slice(-1);
			}
			return result.map(r => { return r + diff });
		}

		getPages(totalPages, pageNum + 1).forEach(num => {
			let $pageBtn = ElementUtils.createElement(
				`
				<li class="page-item ${num === pageNum + 1 ? 'disabled' : 'clickable'}">
			      <a class="page-link none-select ${num === pageNum + 1 ? 'actived' : ''}">
			        <span aria-hidden="true">${num}</span>
			      </a>
			    </li>
			`
			);
			if (num !== pageNum + 1) {
				$pageBtn.addEventListener('click', () => {
					onEvent(num - 1, pageSize)
				});
			}
			$pageBtns.push($pageBtn);
		});

		$pagination.appendChild($firstBtn);
		$pagination.appendChild($previousBtn);
		$pageBtns.forEach($pageBtn => {
			$pagination.appendChild($pageBtn);
		});
		$pagination.appendChild($nextBtn);
		$pagination.appendChild($lastBtn);

		let startIndex = totalSize === 0 ? 0 : (pageNum * pageSize) + 1;
		let endIndex = (pageNum + 1) * pageSize > totalSize ? totalSize : (pageNum + 1) * pageSize;

		let $info = ElementUtils.createElement(
			`
			<span style="padding-top: 6px">
				第${pageNum + 1}頁 第${startIndex}-${endIndex}筆 共${totalSize}筆 
			<span>
			`
		);
    
		let $pageSizer = ElementUtils.createElement(
			`
				<span>
					每頁
					<input type="number" min="1" max="1000" style="height: 38px; width: 82px; text-align: center;" value="${pageSize}"/>
					筆
				</span>
			`
		);
		
		$pageSizer.getElementsByTagName("input")[0].addEventListener("change", (e) => {
			let val = e.target.value;
			if(val < 1 || val > 1000){
				e.target.value = pageSize;
				val = pageSize;
			}else{
				onEvent(0, val)
			}
		})

		$div.appendChild($pageSizer);
		$div.appendChild($info);
		$div.appendChild($pagination);

	},
	/**
	 * 建立可排序的 Table Header
	 * 參數:
	 * thead: 與 renderTable 的 thead 相同，只是多一個 sortKey 參數
	 * onEvent: 執行排序時的 Callback， 範例: onEvent: (key, dir) => {}
	 * sortKey: 當前的 sortKey
	 * sortDir: 當前的列序
	 * theach: 與 renderTable 的 theach 相同
	 */
	createSortableTableHeader: (options = {}) => {
		let thead = options.thead ? options.thead : [];
		let onEvent = options.onEvent ? options.onEvent : (() => { });
		let sortKey = options.sortKey ? options.sortKey : null;
		let sortDir = options.sortDir ? options.sortDir : null;
		let theach = options.theach || (() => { });
		let $thead = ElementUtils.createElement(`
			<thead>
				<tr></tr>			
			</thead>
		`);
		
		let $tr = $thead.getElementsByTagName("tr")[0];
		
		thead.forEach((item, index) => {
			let _clazz = item.clazz ? `class="${item.clazz}"` : "";
			let _style = item.style ? `style="${item.style}"` : "";
			let _attrs = item.attrs ? item.attrs : "";
			let _sortKey = item.sortKey ? item.sortKey : null;
			
			let $th;
			
			if(_sortKey){
				_clazz = item.clazz ? `class="${item.clazz} none-select clickable"` : "class='none-select clickable'";
				if(_sortKey === sortKey && sortDir){
					$th = ElementUtils.createElement(`
						<th ${_attrs} ${_clazz} ${_style}>
							${item.html}
							${sortDir === "desc" ? '<i class="bi bi-arrow-down-short"></i>' : '<i class="bi bi-arrow-up-short"></i>'}
						</th>
					`);
				}else{
					$th = ElementUtils.createElement(`
						<th ${_attrs} ${_clazz} ${_style}>
							${item.html}
						</th>
					`);
				}
				$th.addEventListener("click", () => {
					if(_sortKey === sortKey){
						if(sortDir === "desc"){
							onEvent("", "");
						}else if(sortDir === "asc"){
							onEvent(_sortKey, "desc");
						}else{
							onEvent(_sortKey, "asc");
						}
					}else{
						onEvent(_sortKey, "asc");
					}
				})
			}else{
				$th = ElementUtils.createElement(`
					<th ${_attrs} ${_clazz} ${_style}>
						${item.html}
					</th>
				`);
			}
			if(DocumentUtils.isFunction(theach)){
				theach($th, index);
			}
			$tr.appendChild($th);
		});
		
		
		return {
			custom: true,
			element: $thead
		}
	},
	iconButtonHtml: (options = {}) => {
		const attrs = options.attrs ? options.attrs : "";
		const color = options.color ? `btn-${options.color}` : "";
		const size = options.size ? `btn-${options.size}` : "";
		const icon = options.icon ? `bi bi-${options.icon}` : "";
		const click = options.click ? `onclick="${options.click}"` : "";
		const clazz = options.clazz ? options.clazz : "";
		const title = options.title ? options.title : "";

		return `
			<button title="${title}" ${attrs} type="button" class="btn ${color} ${size} ${clazz}" ${click}>
				<i class="${icon}"></i>
			</button>
		`;
	},
	cronExpressionComponent: (options = {}) => {
		let div = options.div || ElementUtils.createElement("<div></div>");
		let value = options.value || "0 0 0 ? * * * ";
		let id = options.id || "";
		let name = options.name || "";
		let attrs = options.attrs || "";

		let _id = id ? `id="${id}"` : "";
		let _name = name ? `name="${name}"` : "";
		let _attrs = attrs ? attrs : "";

		let $input = ElementUtils.createElement(`
			<input cron-exp-input ${_attrs} type="text" class="form-control text-center" ${_id} ${_name} readonly>
		`);

		let $button = ElementUtils.createElement(`
			<button cron-exp-btn class="btn btn-outline-secondary" type="button">
			  	<i class="bi bi-calendar3"></i>
			  </button>
		`);

		$input.value = value;

		$button.addEventListener("click", () => {
			DialogUtils.window({
				title: "Cron expression generator",
				url: "scheduler/cronExpGenerator",
				data: { cronExp: $input.value },
				callback: (retVal) => {
					if (retVal?.cronExp) {
						$input.value = retVal.cronExp;
					}
				}
			});
		});

		if (div.querySelectorAll("[cron-exp-input]").length) {
			div.querySelector("[cron-exp-input]").replaceWith($input);
		} else {
			div.appendChild($input);
		}

		if (div.querySelectorAll("[cron-exp-btn]").length) {
			div.querySelector("[cron-exp-btn]").replaceWith($button);
		} else {
			div.appendChild($button);
		}

	},
	/**
	 * 建立 Tabs
	 * 
	 * 參數: 
	 * div = HTML Div DOM 物件
	 * tabs = Tab 設定，使用範例:
	 * 			tabs : [
	 * 				{label: "", url: ""},
	 * 				{label: "", url: ""}
	 * 			]
	 * 
	 */
	createTabs: (options = []) => {
		const div = options.div || ElementUtils.createElement("<div></div>");
		const tabs = options.tabs || [];
		
		div.innerHTML = "";
		
		const tabUL = ElementUtils.createElement(`<ul class="nav nav-tabs ap-tab-ul"></ul>`)
		const tabContentDiv = ElementUtils.createElement(`<div class="tab-content border-start border-end border-bottom h-100"></div>`)
		
		const tabLabels = [];
		const tabPanes = [];

		const paneLoaded = [];
		
		tabs.forEach((item, index) => {
			const labelClasses = ["nav-link", "none-select", "clickable", "ap-tab-label"];
			const paneClasses = ["tab-pane", "h-100"];
			
			if(index === 0){
				labelClasses.push("active");
				paneClasses.push("active");
				paneLoaded.push(true)
			}else{
				paneLoaded.push(false)
			}
			
			tabLabels.push(ElementUtils.createElement(`
				<label class="${labelClasses.join(' ')}">
					${item.label}
				</label>
			`))
			
			tabPanes.push(ElementUtils.createElement(`
				<div class="${paneClasses.join(' ')}">
				</div>
			`))
			
			if(index === 0){
				tabPanes[0].appendChild(ElementUtils.createElement(`
					<iframe class="ap-tab-iframe" src="${item.url}"></iframe>
				`))
			}
			
		})
		
		tabLabels.forEach((item, index) => {
			tabUL.appendChild(item);
			item.addEventListener('click', () =>  {
				tabLabels.forEach(_item => { _item.classList.remove('active')})
				tabPanes.forEach(_item => { _item.classList.remove('active')})
				tabLabels[index].classList.add('active');
				tabPanes[index].classList.add('active');
				if(!paneLoaded[index]){
					tabPanes[index].appendChild(ElementUtils.createElement(`
						<iframe class="ap-tab-iframe" src="${tabs[index].url}"></iframe>
					`))
				}
				paneLoaded[index] = true;
			})
		})
		
		tabPanes.forEach(item => {
			tabContentDiv.appendChild(item);
		})
		
		div.appendChild(tabUL);
		div.appendChild(tabContentDiv);
	}
	
}
/** 跳出警示提醒 */
var PromptUtils = top.PromptUtils || {
	info: (msg) => {
		PromptUtils.prompt("alert-info", msg, 3000);
	},
	success: (msg) => {
		PromptUtils.prompt("alert-success", msg, 3000);
	},
	warning: (msg) => {
		PromptUtils.prompt("alert-warning", msg, 5000);
	},
	error: (msg) => {
		PromptUtils.prompt("alert-danger", msg, 10000);
	},
	prompt: (alertClass = "", msg = "", interval = 3000) => {

		let html = ` 
			<div class="alert ${alertClass} prompt" role="alert" style="z-index: 2080">
			  ${msg}
			</div> 
		`;

		let e = ElementUtils.createElement(html);

		DocumentUtils.appendToTop(e);

		let timeout = TimeoutUtils.setTimeout(function() {
			DocumentUtils.removeFromTop(e);
		}, interval);

		e.addEventListener('click', () => {
			DocumentUtils.removeFromTop(e);
			TimeoutUtils.clearTimeout(timeout);
		});
	}
}
/** 核心套件，文字套件 */
var TextUtils = top.TextUtils || {
	randonString: (length = 0) => {
		let result = [];
		let characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
		let charactersLength = characters.length;
		for (let i = 0; i < length; i++) {
			result.push(characters.charAt(Math.floor(Math.random() * charactersLength)));
		}
		return result.join('');
	},
	tranPattern: (text, item) => {
		let key;
		Object.keys(item).forEach(function(k) {
			key = "@{" + k + "}";
			while (text.includes(key)) {
				if (item[k] || item[k] == 0) {
					text = text.replace(key, item[k]);
				} else {
					text = text.replace(key, "");
				}
			}
			key = "@{" + k + ":date}";
			while (text.includes(key)) {
				if (item[k]) {
					let date = new Date(item[k]);
					let formattedDate = `${date.getFullYear()}/${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`;
					text = text.replace(key, formattedDate);
				} else {
					text = text.replace(key, "");
				}
			}
		});
		return text;
	},
	objectToQuerystring: (obj = {}) => {
		return Object.keys(obj).filter((key) => obj[key] != undefined && obj[key] != '').reduce((str, key, i) => {
			let delimiter, val;
			delimiter = (i === 0) ? '?' : '&';
			if (Array.isArray(obj[key])) {
				key = encodeURIComponent(key);
				let arrayVar = obj[key].reduce((str, item) => {
					if (typeof item == "object") {
						val = encodeURIComponent(JSON.stringify(item));
					} else {
						val = encodeURIComponent(item);
					}
					return [str, key, '=', val, '&'].join('');
				}, '');
				return [str, delimiter, arrayVar.trimRightString('&')].join('');
			} else {
				key = encodeURIComponent(key);
				if (typeof obj[key] == "object") {
					val = encodeURIComponent(JSON.stringify(obj[key]));
				} else {
					val = encodeURIComponent(obj[key]);
				}
				return [str, delimiter, key, '=', val].join('');
			}
		}, '');
	},
	fillHead: (text = " ", appender = " ", length = 0) => {
		let _text = "" + text;
		let _appender = "" + appender;
		while (_text.length < length) {
			_text = _appender + _text;
		}
		return _text;
	}
}

/** HTTP Request 套件 */
var HttpUtils = top.HttpUtils || {
	doPost: (options = {}) => {
		let url = options.url || "";
		let data = options.data || {};
		let success = options.success || function() { };
		const defaultError = function() { }
		let error = options.error || defaultError;
		let exception = options.exception || function() { };
		
		ElementUtils.showScreenMask();

		fetch(url, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"Accept": "application/json"
			},
			body: JSON.stringify(data)
		}).then((response) => {
			if (response.ok) {
				response.json().then((responseData) => {
					if (responseData?.succeeded == true) {
						success(responseData);
					} else if (responseData?.succeeded == false) {
						if(defaultError === error){
							PromptUtils.error(responseData.message);							
						}
						error(responseData);
					}

				});
			} else {
				response.json().then((responseData) => {
					PromptUtils.error(responseData.message);
					error(responseData, response);
				});
			}
		}).catch((e, status) => {
			PromptUtils.error(`
				<span>Message：</span><br>
				<span class="ms-3">${e.message}</span><br>
				<span>Stack：</span><br>
				<p class="ms-3">${e.stack.replace("\n", "<br>")}</p>
				
			`);
			exception(e, status);
		}).finally(() => {
			ElementUtils.closeScreenMask();
		})
	},
	doDownload: (options = {}) => {
		let url = options.url || "";
		let data = options.data || {};
		let urlQueryString = url + TextUtils.objectToQuerystring(data);
		let $a = document.createElement("a");
		$a.setAttribute("href", urlQueryString);
		$a.setAttribute("download", "");
		$a.click();
	}
}

var ArrayUtils = top.ArrayUtils || {
	partition: (list, num) => {
		if(!list){
			return [];
		}
		const parts = [];
		let part = []
		for(let i = 0 ; i < list.length ; i++){
			if(i % num == 0 && i > 0){
				parts.push(Array.from(part));
				part = [];						
			}
			part.push(list[i]);			
		}
		if(part.length > 0){
			parts.push(Array.from(part));
		}
		return parts;
	}
}

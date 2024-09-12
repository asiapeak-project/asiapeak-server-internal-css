package com.asiapeak.server.internal.css.core.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum UserAuthEnum {
	ROLE_CUSTOMER_FUNCTION("客戶資訊", 0, "功能啟用"), //
	ROLE_CUSTOMER_CREATE("客戶資訊", 1, "新增客戶"), //

	ROLE_CUSTOMER_INFO_EDIT("客戶資訊", 2, "編輯客戶"), //
	ROLE_CUSTOMER_INFO_DELETE("客戶資訊", 3, "刪除客戶"), //

	ROLE_CUSTOMER_CONTACT_CREATE("客戶資訊", 4, "聯絡人資訊-新增"), //
	ROLE_CUSTOMER_CONTACT_EDIT("客戶資訊", 5, "聯絡人資訊-編輯"), //
	ROLE_CUSTOMER_CONTACT_DELETE("客戶資訊", 6, "聯絡人資訊-刪除"), //

	ROLE_CUSTOMER_PRODUCT_CREATE("客戶資訊", 7, "產品資訊-新增"), //
	ROLE_CUSTOMER_PRODUCT_EDIT("客戶資訊", 8, "產品資訊-編輯"), //
	ROLE_CUSTOMER_PRODUCT_DELETE("客戶資訊", 9, "產品資訊-刪除"), //

	ROLE_CUSTOMER_DEPLOYMENT_CREATE("客戶資訊", 10, "環境佈屬-新增"), //
	ROLE_CUSTOMER_DEPLOYMENT_EDIT("客戶資訊", 11, "環境佈屬-編輯"), //
	ROLE_CUSTOMER_DEPLOYMENT_DELETE("客戶資訊", 12, "環境佈屬-刪除"), //

	ROLE_CUSTOMER_DOCUMENT_CREATE("客戶資訊", 13, "文件文檔-新增"), //
	ROLE_CUSTOMER_DOCUMENT_EDIT("客戶資訊", 14, "文件文檔-編輯"), //
	ROLE_CUSTOMER_DOCUMENT_DELETE("客戶資訊", 15, "文件文檔-刪除"), //

	ROLE_CUSTOMER_SERVICERECORD_CREATE("客戶資訊", 16, "服務紀錄-新增"), //
	ROLE_CUSTOMER_SERVICERECORD_EDIT("客戶資訊", 17, "服務紀錄-編輯"), //
	ROLE_CUSTOMER_SERVICERECORD_DELETE("客戶資訊", 18, "服務紀錄-刪除"), //

	ROLE_CUSTOMER_SERVICEHANDLE_CREATE("客戶資訊", 19, "服務處理-新增"), //
	ROLE_CUSTOMER_SERVICEHANDLE_EDIT("客戶資訊", 20, "服務處理-編輯"), //
	ROLE_CUSTOMER_SERVICEHANDLE_DELETE("客戶資訊", 21, "服務處理-刪除"), //

	ROLE_UNSOLVED_FUNCTION("未解決案件", 0, "功能啟用"), //

	ROLE_MAINTENANCE_FUNCTION("保養維護", 0, "功能啟用"), //

	ROLE_KNOWLEDGE_FUNCTION("知識庫", 0, "功能啟用"), //

	ROLE_SEARCH_FUNCTION("進階搜尋", 0, "功能啟用"), //

	ROLE_VM_FUNCTION("虛擬機資訊", 0, "功能啟用"), //

	ROLE_SOFTWARE_FUNCTION("軟體及工具", 0, "功能啟用"), //

	ROLE_USERMGR_FUNCTION("使用者管理", 0, "功能啟用"); //

	public static final List<String> CATEGORY_ORDER = Collections.unmodifiableList(Arrays.asList(new String[] { //
			"客戶資訊", //
			"未解決案件", //
			"保養維護", //
			"知識庫", //
			"進階搜尋", //
			"虛擬機資訊", //
			"軟體及工具", //
			"使用者管理", //
	}));

	String category;
	Integer seq;
	String desc;

	UserAuthEnum(String category, Integer seq, String desc) {
		this.category = category;
		this.seq = seq;
		this.desc = desc;
	}

	public String getCategory() {

		return category;
	}

	public Integer getSeq() {
		return seq;
	}

	public String getDesc() {
		return desc;
	}

}
